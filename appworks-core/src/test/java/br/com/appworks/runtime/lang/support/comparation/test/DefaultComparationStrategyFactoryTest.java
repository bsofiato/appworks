/*
 * DefaultComparationStrategyFactoryTest.java
 * JUnit based test
 *
 * Created on 6 de Julho de 2005, 00:31
 */

package br.com.appworks.runtime.lang.support.comparation.test;


import br.com.appworks.runtime.lang.Comparable;
import br.com.appworks.runtime.lang.ComparationPolicy;
import br.com.appworks.runtime.lang.support.HashTypeMap;
import br.com.appworks.runtime.lang.support.TypeMap;
import br.com.appworks.runtime.lang.support.comparation.AbstractComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.ArrayValueComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.CalendarValueComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.ComparationHandler;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.comparation.DefaultComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.comparation.IdentityComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.PropertyComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.ValueComparationStrategy;
import br.com.appworks.runtime.lang.support.property.PropertyAccessStrategy;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.property.PropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategy;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import junit.framework.*;
import org.easymock.MockControl;
import br.com.appworks.runtime.lang.OrderPolicy;

/**
 *
 * @author Bubu
 */
public class DefaultComparationStrategyFactoryTest extends TestCase {
  private static class PrivateComparationStrategy <Type> extends AbstractComparationStrategy <Type> {
    public boolean equals(Type op1, Type op2) {
      throw new UnsupportedOperationException();
    }
    public int hashCode(Type op1) {
      throw new UnsupportedOperationException();
    }
    protected int doCompare(Type op1, Type op2) {
      throw new UnsupportedOperationException();
    }
  }
  public static class NoDefaultConstrutorComparationStrategy<Type> extends AbstractComparationStrategy <Type> {
    public NoDefaultConstrutorComparationStrategy(int x) {
    }
    public boolean equals(Type op1, Type op2) {
      throw new UnsupportedOperationException();
    }
    public int hashCode(Type op1) {
      throw new UnsupportedOperationException();
    }
    protected int doCompare(Type op1, Type op2) {
      throw new UnsupportedOperationException();
    }
  }
  
  public static class MockComparationStrategy <Type extends Object> extends AbstractComparationStrategy <Type> {
    public boolean equals(Type op1, Type op2) {
      throw new UnsupportedOperationException();
    }
    public int hashCode(Type op1) {
      throw new UnsupportedOperationException();
    }
    protected int doCompare(Type op1, Type op2) {
      throw new UnsupportedOperationException();
    }
  }

