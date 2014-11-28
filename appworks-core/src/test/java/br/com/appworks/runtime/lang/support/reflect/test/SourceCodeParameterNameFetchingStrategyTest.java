/*
 * SourceCodeParameterNameFetchingStrategyTest.java
 * JUnit based test
 *
 * Created on 30 de Setembro de 2005, 22:49
 */

package br.com.appworks.runtime.lang.support.reflect.test;

import br.com.appworks.runtime.lang.support.reflect.SourceCodeParameterNameFetchingStrategy;
import java.beans.IntrospectionException;
import java.io.Closeable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import junit.framework.*;

public class SourceCodeParameterNameFetchingStrategyTest extends TestCase {
  public @interface TestAnnotation { 
    String [] value() default "";
  }
  private static class TestAnnonymousClassTestBean {
    private Runnable runnable;
    public TestAnnonymousClassTestBean() {
      runnable = new Runnable() { 
        public void run() {}; 
        public void test() {};
      };
    }
    public TestAnnonymousClassTestBean(boolean arg) {
      runnable = new Runnable() { 
        public void run() {}; 
        public void test(String name, String value) {};
      };
    }
    public TestAnnonymousClassTestBean(Object [] object) {
      runnable = new Runnable() { 
        public void run() {}; 
        public void test(Object [] array1, Object [][]array2) {};
      };
    }

  }
  private static interface TestInterface {
    
    int method() throws Exception;
    int method(String name, String value);
  }
  private static class TestBean {
    private TestBean() {
      throw new UnsupportedOperationException();
    }
    private TestBean(String name) {
      throw new UnsupportedOperationException();
    }
    private TestBean(Object [] array1, Object [][] array2) {
      throw new UnsupportedOperationException();
    }
    private int method() throws Exception {
      throw new UnsupportedOperationException();
    }
    private int method(Object [] array1, Object [][] array2) throws Exception {
      throw new UnsupportedOperationException();
    }
    private int method(String name, String value) throws Exception {
      throw new UnsupportedOperationException();
    }
    @TestAnnotation({"a", "b", "c"})
    private int annotatedMethod(String name, String value) throws Exception {
      return 0;
    }

  }
  
  private SourceCodeParameterNameFetchingStrategyTest() {
    throw new UnsupportedOperationException();
  }
  private SourceCodeParameterNameFetchingStrategyTest(Object [] array1, Object [][] array2) {
    throw new UnsupportedOperationException();
  }
  public SourceCodeParameterNameFetchingStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  private int method() throws Exception {
    throw new UnsupportedOperationException();
  }
  private int method(String name, String value) throws Exception {
    throw new UnsupportedOperationException();
  }
  private int method(Object [] array1, Object [][] array2) throws Exception {
    throw new UnsupportedOperationException();
  }
  
  public void testGetMethodParameterNames() {
    try {
      Method method = getClass().getDeclaredMethod("method", String.class, String.class);
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      Collection <String> parameterNames = strategy.getParameterNames(method);
      assertEquals(Arrays.asList( new String [] { "name", "value" }), parameterNames);
    } catch (Exception ex) {
        ex.printStackTrace();
      fail();
    }
  }
  
