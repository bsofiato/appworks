/*
 * PropertyAccessStrategyFactoryTest.java
 * JUnit based test
 *
 * Created on 17 de Junho de 2005, 02:13
 */

package br.com.appworks.runtime.lang.support.property.test;

import br.com.appworks.runtime.lang.support.property.PropertyAccessStrategy;
import br.com.appworks.runtime.lang.support.property.PropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategy;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.PropertySettingStrategy;
import br.com.appworks.runtime.lang.support.property.setting.PropertySettingStrategyFactory;
import java.lang.reflect.Constructor;
import junit.framework.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class DefaultPropertyAccessStrategyFactoryTest extends TestCase {

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
    public String getOverridedProperty() {
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
  
  public DefaultPropertyAccessStrategyFactoryTest(String testName) {
    super(testName);
  }
  
  private Class getInnerClass(String className) throws ClassNotFoundException {
    for (Class klass : DefaultPropertyAccessStrategyFactory.class.getDeclaredClasses()) {
      if (klass.getSimpleName().equals(className)) {
        return klass;
      }
    }
    throw new ClassNotFoundException(className);
  }
  
  private Object createMethodBasedRequest(Method getter, Method setter) throws Exception {
    Class klass = getInnerClass("MethodBasedRequest");
    Constructor constructor = klass.getDeclaredConstructor(Method.class, Method.class);
    constructor.setAccessible(true);
    return constructor.newInstance(getter, setter);
  }
  private Object createFieldBasedRequest(Field field, String getter, String setter) throws Exception {
    Class klass = getInnerClass("FieldBasedRequest");
    Constructor constructor = klass.getDeclaredConstructor(Field.class, String.class, String.class);
    constructor.setAccessible(true);
    return constructor.newInstance(field, getter, setter);
  }
  
  private PropertyGettingStrategy getPropertyGettingStrategy(PropertyAccessStrategy strategy) throws Exception {
    Field field = DefaultPropertyAccessStrategy.class.getDeclaredField("propertyGettingStrategy");
    field.setAccessible(true);
    return (PropertyGettingStrategy)(field.get(strategy));
  }
  
  private PropertySettingStrategy getPropertySettingStrategy(PropertyAccessStrategy strategy) throws Exception {
    Field field = DefaultPropertyAccessStrategy.class.getDeclaredField("propertySettingStrategy");
    field.setAccessible(true);
    return (PropertySettingStrategy)(field.get(strategy));
  }
  
  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  
  public void testGetterBasedPropertyAccessStrategyCreation() {
    MockControl propertyGettingStrategyFactoryControl = MockControl.createControl(PropertyGettingStrategyFactory.class);
    MockControl propertySettingStrategyFactoryControl = MockControl.createControl(PropertySettingStrategyFactory.class);
    MockControl propertyGettingStrategyControl = MockControl.createControl(PropertyGettingStrategy.class);
    try {
      PropertyGettingStrategyFactory propertyGettingStrategyFactory = (PropertyGettingStrategyFactory)(propertyGettingStrategyFactoryControl.getMock());
      PropertySettingStrategyFactory propertySettingStrategyFactory = (PropertySettingStrategyFactory)(propertySettingStrategyFactoryControl.getMock());
      PropertyGettingStrategy propertyGettingStrategy = (PropertyGettingStrategy)(propertyGettingStrategyControl.getMock());
              
      Method getter = TestBean.class.getDeclaredMethod("getProperty");
      PropertyAccessStrategyFactory factory =  new DefaultPropertyAccessStrategyFactory(propertyGettingStrategyFactory, propertySettingStrategyFactory);

      propertyGettingStrategyFactory.create(getter);
      propertyGettingStrategyFactoryControl.setReturnValue(propertyGettingStrategy);

      propertySettingStrategyFactory.create(null);
      propertySettingStrategyFactoryControl.setReturnValue(null);

      propertyGettingStrategyFactoryControl.replay();
      propertySettingStrategyFactoryControl.replay();
      propertyGettingStrategyControl.replay();
      
      assertSame(propertyGettingStrategy, getPropertyGettingStrategy(factory.create(getter, null)));
      assertNull(getPropertySettingStrategy(factory.create(getter, null)));
    } catch (Exception ex) {
      fail();
    } finally {
      propertyGettingStrategyFactoryControl.verify();
      propertySettingStrategyFactoryControl.verify();
      propertyGettingStrategyControl.verify();
    }
  }
  
 
  public void testSetterBasedPropertyAccessStrategyCreation() {
    MockControl propertyGettingStrategyFactoryControl = MockControl.createControl(PropertyGettingStrategyFactory.class);
    MockControl propertySettingStrategyFactoryControl = MockControl.createControl(PropertySettingStrategyFactory.class);
    MockControl propertySettingStrategyControl = MockControl.createControl(PropertySettingStrategy.class);
    try {
      PropertyGettingStrategyFactory propertyGettingStrategyFactory = (PropertyGettingStrategyFactory)(propertyGettingStrategyFactoryControl.getMock());
      PropertySettingStrategyFactory propertySettingStrategyFactory = (PropertySettingStrategyFactory)(propertySettingStrategyFactoryControl.getMock());
      PropertySettingStrategy propertySettingStrategy = (PropertySettingStrategy)(propertySettingStrategyControl.getMock());
              
      Method setter = TestBean.class.getDeclaredMethod("setProperty", String.class);
      PropertyAccessStrategyFactory factory =  new DefaultPropertyAccessStrategyFactory(propertyGettingStrategyFactory, propertySettingStrategyFactory);

      propertyGettingStrategyFactory.create(null);
      propertyGettingStrategyFactoryControl.setReturnValue(null);

      propertySettingStrategyFactory.create(setter);
      propertySettingStrategyFactoryControl.setReturnValue(propertySettingStrategy);

      propertyGettingStrategyFactoryControl.replay();
      propertySettingStrategyFactoryControl.replay();
      propertySettingStrategyControl.replay();
      
      assertSame(propertySettingStrategy, getPropertySettingStrategy(factory.create(null, setter)));
      assertNull(getPropertyGettingStrategy(factory.create(null, setter)));
    } catch (Exception ex) {
      fail();
    } finally {
      propertyGettingStrategyFactoryControl.verify();
      propertySettingStrategyFactoryControl.verify();
      propertySettingStrategyControl.verify();
    }
  }

  public void testGetterSetterBasedPropertyAccessStrategyCreation() {
    MockControl propertyGettingStrategyFactoryControl = MockControl.createControl(PropertyGettingStrategyFactory.class);
    MockControl propertySettingStrategyFactoryControl = MockControl.createControl(PropertySettingStrategyFactory.class);
    MockControl propertySettingStrategyControl = MockControl.createControl(PropertySettingStrategy.class);
    MockControl propertyGettingStrategyControl = MockControl.createControl(PropertyGettingStrategy.class);
    try {
      PropertyGettingStrategyFactory propertyGettingStrategyFactory = (PropertyGettingStrategyFactory)(propertyGettingStrategyFactoryControl.getMock());
      PropertySettingStrategyFactory propertySettingStrategyFactory = (PropertySettingStrategyFactory)(propertySettingStrategyFactoryControl.getMock());
      PropertySettingStrategy propertySettingStrategy = (PropertySettingStrategy)(propertySettingStrategyControl.getMock());
      PropertyGettingStrategy propertyGettingStrategy = (PropertyGettingStrategy)(propertyGettingStrategyControl.getMock());
              
      Method setter = TestBean.class.getDeclaredMethod("setProperty", String.class);
      Method getter = TestBean.class.getDeclaredMethod("getProperty");

      PropertyAccessStrategyFactory factory =  new DefaultPropertyAccessStrategyFactory(propertyGettingStrategyFactory, propertySettingStrategyFactory);

      propertyGettingStrategyFactory.create(getter);
      propertyGettingStrategyFactoryControl.setReturnValue(propertyGettingStrategy);

      propertySettingStrategyFactory.create(setter);
      propertySettingStrategyFactoryControl.setReturnValue(propertySettingStrategy);

      propertyGettingStrategyFactoryControl.replay();
      propertySettingStrategyFactoryControl.replay();
      propertySettingStrategyControl.replay();
      propertyGettingStrategyControl.replay();
      
      assertSame(propertySettingStrategy, getPropertySettingStrategy(factory.create(getter, setter)));
      assertSame(propertyGettingStrategy, getPropertyGettingStrategy(factory.create(getter, setter)));
    } catch (Exception ex) {
      fail();
    } finally {
      propertyGettingStrategyFactoryControl.verify();
      propertySettingStrategyFactoryControl.verify();
      propertySettingStrategyControl.verify();
      propertyGettingStrategyControl.verify();
    }
  }

  public void testFieldBasedPropertyAccessStrategyCreation() {
    MockControl propertyGettingStrategyFactoryControl = MockControl.createControl(PropertyGettingStrategyFactory.class);
    MockControl propertySettingStrategyFactoryControl = MockControl.createControl(PropertySettingStrategyFactory.class);
    MockControl propertySettingStrategyControl = MockControl.createControl(PropertySettingStrategy.class);
    MockControl propertyGettingStrategyControl = MockControl.createControl(PropertyGettingStrategy.class);
    try {
      PropertyGettingStrategyFactory propertyGettingStrategyFactory = (PropertyGettingStrategyFactory)(propertyGettingStrategyFactoryControl.getMock());
      PropertySettingStrategyFactory propertySettingStrategyFactory = (PropertySettingStrategyFactory)(propertySettingStrategyFactoryControl.getMock());
      PropertySettingStrategy propertySettingStrategy = (PropertySettingStrategy)(propertySettingStrategyControl.getMock());
      PropertyGettingStrategy propertyGettingStrategy = (PropertyGettingStrategy)(propertyGettingStrategyControl.getMock());
              
      Field field = TestBean.class.getDeclaredField("property");

      PropertyAccessStrategyFactory factory =  new DefaultPropertyAccessStrategyFactory(propertyGettingStrategyFactory, propertySettingStrategyFactory);

      propertyGettingStrategyFactory.create(field, "getter");
      propertyGettingStrategyFactoryControl.setReturnValue(propertyGettingStrategy);

      propertySettingStrategyFactory.create(field, "setter");
      propertySettingStrategyFactoryControl.setReturnValue(propertySettingStrategy);

      propertyGettingStrategyFactoryControl.replay();
      propertySettingStrategyFactoryControl.replay();
      propertySettingStrategyControl.replay();
      propertyGettingStrategyControl.replay();
      
      assertSame(propertySettingStrategy, getPropertySettingStrategy(factory.create(field, "getter", "setter")));
      assertSame(propertyGettingStrategy, getPropertyGettingStrategy(factory.create(field, "getter", "setter")));
    } catch (Exception ex) {
      fail();
    } finally {
      propertyGettingStrategyFactoryControl.verify();
      propertySettingStrategyFactoryControl.verify();
      propertySettingStrategyControl.verify();
      propertyGettingStrategyControl.verify();
    }
  }

  public void testPropertyBasedPropertyAccessStrategyCreation() {
    MockControl propertyGettingStrategyFactoryControl = MockControl.createControl(PropertyGettingStrategyFactory.class);
    MockControl propertySettingStrategyFactoryControl = MockControl.createControl(PropertySettingStrategyFactory.class);
    MockControl propertySettingStrategyControl = MockControl.createControl(PropertySettingStrategy.class);
    MockControl propertyGettingStrategyControl = MockControl.createControl(PropertyGettingStrategy.class);
    try {
      PropertyGettingStrategyFactory propertyGettingStrategyFactory = (PropertyGettingStrategyFactory)(propertyGettingStrategyFactoryControl.getMock());
      PropertySettingStrategyFactory propertySettingStrategyFactory = (PropertySettingStrategyFactory)(propertySettingStrategyFactoryControl.getMock());
      PropertySettingStrategy propertySettingStrategy = (PropertySettingStrategy)(propertySettingStrategyControl.getMock());
      PropertyGettingStrategy propertyGettingStrategy = (PropertyGettingStrategy)(propertyGettingStrategyControl.getMock());
              
      Field field = TestBean.class.getDeclaredField("noGetterVariable");

      PropertyAccessStrategyFactory factory =  new DefaultPropertyAccessStrategyFactory(propertyGettingStrategyFactory, propertySettingStrategyFactory);

      propertyGettingStrategyFactory.create(field, null);
      propertyGettingStrategyFactoryControl.setReturnValue(propertyGettingStrategy);

      propertySettingStrategyFactory.create(field, null);
      propertySettingStrategyFactoryControl.setReturnValue(propertySettingStrategy);

      propertyGettingStrategyFactoryControl.replay();
      propertySettingStrategyFactoryControl.replay();
      propertySettingStrategyControl.replay();
      propertyGettingStrategyControl.replay();
      
      assertSame(propertySettingStrategy, getPropertySettingStrategy(factory.create(TestBean.class, "noGetterVariable")));
      assertSame(propertyGettingStrategy, getPropertyGettingStrategy(factory.create(TestBean.class, "noGetterVariable")));
    } catch (Exception ex) {
      fail();
    } finally {
      propertyGettingStrategyFactoryControl.verify();
      propertySettingStrategyFactoryControl.verify();
      propertySettingStrategyControl.verify();
      propertyGettingStrategyControl.verify();
    }
  }
  
  
  public void testPropertyBasedPropertyAccessStrategyCreationWithOverrideProperty() {
    MockControl propertyGettingStrategyFactoryControl = MockControl.createControl(PropertyGettingStrategyFactory.class);
    MockControl propertySettingStrategyFactoryControl = MockControl.createControl(PropertySettingStrategyFactory.class);
    MockControl propertySettingStrategyControl = MockControl.createControl(PropertySettingStrategy.class);
    MockControl propertyGettingStrategyControl = MockControl.createControl(PropertyGettingStrategy.class);
    try {
      PropertyGettingStrategyFactory propertyGettingStrategyFactory = (PropertyGettingStrategyFactory)(propertyGettingStrategyFactoryControl.getMock());
      PropertySettingStrategyFactory propertySettingStrategyFactory = (PropertySettingStrategyFactory)(propertySettingStrategyFactoryControl.getMock());
      PropertySettingStrategy propertySettingStrategy = (PropertySettingStrategy)(propertySettingStrategyControl.getMock());
      PropertyGettingStrategy propertyGettingStrategy = (PropertyGettingStrategy)(propertyGettingStrategyControl.getMock());
              
      Method getter = TestBean.class.getMethod("getOverridedProperty");
      Method setter = TestBean.class.getMethod("setOverridedProperty", String.class);

      PropertyAccessStrategyFactory factory =  new DefaultPropertyAccessStrategyFactory(propertyGettingStrategyFactory, propertySettingStrategyFactory);

      propertyGettingStrategyFactory.create(getter);
      propertyGettingStrategyFactoryControl.setReturnValue(propertyGettingStrategy);

      propertySettingStrategyFactory.create(setter);
      propertySettingStrategyFactoryControl.setReturnValue(propertySettingStrategy);

      propertyGettingStrategyFactoryControl.replay();
      propertySettingStrategyFactoryControl.replay();
      propertySettingStrategyControl.replay();
      propertyGettingStrategyControl.replay();
      
      assertSame(propertySettingStrategy, getPropertySettingStrategy(factory.create(TestBean.class, "overridedProperty")));
      assertSame(propertyGettingStrategy, getPropertyGettingStrategy(factory.create(TestBean.class, "overridedProperty")));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      propertyGettingStrategyFactoryControl.verify();
      propertySettingStrategyFactoryControl.verify();
      propertySettingStrategyControl.verify();
      propertyGettingStrategyControl.verify();
    }
  }

  
  public void testPropertyBasedPropertyAccessStrategyCreationWithUnknownProperty() {
    MockControl propertyGettingStrategyFactoryControl = MockControl.createControl(PropertyGettingStrategyFactory.class);
    MockControl propertySettingStrategyFactoryControl = MockControl.createControl(PropertySettingStrategyFactory.class);
    MockControl propertySettingStrategyControl = MockControl.createControl(PropertySettingStrategy.class);
    MockControl propertyGettingStrategyControl = MockControl.createControl(PropertyGettingStrategy.class);
    try {
      PropertyGettingStrategyFactory propertyGettingStrategyFactory = (PropertyGettingStrategyFactory)(propertyGettingStrategyFactoryControl.getMock());
      PropertySettingStrategyFactory propertySettingStrategyFactory = (PropertySettingStrategyFactory)(propertySettingStrategyFactoryControl.getMock());
      PropertySettingStrategy propertySettingStrategy = (PropertySettingStrategy)(propertySettingStrategyControl.getMock());
      PropertyGettingStrategy propertyGettingStrategy = (PropertyGettingStrategy)(propertyGettingStrategyControl.getMock());
              
      PropertyAccessStrategyFactory factory =  new DefaultPropertyAccessStrategyFactory(propertyGettingStrategyFactory, propertySettingStrategyFactory);

      propertyGettingStrategyFactoryControl.replay();
      propertySettingStrategyFactoryControl.replay();
      propertySettingStrategyControl.replay();
      propertyGettingStrategyControl.replay();
      try {
        factory.create(TestBean.class, "xxxxx");
        fail();
      } catch (IllegalArgumentException ex) {
      }
    } catch (Exception ex) {
      fail();
    } finally {
      propertyGettingStrategyFactoryControl.verify();
      propertySettingStrategyFactoryControl.verify();
      propertySettingStrategyControl.verify();
      propertyGettingStrategyControl.verify();
    }
  }

  
  public void testGetterBasedPropertyAccessStrategyCaching() {
    MockControl propertyGettingStrategyFactoryControl = MockControl.createControl(PropertyGettingStrategyFactory.class);
    MockControl propertySettingStrategyFactoryControl = MockControl.createControl(PropertySettingStrategyFactory.class);
    try {
      PropertyGettingStrategyFactory propertyGettingStrategyFactory = (PropertyGettingStrategyFactory)(propertyGettingStrategyFactoryControl.getMock());
      PropertySettingStrategyFactory propertySettingStrategyFactory = (PropertySettingStrategyFactory)(propertySettingStrategyFactoryControl.getMock());
      
      Method getter = TestBean.class.getDeclaredMethod("getProperty");
      PropertyAccessStrategyFactory factory =  new DefaultPropertyAccessStrategyFactory(propertyGettingStrategyFactory, propertySettingStrategyFactory);

      propertyGettingStrategyFactory.create(getter);
      propertyGettingStrategyFactoryControl.setReturnValue(null);

      propertySettingStrategyFactory.create(null);
      propertySettingStrategyFactoryControl.setReturnValue(null);

      propertyGettingStrategyFactoryControl.replay();
      propertySettingStrategyFactoryControl.replay();

      assertSame(factory.create(getter, null), factory.create(getter, null));
    } catch (Exception ex) {
      fail();
    } finally {
      propertyGettingStrategyFactoryControl.verify();
      propertySettingStrategyFactoryControl.verify();
    }
  }

  public void testSetterBasedPropertyAccessStrategyCaching() {
    MockControl propertyGettingStrategyFactoryControl = MockControl.createControl(PropertyGettingStrategyFactory.class);
    MockControl propertySettingStrategyFactoryControl = MockControl.createControl(PropertySettingStrategyFactory.class);
    try {
      PropertyGettingStrategyFactory propertyGettingStrategyFactory = (PropertyGettingStrategyFactory)(propertyGettingStrategyFactoryControl.getMock());
      PropertySettingStrategyFactory propertySettingStrategyFactory = (PropertySettingStrategyFactory)(propertySettingStrategyFactoryControl.getMock());

      Method setter = TestBean.class.getDeclaredMethod("setProperty", String.class);
      PropertyAccessStrategyFactory factory =  new DefaultPropertyAccessStrategyFactory(propertyGettingStrategyFactory, propertySettingStrategyFactory);

      propertyGettingStrategyFactory.create(null);
      propertyGettingStrategyFactoryControl.setReturnValue(null);

      propertySettingStrategyFactory.create(setter);
      propertySettingStrategyFactoryControl.setReturnValue(null);

      propertyGettingStrategyFactoryControl.replay();
      propertySettingStrategyFactoryControl.replay();
      assertSame(factory.create(null, setter), factory.create(null, setter));
    } catch (Exception ex) {
      fail();
    } finally {
      propertyGettingStrategyFactoryControl.verify();
      propertySettingStrategyFactoryControl.verify();
    }
  }

   public void testMethodBasedPropertyAccessStrategyCaching() {
    MockControl propertyGettingStrategyFactoryControl = MockControl.createControl(PropertyGettingStrategyFactory.class);
    MockControl propertySettingStrategyFactoryControl = MockControl.createControl(PropertySettingStrategyFactory.class);
    try {
      PropertyGettingStrategyFactory propertyGettingStrategyFactory = (PropertyGettingStrategyFactory)(propertyGettingStrategyFactoryControl.getMock());
      PropertySettingStrategyFactory propertySettingStrategyFactory = (PropertySettingStrategyFactory)(propertySettingStrategyFactoryControl.getMock());

      Method setter = TestBean.class.getDeclaredMethod("setProperty", String.class);
      Method getter = TestBean.class.getDeclaredMethod("getProperty");

      PropertyAccessStrategyFactory factory =  new DefaultPropertyAccessStrategyFactory(propertyGettingStrategyFactory, propertySettingStrategyFactory);

      propertyGettingStrategyFactory.create(null);
      propertyGettingStrategyFactoryControl.setReturnValue(null);
      propertyGettingStrategyFactory.create(getter);
      propertyGettingStrategyFactoryControl.setReturnValue(null, 2);

      propertySettingStrategyFactory.create(null);
      propertySettingStrategyFactoryControl.setReturnValue(null);
      propertySettingStrategyFactory.create(setter);
      propertySettingStrategyFactoryControl.setReturnValue(null, 2);

      propertyGettingStrategyFactoryControl.replay();
      propertySettingStrategyFactoryControl.replay();
      assertSame(factory.create(getter, setter), factory.create(getter, setter));
      assertNotSame(factory.create(getter, setter), factory.create(null, setter));
      assertNotSame(factory.create(getter, setter), factory.create(getter, null));
    } catch (Exception ex) {
      fail();
    } finally {
      propertyGettingStrategyFactoryControl.verify();
      propertySettingStrategyFactoryControl.verify();
    }
  }

  public void testDefaultGetterSetterPropertyAccessStrategyCaching() {
    MockControl propertyGettingStrategyFactoryControl = MockControl.createControl(PropertyGettingStrategyFactory.class);
    MockControl propertySettingStrategyFactoryControl = MockControl.createControl(PropertySettingStrategyFactory.class);
    try {
      PropertyGettingStrategyFactory propertyGettingStrategyFactory = (PropertyGettingStrategyFactory)(propertyGettingStrategyFactoryControl.getMock());
      PropertySettingStrategyFactory propertySettingStrategyFactory = (PropertySettingStrategyFactory)(propertySettingStrategyFactoryControl.getMock());

      Field field = TestBean.class.getDeclaredField("property");

      PropertyAccessStrategyFactory factory =  new DefaultPropertyAccessStrategyFactory(propertyGettingStrategyFactory, propertySettingStrategyFactory);

      propertyGettingStrategyFactory.create(field, null);
      propertyGettingStrategyFactoryControl.setDefaultReturnValue(null);
      propertyGettingStrategyFactory.create(field, "getProperty");
      propertyGettingStrategyFactoryControl.setDefaultReturnValue(null);

      propertySettingStrategyFactory.create(field, null);
      propertySettingStrategyFactoryControl.setDefaultReturnValue(null);
      propertySettingStrategyFactory.create(field, "setProperty");
      propertySettingStrategyFactoryControl.setDefaultReturnValue(null);

      propertyGettingStrategyFactoryControl.replay();
      propertySettingStrategyFactoryControl.replay();

      assertSame(factory.create(field, null, null), factory.create(field, null, null));
      assertNotSame(factory.create(field, "getProperty", null), factory.create(field, null, null));
      assertNotSame(factory.create(field, null, "setProperty"), factory.create(field, null, null));
      assertNotSame(factory.create(field, "getProperty", "setProperty"), factory.create(field, null, null));
    } catch (Exception ex) {
      fail();
    } finally {
      propertyGettingStrategyFactoryControl.verify();
      propertySettingStrategyFactoryControl.verify();
    }
  }
  
  public void testMethodBaseRequestHashCode() {
    try {
      Method setter = TestBean.class.getDeclaredMethod("setProperty", String.class);
      Method getter = TestBean.class.getDeclaredMethod("getProperty");
      assertEquals(createMethodBasedRequest(null, null).hashCode(), createMethodBasedRequest(null, null).hashCode());
      assertEquals(createMethodBasedRequest(null, setter).hashCode(), createMethodBasedRequest(null, setter).hashCode());
      assertEquals(createMethodBasedRequest(getter, null).hashCode(), createMethodBasedRequest(getter, null).hashCode());
      assertEquals(createMethodBasedRequest(getter, setter).hashCode(), createMethodBasedRequest(getter, setter).hashCode());
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testMethodBaseRequestEquals() {
    try {
      Method setter = TestBean.class.getDeclaredMethod("setProperty", String.class);
      Method getter = TestBean.class.getDeclaredMethod("getProperty");
      assertEquals(createMethodBasedRequest(null, null), createMethodBasedRequest(null, null));
      assertFalse(createMethodBasedRequest(null, null).equals(createMethodBasedRequest(getter, null)));
      assertFalse(createMethodBasedRequest(null, null).equals(createMethodBasedRequest(null, setter)));
      assertFalse(createMethodBasedRequest(null, null).equals(createMethodBasedRequest(getter, setter)));

      assertEquals(createMethodBasedRequest(null, setter), createMethodBasedRequest(null, setter));
      assertFalse(createMethodBasedRequest(null, setter).equals(createMethodBasedRequest(getter, null)));
      assertFalse(createMethodBasedRequest(null, setter).equals(createMethodBasedRequest(null, getter)));
      assertFalse(createMethodBasedRequest(null, setter).equals(createMethodBasedRequest(getter, setter)));

      assertEquals(createMethodBasedRequest(getter, null), createMethodBasedRequest(getter, null));
      assertFalse(createMethodBasedRequest(getter, null).equals(createMethodBasedRequest(setter, null)));
      assertFalse(createMethodBasedRequest(getter, null).equals(createMethodBasedRequest(getter, setter)));

      assertEquals(createMethodBasedRequest(getter, setter), createMethodBasedRequest(getter, setter));
      assertFalse(createMethodBasedRequest(getter, setter).equals(createMethodBasedRequest(setter, getter)));

      assertFalse(createMethodBasedRequest(getter, setter).equals(null));
      assertFalse(createMethodBasedRequest(getter, setter).equals("XXX"));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testFieldBaseRequestHashCode() {
    try {
      Field field = TestBean.class.getDeclaredField("property");
      assertEquals(createFieldBasedRequest(null, null, null).hashCode(), createFieldBasedRequest(null, null, null).hashCode());
      assertEquals(createFieldBasedRequest(field, null, null).hashCode(), createFieldBasedRequest(field, null, null).hashCode());
      assertEquals(createFieldBasedRequest(null, "X", null).hashCode(), createFieldBasedRequest(null, "X", null).hashCode());
      assertEquals(createFieldBasedRequest(null, null, "X").hashCode(), createFieldBasedRequest(null, null, "X").hashCode());
      assertEquals(createFieldBasedRequest(field, "X", null).hashCode(), createFieldBasedRequest(field, "X", null).hashCode());
      assertEquals(createFieldBasedRequest(null, "X", "X").hashCode(), createFieldBasedRequest(null, "X", "X").hashCode());
      assertEquals(createFieldBasedRequest(field, null, "X").hashCode(), createFieldBasedRequest(field, null, "X").hashCode());
      assertEquals(createFieldBasedRequest(field, "X", "X").hashCode(), createFieldBasedRequest(field, "X", "X").hashCode());
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
   
  public void testFieldBaseRequestEquals() {
    try {
      Field field = TestBean.class.getDeclaredField("property");
      Field field2 = TestBean.class.getDeclaredField("variable");
      
      
      assertEquals(createFieldBasedRequest(field, null, null), createFieldBasedRequest(field, null, null));
      assertFalse(createFieldBasedRequest(field, null, null).equals(createFieldBasedRequest(field2, null, null)));
      assertFalse(createFieldBasedRequest(field, null, null).equals(createFieldBasedRequest(field, "X", null)));
      assertFalse(createFieldBasedRequest(field, null, null).equals(createFieldBasedRequest(field, null, "X")));
      
      assertEquals(createFieldBasedRequest(field, "X", null), createFieldBasedRequest(field, "X", null));
      assertFalse(createFieldBasedRequest(field, "X", null).equals(createFieldBasedRequest(field, "Y", null)));

      assertEquals(createFieldBasedRequest(field, null, "X"), createFieldBasedRequest(field, null, "X"));
      assertFalse(createFieldBasedRequest(field, null, "X").equals(createFieldBasedRequest(field, null, "Y")));

      assertFalse(createFieldBasedRequest(field, null, null).equals(null));
      assertFalse(createFieldBasedRequest(field, null, null).equals("XXX"));

    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
}
