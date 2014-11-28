/*
 * ComparationHandlerTest.java
 * JUnit based test
 *
 * Created on 5 de Julho de 2005, 23:36
 */

package br.com.appworks.runtime.lang.support.comparation.test;
import br.com.appworks.runtime.lang.support.comparation.ComparationHandler;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategy;
import java.util.SortedSet;
import java.util.TreeSet;
import junit.framework.*;
import org.easymock.MockControl;
import br.com.appworks.runtime.lang.OrderPolicy;

/**
 *
 * @author Bubu
 */
public class ComparationHandlerTest extends TestCase {
  public static interface MockComparationStrategy extends ComparationStrategy, java.lang.Comparable <ComparationStrategy> {
  }
  public ComparationHandlerTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNullObjectHashCode() {
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    comparationStrategyControl1.replay();
    
    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> (); 
    
    map.add(comparationStrategy1);
    
    assertEquals(0, new ComparationHandler <String> (String.class, map).hashCode(null));
  
    comparationStrategyControl1.verify();
  }
  
  public void testNonNullObjectHashCode() {
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockControl comparationStrategyControl2 = MockControl.createControl(MockComparationStrategy.class);

    final MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    final MockComparationStrategy comparationStrategy2 = (MockComparationStrategy)(comparationStrategyControl2.getMock());

    comparationStrategy1.compareTo(comparationStrategy2); 
    comparationStrategyControl1.setDefaultReturnValue(-1);
    comparationStrategy2.compareTo(comparationStrategy1); 
    comparationStrategyControl2.setDefaultReturnValue(1);
    comparationStrategy1.hashCode("TESTE");
    comparationStrategyControl1.setReturnValue(2);
    comparationStrategy2.hashCode("TESTE");
    comparationStrategyControl2.setReturnValue(1);
    
    comparationStrategyControl1.replay();
    comparationStrategyControl2.replay();

    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();

    map.add(comparationStrategy2);
    map.add(comparationStrategy1);
    
    assertEquals(3, new ComparationHandler <String> (String.class, map).hashCode("TESTE"));
    
    comparationStrategyControl1.verify();
    comparationStrategyControl2.verify();
  }

  public void testNullOperandsEquals() {
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    
    comparationStrategyControl1.replay();
    
    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();
    map.add(comparationStrategy1);
    
    assertTrue(new ComparationHandler <String> (String.class, map).equals(null, null));
  
    comparationStrategyControl1.verify();
  }

  public void testNullFirstOperandEquals() {
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    
    comparationStrategyControl1.replay();
    
    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();
    map.add(comparationStrategy1);
    
    assertFalse(new ComparationHandler <String> (String.class, map).equals(null, "TESTE"));
  
    comparationStrategyControl1.verify();
  }

  public void testNullSecondOperandEquals() {
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    
    comparationStrategyControl1.replay();
    
    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();
    map.add(comparationStrategy1);
    
    assertFalse(new ComparationHandler <String> (String.class, map).equals("TESTE", null));
  
    comparationStrategyControl1.verify();
  }

  public void testSameOperandsEquals() {
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    
    comparationStrategyControl1.replay();
    
    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();
    map.add(comparationStrategy1);
    
    assertTrue(new ComparationHandler <String> (String.class, map).equals("TESTE", "TESTE"));
  
    comparationStrategyControl1.verify();
  }
  public void testFirstStrategyFalseEquals() {
    String op1 = "OP1";
    String op2 = "OP2";
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockControl comparationStrategyControl2 = MockControl.createControl(MockComparationStrategy.class);

    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    MockComparationStrategy  comparationStrategy2 = (MockComparationStrategy)(comparationStrategyControl2.getMock());

    comparationStrategy1.compareTo(comparationStrategy2); 
    comparationStrategyControl1.setDefaultReturnValue(-1);
    comparationStrategy2.compareTo(comparationStrategy1); 
    comparationStrategyControl2.setDefaultReturnValue(1);

    comparationStrategy1.equals(op1, op2);
    comparationStrategyControl1.setReturnValue(false);
    
    comparationStrategyControl1.replay();
    comparationStrategyControl2.replay();

    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();

    map.add(comparationStrategy2);
    map.add(comparationStrategy1);
    
    assertFalse(new ComparationHandler <String> (String.class, map).equals(op1, op2));
    
    comparationStrategyControl1.verify();
    comparationStrategyControl2.verify();

  }
  
