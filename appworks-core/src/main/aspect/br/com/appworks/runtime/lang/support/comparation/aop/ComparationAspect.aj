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

package br.com.appworks.runtime.lang.support.comparation.aop;

import br.com.appworks.runtime.lang.support.comparation.ComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.comparation.DefaultComparationStrategyFactory;

/**
 * <p>Comparation aspect.</p>
 *
 * <p>This comparation aspect mix the <tt>Comparable</tt> interface on classes 
 * annotated with the <tt>@Comparable</tt> annotation, and provide 
 * implementations to the Comparable's interface methods ( <tt>Comparable.compareTo()</tt>, 
 * <tt>Comparable.hashCode()</tt> and <tt>Comparable.equals()</tt> ) that 
 * reflects the advised instance comparation semantics.</p>
 *
 * @author Bruno Sofiato
 */

public final aspect ComparationAspect {

  /**
   * <p>Define comparable types.</p>
   *
   * <p>This interface acts like an tag interface to tag all classes with defined 
   * comparation semantics.</p>
   *
   * @param <Type> Advised type 
   * @author Bruno Sofiato
   */
   
  private static interface AdvisedComparable <Type extends Object> extends java.lang.Comparable<Type> {
  }

  /**
   * <p>Comparation strategy factory associated with the comparation aspect.</p>
   */
  
  private static ComparationStrategyFactory comparationStrategyFactory;

  /**
   * <p>Gets the comparation strategy factory associated with the comparation 
   * aspect.</p>
   *
   * @return Comparation strategy factory associated with the comparation aspect
   */
   private static ComparationStrategyFactory getStaticComparationStrategyFactory() {
    if (comparationStrategyFactory == null) {
      comparationStrategyFactory = DefaultComparationStrategyFactory.createDefault();
    }
    return comparationStrategyFactory;
   }

  /**
   * <p>Gets the comparation strategy factory associated with the comparation 
   * aspect.</p>
   *
   * @return Comparation strategy factory associated with the comparation aspect
   */
  
  public ComparationStrategyFactory getComparationStrategyFactory() {
    return getStaticComparationStrategyFactory();
  }

  /**
   * <p>Sets the comparation strategy factory associated with the comparation 
   * aspect.</p>
   *
   * @param comparationStrategyFactory Comparation strategy factory associated 
   *                                   with the comparation aspect
   */
  
  private static void setStaticComparationStrategyFactory(final ComparationStrategyFactory comparationStrategyFactory) {
    ComparationAspect.comparationStrategyFactory = comparationStrategyFactory;
  }
  
  /**
   * <p>Sets the comparation strategy factory associated with the comparation 
   * aspect.</p>
   *
   * @param comparationStrategyFactory Comparation strategy factory associated 
   *                                   with the comparation aspect
   */
  
  public void setComparationStrategyFactory(final ComparationStrategyFactory comparationStrategyFactory) {
    setStaticComparationStrategyFactory(comparationStrategyFactory);
  }
 
  /**
   * <p>Returns the comparation strategy to be used to compare the supplied 
   * operands.</p>
   *
   * <p>The comparation strategy used when a operand is a super type from the 
   * other operand is the comparation strategy associated to the most generic 
   * type.</p>
   *
   * @return Comparation strategy to be used
   * @param  target  Target from the advised operation
   * @param  operand Operand from the advised operation
   */
  
  private static ComparationStrategy <Object> getComparationStrategy(final Object target, 
                                                                     final Object operand) {
    Class klass = target.getClass();
    if (getStaticComparationStrategyFactory() != null) {
      if ((operand != null) && 
          (klass.isAssignableFrom(operand.getClass()))) {
        klass = operand.getClass();
      }
      return getStaticComparationStrategyFactory().create(klass);
    }
    return null;
  }
  
  /**
   * <p>Declare <tt>AdvisedComparable</tt> interface to all types annotated by
   * the <tt>@Comparable</tt> annotation.</p>
   */
 
  declare parents : (@br.com.appworks.runtime.lang.Comparable *) implements AdvisedComparable;
  
  /**
   * <p>Compare two instances for natural ordering.</p>
   *
   * <p>This method is mixed on this aspect advised instance to provide an 
   * implementation to the <tt>AdvisedComparable.compareTo()</tt> method. </p>
   *
   * <p>The natural ordering processing will be carried over by a comparation 
   * strategy, obtained by the associated comparation strategy factory.</p>
   *
   * @param  operand Instance to compare to
   * @return <tt>0 (zero)</tt> if the two instances are considered to be 
   *         equivalent, a number <tt>greater than 0 (zero)</tt> if the supplied 
   *         operand is considered to be greater than target instance, or a 
   *         number <tt>lesser than 0 (zero)</tt> otherwise     
   */

  public int AdvisedComparable.compareTo(final Object operand) {
    ComparationStrategy <Object> comparationStrategy = getComparationStrategy(this, operand);
    if (comparationStrategy != null) {
      return comparationStrategy.compare(this, operand);
    }
    throw new IllegalStateException();
  }

  /**
   * <p>Calculates the advised instance hash code.</p>
   *
   * <p>This method is mixed on this aspect advised instance to provide an 
   * implementation to the <tt>AdvisedComparable.hashCode()</tt> method. </p>
   *
   * @return Advised instance hash code
   */
  
  public int AdvisedComparable.hashCode() {
    ComparationStrategy <Object> comparationStrategy = (getStaticComparationStrategyFactory() == null) ? null : (ComparationStrategy <Object>) (getStaticComparationStrategyFactory().create(this.getClass()));
    if (comparationStrategy != null) {
      return comparationStrategy.hashCode(this);
    }
    throw new IllegalStateException();
  }

  /**
   * <p>Compare two instances for equality.</p>
   *
   * <p>This method is mixed on this aspect advised instance to provide an 
   * implementation to the <tt>AdvisedComparable.equals()</tt> method. </p>
   *
   * @param  object Instance to compare to the target instance
   * @return <tt>true</tt> if the supplied instance are considered equivalent to
   *         the target instance, <tt>false</tt> otherwise
   */

  public boolean AdvisedComparable.equals(final Object operand) {
    ComparationStrategy <Object> comparationStrategy = getComparationStrategy(this, operand);
    if (comparationStrategy != null) {
      return comparationStrategy.equals(this, operand);
    }
    throw new IllegalStateException();
  }
}