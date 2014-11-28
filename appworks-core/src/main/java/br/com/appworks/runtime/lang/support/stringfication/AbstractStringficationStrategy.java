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

/**
 * <p>Abstract stringfication strategy.</p>
 *
 * <p>This classe defines a basic stringfication strategy implementation which
 * may be used to build new stringfication strategies. This implementation
 * defines an <tt>safeToString</tt> method, which expect a non-null object (the
 * null verification is done on the <tt>toString</tt> method defined on this
 * class).</p>
 *
 * @author Bruno Sofiato
 * @param <Type> Type associated with this stringfication strategy
 */
public abstract class AbstractStringficationStrategy <Type> implements StringficationStrategy <Type> {

  /**
   * <p>Create a string representation from an supplied instance.</p>
   *
   * <p>This method should be overriden by any classes which extends the
   * <tt>AbstractStringficationStrategy</tt>. These implementations should
   * expect that this method is only invoked when the <tt>object</tt> parameters
   * is not null</p>
   *
   * @param object Object from which the string representation will be created
   * @param sb StringBuilder to append the string representation from the
   *           supplied instance.
   */
  protected abstract void safeToString(Type object, StringBuilder sb);

  /**
   * <p>Create a string representation from an supplied instance.</p>
   *
   * <p>If the supplied object isn't null, this method will delegate the string
   * representation creation to the <tt>safeToString</tt> method, which must be
   * overriden by all classes extending this class. Otherwise, this method will
   * append the <tt>null</tt> constant to the supplied <tt>StringBuilder</tt>.</p>
   *
   * @param object Object from which the string representation will be created
   * @param sb StringBuilder to append the string representation from the
   *           supplied instance.
   */
  public final void toString(final Type object, final StringBuilder sb) {
    if (object != null) {
      safeToString(object, sb);
    } else {
      sb.append((Type) null);
    }
  }

  /**
   * <p>Clones a stringfication strategy.</p>
   *
   * @throws CloneNotSupportedException If there's an error on the
   *                                    stringfication strategy cloning process
   * @return The stringfication strategy clone
   */
  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
