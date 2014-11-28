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
 * <p>Comparation policies.</p>
 *
 * <p>A comparation policy dictates how the comparation operands will relate 
 * to each other on the comparation process. For more information on how these 
 * policies relates, see their documentation below.</p>
 * 
 * @author Bruno Sofiato
 */

public enum ComparationPolicy {

  /**
   * <p>Identity comparation policy.</p>
   *
   * <p>The identity cloning policy dictates that to be equivalent, two 
   * references must have the <tt>same identity</tt> (must be the 
   * <tt>same instance</tt>).</p>
   */
  IDENTITY,
  
  /**
   * <p>Value comparation policy.</p>
   *
   * <p>The value cloning policy dictates that the <tt>equivalency semantic
   * used must be the equivalency semantics defined on the properties 
   * type</tt> (to be equivalent, the <tt>equals</tt> method call on the 
   * operands must return <tt>true</tt>).</p>
   */
  VALUE;
}
