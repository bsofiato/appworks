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
import groovy.lang.MissingPropertyException;
import java.beans.IntrospectionException;
import java.util.Map;
import org.codehaus.groovy.runtime.InvokerHelper;


/**
 * <p>Groovy expression binding.</p>
 *
 * <p>The expression binding provide acts like a bridge between the groovy 
 * execution environment and the expression evaluation context. All referenced
 * variables and method invocations from within the expression source code is 
 * resolved by the expression biding.</p>
 * 
 * <p>To resolve a referenced variable, the expression binding first look for it
 * among the declared variables associated with. If the supplied variable isn't
 * among the declared variables, the expression binding will look for a property
 * with the supplied variable's name into the instance associated with the 
 * <tt>this</tt> identifier.</p>
 *
 * <p>When a method invocation is supplied to the expression binding resolve it,
 * it's delegated to the instance associated with the <tt>this</tt> 
 * identifier.</p>
 * 
 * @author Bruno Sofiato
 */
public class ExpressionBinding extends Binding {
  
  /**
   * <p>The <tt>this</tt> keyword.</p>
   */
  
  private static final String THIS_IDENTIFIER = "this";

  /**
   * <p>Associated meta class registry.</p>
   */
  
  private final MetaClassRegistry metaClassRegistry;

  /**
   * <p>Instance associated with the <tt>this</tt> identifier.</p>
   */

  private Object thisInstance;
  
  /**
   * <p>Meta class from the instance associated with the <tt>this</tt> 
   * identifier type.</p>
   *
   * <p>The meta class is provides advance introspection capabilities (like 
   * dynamic method invoking), to regular Java classes.</p>
   */
  private MetaClass thisInstanceMetaClass;

  /**
   * <p>Gets the instance associated with the <tt>this</tt> identifier.</p>
   *
   * @return Instance associated with the <tt>this</tt> identifier
   */
  private Object getThisInstance() {
    return thisInstance;
  }
  
  /**
   * <p>Modify the instance associated with the <tt>this</tt> identifier.</p>
   *
   * @param thisInstance Instance associated with the <tt>this</tt> identifier
   */
  private void setThisInstance(final Object thisInstance) {
    this.thisInstance = thisInstance;
  }

  /**
   * <p>Gets the meta class from the instance associated with the <tt>this</tt> 
   * identifier type.</p>
   *
   * @return Meta class from the instance associated with the <tt>this</tt> 
   *         identifier type
   */

  private MetaClass getThisInstanceMetaClass() {
    return thisInstanceMetaClass;
  }

  /**
   * <p>Modify the meta class from the instance associated with the <tt>this</tt> 
   * identifier type.</p>
   *
   * @param thisInstanceMetaClass Meta class from the instance associated with 
   *                              the <tt>this</tt> identifier type
   */

