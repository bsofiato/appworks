/**
 * The contents of this file are subject to the terms of the Common Development 
 * and Distribution License, Version 1.0 only (the "License"). You may not use 
 * this file except in compliance with the License.
 *
 * You can obtain a copy of the license at LICENSE.html or 
 * http://www.sun.com/cddl/cddl.html. See the License for the specific language 
 * governing permissions and limitations under the License.
 *
 * When distributing Covered Code, include this disclaimer in each file and 
 * include the License file at LICENSE.html. If applicable, add the following 
 * below this disclamer, with the fields enclosed by brackets "[]" replaced with 
 * your own identifying information: 
 * 
 * Portions Copyright [yyyy] [name of copyright owner]
 *
 * Copyright 2005 AppWorks, Inc.  All rights reserved.
 * Use is subject to license terms.
 */

package br.com.appworks.runtime.lang.support.expression.groovy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * <p>Translates an groovy expression and evaluation context to use with the 
 * translated groovy expression.</p>
 *
 * <p>The groovy expression translation must be executed to change keywords 
 * like <tt>this</tt> and <tt>return</tt> to regular identifiers (allowing the 
 * user to use expressions like <code>this==return</code>). These keyworkds 
 * surrogates are stored on the ContextTranslator instance, and are used to 
 * translate the evaluation context to use theses surrogates.</p>
 *
 * <p>Also in the groovy expression translation, all non scoped method 
 * invocation (invocations like <code>getClass()</code>) are scoped to the 
 * <tt>this</tt> instance. Preventing semantics conflicts between the associated
 * bean and the class implementing the groovy expression.</p>
 *
 * @author Bruno Sofiato
 */
public class ContextTranslator {
  
  /**
   * <p>Defines the possible code parsing sections.</p>
   * 
   * <p>The code parsing sections are used by the context translator to keep 
   * track of the current source code's region been parsed.</p>
   *
   * @author Bruno Sofiato
   */
  
  private static enum CodeSection {
    
    /**
     * <p>Represents a code section.</p>
     */
    
    CODE(false), 

    /**
     * <p>Represents a groovy's <tt>GString</tt> expression section.</p>
     */

    GSTRING_EXPRESSION(false), 
    
    /**
     * <p>Represents a string literal section.</p>
     */
    
    STRING(true);
    
    /**
     * <p>Indicates if the code parsing section corresponds to an groovy's 
     * language literal value.</p>
     */

    private final boolean literal;
    
    /**
     * <p>Given a token, calculates the next section from a <tt>CODE</tt> 
     * section.</p>
     *
     * <p>If the supplied token is a quotation mark (<tt>"</tt>), then the next 
     * code section is a <tt>STRING</tt> section, otherwise, the next code 
     * region is a <tt>CODE</tt> section.</p>
     *
     * @param  token Supplied token
     * @return Next code section (<tt>STRING</tt> if the supplied token is a 
     *         quotation mark, <tt>CODE</tt> otherwise)
     */
    
    private static CodeSection getCodeNextSection(final String token) {
      if (token.endsWith("\"")) {
        return STRING;
      } 
      return CODE;
    }

    /**
     * <p>Given a token, calculates the next section from a <tt>STRING</tt> 
     * section.</p>
     *
     * <p>If the supplied token is a quotation mark (<tt>"</tt>), then the next 
     * code section is a <tt>CODE</tt> section, instead, if the supplied token 
     * is a dollar sign followed by a opening brace (<tt>${</tt>), then the next 
     * code regin is a <tt>GSTRING_EXPRESSION</tt>, otherwise, the next code 
     * region is a <tt>STRING</tt> section.</p>
     *
     * @param  token Supplied token
     * @return Next code section (<tt>STRING</tt> if the supplied token is a 
     *         quotation mark, <tt>GSTRING_EXPRESSION</tt> if it's a dolar sign
     *         followed by a opening brace, or <tt>CODE</tt> otherwise)
     */

    private static CodeSection getStringNextSection(final String token) {
      if (token.endsWith("\"")) {
        return CODE;
      } else if (token.endsWith("${")) {
        return GSTRING_EXPRESSION;
      } 
      return STRING;
    }

