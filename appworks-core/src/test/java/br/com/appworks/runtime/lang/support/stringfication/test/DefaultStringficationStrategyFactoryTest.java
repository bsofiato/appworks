/*
 * DefaultStringficationStrategyFactoryTest.java
 * JUnit based test
 *
 * Created on 29 de Novembro de 2005, 00:41
 */

package br.com.appworks.runtime.lang.support.stringfication.test;

import br.com.appworks.runtime.lang.Stringficable;
import br.com.appworks.runtime.lang.StringficationPolicy;
import br.com.appworks.runtime.lang.support.HashTypeMap;
import br.com.appworks.runtime.lang.support.TypeMap;
import br.com.appworks.runtime.lang.support.property.PropertyAccessStrategy;
import br.com.appworks.runtime.lang.support.property.PropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategy;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.stringfication.ArrayStringficationStrategy;
import br.com.appworks.runtime.lang.support.stringfication.CalendarStringficationStrategy;
import br.com.appworks.runtime.lang.support.stringfication.DefaultStringficationStrategyFactory;
import br.com.appworks.runtime.lang.support.stringfication.IdentityStringficationStrategy;
import br.com.appworks.runtime.lang.support.stringfication.PropertyStringficationStrategy;
import br.com.appworks.runtime.lang.support.stringfication.StringficationHandler;
import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategy;
import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategyFactory;
import br.com.appworks.runtime.lang.support.stringfication.TemplateStringficationStrategy;
import br.com.appworks.runtime.lang.support.stringfication.ToStringStringficationStrategy;
import br.com.appworks.runtime.lang.support.template.Template;
import br.com.appworks.runtime.lang.support.template.TemplateCompilationException;
import br.com.appworks.runtime.lang.support.template.TemplateFactory;
import java.lang.reflect.Field;
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
public class DefaultStringficationStrategyFactoryTest extends TestCase {
  private static class PrivateStringficationStrategy implements StringficationStrategy <PrivateCustomStringficationStrategyTestBean> {
    public void toString(PrivateCustomStringficationStrategyTestBean privateCustomStringficationStrategyTestBean, StringBuilder sb) {
      throw new UnsupportedOperationException();
    }
    public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
    }
  }
  
  public static class CustomStringficationStrategy implements StringficationStrategy <CustomStringficationStrategyTestBean> {
    public void toString(CustomStringficationStrategyTestBean customStringficationStrategyTestBean, StringBuilder sb) {
      throw new UnsupportedOperationException();
    }
    public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
    }
  }

  public static class NonCloneableStringficationStrategy implements StringficationStrategy <CustomStringficationStrategyTestBean> {
    public void toString(CustomStringficationStrategyTestBean customStringficationStrategyTestBean, StringBuilder sb) {
      throw new UnsupportedOperationException();
    }
    public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
    }
  }

  public static class NonDefaultConstructorStringficationStrategy implements StringficationStrategy <NonDefaultConstructorCustomStringficationStrategyTestBean> {
    public NonDefaultConstructorStringficationStrategy(String x) {
    }
    public void toString(NonDefaultConstructorCustomStringficationStrategyTestBean nonDefaultConstructorCustomStringficationStrategyTestBean, StringBuilder sb) {
      throw new UnsupportedOperationException();
    }
    public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
    }
  }
  
  @Stringficable(template="TESTE")
  private static class TemplateTestBean {
  }
  
  @Stringficable(strategy=PrivateStringficationStrategy.class)
  public static class PrivateCustomStringficationStrategyTestBean {
  }

  @Stringficable(strategy=NonDefaultConstructorStringficationStrategy.class)
  public static class NonDefaultConstructorCustomStringficationStrategyTestBean {
  }

  @Stringficable(strategy=CustomStringficationStrategy.class)
  public static class CustomStringficationStrategyTestBean {
  }
  
  public static class NonAnnotatedTestBean {
  }
  
  
  @Stringficable
  public static class CustomTypeStrategyTestBean {
    @Stringficable
    private String x;
  }
  @Stringficable
  public static class DerivedPropertyOnlyTestBean {
    @Stringficable
    private String getProperty() {
      return null;
    }
  }
  @Stringficable
  public static class PropertyOnlyTestBean {
    @Stringficable
    private String property;
  }
  
  public static class PropertyOnlyTestBeanChild extends PropertyOnlyTestBean {
  }
  
  @Stringficable
  public static class TestBean {
    @Stringficable
    private String value;
    
    @Stringficable (getter = "overriden")
    private String overriden;
    
    @Stringficable (StringficationPolicy.IDENTITY)
    private String identity;

    @Stringficable (strategy = CustomStringficationStrategy.class)
    private String customStrategy;

    private String nonParticipant;

    @Stringficable
    private Calendar valueCalendar;
    
    @Stringficable (StringficationPolicy.IDENTITY)
    private Calendar identityCalendar;
  
    @Stringficable
    private String [] valueArray;

    @Stringficable (StringficationPolicy.IDENTITY)
    private String [] identityArray;
    
    @Stringficable (StringficationPolicy.TOSTRING)
    private String getDerivedProperty() {
      return null;
    }
    @Stringficable (StringficationPolicy.IDENTITY)
    private String getDerivedIdentityProperty() {
      return null;
    }

    @Stringficable (StringficationPolicy.TOSTRING)
    private String [] getDerivedArray() {
      return null;
    }
    @Stringficable
    private Calendar getDerivedCalendar() {
      return null;
    }
    
    @Stringficable(value=StringficationPolicy.TOSTRING, strategy=CustomStringficationStrategy.class)
    private String getDerivedCustomStrategy() {
      return null;
    }
    
    private String getNonComparableProperty() {
      return null;
    }
  }

  @Stringficable
  public static class PrivateStringficationStrategyFieldTestBean {
    @Stringficable(strategy = PrivateStringficationStrategy.class)
    private int x;
  }

  @Stringficable
  public static class NonDefaultConstrutorStringficationStrategyFieldTestBean {
    @Stringficable(strategy = NonDefaultConstructorStringficationStrategy.class)
    private int x;
  }
    
  public static class TestBeanChild extends TestBean{
  }
  
  @Stringficable(strategy=CustomStringficationStrategy.class)
  public static interface TestInterface {
  }
  public static class TestInterfaceChild implements TestInterface {
  }
  
  @Stringficable
  public static interface TestInterface2 {
    @Stringficable
    String getX();
  }
  
  public static abstract class AbstractClass {
    public String getX() {
      return "X";
    }
  }
  
  public static class TestInterface2AbstractClassChild extends AbstractClass implements TestInterface2 {
    
  }

  private PropertyGettingStrategy getPropertyAccessStrategy(PropertyStringficationStrategy<TestBean, String> propertyStringficationStrategy) throws Exception {
    Field propertyAccessStrategyField = null;
    try {
      propertyAccessStrategyField = PropertyStringficationStrategy.class.getDeclaredField("propertyGettingStrategy");
      propertyAccessStrategyField.setAccessible(true);
      return (PropertyGettingStrategy <TestBean, String>) (propertyAccessStrategyField.get(propertyStringficationStrategy));
    } finally {
      propertyAccessStrategyField.setAccessible(false);
    }
  }
  
  private StringficationStrategy  getStringficationStrategy(PropertyStringficationStrategy propertyStringficationStrategy) throws Exception {
    Field stringficationStrategyField = null;
    try {
      stringficationStrategyField = PropertyStringficationStrategy.class.getDeclaredField("stringficationStrategy");
      stringficationStrategyField.setAccessible(true);
      return (StringficationStrategy) (stringficationStrategyField.get(propertyStringficationStrategy));
    } finally {
      stringficationStrategyField.setAccessible(false);
    }
  }
  
  private Collection <StringficationStrategy> getStringficationStrategies(StringficationHandler stringficationHandler) throws Exception {
    Field stringficationStrategiesField = null;
    try {
      stringficationStrategiesField = StringficationHandler.class.getDeclaredField("stringficationStrategies");
      stringficationStrategiesField.setAccessible(true);
      return (Collection <StringficationStrategy>) (stringficationStrategiesField.get(stringficationHandler));
    } finally {
      stringficationStrategiesField.setAccessible(false);
    }
  }
  
  public DefaultStringficationStrategyFactoryTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNonAnnotatedTestBeanStrategyCreation() {
    assertNull(new DefaultStringficationStrategyFactory(null, null).create(NonAnnotatedTestBean.class));
  }

  public void testCustomStringficationStrategy() {
    assertTrue(new DefaultStringficationStrategyFactory(null, null).create(CustomStringficationStrategyTestBean.class) instanceof CustomStringficationStrategy);
  }

  public void testPrivateStringficationStrategy() {
    try {
      new DefaultStringficationStrategyFactory(null, null).create(PrivateCustomStringficationStrategyTestBean.class);
      fail();
    } catch (RuntimeException ex) {
      assertTrue(ex.getCause() instanceof IllegalAccessException);
    }
  }

  public void testNoDefaultConstructorStringficationStrategy() {
    try {
      new DefaultStringficationStrategyFactory(null, null).create(NonDefaultConstructorCustomStringficationStrategyTestBean.class);
      fail();
    } catch (RuntimeException ex) {
      assertTrue(ex.getCause() instanceof InstantiationException);
    }
  }
  public void testTemplateStringficationWithTemplateCompilationException() {
    TemplateCompilationException error = new TemplateCompilationException();
    MockControl control = MockControl.createControl(TemplateFactory.class);
    try {
      TemplateFactory factory = (TemplateFactory)(control.getMock());
      factory.create("TESTE");
      control.setThrowable(error);
      control.replay();
      new DefaultStringficationStrategyFactory(factory, null).create(TemplateTestBean.class);
      fail();
    } catch (RuntimeException ex) {
      assertSame(error, ex.getCause());
    } catch (Exception ex) {
      fail();
    } finally {
      control.verify();
    }
  }
  
  public void testTemplateStringfication() {
    MockControl control = MockControl.createControl(TemplateFactory.class);
    MockControl control2 = MockControl.createControl(Template.class);
    try {
      
      TemplateTestBean bean = new TemplateTestBean();
      Map <String, Object> context = new HashMap <String, Object> ();
      context.put("this", bean);
      TemplateFactory factory = (TemplateFactory)(control.getMock());
      Template template = (Template)(control2.getMock());
      factory.create("TESTE");
      control.setReturnValue(template);
      template.process(context);
      control2.setReturnValue("TOSTRING");
      control.replay();
      control2.replay();
      StringficationStrategy <TemplateTestBean> strategy = new DefaultStringficationStrategyFactory(factory, null).create(TemplateTestBean.class);
      assertTrue(strategy instanceof TemplateStringficationStrategy);
      StringBuilder sb = new StringBuilder();
      strategy.toString(bean, sb);
      assertEquals("TOSTRING", sb.toString());
    } catch (Exception ex) {
      fail();
    } finally {
      control.verify();
      control2.verify();
    }
  }
  
  public void testWithoutCustomTypeValueStrategyStringficationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> overridenPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> customStrategyPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> valueCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String []> identityArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String []> ((PropertyGettingStrategy <TestBean, String []>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String []> valueArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String []> ((PropertyGettingStrategy <TestBean, String []>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> derivedPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> derivedIdentityPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String []> derivedArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String []> ((PropertyGettingStrategy <TestBean, String []>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Calendar> derivedCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Calendar> ((PropertyGettingStrategy <TestBean, Calendar>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> derivedCustomStrategyPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));

      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("overriden"), "overriden", null);
      propertyAccessStrategyFactoryControl.setReturnValue(overridenPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identity"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("customStrategy"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(customStrategyPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identityCalendar"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("valueCalendar"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valueCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("valueArray"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valueArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identityArray"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedProperty"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedIdentityProperty"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedIdentityPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedArray"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedCalendar"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedCustomStrategy"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedCustomStrategyPropertyAccessStrategy);
      
      propertyAccessStrategyFactoryControl.replay();
   
      StringficationStrategyFactory stringficationStrategyFactory = new DefaultStringficationStrategyFactory(null, propertyAccessStrategyFactory);
      StringficationHandler <TestBean> stringficationHandler = (StringficationHandler <TestBean> )(stringficationStrategyFactory.create(TestBean.class));
      assertEquals(13, getStringficationStrategies(stringficationHandler).size());
      for (StringficationStrategy stringficationStrategy : getStringficationStrategies(stringficationHandler)) {
        PropertyStringficationStrategy <TestBean, String> propertyStringficationStrategy = (PropertyStringficationStrategy <TestBean, String>)(stringficationStrategy);
        StringficationStrategy <String> strategy = getStringficationStrategy(propertyStringficationStrategy);
        if (getPropertyAccessStrategy(propertyStringficationStrategy) == valuePropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == overridenPropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == identityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == customStrategyPropertyAccessStrategy) {
          assertEquals(strategy.getClass(), CustomStringficationStrategy.class);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == identityCalendarPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == valueCalendarPropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == identityArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == valueArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedPropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedIdentityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedCalendarPropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedCustomStrategyPropertyAccessStrategy) {
          assertEquals(strategy.getClass(), CustomStringficationStrategy.class);
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

  public void testWithoutCustomTypeValueStrategyTestBeanChildStringficationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> overridenPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> customStrategyPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> valueCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String []> identityArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String []> ((PropertyGettingStrategy <TestBean, String []>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String []> valueArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String []> ((PropertyGettingStrategy <TestBean, String []>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> derivedPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> derivedIdentityPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String []> derivedArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String []> ((PropertyGettingStrategy <TestBean, String []>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Calendar> derivedCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Calendar> ((PropertyGettingStrategy <TestBean, Calendar>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> derivedCustomStrategyPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));

      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("overriden"), "overriden", null);
      propertyAccessStrategyFactoryControl.setReturnValue(overridenPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identity"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("customStrategy"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(customStrategyPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identityCalendar"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("valueCalendar"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valueCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("valueArray"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valueArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identityArray"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedProperty"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedIdentityProperty"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedIdentityPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedArray"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedCalendar"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedCustomStrategy"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedCustomStrategyPropertyAccessStrategy);
      
      propertyAccessStrategyFactoryControl.replay();
   
      StringficationStrategyFactory stringficationStrategyFactory = new DefaultStringficationStrategyFactory(null, propertyAccessStrategyFactory);
      StringficationHandler <TestBeanChild> stringficationHandler = (StringficationHandler <TestBeanChild> )(stringficationStrategyFactory.create(TestBeanChild.class));
      assertEquals(13, getStringficationStrategies(stringficationHandler).size());
      for (StringficationStrategy stringficationStrategy : getStringficationStrategies(stringficationHandler)) {
        PropertyStringficationStrategy <TestBean, String> propertyStringficationStrategy = (PropertyStringficationStrategy <TestBean, String>)(stringficationStrategy);
        StringficationStrategy <String> strategy = getStringficationStrategy(propertyStringficationStrategy);
        if (getPropertyAccessStrategy(propertyStringficationStrategy) == valuePropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == overridenPropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == identityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == customStrategyPropertyAccessStrategy) {
          assertEquals(strategy.getClass(), CustomStringficationStrategy.class);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == identityCalendarPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == valueCalendarPropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == identityArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == valueArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedPropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedIdentityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedCalendarPropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedCustomStrategyPropertyAccessStrategy) {
          assertEquals(strategy.getClass(), CustomStringficationStrategy.class);
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

  
  public void testWithCustomTypeValueStrategyStringficationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      MockControl customTypeValueStrategyMappingControl = MockControl.createControl(Map.class);

      CalendarStringficationStrategy customCalendarValueStrategy = new CalendarStringficationStrategy();
      Map <Class, StringficationStrategy> customTypeValueStrategyMapping = (Map <Class, StringficationStrategy>)(customTypeValueStrategyMappingControl.getMock());

      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> overridenPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> customStrategyPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> valueCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String []> identityArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String []> ((PropertyGettingStrategy <TestBean, String []>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String []> valueArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String []> ((PropertyGettingStrategy <TestBean, String []>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> derivedPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> derivedIdentityPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String []> derivedArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String []> ((PropertyGettingStrategy <TestBean, String []>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Calendar> derivedCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Calendar> ((PropertyGettingStrategy <TestBean, Calendar>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> derivedCustomStrategyPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));

      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("overriden"), "overriden", null);
      propertyAccessStrategyFactoryControl.setReturnValue(overridenPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identity"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("customStrategy"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(customStrategyPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identityCalendar"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("valueCalendar"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valueCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("valueArray"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valueArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identityArray"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedProperty"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedIdentityProperty"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedIdentityPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedArray"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedCalendar"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedCustomStrategy"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedCustomStrategyPropertyAccessStrategy);
      
      customTypeValueStrategyMapping.containsKey(String.class);
      customTypeValueStrategyMappingControl.setReturnValue(false, 3);
      customTypeValueStrategyMapping.containsKey((new String [0]).getClass());
      customTypeValueStrategyMappingControl.setReturnValue(false, 2);
      customTypeValueStrategyMapping.containsKey(Calendar.class);
      customTypeValueStrategyMappingControl.setReturnValue(true, 2);
      customTypeValueStrategyMapping.get(Calendar.class);
      customTypeValueStrategyMappingControl.setReturnValue(customCalendarValueStrategy, 2);

      propertyAccessStrategyFactoryControl.replay();
      customTypeValueStrategyMappingControl.replay();
    
      StringficationStrategyFactory stringficationStrategyFactory = new DefaultStringficationStrategyFactory(null, propertyAccessStrategyFactory, customTypeValueStrategyMapping);
      StringficationHandler <TestBean> stringficationHandler = (StringficationHandler <TestBean> )(stringficationStrategyFactory.create(TestBean.class));
      assertEquals(13, getStringficationStrategies(stringficationHandler).size());
      for (StringficationStrategy stringficationStrategy : getStringficationStrategies(stringficationHandler)) {
        PropertyStringficationStrategy <TestBean, String> propertyStringficationStrategy = (PropertyStringficationStrategy <TestBean, String>)(stringficationStrategy);
        StringficationStrategy <String> strategy = getStringficationStrategy(propertyStringficationStrategy);
        if (getPropertyAccessStrategy(propertyStringficationStrategy) == valuePropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == overridenPropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == identityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == customStrategyPropertyAccessStrategy) {
          assertEquals(strategy.getClass(), CustomStringficationStrategy.class);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == identityCalendarPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == valueCalendarPropertyAccessStrategy) {
          assertNotSame(strategy, customCalendarValueStrategy);
          assertTrue((Object)(strategy) instanceof CalendarStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == identityArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == valueArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedPropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedIdentityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedCalendarPropertyAccessStrategy) {
          assertNotSame(strategy, customCalendarValueStrategy);
          assertTrue((Object)(strategy) instanceof CalendarStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedCustomStrategyPropertyAccessStrategy) {
          assertEquals(strategy.getClass(), CustomStringficationStrategy.class);
        } else {
          fail();
        }
      }
      propertyAccessStrategyFactoryControl.verify();
      customTypeValueStrategyMappingControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testWithCustomTypeValueStrategyTestBeanChildStringficationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      MockControl customTypeValueStrategyMappingControl = MockControl.createControl(Map.class);

      CalendarStringficationStrategy customCalendarValueStrategy = new CalendarStringficationStrategy();
      Map <Class, StringficationStrategy> customTypeValueStrategyMapping = (Map <Class, StringficationStrategy>)(customTypeValueStrategyMappingControl.getMock());

      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> overridenPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> customStrategyPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> valueCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String []> identityArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String []> ((PropertyGettingStrategy <TestBean, String []>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String []> valueArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String []> ((PropertyGettingStrategy <TestBean, String []>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> derivedPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> derivedIdentityPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String []> derivedArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String []> ((PropertyGettingStrategy <TestBean, String []>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, Calendar> derivedCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, Calendar> ((PropertyGettingStrategy <TestBean, Calendar>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> derivedCustomStrategyPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));

      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("value"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("overriden"), "overriden", null);
      propertyAccessStrategyFactoryControl.setReturnValue(overridenPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identity"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("customStrategy"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(customStrategyPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identityCalendar"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("valueCalendar"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valueCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("valueArray"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valueArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identityArray"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedProperty"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedIdentityProperty"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedIdentityPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedArray"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedCalendar"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredMethod("getDerivedCustomStrategy"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(derivedCustomStrategyPropertyAccessStrategy);
      
      customTypeValueStrategyMapping.containsKey(String.class);
      customTypeValueStrategyMappingControl.setReturnValue(false, 3);
      customTypeValueStrategyMapping.containsKey((new String [0]).getClass());
      customTypeValueStrategyMappingControl.setReturnValue(false, 2);
      customTypeValueStrategyMapping.containsKey(Calendar.class);
      customTypeValueStrategyMappingControl.setReturnValue(true, 2);
      customTypeValueStrategyMapping.get(Calendar.class);
      customTypeValueStrategyMappingControl.setReturnValue(customCalendarValueStrategy, 2);

      propertyAccessStrategyFactoryControl.replay();
      customTypeValueStrategyMappingControl.replay();
    
      StringficationStrategyFactory stringficationStrategyFactory = new DefaultStringficationStrategyFactory(null, propertyAccessStrategyFactory, customTypeValueStrategyMapping);
      StringficationHandler <TestBeanChild> stringficationHandler = (StringficationHandler <TestBeanChild> )(stringficationStrategyFactory.create(TestBeanChild.class));
      assertEquals(13, getStringficationStrategies(stringficationHandler).size());
      for (StringficationStrategy stringficationStrategy : getStringficationStrategies(stringficationHandler)) {
        PropertyStringficationStrategy <TestBean, String> propertyStringficationStrategy = (PropertyStringficationStrategy <TestBean, String>)(stringficationStrategy);
        StringficationStrategy <String> strategy = getStringficationStrategy(propertyStringficationStrategy);
        if (getPropertyAccessStrategy(propertyStringficationStrategy) == valuePropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == overridenPropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == identityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == customStrategyPropertyAccessStrategy) {
          assertEquals(strategy.getClass(), CustomStringficationStrategy.class);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == identityCalendarPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == valueCalendarPropertyAccessStrategy) {
          assertNotSame(strategy, customCalendarValueStrategy);
          assertTrue((Object)(strategy) instanceof CalendarStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == identityArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == valueArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedPropertyAccessStrategy) {
          assertTrue(strategy instanceof ToStringStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedIdentityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedCalendarPropertyAccessStrategy) {
          assertNotSame(strategy, customCalendarValueStrategy);
          assertTrue((Object)(strategy) instanceof CalendarStringficationStrategy);
        } else if (getPropertyAccessStrategy(propertyStringficationStrategy) == derivedCustomStrategyPropertyAccessStrategy) {
          assertEquals(strategy.getClass(), CustomStringficationStrategy.class);
        } else {
          fail();
        }
      }
      propertyAccessStrategyFactoryControl.verify();
      customTypeValueStrategyMappingControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testDerivedPropertyOnlyClassStringficationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <DerivedPropertyOnlyTestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <DerivedPropertyOnlyTestBean, String> ((PropertyGettingStrategy <DerivedPropertyOnlyTestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
    
      propertyAccessStrategyFactory.create(DerivedPropertyOnlyTestBean.class.getDeclaredMethod("getProperty"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
    
      propertyAccessStrategyFactoryControl.replay();
   
      StringficationStrategyFactory stringficationStrategyFactory = new DefaultStringficationStrategyFactory(null, propertyAccessStrategyFactory);
      assertNotNull(stringficationStrategyFactory.create(DerivedPropertyOnlyTestBean.class));
      propertyAccessStrategyFactoryControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testPropertyOnlyClassStringficationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <DerivedPropertyOnlyTestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <DerivedPropertyOnlyTestBean, String> ((PropertyGettingStrategy <DerivedPropertyOnlyTestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
    
      propertyAccessStrategyFactory.create(PropertyOnlyTestBean.class.getDeclaredField("property"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
    
      propertyAccessStrategyFactoryControl.replay();
   
      StringficationStrategyFactory stringficationStrategyFactory = new DefaultStringficationStrategyFactory(null, propertyAccessStrategyFactory);
      assertNotNull(stringficationStrategyFactory.create(PropertyOnlyTestBean.class));
      propertyAccessStrategyFactoryControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testPropertyOnlyChildClassStringficationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <DerivedPropertyOnlyTestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <DerivedPropertyOnlyTestBean, String> ((PropertyGettingStrategy <DerivedPropertyOnlyTestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
    
      propertyAccessStrategyFactory.create(PropertyOnlyTestBean.class.getDeclaredField("property"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
    
      propertyAccessStrategyFactoryControl.replay();
   
      StringficationStrategyFactory stringficationStrategyFactory = new DefaultStringficationStrategyFactory(null, propertyAccessStrategyFactory);
      assertNotNull(stringficationStrategyFactory.create(PropertyOnlyTestBeanChild.class));
      propertyAccessStrategyFactoryControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

   public void testNonCloneableCustomTypeStringficationStrategyFieldTestBeanCreation() {
    MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
    try {
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      propertyAccessStrategyFactory.create(CustomTypeStrategyTestBean.class.getDeclaredField("x"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
      propertyAccessStrategyFactoryControl.replay();

      TypeMap <StringficationStrategy> typeMap = new HashTypeMap <StringficationStrategy> ();
      typeMap.put(String.class, new NonCloneableStringficationStrategy());
      StringficationStrategyFactory stringficationStrategyFactory = new DefaultStringficationStrategyFactory(null, propertyAccessStrategyFactory, (Map <Class, StringficationStrategy>) typeMap);
      StringficationStrategy <CustomTypeStrategyTestBean> stringficationStrategy = (StringficationStrategy <CustomTypeStrategyTestBean> )(stringficationStrategyFactory.create(CustomTypeStrategyTestBean.class));
      fail();
    } catch (RuntimeException ex) {
      assert(ex.getCause() instanceof CloneNotSupportedException);
    } catch (Exception ex) {
      fail();
    } finally {
      propertyAccessStrategyFactoryControl.verify();
    } 
  }

  public void testPrivateStringficationStrategyFieldTestBeanCreation() {
    MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
    try {
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      propertyAccessStrategyFactory.create(PrivateStringficationStrategyFieldTestBean.class.getDeclaredField("x"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
      propertyAccessStrategyFactoryControl.replay();
      StringficationStrategyFactory stringficationStrategyFactory = new DefaultStringficationStrategyFactory(null, propertyAccessStrategyFactory, null);
      StringficationStrategy <PrivateStringficationStrategyFieldTestBean> stringficationStrategy = (StringficationStrategy <PrivateStringficationStrategyFieldTestBean> )(stringficationStrategyFactory.create(PrivateStringficationStrategyFieldTestBean.class));
      fail();
    } catch (RuntimeException ex) {
      assertTrue(ex.getCause() instanceof IllegalAccessException);
    } catch (Exception ex) {
      fail();
    } finally {
      propertyAccessStrategyFactoryControl.verify();
    }
  }
  
  public void testNonDefaultConstructorStringficationStrategyFieldTestBeanCreation() {
    MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
    try {
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      propertyAccessStrategyFactory.create(NonDefaultConstrutorStringficationStrategyFieldTestBean.class.getDeclaredField("x"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
      propertyAccessStrategyFactoryControl.replay();
      
      StringficationStrategyFactory stringficationStrategyFactory = new DefaultStringficationStrategyFactory(null, propertyAccessStrategyFactory, null);
      StringficationStrategy <NonDefaultConstrutorStringficationStrategyFieldTestBean> stringficationStrategy = (StringficationStrategy <NonDefaultConstrutorStringficationStrategyFieldTestBean> )(stringficationStrategyFactory.create(NonDefaultConstrutorStringficationStrategyFieldTestBean.class));
      fail();
    } catch (RuntimeException ex) {
      assertTrue(ex.getCause() instanceof InstantiationException);
    } catch (Exception ex) {
      fail();
    } finally {
      propertyAccessStrategyFactoryControl.verify();
    }
  }
  public void testTestInterfaceChildCustomStringficationStrategy() {
    assertEquals(new DefaultStringficationStrategyFactory(null, null).create(TestInterfaceChild.class).getClass(), CustomStringficationStrategy.class);
  }
  public void testTestInterface2AbstractClassChildStringficationStrategyCreation() {
    MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
    try {
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      propertyAccessStrategyFactory.create(AbstractClass.class.getDeclaredMethod("getX"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
      propertyAccessStrategyFactoryControl.replay();

      StringficationStrategyFactory stringficationStrategyFactory = new DefaultStringficationStrategyFactory(null, propertyAccessStrategyFactory, null);
      StringficationStrategy strategy = stringficationStrategyFactory.create(TestInterface2AbstractClassChild.class);
      assertEquals(1, getStringficationStrategies((StringficationHandler)(strategy)).size());
      PropertyStringficationStrategy propertyStrategy = (PropertyStringficationStrategy)(getStringficationStrategies((StringficationHandler)(strategy)).iterator().next());
      assertTrue(getStringficationStrategy(propertyStrategy) instanceof ToStringStringficationStrategy);
      assertSame(valuePropertyAccessStrategy, getPropertyAccessStrategy(propertyStrategy));
    } catch (Exception ex) {
      fail();
    } finally {
      propertyAccessStrategyFactoryControl.verify();
    }
    
  }
}