  public static class NonClonableComparationStrategy <Type extends Object> extends AbstractComparationStrategy <Type> {
    public boolean equals(Type op1, Type op2) {
      throw new UnsupportedOperationException();
    }
    public int hashCode(Type op1) {
      throw new UnsupportedOperationException();
    }
    protected int doCompare(Type op1, Type op2) {
      throw new UnsupportedOperationException();
    }
    public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
    }
  }

  @Comparable(strategy = PrivateComparationStrategy.class)
  public static class PrivateComparationStrategyTestBean {
  }

  @Comparable(strategy = NoDefaultConstrutorComparationStrategy.class)
  public static class NoDefaultConstrutorComparationStrategyTestBean {
  }

  @Comparable
  public static class PrivateComparationStrategyFieldTestBean {
    @Comparable(strategy = PrivateComparationStrategy.class)
    private int x;
  }

  @Comparable
  public static class NoDefaultConstrutorComparationStrategyFieldTestBean {
    @Comparable(strategy = NoDefaultConstrutorComparationStrategy.class)
    private int x;
  }

  @Comparable(strategy = MockComparationStrategy.class)
  public static class CustomComparationStrategyTestBean {
  }

  @Comparable(strategy = MockComparationStrategy.class, order=OrderPolicy.INVERSE)
  public static class DescendingCustomComparationStrategyTestBean {
  }
  
  @Comparable
  public static class CustomTypeStrategyTestBean {
    @Comparable
    private String x;
  }
  
  public static class FieldOnlyTestBeanChild extends CustomTypeStrategyTestBean {
  }

  @Comparable
  public static class DerivedPropertyOnlyTestBean {
    @Comparable
    private String getProperty() {
      return null;
    }
  }
  @Comparable
  public static class PropertyOnlyTestBean {
    @Comparable
    private String property;
  }
  @Comparable
  public static class DoubleCustomTypeStrategyTestBean {
    @Comparable
    private Calendar first;
    
    @Comparable
    private Calendar second;
  }
  
  @Comparable
  public static class TestBean {
    @Comparable
    private String value;
    
    @Comparable (getter = "overriden")
    private String overriden;
    
    @Comparable (ComparationPolicy.IDENTITY)
    private String identity;
    
    @Comparable (evaluationOrder = 2346)
    private String index;

    @Comparable (order = OrderPolicy.INVERSE)
    private String order;

    @Comparable (order = OrderPolicy.INVERSE, evaluationOrder = 2347, strategy = MockComparationStrategy.class)
    private String customStrategy;

    private String nonParticipant;

    @Comparable (ComparationPolicy.IDENTITY)
    private Calendar identityCalendar;
  
    @Comparable (order = OrderPolicy.INVERSE)
    private Calendar valueCalendar;
    
    @Comparable (order = OrderPolicy.INVERSE)
    private String [] valueArray;

    @Comparable (ComparationPolicy.IDENTITY)
    private String [] identityArray;
    
    @Comparable (ComparationPolicy.VALUE)
    private String getDerivedProperty() {
      return null;
    }
    @Comparable (ComparationPolicy.IDENTITY)
    private String getDerivedIdentityProperty() {
      return null;
    }

    @Comparable (ComparationPolicy.VALUE)
    private String [] getDerivedArray() {
      return null;
    }
    @Comparable (value=ComparationPolicy.VALUE, order=OrderPolicy.INVERSE)
    private Calendar getDerivedCalendar() {
      return null;
    }
    
    @Comparable (value=ComparationPolicy.VALUE, strategy=MockComparationStrategy.class, evaluationOrder = 5947)
    private String getDerivedCustomStrategy() {
      return null;
    }
    
    private String getNonComparableProperty() {
      return null;
    }
  }

  @Comparable(strategy = MockComparationStrategy.class)
  public static interface TestInterface {
  }
  
  public static class TestInterfaceChild implements TestInterface {
  }
  
  public static class TestBeanChild extends TestBean{
  }

 @Comparable
  public static interface TestInterface2 {
    @Comparable
    String getX();
  }
  
  public static abstract class AbstractClass {
    public String getX() {
      return "X";
    }
  }
  
  public static class TestInterface2AbstractClassChild extends AbstractClass implements TestInterface2 {
    
  }

  public DefaultComparationStrategyFactoryTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }

  private PropertyGettingStrategy getPropertyAccessStrategy(PropertyComparationStrategy<TestBean, String> propertyComparationStrategy) throws Exception {
    Field propertyAccessStrategyField = null;
    try {
      propertyAccessStrategyField = PropertyComparationStrategy.class.getDeclaredField("propertyAccessStrategy");
      propertyAccessStrategyField.setAccessible(true);
      return (PropertyGettingStrategy <TestBean, String>) (propertyAccessStrategyField.get(propertyComparationStrategy));
    } finally {
      propertyAccessStrategyField.setAccessible(false);
    }
  }
  private ComparationStrategy  getComparationStrategy(PropertyComparationStrategy propertyComparationStrategy) throws Exception {
    Field comparationStrategyField = null;
    try {
      comparationStrategyField = PropertyComparationStrategy.class.getDeclaredField("comparationStrategy");
      comparationStrategyField.setAccessible(true);
      return (ComparationStrategy) (comparationStrategyField.get(propertyComparationStrategy));
    } finally {
      comparationStrategyField.setAccessible(false);
    }
  }
  
  private Collection <PropertyComparationStrategy> getComparationStrategiesEntries(ComparationHandler comparationHandler) throws Exception {
    Field comparationStrategiesField = null;
    try {
      comparationStrategiesField = ComparationHandler.class.getDeclaredField("comparationStrategies");
      comparationStrategiesField.setAccessible(true);
      return (Collection <PropertyComparationStrategy>)(comparationStrategiesField.get(comparationHandler));
    } finally {
      comparationStrategiesField.setAccessible(false);
    }
  }

  public void testWithoutCustomTypeValueStrategyComparationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> overridenPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> indexPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> orderPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
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
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("order"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(orderPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("index"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(indexPropertyAccessStrategy);
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
   
      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(propertyAccessStrategyFactory);
      ComparationHandler <TestBean> comparationHandler = (ComparationHandler <TestBean> )(comparationStrategyFactory.create(TestBean.class));
      assertEquals(15, getComparationStrategiesEntries(comparationHandler).size());
      Set indexSet = new HashSet();
      for (PropertyComparationStrategy propertyComparationStrategy : getComparationStrategiesEntries(comparationHandler)) {
        ComparationStrategy <String> strategy = getComparationStrategy(propertyComparationStrategy);
        assertFalse(indexSet.contains(propertyComparationStrategy.getIndex()));
        indexSet.add(propertyComparationStrategy.getIndex());
        if (getPropertyAccessStrategy(propertyComparationStrategy) == valuePropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == overridenPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == identityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == indexPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
          assertEquals(2346, propertyComparationStrategy.getIndex());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == orderPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == customStrategyPropertyAccessStrategy) {
          assertTrue(strategy instanceof MockComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
          assertEquals(2347, propertyComparationStrategy.getIndex());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == identityCalendarPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == valueCalendarPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == identityArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == valueArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedIdentityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedCalendarPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedCustomStrategyPropertyAccessStrategy) {
          assertEquals(5947, propertyComparationStrategy.getIndex());
          assertTrue(strategy instanceof MockComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
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

  public void testWithoutCustomTypeValueTestBeanChildStrategyComparationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> overridenPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> indexPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> orderPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
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
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("order"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(orderPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("index"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(indexPropertyAccessStrategy);
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
      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(propertyAccessStrategyFactory);
      ComparationHandler <TestBeanChild> comparationHandler = (ComparationHandler <TestBeanChild> )(comparationStrategyFactory.create(TestBeanChild.class));
      assertEquals(15, getComparationStrategiesEntries(comparationHandler).size());
      Set indexSet = new HashSet();
      for (PropertyComparationStrategy propertyComparationStrategy : getComparationStrategiesEntries(comparationHandler)) {
        ComparationStrategy <String> strategy = getComparationStrategy(propertyComparationStrategy);
        assertFalse(indexSet.contains(propertyComparationStrategy.getIndex()));
        indexSet.add(propertyComparationStrategy.getIndex());
        if (getPropertyAccessStrategy(propertyComparationStrategy) == valuePropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == overridenPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == identityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == indexPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
          assertEquals(2346, propertyComparationStrategy.getIndex());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == orderPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == customStrategyPropertyAccessStrategy) {
          assertTrue(strategy instanceof MockComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
          assertEquals(2347, propertyComparationStrategy.getIndex());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == identityCalendarPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == valueCalendarPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == identityArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == valueArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedIdentityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedCalendarPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedCustomStrategyPropertyAccessStrategy) {
          assertEquals(5947, propertyComparationStrategy.getIndex());
          assertTrue(strategy instanceof MockComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
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

  public void testRegularClassComparationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      propertyAccessStrategyFactoryControl.replay();

      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(propertyAccessStrategyFactory);
      ComparationStrategy <Object> comparationStrategy = (ComparationStrategy <Object> )(comparationStrategyFactory.create(Object.class));
      assertNull(comparationStrategy);
      propertyAccessStrategyFactoryControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }  
  public void testDerivedPropertyOnlyClassComparationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <DerivedPropertyOnlyTestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <DerivedPropertyOnlyTestBean, String> ((PropertyGettingStrategy <DerivedPropertyOnlyTestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
    
      propertyAccessStrategyFactory.create(DerivedPropertyOnlyTestBean.class.getDeclaredMethod("getProperty"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
    
      propertyAccessStrategyFactoryControl.replay();
   
      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(propertyAccessStrategyFactory);
      assertNotNull(comparationStrategyFactory.create(DerivedPropertyOnlyTestBean.class));
      propertyAccessStrategyFactoryControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
    
  }

   public void testPropertyOnlyClassComparationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <DerivedPropertyOnlyTestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <DerivedPropertyOnlyTestBean, String> ((PropertyGettingStrategy <DerivedPropertyOnlyTestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
    
      propertyAccessStrategyFactory.create(PropertyOnlyTestBean.class.getDeclaredField("property"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
    
      propertyAccessStrategyFactoryControl.replay();
   
      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(propertyAccessStrategyFactory);
      assertNotNull(comparationStrategyFactory.create(PropertyOnlyTestBean.class));
      propertyAccessStrategyFactoryControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
    
  }
  
  public void testWithCustomTypeValueStrategyComparationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      MockControl customTypeValueStrategyMappingControl = MockControl.createControl(Map.class);
      
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      Map <Class, ComparationStrategy> customTypeValueStrategyMapping = (Map <Class, ComparationStrategy>)(customTypeValueStrategyMappingControl.getMock());
      CalendarValueComparationStrategy customCalendarValueStrategy = new CalendarValueComparationStrategy();
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> overridenPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> indexPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> orderPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> customStrategyPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> valueCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> valueArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
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
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("order"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(orderPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("index"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(indexPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("customStrategy"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(customStrategyPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identityCalendar"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("valueCalendar"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valueCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identityArray"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("valueArray"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valueArrayPropertyAccessStrategy);
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
      customTypeValueStrategyMappingControl.setReturnValue(false, 5);
      customTypeValueStrategyMapping.containsKey((new String [0]).getClass());
      customTypeValueStrategyMappingControl.setReturnValue(false, 2);
      customTypeValueStrategyMapping.containsKey(Calendar.class);
      customTypeValueStrategyMappingControl.setReturnValue(true, 2);
      customTypeValueStrategyMapping.get(Calendar.class);
      customTypeValueStrategyMappingControl.setReturnValue(customCalendarValueStrategy, 2);
      
      
      propertyAccessStrategyFactoryControl.replay();
      customTypeValueStrategyMappingControl.replay();
      
      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(propertyAccessStrategyFactory, customTypeValueStrategyMapping);
      ComparationHandler <TestBean> comparationHandler = (ComparationHandler <TestBean> )(comparationStrategyFactory.create(TestBean.class));
      assertEquals(15, getComparationStrategiesEntries(comparationHandler).size());
      Set indexSet = new HashSet();
      for (PropertyComparationStrategy propertyComparationStrategy : getComparationStrategiesEntries(comparationHandler)) {
        ComparationStrategy <String> strategy = getComparationStrategy(propertyComparationStrategy);
        assertFalse(indexSet.contains(propertyComparationStrategy.getIndex()));
        indexSet.add(propertyComparationStrategy.getIndex());
        if (getPropertyAccessStrategy(propertyComparationStrategy) == valuePropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == overridenPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == identityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == indexPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
          assertEquals(2346, propertyComparationStrategy.getIndex());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == orderPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == customStrategyPropertyAccessStrategy) {
          assertTrue(strategy instanceof MockComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
          assertEquals(2347, propertyComparationStrategy.getIndex());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == identityCalendarPropertyAccessStrategy) {
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
          assertTrue(strategy instanceof IdentityComparationStrategy);
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == valueCalendarPropertyAccessStrategy) {
          assertNotSame(strategy, customCalendarValueStrategy);
          assertTrue((Object)(strategy) instanceof CalendarValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == identityArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == valueArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedIdentityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedCalendarPropertyAccessStrategy) {
          assertNotSame(strategy, customCalendarValueStrategy);
          assertTrue((Object)(strategy) instanceof CalendarValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedCustomStrategyPropertyAccessStrategy) {
          assertEquals(5947, propertyComparationStrategy.getIndex());
          assertTrue(strategy instanceof MockComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
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

    public void testWithCustomTypeValueStrategyTestBeanChildComparationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      MockControl customTypeValueStrategyMappingControl = MockControl.createControl(Map.class);
      
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      Map <Class, ComparationStrategy> customTypeValueStrategyMapping = (Map <Class, ComparationStrategy>)(customTypeValueStrategyMappingControl.getMock());
      CalendarValueComparationStrategy customCalendarValueStrategy = new CalendarValueComparationStrategy();
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> overridenPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> indexPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> orderPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> customStrategyPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> valueCalendarPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> identityArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <TestBean, String> valueArrayPropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
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
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("order"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(orderPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("index"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(indexPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("customStrategy"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(customStrategyPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identityCalendar"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("valueCalendar"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valueCalendarPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("identityArray"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(identityArrayPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(TestBean.class.getDeclaredField("valueArray"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valueArrayPropertyAccessStrategy);
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
      customTypeValueStrategyMappingControl.setReturnValue(false, 5);
      customTypeValueStrategyMapping.containsKey((new String [0]).getClass());
      customTypeValueStrategyMappingControl.setReturnValue(false, 2);
      customTypeValueStrategyMapping.containsKey(Calendar.class);
      customTypeValueStrategyMappingControl.setReturnValue(true, 2);
      customTypeValueStrategyMapping.get(Calendar.class);
      customTypeValueStrategyMappingControl.setReturnValue(customCalendarValueStrategy, 2);
      
      
      propertyAccessStrategyFactoryControl.replay();
      customTypeValueStrategyMappingControl.replay();
      
      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(propertyAccessStrategyFactory, customTypeValueStrategyMapping);
      ComparationHandler <TestBeanChild> comparationHandler = (ComparationHandler <TestBeanChild> )(comparationStrategyFactory.create(TestBeanChild.class));
      assertEquals(15, getComparationStrategiesEntries(comparationHandler).size());
      Set indexSet = new HashSet();
      for (PropertyComparationStrategy propertyComparationStrategy : getComparationStrategiesEntries(comparationHandler)) {
        ComparationStrategy <String> strategy = getComparationStrategy(propertyComparationStrategy);
        assertFalse(indexSet.contains(propertyComparationStrategy.getIndex()));
        indexSet.add(propertyComparationStrategy.getIndex());
        if (getPropertyAccessStrategy(propertyComparationStrategy) == valuePropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == overridenPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == identityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == indexPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
          assertEquals(2346, propertyComparationStrategy.getIndex());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == orderPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == customStrategyPropertyAccessStrategy) {
          assertTrue(strategy instanceof MockComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
          assertEquals(2347, propertyComparationStrategy.getIndex());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == identityCalendarPropertyAccessStrategy) {
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
          assertTrue(strategy instanceof IdentityComparationStrategy);
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == valueCalendarPropertyAccessStrategy) {
          assertNotSame(strategy, customCalendarValueStrategy);
          assertTrue((Object)(strategy) instanceof CalendarValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == identityArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == valueArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedPropertyAccessStrategy) {
          assertTrue(strategy instanceof ValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedIdentityPropertyAccessStrategy) {
          assertTrue(strategy instanceof IdentityComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedArrayPropertyAccessStrategy) {
          assertTrue(strategy instanceof ArrayValueComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedCalendarPropertyAccessStrategy) {
          assertNotSame(strategy, customCalendarValueStrategy);
          assertTrue((Object)(strategy) instanceof CalendarValueComparationStrategy);
          assertEquals(OrderPolicy.INVERSE, strategy.getOrderPolicy());
        } else if (getPropertyAccessStrategy(propertyComparationStrategy) == derivedCustomStrategyPropertyAccessStrategy) {
          assertEquals(5947, propertyComparationStrategy.getIndex());
          assertTrue(strategy instanceof MockComparationStrategy);
          assertEquals(OrderPolicy.NATURAL, strategy.getOrderPolicy());
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
  
   public void testWithDoubleCustomTypeValueStrategyComparationStrategyCreation() {
    try {
      MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
      MockControl customTypeValueStrategyMappingControl = MockControl.createControl(Map.class);
      
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      Map <Class, ComparationStrategy> customTypeValueStrategyMapping = (Map <Class, ComparationStrategy>)(customTypeValueStrategyMappingControl.getMock());
      CalendarValueComparationStrategy customCalendarValueStrategy = new CalendarValueComparationStrategy();
      PropertyAccessStrategy <DoubleCustomTypeStrategyTestBean, Calendar> firstPropertyAccessStrategy = new DefaultPropertyAccessStrategy <DoubleCustomTypeStrategyTestBean, Calendar> ((PropertyGettingStrategy <DoubleCustomTypeStrategyTestBean, Calendar>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      PropertyAccessStrategy <DoubleCustomTypeStrategyTestBean, Calendar> secondPropertyAccessStrategy = new DefaultPropertyAccessStrategy <DoubleCustomTypeStrategyTestBean, Calendar> ((PropertyGettingStrategy <DoubleCustomTypeStrategyTestBean, Calendar>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
    
      propertyAccessStrategyFactory.create(DoubleCustomTypeStrategyTestBean.class.getDeclaredField("first"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(firstPropertyAccessStrategy);
      propertyAccessStrategyFactory.create(DoubleCustomTypeStrategyTestBean.class.getDeclaredField("second"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(secondPropertyAccessStrategy);
    
      customTypeValueStrategyMapping.containsKey(Calendar.class);
      customTypeValueStrategyMappingControl.setReturnValue(true, 2);
      customTypeValueStrategyMapping.get(Calendar.class);
      customTypeValueStrategyMappingControl.setReturnValue(customCalendarValueStrategy, 2);
      
      propertyAccessStrategyFactoryControl.replay();
      customTypeValueStrategyMappingControl.replay();
      
      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(propertyAccessStrategyFactory, customTypeValueStrategyMapping);
      ComparationHandler <DoubleCustomTypeStrategyTestBean> comparationHandler = (ComparationHandler <DoubleCustomTypeStrategyTestBean> )(comparationStrategyFactory.create(DoubleCustomTypeStrategyTestBean.class));
      assertEquals(2, getComparationStrategiesEntries(comparationHandler).size());
      Set indexSet = new HashSet();
      for (PropertyComparationStrategy propertyComparationStrategy : getComparationStrategiesEntries(comparationHandler)) {
        ComparationStrategy strategy = getComparationStrategy(propertyComparationStrategy);
        assertTrue(strategy instanceof CalendarValueComparationStrategy);
        assertNotSame(strategy, customCalendarValueStrategy);
        assertFalse(indexSet.contains(System.identityHashCode(strategy)));
        indexSet.add(System.identityHashCode(strategy));
      }
      propertyAccessStrategyFactoryControl.verify();
      customTypeValueStrategyMappingControl.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  public void testCustomComparationStrategyTestBeanComparationStrategyCreation() {
    ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(null, null);
    ComparationStrategy <CustomComparationStrategyTestBean> comparationStrategy = (ComparationStrategy <CustomComparationStrategyTestBean> )(comparationStrategyFactory.create(CustomComparationStrategyTestBean.class));
    assertTrue(comparationStrategy instanceof MockComparationStrategy);
    assertEquals(OrderPolicy.NATURAL, comparationStrategy.getOrderPolicy());
  }
  public void testDescendingCustomComparationStrategyTestBeanComparationStrategyCreation() {
    ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(null, null);
    ComparationStrategy <DescendingCustomComparationStrategyTestBean> comparationStrategy = (ComparationStrategy <DescendingCustomComparationStrategyTestBean> )(comparationStrategyFactory.create(DescendingCustomComparationStrategyTestBean.class));
    assertTrue(comparationStrategy instanceof MockComparationStrategy);
    assertEquals(OrderPolicy.INVERSE, comparationStrategy.getOrderPolicy());
  }
  public void testPrivateComparationStrategyTestBeanCreation() {
    try {
      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(null, null);
      ComparationStrategy <PrivateComparationStrategyTestBean> comparationStrategy = (ComparationStrategy <PrivateComparationStrategyTestBean> )(comparationStrategyFactory.create(PrivateComparationStrategyTestBean.class));
      fail();
    } catch (RuntimeException ex) {
      assertTrue(ex.getCause() instanceof IllegalAccessException);
    } catch (Exception ex) {
      fail();
    }
  }
  public void testNoDefaultConstructorComparationStrategyTestBeanCreation() {
    try {
      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(null, null);
      ComparationStrategy <NoDefaultConstrutorComparationStrategyTestBean> comparationStrategy = (ComparationStrategy <NoDefaultConstrutorComparationStrategyTestBean> )(comparationStrategyFactory.create(NoDefaultConstrutorComparationStrategyTestBean.class));
      fail();
    } catch (RuntimeException ex) {
      assertTrue(ex.getCause() instanceof InstantiationException);
    } catch (Exception ex) {
      fail();
    }
  }

  public void testPrivateComparationStrategyFieldTestBeanCreation() {
    MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
    try {
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      propertyAccessStrategyFactory.create(PrivateComparationStrategyFieldTestBean.class.getDeclaredField("x"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
      propertyAccessStrategyFactoryControl.replay();
      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(propertyAccessStrategyFactory, null);
      ComparationStrategy <PrivateComparationStrategyFieldTestBean> comparationStrategy = (ComparationStrategy <PrivateComparationStrategyFieldTestBean> )(comparationStrategyFactory.create(PrivateComparationStrategyFieldTestBean.class));
      fail();
    } catch (RuntimeException ex) {
      assertTrue(ex.getCause() instanceof IllegalAccessException);
    } catch (Exception ex) {
      fail();
    } finally {
      propertyAccessStrategyFactoryControl.verify();
    }
  }
  public void testNoDefaultConstructorComparationStrategyFieldTestBeanCreation() {
    MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
    try {
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      propertyAccessStrategyFactory.create(NoDefaultConstrutorComparationStrategyFieldTestBean.class.getDeclaredField("x"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
      propertyAccessStrategyFactoryControl.replay();
      
      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(propertyAccessStrategyFactory, null);
      ComparationStrategy <NoDefaultConstrutorComparationStrategyFieldTestBean> comparationStrategy = (ComparationStrategy <NoDefaultConstrutorComparationStrategyFieldTestBean> )(comparationStrategyFactory.create(NoDefaultConstrutorComparationStrategyFieldTestBean.class));
      fail();
    } catch (RuntimeException ex) {
      assertTrue(ex.getCause() instanceof InstantiationException);
    } catch (Exception ex) {
      fail();
    } finally {
      propertyAccessStrategyFactoryControl.verify();
    }
  }
  public void testNonCloneableCustomTypeComparationStrategyFieldTestBeanCreation() {
    MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
    try {
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      propertyAccessStrategyFactory.create(CustomTypeStrategyTestBean.class.getDeclaredField("x"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
      propertyAccessStrategyFactoryControl.replay();

      TypeMap <ComparationStrategy> typeMap = new HashTypeMap <ComparationStrategy> ();
      typeMap.put(String.class, new NonClonableComparationStrategy());
      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(propertyAccessStrategyFactory, typeMap);
      ComparationStrategy <CustomTypeStrategyTestBean> comparationStrategy = (ComparationStrategy <CustomTypeStrategyTestBean> )(comparationStrategyFactory.create(CustomTypeStrategyTestBean.class));
      fail();
    } catch (RuntimeException ex) {
      assertTrue(ex.getCause() instanceof CloneNotSupportedException);
    } catch (Exception ex) {
      fail();
    } finally {
      propertyAccessStrategyFactoryControl.verify();
    } 
  }
  
  public void testFieldOnlyTestBeanComparationStrategyFieldTestBeanCreation() {
    MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
    try {
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      propertyAccessStrategyFactory.create(CustomTypeStrategyTestBean.class.getDeclaredField("x"), "", null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
      propertyAccessStrategyFactoryControl.replay();

      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(propertyAccessStrategyFactory);
      ComparationStrategy <FieldOnlyTestBeanChild> comparationStrategy = (ComparationStrategy <FieldOnlyTestBeanChild> )(comparationStrategyFactory.create(FieldOnlyTestBeanChild.class));
      assertNotNull(comparationStrategy);
    } catch (Exception ex) {
      fail();
    } finally {
      propertyAccessStrategyFactoryControl.verify();
    } 
  }
  
  public void testTestIntefarceChildComparationStrategyCreation() {
    ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(null, null);
    ComparationStrategy <TestInterfaceChild> comparationStrategy = (ComparationStrategy <TestInterfaceChild> )(comparationStrategyFactory.create(TestInterfaceChild.class));
    assertTrue(comparationStrategy instanceof MockComparationStrategy);
  }
  
  public void testTestInterface2AbstractClassChildComparationStrategyCreation() {
    MockControl propertyAccessStrategyFactoryControl = MockControl.createControl(PropertyAccessStrategyFactory.class);
    try {
      PropertyAccessStrategyFactory propertyAccessStrategyFactory = (PropertyAccessStrategyFactory)(propertyAccessStrategyFactoryControl.getMock());
      PropertyAccessStrategy <TestBean, String> valuePropertyAccessStrategy = new DefaultPropertyAccessStrategy <TestBean, String> ((PropertyGettingStrategy <TestBean, String>)(MockControl.createControl(PropertyGettingStrategy.class).getMock()));
      propertyAccessStrategyFactory.create(AbstractClass.class.getDeclaredMethod("getX"), null);
      propertyAccessStrategyFactoryControl.setReturnValue(valuePropertyAccessStrategy);
      propertyAccessStrategyFactoryControl.replay();
      
      ComparationStrategyFactory comparationStrategyFactory = new DefaultComparationStrategyFactory(propertyAccessStrategyFactory, null);
      ComparationStrategy strategy = comparationStrategyFactory.create(TestInterface2AbstractClassChild.class);
      assertEquals(1, getComparationStrategiesEntries((ComparationHandler)(strategy)).size());
      PropertyComparationStrategy propertyStrategy = getComparationStrategiesEntries((ComparationHandler)(strategy)).iterator().next();
      assertTrue(getComparationStrategy(propertyStrategy) instanceof ValueComparationStrategy);
      assertSame(valuePropertyAccessStrategy, getPropertyAccessStrategy(propertyStrategy));
    } catch (Exception ex) {
      fail();
    } finally {
      propertyAccessStrategyFactoryControl.verify();
    }
    
  }
}
