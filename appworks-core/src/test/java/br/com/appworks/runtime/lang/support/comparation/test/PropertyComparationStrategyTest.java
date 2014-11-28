/*
 * PropertyComparationStrategyTest.java
 * JUnit based test
 *
 * Created on 5 de Julho de 2005, 01:22
 */

package br.com.appworks.runtime.lang.support.comparation.test;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.PropertyComparationStrategy;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import java.util.Iterator;
import java.util.TreeSet;
import junit.framework.*;
import org.easymock.MockControl;
import br.com.appworks.runtime.lang.OrderPolicy;

public class PropertyComparationStrategyTest extends TestCase {
  
  public PropertyComparationStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testPropertyComparationStrategyEquals() {
    Object op1 = new Object();
    Object op2 = new Object();
    Object propertyOp1 = new Object();
    Object propertyOp2 = new Object();
    MockControl propertyAccessStrategyControl = MockControl.createControl(PropertyGettingStrategy.class);
    MockControl comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
   
    PropertyGettingStrategy <Object, Object> propertyAccessStrategy = (PropertyGettingStrategy <Object, Object>)(propertyAccessStrategyControl.getMock());
    ComparationStrategy <Object> comparationStrategy = (ComparationStrategy <Object>)(comparationStrategyControl.getMock());

    propertyAccessStrategy.get(op1);
    propertyAccessStrategyControl.setReturnValue(propertyOp1);
    propertyAccessStrategy.get(op2);
    propertyAccessStrategyControl.setReturnValue(propertyOp2);
    
    comparationStrategy.equals(propertyOp1, propertyOp2);
    comparationStrategyControl.setReturnValue(true);
    
    propertyAccessStrategyControl.replay();
    comparationStrategyControl.replay();
    
    assertTrue(new PropertyComparationStrategy <Object, Object> (0, propertyAccessStrategy, comparationStrategy).equals(op1, op2));

    propertyAccessStrategyControl.verify();
    comparationStrategyControl.verify();
  }

  public void testPropertyComparationStrategyHashCode() {
    Object op1 = new Object();
    Object propertyOp1 = new Object();
    MockControl propertyAccessStrategyControl = MockControl.createControl(PropertyGettingStrategy.class);
    MockControl comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
   
    PropertyGettingStrategy <Object, Object> propertyAccessStrategy = (PropertyGettingStrategy <Object, Object>)(propertyAccessStrategyControl.getMock());
    ComparationStrategy <Object> comparationStrategy = (ComparationStrategy <Object>)(comparationStrategyControl.getMock());

    propertyAccessStrategy.get(op1);
    propertyAccessStrategyControl.setReturnValue(propertyOp1);
    
    comparationStrategy.hashCode(propertyOp1);
    comparationStrategyControl.setReturnValue(1);
    
    propertyAccessStrategyControl.replay();
    comparationStrategyControl.replay();
    
    assertEquals(1, new PropertyComparationStrategy <Object, Object> (0, propertyAccessStrategy, comparationStrategy).hashCode(op1));

    propertyAccessStrategyControl.verify();
    comparationStrategyControl.verify();
  }

  public void testPropertyComparationStrategyCompare() {
    Object op1 = new Object();
    Object op2 = new Object();
    Object propertyOp1 = new Object();
    Object propertyOp2 = new Object();
    MockControl propertyAccessStrategyControl = MockControl.createControl(PropertyGettingStrategy.class);
    MockControl comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
   
    PropertyGettingStrategy <Object, Object> propertyAccessStrategy = (PropertyGettingStrategy <Object, Object>)(propertyAccessStrategyControl.getMock());
    ComparationStrategy <Object> comparationStrategy = (ComparationStrategy <Object>)(comparationStrategyControl.getMock());

    propertyAccessStrategy.get(op1);
    propertyAccessStrategyControl.setReturnValue(propertyOp1);
    propertyAccessStrategy.get(op2);
    propertyAccessStrategyControl.setReturnValue(propertyOp2);
    
    comparationStrategy.compare(propertyOp1, propertyOp2);
    comparationStrategyControl.setReturnValue(1);
    
    propertyAccessStrategyControl.replay();
    comparationStrategyControl.replay();
    
    assertEquals(1, new PropertyComparationStrategy <Object, Object> (0, propertyAccessStrategy, comparationStrategy).compare(op1, op2));

    propertyAccessStrategyControl.verify();
    comparationStrategyControl.verify();
  }
  
