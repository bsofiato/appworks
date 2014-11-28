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
 * <p>Expression exception.</p>
 *
 * <p>This class is the root class of the expression exception hierarchy, it 
 * provides a base type to be used by the client code for dealing with all 
 * expression related errors. All expression exceptions specializations must 
 * have this class as super class.</p>
 *
 * @author Bruno Sofiato
 */
public class ExpressionException extends Exception {
  
 /**
  * <p>Constructs a new expression exception.</p>
  */
  
  public ExpressionException() {
    super();
  }
  
  /**
   * <p>Constructs a new expression exception.</p>
   *
   * @param cause Original error cause
   */
  
  public ExpressionException(final Throwable cause) {
    super(cause);
  }
}
