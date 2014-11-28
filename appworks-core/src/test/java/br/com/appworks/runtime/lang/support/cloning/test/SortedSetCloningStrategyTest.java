/*
 * SortedSetCloningStrategyTest.java
 * JUnit based test
 *
 * Created on 13 de Agosto de 2005, 01:00
 */

package br.com.appworks.runtime.lang.support.cloning.test;
import br.com.appworks.runtime.lang.support.cloning.SortedSetCloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.Cloneable;
import java.text.Collator;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import junit.framework.*;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import org.easymock.MockControl;


/**
 *
 * @author Bubu
 */
public class SortedSetCloningStrategyTest extends TestCase {
  public static interface MockHelperInterface extends Cloneable, Comparable {
    
  }
  private static class PrivateSortedSet extends TreeSet <Object> {
  }
  public static class NoDefaultConstructorSortedSet extends TreeSet <Object> {
    public NoDefaultConstructorSortedSet(int x) {
    }
  }
  public static class NoComparatorConstructorSortedSet extends TreeSet <String> {
    public NoComparatorConstructorSortedSet() {
      super(Collator.getInstance());
    }
  }
  public static class ComparatorConstructorExceptionSortedSet extends TreeSet <String> {
    public ComparatorConstructorExceptionSortedSet() {
      super(Collator.getInstance());
    }
    public ComparatorConstructorExceptionSortedSet(Comparator comparator) throws Exception {
      throw new Exception();
    }
  }
  public static interface CloneableComparator <Type> extends Cloneable, Comparator <Type> {
    public Type clone() throws CloneNotSupportedException;
  }
  public SortedSetCloningStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNullSortedSetCloning() {
    try {
      assertNull(new SortedSetCloningStrategy <Object> ().clone(null));
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  public void testEmptySortedSetCloningNullComparator() {
    try {
      SortedSet <Object> source = new TreeSet <Object> ();
      SortedSet <Object> clone = new SortedSetCloningStrategy <Object> ().clone(source);
      assertTrue(clone instanceof TreeSet);
      assertTrue(clone.isEmpty());
      assertNull(clone.comparator());
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  public void testEmptySortedSetCloningNonCloneableComparator() {
     MockControl control = MockControl.createControl(Comparator.class);
     try {
      Comparator <Object> comparator = (Comparator <Object>)(control.getMock());
      control.replay();
      SortedSet <Object> source = new TreeSet <Object> (comparator);
      SortedSet <Object> clone = new SortedSetCloningStrategy <Object> ().clone(source);
      assertTrue(clone instanceof TreeSet);
      assertTrue(clone.isEmpty());
      assertSame(comparator, clone.comparator());
    } catch (CloneNotSupportedException ex) {
      fail();
    } finally {
      control.verify();
    }
  }
  
  public void testEmptySortedSetCloningCloneableComparator() {
     MockControl control = MockControl.createControl(CloneableComparator.class);
     MockControl cloneControl = MockControl.createControl(CloneableComparator.class);
     try {
      CloneableComparator <Object> comparator = (CloneableComparator <Object>)(control.getMock());
      CloneableComparator <Object> cloneComparator = (CloneableComparator <Object>)(cloneControl.getMock());
      comparator.clone();
      control.setReturnValue(cloneComparator);
      cloneControl.replay();
      control.replay();
      SortedSet <Object> source = new TreeSet <Object> (comparator);
      SortedSet <Object> clone = new SortedSetCloningStrategy <Object> ().clone(source);
      assertTrue(clone instanceof TreeSet);
      assertTrue(clone.isEmpty());
      assertSame(cloneComparator, clone.comparator());
    } catch (CloneNotSupportedException ex) {
      fail();
    } finally {
      control.verify();
      cloneControl.verify();
    }
  }
  
  public void testEmptySortedSetCloningCloneableComparatorCloningException() {
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
      SortedSet <Object> source = new TreeSet <Object> (comparator);
      SortedSet <Object> clone = new SortedSetCloningStrategy <Object> ().clone(source);
      fail();
    } catch (CloneNotSupportedException ex) {
      assertSame(cnse, ex);
    } finally {
      control.verify();
      cloneControl.verify();
    }
  }
  
  /*public void testNullElementsSortedSetCloning() {
    try {
      SortedSet <Object> source = new TreeSet(Arrays.asList(new Object[] { null }));
      assertNotSame(source, new SortedSetCloningStrategy <Object> ().clone(source));
      assertEquals(1, new SortedSetCloningStrategy <Object> ().clone(source).size());
      Iterator i = new SortedSetCloningStrategy <Object> ().clone(source).iterator();
      while (i.hasNext()) {
        assertNull(i.next());
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }*/

  public void testNonCloneableElementsSortedSetCloning() {
    try {
      SortedSet <Object> source = new TreeSet(Arrays.asList(new Object[] { "X", "Y" }));
      assertNotSame(source, new SortedSetCloningStrategy <Object> ().clone(source));
      assertEquals(2, new SortedSetCloningStrategy <Object> ().clone(source).size());
      assertEquals(source, new SortedSetCloningStrategy <Object> ().clone(source));
      Iterator i = new SortedSetCloningStrategy <Object> ().clone(source).iterator();
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

  public void testCloneableElementsSortedSetCloning() {
    try {
      Calendar c1 = Calendar.getInstance();
      Calendar c2 = Calendar.getInstance();
      c2.add(Calendar.MONTH, 1);
      SortedSet <Object> source = new TreeSet(Arrays.asList(new Object[] { c1, c2 }));
      assertNotSame(source, new SortedSetCloningStrategy <Object> ().clone(source));
      assertEquals(2, new SortedSetCloningStrategy <Object> ().clone(source).size());
      assertEquals(source, new SortedSetCloningStrategy <Object> ().clone(source));
      Iterator i = new SortedSetCloningStrategy <Object> ().clone(source).iterator();
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
  
  public void testMixedElementsSortedSetCloning() {
    try {
      SortedSet <Object> source = new TreeSet <Object> (new Comparator <Object> () {
        public int compare(Object op1, Object op2) {
          int op1HashCode = (op1 == null) ? 0 : op1.hashCode();
          int op2HashCode = (op2 == null) ? 0 : op2.hashCode();
          return op1HashCode - op2HashCode;
        }
      });
      source.addAll(Arrays.asList(new Object[] { Calendar.getInstance(), "X", null }));
      assertNotSame(source, new SortedSetCloningStrategy <Object> ().clone(source));
      assertEquals(3, new SortedSetCloningStrategy <Object> ().clone(source).size());
      assertEquals(source, new SortedSetCloningStrategy <Object> ().clone(source));
      assertSame(source.comparator(), new SortedSetCloningStrategy <Object> ().clone(source).comparator());
      Iterator i = new SortedSetCloningStrategy <Object> ().clone(source).iterator();
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

  
  public void testCloneNotSupportedElementSortedSetCloning() {
    MockHelperInterface cloneable = createMock(MockHelperInterface.class);
    CloneNotSupportedException cnsex = new CloneNotSupportedException();
    try {
      expect(cloneable.compareTo(cloneable)).andStubReturn(0);
      expect(cloneable.clone()).andThrow(cnsex);
      replay(cloneable);
      SortedSet <Object> source = new TreeSet();
      source.add(cloneable);
      new SortedSetCloningStrategy <Object> ().clone(source);
      fail();
    } catch (CloneNotSupportedException ex) {
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      verify(cloneable);
    }
  }
  
  public void testClone() {
    try {
      SortedSetCloningStrategy strategy = new SortedSetCloningStrategy();
      assertTrue(strategy.clone() instanceof SortedSetCloningStrategy);
      assertNotSame(strategy, strategy.clone());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testPrivateSortedSetClone() {
    try {
      SortedSetCloningStrategy strategy = new SortedSetCloningStrategy();
      strategy.clone(new PrivateSortedSet());
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }
  public void testNoDefaultConstructorSortedSetClone() {
    try {
      SortedSetCloningStrategy strategy = new SortedSetCloningStrategy();
      strategy.clone(new NoDefaultConstructorSortedSet(2));
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }
  public void testNoComparatorConstructorSortedSetClone() {
    try {
      SortedSetCloningStrategy strategy = new SortedSetCloningStrategy();
      strategy.clone(new NoComparatorConstructorSortedSet());
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }
  public void testComparatorConstructorExceptionSortedSetClone() {
    try {
      SortedSetCloningStrategy strategy = new SortedSetCloningStrategy();
      strategy.clone(new ComparatorConstructorExceptionSortedSet());
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }
}

