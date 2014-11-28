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

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.Type;
import java.beans.IntrospectionException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * <p>Source code based parameter name fetching strategy.</p>
 *
 * <p>This parameter name fetching strategy implementation parses the Java 
 * source code to fetch a method or constructor parameters names. The parsing 
 * process and model construction is done by the QDox library (for more info 
 * on the QDox library check the <a href="http://qdox.codehaus.org/">
 * QDox WebSite</a>).</p>
 * 
 * <p>This strategy implementation will look for source code on the 
 * <tt>classpath</tt> used to load the associated class. If the source code 
 * isn't avaliable on the <tt>classpath</tt>, or there's an parsing error on the
 * class's source code, a <tt>java.beans.InstrospectionException</tt> is 
 * thrown.</p>
 * 
 * @author Bruno Sofiato
 */

public class SourceCodeParameterNameFetchingStrategy implements ParameterNameFetchingStrategy {
  /**
   * <p>Gets the source code file name from a supplied class.</p>
   * 
   * <p>This method will return the full source code file name from the supplied
   * class. If the class is an annonymous inner class, or if it's a regular 
   * inner class, the enclosing class's source code file name is returned.</p>
   *
   * @param  klass Class to get the source code file name
   * @return Supplied class source code file name
   * @throws IntrospectionException If the supplied class is a annonymous inner 
   *                                class
   */
  private String getSourceFileName(final Class klass) throws IntrospectionException {
    if (klass.getEnclosingMethod() != null) {
      throw new IntrospectionException("Cannot parse annonymous classes");
    } else if (klass.getEnclosingConstructor() != null) {
      throw new IntrospectionException("Cannot parse annonymous classes");
    } else if (klass.getEnclosingClass() != null) {
      return getSourceFileName(klass.getEnclosingClass());
    }
    return klass.getName().replace('.', '/') + ".java";
  }
  
  /**
   * <p>Open's an reader to the supplied class source code.</p>
   * 
   * <p>To open a reader to the supplied class source code, this method get's
   * it's source code file name (via the <tt>getSourceFileName()</tt> method) 
   * and open a reader to the source code file using the supplied class's class
   * loader.</p>
   *
   * @param  klass Class to open an source code reader
   * @return Supplied class source code reader
   * @throws IntrospectionException If the supplied class is a annonymous inner 
   *                                class
   * @throws IOException If there's an error on the source code reader's opening
   *                     process
   */
  private Reader getSourceCodeReader(final Class klass) throws IOException, IntrospectionException {
    String sourceFileName = getSourceFileName(klass);
    ClassLoader classLoader = (klass.getClassLoader() == null) ? ClassLoader.getSystemClassLoader() : klass.getClassLoader();
    InputStream stream = classLoader.getResourceAsStream(sourceFileName);
    if (stream != null) {
      return new InputStreamReader(stream);
    } 
    throw new FileNotFoundException(sourceFileName);
  }
  
  /**
   * <p>Gets the base component type from an supplied array.</p>
   *
   * @param  array Array to get the base component type
   * @return The supplied array's base component type
   */
  private Class getComponentType(final Class array) {
    return (array.isArray()) ? getComponentType(array.getComponentType()) : array; 
  }

  /**
   * <p>Gets dimenstion from an supplied array.</p>
   *
   * @param  array Array to get the dimension
   * @return The supplied array's dimension
   */

  private int getDimension(final Class array) {
    return (!array.isArray()) ? 0 : getDimension(array.getComponentType()) + 1;
  }
  
  /**
   * <p>Convert the supplied parameter types to the QDox type objects.</p>
   * 
   * @param  parameterTypes Parameter types to be converted to the QDox type 
   *                        objects
   * @return Converted parameter types
   */
  private Type [] getParameterTypes(final Class [] parameterTypes) {
    Type [] types = new Type[parameterTypes.length];
    for (int i = 0; i < parameterTypes.length; i++) {
      Class type = parameterTypes[i];
      if (type.isArray()) {
        types[i] = new Type(getComponentType(type).getName(), getDimension(type));
      } else {
        types[i] = new Type(type.getName());
      }
    }
    return types;
  }
  
