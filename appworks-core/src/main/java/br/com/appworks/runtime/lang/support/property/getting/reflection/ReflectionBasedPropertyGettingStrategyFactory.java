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
import br.com.appworks.runtime.lang.support.reflect.ClassUtils;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategyFactory;

/**
 * <p>Property getting strategy factory implementation.</p>
 * 
 * <p>Based on a supplied field and optional getters, creates a property access 
 * strategy reflecting the supplied property fetching semantics. If wasn't 
 * supplied a getter method, the factory will look for the method via the 
 * <tt>Introspector</tt> class, if the class instropection don't find any 
 * suitable method, direct field access is used.</p>
 *
 * @author Bruno Sofiato
 * @see java.beans.Introspector
 */

public class ReflectionBasedPropertyGettingStrategyFactory implements PropertyGettingStrategyFactory {
  
  /**
   * <P>The accessor method name's prefix offset.</p>
   */ 
  
  private static final int METHOD_NAME_PREFIX_OFFSET = 3;
  
  /**
   * <p>Creates a getter based property getting strategy.</p>
   *
   * @param  property Property's name
   * @param  klass Class associated with the property
   * @param  getter Getter name
   * @param <AccessedType> Parametrized property owner type for the property 
   *                       access strategy
   * @param <PropertyType> Parametrized property type for the property access
   *                       strategy
   * @return Getter based property getting strategy
   */
  
  private <AccessedType, PropertyType> PropertyGettingStrategy<AccessedType, PropertyType> createOverridedGetterPropertyAccessStrategy(final Class klass, 
                                                                                                                                       final String getter, 
                                                                                                                                       final String property) {
    return new GetterPropertyGettingStrategy<AccessedType, PropertyType>(ClassUtils.getDeclaredMethod(klass, getter), property);
  }
  
  /**
   * <p>Creates a property getting strategy based on a getter method.</p>
   *
   * <p>This method try to find a suitable getter method to be used to fetch the
   * associated property value. First it tries to find property getter via class
   * introspection. If no suitable getter is found, it tries to found a method 
   * with the name <tt>get<i>PropertyName</i>()</tt> with no parameters and return type
   * compatible with the property type. If no such method is found, return no 
   * strategy.</p>
   * 
   * @param  field Class field associated with the property
   * @param <AccessedType> Parametrized property owner type for the property 
   *                       access strategy
   * @param <PropertyType> Parametrized property type for the property access
   *                       strategy
   * @return Property getting strategy reflecting the supplied property fetching
   *         semantics, or <tt>null</tt> if it's not possible to fetch the 
   *         property value with a getter method
   */ 
  
  private <AccessedType, PropertyType> PropertyGettingStrategy<AccessedType, PropertyType> createGetterPropertyAccessStrategy(final Field field) {
    try {
      BeanInfo info = Introspector.getBeanInfo(field.getDeclaringClass());
      for (PropertyDescriptor propertyDescriptor : info.getPropertyDescriptors()) {
        if ((field.getName().equals(propertyDescriptor.getName())) && 
            (propertyDescriptor.getReadMethod() != null)) {
          return new GetterPropertyGettingStrategy<AccessedType, PropertyType>(propertyDescriptor.getReadMethod(), field.getName());
        }
      }
    } catch (IntrospectionException ex) {
      // Fall Through
    }
    StringBuilder getterName = new StringBuilder("get");
    getterName.append(field.getName());
    getterName.setCharAt(METHOD_NAME_PREFIX_OFFSET, Character.toUpperCase(getterName.charAt(METHOD_NAME_PREFIX_OFFSET)));
    return new GetterPropertyGettingStrategy<AccessedType, PropertyType>(ClassUtils.getDeclaredMethod(field.getDeclaringClass(), getterName.toString()), field.getName());
  }

  /**
   * <p>Creates a property getting strategy.</p>
   *
   * <p>Try to create a property getting strategy based on a supplied property.
   * If a getter method is supplied, it will try to create a getter based 
   * property strategy based on the supplied getter method. Otherwise it will 
   * try to locate a getter method via class instrospector (the 
   * <tt>createGetterPropertyAccessStrategy()</tt> method), if these scenarios 
   * fail to provide a property getting strategy, a direct field access strategy
   * is used.</p>
   * 
   * @param  field Class field associated with the property
   * @param  getter Getter name (optional)
   * @param <AccessedType> Parametrized property owner type for the property 
   *                       access strategy
   * @param <PropertyType> Parametrized property type for the property access
   *                       strategy
   * @return Property getting strategy reflecting the supplied property fetching
   *         semantics
   */ 

  public <AccessedType, PropertyType> PropertyGettingStrategy<AccessedType, PropertyType> create(final Field field, 
                                                                                                 final String getter) {
    if ((getter != null) && (getter.length() > 0)) {
      return createOverridedGetterPropertyAccessStrategy(field.getDeclaringClass(), getter, field.getName());
    } else {
      try {
        return createGetterPropertyAccessStrategy(field);
      } catch (NoSuchMethodError ex) {
        return new DirectFieldPropertyGettingStrategy<AccessedType, PropertyType>(field);
      }
    }
  }

  /**
   * <p>Creates a getter based property getting strategy.</p>
   *
   * @param  getter Getter
   * @param  <AccessedType> Parametrized property owner type for the property 
   *                        access strategy
   * @param  <PropertyType> Parametrized property type for the property access
   *                        strategy
   * @return Getter based property getting strategy
   */
  
  public <AccessedType, PropertyType> PropertyGettingStrategy<AccessedType, PropertyType> create(final Method getter) {
    if (getter != null) {
      return new GetterPropertyGettingStrategy<AccessedType, PropertyType>(ClassUtils.getDeclaredMethod(getter.getDeclaringClass(), getter), ClassUtils.getPropertyName(getter));
    }
    return null;
  }
}

