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
import java.io.Serializable;
import java.util.Comparator;

/**
 * <p>Defines a comparation strategy.</p>
 * 
 * <p>A comparation strategy responsibility is to encapsulate a set of comparation 
 * processing semantics.</p>
 * 
 * <p>A comparation strategy implementation must realize an pre-defined contract 
 * ditacting how it's <tt>Object.hashCode()</tt>, <tt>Object.equals()</tt> and
 * <tt>Object.compareTo()</tt> implementations correlates. To more info on these
 * contracts, see <tt>Object.hashCode()</tt>, <tt>Object.equals()</tt> and 
 * <tt>Comparable</tt> documentation.</p>
 *
 * @see java.lang.Object#hashCode()
 * @see java.lang.Object#equals(Object)
 * @see java.lang.Comparable
 * @param  <Type> Type associated with this comparation strategy
 * @author Bruno Sofiato
 */

public interface ComparationStrategy <Type extends Object> extends Comparator<Type>, Cloneable, Serializable {
 
  /**
   * <p>Compares two supplied objects.</p>
   *
   * <p>The comparation process must realizes the contract dictating how 
   * <tt>Object.hashCode</tt> and <tt>Object.equals()</tt> relates to each 
   * other, and the contract dictating the <tt>Object.equals()</tt> the 
   * <tt>Comparable.compareTo()</tt> relationship.</p>
   *
   * @param  op1 First operand
   * @param  op2 Second operand
   * @return <tt>true</tt> if the supplied operands are considered equivalent, 
   *         or <tt>false</tt> otherwise
   */

  boolean equals(Type op1, Type op2);
    
  /**
   * <p>Calculates the supplied object hash code.</p>
   *
   * <p>The hash code calculating process must realizes the contract dictating 
   * how <tt>Object.hashCode</tt> and <tt>Object.equals()</tt> relates to each
   * other.</p>
   *
   * <p>If the supplied object is <tt>null</tt>, the returned hash code must be
   * <tt>zero</tt>.</p>
   *
   * @param  object Supplied object
   * @return The supplied object hash code
   */
  
  int hashCode(Type object);
  
  /**
   * <p>Gets the natural order policy.</p>
   *
   * @return Natural order policy
   */

  OrderPolicy getOrderPolicy();

  /**
   * <p>Sets the natural order policy.</p>
   *
   * @param orderPolicy Natural order policy
   */

  void setOrderPolicy(OrderPolicy orderPolicy);
  
  /**
   * <p>Clones a comparation strategy.</p>
   *
   * <p>A comparation strategy instance may be clone if it's used as an
   * high specialized comparation strategy mapped to an type.</p>
   *
   * @throws CloneNotSupportedException If there's an error on the comparation 
   *                                    strategy cloning process
   * @return The comparation strategy clone
   */

  Object clone() throws CloneNotSupportedException;
}
