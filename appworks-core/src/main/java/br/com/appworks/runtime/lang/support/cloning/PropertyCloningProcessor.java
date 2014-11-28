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

package br.com.appworks.runtime.lang.support.cloning;

import br.com.appworks.runtime.lang.support.property.PropertyAccessStrategy;

/**
 * <p>Property based cloning process task.</p>
 *
 * <p>A property cloning processor encapsulates a aggregated task to a type 
 * cloning process that's bound to a property from the associated type.</p>
 * 
 * <p>The value of the associated property is manipulated by a property access
 * strategy that abstracts the complexity of the property access. The property
 * value is cloned by a associated cloning strategy.</p>
 *
 * @param <AccessedType> Parametrized property owner type for the property based 
 *                       cloning process task
 * @param <PropertyType> Parametrized property type for the property based 
 *                       cloning process task
 * @author Bruno Sofiato
 */

public class PropertyCloningProcessor <AccessedType extends Object, PropertyType extends Object> implements CloningProcessor <AccessedType> {
  
  /**
   * <p>Property cloning strategy.</p>
   */
  
  private final CloningStrategy <PropertyType> cloningStrategy;
  
  /**
   * <p>Property access strategy.</p>
   */
  
  private final PropertyAccessStrategy <AccessedType, PropertyType> propertyAccessStrategy;
  
  /**
   * <p>Gets the property cloning strategy.</p>
   *
   * @return Property cloning strategy
   */
  
  private CloningStrategy <PropertyType> getCloningStrategy() {
    return cloningStrategy;
  }

  /**
   * <p>Gets the property access strategy.</p>
   *
   * @return Property access strategy
   */

  private PropertyAccessStrategy <AccessedType, PropertyType> getPropertyAccessStrategy() {
    return propertyAccessStrategy;
  }
  /**
   * <p>Constructs a new property cloning processor.</p>
   *
   * @param propertyAccessStrategy Property access strategy
   * @param cloningStrategy Property cloning strategy
   */
  
  public PropertyCloningProcessor(final PropertyAccessStrategy <AccessedType, PropertyType> propertyAccessStrategy, 
                                  final CloningStrategy <PropertyType> cloningStrategy) {
    this.cloningStrategy = cloningStrategy;
    this.propertyAccessStrategy = propertyAccessStrategy;
  }

  /**
   * <p>Process an supplied clone instance.</p>
   *
   * <p>On the property cloning process, the property's value is fetched by the
   * associated property access strategy, then this value is cloned via the 
   * associated cloning strategy. The property value then is modified to conform 
   * to the value returned from the cloning strategy.</p>
   * 
   * @param  object The clone to be processed
   * @return The supplied copy processed
   * @throws CloneNotSupportedException If any error ocurr on the implemented 
   *                                    process
   */

  public AccessedType process(final AccessedType object) throws CloneNotSupportedException {
    if (object != null) {
      PropertyType property = getPropertyAccessStrategy().get(object);
      getPropertyAccessStrategy().set(object, getCloningStrategy().clone(property));
    }
    return object;
  }
}
