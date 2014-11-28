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
 * <p>Comparation strategy based on the object's identity.</p>
 *  
 * <p>This comparation strategy semantics dictates that two object's references
 * are equivalent when they share the same <tt>identity</tt>. The only away to
 * two references share the same identity is if they are referencing the 
 * <tt>same</tt> object's instance.</p>
 *
 * @param  <Type> Type associated with this comparation strategy
 * @author Bruno Sofiato
 */

public class IdentityComparationStrategy <Type extends Object> extends AbstractComparationStrategy <Type> {
  
  /**
   * <p>Compares its two objects for natural order.</p>
   *
   * <p>Returns a negative integer, zero, or a positive integer as the first 
   * argument is less than, equal to, or greater than the second.</p>
   * 
   * <p>The natural order factor calculation is based on the hash code of the
   * operands.</p>
   *
   * @param  op1 First operand
   * @param  op2 Second operand
   * @return A negative integer, zero, or a positive integer as the
   *         first argument is less than, equal to, or greater than the
   *         second
   */
  protected int doCompare(final Type op1, final Type op2) {
    return hashCode(op1) - hashCode(op2);
  }
  
  /**
   * <p>Constructs a new comparation strategy based on the object's identity.</p>
   */
  public IdentityComparationStrategy() {
    this(OrderPolicy.NATURAL);
  }
  /**
   * <p>Constructs a new comparation strategy based on the object's identity.</p>
   *
   * @param policy Natural order policy
   */
  public IdentityComparationStrategy(final OrderPolicy policy) {
    this(policy, -1);
  }
  /**
   * <p>Constructs a new comparation strategy based on the object's identity.</p>
   *
   * @param index Evaluation order
   * @param policy Natural order policy
   */
  public IdentityComparationStrategy(final OrderPolicy policy, final int index) {
    setOrderPolicy(policy);
    setIndex(index);
  }
  
  /**
   * <p>Compares two supplied objects.</p>
   *
   * <p>This method implements the algorithm to compare the supplied objects. 
   * The implemented algorithm is based on the identity of those objects (to 
   * be considered equivalent, the supplied references must be referencing the 
   * same object or they must be <tt>null</tt>.</p>
   *
   * @param  op1 First object
   * @param  op2 Second object
   * @return <tt>true</tt> if the supplied objects are considered equivalent
   *         <tt>false</tt> otherwise
   */
  
  public boolean equals(final Type op1, final Type op2) {
    return op1 == op2;
  }
  
  /**
   * <p>Calculates the supplied object hash code.</p>
   *
   * <p>This method implements the algorithm to calculate the supplied object
   * hash code. The implemented algorithm is based on the identity hash code
   * of the supplied object.</p>
   *
   * <p>For more info on the hash code calculation algorithm, see 
   * <tt>System.identityHashCode()</tt> method.</p>
   *
   * @param  object Supplied object
   * @return The supplied object hash code
   */
  public int hashCode(final Type object) {
    return (object == null) ? 0 : System.identityHashCode(object);
  }
}
