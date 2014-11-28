/*
 * CollectionCloningStrategyTest.java
 * JUnit based test
 *
 * Created on 11 de Agosto de 2005, 23:36
 */

package br.com.appworks.runtime.lang.support.cloning.test;

import br.com.appworks.runtime.lang.support.cloning.CollectionCloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.Cloneable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import junit.framework.*;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class CollectionCloningStrategyTest extends TestCase {
  private static class PrivateCollection extends ArrayList <Object> {
  }
  public static class NoDefaultConstructorCollection extends ArrayList <Object> {
    public NoDefaultConstructorCollection(int x) {
    }
  }

  public CollectionCloningStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNullCollectionCloning() {
    try {
      assertNull(new CollectionCloningStrategy <Object> ().clone(null));
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  public void testEmptyCollectionCloning() {
    try {
      Collection <Object> source = new ArrayList <Object> ();
      Collection <Object> clone = new CollectionCloningStrategy <Object> ().clone(source);
      assertTrue(clone instanceof ArrayList);
      assertTrue(clone.isEmpty());
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  public void testNullElementsCollectionCloning() {
    try {
      Collection <Object> source = new ArrayList(Arrays.asList(new Object[] { null, null }));
      assertNotSame(source, new CollectionCloningStrategy <Object> ().clone(source));
      assertEquals(2, new CollectionCloningStrategy <Object> ().clone(source).size());
      assertEquals(source, new CollectionCloningStrategy <Object> ().clone(source));
      Iterator i = new CollectionCloningStrategy <Object> ().clone(source).iterator();
      while (i.hasNext()) {
        assertNull(i.next());
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testNonCloneableElementsCollectionCloning() {
    try {
      Collection <Object> source = new ArrayList(Arrays.asList(new Object[] { "X", "Y" }));
      assertNotSame(source, new CollectionCloningStrategy <Object> ().clone(source));
      assertEquals(2, new CollectionCloningStrategy <Object> ().clone(source).size());
      assertEquals(source, new CollectionCloningStrategy <Object> ().clone(source));
      Iterator i = new CollectionCloningStrategy <Object> ().clone(source).iterator();
      Iterator i2 = source.iterator();
      while (i.hasNext()) {
        Object op1 = i.next();
        Object op2 = i2.next();
        assertSame(op1, op2);
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testCloneableElementsCollectionCloning() {
    try {
      Collection <Object> source = new ArrayList(Arrays.asList(new Object[] { Calendar.getInstance(), Calendar.getInstance() }));
      assertNotSame(source, new CollectionCloningStrategy <Object> ().clone(source));
      assertEquals(2, new CollectionCloningStrategy <Object> ().clone(source).size());
      assertEquals(source, new CollectionCloningStrategy <Object> ().clone(source));
      Iterator i = new CollectionCloningStrategy <Object> ().clone(source).iterator();
      Iterator i2 = source.iterator();
      while (i.hasNext()) {
        Object op1 = i.next();
        Object op2 = i2.next();
        assertNotSame(op1, op2);
        assertEquals(op1, op2);
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testMixedElementsCollectionCloning() {
    try {
      Collection <Object> source = new ArrayList(Arrays.asList(new Object[] { Calendar.getInstance(), "X", null }));
      assertNotSame(source, new CollectionCloningStrategy <Object> ().clone(source));
      assertEquals(3, new CollectionCloningStrategy <Object> ().clone(source).size());
      assertEquals(source, new CollectionCloningStrategy <Object> ().clone(source));
      Iterator i = new CollectionCloningStrategy <Object> ().clone(source).iterator();
      Iterator i2 = source.iterator();
      while (i.hasNext()) {
        Object op1 = i.next();
        Object op2 = i2.next();
        if (op1 instanceof java.lang.Cloneable) {
          assertNotSame(op1, op2);
          assertEquals(op1, op2);
        } else {
          assertSame(op1, op2);
        }
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testCloneNotSupportedElementCollectionCloning() {
    MockControl control = MockControl.createControl(Cloneable.class);
    CloneNotSupportedException cnsex = new CloneNotSupportedException();
    try {
      Cloneable cloneable = (Cloneable)(control.getMock());
      cloneable.clone();
      control.setThrowable(cnsex);
      control.replay();
      Collection <Object> source = new ArrayList(Arrays.asList(new Object[] { cloneable }));
      new CollectionCloningStrategy <Object> ().clone(source);
      fail();
    } catch (CloneNotSupportedException ex) {
    } finally {
      control.verify();
    }
  }
  
  public void testClone() {
    try {
      CollectionCloningStrategy strategy = new CollectionCloningStrategy();
      assertTrue(strategy.clone() instanceof CollectionCloningStrategy);
      assertNotSame(strategy, strategy.clone());
    } catch (Exception ex) {
      fail();
    }
  }
  public void testPrivateCollectionClone() {
    try {
      CollectionCloningStrategy strategy = new CollectionCloningStrategy();
      strategy.clone(new PrivateCollection());
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }
  public void testNoDefaultConstructorCollectionClone() {
    try {
      CollectionCloningStrategy strategy = new CollectionCloningStrategy();
      strategy.clone(new NoDefaultConstructorCollection(2));
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }

}
