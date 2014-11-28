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

package br.com.appworks.runtime.lang.support.stringfication;

import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;

/**
 * <p>Property based stringfication strategy implementation.</p>
 *
 * <p>This stringfication strategy encapsulates the property fetching/value 
 * stringfication logic. The associated property getting strategy implements the 
 * necessary steps to fetch the property value from the operand and encapsulate
 * the associated property metadata, and the associated stringfication strategy
 * implements the string coversion algorithm used to process the fetched 
 * property value.</p>
 *
 * <p>The associated property string representation created by this strategy is 
 * composed of the associated property name, and a string representation of it's
 * associated value. The string representation follows the given pattern :<p>
 *
 * <blockquote>
 *   <code>property name : property value</code>
 * </blockquote>
 * 
 * @param <AccessedType> Parametrized property owner type for the property based 
 *                       stringfication strategy
 * @param <PropertyType> Parametrized property type for the property based 
 *                       stringfication strategy
 * @author Bruno Sofiato
 */

public class PropertyStringficationStrategy <AccessedType, PropertyType> extends AbstractStringficationStrategy <AccessedType> {

  /**
   * <p>Associated stringfication strategy.</p>
   */
  
  private final StringficationStrategy <PropertyType> stringficationStrategy;

  /**
   * <p>Associated property getting strategy.</p>
   */
  
  private final PropertyGettingStrategy <AccessedType, PropertyType> propertyGettingStrategy;
  
  /**
   * <p>Gets the associated stringfication strategy.</p>
   *
   * @return Associated stringfication strategy
   */
  
  private StringficationStrategy <PropertyType> getStringficationStrategy() {
    return stringficationStrategy;
  }
  
  /**
   * <p>Gets the associated property getting strategy.</p>
   *
   * @return Associated property getting strategy
   */
  
  private PropertyGettingStrategy <AccessedType, PropertyType> getPropertyGettingStrategy() {
    return propertyGettingStrategy;
  }
  
  /**
   * <p>Constructs a property based stringfication strategy.</p>
   *
   * @param propertyGettingStrategy Associated property getting strategy
   * @param stringficationStrategy Associated stringfication strategy
   */
  
  public PropertyStringficationStrategy(final PropertyGettingStrategy <AccessedType, PropertyType> propertyGettingStrategy, 
                                        final StringficationStrategy <PropertyType> stringficationStrategy) {
    this.propertyGettingStrategy = propertyGettingStrategy;
    this.stringficationStrategy = stringficationStrategy;
  }
  
  /**
   * <p>Gets the supplied object string representation, based on a defined 
   * property value.</p>
   * 
   * <p>The property value is fetched from the supplied object by the associated 
   * property getting strategy, the fetched value is delegated to the associated
   * stringfication strategy, who creates the value string representation. The 
   * associated property metadata and it's value's string representation are
   * agreggated following a pre-defined pattern.</p>
   *
   * @param object Supplied object
   * @param sb StringBuilder to append the string representation from the
   *           supplied instance.
   */
  
  protected void safeToString(final AccessedType object, final StringBuilder sb) {
    if (object != null) {
      sb.append(getPropertyGettingStrategy().getProperty());
      sb.append(" : ");
      getStringficationStrategy().toString(getPropertyGettingStrategy().get(object), sb);
    }
  }
}
