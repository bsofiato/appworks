/*
 * ThreadLocalExpressionBindingTest.java
 * JUnit based test
 *
 * Created on 22 de Outubro de 2005, 16:13
 */

package br.com.appworks.runtime.lang.support.expression.groovy.test;

import br.com.appworks.runtime.lang.support.expression.groovy.ExpressionBinding;
import br.com.appworks.runtime.lang.support.expression.groovy.ThreadLocalExpressionBinding;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import junit.framework.*;
import org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl;

/**
 *
 * @author Bubu
 */
public class ThreadLocalExpressionBindingTest extends TestCase {
  private static class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private boolean exceptionOccurred;
    public boolean isExceptionOcurred() {
      return exceptionOccurred;
    }
    public void uncaughtException(Thread t, Throwable e) {
      this.exceptionOccurred = true;
    }
  }
  
  public ThreadLocalExpressionBindingTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public Object method() {
    return this;
  }
  public void testMetaClassRegistryDelegation() {  
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      ThreadLocalExpressionBinding threadLocalExpressionBinding = new ThreadLocalExpressionBinding(metaClassRegistry);
      Method threadLocalExpressionBindingMethod = ThreadLocalExpressionBinding.class.getDeclaredMethod("getThreadLocalExpressionBinding");
      threadLocalExpressionBindingMethod.setAccessible(true);
      ExpressionBinding expressionBinding = (ExpressionBinding)(threadLocalExpressionBindingMethod.invoke(threadLocalExpressionBinding));
      Field field = ExpressionBinding.class.getDeclaredField("metaClassRegistry");
      field.setAccessible(true);
      assertSame(metaClassRegistry, field.get(expressionBinding));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetMetaClass() {  
    try {
      Map <String, Object> variables = new HashMap <String, Object> ();
      variables.put("this", this);
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      ThreadLocalExpressionBinding threadLocalExpressionBinding = new ThreadLocalExpressionBinding(metaClassRegistry);
      threadLocalExpressionBinding.setVariables(variables);
      Method threadLocalExpressionBindingMethod = ThreadLocalExpressionBinding.class.getDeclaredMethod("getThreadLocalExpressionBinding");
      threadLocalExpressionBindingMethod.setAccessible(true);
      ExpressionBinding expressionBinding = (ExpressionBinding)(threadLocalExpressionBindingMethod.invoke(threadLocalExpressionBinding));
      assertSame(threadLocalExpressionBinding.getMetaClass(), expressionBinding.getMetaClass());
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testGetProperty() {  
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      ThreadLocalExpressionBinding threadLocalExpressionBinding = new ThreadLocalExpressionBinding(metaClassRegistry);
      Method threadLocalExpressionBindingMethod = ThreadLocalExpressionBinding.class.getDeclaredMethod("getThreadLocalExpressionBinding");
      threadLocalExpressionBindingMethod.setAccessible(true);
      ExpressionBinding expressionBinding = (ExpressionBinding)(threadLocalExpressionBindingMethod.invoke(threadLocalExpressionBinding));
      expressionBinding.setProperty("x", this);
      assertSame(this, threadLocalExpressionBinding.getProperty("x"));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testGetVariable() {  
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      ThreadLocalExpressionBinding threadLocalExpressionBinding = new ThreadLocalExpressionBinding(metaClassRegistry);
      Method threadLocalExpressionBindingMethod = ThreadLocalExpressionBinding.class.getDeclaredMethod("getThreadLocalExpressionBinding");
      threadLocalExpressionBindingMethod.setAccessible(true);
      ExpressionBinding expressionBinding = (ExpressionBinding)(threadLocalExpressionBindingMethod.invoke(threadLocalExpressionBinding));
      expressionBinding.setVariable("x", this);
      assertSame(this, threadLocalExpressionBinding.getVariable("x"));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testGetVariables() {  
    try {
      Map <String, Object> variables = new HashMap <String, Object> ();
      variables.put("this", this);
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      ThreadLocalExpressionBinding threadLocalExpressionBinding = new ThreadLocalExpressionBinding(metaClassRegistry);
      Method threadLocalExpressionBindingMethod = ThreadLocalExpressionBinding.class.getDeclaredMethod("getThreadLocalExpressionBinding");
      threadLocalExpressionBindingMethod.setAccessible(true);
      ExpressionBinding expressionBinding = (ExpressionBinding)(threadLocalExpressionBindingMethod.invoke(threadLocalExpressionBinding));
      expressionBinding.setVariables(variables);
      assertEquals(variables, threadLocalExpressionBinding.getVariables());
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testInvokeMethod() {  
    try {
      Map <String, Object> variables = new HashMap <String, Object> ();
      variables.put("this", this);
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      ThreadLocalExpressionBinding threadLocalExpressionBinding = new ThreadLocalExpressionBinding(metaClassRegistry);
      threadLocalExpressionBinding.setVariables(variables);
      Method threadLocalExpressionBindingMethod = ThreadLocalExpressionBinding.class.getDeclaredMethod("getThreadLocalExpressionBinding");
      threadLocalExpressionBindingMethod.setAccessible(true);
      ExpressionBinding expressionBinding = (ExpressionBinding)(threadLocalExpressionBindingMethod.invoke(threadLocalExpressionBinding));
      assertSame(threadLocalExpressionBinding.invokeMethod("method", null), expressionBinding.invokeMethod("method", null));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testSetProperty() {  
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      ThreadLocalExpressionBinding threadLocalExpressionBinding = new ThreadLocalExpressionBinding(metaClassRegistry);
      Method threadLocalExpressionBindingMethod = ThreadLocalExpressionBinding.class.getDeclaredMethod("getThreadLocalExpressionBinding");
      threadLocalExpressionBindingMethod.setAccessible(true);
      ExpressionBinding expressionBinding = (ExpressionBinding)(threadLocalExpressionBindingMethod.invoke(threadLocalExpressionBinding));
      threadLocalExpressionBinding.setProperty("x", "x");
      assertEquals("x", expressionBinding.getProperty("x"));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testSetVariable() {  
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      ThreadLocalExpressionBinding threadLocalExpressionBinding = new ThreadLocalExpressionBinding(metaClassRegistry);
      Method threadLocalExpressionBindingMethod = ThreadLocalExpressionBinding.class.getDeclaredMethod("getThreadLocalExpressionBinding");
      threadLocalExpressionBindingMethod.setAccessible(true);
      ExpressionBinding expressionBinding = (ExpressionBinding)(threadLocalExpressionBindingMethod.invoke(threadLocalExpressionBinding));
      threadLocalExpressionBinding.setVariable("x", "x");
      assertEquals("x", expressionBinding.getVariable("x"));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testSetVariables() {  
    try {
      Map <String, Object> variables = new HashMap <String, Object> ();
      variables.put("this", this);
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      ThreadLocalExpressionBinding threadLocalExpressionBinding = new ThreadLocalExpressionBinding(metaClassRegistry);
      Method threadLocalExpressionBindingMethod = ThreadLocalExpressionBinding.class.getDeclaredMethod("getThreadLocalExpressionBinding");
      threadLocalExpressionBindingMethod.setAccessible(true);
      ExpressionBinding expressionBinding = (ExpressionBinding)(threadLocalExpressionBindingMethod.invoke(threadLocalExpressionBinding));
      threadLocalExpressionBinding.setVariables(variables);
      assertEquals(variables, expressionBinding.getVariables());
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testThreadLocalExpressionBinding() {  
    try {
      ExceptionHandler handler = new ExceptionHandler();
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      final ThreadLocalExpressionBinding threadLocalExpressionBinding = new ThreadLocalExpressionBinding(metaClassRegistry);
      final Set <ExpressionBinding> expressionBindingSet = new HashSet <ExpressionBinding> ();
      Runnable runnable = new Runnable() {
        public void run() {
          try {
            Method threadLocalExpressionBindingMethod = ThreadLocalExpressionBinding.class.getDeclaredMethod("getThreadLocalExpressionBinding");
            threadLocalExpressionBindingMethod.setAccessible(true);
            ExpressionBinding expressionBinding = (ExpressionBinding)(threadLocalExpressionBindingMethod.invoke(threadLocalExpressionBinding));
            expressionBindingSet.add(expressionBinding);
          } catch (Exception ex) {
            fail();
          }
        }
      };
      Thread [] threads = new Thread[10];
      for (int i=0; i<threads.length; i++) {
        threads[i] = new Thread(runnable);
        threads[i].setUncaughtExceptionHandler(handler);
        threads[i].start();
      }
      for (Thread thread : threads) {
        thread.join();
      }
      assertEquals(threads.length, expressionBindingSet.size());
      assertFalse(handler.isExceptionOcurred());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testThreadLocalExpressionCaching() {  
    try {
      ExceptionHandler handler = new ExceptionHandler();
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      final ThreadLocalExpressionBinding threadLocalExpressionBinding = new ThreadLocalExpressionBinding(metaClassRegistry);
      Runnable runnable = new Runnable() {
        public void run() {
          try {
            Method threadLocalExpressionBindingMethod = ThreadLocalExpressionBinding.class.getDeclaredMethod("getThreadLocalExpressionBinding");
            threadLocalExpressionBindingMethod.setAccessible(true);
            assertSame(threadLocalExpressionBindingMethod.invoke(threadLocalExpressionBinding), threadLocalExpressionBindingMethod.invoke(threadLocalExpressionBinding));
          } catch (Exception ex) {
            fail();
          }
        }
      };
      Thread [] threads = new Thread[10];
      for (int i=0; i<threads.length; i++) {
        threads[i] = new Thread(runnable);
        threads[i].setUncaughtExceptionHandler(handler);
        threads[i].start();
      }
      for (Thread thread : threads) {
        thread.join();
      }
      assertFalse(handler.isExceptionOcurred());
    } catch (Exception ex) {
      fail();
    }
  }

}
