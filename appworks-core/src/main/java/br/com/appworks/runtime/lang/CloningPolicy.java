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
 * <p>Cloning policies.</p>
 *
 * <p>A cloning policy dictates how the cloned instance properties will relate 
 * to the original instance properties. For more information on how these 
 * policies relates, see their documentation below.</p>
 * 
 * @author Bruno Sofiato
 */

public enum CloningPolicy {
  /**
   * <p>Shallow cloning policy.</p>
   *
   * <p>The shallow cloning policy dictates that every instance copy must 
   * references, at the creation time, the <tt>same properties 
   * instances</tt> referenced from the original instance.</p>
   */
  SHALLOW,

  /**
   * <p>Deep cloning policy.</p>
   *
   * <p>The deep cloning policy dictates that every instance copy must 
   * references, at the creation time, <tt>different properties instances, 
   * unless they're immutable</tt>.
   */
  DEEP;
}
