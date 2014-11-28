/*
 * InheritanceBeanStringficableFunctionalTest.java
 * JUnit based test
 *
 * Created on 5 de Dezembro de 2005, 00:33
 */

package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.Stringficable;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import br.com.appworks.runtime.lang.support.stringfication.CachingStringficationStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.stringfication.DefaultStringficationStrategyFactory;
import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategyFactory;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class InheritanceBeanStringficableFunctionalTest extends TestCase {
  @Stringficable
  private static abstract class AbstractBaseBean {
    @Stringficable
    private String x;
    AbstractBaseBean(String x) {
      this.x = x;
    }
  }
  private static class BaseBean extends AbstractBaseBean {
    public BaseBean() {
      this(null);
    }
    public BaseBean(String x) {
      super(x);
    }
  }

  private static class ChildBean1 extends BaseBean {
    public ChildBean1() {
      this(null);
    }
    public ChildBean1(String x) {
      super(x);
    }
  }
  @Stringficable
  private static class FieldChildBean extends BaseBean {
    @Stringficable
    private String y;
    public FieldChildBean() {
      this(null, null);
    }
    public FieldChildBean(String x, String y) {
      super(x);
      this.y = y;
    }
  }
  
  @Stringficable
  public static interface TestInterface {
    @Stringficable
    String getX();
  }
  public static abstract class AbstractTestBean {
    protected String x;
    public String getX() {
      return x;
    }
  }
  public static class TestInterfaceAbstractTestBeanChild extends AbstractTestBean implements TestInterface {
    public TestInterfaceAbstractTestBeanChild(String x) {
      this.x = x;
    }
  }
  public InheritanceBeanStringficableFunctionalTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
    klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new CachingStringficationStrategyFactoryAdapter(new DefaultStringficationStrategyFactory(null, new DefaultPropertyAccessStrategyFactory(new ReflectionBasedPropertyGettingStrategyFactory(), new ReflectionBasedPropertySettingStrategyFactory()))));
  }

  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
    klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new Object [] {null});
  }
  
  public void testTestInterfaceAbstractTestBeanChildToStringWithNullProperty() {
    assertEquals("[x : null]", new TestInterfaceAbstractTestBeanChild(null).toString());
  }
  public void testTestInterfaceAbstractTestBeanChildToString() {
    assertEquals("[x : X]", new TestInterfaceAbstractTestBeanChild("X").toString());
  }
  
  public void testBaseBeanToStringWithNullProperty() {
    assertEquals("[x : null]", new BaseBean(null).toString());
  }
 
  public void testBaseBeanToString() {
    assertEquals("[x : X]", new BaseBean("X").toString());
  }

  public void testChildBean1ToStringWithNullProperty() {
    assertEquals("[x : null]", new ChildBean1(null).toString());
  }

  public void testChildBean1ToString() {
    assertEquals("[x : X]", new ChildBean1("X").toString());
  }
  

  public void testFieldChildToStringWithNullProperties() {
    assertEquals("[y : null, x : null]", new FieldChildBean(null, null).toString());
  }
  
  public void testFieldChildToString() {
    assertEquals("[y : Y, x : X]", new FieldChildBean("X", "Y").toString());
  }
}
