/*
 * CompositeBeanStringficableFunctionalTest.java
 * JUnit based test
 *
 * Created on 4 de Dezembro de 2005, 16:37
 */

package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.Stringficable;
import br.com.appworks.runtime.lang.StringficationPolicy;
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
public class CompositeBeanStringficableFunctionalTest extends TestCase {
  @Stringficable
  private static class CompositeTestBean {
    @Stringficable
    private String x;
    @Stringficable
    private String y;
    public CompositeTestBean(String x, String y) {
      this.x = x;
      this.y = y;
    }
  }

  @Stringficable
  private static class IdentityCompositeTestBean {
    @Stringficable(StringficationPolicy.IDENTITY)
    private String x;
    @Stringficable(StringficationPolicy.IDENTITY)
    private String y;
    public IdentityCompositeTestBean(String x, String y) {
      this.x = x;
      this.y = y;
    }
  }

  public CompositeBeanStringficableFunctionalTest(String testName) {
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
  
  public void testWithNullProperty() {
    assertEquals("[x : null, y : null]", new CompositeTestBean(null, null).toString());
  }
  public void testIdentityWithNullProperty() {
    assertEquals("[x : null, y : null]", new IdentityCompositeTestBean(null, null).toString());
  }
  public void test() {
    assertEquals("[x : X, y : Y]", new CompositeTestBean("X", "Y").toString());
  }
  public void testIdentity() {
    assertEquals("[x : java.lang.String@" + Integer.toHexString(System.identityHashCode("X")) + ", y : java.lang.String@" + Integer.toHexString(System.identityHashCode("Y")) + "]", new IdentityCompositeTestBean("X", "Y").toString());
  }
}
