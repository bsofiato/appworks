/*
 * ParameterNameFetchingHandlerTest.java
 * JUnit based test
 *
 * Created on 1 de Outubro de 2005, 01:10
 */

package br.com.appworks.runtime.lang.support.reflect.test;

import br.com.appworks.runtime.lang.support.reflect.ParameterNameFetchingHandler;
import br.com.appworks.runtime.lang.support.reflect.ParameterNameFetchingStrategy;
import java.beans.IntrospectionException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import junit.framework.*;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class ParameterNameFetchingHandlerTest extends TestCase {
  
  public ParameterNameFetchingHandlerTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testEmptyStrategiesMethodParameterNameFetching() {
    try {
      ParameterNameFetchingHandler handler = new ParameterNameFetchingHandler();
      Method method = getClass().getDeclaredMethod("setUp");
      handler.getParameterNames(method);
      fail();
    } catch (IntrospectionException ex) {
    } catch (Exception ex) {
      fail();
    }
  }

  public void testEmptyStrategiesConstructorParameterNameFetching() {
    try {
      ParameterNameFetchingHandler handler = new ParameterNameFetchingHandler();
      Constructor constructor = getClass().getDeclaredConstructor(String.class);
      handler.getParameterNames(constructor);
      fail();
    } catch (IntrospectionException ex) {
    } catch (Exception ex) {
      fail();
    }
  }

  
  public void testConstructorParameterNameFetchingWithFirstStrategyFetching() {
    MockControl firstStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    MockControl secondStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    try {
      Constructor constructor = getClass().getDeclaredConstructor(String.class);
      Collection <String> parameterNames = new ArrayList<String> ();

      ParameterNameFetchingStrategy firstStrategy = (ParameterNameFetchingStrategy)(firstStrategyControl.getMock());
      ParameterNameFetchingStrategy secondStrategy = (ParameterNameFetchingStrategy)(secondStrategyControl.getMock());
      firstStrategy.getParameterNames(constructor);
      firstStrategyControl.setReturnValue(parameterNames);
      
      firstStrategyControl.replay();
      secondStrategyControl.replay();

      ParameterNameFetchingHandler handler = new ParameterNameFetchingHandler(Arrays.asList(new ParameterNameFetchingStrategy [] { firstStrategy, secondStrategy }));
      assertSame(parameterNames, handler.getParameterNames(constructor));
    } catch (Exception ex) {
      fail();
    } finally {
      firstStrategyControl.verify();
      secondStrategyControl.verify();
    }
  }

  public void testMethodParameterNameFetchingWithFirstStrategyFetching() {
    MockControl firstStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    MockControl secondStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    try {
      Method method = getClass().getDeclaredMethod("setUp");
      Collection <String> parameterNames = new ArrayList<String> ();

      ParameterNameFetchingStrategy firstStrategy = (ParameterNameFetchingStrategy)(firstStrategyControl.getMock());
      ParameterNameFetchingStrategy secondStrategy = (ParameterNameFetchingStrategy)(secondStrategyControl.getMock());
      firstStrategy.getParameterNames(method);
      firstStrategyControl.setReturnValue(parameterNames);
      
      firstStrategyControl.replay();
      secondStrategyControl.replay();

      ParameterNameFetchingHandler handler = new ParameterNameFetchingHandler(Arrays.asList(new ParameterNameFetchingStrategy [] { firstStrategy, secondStrategy }));
      assertSame(parameterNames, handler.getParameterNames(method));
    } catch (Exception ex) {
      fail();
    } finally {
      firstStrategyControl.verify();
      secondStrategyControl.verify();
    }
  }
  
  public void testConstructorParameterNameFetchingWithFirstStrategyRuntimeException() {
    MockControl firstStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    MockControl secondStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    RuntimeException error = new RuntimeException();
    try {
      Constructor constructor = getClass().getDeclaredConstructor(String.class);

      ParameterNameFetchingStrategy firstStrategy = (ParameterNameFetchingStrategy)(firstStrategyControl.getMock());
      ParameterNameFetchingStrategy secondStrategy = (ParameterNameFetchingStrategy)(secondStrategyControl.getMock());
      firstStrategy.getParameterNames(constructor);
      firstStrategyControl.setThrowable(error);
      
      firstStrategyControl.replay();
      secondStrategyControl.replay();

      ParameterNameFetchingHandler handler = new ParameterNameFetchingHandler(Arrays.asList(new ParameterNameFetchingStrategy [] { firstStrategy, secondStrategy }));
      handler.getParameterNames(constructor);
      fail();
    } catch (RuntimeException ex) {
      assertSame(error, ex);
    } catch (Exception ex) {
      fail();
    } finally {
      firstStrategyControl.verify();
      secondStrategyControl.verify();
    }
  }
  
  public void testMethodParameterNameFetchingWithFirstStrategyRuntimeException() {
    MockControl firstStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    MockControl secondStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    RuntimeException error = new RuntimeException();
    try {
      Method method = getClass().getDeclaredMethod("setUp");

      ParameterNameFetchingStrategy firstStrategy = (ParameterNameFetchingStrategy)(firstStrategyControl.getMock());
      ParameterNameFetchingStrategy secondStrategy = (ParameterNameFetchingStrategy)(secondStrategyControl.getMock());
      firstStrategy.getParameterNames(method);
      firstStrategyControl.setThrowable(error);
      
      firstStrategyControl.replay();
      secondStrategyControl.replay();

      ParameterNameFetchingHandler handler = new ParameterNameFetchingHandler(Arrays.asList(new ParameterNameFetchingStrategy [] { firstStrategy, secondStrategy }));
      handler.getParameterNames(method);
      fail();
    } catch (RuntimeException ex) {
      assertSame(error, ex);
    } catch (Exception ex) {
      fail();
    } finally {
      firstStrategyControl.verify();
      secondStrategyControl.verify();
    }
  }
  
  
  public void testConstructorParameterNameFetchingWithSecondStrategyFetching() {
    MockControl firstStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    MockControl secondStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    try {
      Constructor constructor = getClass().getDeclaredConstructor(String.class);
      Collection <String> parameterNames = new ArrayList<String> ();

      ParameterNameFetchingStrategy firstStrategy = (ParameterNameFetchingStrategy)(firstStrategyControl.getMock());
      ParameterNameFetchingStrategy secondStrategy = (ParameterNameFetchingStrategy)(secondStrategyControl.getMock());
      firstStrategy.getParameterNames(constructor);
      firstStrategyControl.setThrowable(new IntrospectionException(""));
      secondStrategy.getParameterNames(constructor);
      secondStrategyControl.setReturnValue(parameterNames);
      
      firstStrategyControl.replay();
      secondStrategyControl.replay();

      ParameterNameFetchingHandler handler = new ParameterNameFetchingHandler(Arrays.asList(new ParameterNameFetchingStrategy [] { firstStrategy, secondStrategy }));
      assertSame(parameterNames, handler.getParameterNames(constructor));
    } catch (Exception ex) {
      fail();
    } finally {
      firstStrategyControl.verify();
      secondStrategyControl.verify();
    }
  }
  
  public void testMethodParameterNameFetchingWithSecondStrategyFetching() {
    MockControl firstStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    MockControl secondStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    try {
      Method method = getClass().getDeclaredMethod("setUp");
      Collection <String> parameterNames = new ArrayList<String> ();

      ParameterNameFetchingStrategy firstStrategy = (ParameterNameFetchingStrategy)(firstStrategyControl.getMock());
      ParameterNameFetchingStrategy secondStrategy = (ParameterNameFetchingStrategy)(secondStrategyControl.getMock());
      firstStrategy.getParameterNames(method);
      firstStrategyControl.setThrowable(new IntrospectionException(""));
      secondStrategy.getParameterNames(method);
      secondStrategyControl.setReturnValue(parameterNames);
      
      firstStrategyControl.replay();
      secondStrategyControl.replay();

      ParameterNameFetchingHandler handler = new ParameterNameFetchingHandler(Arrays.asList(new ParameterNameFetchingStrategy [] { firstStrategy, secondStrategy }));
      assertSame(parameterNames, handler.getParameterNames(method));
    } catch (Exception ex) {
      fail();
    } finally {
      firstStrategyControl.verify();
      secondStrategyControl.verify();
    }
  }
  
  public void testConstructorParameterNameFetchingWithSecondStrategyRuntimeException() {
    MockControl firstStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    MockControl secondStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    RuntimeException error = new RuntimeException();
    try {
      Constructor constructor = getClass().getDeclaredConstructor(String.class);

      ParameterNameFetchingStrategy firstStrategy = (ParameterNameFetchingStrategy)(firstStrategyControl.getMock());
      ParameterNameFetchingStrategy secondStrategy = (ParameterNameFetchingStrategy)(secondStrategyControl.getMock());
      firstStrategy.getParameterNames(constructor);
      firstStrategyControl.setThrowable(new IntrospectionException(""));
      secondStrategy.getParameterNames(constructor);
      secondStrategyControl.setThrowable(error);
      
      firstStrategyControl.replay();
      secondStrategyControl.replay();

      ParameterNameFetchingHandler handler = new ParameterNameFetchingHandler(Arrays.asList(new ParameterNameFetchingStrategy [] { firstStrategy, secondStrategy }));
      handler.getParameterNames(constructor);
      fail();
    } catch (RuntimeException ex) {
      assertSame(error, ex);
    } catch (Exception ex) {
      fail();
    } finally {
      firstStrategyControl.verify();
      secondStrategyControl.verify();
    }
  }

  public void testMethodParameterNameFetchingWithSecondStrategyRuntimeException() {
    MockControl firstStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    MockControl secondStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    RuntimeException error = new RuntimeException();
    try {
      Method method = getClass().getDeclaredMethod("setUp");

      ParameterNameFetchingStrategy firstStrategy = (ParameterNameFetchingStrategy)(firstStrategyControl.getMock());
      ParameterNameFetchingStrategy secondStrategy = (ParameterNameFetchingStrategy)(secondStrategyControl.getMock());
      firstStrategy.getParameterNames(method);
      firstStrategyControl.setThrowable(new IntrospectionException(""));
      secondStrategy.getParameterNames(method);
      secondStrategyControl.setThrowable(error);
      
      firstStrategyControl.replay();
      secondStrategyControl.replay();

      ParameterNameFetchingHandler handler = new ParameterNameFetchingHandler(Arrays.asList(new ParameterNameFetchingStrategy [] { firstStrategy, secondStrategy }));
      handler.getParameterNames(method);
      fail();
    } catch (RuntimeException ex) {
      assertSame(error, ex);
    } catch (Exception ex) {
      fail();
    } finally {
      firstStrategyControl.verify();
      secondStrategyControl.verify();
    }
  }

  public void testConstructorParameterNameFetchingWithNoFetching() {
    MockControl firstStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    MockControl secondStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    try {
      Constructor constructor = getClass().getDeclaredConstructor(String.class);

      ParameterNameFetchingStrategy firstStrategy = (ParameterNameFetchingStrategy)(firstStrategyControl.getMock());
      ParameterNameFetchingStrategy secondStrategy = (ParameterNameFetchingStrategy)(secondStrategyControl.getMock());
      firstStrategy.getParameterNames(constructor);
      firstStrategyControl.setThrowable(new IntrospectionException(""));
      secondStrategy.getParameterNames(constructor);
      secondStrategyControl.setThrowable(new IntrospectionException(""));
      
      firstStrategyControl.replay();
      secondStrategyControl.replay();

      ParameterNameFetchingHandler handler = new ParameterNameFetchingHandler(Arrays.asList(new ParameterNameFetchingStrategy [] { firstStrategy, secondStrategy }));
      handler.getParameterNames(constructor);
      fail();
    } catch (IntrospectionException ex) {
    } catch (Exception ex) {
      fail();
    } finally {
      firstStrategyControl.verify();
      secondStrategyControl.verify();
    }
  }

  public void testMethodParameterNameFetchingWithNoFetching() {
    MockControl firstStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    MockControl secondStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    try {
      Method method = getClass().getDeclaredMethod("setUp");

      ParameterNameFetchingStrategy firstStrategy = (ParameterNameFetchingStrategy)(firstStrategyControl.getMock());
      ParameterNameFetchingStrategy secondStrategy = (ParameterNameFetchingStrategy)(secondStrategyControl.getMock());
      firstStrategy.getParameterNames(method);
      firstStrategyControl.setThrowable(new IntrospectionException(""));
      secondStrategy.getParameterNames(method);
      secondStrategyControl.setThrowable(new IntrospectionException(""));
      
      firstStrategyControl.replay();
      secondStrategyControl.replay();

      ParameterNameFetchingHandler handler = new ParameterNameFetchingHandler(Arrays.asList(new ParameterNameFetchingStrategy [] { firstStrategy, secondStrategy }));
      handler.getParameterNames(method);
      fail();
    } catch (IntrospectionException ex) {
    } catch (Exception ex) {
      fail();
    } finally {
      firstStrategyControl.verify();
      secondStrategyControl.verify();
    }
  }
  
  public void testConstructorParameterNameFetchingCaching() {
    MockControl firstStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    try {
      Constructor constructor = getClass().getDeclaredConstructor(String.class);
      Collection <String> parameterNames = new ArrayList<String> ();

      ParameterNameFetchingStrategy firstStrategy = (ParameterNameFetchingStrategy)(firstStrategyControl.getMock());
      firstStrategy.getParameterNames(constructor);
      firstStrategyControl.setReturnValue(parameterNames);
      
      firstStrategyControl.replay();

      ParameterNameFetchingHandler handler = new ParameterNameFetchingHandler(Arrays.asList(new ParameterNameFetchingStrategy [] { firstStrategy }));
      assertSame(handler.getParameterNames(constructor), handler.getParameterNames(constructor));
    } catch (Exception ex) {
      fail();
    } finally {
      firstStrategyControl.verify();
    }
  }
  
  public void testMethodParameterNameFetchingCaching() {
    MockControl firstStrategyControl = MockControl.createControl(ParameterNameFetchingStrategy.class);
    try {
      Method method = getClass().getDeclaredMethod("setUp");
      Collection <String> parameterNames = new ArrayList<String> ();

      ParameterNameFetchingStrategy firstStrategy = (ParameterNameFetchingStrategy)(firstStrategyControl.getMock());
      firstStrategy.getParameterNames(method);
      firstStrategyControl.setReturnValue(parameterNames);
      
      firstStrategyControl.replay();

      ParameterNameFetchingHandler handler = new ParameterNameFetchingHandler(Arrays.asList(new ParameterNameFetchingStrategy [] { firstStrategy }));
      assertSame(handler.getParameterNames(method), handler.getParameterNames(method));
    } catch (Exception ex) {
      fail();
    } finally {
      firstStrategyControl.verify();
    }
  }
}
