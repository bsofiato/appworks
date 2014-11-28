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

package br.com.appworks.runtime.lang.support.expression;

import java.util.Map;

/**
 * <p>Encapsulated evaluable expression.</p>
 *
 * <p>An expression encapsulate all the processing logic to evaluate an script
 * expression defined by the user. It provide a common interface to evaluate 
 * expression regardless of it's evaluation environment implementation.</p>
 *
 * @author Bruno Sofiato
 */

public interface Expression {
  
  /**
   * <p>Evaluates the expression.</p>
   *
   * <p>On the expression evaluation process, the supplied evaluation context 
   * is processed by the encapsulated evaluation process, returning to the 
   * client code the result value from the expression evaluation. If there's any
   * error on the evaluation process, a <tt>ExpressionEvaluationException</tt> 
   * will be throw.</p>
   *
   * @param  context Supplied evaluation context
   * @param  <Type> Expression evaluation result type
   * @return Expression's evaluated value
   * @throws ExpressionEvaluationException If there's any error on the 
   *                                       expression evaluation process
   */

  <Type> Type evaluate(Map <String, Object> context) throws ExpressionEvaluationException;
  
  /**
   * <p>Gets the expression's source code.</p>
   *
   * @return Expression's source code
   */

  String getSourceCode();
}