  private void setThisInstanceMetaClass(final MetaClass thisInstanceMetaClass) {
    this.thisInstanceMetaClass = thisInstanceMetaClass;
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
   * <p>Creates a meta class reflecting the supplied type features.</p>
   *
   * @param  klass Supplied type to create a meta class
   * @return A meta class reflecting the supplied type features
   */
  private MetaClass createMetaClass(final Class klass) {
    try {
      return new PrivateMemberAwareMetaClass(getMetaClassRegistry(), klass);
    } catch (IntrospectionException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * <p>Updates the meta class registry to include a supplied object type meta 
   * class.</p>
   *
   * <p>If the supplied object is a instance of <tt>java.lang.Number</tt> or 
   * it's a primitive value, or it's a instance of <tt>java.lang.String</tt> 
   * the meta class registry will not be update (the original groovy meta class
   * will be used then), also, the meta class registry will not be update if the
   * supplied object is a <tt>null</tt> reference.</p>
   *
   * @param  object Object from which the type's meta class will update the meta
   *                class registry              
   * @return The supplied object
   */
  
  private Object updateMetaClassRegistry(final Object object) {
    if (object != null) {
      if ((!(object instanceof Number)) && 
          (!(object instanceof String)) &&
          (!object.getClass().isPrimitive())) {
        getMetaClassRegistry().setMetaClass(object.getClass(), createMetaClass(object.getClass()));
      }
    } 
    return object;
  }
  
  /**
   * <p>Setup the instance associated with the <tt>this</tt> identifier and it's
   * meta class.</p>
   *
   * <p>If there isn't a instance associated with the <tt>this</tt> identifier 
   * (or it's <tt>null</tt>), there will be no meta class associated with 
   * it.</p>
   */
  private void setupThisInstance() {
    setThisInstance(getVariables().get(THIS_IDENTIFIER));
    setThisInstanceMetaClass((getThisInstance() == null) ? null : createMetaClass(getThisInstance().getClass()));
  }
  
  /**
   * <p>Constructs a new groovy expression binding.</p>
   *
   * <p>On the groovy expression binding construction process, the instance 
   * associated with the <tt>this</tt> identifier and it's meta class will be 
   * setted up. (for more info see the <tt>setupThisInstance()</tt> method 
   * documentation)</p>
   *
   * @param context Variables associated with the evaluation context
   * @param metaClassRegistry Associated meta class registry
   */
  public ExpressionBinding(final Map <String, Object> context, 
                           final MetaClassRegistry metaClassRegistry) {
    super(context);
    this.metaClassRegistry = metaClassRegistry;
    setupThisInstance();
  }

  /**
   * <p>Constructs a new groovy expression binding.</p>
   *
   * <p>On the groovy expression binding construction process, the associated 
   * meta class registry will be fetched by the <tt>InvokerHelper</tt> helper 
   * class. (for more info on the <tt>InvokerHelper</tt> class, consult the 
   * groovy documentation)</p>
   *
   * @param context Variables associated with the evaluation context
   */
  
  public ExpressionBinding(final Map <String, Object> context) {
    this(context, InvokerHelper.getMetaRegistry());
  }
  
  /**
   * <p>Invoke an method with supplied arguments.</p>
   *
   * <p>All method invocations are delegated to the instance associated with
   * the <tt>this</tt> identifier meta class, using the instance as target for 
   * the method invocation.</p>
   *
   * @param  name Method name
   * @param  args Method supplied arguments
   * @return Method invocation return value, or <tt>null</tt> if the invoked 
   *         method is a void method
   */
  public Object invokeMethod(final String name, final Object args) {
    return updateMetaClassRegistry(getThisInstanceMetaClass().invokeMethod(getThisInstance(), name, args));
  }
  
  /**
   * <p>Gets the value associated with a variable.</p>
   *
   * <p>If there's a value associated with the supplied variable name, it is 
   * returned, if there isn't, the value associated with the instance associated 
   * with the <tt>this</tt> identifier property is returned.</p>
   *
   * @param  name Variable name
   * @return Value associated with the variable, or the value associated with 
   *         the property from instance associated with the <tt>this</tt> 
   *         identifier
   *
   */
  public Object getVariable(final String name) {
    try {
      return updateMetaClassRegistry(super.getVariable(name));
    } catch (MissingPropertyException ex) {
      return updateMetaClassRegistry(getThisInstanceMetaClass().getProperty(getThisInstance(), name));
    }
  }
  /**
   * <p>Modify the associated variable values.</p>
   *
   * <p>When the associated variable values is modified the instance associated
   * with the <tt>this</tt> identifier and it's meta class will be setted up 
   * again. (for more info see the <tt>setupThisInstance()</tt> method 
   * documentation)</p>

   * @param variables Associated variable values
   */
  public void setVariables(final Map <String, Object> variables) {
    getVariables().clear();
    if (variables != null) {
      getVariables().putAll(variables);
    }
    setupThisInstance();
  }
}
