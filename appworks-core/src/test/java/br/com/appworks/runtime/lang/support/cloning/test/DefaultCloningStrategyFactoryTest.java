/*
 * DefaultCloningStrategyFactoryTest.java
 * JUnit based test
 *
 * Created on 19 de Julho de 2005, 00:10
 */

package br.com.appworks.runtime.lang.support.cloning.test;
import br.com.appworks.runtime.lang.Cloneable;
import br.com.appworks.runtime.lang.CloningPolicy;
import br.com.appworks.runtime.lang.support.HashTypeMap;
import br.com.appworks.runtime.lang.support.TypeMap;
import br.com.appworks.runtime.lang.support.cloning.ArrayCloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.CloneableCloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.CloningHandler;
import br.com.appworks.runtime.lang.support.cloning.CloningProcessor;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategyFactory;
import br.com.appworks.runtime.lang.support.cloning.DefaultCloningStrategyFactory;
import br.com.appworks.runtime.lang.support.cloning.PropertyCloningProcessor;
import br.com.appworks.runtime.lang.support.cloning.ReflectionCloningStrategy;
import br.com.appworks.runtime.lang.support.property.PropertyAccessStrategy;
import br.com.appworks.runtime.lang.support.property.PropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategy;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.property.setting.PropertySettingStrategy;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class DefaultCloningStrategyFactoryTest extends TestCase {
  private static class PrivateCloningStrategy <Type extends Object> implements CloningStrategy <Type> {
    public Type clone(Type source) throws CloneNotSupportedException {
      throw new UnsupportedOperationException();
    }
    public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
    }
  }

  public static class NoDefaultConstructorCloningStrategy <Type extends Object> implements CloningStrategy <Type> {
    public NoDefaultConstructorCloningStrategy(int x) {
    }
    public Type clone(Type source) throws CloneNotSupportedException {
      throw new UnsupportedOperationException();
    }
    public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
    }
    
  }
  
  public static class MockCloningStrategy <Type extends Object> implements CloningStrategy <Type> {
    public Type clone(Type source) throws CloneNotSupportedException {
      throw new UnsupportedOperationException();
    }
    public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
    }
  }
  
  public static class NonCloneableTestBean {
    private Calendar value;
  }

  @Cloneable(strategy = MockCloningStrategy.class)
  public static class CustomCloningStrategyTestBean {
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }

  @Cloneable(strategy = PrivateCloningStrategy.class)
  public static class PrivateCloningStrategyTestBean {
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }

  @Cloneable(strategy = NoDefaultConstructorCloningStrategy.class)
  public static class NoDefaultConstructorCloningStrategyTestBean {
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  @Cloneable(CloningPolicy.DEEP)
  public static class CustomTypeStrategyTestBean {
    private String x;
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(strategy = MockCloningStrategy.class)
  public static interface TestInterface {
  }
  
  public static class TestInterfaceChild implements TestInterface {
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }

  @Cloneable(CloningPolicy.DEEP)
  public static class TestBean implements Serializable {
    @Cloneable(CloningPolicy.SHALLOW)
    private String value;

    @Cloneable(value = CloningPolicy.DEEP, getter = "getter", setter="setter")
    private br.com.appworks.runtime.lang.support.cloning.Cloneable value1;

    private Calendar value2;
    
    private String value3;
    
    private int primitive;
    
    private static Calendar value4;
    
    private static Object [] staticObjectArray;
    
    @Cloneable(strategy=MockCloningStrategy.class)
    private Class value5;
    
    @Cloneable(CloningPolicy.SHALLOW)
    private Object shallowObject;
    
    private Object deepObject;

    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.SHALLOW)
  public static class ChildTestBean extends TestBean {
    @Cloneable(CloningPolicy.DEEP)
    private Calendar value6;

    private String value7;

    @Cloneable(strategy=MockCloningStrategy.class)
    private Calendar value8;
    
    private Object [] shallowArray;
    
    @Cloneable(CloningPolicy.DEEP)
    private Object [] deepArray;
    
    private Class shallowNonCloneableCustomType;
    
    @Cloneable(CloningPolicy.DEEP)
    private Class deepNonCloneableCustomType;

    private ArrayList shallowCloneableCustomType;
    
    @Cloneable(CloningPolicy.DEEP)
    private ArrayList deepCloneableCustomType;

    @Cloneable(CloningPolicy.DEEP)
    private ArrayList deepCloneableCustomType2;

    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  
  @Cloneable(CloningPolicy.DEEP)
  public static class NonCloneableChildTestBean extends NonCloneableTestBean {
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  

  private PropertyAccessStrategy  getPropertyAccessStrategy(PropertyCloningProcessor propertyCloningProcessor) throws Exception {
    Field propertyAccessStrategyField = null;
    try {
      propertyAccessStrategyField = PropertyCloningProcessor.class.getDeclaredField("propertyAccessStrategy");
      propertyAccessStrategyField.setAccessible(true);
      return (PropertyAccessStrategy <TestBean, String>) (propertyAccessStrategyField.get(propertyCloningProcessor));
    } finally {
      propertyAccessStrategyField.setAccessible(false);
    }
  }
  private CloningStrategy getCloningStrategy(PropertyCloningProcessor propertyCloningProcessor) throws Exception {
    Field cloningStrategyField = null;
    try {
      cloningStrategyField = PropertyCloningProcessor.class.getDeclaredField("cloningStrategy");
      cloningStrategyField.setAccessible(true);
      return (CloningStrategy <String>) (cloningStrategyField.get(propertyCloningProcessor));
    } finally {
      cloningStrategyField.setAccessible(false);
    }
  }
  
  private Collection <CloningProcessor> getCloningProcessors(CloningHandler cloningHandler) throws Exception {
    Field cloningHandlersField = null;
    try {
      cloningHandlersField = CloningHandler.class.getDeclaredField("cloningProcessors");
      cloningHandlersField.setAccessible(true);
      return (Collection <CloningProcessor>) (cloningHandlersField.get(cloningHandler));
    } finally {
      cloningHandlersField.setAccessible(false);
    }
  }
  
  public DefaultCloningStrategyFactoryTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testWithoutCustomTypeCloningStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> value1PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, String>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> value2PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, String>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> value5PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, String>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Object> deepObjectPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Object> ((PropertyGettingStrategy <TestBean, Object>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Object>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value1"), "getter", "setter");
      propertyAccessStrategyFactoryControl.setReturnValue(value1PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value2"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(value2PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value5"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(value5PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("deepObject"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(deepObjectPropertyAccessStrategy);
    
      propertyAccessStrategyFactoryControl.replay();
   
      CloningStrategyFactory cloningStrategyFactory = new DefaultCloningStrategyFactory(propertyAccessStrategyFactory);
      CloningHandler <TestBean> cloningHandler = (CloningHandler <TestBean> )(cloningStrategyFactory.create(new TestBean().getClass()));
      assertEquals(4, getCloningProcessors(cloningHandler).size());
      for (CloningProcessor <TestBean> cloningProcessor : getCloningProcessors(cloningHandler)) {
        PropertyCloningProcessor <TestBean, String> propertyCloningProcessor = (PropertyCloningProcessor <TestBean, String>)(cloningProcessor);
        CloningStrategy <String> strategy = getCloningStrategy(propertyCloningProcessor);
        if (getPropertyAccessStrategy(propertyCloningProcessor) == value1PropertyAccessStrategy) {
          assertTrue(strategy instanceof CloneableCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == value2PropertyAccessStrategy) {
          assertTrue(strategy instanceof ReflectionCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == value5PropertyAccessStrategy) {
          assertTrue(strategy instanceof MockCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == deepObjectPropertyAccessStrategy) {
          assertTrue(strategy instanceof ReflectionCloningStrategy);
        } else {
          fail();
        }
      }
      propertyAccessStrategyFactoryControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testWithCustomTypeCloningStrategyCreation() {
    try {
      MockControl deepCloneableCloningStrategyControl = MockControl.createControl(CloningStrategy.class);
      MockControl deepNonCloneableCloningStrategyControl = MockControl.createControl(CloningStrategy.class);
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> value1PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, String>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> value2PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, String>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> value5PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, String>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Object> deepObjectPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Object> ((PropertyGettingStrategy <TestBean, Object>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Object>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value1"), "getter", "setter");
      propertyAccessStrategyFactoryControl.setReturnValue(value1PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value2"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(value2PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value5"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(value5PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("deepObject"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(deepObjectPropertyAccessStrategy);
    
      deepCloneableCloningStrategyControl.replay();
      deepNonCloneableCloningStrategyControl.replay();
      propertyAccessStrategyFactoryControl.replay();
  
      TypeMap <CloningStrategy> typeMap = new HashTypeMap <CloningStrategy> ();
      typeMap.put(Class.class, (CloningStrategy)(deepNonCloneableCloningStrategyControl.getMock()));
      typeMap.put(Collection.class, (CloningStrategy)(deepCloneableCloningStrategyControl.getMock()));
      
      CloningStrategyFactory cloningStrategyFactory = new DefaultCloningStrategyFactory(propertyAccessStrategyFactory, typeMap);
      CloningHandler <TestBean> cloningHandler = (CloningHandler <TestBean> )(cloningStrategyFactory.create(new TestBean().getClass()));
      assertEquals(4, getCloningProcessors(cloningHandler).size());
      for (CloningProcessor <TestBean> cloningProcessor : getCloningProcessors(cloningHandler)) {
        PropertyCloningProcessor <TestBean, String> propertyCloningProcessor = (PropertyCloningProcessor <TestBean, String>)(cloningProcessor);
        CloningStrategy <String> strategy = getCloningStrategy(propertyCloningProcessor);
        if (getPropertyAccessStrategy(propertyCloningProcessor) == value1PropertyAccessStrategy) {
          assertTrue(strategy instanceof CloneableCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == value2PropertyAccessStrategy) {
          assertTrue(strategy instanceof ReflectionCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == value5PropertyAccessStrategy) {
          assertTrue(strategy instanceof MockCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == deepObjectPropertyAccessStrategy) {
          assertTrue(strategy instanceof ReflectionCloningStrategy);
        } else {
          fail();
        }
      }
      propertyAccessStrategyFactoryControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testRegularClassCloningStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      propertyAccessStrategyFactoryControl.replay();

      CloningStrategyFactory cloningStrategyFactory = new DefaultCloningStrategyFactory(propertyAccessStrategyFactory);
      CloningStrategy <Object> cloningStrategy = (CloningStrategy <Object> )(cloningStrategyFactory.create(Object.class));
      assertNull(cloningStrategy);
      propertyAccessStrategyFactoryControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }  

  public void testWithoutCustomTypeChildCloningStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> value1PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, String>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> value2PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, String>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> value5PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, String>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Object> deepObjectPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Object> ((PropertyGettingStrategy <TestBean, Object>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Object>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Calendar> value6PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Calendar> ((PropertyGettingStrategy <TestBean, Calendar>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Calendar>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Calendar> value8PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Calendar> ((PropertyGettingStrategy <TestBean, Calendar>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Calendar>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Object []> deepArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Object []> ((PropertyGettingStrategy <TestBean, Object []>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Object []>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Collection> deepCloneablePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Collection> ((PropertyGettingStrategy <TestBean, Collection>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Collection>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Collection> deepCloneableProperty2AccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Collection> ((PropertyGettingStrategy <TestBean, Collection>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Collection>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value1"), "getter", "setter");
      propertyAccessStrategyFactoryControl.setReturnValue(value1PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value2"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(value2PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(ChildTestBean.class.getDeclaredField("value6"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(value6PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value5"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(value5PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(ChildTestBean.class.getDeclaredField("value8"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(value8PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("deepObject"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(deepObjectPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(ChildTestBean.class.getDeclaredField("deepArray"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(deepArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(ChildTestBean.class.getDeclaredField("deepCloneableCustomType"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(deepCloneablePropertyAccessStrategy);
      propertyAccessStrategyFactory.create(ChildTestBean.class.getDeclaredField("deepCloneableCustomType2"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(deepCloneableProperty2AccessStrategy);
    
      propertyAccessStrategyFactoryControl.replay();
   
      CloningStrategyFactory cloningStrategyFactory = new DefaultCloningStrategyFactory(propertyAccessStrategyFactory);
      CloningHandler <ChildTestBean> cloningHandler = (CloningHandler <ChildTestBean> )(cloningStrategyFactory.create(new ChildTestBean().getClass()));
      assertEquals(9, getCloningProcessors(cloningHandler).size());
      for (CloningProcessor <TestBean> cloningProcessor : getCloningProcessors(cloningHandler)) {
        PropertyCloningProcessor <TestBean, String> propertyCloningProcessor = (PropertyCloningProcessor <TestBean, String>)(cloningProcessor);
        CloningStrategy <String> strategy = getCloningStrategy(propertyCloningProcessor);
        if (getPropertyAccessStrategy(propertyCloningProcessor) == value1PropertyAccessStrategy) {
          assertTrue(strategy instanceof CloneableCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == value6PropertyAccessStrategy) {
          assertTrue(strategy instanceof ReflectionCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == value5PropertyAccessStrategy) {
          assertTrue(strategy instanceof MockCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == value2PropertyAccessStrategy) {
          assertTrue(strategy instanceof ReflectionCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == value8PropertyAccessStrategy) {
          assertTrue(strategy instanceof MockCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == deepObjectPropertyAccessStrategy) {
          assertTrue(strategy instanceof ReflectionCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == deepArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == deepCloneablePropertyAccessStrategy) {
          assertTrue(strategy instanceof ReflectionCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == deepCloneableProperty2AccessStrategy) {
          assertTrue(strategy instanceof ReflectionCloningStrategy);
        } else {
          fail();
        }
      }
      propertyAccessStrategyFactoryControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }


  public void testWithCustomTypeChildCloningStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      MockControl deepCloneableCloningStrategyControl = MockControl.createControl(CloningStrategy.class);
      MockControl deepNonCloneableCloningStrategyControl = MockControl.createControl(CloningStrategy.class);
      CloningStrategy customCloningStrategy1 = (CloningStrategy)(MockControl.createControl(CloningStrategy.class).getMock());
      CloningStrategy customCloningStrategy2 = (CloningStrategy)(MockControl.createControl(CloningStrategy.class).getMock());
      CloningStrategy customCloningStrategy3 = (CloningStrategy)(MockControl.createControl(CloningStrategy.class).getMock());
      
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> value1PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, String>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> value2PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, String>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> value5PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, String>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Object> deepObjectPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Object> ((PropertyGettingStrategy <TestBean, Object>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Object>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Calendar> value6PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Calendar> ((PropertyGettingStrategy <TestBean, Calendar>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Calendar>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Calendar> value8PropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Calendar> ((PropertyGettingStrategy <TestBean, Calendar>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Calendar>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Object []> deepArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Object []> ((PropertyGettingStrategy <TestBean, Object []>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Object []>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Collection> deepCloneablePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Collection> ((PropertyGettingStrategy <TestBean, Collection>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Collection>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Class> deepNonCloneablePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Class> ((PropertyGettingStrategy <TestBean, Class>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Class>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Collection> deepCloneableProperty2AccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Collection> ((PropertyGettingStrategy <TestBean, Collection>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, Collection>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value1"), "getter", "setter");
      propertyAccessStrategyFactoryControl.setReturnValue(value1PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value2"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(value2PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(ChildTestBean.class.getDeclaredField("value6"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(value6PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value5"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(value5PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(ChildTestBean.class.getDeclaredField("value8"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(value8PropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("deepObject"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(deepObjectPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(ChildTestBean.class.getDeclaredField("deepArray"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(deepArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(ChildTestBean.class.getDeclaredField("deepCloneableCustomType"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(deepCloneablePropertyAccessStrategy);
      propertyAccessStrategyFactory.create(ChildTestBean.class.getDeclaredField("deepNonCloneableCustomType"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(deepNonCloneablePropertyAccessStrategy);
      propertyAccessStrategyFactory.create(ChildTestBean.class.getDeclaredField("deepCloneableCustomType2"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(deepCloneableProperty2AccessStrategy);
    
      ((CloningStrategy)(deepNonCloneableCloningStrategyControl.getMock())).clone();
      deepNonCloneableCloningStrategyControl.setReturnValue(customCloningStrategy1);
      
      ((CloningStrategy)(deepCloneableCloningStrategyControl.getMock())).clone();
      deepCloneableCloningStrategyControl.setReturnValue(customCloningStrategy2);
      
      ((CloningStrategy)(deepCloneableCloningStrategyControl.getMock())).clone();
      deepCloneableCloningStrategyControl.setReturnValue(customCloningStrategy3);
      
      propertyAccessStrategyFactoryControl.replay();
      deepCloneableCloningStrategyControl.replay();
      deepNonCloneableCloningStrategyControl.replay();
      
      TypeMap <CloningStrategy> typeMap = new HashTypeMap <CloningStrategy> ();
      typeMap.put(Class.class, (CloningStrategy)(deepNonCloneableCloningStrategyControl.getMock()));
      typeMap.put(Collection.class, (CloningStrategy)(deepCloneableCloningStrategyControl.getMock()));
      
      
      CloningStrategyFactory cloningStrategyFactory = new DefaultCloningStrategyFactory(propertyAccessStrategyFactory, typeMap);
      CloningHandler <ChildTestBean> cloningHandler = (CloningHandler <ChildTestBean> )(cloningStrategyFactory.create(new ChildTestBean().getClass()));
      assertEquals(10, getCloningProcessors(cloningHandler).size());
      for (CloningProcessor <TestBean> cloningProcessor : getCloningProcessors(cloningHandler)) {
        PropertyCloningProcessor <TestBean, String> propertyCloningProcessor = (PropertyCloningProcessor <TestBean, String>)(cloningProcessor);
        CloningStrategy <String> strategy = getCloningStrategy(propertyCloningProcessor);
        if (getPropertyAccessStrategy(propertyCloningProcessor) == value1PropertyAccessStrategy) {
          assertTrue(strategy instanceof CloneableCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == value6PropertyAccessStrategy) {
          assertTrue(strategy instanceof ReflectionCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == value5PropertyAccessStrategy) {
          assertTrue(strategy instanceof MockCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == value2PropertyAccessStrategy) {
          assertTrue(strategy instanceof ReflectionCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == value8PropertyAccessStrategy) {
          assertTrue(strategy instanceof MockCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == deepObjectPropertyAccessStrategy) {
          assertTrue(strategy instanceof ReflectionCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == deepArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayCloningStrategy);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == deepCloneablePropertyAccessStrategy) {
          assertSame(strategy, customCloningStrategy2);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == deepNonCloneablePropertyAccessStrategy) {
          assertSame(strategy, customCloningStrategy1);
        } else if (getPropertyAccessStrategy(propertyCloningProcessor) == deepCloneableProperty2AccessStrategy) {
          assertSame(strategy, customCloningStrategy3);
        } else {
          fail();
        }
      }
      propertyAccessStrategyFactoryControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testNonCloneableChildCloningStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()), (PropertySettingStrategy <TestBean, String>)(MockControl.createControl(PropertySettingStrategy.class).getMock()));
      
      propertyAccessStrategyFactory.create(NonCloneableTestBean.class.getDeclaredField("value"), "", "");
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
    
      propertyAccessStrategyFactoryControl.replay();
   
      CloningStrategyFactory cloningStrategyFactory = new DefaultCloningStrategyFactory(propertyAccessStrategyFactory);
      CloningHandler <NonCloneableChildTestBean> cloningHandler = (CloningHandler <NonCloneableChildTestBean> )(cloningStrategyFactory.create(new NonCloneableChildTestBean().getClass()));
      assertEquals(1, getCloningProcessors(cloningHandler).size());
      for (CloningProcessor <NonCloneableChildTestBean> cloningProcessor : getCloningProcessors(cloningHandler)) {
        PropertyCloningProcessor <NonCloneableChildTestBean, String> propertyCloningProcessor = (PropertyCloningProcessor <NonCloneableChildTestBean, String>)(cloningProcessor);
        CloningStrategy strategy = getCloningStrategy(propertyCloningProcessor);
        if (getPropertyAccessStrategy(propertyCloningProcessor) == valuePropertyAccessStrategy) {
          assertTrue(strategy instanceof ReflectionCloningStrategy);
        } else {
          fail();
        }
      }
      propertyAccessStrategyFactoryControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
   
  public void testCustomCloningStrategyTestBeanCloningStrategyCreation() {
    CloningStrategyFactory cloningStrategyFactory = new DefaultCloningStrategyFactory(null);
    CloningStrategy <CustomCloningStrategyTestBean> cloningStrategy = (CloningStrategy <CustomCloningStrategyTestBean> )(cloningStrategyFactory.create(CustomCloningStrategyTestBean.class));
    assertTrue(cloningStrategy instanceof MockCloningStrategy);
  }
  public void testPrivateCustomCloningStrategyTestBeanCloningStrategyCreation() {
    try {
      CloningStrategyFactory cloningStrategyFactory = new DefaultCloningStrategyFactory(null);
      CloningStrategy <PrivateCloningStrategyTestBean> cloningStrategy = (CloningStrategy <PrivateCloningStrategyTestBean> )(cloningStrategyFactory.create(PrivateCloningStrategyTestBean.class));
      fail();
    } catch(RuntimeException ex) {
      assertTrue(ex.getCause() instanceof IllegalAccessException);
    }
  }
  public void testNoDefaultConstructorCustomCloningStrategyTestBeanCloningStrategyCreation() {
    try {
      CloningStrategyFactory cloningStrategyFactory = new DefaultCloningStrategyFactory(null);
      CloningStrategy <NoDefaultConstructorCloningStrategyTestBean> cloningStrategy = (CloningStrategy <NoDefaultConstructorCloningStrategyTestBean> )(cloningStrategyFactory.create(NoDefaultConstructorCloningStrategyTestBean.class));
      fail();
    } catch(RuntimeException ex) {
      assertTrue(ex.getCause() instanceof InstantiationException);
    }
  }
  public void testNoCloneableCustomPropertyTestBeanCloningStrategyCreation() {
    try {
      TypeMap <CloningStrategy> typeMap = new HashTypeMap <CloningStrategy> ();
      typeMap.put(String.class, new MockCloningStrategy());
      CloningStrategyFactory cloningStrategyFactory = new DefaultCloningStrategyFactory(null, typeMap);
      CloningStrategy <CustomTypeStrategyTestBean> cloningStrategy = (CloningStrategy <CustomTypeStrategyTestBean> )(cloningStrategyFactory.create(CustomTypeStrategyTestBean.class));
      fail();
    } catch(RuntimeException ex) {
      assertTrue(ex.getCause() instanceof CloneNotSupportedException);
    }
  }
  
   public void testTestInterfaceChildCloningStrategyCreation() {
    CloningStrategyFactory cloningStrategyFactory = new DefaultCloningStrategyFactory(null);
    CloningStrategy <TestInterfaceChild> cloningStrategy = (CloningStrategy <TestInterfaceChild> )(cloningStrategyFactory.create(TestInterfaceChild.class));
    assertTrue(cloningStrategy instanceof MockCloningStrategy);
  }
}
