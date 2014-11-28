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

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.SortedSet;

/**
 * <p>Cloning strategy for sorted set cloning.</p>
 * 
 * <p>This sorted set strategy is a specialized cloning strategy thats 
 * encapsulate an sorted set cloning strategy algorithm. The implemented 
 * algorithm creates a <tt>deep</tt> clone of the supplied collection (in a deep 
 * cloning all associated elements and the associated element comparator are 
 * cloned too, unless they're <tt>immutables</tt>).</p>
 *
 * <p>The obtained sorted set copy is completely deatached from the original 
 * sorted set and is a instance of the same class of the original sorted set, 
 * any changes to the original sorted set will <tt>not</tt> propagate to the 
 * copy sorted set and vice-versa.</p>
 *
 * @param  <Type> Parametrized element type for the sorted set cloning strategy
 * @author Bruno Sofiato
 */

public class SortedSetCloningStrategy <Type> implements CloningStrategy <SortedSet<Type>> { 

  /**
   * <p>Element comparator's reflection based cloning strategy.</p>
   */
  
  private final ReflectionCloningStrategy <Comparator> comparatorCloningStrategy = new ReflectionCloningStrategy<Comparator>();
  
  /**
   * <p>Element's reflection based cloning strategy.</p>
   */

  private final ReflectionCloningStrategy <Type> elementCloningStrategy = new ReflectionCloningStrategy<Type>();

  /**
   * <p>Gets the element comparator's reflection based cloning strategy.</p>
   *
   * @return Element comparator's reflection based cloning strategy
   */

  private ReflectionCloningStrategy <Comparator> getComparatorCloningStrategy() {
    return comparatorCloningStrategy;
  }

  /**
   * <p>Gets the element's reflection based cloning strategy.</p>
   *
   * @return Element's reflection based cloning strategy
   */
  
  private ReflectionCloningStrategy <Type> getElementCloningStrategy() {
    return elementCloningStrategy;
  }
  
  /**
   * <p>Creates an new instance of the supplied sorted set implementation.</p>
   *
   * <p>This method creates an new sorted set instance from a supplied sorted 
   * set implementation. If a comparator is supplied, a copy from this 
   * comparator is associated with the created sorted set instance.</p>
   * 
   * @param  klass Sorted set implementation class
   * @param  comparator Associated comparator
   * @return New instance of the supplied sorted set type
   * @throws InstantiationException If there is any error on the sorted map 
   *                                implementation instantiation
   * @throws IllegalAccessException If there is any restrictions on the sorted 
   *                                map implementation constructor invocation
   *                                permissions
   * @throws NoSuchMethodException  If the required constructors aren't 
   *                                implemented in the supplied sorted map  
   *                                implementation
   * @throws InvocationTargetException If the target of a method invocation is 
   *                                   considered a illegal target
   */

  private SortedSet <Type> createNewInstance(final Class <? extends SortedSet> klass, 
                                             final Comparator <Type> comparator) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    if (comparator == null) {
      return klass.newInstance();
    } else {
      return klass.getConstructor(Comparator.class).newInstance(comparator);
    }
  }

  /**
   * <p>Clones a sorted set.</p>
   *
   * <p>If the source sorted set is <tt>null</tt>, the returned sorted set is 
   * also <tt>null</tt>. All sorted set elements that are not <tt>immutable</tt>
   * are also cloned (if the supplied sorted set's associated comparator isn't 
   * <tt>immutable</tt> it will be cloned too).</p>
   *
   * @param  source Sorted set to clone
   * @return Supplied sorted set's clone
   * @throws CloneNotSupportedException If there's any errors in the sorted set 
   *                                    cloning process
   */
  public SortedSet <Type> clone(final SortedSet <Type> source) throws CloneNotSupportedException {
    try {
      SortedSet <Type> clone = null;
      if (source != null) {
        clone = createNewInstance(source.getClass(), getComparatorCloningStrategy().clone(source.comparator()));
        for (Type element : source) {
          clone.add(getElementCloningStrategy().clone(element));
        }
      }
      return clone;
    } catch (NoSuchMethodException ex) {
      throw new CloneNotSupportedException();
    } catch (InvocationTargetException ex) {
      throw new CloneNotSupportedException();
    } catch (InstantiationException ex) {
      throw new CloneNotSupportedException();
    } catch (IllegalAccessException ex) {
      throw new CloneNotSupportedException();
    }
  }  

  /**
   * <p>Clone a cloning strategy for sorted set cloning.</p>
   *
   * <p>A cloning strategy for sorted set cloning instance may be clone if it's 
   * used as an high specialized cloning strategy mapped to an type.</p>
   *
   * @throws CloneNotSupportedException If there's an error on the cloning 
   *                                    strategy for sorted set cloning process
   * @return The cloning strategy for sorted set clone
   */

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
