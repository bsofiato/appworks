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

package br.com.appworks.runtime.lang.support.template.groovy;

import br.com.appworks.runtime.lang.support.expression.Expression;
import br.com.appworks.runtime.lang.support.expression.ExpressionCompilationException;
import br.com.appworks.runtime.lang.support.expression.ExpressionEvaluationException;
import br.com.appworks.runtime.lang.support.expression.groovy.GroovyExpression;
import br.com.appworks.runtime.lang.support.template.Template;
import br.com.appworks.runtime.lang.support.template.TemplateCompilationException;
import br.com.appworks.runtime.lang.support.template.TemplateProcessingException;
import java.util.Map;

/**
 * <p>Groovy template.</p>
 *
 * <p>This template implementation uses the groovy expression facility to 
 * merge the supplied data set with a predefined pattern.</p>
 * 
 * <p>Prior to the groovy expression construction, the template source code is
 * processed to make use of a groovy language feature named <tt>GString</tt> 
 * (The <tt>GString</tt> provide template processing capabilities to the groovy 
 * language).</p>
 *
 * @see br.com.appworks.runtime.lang.support.expression.groovy.GroovyExpression
 * @author Bruno Sofiato
 */

public class GroovyTemplate implements Template {

  /**
   * <p>Groovy expression encapsulating the template merging semantics.</p>
   *
   * <p>This groovy expression encapsulates the template merging semantics on a
   * groovy's <tt>GString</tt>.</p>
   */
  
  private final Expression expression;
  
  /**
   * <p>Gets the associated groovy expression encapsulating the template merging
   * semantics.</p>
   *
   * @return Associated groovy expression encapsulating the template merging 
   *         semantics
   */
  
  private Expression getExpression() {
    return expression;
  }
  
  /**
   * <p>Constructs a new groovy template.</p>
   *
   * <p>On the groovy template creation process, the supplied template source 
   * code is processed to encapsulate the template merging semantics on a 
   * groovy's <tt>GString</tt>.</p>
   *
   * @param  sourceCode Template source code
   * @throws TemplateCompilationException If there's an error on the associated 
   *                                      groovy expression compilation
   */
  public GroovyTemplate(final String sourceCode) throws TemplateCompilationException {
    try {
      this.expression = new GroovyExpression('"' + sourceCode + '"');
    } catch (ExpressionCompilationException ex) {
      throw new TemplateCompilationException(ex);
    }
  }
  
  /**
   * <p>Constructs a new groovy template.</p>
   * 
   * @param expression Associated groovy expression encapsulating the template 
   *                   merging semantics
   */
  
  public GroovyTemplate(final Expression expression) {
    this.expression = expression;
  }
  
  /**
   * <p>Merges the template against the supplied data.</p>
   *
   * <p>This method delegates the template merging process to the associated 
   * groovy expression, which encapsulate the template merging semantics.</p>
   *
   * @param  context Supplied processing context
   * @return Template evaluated value
   * @throws TemplateProcessingException If there's any error on the associated
   *                                     groovy expression evaluation process
   */
  
  public String process(final Map <String, Object> context) throws TemplateProcessingException {
    try {
      return getExpression().evaluate(context);
    } catch (ExpressionEvaluationException ex) {
      throw new TemplateProcessingException(ex);
    }
  }

  /**
   * <p>Gets the template's source code.</p>
   *
   * <p>This method delegates the source code's fetching process to the 
   * associated groovy expression and process the obtained source code to 
   * compensate the changes made on it to wrap the merging semantics on a 
   * groovy's <tt>GString</tt>.</p>
   *
   * @return Template's source code
   */
  
  public String getSourceCode() {
    String sourceCode = getExpression().getSourceCode();
    return sourceCode.substring(1, sourceCode.length() - 1);
  }
}
