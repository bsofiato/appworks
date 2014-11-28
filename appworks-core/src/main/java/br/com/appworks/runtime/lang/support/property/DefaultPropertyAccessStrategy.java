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
package br.com.appworks.runtime.lang.support.property;

import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.property.setting.PropertySettingStrategy;

/**
 * <p>Implementation of a strategy to access a object's property.</p>
 *
 * <p>This property access strategy implementation agregates a property getting 
 * strategy and a property setting strategy to provide a 
 * property access strategy used to fetch and set a property value. All property
 * fetching related operations are delegated to the associated property getting
 * strategy, likewise, all property setting related operations are delegated to 
 * the associated property setting strategy.</p>
 *
 * @param <AccessedType> Parametrized property owner type for this property 
 *                       access strategy
 * @param <PropertyType> Parametrized property type for this property access
 *                       strategy
 * @author Bruno Sofiato
 */

public class DefaultPropertyAccessStrategy <AccessedType extends Object, PropertyType extends Object> implements PropertyAccessStrategy <AccessedType, PropertyType> {
  /**
   * <p>Associated property getting strategy.</p>
   */
  private final PropertyGettingStrategy <AccessedType, PropertyType> propertyGettingStrategy;

  /**
   * <p>Associated property setting strategy.</p>
   */

  private final PropertySettingStrategy <AccessedType, PropertyType> propertySettingStrategy;

  /**
   * <p>Gets the associated property getting strategy.</p>
   *
   * @return associated property getting strategy
   */

  private PropertyGettingStrategy <AccessedType, PropertyType> getPropertyGettingStrategy() {
    return propertyGettingStrategy;
  }

  /**
   * <p>Gets the associated property setting strategy.</p>
   *
   * @return associated property setting strategy
   */

  private PropertySettingStrategy <AccessedType, PropertyType> getPropertySettingStrategy() {
    return propertySettingStrategy;
  }
 
  /**
   * <p>Constructs a property access strategy.</p>
   */
  public DefaultPropertyAccessStrategy() {
    this(null);
  }
  /**
   * <p>Constructs a property access strategy.</p>
   *
   * @param propertyGettingStrategy Associated property getting strategy
   */

  public DefaultPropertyAccessStrategy(final PropertyGettingStrategy <AccessedType, PropertyType> propertyGettingStrategy) {
    this(propertyGettingStrategy, null);
  }

  /**
   * <p>Constructs a property access strategy.</p>
   *
   * @param propertyGettingStrategy Associated property getting strategy
   * @param propertySettingStrategy Associated property setting strategy
   */

  public DefaultPropertyAccessStrategy(final PropertyGettingStrategy <AccessedType, PropertyType> propertyGettingStrategy, 
                                              final PropertySettingStrategy <AccessedType, PropertyType> propertySettingStrategy) {
    this.propertyGettingStrategy = propertyGettingStrategy;
    this.propertySettingStrategy = propertySettingStrategy;
  }


  /**
   * <p>Fetch the property value from a supplied object.</p>
   *
   * <p>This property value fetching request is delegated to the associated 
   * property getting strategy.</p>
   * 
   * @param  object Object to fetch the property value
   * @return The property value from the supplied object
   */
  
  public PropertyType get(final AccessedType object) {
    if (getPropertyGettingStrategy() == null) {
      throw new UnsupportedOperationException();
    }
    return getPropertyGettingStrategy().get(object);
  }
  
  /**
   * <p>Sets the property value from a supplied object to a given value.</p>
   * 
   * <p>This property value fetching request is delegated to the associated 
   * property getting strategy.</p>
   *
   * @param  object Object to sets the property value
   * @param  value Property value 
   */
  
  public void set(final AccessedType object, final PropertyType value) {
    if (getPropertySettingStrategy() == null) {
      throw new UnsupportedOperationException();
    }
    getPropertySettingStrategy().set(object, value);
  }
  
  /**
   * <p>Gets the associated property name.</p>
   *
   * <p>If there's a property getting strategy associated, the property name 
   * will be fetched from it, if there isn't, the property name will be fetched 
   * from the associated property setting strategy (if there's a property 
   * setting strategy associated). If there isn't a property getting or setting
   * strategy associated, the <tt>null</tt> is returned as the associated 
   * property name.</p> 
   *
   * @return Associated property name
   */
  public String getProperty() {
    if (getPropertyGettingStrategy() != null) {
      return getPropertyGettingStrategy().getProperty();
    } else if (getPropertySettingStrategy() != null) {
      return getPropertySettingStrategy().getProperty();
    }
    return null;
  }
}
