/*
 * ClassUtilsTest.java
 * JUnit based test
 *
 * Created on 15 de Junho de 2005, 02:35
 */

package br.com.appworks.runtime.lang.support.reflect.test;

import junit.framework.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import br.com.appworks.runtime.lang.support.reflect.ClassUtils;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

/**
 *
 * @author Bubu
 */
public class ClassUtilsTest extends TestCase {
  public static class MockBean {
    public String getX() {
      return null;
    }
    public void setX(String x) {
    }
  }
  public static class PropertyNameTestBean {
    public void get() {
    }
    public void set() {
    }
    public void is() {
    }
    public void xxx() {
    }
    public void getX() {
    }
    public void setX() {
    }
    public void isX() {
    }
  }
  
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD, ElementType.TYPE})
  @Inherited
  public @interface TestAnnotation {
    String value();
  }
  
  @TestAnnotation("Interface")
  public static interface Interface extends Serializable {
    void publicInterface();
    void publicInterface(String arg);
    @TestAnnotation("Interface")
    void annotatedMethod();
    @TestAnnotation("Interface")
    void annotatedInterfaceMethod();
    void annotatedBaseClassMethod();
  }
  
  public abstract static class InterfaceChild implements Interface {
  }

  public static interface Interface2 extends Serializable {
    @TestAnnotation("Interface2")
    void annotatedMethod();
  }

  @TestAnnotation("BaseClass")
  public static class BaseClass implements Interface {
    private String privateBase;
    protected String protectedBase;
    String packageBase;
    public String publicBase;
    private void privateBase() {
    }
    protected void protectedBase() {
    }
    void packageBase() {
    }
    public void publicBase() {
    }
    public void publicInterface() {
    }
    public void publicInterface(String arg) {
    }
    @TestAnnotation("BaseClass")
    public void annotatedMethod() {
    }
    public void annotatedInterfaceMethod() {
    }
    @TestAnnotation("BaseClass")
    public void annotatedBaseClassMethod() {
    }
  }
  
  public static class ChildClass2 extends BaseClass {
  }
  public static class ChildClass3 extends ChildClass implements Interface2 {
  }
  @TestAnnotation("ChildClass")
  public static class ChildClass extends BaseClass {
    private String privateChild;
    protected String protectedChild;
    String packageChild;
    public String publicChild;
    private void privateChild() {
    }
    protected void protectedChild() {
    }
    void packageChild() {
    }
    public void publicChild() {
    }
    public void publicChildWithArgs(String string) {
    }
    @TestAnnotation("ChildClass")
    public void annotatedMethod() {
    }
    public void annotatedInterfaceMethod() {
    }
    public void annotatedBaseClassMethod() {
    }
  }
  
  public ClassUtilsTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }

  public void testBaseClassGetDeclaredFields() {
    Collection <Field> fields = ClassUtils.getDeclaredFields(BaseClass.class);
    assertEquals(4, fields.size());
    for (Field field : fields) {
      if (field.getName().equals("privateBase")) {
        continue;
      }
      if (field.getName().equals("protectedBase")) {
        continue;
      }
      if (field.getName().equals("packageBase")) {
        continue;
      }
      if (field.getName().equals("publicBase")) {
        continue;
      }
      fail();
    }
  }

  public void testChildClassGetDeclaredFields() {
    Collection <Field> fields = ClassUtils.getDeclaredFields(ChildClass.class);
    assertEquals(8, fields.size());
    for (Field field : fields) {
      if (field.getName().equals("privateBase")) {
        continue;
      }
      if (field.getName().equals("protectedBase")) {
        continue;
      }
      if (field.getName().equals("packageBase")) {
        continue;
      }
      if (field.getName().equals("publicBase")) {
        continue;
      }
      if (field.getName().equals("privateChild")) {
        continue;
      }
      if (field.getName().equals("protectedChild")) {
        continue;
      }
      if (field.getName().equals("packageChild")) {
        continue;
      }
      if (field.getName().equals("publicChild")) {
        continue;
      }
      fail();
    }
  }

  public void testBaseClassGetDeclaredFieldsByName() {
    assertEquals("privateBase", ClassUtils.getDeclaredField(BaseClass.class, "privateBase").getName());
    assertEquals("protectedBase", ClassUtils.getDeclaredField(BaseClass.class, "protectedBase").getName());
    assertEquals("packageBase", ClassUtils.getDeclaredField(BaseClass.class, "packageBase").getName());
    assertEquals("publicBase", ClassUtils.getDeclaredField(BaseClass.class, "publicBase").getName());
    try {
      ClassUtils.getDeclaredField(BaseClass.class, "NONFIELD");
      fail();
    } catch (NoSuchFieldError ex) {
      assertEquals("NONFIELD", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testChildClassGetDeclaredFieldsByName() {
    assertEquals("privateBase", ClassUtils.getDeclaredField(ChildClass.class, "privateBase").getName());
    assertEquals("protectedBase", ClassUtils.getDeclaredField(ChildClass.class, "protectedBase").getName());
    assertEquals("packageBase", ClassUtils.getDeclaredField(ChildClass.class, "packageBase").getName());
    assertEquals("publicBase", ClassUtils.getDeclaredField(ChildClass.class, "publicBase").getName());
    assertEquals("privateChild", ClassUtils.getDeclaredField(ChildClass.class, "privateChild").getName());
    assertEquals("protectedChild", ClassUtils.getDeclaredField(ChildClass.class, "protectedChild").getName());
    assertEquals("packageChild", ClassUtils.getDeclaredField(ChildClass.class, "packageChild").getName());
    assertEquals("publicChild", ClassUtils.getDeclaredField(ChildClass.class, "publicChild").getName());
    try {
      ClassUtils.getDeclaredField(ChildClass.class, "NONFIELD");
      fail();
    } catch (NoSuchFieldError ex) {
      assertEquals("NONFIELD", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testInterfaceGetDeclaredMethods() {
    Collection <Method> methods = ClassUtils.getDeclaredMethods(Interface.class);
    assertEquals(5, methods.size());
    for (Method method : methods) {
      if (method.getName().equals("publicInterface")) {
        continue;
      }
      if (method.getName().equals("annotatedMethod")) {
        continue;
      }
      if (method.getName().equals("annotatedInterfaceMethod")) {
        continue;
      }
      if (method.getName().equals("annotatedBaseClassMethod")) {
        continue;
      }
      fail();
    }
  }
  public void testBaseClassGetDeclaredMethods() {
    Collection <Method> methods = ClassUtils.getDeclaredMethods(BaseClass.class);
    for (Method method : methods) {
      if (method.getName().equals("publicInterface")) {
        continue;
      }
      if (method.getName().equals("privateBase")) {
        continue;
      }
      if (method.getName().equals("protectedBase")) {
        continue;
      }
      if (method.getName().equals("packageBase")) {
        continue;
      }
      if (method.getName().equals("publicBase")) {
        continue;
      }
      if (method.getName().equals("annotatedMethod")) {
        continue;
      }
      if (method.getName().equals("annotatedInterfaceMethod")) {
        continue;
      }
      if (method.getName().equals("annotatedBaseClassMethod")) {
        continue;
      }
      if (method.getDeclaringClass() != Object.class) {
        fail();
      }
    }
    assertEquals(21, methods.size());
  }
 
  public void testChildClassGetDeclaredMethods() {
    Collection <Method> methods = ClassUtils.getDeclaredMethods(ChildClass.class);
    assertEquals(26, methods.size());
    for (Method method : methods) {
      if (method.getName().equals("publicInterface")) {
        continue;
      }
      if (method.getName().equals("privateBase")) {
        continue;
      }
      if (method.getName().equals("protectedBase")) {
        continue;
      }
      if (method.getName().equals("packageBase")) {
        continue;
      }
      if (method.getName().equals("publicBase")) {
        continue;
      }
      if (method.getName().equals("privateChild")) {
        continue;
      }
      if (method.getName().equals("protectedChild")) {
        continue;
      }
      if (method.getName().equals("packageChild")) {
        continue;
      }
      if (method.getName().equals("publicChild")) {
        continue;
      }
      if (method.getName().equals("publicChildWithArgs")) {
        continue;
      }
      if (method.getName().equals("annotatedMethod")) {
        continue;
      }
      if (method.getName().equals("annotatedInterfaceMethod")) {
        continue;
      }
      if (method.getName().equals("annotatedBaseClassMethod")) {
        continue;
      }
      if (method.getDeclaringClass() != Object.class) {
        fail();
      }

    }
  }

  public void testInterfaceGetDeclaredMethodsByName() {
    assertEquals("publicInterface", ClassUtils.getDeclaredMethod(Interface.class, "publicInterface").getName());
    try {
      ClassUtils.getDeclaredMethod(Interface.class, "publicInterface", Integer.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("publicInterface", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    try {
      ClassUtils.getDeclaredMethod(Interface.class, "NONMETHOD");
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("NONMETHOD", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testBaseClassGetDeclaredMethodsByName() {
    assertEquals("publicInterface", ClassUtils.getDeclaredMethod(BaseClass.class, "publicInterface").getName());
    try {
      ClassUtils.getDeclaredMethod(Interface.class, "publicInterface", Integer.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("publicInterface", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    assertEquals("publicBase", ClassUtils.getDeclaredMethod(BaseClass.class, "publicBase").getName());
    try {
      ClassUtils.getDeclaredMethod(BaseClass.class, "publicBase", String.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("publicBase", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    assertEquals("protectedBase", ClassUtils.getDeclaredMethod(BaseClass.class, "protectedBase").getName());
    try {
      ClassUtils.getDeclaredMethod(BaseClass.class, "protectedBase", String.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("protectedBase", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    assertEquals("packageBase", ClassUtils.getDeclaredMethod(BaseClass.class, "packageBase").getName());
    try {
      ClassUtils.getDeclaredMethod(BaseClass.class, "packageBase", String.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("packageBase", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    assertEquals("privateBase", ClassUtils.getDeclaredMethod(BaseClass.class, "privateBase").getName());
    try {
      ClassUtils.getDeclaredMethod(BaseClass.class, "privateBase", String.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("privateBase", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    try {
      ClassUtils.getDeclaredMethod(BaseClass.class, "NONMETHOD");
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("NONMETHOD", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
  }
  public void testChildClassGetDeclaredMethodsByName() {
    assertEquals("publicInterface", ClassUtils.getDeclaredMethod(ChildClass.class, "publicInterface").getName());
    try {
      ClassUtils.getDeclaredMethod(ChildClass.class, "publicInterface", Integer.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("publicInterface", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    assertEquals("publicBase", ClassUtils.getDeclaredMethod(ChildClass.class, "publicBase").getName());
    try {
      ClassUtils.getDeclaredMethod(ChildClass.class, "publicBase", String.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("publicBase", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    assertEquals("protectedBase", ClassUtils.getDeclaredMethod(ChildClass.class, "protectedBase").getName());
    try {
      ClassUtils.getDeclaredMethod(ChildClass.class, "protectedBase", String.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("protectedBase", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    assertEquals("packageBase", ClassUtils.getDeclaredMethod(ChildClass.class, "packageBase").getName());
    try {
      ClassUtils.getDeclaredMethod(ChildClass.class, "packageBase", String.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("packageBase", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    assertEquals("privateBase", ClassUtils.getDeclaredMethod(ChildClass.class, "privateBase").getName());
    try {
      ClassUtils.getDeclaredMethod(ChildClass.class, "privateBase", String.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("privateBase", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    assertEquals("publicChild", ClassUtils.getDeclaredMethod(ChildClass.class, "publicChild").getName());
    try {
      ClassUtils.getDeclaredMethod(ChildClass.class, "publicChild", String.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("publicChild", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    assertEquals("protectedChild", ClassUtils.getDeclaredMethod(ChildClass.class, "protectedChild").getName());
    try {
      ClassUtils.getDeclaredMethod(ChildClass.class, "protectedChild", String.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("protectedChild", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    assertEquals("packageChild", ClassUtils.getDeclaredMethod(ChildClass.class, "packageChild").getName());
    try {
      ClassUtils.getDeclaredMethod(ChildClass.class, "packageChild", String.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("packageChild", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    assertEquals("privateChild", ClassUtils.getDeclaredMethod(ChildClass.class, "privateChild").getName());
    try {
      ClassUtils.getDeclaredMethod(ChildClass.class, "privateChild", String.class);
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("privateChild", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
    try {
      ClassUtils.getDeclaredMethod(ChildClass.class, "NONMETHOD");
      fail();
    } catch (NoSuchMethodError ex) {
      assertEquals("NONMETHOD", ex.getMessage());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testNullParametersGetDeclaredMethodsByName() {
    try {
      Method cloneMethod = Calendar.class.getDeclaredMethod("clone");
      assertEquals(cloneMethod, ClassUtils.getDeclaredMethod(Calendar.class, "clone"));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetClassLevelObjectClass() {
    assertEquals(0, ClassUtils.getClassInheritanceLevel(Object.class));
  }
  public void testGetClassLevelInterface() {
    assertEquals(1, ClassUtils.getClassInheritanceLevel(Serializable.class));
  }
  public void testGetClassLevelArrayListClass() {
    assertEquals(5, ClassUtils.getClassInheritanceLevel(ArrayList.class));
  }


  public void testGetSuperClassesObjectClass() {
    assertTrue(ClassUtils.getSuperClasses(Object.class).isEmpty());
  }

  public void testGetSuperClassesListInterfaceClass() {
    assertEquals(2, ClassUtils.getSuperClasses(List.class).size());
    Iterator i = ClassUtils.getSuperClasses(List.class).iterator();
    assertEquals(i.next(), Collection.class);
    assertEquals(i.next(), Iterable.class);
  }

  public void testGetSuperClassesArrayListClass() {
    assertEquals(8, ClassUtils.getSuperClasses(ArrayList.class).size());
    Iterator i = ClassUtils.getSuperClasses(ArrayList.class).iterator();
    assertEquals(i.next(), AbstractList.class);
    assertEquals(i.next(), List.class);
    assertEquals(i.next(), AbstractCollection.class);
    assertEquals(i.next(), Collection.class);
    assertEquals(i.next(), RandomAccess.class);
    assertEquals(i.next(), Iterable.class);
    assertEquals(i.next(), Cloneable.class);
    assertEquals(i.next(), Serializable.class);
  }
  public void testGetDeclaredMethodByTemplate() {
    try {
      Method template = BaseClass.class.getDeclaredMethod("publicInterface");
      Method interfaceMethod = Interface.class.getDeclaredMethod("publicInterface");
      assertEquals(interfaceMethod, ClassUtils.getDeclaredMethod(Interface.class, template));
    } catch (Exception ex) {
      fail();
    }
  }
  public void testGetDeclaredMethodByTemplateWithArgs() {
    try {
      Method template = BaseClass.class.getDeclaredMethod("publicInterface", String.class);
      Method interfaceMethod = Interface.class.getDeclaredMethod("publicInterface", String.class);
      assertEquals(interfaceMethod, ClassUtils.getDeclaredMethod(Interface.class, template));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetInterfaceAnnotatedMethodInheritedAnnotations() {
    try {
      Method method = ClassUtils.getDeclaredMethod(Interface.class, "annotatedMethod");
      Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(Interface.class, method);
      assertEquals(1, annotations.size());
      for (Annotation annotation : annotations) {
        assertTrue(annotation instanceof TestAnnotation);
        assertEquals("Interface", ((TestAnnotation)(annotation)).value());
      }
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetInterfaceAnnotatedInterfaceMethodInheritedAnnotations() {
    try {
      Method method = ClassUtils.getDeclaredMethod(Interface.class, "annotatedInterfaceMethod");
      Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(Interface.class, method);
      assertEquals(1, annotations.size());
      for (Annotation annotation : annotations) {
        assertTrue(annotation instanceof TestAnnotation);
        assertEquals("Interface", ((TestAnnotation)(annotation)).value());
      }
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetInterfaceAnnotatedBaseClassMethodInheritedAnnotations() {
    try {
      Method method = ClassUtils.getDeclaredMethod(Interface.class, "annotatedBaseClassMethod");
      Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(Interface.class, method);
      assertTrue(annotations.isEmpty());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetBaseClassAnnotatedMethodInheritedAnnotations() {
    try {
      Method method = ClassUtils.getDeclaredMethod(BaseClass.class, "annotatedMethod");
      Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(BaseClass.class, method);
      assertEquals(2, annotations.size());
      Iterator <? extends Annotation> i = annotations.iterator();
      for (Annotation annotation : annotations) {
        if (annotation instanceof TestAnnotation) {
          if ("BaseClass".equals(((TestAnnotation)(annotation)).value())) {
            continue;
          } else if ("Interface".equals(((TestAnnotation)(annotation)).value())) {
            continue;
          }
        }
        fail();
      }
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetBaseClassAnnotatedInterfaceMethodInheritedAnnotations() {
    try {
      Method method = ClassUtils.getDeclaredMethod(BaseClass.class, "annotatedInterfaceMethod");
      Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(BaseClass.class, method);
      assertEquals(1, annotations.size());
      for (Annotation annotation : annotations) {
        assertTrue(annotation instanceof TestAnnotation);
        assertEquals("Interface", ((TestAnnotation)(annotation)).value());
      }
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetBaseClassAnnotatedBaseClassMethodInheritedAnnotations() {
    try {
      Method method = ClassUtils.getDeclaredMethod(BaseClass.class, "annotatedBaseClassMethod");
      Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(BaseClass.class, method);
      assertEquals(1, annotations.size());
      for (Annotation annotation : annotations) {
        assertTrue(annotation instanceof TestAnnotation);
        assertEquals("BaseClass", ((TestAnnotation)(annotation)).value());
      }
    } catch (Exception ex) {
      fail();
    }
  }



  public void testGetChildClassAnnotatedMethodInheritedAnnotations() {
    try {
      Method method = ClassUtils.getDeclaredMethod(ChildClass.class, "annotatedMethod");
      Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(ChildClass.class, method);
      assertEquals(3, annotations.size());
      for (Annotation annotation : annotations) {
        if (annotation instanceof TestAnnotation) {
          if ("BaseClass".equals(((TestAnnotation)(annotation)).value())) {
            continue;
          } else if ("Interface".equals(((TestAnnotation)(annotation)).value())) {
            continue;
          } else if ("ChildClass".equals(((TestAnnotation)(annotation)).value())) {
            continue;
          }
        }
        fail();
      }
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetChildClassAnnotatedInterfaceMethodInheritedAnnotations() {
    try {
      Method method = ClassUtils.getDeclaredMethod(ChildClass.class, "annotatedInterfaceMethod");
      Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(ChildClass.class, method);
      assertEquals(1, annotations.size());
      for (Annotation annotation : annotations) {
        assertTrue(annotation instanceof TestAnnotation);
        assertEquals("Interface", ((TestAnnotation)(annotation)).value());
      }
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetChildClassAnnotatedBaseClassMethodInheritedAnnotations() {
    try {
      Method method = ClassUtils.getDeclaredMethod(ChildClass.class, "annotatedBaseClassMethod");
      Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(ChildClass.class, method);
      for (Annotation annotation : annotations) {
        assertTrue(annotation instanceof TestAnnotation);
        assertEquals("BaseClass", ((TestAnnotation)(annotation)).value());
      }
    } catch (Exception ex) {
      fail();
    }
  }
  public void testGetChildClass3AnnotatedBaseClassMethodInheritedAnnotations() {
    try {
      Method method = ClassUtils.getDeclaredMethod(ChildClass3.class, "annotatedMethod");
      Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(ChildClass3.class, method);
      for (Annotation annotation : annotations) {
        if (annotation instanceof TestAnnotation) {
          if ("BaseClass".equals(((TestAnnotation)(annotation)).value())) {
            continue;
          } else if ("Interface".equals(((TestAnnotation)(annotation)).value())) {
            continue;
          } else if ("ChildClass".equals(((TestAnnotation)(annotation)).value())) {
            continue;
          } else if ("Interface2".equals(((TestAnnotation)(annotation)).value())) {
            continue;
          }
        }
        fail();
      }
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetMethodInheritedAnnotationsByType() {
    try {
      Method method = ClassUtils.getDeclaredMethod(ChildClass.class, "annotatedMethod");
      Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(ChildClass.class, method, TestAnnotation.class);
      assertEquals(3, annotations.size());
      for (Annotation annotation : annotations) {
        if (annotation instanceof TestAnnotation) {
          if ("BaseClass".equals(((TestAnnotation)(annotation)).value())) {
            continue;
          } else if ("Interface".equals(((TestAnnotation)(annotation)).value())) {
            continue;
          } else if ("ChildClass".equals(((TestAnnotation)(annotation)).value())) {
            continue;
          }
        }
        fail();
      }
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetMethodInheritedAnnotationsByUnknownType() {
    try {
      Method method = ClassUtils.getDeclaredMethod(ChildClass.class, "annotatedMethod");
      Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(ChildClass.class, method, Deprecated.class);
      assertTrue(annotations.isEmpty());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testGetInterfaceInheritedAnnotations() {
    Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(Interface.class);
    assertEquals(1, annotations.size());
    Iterator <? extends Annotation> i = annotations.iterator();
    Annotation annotation = i.next();
    assertTrue(annotation instanceof TestAnnotation);
    assertEquals("Interface", ((TestAnnotation)(annotation)).value());
  }
  
  public void testGetBaseClassInheritedAnnotations() {
    Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(BaseClass.class);
    assertEquals(2, annotations.size());
    Iterator <? extends Annotation> i = annotations.iterator();
    Annotation annotation = i.next();
    assertTrue(annotation instanceof TestAnnotation);
    assertEquals("BaseClass", ((TestAnnotation)(annotation)).value());
    annotation = i.next();
    assertTrue(annotation instanceof TestAnnotation);
    assertEquals("Interface", ((TestAnnotation)(annotation)).value());
  }
  
  public void testGetChildClassInheritedAnnotations() {
    Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(ChildClass.class);
    assertEquals(3, annotations.size());
    Iterator <? extends Annotation> i = annotations.iterator();
    Annotation annotation = i.next();
    assertTrue(annotation instanceof TestAnnotation);
    assertEquals("ChildClass", ((TestAnnotation)(annotation)).value());
    annotation = i.next();
    assertTrue(annotation instanceof TestAnnotation);
    assertEquals("BaseClass", ((TestAnnotation)(annotation)).value());
    annotation = i.next();
    assertTrue(annotation instanceof TestAnnotation);
    assertEquals("Interface", ((TestAnnotation)(annotation)).value());
  }

  public void testGetChildClass2InheritedAnnotations() {
    Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(ChildClass2.class);
    assertEquals(2, annotations.size());
    Iterator <? extends Annotation> i = annotations.iterator();
    Annotation annotation = i.next();
    assertTrue(annotation instanceof TestAnnotation);
    assertEquals("BaseClass", ((TestAnnotation)(annotation)).value());
    annotation = i.next();
    assertTrue(annotation instanceof TestAnnotation);
    assertEquals("Interface", ((TestAnnotation)(annotation)).value());
  }
  public void testGetClassInheritedAnnotationsByType() {
    Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(ChildClass.class, TestAnnotation.class);
    assertEquals(3, annotations.size());
    Iterator <? extends Annotation> i = annotations.iterator();
    Annotation annotation = i.next();
    assertTrue(annotation instanceof TestAnnotation);
    assertEquals("ChildClass", ((TestAnnotation)(annotation)).value());
    annotation = i.next();
    assertTrue(annotation instanceof TestAnnotation);
    assertEquals("BaseClass", ((TestAnnotation)(annotation)).value());
    annotation = i.next();
    assertTrue(annotation instanceof TestAnnotation);
    assertEquals("Interface", ((TestAnnotation)(annotation)).value());
  }
  public void testGetClassInheritedAnnotationsByUnknowType() {
    Collection <? extends Annotation> annotations = ClassUtils.getInheritedAnnotations(ChildClass.class, Deprecated.class);
    assertTrue(annotations.isEmpty());
  }
  
  
  
  
  
  
  
  public void testGetInterfaceAnnotatedMethodOverridedMethods() {
    try {
      Method method = ClassUtils.getDeclaredMethod(Interface.class, "annotatedMethod");
      Collection <Method> methods = ClassUtils.getOverridedMethods(Interface.class, method);
      assertTrue(methods.isEmpty());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetBaseClassAnnotatedMethodOverridedMethods() {
    try {
      Method method = ClassUtils.getDeclaredMethod(BaseClass.class, "annotatedMethod");
      Collection <Method> methods = ClassUtils.getOverridedMethods(BaseClass.class, method);
      assertEquals(1, methods.size());
      assertTrue(methods.contains(ClassUtils.getDeclaredMethod(Interface.class, "annotatedMethod")));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetChildClassAnnotatedMethodOverridedMethods() {
    try {
      Method method = ClassUtils.getDeclaredMethod(ChildClass.class, "annotatedMethod");
      Collection <Method> methods = ClassUtils.getOverridedMethods(ChildClass.class, method);
      assertEquals(2, methods.size());
      assertTrue(methods.contains(ClassUtils.getDeclaredMethod(Interface.class, "annotatedMethod")));
      assertTrue(methods.contains(ClassUtils.getDeclaredMethod(BaseClass.class, "annotatedMethod")));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetChildClass2AnnotatedMethodOverridedMethods() {
    try {
      Method method = ClassUtils.getDeclaredMethod(ChildClass2.class, "annotatedMethod");
      Collection <Method> methods = ClassUtils.getOverridedMethods(ChildClass2.class, method);
      assertEquals(2, methods.size());
      assertTrue(methods.contains(ClassUtils.getDeclaredMethod(Interface.class, "annotatedMethod")));
      assertTrue(methods.contains(ClassUtils.getDeclaredMethod(BaseClass.class, "annotatedMethod")));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetChildClass3AnnotatedMethodOverridedMethods() {
    try {
      Method method = ClassUtils.getDeclaredMethod(ChildClass3.class, "annotatedMethod");
      Collection <Method> methods = ClassUtils.getOverridedMethods(ChildClass3.class, method);
      assertEquals(4, methods.size());
      assertTrue(methods.contains(ClassUtils.getDeclaredMethod(Interface.class, "annotatedMethod")));
      assertTrue(methods.contains(ClassUtils.getDeclaredMethod(Interface2.class, "annotatedMethod")));
      assertTrue(methods.contains(ClassUtils.getDeclaredMethod(BaseClass.class, "annotatedMethod")));
      assertTrue(methods.contains(ClassUtils.getDeclaredMethod(ChildClass.class, "annotatedMethod")));
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testPureGetPropertyName() {
    try {
      assertEquals("get()", ClassUtils.getPropertyName(PropertyNameTestBean.class.getMethod("get")));
    } catch(Exception ex) {
      fail();
    }
  }
  public void testPureSetPropertyName() {
    try {
      assertEquals("set()", ClassUtils.getPropertyName(PropertyNameTestBean.class.getMethod("set")));
    } catch(Exception ex) {
      fail();
    }
  }
  public void testPureIsPropertyName() {
    try {
      assertEquals("is()", ClassUtils.getPropertyName(PropertyNameTestBean.class.getMethod("is")));
    } catch(Exception ex) {
      fail();
    }
  }
  public void testGetPropertyName() {
    try {
      assertEquals("x", ClassUtils.getPropertyName(PropertyNameTestBean.class.getMethod("getX")));
    } catch(Exception ex) {
      fail();
    }
  }
  public void testSetPropertyName() {
    try {
      assertEquals("x", ClassUtils.getPropertyName(PropertyNameTestBean.class.getMethod("setX")));
    } catch(Exception ex) {
      fail();
    }
  }
  public void testIsPropertyName() {
    try {
      assertEquals("x", ClassUtils.getPropertyName(PropertyNameTestBean.class.getMethod("isX")));
    } catch(Exception ex) {
      fail();
    }
  }
  public void testNonPropertyMethodPropertyName() {
    try {
      assertEquals("xxx()", ClassUtils.getPropertyName(PropertyNameTestBean.class.getMethod("xxx")));
    } catch(Exception ex) {
      fail();
    }
  }
  
  public void testGetInheritedAnnotationInexistentAnnotation() {
    assertNull(ClassUtils.getInheritedAnnotation(PropertyNameTestBean.class, Deprecated.class));
  }
  public void testGetInheritedAnnotationSingleAnnotation() {
    assertEquals("Interface", ((TestAnnotation)(ClassUtils.getInheritedAnnotation(Interface.class, TestAnnotation.class))).value());
  }
  public void testGetInheritedAnnotation() {
    assertEquals("ChildClass", ((TestAnnotation)(ClassUtils.getInheritedAnnotation(ChildClass.class, TestAnnotation.class))).value());
  }
  public void testGetInheritedAnnotationInterfaceChild() {
    assertEquals("Interface", ((TestAnnotation)(ClassUtils.getInheritedAnnotation(InterfaceChild.class, TestAnnotation.class))).value());
  }

  
  
  
  
  public void testGetInterfaceAnnotatedMethodInheritedAnnotation() {
    try {
      Method method = ClassUtils.getDeclaredMethod(Interface.class, "annotatedMethod");
      TestAnnotation annotation = ClassUtils.getInheritedAnnotation(Interface.class, method, TestAnnotation.class);
      assertEquals("Interface", annotation.value());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetInterfaceAnnotatedInterfaceMethodInheritedAnnotation() {
    try {
      Method method = ClassUtils.getDeclaredMethod(Interface.class, "annotatedInterfaceMethod");
      TestAnnotation annotation = ClassUtils.getInheritedAnnotation(Interface.class, method, TestAnnotation.class);
      assertEquals("Interface", annotation.value());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetInterfaceAnnotatedBaseClassMethodInheritedAnnotation() {
    try {
      Method method = ClassUtils.getDeclaredMethod(Interface.class, "annotatedBaseClassMethod");
      assertNull(ClassUtils.getInheritedAnnotation(Interface.class, method, TestAnnotation.class));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetBaseClassAnnotatedMethodInheritedAnnotation() {
    try {
      Method method = ClassUtils.getDeclaredMethod(BaseClass.class, "annotatedMethod");
      TestAnnotation annotation = ClassUtils.getInheritedAnnotation(Interface.class, method, TestAnnotation.class);
      assertEquals("BaseClass", annotation.value());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetBaseClassAnnotatedInterfaceMethodInheritedAnnotation() {
    try {
      Method method = ClassUtils.getDeclaredMethod(BaseClass.class, "annotatedInterfaceMethod");
      TestAnnotation annotation = ClassUtils.getInheritedAnnotation(BaseClass.class, method, TestAnnotation.class);
      assertEquals("Interface", annotation.value());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetBaseClassAnnotatedBaseClassMethodInheritedAnnotation() {
    try {
      Method method = ClassUtils.getDeclaredMethod(BaseClass.class, "annotatedBaseClassMethod");
      TestAnnotation annotation = ClassUtils.getInheritedAnnotation(BaseClass.class, method, TestAnnotation.class);
      assertEquals("BaseClass", annotation.value());
    } catch (Exception ex) {
      fail();
    }
  }



  public void testGetChildClassAnnotatedMethodInheritedAnnotation() {
    try {
      Method method = ClassUtils.getDeclaredMethod(ChildClass.class, "annotatedMethod");
      TestAnnotation annotation = ClassUtils.getInheritedAnnotation(ChildClass.class, method, TestAnnotation.class);
      assertEquals("ChildClass", annotation.value());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetChildClassAnnotatedInterfaceMethodInheritedAnnotation() {
    try {
      Method method = ClassUtils.getDeclaredMethod(ChildClass.class, "annotatedInterfaceMethod");
      TestAnnotation annotation = ClassUtils.getInheritedAnnotation(ChildClass.class, method, TestAnnotation.class);
      assertEquals("Interface", annotation.value());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetChildClassAnnotatedBaseClassMethodInheritedAnnotation() {
    try {
      Method method = ClassUtils.getDeclaredMethod(ChildClass.class, "annotatedBaseClassMethod");
      TestAnnotation annotation = ClassUtils.getInheritedAnnotation(ChildClass.class, method, TestAnnotation.class);
      assertEquals("BaseClass", annotation.value());
    } catch (Exception ex) {
      fail();
    }
  }
  public void testGetChildClass3AnnotatedBaseClassMethodInheritedAnnotation() {
    try {
      Method method = ClassUtils.getDeclaredMethod(ChildClass3.class, "annotatedMethod");
      TestAnnotation annotation = ClassUtils.getInheritedAnnotation(ChildClass.class, method, TestAnnotation.class);
      assertEquals("ChildClass", annotation.value());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testGetInheritedAnnotationWithWrongAnnotation() {
    try {
      Method method = ClassUtils.getDeclaredMethod(ChildClass3.class, "annotatedMethod");
      assertNull(ClassUtils.getInheritedAnnotation(ChildClass.class, method, Deprecated.class));
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testMethodEqualitySanityCheck() {
    try {
      assertFalse(ClassUtils.getDeclaredMethod(Interface.class, "annotatedMethod").equals(ClassUtils.getDeclaredMethod(BaseClass.class, "annotatedMethod")));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetPropertiesDescriptorsWithNullClass() {
    assertTrue(ClassUtils.getPropertyDescriptors(null).isEmpty());
  }

  public void testGetPropertiesDescriptors() {
    Collection <PropertyDescriptor> descriptors = ClassUtils.getPropertyDescriptors(MockBean.class);
    assertEquals(2, descriptors.size());
    for (PropertyDescriptor descriptor : descriptors) {
      if (descriptor.getName().equals("x")) {
      } else if (descriptor.getName().equals("class")) {
      } else {
        fail();
      }
    }
  }
}
