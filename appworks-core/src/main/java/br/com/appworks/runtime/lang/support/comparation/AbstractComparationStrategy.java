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
 * <p>Base implementation for comparation strategies.</p>
 *
 * <p>The natural ordering of the comparation strategies is based on their's 
 * <tt>evaluation order</tt> (comparation stategies with a lower evaluation 
 * order must be evaluated before comparation strategies with a higher 
 * evaluation order).</p>
 *
 * <p>This class defines a basic structure and helper methods to easy the
 * comparation strategy implementation development.</p>
 *
 * @param  <Type> Parametrized type for the comparation strategy
 * @author Bruno Sofiato
 */

public abstract class AbstractComparationStrategy <Type> implements ComparationStrategy <Type> {
  
  /**
   * <p>Evaluation order.</p>
   */
  
  private int index = -1;
  
  /**
   * <p>Natural order policy.</p>
   */

  private OrderPolicy orderPolicy = OrderPolicy.NATURAL;
  
  /**
   * <p>Compares two operands.</p>
   *
   * <p>This method implements the natural order comparation algorithm. The 
   * return natural order result <tt>must not</tt> be processed by the 
   * associated order policy. All implementing classes may assume that the 
   * supplied operands will not be <tt>null</tt>.</p>
   *
   * @param  op1 First operand
   * @param  op2 Second operand
   * @return A negative integer, zero, or a positive integer as first operand
   *         is less than, equal to, or greater than the second operand
   */
  protected abstract int doCompare(Type op1, Type op2);

  /**
   * <p>Gets the natural order policy.</p>
   *
   * @return Natural order policy
   */
  
  public OrderPolicy getOrderPolicy() {
    return orderPolicy;
  }

  /**
   * <p>Sets the natural order policy.</p>
   *
   * @param orderPolicy Natural order policy
   */

  public void setOrderPolicy(final OrderPolicy orderPolicy) {
    this.orderPolicy = orderPolicy;
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
   * <p>Sets the evaluation order.</p>
   *
   * @param index Evaluation order
   */

  public void setIndex(final int index) {
    this.index = index;
  }
  
  /**
   * <p>Compares its two arguments for natural order.</p>
   *
   * <p>Returns a negative integer, zero, or a positive integer as the first 
   * argument is less than, equal to, or greater than the second.</p>
   * 
   * <p>The natural order factor calculation is delegated to the 
   * <tt>doCompare()</tt> method, the returned factor is processed by the 
   * associated order policy. The natural order of an <tt>null</tt> operand is 
   * considered lower than the natural order of an <tt>not null</tt> operand.</p>
   *
   * @param  op1 First operand
   * @param  op2 Second operand
   * @return A negative integer, zero, or a positive integer as the
   *         first argument is less than, equal to, or greater than the
   *         second
   */

  public int compare(final Type op1, final Type op2) {
    int compare = 0;
    if (!equals(op1, op2)) {
      if (op1 == null) {
        compare = -1;
      } else if (op2 == null) {
        compare = 1;
      } else {
        compare = doCompare(op1, op2);
      }
    }
    return getOrderPolicy().adjustComparationResult(compare);
  }
  
  /**
   * <p>Clone an comparation strategy.</p>
   *
   * <p>A comparation strategy instance may be clone if it's used as an
   * high specialized comparation strategy mapped to an type.</p>
   *
   * @throws CloneNotSupportedException If there's an error on the comparation 
   *                                    strategy cloning process
   * @return The comparation strategy clone
   */

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  
}
