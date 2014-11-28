/*
 * PropertyStringficationStrategyTest.java
 * JUnit based test
 *
 * Created on 23 de Novembro de 2005, 00:42
 */

package br.com.appworks.runtime.lang.support.stringfication.test;

import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.stringfication.PropertyStringficationStrategy;
import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategy;
import junit.framework.*;
import org.easymock.MockControl;

public class PropertyStringficationStrategyTest extends TestCase {
  
  public PropertyStringficationStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }

  public void testNullStringfication() {
    Object op1 = new Object();
    Object propertyOp1 = new Object();
    MockControl propertyAccessStrategyControl = MockControl.createControl(PropertyGettingStrategy.class);
    MockControl stringficationStrategyControl = MockControl.createControl(StringficationStrategy.class);
   
    PropertyGettingStrategy <Object, Object> propertyAccessStrategy = (PropertyGettingStrategy <Object, Object>)(propertyAccessStrategyControl.getMock());
    StringficationStrategy <Object> stringficationStrategy = (StringficationStrategy <Object>)(stringficationStrategyControl.getMock());

    propertyAccessStrategyControl.replay();
    stringficationStrategyControl.replay();
    StringBuilder sb = new StringBuilder();

    new PropertyStringficationStrategy <Object, Object> (propertyAccessStrategy, stringficationStrategy).toString(null, sb);
    assertEquals("null", sb.toString());

    propertyAccessStrategyControl.verify();
    stringficationStrategyControl.verify();
  }

  public void testStringfication() {
    Object op1 = new Object();
    Object propertyOp1 = new Object();
    MockControl propertyAccessStrategyControl = MockControl.createControl(PropertyGettingStrategy.class);
    MockControl stringficationStrategyControl = MockControl.createControl(StringficationStrategy.class);
   
    PropertyGettingStrategy <Object, Object> propertyAccessStrategy = (PropertyGettingStrategy <Object, Object>)(propertyAccessStrategyControl.getMock());
    StringficationStrategy <Object> stringficationStrategy = (StringficationStrategy <Object>)(stringficationStrategyControl.getMock());

    StringBuilder sb = new StringBuilder();

    propertyAccessStrategy.getProperty();
    propertyAccessStrategyControl.setReturnValue("property");
    
    propertyAccessStrategy.get(op1);
    propertyAccessStrategyControl.setReturnValue(propertyOp1);
    
    stringficationStrategy.toString(propertyOp1, sb);
    
    propertyAccessStrategyControl.replay();
    stringficationStrategyControl.replay();
    
    new PropertyStringficationStrategy <Object, Object> (propertyAccessStrategy, stringficationStrategy).toString(op1, sb);
    assertTrue(sb.toString().contains("property : "));

    propertyAccessStrategyControl.verify();
    stringficationStrategyControl.verify();
  }
  
  public void testClone() {
    try {
      StringBuilder sb = new StringBuilder();

      Object op1 = new Object();
      Object propertyOp1 = new Object();
      MockControl propertyAccessStrategyControl = MockControl.createControl(PropertyGettingStrategy.class);
      MockControl stringficationStrategyControl = MockControl.createControl(StringficationStrategy.class);

      PropertyGettingStrategy <Object, Object> propertyAccessStrategy = (PropertyGettingStrategy <Object, Object>)(propertyAccessStrategyControl.getMock());
      StringficationStrategy <Object> stringficationStrategy = (StringficationStrategy <Object>)(stringficationStrategyControl.getMock());

      propertyAccessStrategy.getProperty();
      propertyAccessStrategyControl.setReturnValue("property");

      propertyAccessStrategy.get(op1);
      propertyAccessStrategyControl.setReturnValue(propertyOp1);

      stringficationStrategy.toString(propertyOp1, sb);

      propertyAccessStrategyControl.replay();
      stringficationStrategyControl.replay();
      PropertyStringficationStrategy strategy = (PropertyStringficationStrategy)(new PropertyStringficationStrategy <Object, Object> (propertyAccessStrategy, stringficationStrategy).clone());
      strategy.toString(op1, sb);
      assertTrue(sb.toString().contains("property : "));

      propertyAccessStrategyControl.verify();
      stringficationStrategyControl.verify();
    } catch (Exception ex) {
      fail();
    }
  }

}
