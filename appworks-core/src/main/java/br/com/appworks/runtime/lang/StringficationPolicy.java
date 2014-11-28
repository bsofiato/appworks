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

package br.com.appworks.runtime.lang;

/**
 * <p>Stringfication policies.</p>
 *
 * <p>A stringfication policy dictates how the stringficable instance properties
 * will relate on it's string representation. For more information on how these 
 * policies relates, see their documentation below.</p>
 * 
 * @author Bruno Sofiato
 */
public enum StringficationPolicy {
  /**
   * <p>Identity stringfication policy.</p>
   *
   * <p>The identity stringfication policy dictates that the string 
   * representation of the associated property must be it's <tt>identity 
   * string representation</tt> (the value returned by the default implementation
   * of the <tt>toString()</tt> method, defined on the <tt>Object</tt> class). 
   * This identity information carries the property value's class name, and it's 
   * internal address.</p>
   *
   * <p>This policy will return the identity string representation regardless of
   * the property value <tt>toString()</tt> implementation.</p>
   */
  IDENTITY,

  /**
   * <p>ToString stringfication policy.</p>
   *
   * <p>The ToString stringfication policy dictates that the string 
   * representation of the associated property must be it's <tt>string 
   * representation</tt> (the value returned when the <tt>toString()</tt> 
   * method is invoked on the instance associated with the given property).</p>
   */

  TOSTRING;
}