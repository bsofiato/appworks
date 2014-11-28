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

/**
 * <p>Expression evaluation exception.</p>
 *
 * <p>The expression evaluation exception provides information from an error 
 * that occurred on the expression evaluation process execution. The expression 
 * evaluation exception shields the client code from dealing with implementation
 * specific exceptions from the expression evaluation environment been used.</p>
 *
 * @author Bruno Sofiato
 */
public class ExpressionEvaluationException extends ExpressionException {
  
 /**
  * <p>Constructs a new expression evaluation exception.</p>
  */
  
  public ExpressionEvaluationException() {
    super();
  }
  
  /**
   * <p>Constructs a new expression evaluation exception.</p>
   *
   * @param cause Original evaluation error cause
   */

  public ExpressionEvaluationException(final Throwable cause) {
    super(cause);
  }
}