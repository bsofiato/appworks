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

package br.com.appworks.runtime.lang.support;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.io.Externalizable;
import br.com.appworks.runtime.lang.support.reflect.ClassUtils;


/**
 * <p>An HashMap based, TypeMap implementation.</p>
 *
 * <p>This TypeMap implementation uses an HashMap instance as the back end to
 * all concrete type mappings. For sub-type mapping it's used an WeakHashMap 
 * to optimize the memory consumption.</p>
 * 
 * <p>To a explanation of the TypeMap contract, consult the TypeMap 
 * documentation.</p>
 *
 * @see TypeMap
 * @param <Type> Parametrized value type for this hash mapped based type map
 * @author Bruno Sofiato
 */
public class HashTypeMap <Type> implements TypeMap <Type>, Externalizable {
  
  /**
   * <p>Concrete type map.</p>
   */
  
  private Map <Class, Type> typeMapping = new HashMap<Class, Type>();
  
  /**
   * <p>Sub type map.</p>
   *
   * <p>This map is used to acccelerate sub type mapping look-up (when the type
   * is a sub type of the concrete mapped type).</p>
   */
  
  private final transient Map <Class <? extends Object>, Type> subTypeMapping = new WeakHashMap<Class <? extends Object>, Type>();
  
  /**
   * <p>Sets the concrete type map.</p>
   * 
   * @param  typeMapping Concrete type map
   */
  private void setTypeMapping(final Map <Class, Type> typeMapping) {
    this.typeMapping = typeMapping;
  }

  /**
   * <p>Gets the concrete type map.</p>
   * 
   * @return Concrete type map
   */

  private Map <Class, Type> getTypeMapping() {
    return typeMapping;
  }

  /**
   * <p>Gets the sub type map.</p>
   * 
   * @return Sub type map
   */

  private Map <Class <? extends Object>, Type> getSubTypeMapping() {
    return subTypeMapping;
  }
  
  /**
   * <p>Look up on the back end maps for a supplied type mapping.</p>
   * 
   * @param  key Type whose associated value is to be returned
   * @return The value to which the back end maps maps the specified type or 
   *         super type, or <tt>null</tt> if the back end maps contains no 
   *         mapping for this type or super type
   * @throws NullPointerException If the type is <tt>null</tt>
   */
  private Type get(final Class <? extends Object> key) {
    if (getSubTypeMapping().containsKey(key)) {
      return getSubTypeMapping().get(key);
    } else if (getTypeMapping().containsKey(key)) {
      return getTypeMapping().get(key);
    } else {
      return null;
    }
  }
  /**
   * <p>Test the back end maps for a mapping with the supplied type.</p>
   *
   * @param  key Type or sub type whose presence in this map is to be tested
   * @return <tt>true</tt> if this map contains a mapping for the specified
   *         type or sub type
   * @throws NullPointerException If the type is <tt>null</tt>
   */
  private boolean containsKey(final Class <? extends Object> key) {
    return getTypeMapping().containsKey(key) || getSubTypeMapping().containsKey(key);
  }
  
  /**
   * <p>Construct an new empty HashTypeMap instance.</p>
   */
  
  public HashTypeMap() {
  }
  
  /**
   * <p>Construct an new HashTypeMap instance, using the supplied map as the
   * initial concrete type map.</p>
   * 
   * @param typeMapping Initial concrete type map
   */
  
  public HashTypeMap(final Map <? extends Class <? extends Object>, ? extends Type> typeMapping) {
    putAll(typeMapping);
  }

  /**
   * <p>Returns a set view of the concrete type mappings contained in this 
   * map.</p>
   *
   * <p>Each element in the returned set is a <tt>Map.Entry</tt>.  The set is 
   * backed by the map, so changes to the map are reflected in the set, and 
   * vice-versa. If the map is modified while an iteration over the set is in
   * progress (except through the iterator's own <tt>remove</tt> operation, or 
   * through the <tt>setValue</tt> operation on a map entry returned by the 
   * iterator) the results of the iteration are undefined.  The set supports 
   * element removal, which removes the corresponding mapping from the map, via
   * the <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>,
   * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not support the 
   * <tt>add</tt> or <tt>addAll</tt> operations.</p>
   *
   * @return A set view of the concrete type mappings contained in this map
   */

  public Set<Map.Entry<Class, Type>> entrySet() {
    return getTypeMapping().entrySet();
  }
  
  /**
   * <p>Returns a collection view of the values contained in the concrete type
   * map.</p>
   * 
   * <p>The collection is backed by the map, so changes to the map are reflected 
   * in the collection, and vice-versa. If the map is modified while an 
   * iteration over the collection is in progress (except through the iterator's 
   * own <tt>remove</tt> operation), the results of the iteration are 
   * undefined. The collection supports element removal, which removes the 
   * corresponding mapping from the map, via the <tt>Iterator.remove</tt>, 
   * <tt>Collection.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and 
   * <tt>clear</tt> operations. It does not support the add or <tt>addAll</tt> 
   * operations.</p>
   *
   * @return A collection view of the values contained in this map
   */