  public void testGetMethodArrayParameterNames() {
    try {
      Method method = getClass().getDeclaredMethod("method", new Object [0].getClass(), new Object [0][0].getClass());
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      Collection <String> parameterNames = strategy.getParameterNames(method);
      assertEquals(Arrays.asList( new String [] { "array1", "array2" }), parameterNames);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
 
  public void testGetConstructorParameterNames() {
    try {
      Constructor constructor = getClass().getConstructor(String.class);
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      Collection <String> parameterNames = strategy.getParameterNames(constructor);
      assertEquals(Arrays.asList( new String [] { "testName" }), parameterNames);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  public void testGetConstructorArrayParameterNames() {
    try {
      Constructor constructor = getClass().getDeclaredConstructor(new Object [0].getClass(), new Object [0][0].getClass());
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      Collection <String> parameterNames = strategy.getParameterNames(constructor);
      assertEquals(Arrays.asList( new String [] { "array1", "array2" }), parameterNames);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testGetNoArgsMethodParameterNames() {
    try {
      Method method = getClass().getDeclaredMethod("method");
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      assertTrue(strategy.getParameterNames(method).isEmpty());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testGetNoArgsConstructorParameterNames() {
    try {
      Constructor constructor = getClass().getDeclaredConstructor();
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      assertTrue(strategy.getParameterNames(constructor).isEmpty());
    } catch (Exception ex) {
      fail();
    }
  }
  
   public void testGetInnerClassMethodParameterNames() {
    try {
      Method method = TestBean.class.getDeclaredMethod("method", String.class, String.class);
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      Collection <String> parameterNames = strategy.getParameterNames(method);
      assertEquals(Arrays.asList( new String [] { "name", "value" }), parameterNames);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
   
  public void testInnerClassGetMethodArrayParameterNames() {
    try {
      Method method = TestBean.class.getDeclaredMethod("method", new Object [0].getClass(), new Object [0][0].getClass());
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      Collection <String> parameterNames = strategy.getParameterNames(method);
      assertEquals(Arrays.asList( new String [] { "array1", "array2" }), parameterNames);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
   
  public void testGetInnerClassConstructorParameterNames() {
    try {
      Constructor constructor = TestBean.class.getDeclaredConstructor(String.class);
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      Collection <String> parameterNames = strategy.getParameterNames(constructor);
      assertEquals(Arrays.asList( new String [] { "name"}), parameterNames);
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetInnerClassConstructorArrayParameterNames() {
    try {
      Constructor constructor = TestBean.class.getDeclaredConstructor(new Object [0].getClass(), new Object [0][0].getClass());
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      Collection <String> parameterNames = strategy.getParameterNames(constructor);
      assertEquals(Arrays.asList( new String [] { "array1", "array2" }), parameterNames);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testGetInnerClassNoArgsMethodParameterNames() {
    try {
      Method method = TestBean.class.getDeclaredMethod("method");
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      assertTrue(strategy.getParameterNames(method).isEmpty());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testGetInnerClassNoArgsConstructorParameterNames() {
    try {
      Constructor constructor = TestBean.class.getDeclaredConstructor();
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      assertTrue(strategy.getParameterNames(constructor).isEmpty());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testSourceNotAvaliableClassMethodParameterNames() {
    try {
      Method method = Object.class.getMethod("equals", Object.class);
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      strategy.getParameterNames(method);
      fail();
    } catch (IntrospectionException ex) {
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testSourceNotAvaliableClassNoArgsMethodParameterNames() {
    try {
      Method method = Object.class.getMethod("hashCode");
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      assertTrue(strategy.getParameterNames(method).isEmpty());
    } catch (Exception ex) {
      fail();
    }
  }

   public void testSourceNotAvaliableClassConstructorParameterNames() {
    try {
      Constructor constructor = Exception.class.getConstructor(Throwable.class);
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      strategy.getParameterNames(constructor);
      fail();
    } catch (IntrospectionException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
   
  public void testSourceNotAvaliableClassNoArgsConstructorParameterNames() {
    try {
      Constructor constructor = Exception.class.getConstructor();
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      assertTrue(strategy.getParameterNames(constructor).isEmpty());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testInterfaceMethodParameterNames() {
    try {
      Method method = TestInterface.class.getMethod("method", String.class, String.class);
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      Collection <String> parameterNames = strategy.getParameterNames(method);
      assertEquals(Arrays.asList( new String [] { "name", "value" }), parameterNames);
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testInterfaceNoArgsMethodParameterNames() {
    try {
      Method method = TestInterface.class.getMethod("method");
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      assertTrue(strategy.getParameterNames(method).isEmpty());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testSourceNotAvaliableInterfaceMethodParameterNames() {
    try {
      Method method = Comparator.class.getMethod("compare", Object.class, Object.class);
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      strategy.getParameterNames(method);
      fail();
    } catch (IntrospectionException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testSourceNotAvaliableInterfaceNoArgsMethodParameterNames() {
    try {
      Method method = Closeable.class.getMethod("close");
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      assertTrue(strategy.getParameterNames(method).isEmpty());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testAnnonymousClassNoArgsMethodParameterNames() {
    try {
      Runnable runnable = new Runnable() { 
        public void run() {}; 
        public void test() {};
      };
      Method method = runnable.getClass().getMethod("test");
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      assertTrue(strategy.getParameterNames(method).isEmpty());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testAnnonymousClassMethodParameterNames() {
    try {
      Runnable runnable = new Runnable() { 
        public void run() {}; 
        public void test(String name, String value) {};
      };
      Method method = runnable.getClass().getMethod("test", String.class, String.class);
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      strategy.getParameterNames(method);
      fail();
    } catch (IntrospectionException ex) {
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testAnnonymousClassMethodArrayParameterNames() {
    try {
      Runnable runnable = new Runnable() { 
        public void run() {}; 
        public void test(Object [] array1, Object [][] array2) {};
      };
      Method method = runnable.getClass().getDeclaredMethod("test", new Object [0].getClass(), new Object [0][0].getClass());
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      strategy.getParameterNames(method);
      fail();
    } catch (IntrospectionException ex) {
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testAnnonymousConstructorClassNoArgsMethodParameterNames() {
    try {
      Runnable runnable = new TestAnnonymousClassTestBean().runnable;
      Method method = runnable.getClass().getMethod("test");
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      assertTrue(strategy.getParameterNames(method).isEmpty());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testAnnonymousConstructorClassMethodArrayParameterNames() {
    try {
      Runnable runnable = new TestAnnonymousClassTestBean(new Object [0]).runnable;
      Method method = runnable.getClass().getDeclaredMethod("test", new Object [0].getClass(), new Object [0][0].getClass());
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      strategy.getParameterNames(method);
      fail();
    } catch (IntrospectionException ex) {
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  

  public void testAnnonymousConstructorClassMethodParameterNames() {
    try {
      Runnable runnable = new TestAnnonymousClassTestBean(true).runnable;
      Method method = runnable.getClass().getMethod("test", String.class, String.class);
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      strategy.getParameterNames(method);
      fail();
    } catch (IntrospectionException ex) {
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  public void testAnnotatedGetMethodParameterNames() {
    try {
      Method method = TestBean.class.getDeclaredMethod("annotatedMethod", String.class, String.class);
      SourceCodeParameterNameFetchingStrategy strategy = new SourceCodeParameterNameFetchingStrategy();
      Collection <String> parameterNames = strategy.getParameterNames(method);
      assertEquals(Arrays.asList( new String [] { "name", "value" }), parameterNames);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

}
