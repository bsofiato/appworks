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
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Iterator;

/**
 * <p>Class introspection utility class.</p>
 *
 * <p>This utility class defines helper methods used for runtime class
 * introspection and to fetch class metadata.</p>
 *
 * @author Bruno Sofiato
 */

public final class ClassUtils {
  
  /**
   * <p>Compare methods by their signature.</p>
   *
   * <p>The identity of a method is defined as the method's characteristics who 
   * make them unique within a class declaration, These characteristics are it's
   * method, and it's parameter's type.</p>
   *
   * <p>This comparation implementation ignores the method declaring class on 
   * it's comparation algorithm (it's sole based on the method's signature).</p>
   *
   * @author Bruno Sofiato
   */
  private static class MethodComparator implements Comparator <Method>, Serializable {

    /**
     * <p>Compare methods by their signature.</p>
     *
     * @param  op1 First method to compare
     * @param  op2 Second method to compare
     * @return Zero if the operands are equivalent, any other value otherwise
     */
    public int compare(final Method op1, final Method op2) {
      if (!op1.getName().equals(op2.getName())) {
        return op1.getName().compareTo(op2.getName());
      } else if (op1.getParameterTypes().length == op2.getParameterTypes().length) {
        for (int i = 0; i < op1.getParameterTypes().length; i++) {
          if (!op1.getParameterTypes()[i].getName().equals(op2.getParameterTypes()[i].getName())) {
            return op1.getParameterTypes()[i].getName().compareTo(op2.getParameterTypes()[i].getName());
          }
        }
        return 0;
      } else {
        return op1.getParameterTypes().length - op2.getParameterTypes().length;   // Just to mark the methods as different
      }
    }
  }

  /**
   * <p>Compares classes in regards to their's inheritance level.</p>
   *
   * <p>This comparator defines a natural ordering schema based on the
   * inheritance level. Classes with a greater inheritance level are considered
   * to be <tt>lesser</tt> than classes with a lesser inheritance
   * level (Classes with a greater inheritance level should be iterated
   * first).</p>
   *
   * <p>When two classes have the same inheritance level, <tt>their's name's
   * alphabetical order will be used to desambiguate the natural order
   * classification.</tt></p>
   *
   * @author Bruno Sofiato
   */

  private static class ClassInheritanceLevelComparator implements Comparator <Class>, Serializable {

    /**
     * <p>Compares classes in regards to their's inheritance level.</p>
     *
     * @param  op1 First operand
     * @param  op2 Second operand
     * @return A negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     */

    public int compare(final Class op1, final Class op2) {
      int op1Level = getClassInheritanceLevel(op1);
      int op2Level = getClassInheritanceLevel(op2);
      return (op1Level != op2Level) ? op2Level - op1Level : op2.getName().compareTo(op1.getName());
    }
  }

  /**
   * <p>Getter property name offset.</p>
   */
  
  private static final int GETTER_PROPERTY_NAME_OFFSET = 3;
  
  /**
   * <p>Setter property name offset.</p>
   */
  
  private static final int SETTER_PROPERTY_NAME_OFFSET = 3;

  /**
   * <p>Boolean getter property name offset.</p>
   */
  
  private static final int BOOLEAN_GETTER_PROPERTY_NAME_OFFSET = 2;

  
  /**
   * <p>Checks if the supplied method is a getter.</p>
   *
   * <p>This method checking algorithm is based on the JavaBeans naming 
   * convention.</p>
   *
   * @param  method Method to be checked
   * @return <tt>true</tt> if the supplied method is a getter, <tt>false</tt>
   *         otherwise
   */
  private static boolean isGetter(final Method method) {
    return (method.getName().length() > GETTER_PROPERTY_NAME_OFFSET) && method.getName().startsWith("get");
  }

  /**
   * <p>Checks if the supplied method is a setter.</p>
   *
   * <p>This method checking algorithm is based on the JavaBeans naming 
   * convention.</p>
   *
   * @param  method Method to be checked
   * @return <tt>true</tt> if the supplied method is a setter, <tt>false</tt>
   *         otherwise
   */

