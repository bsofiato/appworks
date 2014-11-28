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

package br.com.appworks.runtime.lang.support.reflect;

import java.beans.IntrospectionException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * <p>Parameter name fetching strategy.</p>
 *
 * <p>The parameter name fetching strategy encapsulate algorithms to introspect 
 * supplied methods or constructors for parameter's names information, isolating
 * the client code of the parameter name fetching algorithm's complexity and 
 * dependencies.</p>
 * 
 * <p>If the parameter name fetching strategy's algorithm could not instrospect 
 * a supplied method or constructor, a <tt>InstrospectionException</tt> must be 
 * thrown.</p>
 *
 * @author Bruno Sofiato
 */
public interface ParameterNameFetchingStrategy {
  
  /**
   * <p>Gets the supplied method parameter's name.</p>
   *
   * <p>If strategy's parameter name fetching algorithm couldn't instrospect the
   * supplied method, an <tt>InstrospectException</tt> must be thrown.</p>
   *
   * @param  method Method to fetch the parameter's name
   * @return Supplied method parameter's name
   * @throws IntrospectionException If the strategy's algorithm couldn't 
   *                                introspect the supplied method
   */

  List <String> getParameterNames(Method method) throws IntrospectionException;

  /**
   * <p>Gets the supplied constructor parameter's name.</p>
   *
   * <p>If strategy's parameter name fetching algorithm couldn't instrospect the
   * supplied constructor, an <tt>InstrospectException</tt> must be thrown.</p>
   *
   * @param  constructor Constructor to fetch the parameter's name
   * @return Supplied constructor parameter's name
   * @throws IntrospectionException If the strategy's algorithm couldn't 
   *                                introspect the supplied constructor
   */

  List <String> getParameterNames(Constructor constructor) throws IntrospectionException;
}
