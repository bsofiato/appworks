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

import java.util.Collection;

/**
 * <p>Cloning strategy for collections cloning.</p>
 * 
 * <p>This cloning strategy is a specialized cloning strategy thats encapsulate
 * an generic collection cloning strategy algorithm. The implemented algorithm 
 * creates a <tt>deep</tt> clone of the supplied collection (in a deep cloning
 * all associated elements are cloned too, unless they're <tt>immutables</tt>).</p>
 *
 * <p>The obtained collection copy is completely deatached from the original 
 * collection and is a instance of the same class of the original collection, 
 * any changes to the original collection will <tt>not</tt> propagate to the 
 * copy collection and vice-versa.</p>
 *
 * @param  <Type> Parametrized element type for the collection cloning strategy
 * @author Bruno Sofiato
 */

public class CollectionCloningStrategy <Type> implements CloningStrategy <Collection<Type>> {
  
  /**
   * <p>Element's reflection based cloning strategy.</p>
   */

  private final ReflectionCloningStrategy <Type> cloningStrategy = new ReflectionCloningStrategy<Type>();
  
  /**
   * <p>Gets the element's reflection based cloning strategy.</p>
   *
   * @return Element's reflection based cloning strategy
   */

  private ReflectionCloningStrategy <Type> getCloningStrategy() {
    return cloningStrategy;
  }
  
  /**
   * <p>Clones a collection.</p>
   *
   * <p>If the source collection is <tt>null</tt>, the returned collection is 
   * also <tt>null</tt>. All collection elements that are not <tt>immutable</tt>
   * are also cloned.</p>
   *
   * @param  source Collection to clone
   * @return Supplied collection's clone
   * @throws CloneNotSupportedException If there's any errors in the collection 
   *                                    cloning process
   */

  public Collection <Type> clone(final Collection <Type> source) throws CloneNotSupportedException {
    try {
      Collection <Type> clone = null;
      if (source != null) {
        clone = source.getClass().newInstance();
        for (Type element : source) {
          clone.add(getCloningStrategy().clone(element));
        }
      }
      return clone;
    } catch (InstantiationException ex) {
      throw new CloneNotSupportedException();
    } catch (IllegalAccessException ex) {
      throw new CloneNotSupportedException();
    }
  }
  
  /**
   * <p>Clone a cloning strategy for collections cloning.</p>
   *
   * <p>A cloning strategy for collections cloning instance may be clone if it's
   * used as an high specialized cloning strategy mapped to an type.</p>
   *
   * @throws CloneNotSupportedException If there's an error on the cloning 
   *                                    strategy for collections cloning
   *                                    process
   * @return The cloning strategy for collections cloning clone
   */

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