    /**
     * <p>Given a token, calculates the next section from a <tt>GSTRING_EXPRESSION</tt> 
     * section.</p>
     *
     * <p>If the supplied token is a closing brace (<tt>}</tt>), then the next 
     * code section is a <tt>STRING</tt> section, otherwise, the next code 
     * region is a <tt>GSTRING_EXPRESSION</tt> section.</p>
     *
     * @param  token Supplied token
     * @return Next code section (<tt>STRING</tt> if the supplied token is a 
     *         closing brace, <tt>GSTRING_EXPRESSION</tt> otherwise)
     */

    private static CodeSection getGStringExpressionNextSection(final String token) {
      if (token.endsWith("}")) {
        return STRING;
      } 
      return GSTRING_EXPRESSION;
    }
    
    /**
     * <p>Constructs a new code parsing section.</p>
     *
     * @param literal Flag indicating if the new code parsing section is a 
     *                groovy language literal
     */

    private CodeSection(final boolean literal) {
      this.literal = literal;
    }
    
    /**
     * <p>Given a token, calculates the next section.</p>
     *
     * <p>This method will delegate, to the helper method associated with the 
     * current code section, the next section calculation process.</p>
     *
     * @param  token Supplied token
     * @return Next code section
     */

    public CodeSection getNextSection(final String token) {
      switch (this) {
        case CODE : 
          return getCodeNextSection(token);
        case STRING : 
          return getStringNextSection(token);
        default : 
          return getGStringExpressionNextSection(token);
      }
    }
    
    /**
     * <p>Verify if the section correspond to a groovy language literal.</p>
     *
     * @return <tt>true</tt> if the section correspond to a groovy language 
     *         literal, <tt>false</tt> otherwise
     */
    
    public boolean isLiteral() {
      return literal;
    }
  }
  
  /**
   * <p>The <tt>this</tt> keyword.</p>
   */
  private static final String THIS_IDENTIFIER = "this";

  /**
   * <p>The <tt>return</tt> keyword.</p>
   */

  private static final String RETURN_IDENTIFIER = "return";
  
  /**
   * <p>Tokens used on the groovy expression pre translation process.</p>
   */
 
  private static final String PRE_TRANSLATION_TOKENS = "()\"[];+-/*%.><={}! ";

  /**
   * <p>Tokens used on the groovy expression translation process.</p>
   */

  private static final String TRANSLATION_TOKENS = PRE_TRANSLATION_TOKENS;
  
  /**
   * <p>The <tt>this</tt> keyword surrogate identifier.</p>
   */

  private final String thisTranslatedIdentifier;
 
  /**
   * <p>The <tt>return</tt> keyword surrogate identifier.</p>
   */

  private final String returnTranslatedIdentifier;

  /**
   * <p>Gets the <tt>this</tt> keyword surrogate identifier.<p>
   *
   * @return The <tt>this</tt> keyword surrogate identifier
   */
  
  private String getThisTranslatedIdentifier() {
    return thisTranslatedIdentifier;
  }

  /**
   * <p>Gets the <tt>return</tt> keyword surrogate identifier.</p>
   *
   * @return The <tt>return</tt> keyword surrogate identifier
   */
  
  private String getReturnTranslatedIdentifier() {
    return returnTranslatedIdentifier;
  }

  /**
   * <p>Translates an element from a expression evaluation context.</p>
   *
   * <p>If there's an element with the old identifier on the supplied evaluation
   * context, the same element will be added with the new identifier to the 
   * context.</p>
   *
   * @param translatedContext Translated context to be processed
   * @param oldIdentifier Old context element identifier
   * @param newIdentifier New context element identifier
   */
  
  private void translate(final Map <String, Object> translatedContext, 
                         final String oldIdentifier, 
                         final String newIdentifier) {
    if (translatedContext.containsKey(oldIdentifier)) {
      translatedContext.put(newIdentifier, translatedContext.get(oldIdentifier));
    }
  }
  
  /**
   * <p>Pre translate an groovy expression.</p>
   * 
   * <p>On the pre translation process, all non scoped method invocation 
   * (invocations like <code>getClass()</code>) are scoped to the <tt>this</tt>
   * instance. Preventing semantics conflicts between the associated bean and 
   * the class implementing the groovy expression.</p>
   *
   * <p>Obs : </p>
   *
   * <blockquote>
   *   All the pre translation operations described above are executed 
   *   <tt>only</tt> against tokens found on code sections that are <tt>not</tt>
   *   groovy language literal.
   * </blockquote>
   *
   * @param  expression Expression to be pre translated
   * @return Pre translated expression
   */
  