  private static boolean isSetter(final Method method) {
    return (method.getName().length() > SETTER_PROPERTY_NAME_OFFSET) && method.getName().startsWith("set");
  }

  /**
   * <p>Checks if the supplied method is a boolean type getter.</p>
   *
   * <p>This method checking algorithm is based on the JavaBeans naming 
   * convention.</p>
   *
   * @param  method Method to be checked
   * @return <tt>true</tt> if the supplied method is a boolean type getter, 
   *         <tt>false</tt> otherwise
   */
  private static boolean isBooleanGetter(final Method method) {
    return (method.getName().length() > BOOLEAN_GETTER_PROPERTY_NAME_OFFSET) && method.getName().startsWith("is");
  }

  /**
   * <p>Gets all supplied class fields.</p>
   *
   * <p>This method returns all class fields, these fields may have any visibility
   * modifier. Any fields declared on the supplier class's super classes are
   * returned also.</p>
   *
   * @param  klass Class to get all declared fields
   * @return All supplied class declared fields
   */

  public static Collection <Field> getDeclaredFields(final Class klass) {
    Collection <Field> fields = new ArrayList(Arrays.asList(klass.getDeclaredFields()));
    if (klass.getSuperclass() != null) {
      fields.addAll(getDeclaredFields(klass.getSuperclass()));
    }
    return fields;
  }

  /**
   * <p>Gets a named field from a supplied class.</p>
   *
   * <p>This method returns a class field declared with the supplied name. The
   * field may have any visibility modifier and may be declared on the supplier
   * class's super classes also.<p>
   *
   * @throws NoSuchFieldError Raised when there is no such field declared with
   *                          the supplied name on the supplied class or it's
   *                          super classes
   * @param  klass Class to get a named field
   * @param  name Field name
   * @return Supplied class named field
   */

  public static Field getDeclaredField(final Class klass, final String name) {
    for (Field field : getDeclaredFields(klass)) {
      if (field.getName().equals(name)) {
        return field;
      }
    }
    throw new NoSuchFieldError(name);
  }

  /**
   * <p>Gets all supplied class methods.</p>
   *
   * <p>This method returns all class methods, these methods may have any
   * visibility modifier. Any methods declared on the supplier class's super
   * classes are returned also.</p>
   *
   * @param  klass Class to get all declared methods
   * @return All supplied class declared methods
   */
  public static Collection <Method> getDeclaredMethods(final Class klass) {
    Collection <Method> methods = new TreeSet(new MethodComparator());
    methods.addAll(Arrays.asList(klass.getDeclaredMethods()));
    if (klass.getSuperclass() != null) {
      methods.addAll(getDeclaredMethods(klass.getSuperclass()));
    }
    for (Class interfac : klass.getInterfaces()) {
      methods.addAll(getDeclaredMethods(interfac));
    }
    return methods;
  }

  /**
   * <p>Gets a method from a supplied class.</p>
   *
   * <p>This method returns a class method declared with the supplied name and
   * parameters. The method may have any visibility modifier and may be declared
   * on the supplier class's super classes also.</p>
   *
   * @throws NoSuchMethodError Raised when there is no such method declared
   *                           with the supplied name and parameters on the
   *                           supplied class or it's super classes
   * @param  klass Class to get a method
   * @param  name Method name
   * @param  parametersType Method parameter's type
   * @return Supplied class method
   */

  public static Method getDeclaredMethod(final Class klass, final String name, final Class ... parametersType) {
    for (Method method : getDeclaredMethods(klass)) {
      if (method.getName().equals(name) &&
         (Arrays.deepEquals(method.getParameterTypes(), parametersType))) {
        return method;
      }
    }
    throw new NoSuchMethodError(name);
  }

  /**
   * <p>Gets the inheritance level from a supplied class.</p>
   *
   * <p>The inheritance level is the number of classes between the supplied
   * class and the <tt>Object</tt> class.</p>
   *
   * @param  klass Class to obtain it's inheritance level
   * @return Supplied class inheritance level
   */

