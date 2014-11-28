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

package br.com.appworks.runtime.lang.support.property.setting;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * <p>Factory of property setting strategies.</p>
 *
 * <p>Based on a supplied field and optional setters, creates a property setting
 * strategy reflecting the supplied property setting semantics.</p>
 *
 * @author Bruno Sofiato
 */

public interface PropertySettingStrategyFactory {

  /**
   * <p>Constructs a property setting strategy reflecting the supplied property
   * setting semantics.</p>
   *
   * <p>If is supplied a setter method, it must be used to fetch the property 
   * value.</p>
   *
   * @param  field Class field associated with the property
   * @param  setter Setter method name (optional)
   * @param <AccessedType> Parametrized property owner type for the property 
   *                       setting strategy
   * @param <PropertyType> Parametrized property type for the property access
   *                       setting
   * @return Property setting strategy reflecting the supplied property access 
   *         semantics
   */

  <AccessedType, PropertyType> PropertySettingStrategy<AccessedType, PropertyType> create(Field field, String setter);
  
  /**
   * <p>Constructs a property setting strategy reflecting the supplied property
   * setting semantics.</p>
   *
   * @param  setter Setter method
   * @param <AccessedType> Parametrized property owner type for the property 
   *                       setting strategy
   * @param <PropertyType> Parametrized property type for the property setting
   *                       strategy
   * @return Property access strategy reflecting the supplied property setting 
   *         semantics
   */

  <AccessedType, PropertyType> PropertySettingStrategy<AccessedType, PropertyType> create(Method setter);
}
