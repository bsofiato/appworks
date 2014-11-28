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

package br.com.appworks.runtime.lang.support.stringfication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * <p>Implements a stringfication handler.</p>
 *
 * <p>A stringfication handler is a special kind of stringfication strategy that 
 * encapsulates a set of property based stringfication strategies and agreggates
 * theirs single property based string representation on a string representation 
 * agreggating string representations from all properties on the supplied 
 * object.</p>
 * 
 * <p>To agreggate the associated strategies string representations on a single 
 * representation, this stringfication handler uses the following pattern : </p> 
 *
 * <blockquote>
 *   <code>[s1, s2, ..., sN]</code>
 * </blockquote>
 *
 * <p>Where s1, s2 and sN are string representations created by the associated 
 * stringfication strategies.</p>
 *
 * @param  <Type> Type associated with the stringfication handler
 * @author Bruno Sofiato
 */


public class StringficationHandler <Type> extends AbstractStringficationStrategy <Type> {
  
  /**
   * <p>Associated stringfication strategies.</p>
   */

  private final Collection <StringficationStrategy <Type>> stringficationStrategies = new ArrayList <StringficationStrategy <Type>>();

  /**
   * <p>Gets the associated stringfication strategies.</p>
   *
   * @return Associated stringfication strategies
   */
  
  private Collection <StringficationStrategy <Type>> getStringficationStrategies() {
    return stringficationStrategies;
  }
  
  /**
   * <p>Modify the associated stringfication strategies.</p>
   *
   * @param stringficationStrategies Associated stringfication strategies
   */

  private void setStringficationStrategies(final Collection <StringficationStrategy <Type>> stringficationStrategies) {
    getStringficationStrategies().clear();
    if (stringficationStrategies != null) {
      getStringficationStrategies().addAll(stringficationStrategies);
    }
  }
  
  /**
   * <p>Constructs a new comparation handler.</p>
   */

  public StringficationHandler() {
    this(null);
  }

  /**
   * <p>Constructs a new comparation handler.</p>
   *
   * @param stringficationStrategies Associated stringfication strategies
   */

  public StringficationHandler(final Collection <StringficationStrategy <Type>> stringficationStrategies) {
    setStringficationStrategies(stringficationStrategies);
  }
  
  /**
   * <p>Gets the supplied object string representation agreggating the 
   * associated stringfication strategies string representation.</p>
   *
   * <p>To agreggate the associated stringfication strategies string 
   * representation on a string representation, this handler iterates over the
   * associated stringfication strategies fetching it's string representations 
   * (these string representation are created against the supplied object) and
   * agreggating them on an unifed string representation of the supplied 
   * object.</p>
   * 
   * @param object Supplied object
   * @param sb StringBuilder to append the string representation from the
   *           supplied instance.
   */
  
  protected void safeToString(final Type object, final StringBuilder sb) {
    if (object != null) {
      sb.append("[");
      Iterator <StringficationStrategy <Type>>  i = getStringficationStrategies().iterator();
      while (i.hasNext()) {
        i.next().toString(object, sb);
        if (i.hasNext()) {
          sb.append(", ");
        }
      }
      sb.append("]");
    }
  }
  
  /**
   * <p>Clones a stringfication handler.</p>
   *
   * <p>This method exists only to realizes the contract defined by the 
   * <tt>StringficationStrategy</tt> interface, any call to this method will
   * result on the <tt>CloneNotSupportedException</tt> being throw.</p>
   *
   * @return A stringfication handler clone
   * @throws CloneNotSupportedException If this method is invoked
   */
  
  public Object clone() throws CloneNotSupportedException {
    super.clone();
    throw new CloneNotSupportedException();
  }
}
