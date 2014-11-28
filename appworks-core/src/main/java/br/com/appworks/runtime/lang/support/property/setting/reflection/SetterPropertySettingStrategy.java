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
package br.com.appworks.runtime.lang.support.property.setting.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import br.com.appworks.runtime.lang.support.property.setting.PropertySettingStrategy;

/**
 * <p>Setter based property setting strategy.</p>
 *
 * <p>This property setting strategy access the property value using a supplier
 * setter method, even if the supplied method would not be normaly acessible 
 * (like if it's a private method).</p>
 *
 * @param <AccessedType> Parametrized property owner type for this setter based,
 *                       property setting strategy
 * @param <PropertyType> Parametrized property type for this setter based, 
 *                       property setting strategy
 * @author Bruno Sofiato
 */

public class SetterPropertySettingStrategy <AccessedType extends Object, PropertyType extends Object> implements PropertySettingStrategy <AccessedType, PropertyType> {

  /**
   * <p>Associated property name.</p>
   */
  
  private final String property;
  
  /**
   * <p>Associated setter method.</p>
   */
  
  private final Method setter;
  
  /**
   * <p>Gets the associated setter method.</p>
   *
   * @return Associated setter method
   */
  
  private Method getSetter() {
    return setter;
  }
  
  /**
   * <p>Constructs a new setter based property setting strategy.</p>
   *
   * @param setter Associated setter method
   * @param property Associated property name
   */

  public SetterPropertySettingStrategy(final Method setter, final String property) {
    this.setter = setter;
    this.property = property;
    setter.setAccessible(true);
  }
  
  /**
   * <p>Sets the property value from a supplied object to a given value.</p>
   * 
   * <p>The property value is setted by the associated setter method. If the 
   * supplied object isn't an instance from the associated method class, a 
   * <tt>RuntimeException</tt> will be thrown.</p>
   *
   * @param  object Object to set the property value
   * @param  value Property value
   * @throws RuntimeException If the supplied object isn't an instance from the 
   *                          associated method class
   */
  public void set(final AccessedType object, final PropertyType value) {
    try {
      getSetter().invoke(object, value);
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
