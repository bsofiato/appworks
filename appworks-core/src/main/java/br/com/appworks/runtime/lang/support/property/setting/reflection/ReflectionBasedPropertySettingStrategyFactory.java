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
import java.lang.reflect.Field;
import br.com.appworks.runtime.lang.support.reflect.ClassUtils;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import br.com.appworks.runtime.lang.support.property.setting.PropertySettingStrategy;
import br.com.appworks.runtime.lang.support.property.setting.PropertySettingStrategyFactory;

/**
 * <p>Property setting strategy factory implementation.</p>
 * 
 * <p>Based on a supplied field and optional setters, creates a property access 
 * strategy reflecting the supplied property fetching semantics. If wasn't 
 * supplied a setter method, the factory will look for the method via the 
 * <tt>Introspector</tt> class, if the class instropection don't find any 
 * suitable method, direct field access is used.</p>
 *
 * @author Bruno Sofiato
 * @see java.beans.Introspector
 */

public class ReflectionBasedPropertySettingStrategyFactory implements PropertySettingStrategyFactory {
  
  /**
   * <P>The accessor method name's prefix offset.</p>
   */ 
  
  private static final int METHOD_NAME_PREFIX_OFFSET = 3;
  
  /**
   * <p>Creates a setter based property getting strategy.</p>
   *
   * @param  klass Class associated with the property
   * @param  setter Setter name
   * @param  propertyType Property type
   * @param  propertyName Property name
   * @param  <AccessedType> Parametrized property owner type for the property 
   *                        access strategy
   * @param  <PropertyType> Parametrized property type for the property access
   *                        strategy
   * @return Setter based property setting strategy
   */
  
  private <AccessedType, PropertyType> PropertySettingStrategy<AccessedType, PropertyType> createOverridedSetterPropertyAccessStrategy(final Class klass, 
                                                                                                                                       final String setter, 
                                                                                                                                       final Class propertyType, 
                                                                                                                                       final String propertyName) {
    return new SetterPropertySettingStrategy<AccessedType, PropertyType>(ClassUtils.getDeclaredMethod(klass, setter, propertyType), propertyName);
  }
  
  /**
   * <p>Creates a property setting strategy based on a getter method.</p>
   *
   * <p>This method try to find a suitable setter method to be used to fetch the
   * associated property value. First it tries to find property setter via class
   * introspection. If no suitable setter is found, it tries to found a method 
   * with the name <tt>set<i>PropertyName</i>()</tt> with one parameter 
   * compatible with the property type. If no such method is found, return no 
   * strategy.</p>
   * 
   * @param  field Class field associated with the property
   * @param <AccessedType> Parametrized property owner type for the property 
   *                       access strategy
   * @param <PropertyType> Parametrized property type for the property access
   *                       strategy
   * @return Property setting strategy reflecting the supplied property setting
   *         semantics, or <tt>null</tt> if it's not possible to set the 
   *         property value with a setter method
   */ 
  
  private <AccessedType, PropertyType> PropertySettingStrategy<AccessedType, PropertyType> createSetterPropertyAccessStrategy(final Field field) {
    try {
      BeanInfo info = Introspector.getBeanInfo(field.getDeclaringClass());
      for (PropertyDescriptor propertyDescriptor : info.getPropertyDescriptors()) {
        if ((field.getName().equals(propertyDescriptor.getName())) && 
            (propertyDescriptor.getWriteMethod() != null)) {
          return new SetterPropertySettingStrategy<AccessedType, PropertyType>(propertyDescriptor.getWriteMethod(), field.getName());
        }
      }
    } catch (IntrospectionException ex) {
      // Fall Through
    }
    StringBuilder setterName = new StringBuilder("set");
    setterName.append(field.getName());
    setterName.setCharAt(METHOD_NAME_PREFIX_OFFSET, Character.toUpperCase(setterName.charAt(METHOD_NAME_PREFIX_OFFSET)));
    return new SetterPropertySettingStrategy<AccessedType, PropertyType>(ClassUtils.getDeclaredMethod(field.getDeclaringClass(), setterName.toString(), field.getType()), field.getName());
  }

  /**
   * <p>Creates a property setting strategy.</p>
   *
   * <p>Try to create a property setting strategy based on a supplied property.
   * If a setter method is supplied, it will try to create a setter based 
   * property strategy based on the supplied setter method. Otherwise it will 
   * try to locate a setter method via class instrospector (the 
   * <tt>createSetterPropertyAccessStrategy()</tt> method), if these scenarios 
   * fail to provide a property getting strategy, a direct field access strategy
   * is used.</p>
   * 
   * @param  field Class field associated with the property
   * @param  setter Setter name (optional)
   * @param <AccessedType> Parametrized property owner type for the property 
   *                       access strategy
   * @param <PropertyType> Parametrized property type for the property access
   *                       strategy
   * @return Property setting strategy reflecting the supplied property fetching
   *         semantics
   */ 

  public <AccessedType, PropertyType> PropertySettingStrategy<AccessedType, PropertyType> create(final Field field, 
                                                                                                 final String setter) {
    if ((setter != null) && (setter.length() > 0)) {
      return createOverridedSetterPropertyAccessStrategy(field.getDeclaringClass(), setter, field.getType(), field.getName());
    } else {
      try {
        return createSetterPropertyAccessStrategy(field);
      } catch (NoSuchMethodError ex) {
        return new DirectFieldPropertySettingStrategy<AccessedType, PropertyType>(field);
      }
    }
  }

  /**
   * <p>Creates a setter based property setting strategy.</p>
   *
   * @param  setter Setter
   * @param  <AccessedType> Parametrized property owner type for the property 
   *                        access strategy
   * @param  <PropertyType> Parametrized property type for the property access
   *                        strategy
   * @return Setter based property getting strategy
   */
  
  public <AccessedType, PropertyType> PropertySettingStrategy<AccessedType, PropertyType> create(final Method setter) {
    return (setter == null) ? null : new SetterPropertySettingStrategy<AccessedType, PropertyType>(ClassUtils.getDeclaredMethod(setter.getDeclaringClass(), setter), ClassUtils.getPropertyName(setter));
  }
}