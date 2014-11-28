/*
 * DerivedPropertyStringficableFunctionalTest.java
 * JUnit based test
 *
 * Created on 4 de Dezembro de 2005, 16:14
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
public class DerivedPropertyStringficableFunctionalTest extends TestCase {
  @Stringficable
  private static class DerivedPropertyTestBean {
    private String x;
    private String y;
    public DerivedPropertyTestBean(String x, String y) {
      this.x = x;
      this.y = y;
    }
    @Stringficable
    public String getValue() {
      return ((x!= null) && (y != null)) ? x+y : null; 
    }
  }

 @Stringficable
  private static class MethodTestBean {
    private String x;
    private String y;
    public MethodTestBean(String x, String y) {
      this.x = x;
      this.y = y;
    }
    @Stringficable
    public String valueGetter() {
      return ((x!= null) && (y != null)) ? x+y : null; 
    }
  }

  public DerivedPropertyStringficableFunctionalTest(String testName) {
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
  
  public void testGetter() {
    assertEquals("[value : xy]", new DerivedPropertyTestBean("x", "y").toString());
  }
  public void testGetterWithNull() {
    assertEquals("[value : null]", new DerivedPropertyTestBean(null, null).toString());
  }

  public void testMethod() {
    assertEquals("[valueGetter() : xy]", new MethodTestBean("x", "y").toString());
  }
  public void testMethodWithNull() {
    assertEquals("[valueGetter() : null]", new MethodTestBean(null, null).toString());
  }

}
