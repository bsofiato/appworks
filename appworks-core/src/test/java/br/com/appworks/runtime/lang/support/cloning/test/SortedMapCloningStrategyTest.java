/*
 * SortedMapCloningStrategyTest.java
 * JUnit based test
 *
 * Created on 14 de Agosto de 2005, 18:27
 */

package br.com.appworks.runtime.lang.support.cloning.test;
import br.com.appworks.runtime.lang.support.cloning.SortedMapCloningStrategy;
import java.util.Comparator;
import br.com.appworks.runtime.lang.support.cloning.Cloneable;
import java.text.Collator;
import java.util.Calendar;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import junit.framework.*;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import org.easymock.MockControl;


public class SortedMapCloningStrategyTest extends TestCase {
  public static interface MockHelperInterface extends Cloneable, Comparable {
  }  
  private static class PrivateSortedMap extends TreeMap <Object, Object> {
  }
  public static class NoDefaultConstructorSortedMap extends TreeMap <Object, Object> {
    public NoDefaultConstructorSortedMap(int x) {
    }
  }
  public static class NoComparatorConstructorSortedMap extends TreeMap <String, String> {
    public NoComparatorConstructorSortedMap() {
      super(Collator.getInstance());
    }
  }
  public static class ComparatorConstructorExceptionSortedMap extends TreeMap <String, String> {
    public ComparatorConstructorExceptionSortedMap() {
      super(Collator.getInstance());
    }
    public ComparatorConstructorExceptionSortedMap(Comparator comparator) throws Exception {
      throw new Exception();
    }
  }

  public static interface CloneableComparator <Type> extends Cloneable, Comparator <Type> {
    public Type clone() throws CloneNotSupportedException;
  }

