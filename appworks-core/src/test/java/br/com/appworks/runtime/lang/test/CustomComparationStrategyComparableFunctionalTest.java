/*
 * CustomStrategyComparableFunctionalTest.java
 * JUnit based test
 *
 * Created on 26 de Julho de 2005, 00:23
 */

package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.support.comparation.AbstractComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.DefaultComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import junit.framework.*;
import br.com.appworks.runtime.lang.Comparable;
import br.com.appworks.runtime.lang.support.comparation.CachingComparationStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import java.io.Serializable;
import org.aspectj.lang.Aspects;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class CustomComparationStrategyComparableFunctionalTest extends TestCase {
  public static class CustomPropertyComparationStrategy extends AbstractComparationStrategy {
    private static MockControl control = MockControl.createControl(ComparationStrategy.class);
    protected int doCompare(Object op1, Object op2) {
      return ((ComparationStrategy)(getControl().getMock())).compare(op1, op2);
    }
    public static MockControl getControl() {
      return control;
    }
    public boolean equals(Object op1, Object op2) {
      return ((ComparationStrategy)(getControl().getMock())).equals(op1, op2);
    }
    public int hashCode(Object object) {
      return ((ComparationStrategy)(getControl().getMock())).hashCode(object);
    }
  }

  @Comparable(strategy=CustomPropertyComparationStrategy.class)
  private static class CustomComparationStrategyTestBean implements Serializable {
    public CustomComparationStrategyTestBean() {
    }
  }
  
  @Comparable(strategy=CustomPropertyComparationStrategy.class)
  private static interface CustomComparationStrategyTestInterface extends Serializable {
  }

  private static class CustomComparationStrategyChildTestInterface implements CustomComparationStrategyTestInterface {
  }


  private static class CustomComparationStrategyChildTestBean extends CustomComparationStrategyTestBean {
    public CustomComparationStrategyChildTestBean() {
    }
  }

  @Comparable
  private static class PropertyCustomComparationStrategyTestBean implements Serializable {
    @Comparable(strategy=CustomPropertyComparationStrategy.class)
    private String x;
    public PropertyCustomComparationStrategyTestBean(String x) {
      this.x = x;
    }
  }
  
  public CustomComparationStrategyComparableFunctionalTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.comparation.aop.ComparationAspect");
    klass.getDeclaredMethod("setComparationStrategyFactory", ComparationStrategyFactory.class).invoke(Aspects.aspectOf(klass), new CachingComparationStrategyFactoryAdapter(new DefaultComparationStrategyFactory(new DefaultPropertyAccessStrategyFactory(new ReflectionBasedPropertyGettingStrategyFactory(), new ReflectionBasedPropertySettingStrategyFactory()))));
  }

  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.comparation.aop.ComparationAspect");
    klass.getDeclaredMethod("setComparationStrategyFactory", ComparationStrategyFactory.class).invoke(Aspects.aspectOf(klass), new Object [] { null });
    CustomPropertyComparationStrategy.getControl().reset();
  }  
  
  public void testPropertyCustomComparationStrategyTestBeanHashCode() {
    CustomPropertyComparationStrategy x = new CustomPropertyComparationStrategy();
    x.hashCode("X");
    x.getControl().setReturnValue(0);
    x.getControl().replay();
    assertEquals(0, new PropertyCustomComparationStrategyTestBean("X").hashCode());
    x.getControl().verify();
  }

  public void testPropertyCustomComparationStrategyTestBeanEquals() {
    CustomPropertyComparationStrategy x = new CustomPropertyComparationStrategy();
    x.equals("X", "X");
    x.getControl().setReturnValue(true);
    x.getControl().replay();
    assertEquals(true, new PropertyCustomComparationStrategyTestBean("X").equals(new PropertyCustomComparationStrategyTestBean("X")));
    x.getControl().verify();
  }
  
  public void testPropertyCustomComparationStrategyTestBeanCompareTo() {
    CustomPropertyComparationStrategy x = new CustomPropertyComparationStrategy();
    x.equals("X", "X");
    x.getControl().setReturnValue(true);
    x.getControl().replay();
    assertEquals(0, ((java.lang.Comparable)(new PropertyCustomComparationStrategyTestBean("X"))).compareTo(new PropertyCustomComparationStrategyTestBean("X")));
    x.getControl().verify();
  }
  
  
  public void testCustomComparationStrategyTestBeanHashCode() {
    CustomComparationStrategyTestBean bean = new CustomComparationStrategyTestBean();
    CustomPropertyComparationStrategy x = new CustomPropertyComparationStrategy();
    x.getControl().setDefaultMatcher(MockControl.ALWAYS_MATCHER);
    x.hashCode(bean);
    x.getControl().setReturnValue(0);
    x.getControl().replay();
    assertEquals(0, bean.hashCode());
    x.getControl().verify();
  }
  
  public void testCustomComparationStrategyTestBeanEquals() {
    CustomComparationStrategyTestBean bean = new CustomComparationStrategyTestBean();
    CustomPropertyComparationStrategy x = new CustomPropertyComparationStrategy();
    x.getControl().setDefaultMatcher(MockControl.ALWAYS_MATCHER);
    x.equals(bean, bean);
    x.getControl().setReturnValue(true);
    x.getControl().replay();
    assertTrue(bean.equals(bean));
    x.getControl().verify();
  }
  
  public void testCustomComparationStrategyTestBeanCompareTo() {
    CustomComparationStrategyTestBean bean = new CustomComparationStrategyTestBean();
    CustomPropertyComparationStrategy x = new CustomPropertyComparationStrategy();
    x.getControl().setDefaultMatcher(MockControl.ALWAYS_MATCHER);
    x.equals(bean, bean);
    x.getControl().setReturnValue(true);
    x.getControl().replay();
    assertEquals(0, ((java.lang.Comparable)(bean)).compareTo(bean));
    x.getControl().verify();
  }

  
  public void testCustomComparationStrategyChildTestBeanHashCode() {
    CustomComparationStrategyChildTestBean bean = new CustomComparationStrategyChildTestBean();
    CustomPropertyComparationStrategy x = new CustomPropertyComparationStrategy();
    x.getControl().setDefaultMatcher(MockControl.ALWAYS_MATCHER);
    x.hashCode(bean);
    x.getControl().setReturnValue(0);
    x.getControl().replay();
    assertEquals(0, bean.hashCode());
    x.getControl().verify();
  }
  
  public void testCustomComparationStrategyChildTestBeanEquals() {
    CustomComparationStrategyChildTestBean bean = new CustomComparationStrategyChildTestBean();
    CustomPropertyComparationStrategy x = new CustomPropertyComparationStrategy();
    x.getControl().setDefaultMatcher(MockControl.ALWAYS_MATCHER);
    x.equals(bean, bean);
    x.getControl().setReturnValue(true);
    x.getControl().replay();
    assertTrue(bean.equals(bean));
    x.getControl().verify();
  }
  
  public void testCustomComparationStrategyChildTestBeanCompareTo() {
    CustomComparationStrategyChildTestBean bean = new CustomComparationStrategyChildTestBean();
    CustomPropertyComparationStrategy x = new CustomPropertyComparationStrategy();
    x.getControl().setDefaultMatcher(MockControl.ALWAYS_MATCHER);
    x.equals(bean, bean);
    x.getControl().setReturnValue(true);
    x.getControl().replay();
    assertEquals(0, ((java.lang.Comparable)(bean)).compareTo(bean));
    x.getControl().verify();
  }
  
  public void testCustomComparationStrategyChildTestInterfaceEquals() {
    CustomComparationStrategyChildTestInterface bean = new CustomComparationStrategyChildTestInterface();
    CustomPropertyComparationStrategy x = new CustomPropertyComparationStrategy();
    x.getControl().setDefaultMatcher(MockControl.ALWAYS_MATCHER);
    x.equals(bean, bean);
    x.getControl().setReturnValue(true);
    x.getControl().replay();
    assertTrue(bean.equals(bean));
    x.getControl().verify();
  }

  public void testCustomComparationStrategyChildTestInterfaceHashCode() {
    CustomComparationStrategyChildTestInterface bean = new CustomComparationStrategyChildTestInterface();
    CustomPropertyComparationStrategy x = new CustomPropertyComparationStrategy();
    x.getControl().setDefaultMatcher(MockControl.ALWAYS_MATCHER);
    x.hashCode(bean);
    x.getControl().setReturnValue(1);
    x.getControl().replay();
    assertEquals(1, bean.hashCode());
    x.getControl().verify();
  }
}
