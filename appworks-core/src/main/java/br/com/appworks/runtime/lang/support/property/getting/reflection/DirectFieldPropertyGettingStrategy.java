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

package br.com.appworks.runtime.lang.support.property.getting.reflection;

import java.lang.reflect.Field;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;

/**
 * <p>Direct field access property getting strategy.</p>
 *
 * <p>This property getting strategy access the class field associated with the
 * property directly, bypassing any acessors or the access modifiers.</p>
 *
 * @param <AccessedType> Parametrized property owner type for this direct field 
 *                       access, property getting strategy
 * @param <PropertyType> Parametrized property type for this direct field 
 *                       access, property getting strategy
 * @author Bruno Sofiato
 */
public class DirectFieldPropertyGettingStrategy <AccessedType extends Object, PropertyType extends Object> implements PropertyGettingStrategy <AccessedType, PropertyType> {

  /**
   * <p>Associated class field.</p>
   */
  
  private final Field field;
  
  /**
   * <p>Gets the associated class field.</p>
   *
   * @return Associated class field
   */

  private Field getField() {
    return field;
  }
    
  /**
   * <p>Constructs a new direct field access property getting strategy.</p>
   * 
   * @param field Associated class field
   */
  
  public DirectFieldPropertyGettingStrategy(final Field field) {
    this.field = field;
    field.setAccessible(true);
  }
  
  /**
   * <p>Fetch the property value from a supplied object.</p>
   * 
   * <p>The property value is fetch by the associated class field. If the 
   * supplied object isn't an instance from the associated field class, a 
   * <tt>RuntimeException</tt> will be thrown.</p>
   *
   * @param  object Object to fetch the property value
   * @return The property value from the supplied object
   * @throws RuntimeException If the supplied object isn't an instance from the 
   *                          associated field class
   */

  public PropertyType get(final AccessedType object) {
    try {
      return (PropertyType) (getField().get(object));
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * <p>Gets the property name of the property associated with this property 
   * fetching strategy</p>
   *
   * <p>Direct field access property name is always the associated field's
   * name.</p>
   *
   * @return Property name
   */
  
  public String getProperty() {
    return getField().getName();
  }
}