  public SortedMapCloningStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNullMapCloning() {
    try {
      assertNull(new SortedMapCloningStrategy <Object, Object> ().clone(null));
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testEmptySortedMapCloningNullComparator() {
    try {
      SortedMap <Object, Object> source = new TreeMap <Object, Object> ();
      SortedMap <Object, Object> clone = new SortedMapCloningStrategy <Object, Object> ().clone(source);
      assertTrue(clone instanceof TreeMap);
      assertTrue(clone.isEmpty());
      assertNull(clone.comparator());
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  public void testEmptySortedMapCloningNonCloneableComparator() {
     MockControl control = MockControl.createControl(Comparator.class);
     try {
      Comparator <Object> comparator = (Comparator <Object>)(control.getMock());
      control.replay();
      SortedMap <Object, Object> source = new TreeMap <Object, Object> (comparator);
      SortedMap <Object, Object> clone = new SortedMapCloningStrategy <Object, Object> ().clone(source);
      assertTrue(clone instanceof TreeMap);
      assertTrue(clone.isEmpty());
      assertSame(comparator, clone.comparator());
    } catch (CloneNotSupportedException ex) {
      fail();
    } finally {
      control.verify();
    }
  }
  
  public void testEmptySortedMapCloningCloneableComparator() {
     MockControl control = MockControl.createControl(CloneableComparator.class);
     MockControl cloneControl = MockControl.createControl(CloneableComparator.class);
     try {
      CloneableComparator <Object> comparator = (CloneableComparator <Object>)(control.getMock());
      CloneableComparator <Object> cloneComparator = (CloneableComparator <Object>)(cloneControl.getMock());
      comparator.clone();
      control.setReturnValue(cloneComparator);
      cloneControl.replay();
      control.replay();
      SortedMap <Object, Object> source = new TreeMap <Object, Object> (comparator);
      SortedMap <Object, Object> clone = new SortedMapCloningStrategy <Object, Object> ().clone(source);
      assertTrue(clone instanceof TreeMap);
      assertTrue(clone.isEmpty());
      assertSame(cloneComparator, clone.comparator());
    } catch (CloneNotSupportedException ex) {
      fail();
    } finally {
      control.verify();
      cloneControl.verify();
    }
  }
  
  public void testEmptySortedMapCloningCloneableComparatorCloningException() {
     MockControl control = MockControl.createControl(CloneableComparator.class);
     MockControl cloneControl = MockControl.createControl(CloneableComparator.class);
     CloneNotSupportedException cnse = new CloneNotSupportedException();
     try {
      CloneableComparator <Object> comparator = (CloneableComparator <Object>)(control.getMock());
      CloneableComparator <Object> cloneComparator = (CloneableComparator <Object>)(cloneControl.getMock());
      comparator.clone();
      control.setThrowable(cnse);
      cloneControl.replay();
      control.replay();
      SortedMap <Object, Object> source = new TreeMap <Object, Object> (comparator);
      SortedMap <Object, Object> clone = new SortedMapCloningStrategy <Object, Object> ().clone(source);
      fail();
    } catch (CloneNotSupportedException ex) {
      assertSame(cnse, ex);
    } finally {
      control.verify();
      cloneControl.verify();
    }
  }

  
  public void testEmptySortedMapCloning() {
    try {
      SortedMap <Object, Object> source = new TreeMap <Object, Object> ();
      SortedMap <Object, Object> clone = new SortedMapCloningStrategy <Object, Object> ().clone(source);
      assertTrue(clone instanceof TreeMap);
      assertEquals(source, clone);
      assertNotSame(source, clone);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testNullValueSortedMapCloning() {
    try {
      SortedMap <Object, Object> source = new TreeMap <Object, Object> ();
      source.put("TESTE", null);
      source.put("TESTE2", null);
      Map <Object, Object> clone = new SortedMapCloningStrategy <Object, Object> ().clone(source);
      assertTrue(clone instanceof TreeMap);
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
  
  public void testNonCloneableValueSortedMapCloning() {
    try {
      SortedMap <Object, Object> source = new TreeMap <Object, Object> ();
      source.put("TESTE", "TESTE");
      source.put("TESTE2", "TESTE2");
      Map <Object, Object> clone = new SortedMapCloningStrategy <Object, Object> ().clone(source);
      assertTrue(clone instanceof TreeMap);
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
  public void testCloneableValueSortedMapCloning() {
    try {
      SortedMap <Object, Object> source = new TreeMap <Object, Object> ();
      source.put("TESTE", Calendar.getInstance());
      source.put("TESTE2", Calendar.getInstance());
      Map <Object, Object> clone = new SortedMapCloningStrategy <Object, Object> ().clone(source);
      assertEquals(source, clone);
      assertNotSame(source, clone);
      assertTrue(clone instanceof TreeMap);
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
  
   public void testCloneNotSupportedValueSortedMapCloning() {
    MockControl control = MockControl.createControl(Cloneable.class);
    CloneNotSupportedException cnsex = new CloneNotSupportedException();
    try {
      Cloneable cloneable = (Cloneable)(control.getMock());
      cloneable.clone();
      control.setThrowable(cnsex);
      control.replay();
      SortedMap <Object, Object> source = new TreeMap <Object, Object> ();
      source.put("TESTE", cloneable);
      new SortedMapCloningStrategy <Object, Object> ().clone(source);
      fail();
    } catch (CloneNotSupportedException ex) {
    } finally {
      control.verify();
    }
  }
   
  public void testNonCloneableKeySortedMapCloning() {
    try {
      SortedMap <Object, Object> source = new TreeMap <Object, Object> ();
      source.put("TESTE", "TESTE");
      source.put("TESTE2", "TESTE2");
      SortedMap <Object, Object> clone = new SortedMapCloningStrategy <Object, Object> ().clone(source);
      assertTrue(clone instanceof TreeMap);
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
  
  public void testCloneableKeySortedMapCloning() {
    try {
      Calendar key1 = Calendar.getInstance();
      Calendar key2 = Calendar.getInstance();
      key2.add(Calendar.MONTH, 1);
      SortedMap <Calendar, Object> source = new TreeMap <Calendar, Object> ();
      source.put(key1, "TESTE");
      source.put(key2, "TESTE");
      SortedMap <Calendar, Object> clone = new SortedMapCloningStrategy <Calendar, Object> ().clone(source);
      assertTrue(clone instanceof TreeMap);
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
  
  public void testCloneNotSupportedKeySortedMapCloning() {
    MockHelperInterface cloneable = createMock(MockHelperInterface.class);
    CloneNotSupportedException cnsex = new CloneNotSupportedException();
    try {
      expect(cloneable.compareTo(cloneable)).andReturn(0);
      expect(cloneable.clone()).andThrow(cnsex);
      replay(cloneable);
      SortedMap <Object, Object> source = new TreeMap <Object, Object> ();
      source.put(cloneable, "TESTE");
      new SortedMapCloningStrategy <Object, Object> ().clone(source);
      fail();
    } catch (CloneNotSupportedException ex) {
    } finally {
      verify(cloneable);
    }
  }
  
  public void testClone() {
    try {
      SortedMapCloningStrategy strategy = new SortedMapCloningStrategy();
      assertTrue(strategy.clone() instanceof SortedMapCloningStrategy);
      assertNotSame(strategy, strategy.clone());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testPrivateSortedMapClone() {
    try {
      SortedMapCloningStrategy strategy = new SortedMapCloningStrategy();
      strategy.clone(new PrivateSortedMap());
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }
  public void testNoDefaultConstructorSortedMapClone() {
    try {
      SortedMapCloningStrategy strategy = new SortedMapCloningStrategy();
      strategy.clone(new NoDefaultConstructorSortedMap(2));
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }
  public void testNoComparatorConstructorSortedMapClone() {
    try {
      SortedMapCloningStrategy strategy = new SortedMapCloningStrategy();
      strategy.clone(new NoComparatorConstructorSortedMap());
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }
  public void testComparatorConstructorExceptionSortedMapClone() {
    try {
      SortedMapCloningStrategy strategy = new SortedMapCloningStrategy();
      strategy.clone(new ComparatorConstructorExceptionSortedMap());
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }

}