  public void testSecondStrategyFalseEquals() {
    String op1 = "OP1";
    String op2 = "OP2";
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockControl comparationStrategyControl2 = MockControl.createControl(MockComparationStrategy.class);

    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    MockComparationStrategy comparationStrategy2 = (MockComparationStrategy)(comparationStrategyControl2.getMock());

    comparationStrategy1.equals(op1, op2);
    comparationStrategyControl1.setReturnValue(true);

    comparationStrategy2.equals(op1, op2);
    comparationStrategyControl2.setReturnValue(false);

    comparationStrategy1.compareTo(comparationStrategy2); 
    comparationStrategyControl1.setDefaultReturnValue(-1);
    comparationStrategy2.compareTo(comparationStrategy1); 
    comparationStrategyControl2.setDefaultReturnValue(1);

    comparationStrategyControl1.replay();
    comparationStrategyControl2.replay();

    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();

    map.add(comparationStrategy2);
    map.add(comparationStrategy1);
    
    assertFalse(new ComparationHandler <String> (String.class, map).equals(op1, op2));
    
    comparationStrategyControl1.verify();
    comparationStrategyControl2.verify();

  }
  
  public void testAllStrategiesTrueEquals() {
    String op1 = "OP1";
    String op2 = "OP2";
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockControl comparationStrategyControl2 = MockControl.createControl(MockComparationStrategy.class);

    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    MockComparationStrategy comparationStrategy2 = (MockComparationStrategy)(comparationStrategyControl2.getMock());

    comparationStrategy1.equals(op1, op2);
    comparationStrategyControl1.setReturnValue(true);

    comparationStrategy2.equals(op1, op2);
    comparationStrategyControl2.setReturnValue(true);

    comparationStrategy1.compareTo(comparationStrategy2); 
    comparationStrategyControl1.setDefaultReturnValue(-1);
    comparationStrategy2.compareTo(comparationStrategy1); 
    comparationStrategyControl2.setDefaultReturnValue(1);

    comparationStrategyControl1.replay();
    comparationStrategyControl2.replay();

    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();

    map.add(comparationStrategy2);
    map.add(comparationStrategy1);
    
    assertTrue(new ComparationHandler <String> (String.class, map).equals(op1, op2));
    
    comparationStrategyControl1.verify();
    comparationStrategyControl2.verify();
  }
  
  public void testNullOperandsCompare() {
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    
    comparationStrategyControl1.replay();
    
    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();
    map.add(comparationStrategy1);
    
    assertEquals(0, new ComparationHandler <String> (String.class, map).compare(null, null));
  
    comparationStrategyControl1.verify();
  }
  public void testSameOperandsCompare() {
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    
    comparationStrategyControl1.replay();
    
    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();
    map.add(comparationStrategy1);
    
    assertEquals(0, new ComparationHandler <String> (String.class, map).compare("TEST", "TEST"));
  
    comparationStrategyControl1.verify();
  }

  public void testNullFirstOperandCompare() {
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    
    comparationStrategyControl1.replay();
    
    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();
    map.add(comparationStrategy1);
    try {
      new ComparationHandler <String> (String.class, map).compare(null, "TEST");
      fail();
    } catch (NullPointerException ex) {
    } catch (Exception ex) {
    }
    comparationStrategyControl1.verify();
  }
  public void testNullSecondOperandCompare() {
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    
    comparationStrategyControl1.replay();
    
    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();
    map.add(comparationStrategy1);
    try {
      new ComparationHandler <String> (String.class, map).compare("TEST", null);
      fail();
    } catch (NullPointerException ex) {
    } catch (Exception ex) {
    }
    comparationStrategyControl1.verify();
  }

  public void testNotSameTypeOperandsCompare() {
    Object obj1 = new Object();
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    
    comparationStrategy1.compare("TEST", obj1);
    comparationStrategyControl1.setThrowable(new ClassCastException());
    comparationStrategyControl1.replay();
    
    SortedSet <ComparationStrategy <Object>> map = new TreeSet <ComparationStrategy <Object>> ();
    map.add(comparationStrategy1);
    try {
      new ComparationHandler <Object> (Object.class, map).compare("TEST", obj1);
      fail();
    } catch (ClassCastException ex) {
    } catch (Exception ex) {
    }
    comparationStrategyControl1.verify();
  }

