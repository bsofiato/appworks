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
import java.util.SortedMap;

/**
 * <p>Cloning strategy for sorted map cloningp>
 * 
 * <p>This cloning strategy is a specialized cloning strategy thats encapsulate
 * an sorted map cloning strategy algorithm. The implemented algorithm creates 
 * a <tt>deep</tt> clone of the supplied sorted map (in a deep cloning all 
 * associated keys, values and any associated comparator are cloned too, unless 
 * they're <tt>immutables</tt>).</p>
 *
 * <p>The obtained map copy is completely deatached from the original map and is 
 * a instance of the same class of the original map, any changes to the original
 * map will <tt>not</tt> propagate to the copy map and vice-versa.</p>
 *
 * @param <Key> Parametrized key type for the sorted map cloning strategy
 * @param <Value> Parametrized value type for the sorted map cloning strategy
 * @author Bruno Sofiato
 */
public class SortedMapCloningStrategy <Key, Value> implements CloningStrategy <SortedMap<Key, Value>> { 

  /**
   * <p>Key's reflection based cloning strategy.</p>
   */

  private final ReflectionCloningStrategy <Key> keyCloningStrategy = new ReflectionCloningStrategy<Key>();
  
  /**
   * <p>Value's reflection based cloning strategy.</p>
   */

  private final ReflectionCloningStrategy <Value> valueCloningStrategy = new ReflectionCloningStrategy<Value>();

  /**
   * <p>Key comparator's reflection based cloning strategy.</p>
   */

  private final ReflectionCloningStrategy <Comparator<Key>> comparatorCloningStrategy = new ReflectionCloningStrategy<Comparator<Key>>();
  
  /**
   * <p>Gets the key's reflection based cloning strategy.</p>
   *
   * @return Key's reflection based cloning strategy
   */
  
  private ReflectionCloningStrategy <Key> getKeyCloningStrategy() {
    return keyCloningStrategy;
  }
  
  /**
   * <p>Gets the value's reflection based cloning strategy.</p>
   *
   * @return Value's reflection based cloning strategy
   */

  private ReflectionCloningStrategy <Value> getValueCloningStrategy() {
    return valueCloningStrategy;
  }

  /**
   * <p>Gets the key comparator's reflection based cloning strategy.</p>
   *
   * @return Key comparator's reflection based cloning strategy
   */

  private ReflectionCloningStrategy <Comparator<Key>> getComparatorCloningStrategy() {
    return comparatorCloningStrategy;
  }
  
  /**
   * <p>Creates an new instance of the supplied sorted map implementation.</p>
   *
   * <p>This method creates an new sorted map instance from a supplied sorted 
   * map implementation. If a comparator is supplied, a copy from this 
   * comparator is associated with the created sorted map instance.</p>
   * 
   * @param  klass Sorted map implementation class
   * @param  comparator Associated comparator
   * @return New instance of the supplied sorted map type
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

  private SortedMap <Key, Value> createNewInstance(final Class <? extends SortedMap> klass, 
                                                   final Comparator <Key> comparator) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    if (comparator == null) {
      return klass.newInstance();
    } else {
      return klass.getConstructor(Comparator.class).newInstance(comparator);
    }
  }

  /**
   * <p>Clones a sorted map.</p>
   *
   * <p>If the source sorted map is <tt>null</tt>, the returned map is also 
   * <tt>null</tt>. All sorted map keys or values that are not <tt>immutable</tt> 
   * are also cloned (if the supplied sorted map's associated comparator isn't 
   * <tt>immutable</tt> it will be cloned too).</p>
   *
   * @param  source Sorted map to clone
   * @return Supplied sorted map's clone
   * @throws CloneNotSupportedException If there's any errors in the sorted map 
   *                                    cloning process
   */

  public SortedMap <Key, Value> clone(final SortedMap <Key, Value> source) throws CloneNotSupportedException {
    try {
      SortedMap <Key, Value> clone = null;
      if (source != null) {
        clone = createNewInstance(source.getClass(), getComparatorCloningStrategy().clone((Comparator) (source.comparator())));
        for (SortedMap.Entry <Key, Value> entry : source.entrySet()) {
          Key key = getKeyCloningStrategy().clone(entry.getKey());
          Value value = getValueCloningStrategy().clone(entry.getValue());
          clone.put(key, value);
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
   * <p>Clone a cloning strategy for sorted map cloning.</p>
   *
   * <p>A cloning strategy for sorted map cloning instance may be clone if it's 
   * used as an high specialized cloning strategy mapped to an type.</p>
   *
   * @throws CloneNotSupportedException If there's an error on the cloning 
   *                                    strategy for sorted map cloning process
   * @return The cloning strategy for sorted map clone
   */

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
