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

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * <p>Cloning strategy for array cloning.</p>
 *
 * <p>This cloning strategy makes deep clones from arrays. All elements from 
 * the array are also cloned, unless they are <tt>immutable</tt> (Objects that 
 * are not <tt>cloneable</tt> are considered immutable).</p>
 * 
 * <p>The obtained array copy is completely deatached from the original array, 
 * any changes to the original array will <tt>not</tt> propagate to the copy 
 * array and vice-versa.</p>
 *
 * @param  <Type> Component type of the array associated with this cloning 
 *                strategy
 * @author Bruno Sofiato
 */
public class ArrayCloningStrategy <Type> implements CloningStrategy <Type> {

  /**
   * <p>Element's reflection based cloning strategy.</p>
   */
  
  private final ReflectionCloningStrategy<Type> cloningStrategy = new ReflectionCloningStrategy<Type>();
  
  /**
   * <p>Gets the element's reflection based cloning strategy.</p>
   *
   * @return Element's reflection based cloning strategy
   */
  
  private ReflectionCloningStrategy <Type> getCloningStrategy() {
    return cloningStrategy;
  }
  
  /**
   * <p>Associated array component type.</p>
   */
  
  private final Class componentType;

  /**
   * <p>Gets the associated array compoent type.</p>
   *
   * @return Associated array component type
   */
  
  private Class getComponentType() {
    return componentType;
  }

   /**
   * <p>Constructs a new array's cloning strategy.</p>
   *
   * @param componentType Component type of the associated array
   */
  
  public ArrayCloningStrategy(Class componentType) {
    this.componentType = componentType;
  }
 
  /**
   * <p>Clones an array.</p>
   *
   * <p>If the source array is <tt>null</tt>, the returned array is also 
   * <tt>null</tt>. All array elements that are not <tt>immutable</tt> are also
   * cloned.</p>
   *
   * @param  source Array to clone
   * @return Supplied array's clone
   * @throws CloneNotSupportedException If there's any errors in the array 
   *                                    cloning process
   */
  public Type clone(final Type source) throws CloneNotSupportedException {
    if (source != null) {
      if (getComponentType().isPrimitive()) {
        return getCloningStrategy().clone(source);
      } else {
        Object [] sourceArray = (Object [])(source);
        Object [] clone = (Object [])(Array.newInstance(getComponentType(), sourceArray.length));
        for (int i = 0; i < sourceArray.length; i++) {
          clone[i] = getCloningStrategy().clone((Type)(sourceArray[i]));
        }
        return (Type)(clone);
      }
    }
    return null;
  }
  
  /**
   * <p>Clone a cloning strategy for array cloning.</p>
   *
   * <p>A array cloning strategy instance may be clone if it's used as an
   * high specialized cloning strategy mapped to an type.</p>
   *
   * @throws CloneNotSupportedException If there's an error on the array cloning 
   *                                    strategy cloning process
   * @return The cloning strategy for array cloning clone
   */

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
