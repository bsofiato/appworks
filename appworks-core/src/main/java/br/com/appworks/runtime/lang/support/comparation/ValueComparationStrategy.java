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

/**
 * <p>Comparation strategy based on the object's value.</p>
 *  
 * <p>This comparation strategy use the <tt>Object.equals()</tt>, 
 * <tt>Object.hashCode()</tt> and <tt>Comparable.compareTo()</tt> defined on 
 * the supplied object class.</p>
 *
 * @param  <Type> Type associated with this value based comparation strategy
 * @author Bruno Sofiato
 */
public class ValueComparationStrategy <Type extends Object> extends AbstractComparationStrategy <Type> {
  
  /**
   * <p>Compares its two arrays for natural order.</p>
   *
   * <p>Returns a negative integer, zero, or a positive integer as the first 
   * argument is less than, equal to, or greater than the second.</p>
   * 
   * <p>The natural order factor calculation is based on the 
   * <tt>Comparable.compareTo()</tt> implemented by the operands. If the 
   * operands are not instances of <tt>Comparable</tt>, a <tt>ClassCastException</tt> 
   * exception is throwed.</p>
   *
   * @param  op1 First operand
   * @param  op2 Second operand
   * @return A negative integer, zero, or a positive integer as the
   *         first argument is less than, equal to, or greater than the
   *         second
   * @throws ClassCastException If the supplied objects aren't instance from 
   *                            <tt>Cloneable</tt>
   */

  protected int doCompare(final Type op1, final Type op2) {
    return ((java.lang.Comparable <Type>) op1).compareTo(op2);
  }
  
  /**
   * <p>Constructs a new comparation strategy based on the object's value.</p>
   */
  
  public ValueComparationStrategy() {
    this(OrderPolicy.NATURAL);
  }
  
  /**
   * <p>Constructs a new comparation strategy based on the object's value.</p>
   *
   * @param policy Natural order policy
   */

  public ValueComparationStrategy(final OrderPolicy policy) {
    this(policy, -1);
  }

  /**
   * <p>Constructs a new comparation strategy based on the object's value.</p>
   *
   * @param index Evaluation order
   * @param policy Natural order policy
   */

  public ValueComparationStrategy(final OrderPolicy policy, final int index) {
    setOrderPolicy(policy);
    setIndex(index);
  }
  
  /**
   * <p>Compares two supplied objects.</p>
   *
   * <p>If the first operand is not <tt>null</tt>, the comparation processing is
   * delegated to the first operand <tt>Object.equals()</tt> method, otherwise, 
   * if the second operand is <tt>null</tt>, the operands are considered to be 
   * equivalent.</p>
   *
   * @param  op1 First object
   * @param  op2 Second object
   * @return <tt>true</tt> if the supplied objects are considered equivalent
   *         <tt>false</tt> otherwise
   */
  public boolean equals(final Type op1, final Type op2) {
    if (op1 != op2) {
      return (op1 == null) ? false : op1.equals(op2);
    }
    return true;
  }
  
  /**
   * <p>Calculates the supplied object hash code.</p>
   *
   * <p>If the supplied object is not null, the hash code calculation process is
   * delegated to the <tt>Object.hashCode()</tt> method implemented by the 
   * supplied object class, otherwise, the returned hash code is <tt>zero</tt>.
   *
   * @param  object Supplied object
   * @return The supplied object hash code
   */
  public int hashCode(final Type object) {
    return (object == null) ? 0 : object.hashCode();
  }
}
