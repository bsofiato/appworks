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

package br.com.appworks.runtime.lang.support.cloning;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>Implements a cloning handler.</p>
 *
 * <p>A cloning handler is a special kind of cloning strategy that creates a new
 * object instance based on the object to be clone (mainly be invoking the 
 * <tt>Object.clone()</tt> method with the supplied instance as target) and 
 * process all relevant properties (properties that need themselves a special 
 * cloning process) from that object.</p>
 *
 * @param <Type> Type associated with this cloning handler
 * @author Bruno Sofiato
 */

public class CloningHandler <Type extends Object> implements CloningStrategy <Type> {
  
  /**
   * <p>Associated cloning processors.</p>
   */

  private final Collection <CloningProcessor <Type>> cloningProcessors = new ArrayList<CloningProcessor <Type>>(); 
  
  /**
   * <p>Gets the associated cloning processors.</p>
   *
   * @return Associated cloning processors
   */
  
  private Collection <CloningProcessor <Type>> getCloningProcessors() {
    return cloningProcessors;
  }

  /**
   * <p>Sets the associated cloning processors.</p>
   *
   * @param  cloningProcessors Associated cloning processors
   */

  private void setCloningProcessors(final Collection <CloningProcessor <Type>> cloningProcessors) {
    getCloningProcessors().clear();
    if (cloningProcessors != null) {
      getCloningProcessors().addAll(cloningProcessors);
    }
  }
  
  /**
   * <p>Constructs a cloning handler.</p>
   *
   * <p>The constructed cloning handler has no cloning processors 
   * associated.</p>
   */
  
  public CloningHandler() {
    this(null);
  }
  
  /**
   * <p>Constructs a cloning handler.</p>
   *
   * @param  cloningProcessors Associated cloning processors
   */
  
  public CloningHandler(final Collection <CloningProcessor <Type>> cloningProcessors) {
    setCloningProcessors(cloningProcessors);
  }
  
  /**
   * <p>Process an cloned instance's properties.</p>
   *
   * <p>This method delegates the cloned instance properties processing process
   * to the associated cloning processors.</p>
   *
   * @param  source Cloned instance to be processed
   * @return Processed cloned instance
   * @throws CloneNotSupportedException If there's an error on the clone 
   *                                    property processing process
   */
  public Type clone(final Type source) throws CloneNotSupportedException {
    Type clone = source;
    if (source != null) {
      for (CloningProcessor <Type> processor : getCloningProcessors()) {
        clone = processor.process(clone);
      }
    }
    return clone;
  }
  
  /**
   * <p>Clone a cloning handler.</p>
   *
   * @throws CloneNotSupportedException If there's an error on the cloning
   *                                    handler cloning process
   * @return The cloning handler clone
   */

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
