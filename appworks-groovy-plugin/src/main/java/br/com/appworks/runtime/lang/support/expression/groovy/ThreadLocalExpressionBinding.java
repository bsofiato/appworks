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

package br.com.appworks.runtime.lang.support.expression.groovy;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.groovy.runtime.InvokerHelper;

/**
 * <p>Thread local expression binding.</p>
 *
 * <p>The thread local expression binding wraps an expression binding, 
 * delegating all requests to the wrapped expression binding. The wrapped 
 * expression binding will unique for each thread invoking it (A thread cannot 
 * invoke a wrapped expression binding instance that is invoked by another 
 * thread).</p>
 *
 * @author Bruno Sofiato
 */
public class ThreadLocalExpressionBinding extends Binding {

  /**
   * <p>Expression binding thread local context.</p>
   * 
   * <p>This class provides a thread local expression binding context 
   * implementation. This implementation is responsible to fetch the expression
   * binding associated with the current thread.</p>
   *
   * <p>If there isn't a expression binding associated with the current thread 
   * at the moment of the expression binding fetching process, a new expression 
   * binding is created and associated with the current thread.</p>
   * 
   * @author Bruno Sofiato
   */
  
  private class ThreadLocalExpressionBindingContext extends ThreadLocal <ExpressionBinding> {
    
    /**
     * <p>Creates a new expression binding to be associated with the current 
     * thread.</p>
     *
     * <p>This method is invoked by the <tt>ThreadLocal</tt> base implementation
     * when a there's a fetching request for a expression binding associated 
     * with the current thread and there's none associated.</p>
     *
     * @return A new expression binding to be associated with the current thread
     */
    
    protected final ExpressionBinding initialValue() {
      return new ExpressionBinding(new HashMap<String, Object>(), getMetaClassRegistry());
    }
  }
  
  /**
   * <p>Thread local variable storing the expression binding.</p>
   */
  
  private final ThreadLocal <ExpressionBinding> expressionBindingContext = new ThreadLocalExpressionBindingContext();

  /**
   * <p>Associated meta class registry.</p>
   */
  
  private final MetaClassRegistry metaClassRegistry;

  /**
   * <p>Gets the thread local variable storing the expression binding.</p>
   *
   * @return Thread local variable storing the expression binding
   */
  
  private ThreadLocal <ExpressionBinding> getExpressionBindingContext() {
    return expressionBindingContext;
  }
  
  /**
   * <p>Gets the expression binding associated with the current thread.</p>
   *
   * @return Expression binding associated with the current thread
   */
  
  private ExpressionBinding getThreadLocalExpressionBinding() {
    return getExpressionBindingContext().get();
  }
  
  /**
   * <p>Gets the associated meta class registry.</p>
   *
   * @return Associated meta class registry
   */
  
  private MetaClassRegistry getMetaClassRegistry() {
    return metaClassRegistry;
  }
  
  /**
   * <p>Constructs a new thread local expression binding.</p>
   *
   * <p>On the thread local expression binding construction process, the 
   * associated meta class registry will be fetched by the <tt>InvokerHelper</tt> 
   * helper class. (for more info on the <tt>InvokerHelper</tt> class, consult 
   * the groovy documentation)</p>   
   */
  public ThreadLocalExpressionBinding() {
    this(InvokerHelper.getMetaRegistry());
  }
  
  /**
   * <p>Constructs a new thread local expression binding.</p>
   *
   * @param metaClassRegistry Associated meta class registry
   */
  
  public ThreadLocalExpressionBinding(final MetaClassRegistry metaClassRegistry) {
    this.metaClassRegistry = metaClassRegistry;
  }
  
  /**
   * <p>Modify the associated variable values.</p>
   *
   * <p>This method invocation is delegated to the expression binding associated
   * with the current thread.</p>
   *
   * @param variables Associated variable values
   */
  
  public void setVariables(final Map <String, Object> variables) {
    getThreadLocalExpressionBinding().setVariables(variables);
  }
  
  /**
   * <p>Modify the expression binding meta class.</p>
   *
   * <p>This method invocation is delegated to the expression binding associated
   * with the current thread.</p>
   *
   * @param metaClass Expression binding meta class
   */
  
  public void setMetaClass(final MetaClass metaClass) {
    getThreadLocalExpressionBinding().setMetaClass(metaClass);
  }
  
  /** 
   * <p>Gets the expression binding meta class.</p>
   *
   * <p>This method invocation is delegated to the expression binding associated
   * with the current thread.</p>
   *
   * @return Expression binding meta class
   */
  
  public MetaClass getMetaClass() {
    return getThreadLocalExpressionBinding().getMetaClass();
  }
  
  /**
   * <p>Invoke an method with supplied arguments.</p>
   *
   * <p>This method invocation is delegated to the expression binding associated
   * with the current thread.</p>
   *
   * @param  name Method name
   * @param  args Method supplied arguments
   * @return Method invocation return value, or <tt>null</tt> if the invoked 
   *         method is a void method
   */
  
  public Object invokeMethod(final String name, final Object args) {
    return getThreadLocalExpressionBinding().invokeMethod(name, args);
  }
  
  /**
   * <p>Fetch a property's value from an expression binding.</p>
   *
   * <p>This method invocation is delegated to the expression binding associated
   * with the current thread.</p>
   *
   * @param  property Property name
   * @return Property's value
   */
  
  public Object getProperty(final String property) {
    return getThreadLocalExpressionBinding().getProperty(property);
  }
  
  /**
   * <p>Gets the value associated with a variable.</p>
   *
   * <p>This method invocation is delegated to the expression binding associated
   * with the current thread.</p>
   *
   * @param  name Variable name
   * @return Value associated with the variable
   */

  public Object getVariable(final String name) {
    return getThreadLocalExpressionBinding().getVariable(name);
  }
  
  /**
   * <p>Gets all variables associated with the expression binding.</p>
   *
   * <p>This method invocation is delegated to the expression binding associated
   * with the current thread.</p>
   *
   * @return All variables associated with the expression binding
   */
  
  public Map getVariables() {
    return getThreadLocalExpressionBinding().getVariables();
  }
  
  /**
   * <p>Modify a property's value from an expression binding.</p>
   *
   * <p>This method invocation is delegated to the expression binding associated
   * with the current thread.</p>
   *
   * @param property Property name
   * @param value Property's value
   */
  
  public void setProperty(final String property, final Object value) {
    getThreadLocalExpressionBinding().setProperty(property, value);
  }

  /**
   * <p>Modify the value associated with a variable.</p>
   *
   * <p>This method invocation is delegated to the expression binding associated
   * with the current thread.</p>
   *
   * @param name Variable name
   * @param value Value associated with the variable
   */

  public void setVariable(final String name, final Object value) {
    getThreadLocalExpressionBinding().setVariable(name, value);
  }
}
