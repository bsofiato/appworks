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

package br.com.appworks.runtime.lang.support.cloning;

/**
 * <p>Defines the <tt>clone()</tt> method signature contract for the cloneable 
 * classes.</p>
 *
 * <p>This interface extends the <tt>java.lang.Cloneable</tt> to ensure the 
 * <tt>clone()</tt> method implementation.</p>
 *
 * @param <Type> Cloneable type 
 * @author Bruno Sofiato
 */

public interface Cloneable <Type> extends java.lang.Cloneable {
  
  /**
   * <p>Creates and returns a copy of this object.</p>
   * 
   * @return  A clone of this instance
   * @throws  CloneNotSupportedException If any error occurs in the clonining 
   *                                     process
   */
  
  Type clone() throws CloneNotSupportedException;
}
