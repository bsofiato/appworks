/*
 * PropertyGettingStrategyFactoryTest.java
 * JUnit based test
 *
 * Created on 19 de Novembro de 2005, 01:56
 */

package br.com.appworks.runtime.lang.support.property.getting.reflection.test;

import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import junit.framework.*;

public class ReflectionBasedPropertyGettingStrategyFactoryTest extends TestCase {

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
  
  public ReflectionBasedPropertyGettingStrategyFactoryTest(String testName) {
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
  
  public void testGetterBasedPropertyAccessStrategyCreation() {
    try {
      Method getter = TestBean.class.getDeclaredMethod("getProperty");
      TestBean testBean = new TestBean();
      assertEquals("PROPERTY", new ReflectionBasedPropertyGettingStrategyFactory().create(getter).get(testBean));
      assertEquals("property", new ReflectionBasedPropertyGettingStrategyFactory().create(getter).getProperty());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testDefaultGetterPropertyAccessStrategyCreation() {
    try {
      Field field = TestBean.class.getDeclaredField("property");
      assertEquals("PROPERTY", new ReflectionBasedPropertyGettingStrategyFactory().create(field, null).get(new TestBean()));
      assertEquals("property", new ReflectionBasedPropertyGettingStrategyFactory().create(field, null).getProperty());
      assertEquals("PROPERTY", new ReflectionBasedPropertyGettingStrategyFactory().create(field, "").get(new TestBean()));
      assertEquals("property", new ReflectionBasedPropertyGettingStrategyFactory().create(field, "").getProperty());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testOverridedGetterPropertyAccessStrategyCreation() {
    try {
      Field field = TestBean.class.getDeclaredField("property");
      assertEquals("OVERRIDE.PROPERTY", new ReflectionBasedPropertyGettingStrategyFactory().create(field, "getOverrideProperty").get(new TestBean()));
      assertEquals(field.getName(), new ReflectionBasedPropertyGettingStrategyFactory().create(field, "getOverrideProperty").getProperty());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testNullGetterPropertyAccessStrategyCreation() {
    try {
      Field field = TestBean.class.getDeclaredField("property");
      TestBean testBean = new TestBean();
      assertEquals("PROPERTY", new ReflectionBasedPropertyGettingStrategyFactory().create(field, null).get(testBean));
      assertEquals("property", new ReflectionBasedPropertyGettingStrategyFactory().create(field, null).getProperty());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testNullGetterMethodPropertyAccessStrategyCreation() {
    try {
      assertNull(new ReflectionBasedPropertyGettingStrategyFactory().create(null));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testEmptyGetterPropertyAccessStrategyCreation() {
    try {
      Field field = TestBean.class.getDeclaredField("property");
      TestBean testBean = new TestBean();
      assertEquals("PROPERTY", new ReflectionBasedPropertyGettingStrategyFactory().create(field, "").get(testBean));
      assertEquals("property", new ReflectionBasedPropertyGettingStrategyFactory().create(field, "").getProperty());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testMismatchedGetterPropertyAccessStrategyCreation() {
    try {
      Field field = TestBean.class.getDeclaredField("property");
      assertEquals(field.getName(), new ReflectionBasedPropertyGettingStrategyFactory().create(field, "ERROR").getProperty());
      assertEquals("FIELD.PROPERTY", new ReflectionBasedPropertyGettingStrategyFactory().create(field, "ERROR").get(new TestBean()));
      fail();
    } catch (NoSuchMethodError ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testPrivateFieldGetterPropertyAccessStrategyCreation() {
    try {
      Field field = TestBean.class.getDeclaredField("variable");
      assertEquals("VARIABLE", new ReflectionBasedPropertyGettingStrategyFactory().create(field, null).get(new TestBean()));
      assertEquals(field.getName(), new ReflectionBasedPropertyGettingStrategyFactory().create(field, null).getProperty());
      assertEquals("VARIABLE", new ReflectionBasedPropertyGettingStrategyFactory().create(field, "").get(new TestBean()));
      assertEquals(field.getName(), new ReflectionBasedPropertyGettingStrategyFactory().create(field, "").getProperty());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testDirectFieldPropertyGettingtrategyCreation() {
    try {
      Field field = TestBean.class.getDeclaredField("noGetterVariable");
      assertEquals("NOGETTER", new ReflectionBasedPropertyGettingStrategyFactory().create(field, null).get(new TestBean()));
      assertEquals(field.getName(), new ReflectionBasedPropertyGettingStrategyFactory().create(field, null).getProperty());
      assertEquals("NOGETTER", new ReflectionBasedPropertyGettingStrategyFactory().create(field, "").get(new TestBean()));
      assertEquals(field.getName(), new ReflectionBasedPropertyGettingStrategyFactory().create(field, "").getProperty());
    } catch (Exception ex) {
      fail();
    }
  }
}
