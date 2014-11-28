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
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * <p>Parameter name fetching handler.</p>
 *
 * <p>The parameter name fetching handler aggregates a set of parameter name 
 * fetching strategies and use then to introspect method's.</p>
 *
 * <p>The parameter name introspection process is delegated to the first 
 * parameter name fetching strategy associated, if this strategy fails to fetch
 * the supplied method parameter's names, the next strategy of the associated 
 * strategies set are required to carry over with the parameter's name fetching 
 * process. If none of the associated strategies could fetch the supplied method
 * parameter's name, an <tt>IntrospectionException</tt> is thrown (realizing the
 * <tt>ParameterNameFetchingStrategy</tt> contract).</p>
 * 
 * <p>Is <tt>strongly</tt> recommended that the associated parameter name
 * fetching strategies are ordered by the computational cost of their's 
 * parameter's name fetching algorithm. The cheaper the strategy's algorithm 
 * computational cost, sooner the strategy may be tried by the parameter name 
 * fetching handler.</p>
 *
 * <p>All parameter name request are cached on the parameter name fetching 
 * handler. This caching helps to improve performance, as the parameter name 
 * fetching process generally are very crubersome to be executed repeatly.</p>
 * 
 * <p>The parameter name fetching handler is also a parameter name fetching 
 * strategy, and can be included on the strategies set of another parameter name
 * fetching handler.</p>
 * 
 * @author Bruno Sofiato
 */
public class ParameterNameFetchingHandler implements ParameterNameFetchingStrategy {
  
  /**
   * <p>Associated parameter's name cache.</p>
   */
  
  private final Map <Object, List <String>> parameterNameCache = new WeakHashMap<Object, List <String>>();
  
  /**
   * <p>Associated parameter name fetching strategies.</p>
   */
  
  private final Collection <ParameterNameFetchingStrategy> parameterNameFetchingStrategies = new ArrayList<ParameterNameFetchingStrategy>();

  /**
   * <p>Gets the associated parameter's name cache.</p>
   *
   * @return Associated parameter's name cache
   */
  
  private Map <Object, List <String>> getParameterNameCache() {
    return parameterNameCache;
  }

  /**
   * <p>Modify the associated parameter name fetching strategies.</p>
   *
   * @param parameterNameFetchingStrategies Associated parameter's name cache
   */
  private void setParameterNameFetchingStrategies(final Collection <ParameterNameFetchingStrategy> parameterNameFetchingStrategies) {
    getParameterNameFetchingStrategies().clear();
    if (parameterNameFetchingStrategies != null) {
      getParameterNameFetchingStrategies().addAll(parameterNameFetchingStrategies);
    }
  }

  /**
   * <p>Gets the associated parameter name fetching strategies.</p>
   * 
   * @return Associated parameter name fetching strategies
   */
  
  private Collection <ParameterNameFetchingStrategy> getParameterNameFetchingStrategies() {
    return parameterNameFetchingStrategies;
  }

  /**
   * <p>Constructs a new parameter name fetching handler.</p>
   */
  
  public ParameterNameFetchingHandler() {
    this(null);
  }
  /**
   * <p>Constructs a new parameter name fetching handler.</p>
   *
   * @param parameterNameFetchingStrategies Associated parameter name fetching 
   *                                        strategies
   */

  public ParameterNameFetchingHandler(final List <ParameterNameFetchingStrategy> parameterNameFetchingStrategies) {
    setParameterNameFetchingStrategies(parameterNameFetchingStrategies);
  }
  
  /**
   * <p>Gets the supplied method parameter's name.</p>
   *
   * <p>If the supplied method parameter's names are stored on the handler's 
   * cache, the stored names are returned. If it isn't, the supplied method 
   * parameter's names introspection is carried over by the associated parameter
   * name fetching strategies.</p>
   *
   * <p>If none of the associated strategies could instrospect the supplied 
   * method, an <tt>InstrospectException</tt> is thrown.</p>
   *
   * @param  method Method to fetch the parameter's name
   * @return Supplied method parameter's name
   * @throws IntrospectionException If none of the associated parameter name 
   *                                fetching strategies could introspect the 
   *                                supplied method
   */
  public List <String> getParameterNames(final Method method) throws IntrospectionException {
    List <String> parameterNames = getParameterNameCache().get(method);
    if (parameterNames == null) {
      for (ParameterNameFetchingStrategy parameterNameFetchingStrategy : getParameterNameFetchingStrategies()) {
        try {
          parameterNames = parameterNameFetchingStrategy.getParameterNames(method);
          break;
        } catch (IntrospectionException ex) {
          //Fall through
        }
      }
      if (parameterNames == null) {
        throw new IntrospectionException("Could not introspect class :" + method.getDeclaringClass().getName());
      } else {
        getParameterNameCache().put(method, parameterNames);
      }
    }
    return parameterNames;
  }
  
  /**
   * <p>Gets the supplied constructor parameter's name.</p>
   *
   * <p>If the supplied constructor parameter's names are stored on the 
   * handler's cache, the stored names are returned. If it isn't, the supplied 
   * constructor parameter's names introspection is carried over by the 
   * associated parameter name fetching strategies.</p>
   *
   * <p>If none of the associated strategies could instrospect the supplied 
   * constructor, an <tt>InstrospectException</tt> is thrown.</p>
   *
   * @param  constructor Constructor to fetch the parameter's name
   * @return Supplied constructor parameter's name
   * @throws IntrospectionException If none of the associated parameter name 
   *                                fetching strategies could introspect the 
   *                                supplied constructor
   */
  public List <String> getParameterNames(final Constructor constructor) throws IntrospectionException {
    List <String> parameterNames = getParameterNameCache().get(constructor);
    if (parameterNames == null) {
      for (ParameterNameFetchingStrategy parameterNameFetchingStrategy : getParameterNameFetchingStrategies()) {
        try {
          parameterNames = parameterNameFetchingStrategy.getParameterNames(constructor);
          break;
        } catch (IntrospectionException ex) {
          //Fall through
        }
      }
      if (parameterNames == null) {
        throw new IntrospectionException("Could not introspect class :" + constructor.getDeclaringClass().getName());
      } else {
        getParameterNameCache().put(constructor, parameterNames);
      }
    }
    return parameterNames;
  }
}