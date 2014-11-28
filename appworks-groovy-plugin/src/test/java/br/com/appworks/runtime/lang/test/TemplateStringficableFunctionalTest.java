/*
 * TemplateStringficableFunctionalTest.java
 * JUnit based test
 *
 * Created on 1 de Dezembro de 2005, 00:43
 */

package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.Stringficable;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import br.com.appworks.runtime.lang.support.stringfication.CachingStringficationStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.stringfication.DefaultStringficationStrategyFactory;
import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategyFactory;
import br.com.appworks.runtime.lang.support.template.groovy.GroovyTemplateFactory;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class TemplateStringficableFunctionalTest extends TestCase {
  @Stringficable(template="Interface ${this.getX()}")
  private static interface TestInterface {
    String getX();
  }

  @Stringficable(template="TestInterfaceChild2 ${this.getX()}")
  private static class TestInterfaceChild2 implements TestInterface {
    private String x;
    private TestInterfaceChild2(String x) {
      this.x = x;
    }
    public String getX() {
      return x;
    }
  }

  private static class TestInterfaceChild implements TestInterface {
    private String x;
    private TestInterfaceChild(String x) {
      this.x = x;
    }
    public String getX() {
      return x;
    }
  }
  @Stringficable(template="TestBaseClass ${this.getX()} ${this.y}")
  private static abstract class TestBaseClass {
    protected String y;
    public abstract String getX();
  }
  
  private static class TestBaseClassChild extends TestBaseClass {
    private String x;
    public TestBaseClassChild(String x, String y) {
      this.x = x;
      this.y = y;
    }
    public String getX() {
      return x;
    }
  }
  
  @Stringficable(template="Test ${this.x}, ${y}, ${getY()}, ${xy()}")
  private static class TestBean {
    private String x;
    private String y;
    public TestBean(String x, String y) {
      this.x = x;
      this.y = y;
    }
    public String getY() {
      return y;
    }
    public String xy() {
      StringBuffer sb = new StringBuffer();
      if (x != null) {
        sb.append(x);
      } 
      if (getY() != null) {
        sb.append(getY());
      }
      return (sb.length() == 0) ? null : sb.toString();
    }
  }
  
  
  public TemplateStringficableFunctionalTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
    klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new CachingStringficationStrategyFactoryAdapter(new DefaultStringficationStrategyFactory(new GroovyTemplateFactory(), new DefaultPropertyAccessStrategyFactory(new ReflectionBasedPropertyGettingStrategyFactory(), new ReflectionBasedPropertySettingStrategyFactory()))));
  }

  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
    klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new Object [] {null});
  }
  
  public void testTestInterfaceChildWithNullPropertyToString() {
    assertEquals("Interface null", new TestInterfaceChild(null).toString());
  }

  public void testTestInterfaceChildToString() {
    assertEquals("Interface X", new TestInterfaceChild("X").toString());
  }

  public void testTestInterfaceChild2WithNullPropertyToString() {
    assertEquals("TestInterfaceChild2 null", new TestInterfaceChild2(null).toString());
  }

  public void testTestInterfaceChild2ToString() {
    assertEquals("TestInterfaceChild2 X", new TestInterfaceChild2("X").toString());
  }

  public void testTestBaseClassChildWithNullPropertiesToString() {
    assertEquals("TestBaseClass null null", new TestBaseClassChild(null, null).toString());
  }
  
  public void testTestBaseClassChildToString() {
    assertEquals("TestBaseClass X Y", new TestBaseClassChild("X", "Y").toString());
  }
  
  public void testTestBeanWithNullPropertiesToString() {
    assertEquals("Test null, null, null, null", new TestBean(null, null).toString());
  }
  
   public void testTestBeanWithXPropertyNotNullToString() {
    assertEquals("Test X, null, null, X", new TestBean("X", null).toString());
  }
   
  public void testTestBeanWithYPropertyNotNullToString() {
    assertEquals("Test null, Y, Y, Y", new TestBean(null, "Y").toString());
  }
  public void testTestBeanToString() {
    assertEquals("Test X, Y, Y, XY", new TestBean("X", "Y").toString());
  }

}
