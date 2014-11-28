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
import java.util.Arrays;

/**
 * <p>Comparation strategy for arrays.</p>
 *
 * <p>The array value comparation strategy is a high specialized value 
 * comparation strategy that deal with arrays based on theirs <tt>deep 
 * content</tt>.</p>
 *
 * @param  <Type> Component type of the array associated with this comparation 
 *                strategy
 * @author Bruno Sofiato
 */

public class ArrayValueComparationStrategy <Type> extends AbstractComparationStrategy <Type []> {
  
  /**
   * <p>Associated array component type.</p>
   */
  
  private final Class <? extends Type> componentType;

  /**
   * <p>Gets the associated array compoent type.</p>
   *
   * @return Associated array component type
   */
  
  private Class <? extends Type> getComponentType() {
    return componentType;
  }
  
  /**
   * <p>Compares its two arrays for natural order.</p>
   *
   * <p>Returns a negative integer, zero, or a positive integer as the first 
   * argument is less than, equal to, or greater than the second.</p>
   * 
   * <p>The natural order factor calculation is delegated to the 
   * <tt>compare()</tt> method define in the <tt>AbstractComparationStrategy</tt>
   * if the component type of the supplied arrays are an <tt>Comparable</tt> 
   * instance.</p>
   *
   * @param  op1 First operand
   * @param  op2 Second operand
   * @return A negative integer, zero, or a positive integer as the
   *         first argument is less than, equal to, or greater than the
   *         second
   * @throws ClassCastException If the component type of the supplied array 
   *                            isn't an <tt>Comparable</tt> instance.
   */

  public int compare(final Type [] op1, final Type [] op2) {
    if (java.lang.Comparable.class.isAssignableFrom(getComponentType())) {
      return super.compare(op1, op2);
    }
    throw new ClassCastException();
  }
 
  /**
   * <p>Compares two arrays.</p>
   *
   * <p>This method implements the array's natural order comparation algorithm. 
   * The evaluation order is defined by the index of the comparable element 
   * on the array (elements with a lower index are processed before elements 
   * with a higher index).</p>
   *
   * @param  op1 First array
   * @param  op2 Second array
   * @return A negative integer, zero, or a positive integer as first operand
   *         is less than, equal to, or greater than the second operand
   */

  protected int doCompare(final Type [] op1, final Type [] op2) {
    if (op1.length == op2.length) {
      for (int i = 0; i < op1.length; i++) {
        if (op1[i] == null)  {
          return -1;
        } else if (op2[i] == null) {
          return 1;
        } else {
          int compareTo = ((java.lang.Comparable) (op1[i])).compareTo(op2[i]);
          if (compareTo != 0) {
            return compareTo;
          }
        }
      }
    }
    return op1.length - op2.length;
  }
  /**
   * <p>Constructs a new array's comparation strategy.</p>
   *
   * @param componentType Component type of the associated array
   */
  public ArrayValueComparationStrategy(final Class <? extends Type> componentType) {
    this(componentType, OrderPolicy.NATURAL);
  }

  /**
   * <p>Constructs a new array's comparation strategy.</p>
   *
   * @param componentType Component type of the associated array
   * @param policy Natural order policy
   */

  public ArrayValueComparationStrategy(final Class <? extends Type> componentType, 
                                       final OrderPolicy policy) {
    this(componentType, policy, -1);
  }

  /**
   * <p>Constructs a new array's comparation strategy.</p>
   *
   * @param componentType Component type of the associated array
   * @param policy Natural order policy
   * @param index Evaluation order
   */

  public ArrayValueComparationStrategy(final Class <? extends Type> componentType, 
                                       final OrderPolicy policy, 
                                       final int index) {
    this.componentType = componentType;
    setOrderPolicy(policy);
    setIndex(index);
  }

  /**
   * <p>Compares two arrays.</p>
   *
   * <p>This method implements the algorithm to compare the supplied arrays. The
   * implemented algorithm is based on the <tt>deep contents</tt>
   * of the array.</p>
   *
   * <p>For more info on the comparation algorithm, see 
   * <tt>Arrays.deepEquals()</tt> method.</p>
   *
   * @param  op1 First array
   * @param  op2 Second array
   * @return <tt>true</tt> if the supplied arrays are considered equivalent
   *         <tt>false</tt> otherwise
   */

  public boolean equals(final Type [] op1, final Type [] op2) {
    return Arrays.deepEquals(op1, op2);
  }

  /**
   * <p>Calculates the array hash code.</p>
   *
   * <p>This method implements the algorithm to calculate the supplied array 
   * hash code. The implemented algorithm is based on the <tt>deep contents</tt>
   * of the array.</p>
   *
   * <p>For more info on the hash code calculation algorithm, see 
   * <tt>Arrays.deepHashCode()</tt> method.</p>
   *
   * @param  object Supplied array
   * @return The supplied array hash code
   */

  public int hashCode(final Type [] object) {
    return Arrays.deepHashCode(object);
  }
}