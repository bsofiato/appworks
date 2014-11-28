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
import br.com.appworks.runtime.lang.support.expression.ExpressionFactory;

/**
 * <p>Groovy expression factory.</p>
 *
 * <p>This factory creates expressions that will be evaluated using the groovy
 * expression evaluation environment, it's encapsulates all processing logic to 
 * create the groovy expression, shielding the client code requesting the 
 * expression creation from the groovy API.</p>
 *
 * @author Bruno Sofiato
 * @see br.com.appworks.runtime.lang.support.expression.groovy.GroovyExpression
 */

public class GroovyExpressionFactory implements ExpressionFactory {
  
  /**
   * <p>Creates a new groovy expression.</p>
   *
   * <p>If the supplied source code isn't <tt>null</tt>, the groovy expression 
   * creation is carried over by the <tt>GroovyExpression</tt> class. (for 
   * more info on the groovy expression creation context, consult the 
   * <tt>GroovyExpression</tt> documentation)</p>
   *
   * @param  sourceCode Groovy expression's source code
   * @return Groovy expression reflecting the supplied source code
   * @throws ExpressionCompilationException If there's an error on the groovy 
   *                                        expression creation process, or the 
   *                                        supplied source code is <tt>null</tt>
   */
  
  public Expression create(final String sourceCode) throws ExpressionCompilationException {
    if (sourceCode != null) {
      return new GroovyExpression(sourceCode);
    } 
    throw new ExpressionCompilationException();
  }
}
