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

import br.com.appworks.runtime.lang.support.expression.Expression;
import br.com.appworks.runtime.lang.support.expression.ExpressionCompilationException;
import br.com.appworks.runtime.lang.support.expression.ExpressionEvaluationException;
import groovy.lang.GString;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.util.Map;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 * <p>Groovy expression.</p>
 *
 * <p>The groovy expression encapulates all processing logic to compile and 
 * execute expressions using the groovy expression evaluation environment, 
 * shielding the client code using a expression from the groovy API.</p>
 *
 * <p>The supplied expression is compiled on the construction phase and the 
 * result script is cached, to maximize runtime performance. Also, prior to the
 * expression compilation, the expression is translated by the associated 
 * context translator to allow the use of reserved keywords such as the 
 * <tt>this</tt> and <tt>return</tt> keyword on the expression code.</p> 
 *
 * <p>Prior to evaluation, the supplied evaluation context is translate by the 
 * associated context translator to cope the expression source code changes 
 * introduced by the source code translation process.</p>
 * 
 * @author Bruno Sofiato
 */

public class GroovyExpression implements Expression {
  
  /**
   * <p>Original expression's source code.</p>
   */

  private final String sourceCode;

  /**
   * <p>Associated context translator.</p>
   */
  
  private final ContextTranslator contextTranslator = new ContextTranslator();
  
  /**
   * <p>Compiled expression.</p>
   */
  private final Script script; 
  
  /**
   * <p>Gets the compiled expression.</p>
   *
   * @return Compiled expression
   */
  
  private Script getScript() {
    return script;
  }
  
  /**
   * <p>Gets the associated context translator.</p>
   *
   * @return Associated context translator
   */
  
  private ContextTranslator getContextTranslator() {
    return contextTranslator;
  }
 
  /**
   * <p>Constructs a new groovy expression.</p>
   *
   * <p>On the groovy expression construction process, the supplied expression 
   * source code is compiled and the compilation result cached for futher use. 
   * If there's an syntax error or another compilation error, a <tt>ExpressionCompilationException</tt> 
   * will be throw.</p>
   *
   * @param  sourceCode Expression source code
   * @throws ExpressionCompilationException If there's an syntax error or 
   *                                        another compilation error on the 
   *                                        supplied source code
   */
  
  public GroovyExpression(final String sourceCode) throws ExpressionCompilationException {
    try {
      this.sourceCode = sourceCode;
      this.script = new GroovyShell(new ThreadLocalExpressionBinding()).parse(getContextTranslator().translate(sourceCode));
    } catch (CompilationFailedException ex) {
      throw new ExpressionCompilationException(ex);
    }
  }
  
  /**
   * <p>Evaluates the expression.</p>
   *
   * <p>On the expression evaluation process, the supplied evaluation context 
   * is translated by the associated context translator to cope the expression 
   * source code changes. If there's any error on the evaluation process (An 
   * uncaught exception thrown on the expression source code is considered an
   * error) a <tt>ExpressionEvaluationException</tt> will be throw.</p>
   *
   * @param  context Supplied evaluation context
   * @param  <Type> Expression evaluation result type
   * @return Expression's evaluated value
   * @throws ExpressionEvaluationException If there's any error on the 
   *                                       expression evaluation process
   */
  public <Type> Type evaluate(final Map <String, Object> context) throws ExpressionEvaluationException {
    try {
      ThreadLocalExpressionBinding expressionBinding = (ThreadLocalExpressionBinding) (getScript().getBinding());
      expressionBinding.setVariables(getContextTranslator().translate(context));
      Object evaluationResult = getScript().run();
      return (Type) ((evaluationResult instanceof GString) ? evaluationResult.toString() : evaluationResult);
    } catch (GroovyRuntimeException ex) {
      throw new ExpressionEvaluationException(ex);
    }
  }
  
  /**
   * <p>Gets the original expression's source code.</p>
   *
   * @return Original expression's source code
   */
  
  public String getSourceCode() {
    return sourceCode;
  }
}