  private String preTranslate(final String expression) {
    CodeSection status = CodeSection.CODE;
    StringTokenizer tokenizer = new StringTokenizer(expression, PRE_TRANSLATION_TOKENS, true);
    StringBuilder buffer = new StringBuilder();
    StringBuilder newExpression = new StringBuilder();
    while (tokenizer.hasMoreTokens()) {
      String nextToken = tokenizer.nextToken();
      buffer.append(nextToken);
      CodeSection nextStatus = status.getNextSection(buffer.toString());
      if ((nextToken.length() == 1) && 
          (PRE_TRANSLATION_TOKENS.contains(nextToken))) {
        if (!status.isLiteral()) {
          if ("(".equals(nextToken)) { 
            if (!((newExpression.toString().endsWith(".")) ||
                  (newExpression.toString().endsWith("->")))) {
              buffer.insert(0, "this.");
            }
          }
        }
        newExpression.append(buffer);
        buffer = new StringBuilder();
      }
      status = nextStatus;
    }
    newExpression.append(buffer);
    return newExpression.toString();
  }

  /**
   * <p>Constructs a new groovy expression context translator.</p>
   * 
   * <p>On the groovy expression context translator constructor process, the 
   * <tt>this</tt> keyword surrogate and the <tt>return</tt> keyword surrogate 
   * associated with the new instance are created.</p>
   */
  public ContextTranslator() {
    this.thisTranslatedIdentifier = THIS_IDENTIFIER + System.currentTimeMillis();
    this.returnTranslatedIdentifier = RETURN_IDENTIFIER + System.currentTimeMillis();
  }
  
  /**
   * <p>Translates a expression evaluation context.</p>
   *
   * <p>On the expression evaluation context translation process, the element
   * associated <tt>this</tt> keyword and the element associated with the 
   * <tt>return</tt> keyword, if there's one, are associated with the 
   * <tt>this</tt> surrogate identifier and the <tt>return</tt> surrogate 
   * identifier.</p>
   *
   * <p>Obs : </p>
   *
   * <blockquote>
   *   All the translation operations described above are executed <tt>only</tt> 
   *   against tokens found on code sections that are <tt>not</tt> groovy
   *   language literal.
   * </blockquote>
   *
   * @param  context Expression evaluation context
   * @return Translated expression evaluation context
   */
  
  public Map <String, Object> translate(final Map <String, Object> context) {
    if ((context != null) && (!context.isEmpty())) {
      Map <String, Object> translatedContext = new HashMap(context);
      translate(translatedContext, THIS_IDENTIFIER, getThisTranslatedIdentifier());
      translate(translatedContext, RETURN_IDENTIFIER, getReturnTranslatedIdentifier());
      return translatedContext;
    }
    return Collections.EMPTY_MAP;
  }
  
  /**
   * <p>Translates a expression's source code.</p>
   *
   * <p>On the expression's source code translation process, all non scoped 
   * method are scoped to the <tt>this</tt> instance, and all references to the
   * <tt>this</tt> keyword and the <tt>return</tt> keywords are changed to 
   * references of their surrogates identifiers.</p>
   * 
   * @return Translated expression source code
   * @param  expression Expression source code
   */
  public String translate(final String expression) {
    CodeSection status = CodeSection.CODE;
    String translatedExpression = null;
    if (expression != null) {
      translatedExpression = preTranslate(expression);
      StringBuilder builder = new StringBuilder();
      StringTokenizer tokenizer = new StringTokenizer(translatedExpression, TRANSLATION_TOKENS, true);
      while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken();
        if (!status.isLiteral()) {
          if (THIS_IDENTIFIER.equals(token)) {
            token = getThisTranslatedIdentifier();
          } else if (RETURN_IDENTIFIER.equals(token)) {
            token = getReturnTranslatedIdentifier();
          }
        }
        builder.append(token);
        status = status.getNextSection(builder.toString());
      }
      translatedExpression = builder.toString(); 
    }
    return translatedExpression;
  }
  
}
