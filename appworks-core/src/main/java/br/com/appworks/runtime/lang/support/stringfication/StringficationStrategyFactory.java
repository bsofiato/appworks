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
 * <p>Factory of stringfication strategies.</p>
 *
 * <p>Based on a supplied type, creates a stringfication strategy reflecting the 
 * supplied type stringfication semantics.</p>
 * 
 * @author Bruno Sofiato
 */

public interface StringficationStrategyFactory {
  
  /**
   * <p>Constructs a stringfication strategy reflecting the supplied type 
   * stringfication semantics.</p>
   * 
   * @param  <Type> Parametrized type for the stringfication strategy be 
   *                constructed
   * @param  type Type for the stringfication strategy be constructed
   * @return Stringfication strategy reflecting the supplied type stringfication 
   *         semantics
   */
  
  <Type> StringficationStrategy <Type> create(Class <Type> type);
}