  public Collection<Type> values() {
    return getTypeMapping().values();
  }

  /**
   * <p>Returns a set view of the concrete types contained in this map.</p>
   * 
   * <p>The set is backed by the map, so changes to the map are reflected in the 
   * set, and vice-versa.  If the map is modified while an iteration over the 
   * set is in progress (except through the iterator's own <tt>remove</tt>
   * operation), the results of the iteration are undefined. The set supports 
   * element removal, which removes the corresponding mapping from the map, via
   * the <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt> 
   * <tt>retainAll</tt>, and <tt>clear</tt> operations. It does not support the 
   * add or <tt>addAll</tt> operations.</p>
   *
   * @return A set view of the keys contained in this map
   */
  
  public Set<Class> keySet() {
    return getTypeMapping().keySet();
  }
  
  /**
   * <p>Removes the mapping for this concrete type from this map if it is 
   * present.</p>
   *
   * <p>More formally, if this map contains a mapping from type <tt>t</tt> to 
   * value <tt>v</tt> such that <code>(type==null ? t==null : 
   * type.equals(t))</code>, that mapping is removed. (The map can contain at 
   * most one such mapping).</p>
   *
   * <p>Returns the value to which the map previously associated the concrete 
   * type, or <tt>null</tt> if the map contained no mapping for this concrete 
   * type (A <tt>null</tt> return can also indicate that the map previously
   * associated <tt>null</tt> with the specified concrete type). The map will 
   * not contain a mapping for the specified concrete type once the call 
   * returns.</p>
   *
   * <p>When one concrete type mapping is removed, the sub type cache mapping is
   * reseted, to enforce the type mapping integrity. </p>
   *
   * @param  key Concrete type whose mapping is to be removed from the map
   * @return Previous value associated with specified concrete type, or 
   *         <tt>null</tt> if there was no mapping for key.
   */

  public Type remove(final Object key) {
    getSubTypeMapping().clear();
    return getTypeMapping().remove(key);
  }

  /**
   * <p>Copies all of the concrete type mappings from the specified map to 
   * this map. <p>
   * 
   * <p>The effect of this call is equivalent to that of calling 
   * <tt>put(type, value)</tt> on this map once for each mapping from concrete 
   * type <tt>type</tt> to value <tt>value</tt> in the specified map. </p>
   *
   * <p>When concrete type mappings are added, the sub type cache mapping is
   * reseted, to enforce the type mapping integrity. </p>
   *
   * @param  map Type mappings to be stored in this map.
   * @throws NullPointerException if the specified map is <tt>null</tt>
   */

  public void putAll(final Map <? extends Class, ? extends Type> map) {
    clear();
    getTypeMapping().putAll(map);
  }
  
  /**
   * <p>Associates the specified value with the specified concrete type in this 
   * map.</p>
   * 
   * <p>If the map previously contained a mapping for this concrete type, the 
   * old value is replaced by the specified value (A map <tt>m</tt> is said to 
   * contain a mapping for a concrete type <tt>type</tt> if and only if 
   * <tt>m.containsKey(k)</tt> would return <tt>true</tt>).</p> 
   *
   * <p>When a concrete type mapping is added, the sub type cache mapping is
   * reseted, to enforce the type mapping integrity. </p>
   *
   * @param  key Concrete type with which the specified value is to be 
   *             associated
   * @param  value Value to be associated with the specified concrete type
   * @return Previous value associated with specified concrete type, or 
   *         <tt>null</tt> if there was no mapping for concrete type.  A 
   *         <tt>null</tt> return can also indicate that the map previously 
   *         associated <tt>null</tt> with the specified concrete type
   */


  public Type put(final Class key, final Type value) {
    getSubTypeMapping().clear();
    return getTypeMapping().put(key, value);
  }

  /**
   * <p>Returns the value to which this map maps the type.  Returns <tt>null</tt> 
   * if the map contains no mapping for this type or any type's super type. A 
   * return value of <tt>null</tt> does not <i>necessarily</i> indicate that the
   * map contains no mapping for the type or super type; it's also possible that 
   * the map explicitly maps the type or super type to <tt>null</tt>.  
   * The <tt>containsKey</tt> operation may be used to distinguish these two 
   * cases.</p>
   *
   * <p>When two or more super types from a type are mapped. It's returned the 
   * super type with the <tt>greater</tt> inheritance level (the more specialized
   * type).</p>
   *
   * @param  key Type whose associated value is to be returned
   * @return The value to which this map maps the specified type or super type,
   *         or<tt>null</tt> if the map contains no mapping for this type or sub
   *         type
   * @throws NullPointerException If the type is <tt>null</tt>
   * @see #containsKey(Object)
   */

