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

package br.com.appworks.runtime.lang.support.property.getting;

/**
 * <p>Property getting strategy.</p>
 *
 * <p>A property getting strategy encapsulates the needed logic to fetch an 
 * property value from a given object.</p>
 *
 * @param <AccessedType> Parametrized property owner type for this property 
 *                       getting strategy
 * @param <PropertyType> Parametrized property type for this property getting
 *                       strategy
 * @author Bruno Sofiato
 */
public interface PropertyGettingStrategy <AccessedType extends Object, PropertyType extends Object>  {

  /**
   * <p>Fetch the property value from a supplied object.</p>
   * 
   * @param  object Object to fetch the property value
   * @return The property value from the supplied object
   */

  PropertyType get(AccessedType object);
  
  /**
   * <p>Gets the associated property name.</p>
   *
   * @return Associated property name
   */
  
  String getProperty();
}