  public void testGetOrderPolicy() {
    MockControl comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
    ComparationStrategy <Object> comparationStrategy = (ComparationStrategy <Object>)(comparationStrategyControl.getMock());
    comparationStrategy.getOrderPolicy();
    comparationStrategyControl.setReturnValue(OrderPolicy.NATURAL);
    comparationStrategyControl.replay();
    assertEquals(OrderPolicy.NATURAL, new PropertyComparationStrategy <Object, Object> (0, null, comparationStrategy).getOrderPolicy());
    comparationStrategyControl.verify();
  }

  public void testSetOrderPolicy() {
    MockControl comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
    ComparationStrategy <Object> comparationStrategy = (ComparationStrategy <Object>)(comparationStrategyControl.getMock());
    comparationStrategy.setOrderPolicy(OrderPolicy.NATURAL);
    comparationStrategyControl.replay();
    new PropertyComparationStrategy <Object, Object> (0, null, comparationStrategy).setOrderPolicy(OrderPolicy.NATURAL);
    comparationStrategyControl.verify();
  }

  public void testClone() {
    try {
      new PropertyComparationStrategy <Object, Object> (0, null, null).clone();
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }
  
  public void testCompareToWithSameEvaluationOrder() {
    assertEquals(0, new PropertyComparationStrategy <Object, Object>(0, null, null).compareTo(new PropertyComparationStrategy <Object, Object>(0, null, null)));
  }
  
  public void testCompareToLesserEvalutionOrder() {
    assertTrue(new PropertyComparationStrategy <Object, Object>(0, null, null).compareTo(new PropertyComparationStrategy <Object, Object>(1, null, null)) < 0);
  }

  public void testCompareToGreaterEvalutionOrder() {
    assertTrue(0 < new PropertyComparationStrategy <Object, Object>(2, null, null).compareTo(new PropertyComparationStrategy <Object, Object>(1, null, null)));
  }
  
  public void testSortedSetIterationSanityCheck() {
    TreeSet <PropertyComparationStrategy> treeSet = new TreeSet <PropertyComparationStrategy> ();
    treeSet.add(new PropertyComparationStrategy <Object, Object>(2, null, null));
    treeSet.add(new PropertyComparationStrategy <Object, Object>(1, null, null));
    Iterator <PropertyComparationStrategy> i = treeSet.iterator();
    assertEquals(1, i.next().getIndex());
    assertEquals(2, i.next().getIndex());
  }
  
  public void testHashCode() {
    PropertyComparationStrategy strategy = new PropertyComparationStrategy(2, null, null);
    assertEquals(2, strategy.hashCode());
  }
  
  public void testEqualsNull() {
    PropertyComparationStrategy strategy = new PropertyComparationStrategy(2, null, null);
    assertFalse(strategy.equals(null));
  }

  public void testEqualsNotPropertyComparationStrategy() {
    PropertyComparationStrategy strategy = new PropertyComparationStrategy(2, null, null);
    assertFalse(strategy.equals("A"));
  }
  
  public void testEqualsSamePropertyComparationStrategy() {
    PropertyComparationStrategy strategy = new PropertyComparationStrategy(2, null, null);
    assertTrue(strategy.equals(strategy));
  }

  public void testNotEquals() {
    PropertyComparationStrategy strategy = new PropertyComparationStrategy(2, null, null);
    PropertyComparationStrategy strategy2 = new PropertyComparationStrategy(3, null, null);
    assertFalse(strategy.equals(strategy2));
  }
  
  public void testEquals() {
    PropertyComparationStrategy strategy = new PropertyComparationStrategy(3, null, null);
    PropertyComparationStrategy strategy2 = new PropertyComparationStrategy(3, null, null);
    assertTrue(strategy.equals(strategy2));
  }
}