  public Type get(final Object key) {
    Class <? extends Object> klass = (Class <? extends Object>) key;
    Type type = get(klass);
    if (type == null) {
      for (Class <? extends Object> superClass : ClassUtils.getSuperClasses(klass)) {
        type = get(superClass);
        if (type != null) {
          getSubTypeMapping().put(klass, type);
          break;
        }
      }
    }
    return type;
  }

  /**
   * <p>Returns <tt>true</tt> if this map maps one or more concrete types to the
   * specified value.</p>
   *
   * <p>More formally, returns <tt>true</tt> if and only if this map contains at
   * least one mapping to a value <tt>v</tt> such that <tt>(value==null ? 
   * v==null : value.equals(v))</tt>.  This operation will require time linear 
   * in the map size.</p>
   *
   * @param  value Value whose presence in this map is to be tested
   * @return <tt>True</tt> if this map maps one or more concrete types to the 
   *         specified value
   */

  public boolean containsValue(final Object value) {
    return getTypeMapping().containsValue(value);
  }
  
  /**
   * Returns <tt>true</tt> if this map contains a mapping for the specified
   * type or super type.  More formally, returns <tt>true</tt> if and only if
   * this map contains a mapping for a key <tt>k</tt> such that
   * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
   * at most one such mapping.)
   *
   * @param key Type or sub type whose presence in this map is to be tested
   * @return <tt>true</tt> if this map contains a mapping for the specified
   *         type or sub type
   * @throws NullPointerException If the type is <tt>null</tt>
   */
    
  public boolean containsKey(final Object key) {
    Class <? extends Object> klass = (Class <? extends Object>) key;
    if (!containsKey(klass)) {
      for (Class <? extends Object> superClass : ClassUtils.getSuperClasses(klass)) {
        if (containsKey(superClass)) {
          return true;
        }
      }
      return false;
    }
    return true;
  }

  /**
   * <p>Removes all type mappings from this map.</p>
   *
   * <p>When a concrete type mapping is cleared, the sub type cache mapping is
   * reseted, to enforce the type mapping integrity. </p>
   */

  public void clear() {
    getTypeMapping().clear();
    getSubTypeMapping().clear();
  }

  /**
   * <p>Returns the number of concrete types-value mappings in this map.  If the
   * map contains more than <tt>Integer.MAX_VALUE</tt> elements, returns 
   * <tt>Integer.MAX_VALUE</tt>.</p>
   *
   * @return The number of concrete types-value mappings in this map
   */
  public int size() {
    return getTypeMapping().size();
  }
  
  /**
   * <p>Returns <tt>true</tt> if this map contains no concrete types-value 
   * mappings.</p>
   *
   * @return <tt>True</tt> if this map contains no concrete types-value mappings
   */

  public boolean isEmpty() {
    return getTypeMapping().isEmpty();
  }
  
  /**
   * <p>Returns the hash code value for this map.</p>
   * 
   * <p>The hash code of a map is defined to be the sum of the hashCodes of each 
   * entry in the concrete type map's entrySet view. This ensures that 
   * <tt>t1.equals(t2)</tt> implies that <tt>t1.hashCode()==t2.hashCode()</tt> 
   * for any two maps <tt>t1</tt> and <tt>t2</tt>, as required by the general
   * contract of <tt>Object.hashCode()</tt>. The sub type caching has no role 
   * in the hash code calculation process.</p>
   *
   * @return The hash code value for this map
   */
  
  public int hashCode() {
    return getTypeMapping().hashCode();
  }
  
  /**
   * <p>Compares the specified object with this map for equality.</p>
   *
   * <p>Returns <tt>true</tt> if the given object is also a map and the two Maps
   * represent the same mappings.  More formally, two maps <tt>t1</tt> and
   * <tt>t2</tt> represent the same mappings if 
   * <tt>t1.entrySet().equals(t2.entrySet())</tt>. The sub type caching has no 
   * role in the map comparation process.</p>
   *
   * @param  op1 Object to be compared for equality with this map
   * @return <tt>True</tt> if the specified object is equal to this map
   */

  public boolean equals(final Object op1) {
    return getTypeMapping().equals(op1);
  }
  
   /**
    * <p>Restores the mapping to a object input stream.</p>
    *
    * <p>Althrough the sub type mapping isn't serialized with the type map, 
    * it's initialized on this method.</p>
    *
    * @serialData typeMapping The concrete type mapping
    *
    * @param  in The stream to read data from in order to restore the object
    * @exception IOException Includes any I/O exceptions that may occur
    * @exception ClassNotFoundException If the class for an object being 
    *                                   restored cannot be found
    */
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    setTypeMapping((Map <Class, Type>) (in.readObject()));
  }
  
 /**
  * <p>Writes the mapping to a object output stream.</p>
  *
  * <p>The sub type cache data isn't serialized.</p>
  *
  * @serialData typeMapping The concrete type mapping
  *
  * @param  out The stream to write the object to
  * @throws IOException Includes any I/O exceptions that may occur
  */

  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeObject(getTypeMapping());
  }

}
