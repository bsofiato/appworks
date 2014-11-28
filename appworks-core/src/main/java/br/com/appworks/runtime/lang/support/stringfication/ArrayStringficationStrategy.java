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

import java.util.Arrays;

/**
 * <p>Stringfication strategy for arrays.</p>
 *
 * <p>The array stringfication strategy is a high specialized stringfication
 * strategy that deal with arrays based on theirs <tt>deep content</tt>.</p>
 *
 * @param  <Type> Component type of the array associated with this 
 *                stringfication strategy
 * @author Bruno Sofiato
 */
public class ArrayStringficationStrategy <Type> extends AbstractStringficationStrategy <Type []>{

  /**
   * <p>Gets the supplied array string representation.</p>
   *
   * <p>The string representation returned by this strategy implementation is 
   * based on the supplied array item's string representation.</p>
   *
   * <p>For more info on the array's string representation creation algorithm, 
   * consult the <tt>Arrays.deepToString()</tt> method documentation.</p>
   *
   * @param object Array to obtain the string representation
   * @param sb StringBuilder to append the string representation from the
   *           supplied instance.
   */

  protected void safeToString(final Type [] object, final StringBuilder sb) {
    if (object != null) {
      sb.append(Arrays.deepToString(object));
    }
  }
}
