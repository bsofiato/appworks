/*
 * AbstractComparationStrategyTest.java
 * JUnit based test
 *
 * Created on 3 de Julho de 2005, 23:47
 */

package br.com.appworks.runtime.lang.support.comparation.test;

import br.com.appworks.runtime.lang.OrderPolicy;
import br.com.appworks.runtime.lang.support.comparation.AbstractComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategy;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class AbstractComparationStrategyTest extends TestCase {
  private static class MockComparationStrategy <Type extends Object> extends AbstractComparationStrategy <Type> {
    protected int doCompare(Type op1, Type op2) {
      return -1;
    }
    public MockComparationStrategy(OrderPolicy orderPolicy, int index) {
      setOrderPolicy(orderPolicy);
      setIndex(index);
    }
    public int hashCode(Type object) {
      throw new UnsupportedOperationException();
    }
    public boolean equals(Type op1, Type op2) {
      if (op1 == op2) { 
        return true;
      } else if (op1 == null) {
        return false;
      } else {
        return op1.equals(op2);
      }
    }
  }
  
  protected void checkContract(ComparationStrategy  strategy, Object op1, Object op2, Object op3) {
    assertTrue(strategy.equals(null, null));
    assertEquals(0, strategy.compare(null, null));
    assertEquals(strategy.hashCode(null), strategy.hashCode(null));
    assertTrue(strategy.equals(op1, op1));
    assertEquals(0, strategy.compare(op1, op1));
    assertEquals(strategy.hashCode(op1), strategy.hashCode(op1));
    assertTrue(strategy.equals(op2, op2));
    assertEquals(0, strategy.compare(op2, op2));
    assertEquals(strategy.hashCode(op2), strategy.hashCode(op2));
    assertTrue(strategy.equals(op3, op3));
    assertEquals(0, strategy.compare(op3, op3));
    assertEquals(strategy.hashCode(op3), strategy.hashCode(op3));
    assertFalse(strategy.equals(null, op1));
    assertTrue(0 != strategy.compare(null, op1));
    assertFalse(strategy.equals(op1, null));
    assertTrue(0 != strategy.compare(op1, null));
    assertTrue(Math.signum(strategy.compare(null, op1)) == (-1 * Math.signum(strategy.compare(op1, null))));
    assertFalse(strategy.equals(null, op2));
    assertTrue(0 != strategy.compare(null, op2));
    assertFalse(strategy.equals(op2, null));
    assertTrue(0 != strategy.compare(op2, null));
    assertTrue(Math.signum(strategy.compare(null, op2)) == (-1 * Math.signum(strategy.compare(op2, null))));
    assertFalse(strategy.equals(null, op3));
    assertTrue(0 != strategy.compare(null, op3));
    assertFalse(strategy.equals(op3, null));
    assertTrue(0 != strategy.compare(op3, null));
    assertTrue(Math.signum(strategy.compare(null, op3)) == (-1 * Math.signum(strategy.compare(op3, null))));
    if (strategy.equals(op1, op2)) {
      assertTrue(strategy.equals(op2, op1));
      assertEquals(strategy.hashCode(op1), strategy.hashCode(op2));
      assertEquals(0, strategy.compare(op2, op1));
      assertEquals(0, strategy.compare(op1, op2));
    } else {
      assertFalse(strategy.equals(op2, op1));
      assertTrue(0 != strategy.compare(op2, op1));
      assertTrue(Math.signum(strategy.compare(op2, op1)) == (-1 * Math.signum(strategy.compare(op1, op2))));
    }
    if (strategy.equals(op1, op3)) {
      assertTrue(strategy.equals(op3, op1));
      assertEquals(strategy.hashCode(op3), strategy.hashCode(op1));
      assertEquals(0, strategy.compare(op3, op1));
      assertEquals(0, strategy.compare(op1, op3));
    } else {
      assertFalse(strategy.equals(op3, op1));
      assertTrue(0 != strategy.compare(op3, op1));
      assertTrue(Math.signum(strategy.compare(op3, op1)) == (-1 * Math.signum(strategy.compare(op1, op3))));
    }
    if (strategy.equals(op2, op3)) {
      assertTrue(strategy.equals(op3, op2));
      assertEquals(strategy.hashCode(op3), strategy.hashCode(op2));
      assertEquals(0, strategy.compare(op3, op2));
      assertEquals(0, strategy.compare(op2, op3));
    } else {
      assertFalse(strategy.equals(op3, op2));
      assertTrue(0 != strategy.compare(op3, op2));
      assertTrue(Math.signum(strategy.compare(op3, op2)) == (-1 * Math.signum(strategy.compare(op2, op3))));
    }
    if (strategy.equals(op1, op2) && strategy.equals(op2, op3)) {
      assertTrue(strategy.equals(op1, op3));
      assertEquals(strategy.hashCode(op1), strategy.hashCode(op2));
      assertEquals(strategy.hashCode(op1), strategy.hashCode(op3));
      assertEquals(strategy.hashCode(op2), strategy.hashCode(op3));
      assertEquals(0, strategy.compare(op3, op2));
      assertEquals(0, strategy.compare(op2, op1));
      assertEquals(0, strategy.compare(op3, op1));
    }
    if (strategy.equals(op1, op3) && strategy.equals(op2, op3)) {
      assertTrue(strategy.equals(op1, op2));
      assertEquals(strategy.hashCode(op1), strategy.hashCode(op2));
      assertEquals(strategy.hashCode(op1), strategy.hashCode(op3));
      assertEquals(strategy.hashCode(op2), strategy.hashCode(op3));
      assertEquals(0, strategy.compare(op3, op2));
      assertEquals(0, strategy.compare(op2, op1));
      assertEquals(0, strategy.compare(op3, op1));
    }
  }
  public AbstractComparationStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testGetOrderPolicy() {
    assertEquals(OrderPolicy.NATURAL, new MockComparationStrategy(OrderPolicy.NATURAL, 0).getOrderPolicy());
    assertEquals(OrderPolicy.INVERSE, new MockComparationStrategy(OrderPolicy.INVERSE, 0).getOrderPolicy());
  }

  public void testGetParametrizedIndex() {
    assertEquals(0, new MockComparationStrategy(OrderPolicy.NATURAL, 0).getIndex());
    assertEquals(1, new MockComparationStrategy(OrderPolicy.NATURAL, 1).getIndex());
  }
  
  public void testNullOperandsCompare() {
    assertEquals(0, new MockComparationStrategy(OrderPolicy.NATURAL, 0).compare(null, null));
  }
  public void testSameOperandsCompare() {
    Object operand = new Object();
    assertEquals(0, new MockComparationStrategy(OrderPolicy.NATURAL, 0).compare(operand, operand));
  }
  public void testFirstOperandNullCompare() {
    Object operand = new Object();
    assertEquals(-1, new MockComparationStrategy(OrderPolicy.NATURAL, 0).compare(null, operand));
  }
  public void testSecondOperandNullCompare() {
    Object operand = new Object();
    assertEquals(1, new MockComparationStrategy(OrderPolicy.NATURAL, 0).compare(operand, null));
  }


  public void testCompare() {
    Object op1 = new Object();
    Object op2 = new Object();
    assertEquals(-1, new MockComparationStrategy(OrderPolicy.NATURAL, 0).compare(op1, op2));
    assertEquals(1, new MockComparationStrategy(OrderPolicy.INVERSE, 0).compare(op1, op2));
  }
  
  public void testClone() {
    try {
      MockComparationStrategy strategy = new MockComparationStrategy(OrderPolicy.NATURAL, 0);
      assertEquals(strategy.getOrderPolicy(), ((MockComparationStrategy)(strategy.clone())).getOrderPolicy());
      assertEquals(strategy.getIndex(), ((MockComparationStrategy)(strategy.clone())).getIndex());
      assertNotSame(strategy, strategy.clone());
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
}
