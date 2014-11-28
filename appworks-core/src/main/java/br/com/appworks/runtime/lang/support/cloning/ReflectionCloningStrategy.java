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

import br.com.appworks.runtime.lang.support.reflect.ClassUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>Cloning strategy based on invocation of the <tt>Object.clone()</tt> 
 * method.</p>
 * 
 * <p>This cloning strategy is a specialized cloning strategy that delegates 
 * the cloning processing to the <tt>clone</tt> method of the to be cloned 
 * instance. However, if the supplied instance is an instance from a 
 * <tt>immutable</tt> type (a immutable type is a type that doesn't realizes the
 * <tt>Cloneable</tt> interface), the supplied instance itself will be 
 * returned.</p>
 *
 * @param <Type> Parametrized type for the reflection cloning strategy
 * @see java.lang.Cloneable
 * @author Bruno Sofiato
 */
public class ReflectionCloningStrategy <Type extends Object> implements CloningStrategy <Type> {
  
  /**
   * <p>Clones an supplied instance.</p>
   *
   * <p>If the source object is <tt>null</tt>, the return copy will be 
   * <tt>null</tt>, also, if the supplied instance is an instance from a 
   * <tt>immutable</tt> type (a immutable type is a type that doesn't realizes 
   * the <tt>Cloneable</tt> interface), the supplied instance itself will be 
   * returned.</p>
   *
   * @param  source Object to be cloned
   * @return A copy from the supplied instance
   * @throws CloneNotSupportedException If any error ocurr on the cloning 
   *                                    process
   */

  public Type clone(final Type source) throws CloneNotSupportedException {
    try {
      if (source instanceof java.lang.Cloneable) {
        Method method = null;
        try {
          method = source.getClass().getMethod("clone");
        } catch (NoSuchMethodException ex) {
          method = ClassUtils.getDeclaredMethod(source.getClass(), "clone");
        }
        method.setAccessible(true);
        return (Type) (method.invoke(source));
      }
      return source;
    } catch (IllegalAccessException ex) {
      throw new CloneNotSupportedException(ex.getMessage());
    } catch (InvocationTargetException ex) {
      if (ex.getTargetException() instanceof CloneNotSupportedException) {
        throw (CloneNotSupportedException) (ex.getTargetException());
      }
      throw new CloneNotSupportedException(ex.getTargetException().getMessage());
    }
  }
  
  /**
   * <p>Clone a reflection based cloning strategy.</p>
   *
   * <p>A reflection based cloning strategy instance may be clone if it's used 
   * as an high specialized cloning strategy mapped to an type.</p>
   *
   * @throws CloneNotSupportedException If there's an error on the reflection 
   *                                    based cloning strategy cloning process
   * @return The reflection based cloning strategy clone
   */

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
