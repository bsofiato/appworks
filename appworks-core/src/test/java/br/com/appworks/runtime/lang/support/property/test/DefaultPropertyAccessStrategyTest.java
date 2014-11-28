/*
 * DefaultPropertyAccessStrategyTest.java
 * JUnit based test
 *
 * Created on 13 de Julho de 2005, 17:01
 */

package br.com.appworks.runtime.lang.support.property.test;

import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategy;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.property.setting.PropertySettingStrategy;
import junit.framework.*;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class DefaultPropertyAccessStrategyTest extends TestCase {
  
  public DefaultPropertyAccessStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    System.gc();
  }
  

  protected void tearDown() throws Exception {
  }
  
  public void testNullGettingStrategy() {
    try {
      new DefaultPropertyAccessStrategy <Object, Object> ().get(null);
      fail();
    } catch (UnsupportedOperationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }

  public void testNullSettingStrategy() {
    try {
      new DefaultPropertyAccessStrategy <Object, Object> ().set(null, null);
      fail();
    } catch (UnsupportedOperationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }

  public void testNotNullGettingStrategy() {
    try {
      MockControl control = MockControl.createControl(PropertyGettingStrategy.class);
      PropertyGettingStrategy <String, String> propertyGettingStrategy = (PropertyGettingStrategy <String, String>)(control.getMock());
      propertyGettingStrategy.get("TEST");
      control.setReturnValue("TEST");
      control.replay();
      assertEquals("TEST", new DefaultPropertyAccessStrategy <String, String> (propertyGettingStrategy).get("TEST"));
      control.verify();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testNotNullSettingStrategy() {
    try {
      MockControl control = MockControl.createControl(PropertySettingStrategy.class);
      PropertySettingStrategy <String, String> propertySettingStrategy = (PropertySettingStrategy <String, String>)(control.getMock());
      propertySettingStrategy.set("TEST", "TEST");
      control.replay();
      new DefaultPropertyAccessStrategy <String, String> (null, propertySettingStrategy).set("TEST", "TEST");
      control.verify();
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testGetPropertyViaGetterStrategy() {
    try {
      MockControl control = MockControl.createControl(PropertyGettingStrategy.class);
      MockControl control2 = MockControl.createControl(PropertySettingStrategy.class);
      
      PropertyGettingStrategy <String, String> propertyGettingStrategy = (PropertyGettingStrategy <String, String>)(control.getMock());
      PropertySettingStrategy <String, String> propertySettingStrategy = (PropertySettingStrategy <String, String>)(control2.getMock());

      propertyGettingStrategy.getProperty();
      control.setReturnValue("TEST");
      control.replay();
      control2.replay();
      
      assertEquals("TEST", new DefaultPropertyAccessStrategy <String, String> (propertyGettingStrategy, propertySettingStrategy).getProperty());
      control.verify();
      control2.verify();
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testGetPropertyViaSetterStrategy() {
    try {
      MockControl control2 = MockControl.createControl(PropertySettingStrategy.class);
      
      PropertySettingStrategy <String, String> propertySettingStrategy = (PropertySettingStrategy <String, String>)(control2.getMock());

      propertySettingStrategy.getProperty();
      control2.setReturnValue("TEST");
      control2.replay();
      
      assertEquals("TEST", new DefaultPropertyAccessStrategy <String, String> (null, propertySettingStrategy).getProperty());
      control2.verify();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testGetPropertyNullGetterSetterStrategy() {
    assertEquals(null, new DefaultPropertyAccessStrategy <String, String> (null, null).getProperty());
  }

}
