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

import groovy.lang.MetaClass;
import groovy.lang.MetaClassImpl;
import groovy.lang.MetaClassRegistry;
import groovy.lang.MissingFieldException;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;
import java.beans.IntrospectionException;

/**
 * <p>Groovy meta class capable of introspect private methods and private 
 * fields.</p>
 *
 * <p>The groovy meta class provides to groovy expression evaluation environment
 * advanced introspection capabilities. Among these capabilities are the private
 * method invocation and private field value processing.</p>
 * 
 * @author Bruno Sofiato
 */
public class PrivateMemberAwareMetaClass extends MetaClassImpl {
  /**
   * <p>Associated meta class registry.</p>
   */
  private final MetaClassRegistry metaClassRegistry;
  
  /**
   * <p>Gets the associated meta class registry.</p>
   *
   * @return Associated meta class registry
   */
  private MetaClassRegistry getMetaClassRegistry() {
    return this.metaClassRegistry;
  }
  
  /**
   * <p>Gets the associated Java type.</p>
   *
   * @return Associated Java type
   */
  private Class getType() {
    return theClass;
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
   * <p>Constructs a new private member aware meta class.</p>
   *
   * @param  metaClassRegistry Associated meta class registry
   * @param  klass Associated Java type
   * @throws IntrospectionException If there's an error on the supplied Java 
   *                                type introspection process
   */
  public PrivateMemberAwareMetaClass(final MetaClassRegistry metaClassRegistry, 
                                     final Class klass) throws IntrospectionException {
    super(metaClassRegistry, klass);
    this.metaClassRegistry = metaClassRegistry;
    initialize();
  }

  /**
   * <p>Invoke an method from the meta class against the supplied instance.</p>
   *
   * <p>This meta class implementation will resolve method's invocations 
   * regardless to theirs access modifiers.</p>
   *
   * @param  object Instance against the method will be invoked
   * @param  name Method name
   * @param  args Supplied method arguments
   * @return Method invocation return value, or <tt>null</tt> if the invoked 
   *         method is a void method
   */
  public Object invokeMethod(final Object object, final String name, final Object [] args) {
    try {
      return updateMetaClassRegistry(super.invokeMethod(object, name, args));
    } catch (MissingMethodException ex) {
      if (getType().getSuperclass() !=  null) {
        return updateMetaClassRegistry(createMetaClass(getType().getSuperclass()).invokeMethod(object, name, args));
      } 
      throw ex;
    }
  }
  
  /**
   * <p>Fetch a attribute's value from an supplied instance.</p>
   *
   * <p>This meta class implementation will fetch attribute's values regardless 
   * to theirs access modifiers.</p>
   *
   * @param  object Instance from which the attribute value will be fetched
   * @param  name Parameter name
   * @return Attribute's value
   */
  public Object getAttribute(final Object object, final String name) {
    try {
      return updateMetaClassRegistry(super.getAttribute(object, name));
    } catch (MissingFieldException ex) {
      if (getType().getSuperclass() !=  null) {
        return updateMetaClassRegistry(createMetaClass(getType().getSuperclass()).getAttribute(object, name));
      } 
      throw ex;
    }
  }

  /**
   * <p>Fetch a property's value from an supplied instance.</p>
   *
   * <p>If the property's value access via access method (getter method) 
   * fails, then the meta class will try to fecth the property's value via 
   * attribute. (using the <tt>getAttribute()</tt> method). This meta class 
   * implementation will also fetch property's values regardless to theirs 
   * accessor method access modifiers.</p>
   *
   * @param  object Instance from which the attribute value will be fetched
   * @param  name Property name
   * @return Property's value
   */

  public Object getProperty(final Object object, final String name) {
    try {
      return updateMetaClassRegistry(super.getProperty(object, name));
    } catch (MissingPropertyException ex) {
      if (getType().getSuperclass() !=  null) {
        try {
          return updateMetaClassRegistry(createMetaClass(getType().getSuperclass()).getProperty(object, name));
        } catch (MissingFieldException ex2) {
          // Fall through
        }
      }
      return updateMetaClassRegistry(getAttribute(object, name));
    }
  }
}
