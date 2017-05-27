/*
 * ArrayCloningStrategyTest.java
 * JUnit based test
 *
 * Created on 11 de Agosto de 2005, 00:10
 */

package br.com.appworks.runtime.lang.support.cloning.test;

import br.com.appworks.runtime.lang.support.cloning.ArrayCloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.Cloneable;
import java.util.Calendar;
import junit.framework.*;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class ArrayCloningStrategyTest extends TestCase {
  
  public ArrayCloningStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  public void testNullArrayCloning() {
    try {
      assertNull(new ArrayCloningStrategy <Object> (Object.class).clone(null));
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  } 
  public void testEmptyArrayCloning() {
    try {
      Object [] array = new Object[0];
      assertNotSame(array, new ArrayCloningStrategy <Object []> (Object.class).clone(array));
      assertEquals(0, new ArrayCloningStrategy <Object []> (Object.class).clone(array).length);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testNullElementsArrayCloning() {
    try {
      Object [] array = new Object[] { null, null };
      assertNotSame(array, new ArrayCloningStrategy <Object []> (Object.class).clone(array));
      assertEquals(2, new ArrayCloningStrategy <Object []> (Object.class).clone(array).length);
      assertNull(new ArrayCloningStrategy <Object []> (Object.class).clone(array)[0]);
      assertNull(new ArrayCloningStrategy <Object []> (Object.class).clone(array)[1]);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testNonCloneableElementsArrayCloning() {
    try {
      String [] array = new String[] { "X", "Y" };
      assertNotSame(array, new ArrayCloningStrategy <String []> (String.class).clone(array));
      assertEquals(2, new ArrayCloningStrategy <String []> (String.class).clone(array).length);
      assertSame(array[0], new ArrayCloningStrategy <String []> (String.class).clone(array)[0]);
      assertSame(array[1], new ArrayCloningStrategy <String []> (String.class).clone(array)[1]);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testCloneableElementsArrayCloning() {
    try {
      Calendar [] array = new Calendar[] { Calendar.getInstance(), Calendar.getInstance() };
      assertNotSame(array, new ArrayCloningStrategy <Calendar []> (Calendar.class).clone(array));
      assertEquals(2, new ArrayCloningStrategy <Calendar []> (Calendar.class).clone(array).length);
      assertNotSame(array[0], new ArrayCloningStrategy <Calendar []> (Calendar.class).clone(array)[0]);
      assertEquals(array[0], new ArrayCloningStrategy <Calendar []> (Calendar.class).clone(array)[0]);
      assertNotSame(array[1], new ArrayCloningStrategy <Calendar []> (Calendar.class).clone(array)[1]);
      assertEquals(array[1], new ArrayCloningStrategy <Calendar []> (Calendar.class).clone(array)[1]);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testMixedElementsArrayCloning() {
    try {
      Object [] array = new Object[] { "X", Calendar.getInstance(), null };
      assertNotSame(array, new ArrayCloningStrategy <Object []> (Object.class).clone(array));
      assertEquals(3, new ArrayCloningStrategy <Object []> (Object.class).clone(array).length);
      assertSame(array[0], new ArrayCloningStrategy <Object []> (Object.class).clone(array)[0]);
      assertNotSame(array[1], new ArrayCloningStrategy <Object []> (Object.class).clone(array)[1]);
      assertEquals(array[1], new ArrayCloningStrategy <Object []> (Object.class).clone(array)[1]);
      assertNull(new ArrayCloningStrategy <Object []> (Object.class).clone(array)[2]);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testCloneNotSupportedElementArrayCloning() {
    MockControl control = MockControl.createControl(Cloneable.class);
    CloneNotSupportedException cnsex = new CloneNotSupportedException();
    try {
      Cloneable cloneable = (Cloneable)(control.getMock());
      cloneable.clone();
      control.setThrowable(cnsex);
      control.replay();
      Cloneable [] array = new Cloneable[] { cloneable };
      new ArrayCloningStrategy <Cloneable []> (Cloneable.class).clone(array);
      fail();
    } catch (CloneNotSupportedException ex) {
    } finally {
      control.verify();
    }
  }
  
  public void testClone() {
    try {
      ArrayCloningStrategy strategy = new ArrayCloningStrategy(Object.class);
      assertTrue(strategy.clone() instanceof ArrayCloningStrategy);
      assertNotSame(strategy, strategy.clone());
    } catch (Exception ex) {
      fail();
    }
  }
}
