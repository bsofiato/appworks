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
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 * <p>Bytecode based parameter name fetching strategy.</p>
 *
 * <p>This parameter name fetching strategy implementation uses the debug info
 * on the Java bytecode to fetch a method or constructor parameters names, so 
 * it can only be used if the debug information is kept on the class files.</p>
 *
 * <p>On the Java bytecode specification, the parameter name debug information 
 * is kept on the method implementation, not on the method declaration, because 
 * of that, this paramter name fetching strategy implementation cannot 
 * introspect abstract method's parameter names, or method defined on an 
 * interface.</p>
 *
 * <p>If a parameter name fetching request is made to an instance of the 
 * <tt>ByteCodeParameterNameFetchingStrategy</tt>, and it's associated class 
 * doesn't have the required debug information, or the supplied method is a
 * abstract method, a <tt>java.beans.IntrospectionException</tt> is thrown.</p>
 *
 * @author Bruno Sofiato
 */
public class ByteCodeParameterNameFetchingStrategy implements ParameterNameFetchingStrategy {
  
  /**
   * <p>Constructor identifier.</p>
   */
  
  private static final String CONSTRUCTOR_NAME = "<init>";
  
  /**
   * <p>Method parameter name fetcher visitor.</p>
   *
   * <p>This method visitor iterates over a class's byte code searching for the
   * associated method pattern. If such method is found, it's parameter names 
   * are fetched by the debug information embedded on it's bytecode, and it's 
   * store for futher use.</p>
   *
   * @author Bruno Sofiato
   */
  
  private static final class MethodParameterNameFetcherVisitor extends EmptyVisitor {
    
    /**
     * <p>The <tt>this</tt> reference identifier.</p>
     */
    
    private static final String THIS_REFERENCE_NAME = "this";

    /**
     * <p>Name of the method to be found.</p>
     */
    
    private final String method;
    
    /**
     * <p>Parameter's types of the method to be found.</p>
     */
    
    private final Type [] parameterTypes;
    
    /**
     * <p>Fetched parameter's names.</p>
     */
    
    private final List <String> parameterNames;
    
    /**
     * <p>Gets the name of the method to be found.</p>
     *
     * @return Name of the method to be found
     */
    
    private String getMethod() {
      return method;
    }
    
    /**
     * <p>Gets the parameter's types of the method to be found.</p>
     *
     * @return Parameter's types of the method to be found
     */
    
    private Type [] getParameterTypes() {
      return parameterTypes;
    }
    
    /**
     * <p>Constructs a new method parameter name fetcher visitor.</p>
     *
     * @param method Name of the method to be found
     * @param parameterTypes Parameter's types of the method to be found
     */

    private MethodParameterNameFetcherVisitor(final String method, 
                                              final Class [] parameterTypes) {
      this.method = method;
      this.parameterNames = new ArrayList<String>(parameterTypes.length);
      this.parameterTypes = new Type[parameterTypes.length];
      for (int i = 0; i < parameterTypes.length; i++) {
        this.parameterTypes[i] = Type.getType(parameterTypes[i]);
      }
    }
    
    /**
     * <p>Visits a class's method.</p>
     *
     * <p>This method is invoked to process a method declaration on the bytecode
     * been processed. If the supplied method declaration matches the method 
     * declaration from the method from which the parameter's name must be 
     * fetched, this instance is returned as a method visitor to continue the 
     * parameter name fetching process, otherwise, <tt>null</tt> is returned, 
     * forcing the parameter name fetching process to resume to the next method 
     * declaration.</p>
     *
     * @param  access Method's access flags
     * @param  desc Method's descriptor
     * @param  name Method's name
     * @param  signature Method's signature
     * @param  exceptions Method's exceptions
     * @return This visitor instance if the supplied method declaration matches 
     *         the required method declaration, <tt>null</tt> otherwise
     * 
     */
    
    public MethodVisitor visitMethod(final int access, 
                                     final String name, 
                                     final String desc, 
                                     final String signature, 
                                     final String[] exceptions) { 
      if (getMethod().equals(name)) {
        if (Arrays.equals(getParameterTypes(), Type.getArgumentTypes(desc))) {
          return this;
        }
      }
      return null;
    }
    
    /**
     * <p>Visits a local variable's declaration.</p>
     * 
     * <p>At the bytecode level, all parameters (also the <tt>this</tt> 
     * reference) are associated, prior to the execution of any method's 
     * instruction, to local variables. If the embedded bytecode's debug 
     * information was kept, this local variables will have the same identifiers
     * as it's associated parameters. This method is invoked to process these
     * local variables declarations, storing their identifiers as parameters 
     * names for futher use.</p>
     * 
     * @param name Local variable's name
     * @param desc Local variable's type descriptor
     * @param signature Local variable's signature
     * @param start Instruction corresponding to first instruction on the local 
     *              variable's scope (inclusive)
     * @param end Instruction corresponding to last instruction on the local 
     *            variable's scope (exclusive)
     * @param index Local variable's index
     */
    public void visitLocalVariable(final String name, 
                                   final String desc, 
                                   final String signature, 
                                   final Label start, 
                                   final Label end, 
                                   final int index) {
      if ((!THIS_REFERENCE_NAME.equals(name)) && 
          (getParameterNames().size() < getParameterTypes().length)) {
        getParameterNames().add(name);
      }
    }
    
