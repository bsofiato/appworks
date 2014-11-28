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
import java.util.SortedSet;
import java.util.TreeSet;
import br.com.appworks.runtime.lang.OrderPolicy;

/**
 * <p>Implements a comparation handler.</p>
 *
 * <p>A comparation handler is a special kind of comparation strategy that 
 * encapsulates a set of property based comparation strategies that must be used
 * together to implements the associated type comparation semantics.</p>
 *
 * @param  <Type> Type associated with the comparation handler
 * @author Bruno Sofiato
 */
public class ComparationHandler <Type extends Object> implements ComparationStrategy <Type> {

  /**
   * <p>Associated property based comparation strategies.</p>
   *
   * <p>The associated property based comparation strategies iterator order is 
   * based on theirs evaluation order (strategies with a lower evaluation order 
   * are iterated before than strategies with a higher evalutation order).</p>
   */   
  
  private final SortedSet<ComparationStrategy<Type>> comparationStrategies = new TreeSet<ComparationStrategy <Type>>();

  /**
   * <p>Associated type.</p>
   */
  
  private final Class <Type> klass;
  
  /**
   * <p>Gets the associated type.</p>
   *
   * @return Associated type
   */
  
  private Class <Type> getAssociatedClass() {
    return klass;
  }

  /**
   * <p>Gets the associated property based comparation strategies.</p>
   *
   * <p>The associated property based comparation strategies iterator order is 
   * based on theirs evaluation order (strategies with a lower evaluation order 
   * are iterated before than strategies with a higher evalutation order).</p>
   *
   * @return Associated property based comparation strategies
   */
  
  private SortedSet <ComparationStrategy<Type>> getComparationStrategies() {
    return comparationStrategies;
  }
  
  /**
   * <p>Sets the associated property based comparation strategies.</p>
   *
   * @param comparationStrategies Associated property based comparation 
   *                              strategies
   */
  
  private void setComparationStrategies(final SortedSet<ComparationStrategy<Type>> comparationStrategies) {
    getComparationStrategies().clear();
    if (comparationStrategies != null) {
      getComparationStrategies().addAll(comparationStrategies);
    }
  }
  
  /**
   * <p>Constructs a new comparation handler.</p>
   * 
   * @param klass Associated type
   */
  
  public ComparationHandler(final Class <Type> klass) {
    this(klass, null);
  }

  /**
   * <p>Constructs a new comparation handler.</p>
   * 
   * @param klass Associated type
   * @param comparationStrategies Associated property based comparation 
   *                              strategies
   */

  public ComparationHandler(final Class <Type> klass, 
                            final SortedSet<ComparationStrategy<Type>> comparationStrategies) {
    setComparationStrategies(comparationStrategies);
    this.klass = klass;
  }
  
  /**
   * <p>Calculates the supplied object hash code.</p>
   *
   * <p>This method implements the algorithm to calculate the supplied object 
   * hash code. The implemented algorithm is based on the aggregation of the 
   * hash code results obtained from all associated comparation strategies (the 
   * sum of all obtained hash codes is used).</p>
   * 
   * <p>If the supplied object is <tt>null</tt>, the returned hash code will be
   * <tt>zero</tt>.</p>
   *
   * @param  object Supplied object
   * @return The supplied object hash code
   */
  public int hashCode(final Type object) {
    int hashCode = 0;
    if (object != null) {
      for (ComparationStrategy <Type> strategy : getComparationStrategies()) {
        hashCode += strategy.hashCode(object);
      }
    }
    return hashCode;
  }

  /**
   * <p>Compares two supplied objects.</p>
   *
   * <p>This method implements the algorithm to compare two supplied operands.
   * To be considered equivalent, the two supplied operand must be considered 
   * equivalent by all associated comparation strategies, besides that, they 
   * must be an instance of this comparation handler associated type.</p>
   *
   * @param  op1 First operand
   * @param  op2 Second operand
   * @return <tt>true</tt> if the supplied operands are considered equivalent, 
   *         or <tt>false</tt> otherwise
   */

  public boolean equals(final Type op1, final Type op2) {
    if (op1 != op2) {
      if ((!(getAssociatedClass().isInstance(op1))) ||
          (!(getAssociatedClass().isInstance(op2)))) {
        return false;
      } else {
        for (ComparationStrategy <Type> strategy : getComparationStrategies()) {
          if (!(strategy.equals(op1, op2))) {
            return false;
          }
        }
      }
    }
    return true;
  }
  /**
   * <p>Compare two supplied objects.</p>
   *
   * <p>This method implements the algorithm to compare two supplied operands. 
   * The defined algorithm iterates over the associated comparation strategies 
   * (the iteration order is based on the evaluation order of the associated 
   * comparation strategies) obtaining the natural order factor via the 
   * <tt>ComparationStrategy.compare()</tt> method. If the obtained factor is 
   * different from <tt>zero</tt>, the iteration is aborted and the factor is 
   * returned as the natural order factor of the supplied objects. If all 
   * associated comparation strategies is iterated and all obtained factor is 
   * <tt>zero</tt>, then <tt>zero</tt> is returned as the natural order factor 
   * of the supplied objects.</p>
   *
   * @param  op1 First operand
   * @param  op2 Second operand
   * @return A negative integer, zero, or a positive integer as first operand
   *         is less than, equal to, or greater than the second operand
   * @throws NullPointerException If any operand is <tt>null</tt>
   * @throws ClassCastException If any operand isn't an instance of the 
   *                            associated type
   */

  public int compare(final Type op1, final Type op2) {
    if (op1 != op2) {
      if ((op1 == null) || (op2 == null)) {
        throw new NullPointerException();
      } else if ((!(getAssociatedClass().isInstance(op1))) ||
                 (!(getAssociatedClass().isInstance(op2)))) {
        throw new ClassCastException();
      } else {
        for (ComparationStrategy <Type> strategy : getComparationStrategies()) {
          int compare = strategy.compare(op1, op2);
          if (compare != 0) {
            return compare;
          }
        }
      }
    }
    return 0;
  }
    
  /**
   * <p>Sets the natural order policy.</p>
   *
   * <p>This operation is not supported, it's implemented to realize the 
   * <tt>ComparationStrategy</tt> inteface contract. All invocations to this 
   * method will result on the <tt>UnsupportedOperationException</tt> exception 
   * to be throw.</p>
   *
   * @param orderPolicy Natural order policy
   * @throws UnsupportedOperationException If this method is invoked
   */
  
  public void setOrderPolicy(final OrderPolicy orderPolicy) {
    throw new UnsupportedOperationException();
  }

  /**
   * <p>Gets the natural order policy.</p>
   *
   * <p>This operation is not supported, it's implemented to realize the 
   * <tt>ComparationStrategy</tt> inteface contract. All invocations to this 
   * method will result on the <tt>UnsupportedOperationException</tt> exception 
   * to be throw.</p>
   *
   * @return Natural order policy
   * @throws UnsupportedOperationException If this method is invoked
   */
  
  public OrderPolicy getOrderPolicy() {
    throw new UnsupportedOperationException();
  }
  
   /**
   * <p>Clone a comparation handler.</p>
   *
   * <p>This method exists only to realizes the contract defined by the 
   * <tt>ComparationStrategy</tt> interface, any call to this method will
   * result on the <tt>CloneNotSupportedException</tt> being throw.</p>
   *
   * @throws CloneNotSupportedException If this method is invoked
   * @return The comparation handler clone
   */

  public Object clone() throws CloneNotSupportedException {
    super.clone();
    throw new CloneNotSupportedException();
  }
}
