/*
 * ReflectionBasedPropertySettingStrategyFactoryTest.java
 * JUnit based test
 *
 * Created on 19 de Novembro de 2005, 02:20
 */

package br.com.appworks.runtime.lang.support.property.setting.reflection.test;

import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import junit.framework.*;

public class ReflectionBasedPropertySettingStrategyFactoryTest extends TestCase {

  public static class TestBean {
    private String property = "FIELD.PROPERTY";
    private String variable = "FIELD.VARIABLE";
    private String noGetterVariable="NOGETTER";
    public void setProperty(String property) {
      this.property = "setProperty";
    }
    public String getProperty() {
      return "PROPERTY";
    }
    public String getOverrideProperty() {
      return "OVERRIDE.PROPERTY";
    }
    public void setOverridedProperty(String overrideProperty) {
      this.property = "setOverridedProperty";
    }
    
    private String getVariable() {
      return "VARIABLE";
    }
    private void setVariable(String variable) {
      this.variable = "setVariable";
    }
  }
  
  public ReflectionBasedPropertySettingStrategyFactoryTest(String testName) {
    super(testName);
  }
  
  private Object createMethodBasedRequest(Method getter, Method setter) throws Exception {
    Class klass = DefaultPropertyAccessStrategyFactory.class.getDeclaredClasses()[1];
    Constructor constructor = klass.getDeclaredConstructor(Method.class, Method.class);
    constructor.setAccessible(true);
    return constructor.newInstance(getter, setter);
  }
  private Object createFieldBasedRequest(Field field, String getter, String setter) throws Exception {
    Class klass = DefaultPropertyAccessStrategyFactory.class.getDeclaredClasses()[2];
    Constructor constructor = klass.getDeclaredConstructor(Field.class, String.class, String.class);
    constructor.setAccessible(true);
    return constructor.newInstance(field, getter, setter);
  }
  
  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNullSetterPropertyAccessStrategyCreation() {
    try {
      Field field = TestBean.class.getDeclaredField("property");
      TestBean testBean = new TestBean();
      new ReflectionBasedPropertySettingStrategyFactory().create(field, null).set(testBean, "Test");
      assertEquals(field.getName(), new ReflectionBasedPropertySettingStrategyFactory().create(field, null).getProperty());
      assertEquals("setProperty", testBean.property);
    } catch (Exception ex) {
      fail();
    }
  }

  public void testEmptySetterPropertyAccessStrategyCreation() {
    try {
      Field field = TestBean.class.getDeclaredField("property");
      TestBean testBean = new TestBean();
      new ReflectionBasedPropertySettingStrategyFactory().create(field, "").set(testBean, "Test");
      assertEquals(field.getName(), new ReflectionBasedPropertySettingStrategyFactory().create(field, "").getProperty());
      assertEquals("setProperty", testBean.property);
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testNullSetterMethodPropertyAccessStrategyCreation() {
    try {
      assertNull(new ReflectionBasedPropertySettingStrategyFactory().create(null));
    } catch (Exception ex) {
      fail();
    }
  }
  

  public void testSetterBasedPropertyAccessStrategyCreation() {
    try {
      Method setter = TestBean.class.getDeclaredMethod("setProperty", String.class);
      TestBean testBean = new TestBean();
      new ReflectionBasedPropertySettingStrategyFactory().create(setter).set(testBean, "Test");
      assertEquals("property", new ReflectionBasedPropertySettingStrategyFactory().create(setter).getProperty());
      assertEquals("setProperty", testBean.property);
    } catch (Exception ex) {
      fail();
    }
  }

  
  public void testDefaultSetterPropertyAccessStrategyCreation() {
    try {
      Field field = TestBean.class.getDeclaredField("property");
      TestBean bean = new TestBean();
      new ReflectionBasedPropertySettingStrategyFactory().create(field, null).set(bean, null);
      assertEquals(field.getName(), new ReflectionBasedPropertySettingStrategyFactory().create(field, null).getProperty());
      assertEquals("setProperty", bean.property);
      bean = new TestBean();
      new ReflectionBasedPropertySettingStrategyFactory().create(field, "").set(bean, null);
      assertEquals(field.getName(), new ReflectionBasedPropertySettingStrategyFactory().create(field, "").getProperty());
      assertEquals("setProperty", bean.property);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testOverridedSetterPropertyAccessStrategyCreation() {
    try {
      Field field = TestBean.class.getDeclaredField("property");
      TestBean bean = new TestBean();
      new ReflectionBasedPropertySettingStrategyFactory().create(field, "setOverridedProperty").set(bean, null);
      assertEquals("setOverridedProperty", bean.property);
      assertEquals(field.getName(), new ReflectionBasedPropertySettingStrategyFactory().create(field, "setOverridedProperty").getProperty());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testMismatchedSetterPropertyAccessStrategyCreation() {
    try {
      Field field = TestBean.class.getDeclaredField("property");
      TestBean bean = new TestBean();
      new ReflectionBasedPropertySettingStrategyFactory().create(field, "ERROR").set(bean, null);
      assertEquals(field.getName(), new ReflectionBasedPropertySettingStrategyFactory().create(field, "ERROR").getProperty());
      fail();
    } catch (NoSuchMethodError ex) {
    } catch (Exception ex) {
      fail();
    }
  }

  public void testPrivateFieldSetterPropertyAccessStrategyCreation() {
    try {
      Field field = TestBean.class.getDeclaredField("variable");
      TestBean bean = new TestBean();
      new ReflectionBasedPropertySettingStrategyFactory().create(field, null).set(bean, null);
      assertEquals("setVariable", bean.variable);
      assertEquals(field.getName(), new ReflectionBasedPropertySettingStrategyFactory().create(field, null).getProperty());

      bean = new TestBean();
      new ReflectionBasedPropertySettingStrategyFactory().create(field, "").set(bean, null);
      assertEquals(field.getName(), new ReflectionBasedPropertySettingStrategyFactory().create(field, "").getProperty());
      assertEquals("setVariable", bean.variable);
    } catch (Exception ex) {
      fail();
    }
  }
  

  public void testDirectFieldPropertySettingStrategyCreation() {
    try {
      Field field = TestBean.class.getDeclaredField("noGetterVariable");
      TestBean bean = new TestBean();
      new ReflectionBasedPropertySettingStrategyFactory().create(field, null).set(bean, "NOGETTERSETTING");
      assertEquals(field.getName(), new ReflectionBasedPropertySettingStrategyFactory().create(field, null).getProperty());
      assertEquals("NOGETTERSETTING", bean.noGetterVariable);
      bean = new TestBean();
      new ReflectionBasedPropertySettingStrategyFactory().create(field, "").set(bean, "NOGETTERSETTING");
      assertEquals(field.getName(), new ReflectionBasedPropertySettingStrategyFactory().create(field, "").getProperty());
      assertEquals("NOGETTERSETTING", bean.noGetterVariable);
    } catch (Exception ex) {
      fail();
    }
  }
}
