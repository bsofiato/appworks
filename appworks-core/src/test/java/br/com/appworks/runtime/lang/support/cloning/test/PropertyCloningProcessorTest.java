/*
 * PropertyCloningProcessorTest.java
 * JUnit based test
 *
 * Created on 14 de Julho de 2005, 22:58
 */

package br.com.appworks.runtime.lang.support.cloning.test;

import br.com.appworks.runtime.lang.support.cloning.CloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.PropertyCloningProcessor;
import br.com.appworks.runtime.lang.support.property.PropertyAccessStrategy;
import junit.framework.*;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class PropertyCloningProcessorTest extends TestCase {
  
  public PropertyCloningProcessorTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  public void testProcessNull() {
    try {
      MockControl propertyAccessStrategyControl = MockControl.createControl(PropertyAccessStrategy.class);
      MockControl cloningStrategyControl = MockControl.createControl(CloningStrategy.class);
      PropertyAccessStrategy <Object, Object> propertyAccessStrategy = (PropertyAccessStrategy <Object, Object>)(propertyAccessStrategyControl.getMock());
      CloningStrategy <Object> cloningStrategy = (CloningStrategy <Object>)(cloningStrategyControl.getMock());

      propertyAccessStrategyControl.replay();
      cloningStrategyControl.replay();

      assertNull(new PropertyCloningProcessor <Object, Object> (propertyAccessStrategy, cloningStrategy).process(null));

      propertyAccessStrategyControl.verify();
      cloningStrategyControl.verify();
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  public void testProcess() {
    try {
      Object object = new Object();
      Object property = new Object();
      Object clonedProperty = new Object();
      
      MockControl propertyAccessStrategyControl = MockControl.createControl(PropertyAccessStrategy.class);
      MockControl cloningStrategyControl = MockControl.createControl(CloningStrategy.class);
      PropertyAccessStrategy <Object, Object> propertyAccessStrategy = (PropertyAccessStrategy <Object, Object>)(propertyAccessStrategyControl.getMock());
      CloningStrategy <Object> cloningStrategy = (CloningStrategy <Object>)(cloningStrategyControl.getMock());

      propertyAccessStrategy.get(object);
      propertyAccessStrategyControl.setReturnValue(property);
      
      cloningStrategy.clone(property);
      cloningStrategyControl.setReturnValue(clonedProperty);
      
      propertyAccessStrategy.set(object, clonedProperty);

      propertyAccessStrategyControl.replay();
      cloningStrategyControl.replay();

      assertSame(object, new PropertyCloningProcessor <Object, Object> (propertyAccessStrategy, cloningStrategy).process(object));

      propertyAccessStrategyControl.verify();
      cloningStrategyControl.verify();
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testCloneNotSupportedExceptionProcess() {
    MockControl propertyAccessStrategyControl = MockControl.createControl(PropertyAccessStrategy.class);
    MockControl cloningStrategyControl = MockControl.createControl(CloningStrategy.class);
    CloneNotSupportedException exp = new CloneNotSupportedException();
    try {
      Object object = new Object();
      Object property = new Object();
      
      PropertyAccessStrategy <Object, Object> propertyAccessStrategy = (PropertyAccessStrategy <Object, Object>)(propertyAccessStrategyControl.getMock());
      CloningStrategy <Object> cloningStrategy = (CloningStrategy <Object>)(cloningStrategyControl.getMock());

      propertyAccessStrategy.get(object);
      propertyAccessStrategyControl.setReturnValue(property);
      
      cloningStrategy.clone(property);
      cloningStrategyControl.setThrowable(exp);
      
      propertyAccessStrategyControl.replay();
      cloningStrategyControl.replay();

      new PropertyCloningProcessor <Object, Object> (propertyAccessStrategy, cloningStrategy).process(object);
      fail();
    } catch (CloneNotSupportedException ex) {
      assertSame(exp, ex);
    } finally {
      propertyAccessStrategyControl.verify();
      cloningStrategyControl.verify();
   }
    
  }
}
