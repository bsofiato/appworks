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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;


/**
 * <p>Getter based property getting strategy.</p>
 *
 * <p>This property getting strategy access the property value using a supplier
 * getter method, even if the supplied method would not be normaly acessible 
 * (like if it's a private method).</p>
 *
 * @param <AccessedType> Parametrized property owner type for this getter based,
 *                       property getting strategy
 * @param <PropertyType> Parametrized property type for this getter based, 
 *                       property getting strategy
 * @author Bruno Sofiato
 */
public class GetterPropertyGettingStrategy <AccessedType extends Object, PropertyType extends Object> implements PropertyGettingStrategy <AccessedType, PropertyType> {
  
  /**
   * <p>Associated property name.</p>
   */
  
  private final String property;
  
  /**
   * <p>Associated getter method.</p>
   */

  private final Method getter;
  
  /**
   * <p>Gets the associated getter method.</p>
   *
   * @return Associated getter method
   */
  
  private Method getGetter() {
    return getter;
  }
  
  /**
   * <p>Constructs a new getter based property getting strategy.</p>
   *
   * @param getter Associated getter method
   * @param property Associated property name
   */

  public GetterPropertyGettingStrategy(final Method getter, final String property) {
    this.getter = getter;
    this.property = property;
    getter.setAccessible(true);
  }
  
  /**
   * <p>Fetch the property value from a supplied object.</p>
   * 
   * <p>The property value is fetch by the associated getter method. If the 
   * supplied object isn't an instance from the associated method class, a 
   * <tt>RuntimeException</tt> will be thrown.</p>
   *
   * @param  object Object to fetch the property value
   * @return The property value from the supplied object
   * @throws RuntimeException If the supplied object isn't an instance from the 
   *                          associated method class
   */
  
  public PropertyType get(final AccessedType object) {
    try {
      return (PropertyType) (getGetter().invoke(object));
    } catch (InvocationTargetException ex) {
      throw new RuntimeException(ex.getCause());
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * <p>Gets the associated property name.</p>
   *
   * @return Associated property name
   */
  public String getProperty() {
    return property;
  }
}
