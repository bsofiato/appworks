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

package br.com.appworks.runtime.lang.support.cloning.aop;

import br.com.appworks.runtime.lang.support.cloning.Cloneable;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategyFactory;
import java.lang.reflect.Method;

/**
 * <p>Cloning aspect.</p>
 *
 * <p>This cloning aspect mix the <tt>Cloneable</tt> interface on classes 
 * annotated with the <tt>@Cloenable</tt> annotation, and advised thoses classes
 * <tt>clone()</tt> method execution, to process the obtained clone properties 
 * to reflect the annotated cloning semantics.</p> 
 *
 * @author Bruno Sofiato
 */

public privileged aspect CloningAspect {
  
  /**
   * <p>Cloning aspect advised classes tag interface.</p>
   *
   * <p>This interface tags types advised by the cloning aspect 
   * advices.</p>
   *
   * @param <Type> Advised type 
   * @author Bruno Sofiato
   */  
  private static interface AdvisedCloneable <Type> extends Cloneable <Type> {
  }

  /**
   * <p>Cloning strategy factory associated with the cloning aspect.</p>
   */
  
  private static CloningStrategyFactory cloningStrategyFactory;
  
  /**
   * <p>Gets the cloning strategy factory associated with the cloning 
   * aspect.</p>
   *
   * @return Cloning strategy factory associated with the cloning aspect
   */
  
  public static CloningStrategyFactory getStaticCloningStrategyFactory() {
    return cloningStrategyFactory;
  }
  
  /**
   * <p>Sets the cloning strategy factory associated with the cloning 
   * aspect.</p>
   *
   * @param cloningStrategyFactory Cloning strategy factory associated with the 
   *                               cloning aspect
   */
  
  public static void setStaticCloningStrategyFactory(final CloningStrategyFactory cloningStrategyFactory) {
    CloningAspect.cloningStrategyFactory = cloningStrategyFactory;
  }

  /**
   * <p>Gets the cloning strategy factory associated with the cloning 
   * aspect.</p>
   *
   * @return Cloning strategy factory associated with the cloning aspect
   */
  
  public CloningStrategyFactory getCloningStrategyFactory() {
    return getStaticCloningStrategyFactory();
  }
  
  /**
   * <p>Sets the cloning strategy factory associated with the cloning 
   * aspect.</p>
   *
   * @param cloningStrategyFactory Cloning strategy factory associated with the 
   *                               cloning aspect
   */
  
  public void setCloningStrategyFactory(final CloningStrategyFactory cloningStrategyFactory) {
    setStaticCloningStrategyFactory(cloningStrategyFactory);
  }
  
  /**
   * <p>Declare <tt>Cloneable</tt> interface to all types annotated by the 
   * <tt>@Cloneable</tt> annotation.</p>
   */
 
  declare parents : (@br.com.appworks.runtime.lang.Cloneable *) implements AdvisedCloneable;

  /**
   * <p>Advises the <tt>clone()</tt> method execution, processing the returned
   * clone properties to reflect the advised instance clone semantics.</p>
   *
   * @return Advised instance cloned instance
   * @throws CloneNotSupporedException If there's an error on the advised 
   *                                   instance cloning process
   */
  
  Object around() throws CloneNotSupportedException : (execution(* AdvisedCloneable+.clone(..))) {
    Object clone = proceed();
    CloningStrategy cloningStrategy = (getStaticCloningStrategyFactory() == null) ? null : getStaticCloningStrategyFactory().create(clone.getClass());
    if (cloningStrategy != null) {
      return cloningStrategy.clone(clone);
    }
    throw new CloneNotSupportedException();
  }
}