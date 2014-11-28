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

package br.com.appworks.runtime.lang.support.comparation;

import br.com.appworks.runtime.lang.OrderPolicy;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;

/**
 * <p>Property based comparation strategy implementation.</p>
 *
 * <p>This comparation strategy encapsulates the property fetching/value 
 * comparation logic. The associated property getting strategy implements the 
 * necessary steps to fetch the property value from the operand, the associated
 * comparation strategy implements the comparation semantics used to compare the 
 * fetched property value.</p>
 * 
 * @param <AccessedType> Parametrized property owner type for the property based 
 *                       comparation strategy
 * @param <PropertyType> Parametrized property type for the property based 
 *                       comparation strategy
 * @author Bruno Sofiato
 */

public class PropertyComparationStrategy <AccessedType, PropertyType> implements ComparationStrategy <AccessedType>, Comparable <PropertyComparationStrategy> {

  /**
   * <p>Evaluation order.</p>
   */
  
  private final int index;
  
  /**
   * <p>Associated comparation strategy.</p>
   */
  
  private final ComparationStrategy <PropertyType> comparationStrategy;
  
  /**
   * <p>Associated property getting strategy.</p>
   */

  private final PropertyGettingStrategy<AccessedType, PropertyType> propertyAccessStrategy;

  /**
   * <p>Gets the associated comparation strategy.</p>
   *
   * @return Associated comparation strategy
   */

  private ComparationStrategy <PropertyType> getComparationStrategy() {
    return comparationStrategy;
  }

  /**
   * <p>Gets the associated property getting strategy.</p>
   *
   * @return Associated property getting strategy
   */

  private PropertyGettingStrategy<AccessedType, PropertyType> getPropertyAccessStrategy() {
    return propertyAccessStrategy;
  }
  
  /**
   * <p>Constructs a property based comparation strategy.</p>
   *
   * @param index Evaluation order
   * @param propertyAccessStrategy Associated property getting strategy
   * @param comparationStrategy Associated comparation strategy
   */
  
  public PropertyComparationStrategy(final int index,
                                     final PropertyGettingStrategy<AccessedType, PropertyType> propertyAccessStrategy, 
                                     final ComparationStrategy <PropertyType> comparationStrategy) {
    this.index = index;                                    
    this.propertyAccessStrategy = propertyAccessStrategy;
    this.comparationStrategy = comparationStrategy;
  }
  
  /**
   * <p>Calculates the supplied object hash code, based on a defined 
   * property value.</p>
   * 
   * <p>The property value is fetched from the supplied object by the associated 
   * property getting strategy, the fetched value is delegated to the associated
   * comparation strategy, who calculates the hash code.</p>
   *
   * @param  object Supplied object
   * @return The supplied object hash code
   */
  
  public int hashCode(final AccessedType object) {
    return getComparationStrategy().hashCode(getPropertyAccessStrategy().get(object));
  }
  
  /**
   * <p>Compares two supplied objects, based on a defined property value.</p>
   *
   * <p>The property value is fetched from the supplied objects by the 
   * associated property getting strategy, the fetched values is delegated to 
   * the associated comparation strategy, who compares them.</p>
   *
   * @param  op1 First operand
   * @param  op2 Second operand
   * @return <tt>true</tt> if the supplied operands are considered equivalent, 
   *         or <tt>false</tt> otherwise
   */
  public boolean equals(final AccessedType op1, final AccessedType op2) {
    return getComparationStrategy().equals(getPropertyAccessStrategy().get(op1), 
                                           getPropertyAccessStrategy().get(op2));
  }
  
  /**
   * <p>Compares two supplied objects, based on a defined property value.</p>
   *
   * <p>The property value is fetched from the supplied objects by the 
   * associated property getting strategy, the fetched values is delegated to 
   * the associated comparation strategy, who compares them.</p>
   *
   * @param  op1 First operand
   * @param  op2 Second operand
   * @return A negative integer, zero, or a positive integer as first operand
   *         is less than, equal to, or greater than the second operand
   */
  public int compare(final AccessedType op1, final AccessedType op2) {
    return getComparationStrategy().compare(getPropertyAccessStrategy().get(op1), 
                                            getPropertyAccessStrategy().get(op2));
  }

  /**
   * <p>Sets the natural order policy.</p>
   *
   * <p>This comparation natural order policy is based on the associated 
   * comparation strategy natural order policy. Any changes in this comparation
   * strategy natural order policy are propragated to the associated comparation
   * strategy.</p>
   *
   * @param orderPolicy Natural order policy
   */

  public void setOrderPolicy(final OrderPolicy orderPolicy) {
    getComparationStrategy().setOrderPolicy(orderPolicy);
  }
  
  /**
   * <p>Gets the natural order policy.</p>
   *
   * <p>This comparation natural order policy is based on the associated 
   * comparation strategy natural order policy.</p>
   *
   * @return Natural order policy
   */
  
  public OrderPolicy getOrderPolicy() {
    return getComparationStrategy().getOrderPolicy();
  }
  
  /**
   * <p>Clone a property comparation strategy.</p>
   *
   * <p>This method exists only to realizes the contract defined by the 
   * <tt>ComparationStrategy</tt> interface, any call to this method will
   * result on the <tt>CloneNotSupportedException</tt> being throw.</p>
   *
   * @throws CloneNotSupportedException If this method is invoked
   * @return The property comparation strategy clone
   */

  @Deprecated
  public Object clone() throws CloneNotSupportedException {
    super.clone();
    throw new CloneNotSupportedException();
  }
  
  /**
   * <p>Gets the evaluation order.</p>
   * 
   * @return Evaluation order
   */
  
  public int getIndex() {
    return index;
  }
  
  /**
   * <p>Compare two property based comparation strategy implementation 
   * instances.</p>
   *
   * <p>The natural ordering of two property based comparation strategy is based
   * on it's evaluation order. Strategies with a lesser evaluation order should
   * be evaluated first.</p>
   *
   * @param  operand Property based comparation strategy to be compared to the 
   *                 current instance
   * @return Zero if the operands have the same evaluation order, a negative 
   *         integer if the current instance should be evaluated first, or a 
   *         positive integer if the operand should be evaluated first
   */
  
  public int compareTo(final PropertyComparationStrategy operand) {
    return getIndex() - operand.getIndex();
  }
  
  /**
   * <p>Obtains the hash code for the given property based comparation strategy 
   * instance.</p>
   * 
   * <p>The hash code is based on the index of the given comparation strategy.</p>
   * 
   * @return Hash code for property based comparation strategy
   */
  public int hashCode() {
    return getIndex();
  }
  
  /**
   * <p>Compares the given property based comparation strategy with a given 
   * operand.</p>
   * 
   * <p>To be considered equals, the given operand must be an instance of 
   * <tt>PropertyComparationStrategy</tt> and must have the same index of the 
   * given property based comparation strategy.</p>
   * 
   * @param object Object to compare to equality
   * @return <tt>True</tt> if the given object is a property based comparation
   *         strategy with the same index, <tt>false</tt> otherwise.
   */
  public boolean equals(final Object object) {
    if (object instanceof PropertyComparationStrategy) {
      return getIndex() == ((PropertyComparationStrategy) object).getIndex();
    }
    return false;
  }
}