  public static int getClassInheritanceLevel(final Class klass) {
    if ((klass != null) && (!(Object.class.equals(klass)))) {
      int level = getClassInheritanceLevel(klass.getSuperclass());
      for (Class interfac : klass.getInterfaces()) {
        level = Math.max(level, getClassInheritanceLevel(interfac));
      }
      return level + 1;
    }
    return 0;
  }

  /**
   * <p>Gets the super classes from a supplied class.</p>
   *
   * <p>The returned super classes are ordered by their inheritance level,
   * <tt>classes with a greater inheritance level should be iterated first
   * than classses with a lesser inheritance level</tt>. Also, interfaces
   * are included on the returned super classes set.</p>
   *
   * @param  klass Class to obtain it's super classes
   * @return Supplied class super classes
   */
  public static Collection<Class> getSuperClasses(final Class klass) {
    Collection<Class> superClasses = new TreeSet<Class>(new ClassInheritanceLevelComparator());
    if ((klass.getSuperclass() != null) &&
        (!(Object.class.equals(klass.getSuperclass())))) {
      superClasses.add(klass.getSuperclass());
      for (Class superClass : getSuperClasses(klass.getSuperclass())) {
        superClasses.add(superClass);
      }
    }
    if (klass.getInterfaces() != null) {
      for (Class interfac : klass.getInterfaces()) {
        superClasses.add(interfac);
        superClasses.addAll(getSuperClasses(interfac));
      }
    }
    return superClasses;
  }

  /**
   * <p>Gets a method from a supplied class.</p>
   *
   * <p>This method returns a class method declared according to the supplied 
   * method template. The method may have any visibility modifier and may be 
   * declared on the supplier class's super classes also.</p>
   *
   * @throws NoSuchMethodError Raised when there is no such method declared
   *                           with the supplied name and parameters on the
   *                           supplied class or it's super classes
   * @param  klass Class to get a method
   * @param  method Method template
   * @return Supplied class method
   */
  
  public static Method getDeclaredMethod(final Class klass, final Method method) {
    return getDeclaredMethod(klass, method.getName(), method.getParameterTypes());
  }

  /**
   * <p>Gets all inherited annotations from a supplied class.</p>
   *
   * <p>This method returns a collection of all inherited annotations from the
   * given class. This collection includes all annotations annotating the 
   * supplied class parents, <tt>even if it's overrided by another 
   * annotation</tt>.</p>
   *
   * <p>This behavior violates the Java specification for inheritable 
   * annotations, where if an class is annotated with a annotation that has the 
   * same type from a inherited annotation, the former must be used instead of 
   * the later.</p>
   *
   * @param  klass Class to gets all inherited annotations
   * @return All inherited annotations from the supplied class
   */
  
  public static Collection <Annotation> getInheritedAnnotations(final Class klass) {
    Collection <Annotation> annotations = new ArrayList<Annotation>(Arrays.asList(klass.getDeclaredAnnotations()));
    for (Class superClass : getSuperClasses(klass)) {
      for (Annotation annotation : superClass.getDeclaredAnnotations()) {
        if (!annotations.contains(annotation)) {
          annotations.add(annotation);
        }
      }
    }
    return annotations;
  }
  
  /**
   * <p>Gets all inherited annotations from a supplied class of a given 
   * type.</p>
   *
   * <p>This method returns a collection of all inherited annotations with 
   * the supplied type from the given class. This collection includes all 
   * annotations annotating the supplied class parents, <tt>even if it's 
   * overrided by another annotation</tt>.</p>
   *
   * <p>This behavior violates the Java specification for inheritable 
   * annotations, where if an class is annotated with a annotation that has the 
   * same type from a inherited annotation, the former must be used instead of 
   * the later.</p>
   *
   * @param  klass Class to gets all inherited annotations of a given type
   * @param  type Type of the to be fetched annotations
   * @param  <Type> Fetched annotations type
   * @return All inherited annotations from the supplied class of a given type
   */
  
