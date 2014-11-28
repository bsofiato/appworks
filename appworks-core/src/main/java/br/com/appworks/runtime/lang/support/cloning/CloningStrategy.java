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
 * <p>Cloning strategy.</p>
 *
 * <p>The cloning strategy encapsulate all required task or processes to create
 * an copy from a supplied object. This facilitates code reuse and minimizes 
 * code complexity.</p>
 *
 * @param <Type> Type associated with this cloning strategy
 * @author Bruno Sofiato
 */
public interface CloningStrategy <Type extends Object> extends Cloneable {

  /**
   * <p>Clones an supplied instance.</p>
   *
   * <p>If the source object is <tt>null</tt>, the return copy will be 
   * <tt>null</tt> also.</p>
   *
   * @param  source Object to be cloned
   * @return A copy from the supplied instance
   * @throws CloneNotSupportedException If any error ocurr on the cloning 
   *                                    process
   */
  
  Type clone(Type source) throws CloneNotSupportedException;
  
  /**
   * <p>Clones a cloning strategy.</p>
   *
   * <p>A cloning strategy instance may be clone if it's used as an
   * high specialized cloning strategy mapped to an type.</p>
   *
   * @throws CloneNotSupportedException If there's an error on the cloning 
   *                                    strategy cloning process
   * @return The cloning strategy clone
   */

  Object clone() throws CloneNotSupportedException;
}