  public void testFirstStrategyNotEqualsCompare() {
    String op1 = "OP1";
    String op2 = "OP2";
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockControl comparationStrategyControl2 = MockControl.createControl(MockComparationStrategy.class);

    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    MockComparationStrategy comparationStrategy2 = (MockComparationStrategy)(comparationStrategyControl2.getMock());

    comparationStrategy1.compare(op1, op2);
    comparationStrategyControl1.setReturnValue(-1);

    comparationStrategy1.compareTo(comparationStrategy2); 
    comparationStrategyControl1.setDefaultReturnValue(-1);
    comparationStrategy2.compareTo(comparationStrategy1); 
    comparationStrategyControl2.setDefaultReturnValue(1);

    comparationStrategyControl1.replay();
    comparationStrategyControl2.replay();

    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();

    map.add(comparationStrategy2);
    map.add(comparationStrategy1);
    
    assertEquals(-1, new ComparationHandler <String> (String.class, map).compare(op1, op2));
    
    comparationStrategyControl1.verify();
    comparationStrategyControl2.verify();
  }  
  
  public void testSecondStrategyNotEqualsCompare() {
    String op1 = "OP1";
    String op2 = "OP2";
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockControl comparationStrategyControl2 = MockControl.createControl(MockComparationStrategy.class);

    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    MockComparationStrategy comparationStrategy2 = (MockComparationStrategy)(comparationStrategyControl2.getMock());

    comparationStrategy1.compare(op1, op2);
    comparationStrategyControl1.setReturnValue(0);

    comparationStrategy2.compare(op1, op2);
    comparationStrategyControl2.setReturnValue(1);

    comparationStrategy1.compareTo(comparationStrategy2); 
    comparationStrategyControl1.setDefaultReturnValue(-1);
    comparationStrategy2.compareTo(comparationStrategy1); 
    comparationStrategyControl2.setDefaultReturnValue(1);

    comparationStrategyControl1.replay();
    comparationStrategyControl2.replay();

    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();

    map.add(comparationStrategy2);
    map.add(comparationStrategy1);
        
    assertEquals(1, new ComparationHandler <String> (String.class, map).compare(op1, op2));
    
    comparationStrategyControl1.verify();
    comparationStrategyControl2.verify();
  }

  public void testAllStrategiesEqualsCompare() {
    String op1 = "OP1";
    String op2 = "OP2";
    MockControl comparationStrategyControl1 = MockControl.createControl(MockComparationStrategy.class);
    MockControl comparationStrategyControl2 = MockControl.createControl(MockComparationStrategy.class);

    MockComparationStrategy comparationStrategy1 = (MockComparationStrategy)(comparationStrategyControl1.getMock());
    MockComparationStrategy comparationStrategy2 = (MockComparationStrategy)(comparationStrategyControl2.getMock());

    comparationStrategy1.compare(op1, op2);
    comparationStrategyControl1.setReturnValue(0);

    comparationStrategy2.compare(op1, op2);
    comparationStrategyControl2.setReturnValue(0);

    comparationStrategy1.compareTo(comparationStrategy2); 
    comparationStrategyControl1.setDefaultReturnValue(-1);
    comparationStrategy2.compareTo(comparationStrategy1); 
    comparationStrategyControl2.setDefaultReturnValue(1);
    
    comparationStrategyControl1.replay();
    comparationStrategyControl2.replay();

    SortedSet <ComparationStrategy <String>> map = new TreeSet <ComparationStrategy <String>> ();

    map.add(comparationStrategy2);
    map.add(comparationStrategy1);
    
    assertEquals(0, new ComparationHandler <String> (String.class, map).compare(op1, op2));
    
    comparationStrategyControl1.verify();
    comparationStrategyControl2.verify();
  }
  
  public void testCompareWithClassCast() {
    try {
      ((ComparationHandler)(new ComparationHandler <String> (String.class))).compare(new Integer(1), new Integer(2));
      fail();
    } catch (ClassCastException ex) {
    }
  }

  public void testSetOrderPolicy() {
    try {
      new ComparationHandler <String> (String.class).setOrderPolicy(OrderPolicy.NATURAL);
      fail();
    } catch (UnsupportedOperationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testGetOrderPolicy() {
    try {
      new ComparationHandler <String> (String.class).getOrderPolicy();
      fail();
    } catch (UnsupportedOperationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }

  public void testClone() {
    try {
      new ComparationHandler <String> (String.class).clone();
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }
  
}
