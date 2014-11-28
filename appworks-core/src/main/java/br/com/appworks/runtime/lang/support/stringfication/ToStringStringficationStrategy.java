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
 * <p>Stringfication strategy based on <tt>toString()</tt> method 
 * implementation.</p>
 *  
 * <p>This stringfication strategy creates string representations based on the
 * supplied object <tt>toString()</tt> method implementation. The string 
 * representations created by this strategy are the raw result of the <tt>toString()</tt> 
 * method invocation on the supplied objects.</p>
 *
 * @param  <Type> Type associated with this stringfication strategy
 * @author Bruno Sofiato
 */

public class ToStringStringficationStrategy <Type> extends AbstractStringficationStrategy <Type>{
  
  /**
   * <p>Gets the supplied object's string representation based on it's 
   * <tt>toString()</tt> method implementation.</p>
   *
   * <p>The string representation returned by this strategy contains the 
   * supplied object's <tt>toString()</tt> method invocation raw result. In 
   * fact, the only checking made by this strategy is check if the supplied 
   * object isn't a <tt>null</tt> reference (If it is, a <tt>null</tt> string 
   * representation is returned).</p>
   *
   * @param object Object to obtain the string representation
   * @param sb StringBuilder to append the string representation from the
   *           supplied instance.
   */
  protected void safeToString(final Type object, final StringBuilder sb) {
    sb.append(object.toString());
  }
}
