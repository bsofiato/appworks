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

import java.util.Map;

/**
 * <p>Cloning strategy for map cloning.</p>
 * 
 * <p>This cloning strategy is a specialized cloning strategy thats encapsulate
 * an generic map cloning strategy algorithm. The implemented algorithm creates 
 * a <tt>deep</tt> clone of the supplied map (in a deep cloning
 * all associated keys and values are cloned too, unless they're 
 * <tt>immutables</tt>).</p>
 *
 * <p>The obtained map copy is completely deatached from the original map and is 
 * a instance of the same class of the original map, any changes to the original
 * map will <tt>not</tt> propagate to the copy map and vice-versa.</p>
 * 
 * @param <Key> Parametrized key type for the map cloning strategy
 * @param <Value> Parametrized value type for the map cloning strategy
 * @author Bruno Sofiato
 */
public class MapCloningStrategy <Key, Value> implements CloningStrategy <Map<Key, Value>> { 

  /**
   * <p>Key's reflection based cloning strategy.</p>
   */

  private final ReflectionCloningStrategy <Key> keyCloningStrategy = new ReflectionCloningStrategy<Key>();

  /**
   * <p>Value's reflection based cloning strategy.</p>
   */

  private final ReflectionCloningStrategy <Value> valueCloningStrategy = new ReflectionCloningStrategy<Value>();
  
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
   * <p>Clones a map.</p>
   *
   * <p>If the source map is <tt>null</tt>, the returned map is also 
   * <tt>null</tt>. All map keys or values that are not <tt>immutable</tt>
   * are also cloned.</p>
   *
   * @param  source Map to clone
   * @return Supplied map's clone
   * @throws CloneNotSupportedException If there's any errors in the map 
   *                                    cloning process
   */

  public Map <Key, Value> clone(final Map <Key, Value> source) throws CloneNotSupportedException {
    try {
      Map <Key, Value> clone = null;
      if (source != null) {
        clone = (Map <Key, Value>) (source.getClass().newInstance());
        for (Map.Entry <Key, Value> entry : source.entrySet()) {
          Key key = getKeyCloningStrategy().clone(entry.getKey());
          Value value = getValueCloningStrategy().clone(entry.getValue());
          clone.put(key, value);
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
   * <p>Clone a cloning strategy for map cloning.</p>
   *
   * <p>A cloning strategy for map cloning instance may be clone if it's used as
   * an high specialized cloning strategy mapped to an type.</p>
   *
   * @throws CloneNotSupportedException If there's an error on the cloning 
   *                                    strategy for map cloning process
   * @return The cloning strategy for map clone
   */

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