  public static <Type extends Annotation> Collection <Type> getInheritedAnnotations(final Class klass, 
                                                                                    final Class <Type> type) {
    Collection <Annotation> annotations = getInheritedAnnotations(klass);
    Iterator <Annotation> i = annotations.iterator();
    while (i.hasNext()) {
      Annotation annotation = i.next();
      if (!(type.isAssignableFrom(annotation.getClass()))) {
        i.remove();
      }
    }
    return (Collection <Type>) annotations;
  }
  
  /**
   * <p>Gets the overrided method chain from a given method, based on a supplied
   * class.</p>
   *
   * <p>This method returns all methods that is overrided by the supplied method 
   * when it's target instance is a instance from the supplied class. </p>
   *
   * <p>All super class or interface's method that have the same signature as 
   * the supplied method are considered to be <tt>overriden</tt> by the supplied 
   * method.</p>
   *
   * @param  klass Target class
   * @param  method Method to get the overrided method chain
   * @return The given method overrided method chain
   */

  public static Collection <Method> getOverridedMethods(final Class klass, 
                                                        final Method method) {
    Collection <Method> overridedMethods = new HashSet<Method>();
    if (klass.getSuperclass() != null) {
      try {
        Method parentMethod = getDeclaredMethod(klass.getSuperclass(), method);
        overridedMethods.add(parentMethod);
        overridedMethods.addAll(getOverridedMethods(klass.getSuperclass(), parentMethod));
      } catch (NoSuchMethodError ex) {
        // Fall Through
      }
    }
    for (Class inteface : klass.getInterfaces()) {
      try {
        Method interfaceMethod = getDeclaredMethod(inteface, method);
        if (interfaceMethod != null) {
          overridedMethods.add(interfaceMethod);
          overridedMethods.addAll(getOverridedMethods(inteface, interfaceMethod));
        }
      } catch (NoSuchMethodError ex) {
        // Fall Through
      }
    }
    return overridedMethods;
  }
  
  /**
   * <p>Gets the inherited annotations from a given method, based on a supplied
   * class.</p>
   *
   * <p>This method return the supplied method inherited annotations. Inherited
   * annotations are annotations that annotates method's that belong to the 
   * supplied method's overrided method chain.</p>
   *
   * <p>This behavior violates the Java specification for inheritable 
   * annotations, that dictactes that method and constructor annotations 
   * <tt>cannot</tt> be inherited, even when they're annotated as 
   * <tt>@Inherited</tt>.</p>
   *
   * @param  klass Target class
   * @param  method Method to get the inherited annotations
   * @return Supplied method inherited annotation
   */
  
  public static Collection <? extends Annotation> getInheritedAnnotations(final Class klass, 
                                                                          final Method method) {
    Collection <Annotation> annotations = new HashSet<Annotation>();
    annotations.addAll(Arrays.asList(method.getDeclaredAnnotations()));
    for (Method overridedMethod : getOverridedMethods(klass, method)) {
      annotations.addAll(Arrays.asList(overridedMethod.getDeclaredAnnotations()));
    }
    return annotations;
  }

  /**
   * <p>Gets the inherited annotations from a given method, based on a supplied
   * class of a given type.</p>
   *
   * <p>This method return the supplied method inherited annotations that are 
   * instance of a given annotation type. Inherited annotations are annotations 
   * that annotates method's that belong to the supplied method's overrided 
   * method chain.</p>
   *
   * <p>This behavior violates the Java specification for inheritable 
   * annotations, that dictactes that method and constructor annotations 
   * <tt>cannot</tt> be inherited, even when they're annotated as 
   * <tt>@Inherited</tt>.</p>
   *
   * @param  klass Target class
   * @param  method Method to get the inherited annotations
   * @param  type Required annotation's type
   * @param  <Type> Required annotation's type
   * @return Supplied method inherited annotation
   */

