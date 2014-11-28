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

package br.com.appworks.runtime.lang.support.comparation;

/**
 * <p>Comparation strategy factory.</p>
 * 
 * <p>The comparation strategy factory is resposible for constructing 
 * comparation strategies that model the comparation semantics from a given 
 * class. </p>
 * 
 * <p>There are no constraints in regards of the comparation strategy creation
 * scheme or mechanism, the only real constraint is that the returned 
 * comparation strategy must perfectly model the supplied class comparation 
 * semantics.</p>
 *
 * @author Bruno Sofiato
 */

public interface ComparationStrategyFactory {
  
  /**
   * <p>Creates an comparation strategy.</p>
   *
   * <p>The created comparation strategy must model the supplied class 
   * comparation semantics.</p>
   * 
   * @param  klass Supplied class
   * @param  <Type> Parametrized type for the cloning strategy be constructed
   * @return Comparation strategy implementing the supplied class comparation 
   *         semantics
   */
  
  <Type> ComparationStrategy <Type> create(Class <Type> klass);
}
