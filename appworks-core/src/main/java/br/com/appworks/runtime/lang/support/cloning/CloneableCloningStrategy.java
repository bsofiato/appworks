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
 * <p>Cloning strategy for instance of <tt>Cloneable</tt> class cloning.</p>
 *
 * <p>This cloning strategy is a cloning strategy specialized to clone instances
 * from classes that realizes the 
 * {@link br.com.appworks.runtime.lang.support.cloning.Cloneable} interface. 
 * When an instance from a class type realizes the <tt>Cloneable</tt> interface
 * is cloned, the process is delegated to it's <tt>clone()</tt> method.</p>
 *
 * @param <Type> Cloneable type associated with this cloning strategy
 * @author Bruno Sofiato
 */
public class CloneableCloningStrategy <Type extends Cloneable> implements CloningStrategy <Type> {
  
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
  
  public Type clone(final Type source) throws CloneNotSupportedException {
    return (source == null) ? null : (Type) (source.clone());
  }
  
  /**
   * <p>Clone a cloning strategy for instance of <tt>Cloneable</tt> class 
   * cloning.</p>
   *
   * <p>A cloning strategy instance may be clone if it's used as an
   * high specialized cloning strategy mapped to an type.</p>
   *
   * @throws CloneNotSupportedException If there's an error on the cloning 
   *                                    strategy cloning process
   * @return The cloning strategy clone
   */

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