    /**
     * <p>Gets the fetched parameter's names.</p>
     *
     * @return Fetched parameter's names
     */
    
    public List <String> getParameterNames() {
      return parameterNames;
    }
  }
  
  /**
   * <p>Open a stream to the supplied class's bytecode.</p>
   *
   * @param  klass Class from which a stream to bytecode will be open
   * @return Stream to the supplied class bytecode
   * @throws IOException If there's an error on the supplied class bytecode 
   *                     stream opening process
   */
  
  private InputStream getByteCodeStream(final Class klass) throws IOException {
    String classResource = klass.getName().replace('.', '/') + ".class";
    ClassLoader classLoader = (klass.getClassLoader() == null) ? ClassLoader.getSystemClassLoader() : klass.getClassLoader();
    return classLoader.getResourceAsStream(classResource);
  }
  
  /**
   * <p>Iterates over the supplied bytecode stream and fetch the parameter's 
   * names associated with the supplied method declaration.</p>
   *
   * <p>This method iterates over the supplied bytecode using a 
   * <tt>MethodParameterNameFetcherVisitor</tt>, that searches the bytecode for
   * the supplied method declaration pattern and fetches the parameter's names 
   * from it.</p>
   *
   * @param  byteCodeStream Stream to the bytecode to be iterated
   * @param  method Method's name
   * @param  parameterTypes Method's parameter's types
   * @return Fetched method's parameter names
   * @throws IOException If there's an error on the bytecode stream reading 
   *                     process
   * @throws IntrospectionException If there's an error on the supplied method
   *                                parameter's name instrospection process
   */

  private List <String> getParameterNames(final InputStream byteCodeStream, 
                                          final String method, 
                                          final Class [] parameterTypes) throws IntrospectionException, IOException {
    ClassReader reader = new ClassReader(byteCodeStream);
    MethodParameterNameFetcherVisitor visitor = new MethodParameterNameFetcherVisitor(method, parameterTypes);
    reader.accept(visitor, 0);
    if (visitor.getParameterNames().size() == parameterTypes.length) {
      return visitor.getParameterNames();
    }
    throw new IntrospectionException("Resource Not Found");
  }
  
  /**
   * <p>Gets the supplied method parameter names.</p>
   *
   * <p>This method opens a stream to the method's declaring class's bytecode 
   * and delegates the parameter name fetching processing.</p>
   *
   * @param  method Method's name
   * @param  declaringClass Method's declaring class
   * @param  parameterTypes Method's parameter types
   * @return Fetched method's parameter names
   * @throws IntrospectionException If there's an error on the supplied method
   *                                parameter's name instrospection process
   */
  
  private List <String> getParameterNames(final String method, 
                                          final Class declaringClass, 
                                          final Class [] parameterTypes) throws IntrospectionException {
    try {
      InputStream byteCodeStream = null;
      try {
        if (parameterTypes.length != 0) {
          if (!declaringClass.isInterface()) {
            byteCodeStream = getByteCodeStream(declaringClass);
            return getParameterNames(byteCodeStream, method, parameterTypes);
          }
          throw new IntrospectionException("Resource Not Found");
        } 
        return Collections.EMPTY_LIST;
      } finally {
        if (byteCodeStream != null) {
          byteCodeStream.close();
        }
      }
    } catch (IOException ex) {
      throw new IntrospectionException(ex.getMessage());
    }    
  }
  
  /**
   * <p>Gets the supplied method parameter's names.</p>
   *
   * <p>This method iterates over the supplied method declaring class's bytecode
   * to fetch the supplied method's parameter's names.</p>
   * 
   * @param  method Method from which the parameter's names will be fetched
   * @return Supplied method parameter's names
   * @throws IntrospectionException If there's an error on the supplied method 
   *                                parameter's names instrospection process
   */
  
  public List <String> getParameterNames(final Method method) throws IntrospectionException {
    return getParameterNames(method.getName(), method.getDeclaringClass(), method.getParameterTypes());
  }

  /**
   * <p>Gets the supplied constructor parameter's names.</p>
   *
   * <p>This method iterates over the supplied constructor declaring class's 
   * bytecode to fetch the supplied constructor parameter's names.</p>
   * 
   * @param  constructor Constructor from which the parameter's names will be 
   *                     fetched
   * @return Supplied constructor parameter's names
   * @throws IntrospectionException If there's an error on the supplied 
   *                                constructor parameter's names instrospection 
   *                                process
   */

  public List <String> getParameterNames(final Constructor constructor) throws IntrospectionException {
    return getParameterNames(CONSTRUCTOR_NAME, constructor.getDeclaringClass(), constructor.getParameterTypes());
  }
}
