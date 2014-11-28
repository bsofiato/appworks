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

/**
 * <p>Property setting strategy.</p>
 *
 * <p>A property setting strategy encapsulates the needed logic to sets an 
 * property value from a given object.</p>
 *
 * @param <AccessedType> Parametrized property owner type for this property 
 *                       setting strategy
 * @param <PropertyType> Parametrized property type for this property setting
 *                       strategy
 * @author Bruno Sofiato
 */

public interface PropertySettingStrategy <AccessedType extends Object, PropertyType extends Object>  {
  /**
   * <p>Sets the property value from a supplied object to a given value.</p>
   * 
   * @param  object Object to sets the property value
   * @param  value Property value 
   */

  void set(AccessedType object, PropertyType value);
  
  /**
   * <p>Gets the associated property name.</p>
   *
   * @return Associated property name
   */
  
  String getProperty();
}
