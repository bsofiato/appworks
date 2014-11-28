/*
 * MapCloningStrategyTest.java
 * JUnit based test
 *
 * Created on 14 de Agosto de 2005, 01:00
 */

package br.com.appworks.runtime.lang.support.cloning.test;
import br.com.appworks.runtime.lang.support.cloning.MapCloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.Cloneable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import junit.framework.*;
import org.easymock.MockControl;


/**
 *
 * @author Bubu
 */
public class MapCloningStrategyTest extends TestCase {
  private static class PrivateMap extends HashMap <Object, Object> {
  }
  public static class NoDefaultConstructorMap extends HashMap <Object, Object> {
    public NoDefaultConstructorMap(int x) {
    }
  }
  public MapCloningStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNullMapCloning() {
    try {
      assertNull(new MapCloningStrategy <Object, Object> ().clone(null));
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  public void testEmptyMapCloning() {
    try {
      Map <Object, Object> source = new HashMap <Object, Object> ();
      Map <Object, Object> clone = new MapCloningStrategy <Object, Object> ().clone(source);
      assertTrue(clone instanceof HashMap);
      assertEquals(source, clone);
      assertNotSame(source, clone);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testNullValueMapCloning() {
    try {
      Map <Object, Object> source = new HashMap <Object, Object> ();
      source.put("TESTE", null);
      source.put("TESTE2", null);
      Map <Object, Object> clone = new MapCloningStrategy <Object, Object> ().clone(source);
      assertTrue(clone instanceof HashMap);
      assertEquals(source, clone);
      assertNotSame(source, clone);
      assertTrue(clone.containsKey("TESTE"));
      assertSame(source.get("TESTE"), clone.get("TESTE"));
      assertTrue(clone.containsKey("TESTE2"));
      assertSame(source.get("TESTE2"), clone.get("TESTE2"));
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testNonCloneableValueMapCloning() {
    try {
      Map <Object, Object> source = new HashMap <Object, Object> ();
      source.put("TESTE", "TESTE");
      source.put("TESTE2", "TESTE2");
      Map <Object, Object> clone = new MapCloningStrategy <Object, Object> ().clone(source);
      assertTrue(clone instanceof HashMap);
      assertEquals(source, clone);
      assertNotSame(source, clone);
      assertTrue(clone.containsKey("TESTE"));
      assertSame(source.get("TESTE"), clone.get("TESTE"));
      assertTrue(clone.containsKey("TESTE2"));
      assertSame(source.get("TESTE2"), clone.get("TESTE2"));
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  public void testCloneableValueMapCloning() {
    try {
      Map <Object, Object> source = new HashMap <Object, Object> ();
      source.put("TESTE", Calendar.getInstance());
      source.put("TESTE2", Calendar.getInstance());
      Map <Object, Object> clone = new MapCloningStrategy <Object, Object> ().clone(source);
      assertEquals(source, clone);
      assertNotSame(source, clone);
      assertTrue(clone instanceof HashMap);
      assertTrue(clone.containsKey("TESTE"));
      assertNotSame(source.get("TESTE"), clone.get("TESTE"));
      assertEquals(source.get("TESTE"), clone.get("TESTE"));
      assertTrue(clone.containsKey("TESTE2"));
      assertNotSame(source.get("TESTE2"), clone.get("TESTE2"));
      assertEquals(source.get("TESTE2"), clone.get("TESTE2"));
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
   public void testCloneNotSupportedValueMapCloning() {
    MockControl control = MockControl.createControl(Cloneable.class);
    CloneNotSupportedException cnsex = new CloneNotSupportedException();
    try {
      Cloneable cloneable = (Cloneable)(control.getMock());
      cloneable.clone();
      control.setThrowable(cnsex);
      control.replay();
      Map <Object, Object> source = new HashMap <Object, Object> ();
      source.put("TESTE", cloneable);
      new MapCloningStrategy <Object, Object> ().clone(source);
      fail();
    } catch (CloneNotSupportedException ex) {
    } finally {
      control.verify();
    }
  }
   
  public void testNonCloneableKeyMapCloning() {
    try {
      Map <Object, Object> source = new HashMap <Object, Object> ();
      source.put("TESTE", "TESTE");
      source.put("TESTE2", "TESTE2");
      Map <Object, Object> clone = new MapCloningStrategy <Object, Object> ().clone(source);
      assertTrue(clone instanceof HashMap);
      assertEquals(source, clone);
      assertNotSame(source, clone);
      for (Object k1 : source.keySet()) {
        assertTrue(clone.containsKey(k1));
        for (Object k2 : clone.keySet()) {
          if (k1.equals(k2)) {
            assertSame(k1, k2);
          }
        }
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  public void testCloneableKeyMapCloning() {
    try {
      Calendar key1 = Calendar.getInstance();
      Calendar key2 = Calendar.getInstance();
      key2.add(Calendar.MONTH, 1);
      Map <Calendar, Object> source = new HashMap <Calendar, Object> ();
      source.put(key1, "TESTE");
      source.put(key2, "TESTE");
      Map <Calendar, Object> clone = new MapCloningStrategy <Calendar, Object> ().clone(source);
      assertTrue(clone instanceof HashMap);
      assertEquals(source, clone);
      assertNotSame(source, clone);
      for (Calendar k1 : source.keySet()) {
        assertTrue(clone.containsKey(k1));
        for (Calendar k2 : clone.keySet()) {
          assertNotSame(k1, k2);
        }
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testCloneNotSupportedKeyMapCloning() {
    MockControl control = MockControl.createControl(Cloneable.class);
    CloneNotSupportedException cnsex = new CloneNotSupportedException();
    try {
      Cloneable cloneable = (Cloneable)(control.getMock());
      cloneable.clone();
      control.setThrowable(cnsex);
      control.replay();
      Map <Object, Object> source = new HashMap <Object, Object> ();
      source.put(cloneable, "TESTE");
      new MapCloningStrategy <Object, Object> ().clone(source);
      fail();
    } catch (CloneNotSupportedException ex) {
    } finally {
      control.verify();
    }
  }
  
  public void testClone() {
    try {
      MapCloningStrategy strategy = new MapCloningStrategy();
      assertTrue(strategy.clone() instanceof MapCloningStrategy);
      assertNotSame(strategy, strategy.clone());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testPrivateMapClone() {
    try {
      MapCloningStrategy strategy = new MapCloningStrategy();
      strategy.clone(new PrivateMap());
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }
  public void testNoDefaultConstructorMapClone() {
    try {
      MapCloningStrategy strategy = new MapCloningStrategy();
      strategy.clone(new NoDefaultConstructorMap(2));
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }
}