  /**
   * <p>Fetch the supplied method parameter's name.</p>
   *
   * <p>This method constructs the source code model by parsing the supplied 
   * source code, after creating the source code model, it will iterate over the
   * created source code model, looking for methods that matches the supplied 
   * method name and parameter types. If such method is found, it's parameter's 
   * names are returned.</p>
   *
   * @param  sourceCodeReader Reader to the associated class source code
   * @param  method Name of the method from which the parameter's names will be 
   *                fetched
   * @param  declaringClass Declaring class of the method from which the 
   *                        parameter's names will be fetched
   * @param  parametersType Parameter's type of the method from which the 
   *                        parameter's name will be fetched
   * @return Fetched parameter's names
   */
  
  private List <String> getParameterNames(final Reader sourceCodeReader, 
                                          final String method, 
                                          final Class declaringClass,
                                          final Class [] parametersType)  {
    JavaDocBuilder javaDocBuilder = new JavaDocBuilder();
    javaDocBuilder.addSource(sourceCodeReader);
    JavaClass javaClass = javaDocBuilder.getClassByName(declaringClass.getName());
    JavaMethod javaMethod = javaClass.getMethodBySignature(method, getParameterTypes(parametersType));
    List <String> parameterNames = new ArrayList<String>(parametersType.length);
    for (JavaParameter javaParameter : javaMethod.getParameters()) {
      parameterNames.add(javaParameter.getName());
    }
    return parameterNames;
  }

  /**
   * <p>Fetch the supplied method parameter's name.</p>
   *
   * <p>This method opens the supplied method's declaring class source code, and 
   * delegates the parameter's name fetching process to the 
   * <tt>getParameterNames()</tt> method.</p>
   *
   * @param  method Name of the method from which the parameter's names will be 
   *                fetched
   * @param  declaringClass Declaring class of the method from which the 
   *                        parameter's names will be fetched
   * @param  parametersType Parameter's type of the method from which the 
   *                        parameter's name will be fetched
   * @return Fetched parameter's names
   * @throws IntrospectionException If there's an error on the supplied method 
   *                                parameter's names introspection process
   */
  private List <String> getParameterNames(final String method, 
                                          final Class declaringClass, 
                                          final Class [] parametersType) throws IntrospectionException {
    Reader sourceCodeReader = null;
    try {
      try {
        if (parametersType.length != 0) {
          sourceCodeReader = getSourceCodeReader(declaringClass);
          return getParameterNames(sourceCodeReader, method, declaringClass, parametersType);
        }
        return Collections.EMPTY_LIST;
      } finally {
        if (sourceCodeReader != null) {
          sourceCodeReader.close();
        }
      }
    } catch (IOException ex) {
      throw new IntrospectionException(ex.getMessage());
    }
  }
  
  /**
   * <p>Gets the supplied method parameter's name.</p>
   *
   * <p>This method parses the supplied method declaring class source code to 
   * fetch the supplied method's parameter's names.</p>
   *
   * @param  method Method to fetch the parameter's name
   * @return Supplied method parameter's name
   * @throws IntrospectionException If the there's an error on the supplied 
   *                                method's parameter's name introspection 
   *                                process
   */
  
  public List <String> getParameterNames(final Method method) throws IntrospectionException {
    return getParameterNames(method.getName(), method.getDeclaringClass(), method.getParameterTypes());
  }

  /**
   * <p>Gets the supplied constructor parameter's name.</p>
   *
   * <p>This method parses the supplied constructor declaring class source code 
   * to fetch the supplied constructor's parameter's names.</p>
   *
   * @param  constructor Constructor to fetch the parameter's name
   * @return Supplied constructor parameter's name
   * @throws IntrospectionException If the there's an error on the supplied 
   *                                constructor's parameter's name introspection 
   *                                process
   */

  public List <String> getParameterNames(final Constructor constructor) throws IntrospectionException {
    return getParameterNames(constructor.getDeclaringClass().getSimpleName(), constructor.getDeclaringClass(), constructor.getParameterTypes());
  }
}