  public static <Type extends Annotation> Collection <Type> getInheritedAnnotations(final Class klass,  
                                                                                    final Method method, 
                                                                                    final Class <Type> type) {
    Collection <? extends Annotation> annotations = getInheritedAnnotations(klass, method);
    Iterator <? extends Annotation> i = annotations.iterator();
    while (i.hasNext()) {
      Annotation annotation = i.next();
      if (!(type.isAssignableFrom(annotation.getClass()))) {
        i.remove();
      }
    }
    return (Collection <Type>) annotations;
  }
  
  /**
   * <p>Gets the inherited annotation from a supplied type, from a given 
   * class.</p>
   *
   * <p>This method return the most specialized version of the supplied 
   * annotation type for a given class, or <tt>null</tt> if there isn't any.</p>
   *
   * @param  klass Supplied class to fetch for inherited annotations
   * @param  annotationType Required annotation's type
   * @param  <Type> Required annotation's type
   * @return Supplied class inherited annotation
   */
  
  public static <Type extends Annotation> Type getInheritedAnnotation(final Class klass, final Class <Type> annotationType) {
    Collection <Type> annotations = (Collection <Type>) (getInheritedAnnotations(klass, annotationType));
    return (annotations.isEmpty()) ? null : annotations.iterator().next();
  }
  
  /**
   * <p>Return the given acessor method's associated property name.</p> 
   *
   * <p>This method algorithm is based on the JavaBeans naming convention, so 
   * it will assume that the given method follow it's naming covention.</p>
   * 
   * <p>If the given method isn't a property accessor, it's own name is 
   * returned, with an open and close parentheses (<tt>()</tt>) appended to it's
   * end.</p>
   *
   * @param  method Method to fetch the associated property name
   * @return The given method's associated property name, or the given method's 
   *         name with open and close parentheses appended to it's end if it 
   *         isn't a property accessor method
   */
  
  public static String getPropertyName(final Method method) {
    if (isGetter(method) || isSetter(method)) {
      return Character.toLowerCase(method.getName().charAt(GETTER_PROPERTY_NAME_OFFSET)) + method.getName().substring(GETTER_PROPERTY_NAME_OFFSET + 1);
    } else if (isBooleanGetter(method)) {
      return Character.toLowerCase(method.getName().charAt(BOOLEAN_GETTER_PROPERTY_NAME_OFFSET)) + method.getName().substring(BOOLEAN_GETTER_PROPERTY_NAME_OFFSET + 1);
    } 
    return method.getName() + "()";
  }

  /**
   * <p>Gets the inherited annotation from a supplied method, usign a given 
   * type as target.</p>
   *
   * <p>This method return the most specialized version of the supplied 
   * annotation type for a given class, or <tt>null</tt> if there isn't any.</p>
   *
   * <p>This behavior violates the Java specification for inheritable 
   * annotations, that dictactes that method and constructor annotations 
   * <tt>cannot</tt> be inherited, even when they're annotated as 
   * <tt>@Inherited</tt>.</p>
   *
   * @param  klass Target class
   * @param  method Method to get the inherited annotations
   * @param  annotationType Required annotation's type
   * @param  <Type> Required annotation's type
   * @return Supplied method inherited annotation
   */

  public static <Type extends Annotation> Type getInheritedAnnotation(final Class klass, 
                                                                      final Method method, 
                                                                      final Class <Type> annotationType) {
    Collection <Type> annotations = getInheritedAnnotations(klass, method, annotationType);
    return (annotations.isEmpty()) ? null : annotations.iterator().next();
  }

  /**
   * <p>Gets the supplied type property descriptors.</p>
   *
   * <p>This utility method delegates the descriptor fetching processing to the
   * <tt>Introspector</tt> class. All thrown <tt>IntrospectionException</tt> are
   * catched and propagated as an <tt>RuntimeException</tt></p>
   * 
   * @param klass Class from which the property descriptors will be fetched
   * @return Supplied class property descriptors
   */
  public static Collection <PropertyDescriptor> getPropertyDescriptors(final Class klass) {
    try {
      return (klass == null) ? Collections.EMPTY_LIST : Arrays.asList(Introspector.getBeanInfo(klass).getPropertyDescriptors());
    } catch (IntrospectionException ex) {
      throw new RuntimeException(ex);
    }
  }
}
