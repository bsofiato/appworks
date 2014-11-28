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

/**
 * <p>Cloning process task.</p>
 *
 * <p>A cloning processor encapsulates distinct processes or task that are part
 * of a ordinary class's cloning process. This facilitates code reuse and 
 * minimises code complexity.</p>
 *
 * @param <Type> Type processed by this cloning processor
 * @author Bruno Sofiato
 */

public interface CloningProcessor <Type extends Object> {

  /**
   * <p>Process an supplied clone instance.</p>
   * 
   * @param  object The clone to be processed
   * @return The supplied copy processed
   * @throws CloneNotSupportedException If any error ocurr on the implemented 
   *                                    process
   */
  
  Type process(Type object) throws CloneNotSupportedException;
}
