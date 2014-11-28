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

package br.com.appworks.runtime.lang.support.stringfication.aop;

import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategy;
import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategyFactory;

/**
 * <p>Stringfication aspect.</p>
 *
 * <p>This stringfication aspect provide string representation fetching method
 * to classes annotated with the <tt>@Stringficable</tt> annotations, 
 * implementing those classes stringfication semantics.</p>
 *
 * @author Bruno Sofiato
 */

public aspect StringficationAspect {

  /**
   * <p>Define stringficables types.</p>
   *
   * <p>This interface acts like an tag interface to tag all classes with 
   * defined stringfication semantics.</p>
   *
   * @author Bruno Sofiato
   */

  private static interface AdvisedStringficable {
    
    /**
     * <p>Fetchs a string representation.</p>
     *
     * @return String representation
     */

    String toString();
  }

  /**
   * <p>Stringfication strategy factory associated with the comparation 
   * aspect.</p>
   */

  private static StringficationStrategyFactory stringficationStrategyFactory;
  
  /**
   * <p>Gets the stringfication strategy factory associated with the 
   * stringfication aspect.</p>
   *
   * @return Stringfication strategy factory associated with the stringfication
   *         aspect
   */

  public static StringficationStrategyFactory getStaticStringficationStrategyFactory() {
    return stringficationStrategyFactory;
  }

  /**
   * <p>Gets the stringfication strategy factory associated with the 
   * stringfication aspect.</p>
   *
   * @return Stringfication strategy factory associated with the stringfication
   *         aspect
   */

  public StringficationStrategyFactory getStringficationStrategyFactory() {
    return getStaticStringficationStrategyFactory();
  }

  /**
   * <p>Modify the stringfication strategy factory associated with the 
   * stringfication aspect.</p>
   *
   * @param stringficationStrategyFactory Stringfication strategy factory 
   *                                      associated with the stringfication
   *                                      aspect
   */  

  public static void setStaticStringficationStrategyFactory(final StringficationStrategyFactory stringficationStrategyFactory) {
    StringficationAspect.stringficationStrategyFactory = stringficationStrategyFactory;
  }

  /**
   * <p>Modify the stringfication strategy factory associated with the 
   * stringfication aspect.</p>
   *
   * @param stringficationStrategyFactory Stringfication strategy factory 
   *                                      associated with the stringfication
   *                                      aspect
   */  

  public void setStringficationStrategyFactory(final StringficationStrategyFactory stringficationStrategyFactory) {
    setStaticStringficationStrategyFactory(stringficationStrategyFactory);
  }
  
  /**
   * <p>Declare <tt>AdvisedStringficable</tt> interface to all types annotated 
   * by the <tt>@Stringficable</tt> annotation.</p>
   */

  declare parents : (@br.com.appworks.runtime.lang.Stringficable *) implements AdvisedStringficable;
  
  /**
   * <p>Fetch the string representation.</p>
   *
   * <p>This method is mixed on this aspect advised instance to provide an 
   * implementation to the <tt>AdvisedStringficable.toString()</tt> method. </p>
   *
   * <p>The string representation creation will be carried over by a 
   * stringfication strategy, obtained by the associated stringfication strategy
   * factory.</p>
   *
   * @return String representation
   */

  public String AdvisedStringficable.toString() {
    StringficationStrategy stringficationStrategy = (getStaticStringficationStrategyFactory() == null) ? null : getStaticStringficationStrategyFactory().create(this.getClass());
    if (stringficationStrategy != null) {
      StringBuilder sb = new StringBuilder();
      stringficationStrategy.toString(this, sb);
      return sb.toString();
    }
    throw new IllegalStateException();
  }
}