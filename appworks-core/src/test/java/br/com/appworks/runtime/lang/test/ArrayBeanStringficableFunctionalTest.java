/*
 * ArrayBeanStringficableFunctionalTest.java
 * JUnit based test
 *
 * Created on 3 de Dezembro de 2005, 21:45
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
public class ArrayBeanStringficableFunctionalTest extends TestCase {
  
  @Stringficable
  private static class TestBean {
    @Stringficable
    private Object [] array;
    public TestBean(Object [] array) {
      this.array = array;
    }
  }
  
  @Stringficable
  private static class MultiDimensionalTestBean {
    @Stringficable
    private Object [][] array;
    public MultiDimensionalTestBean(Object [][] array) {
      this.array = array;
    }
  }

  @Stringficable
  private static class IdentityTestBean {
    @Stringficable(StringficationPolicy.IDENTITY)
    private Object [] array;
    public IdentityTestBean(Object [] array) {
      this.array = array;
    }
  }
  
  @Stringficable
  private static class IdentityMultiDimensionalTestBean {
    @Stringficable(StringficationPolicy.IDENTITY)
    private Object [][] array;
    public IdentityMultiDimensionalTestBean(Object [][] array) {
      this.array = array;
    }
  }

  protected void setUp() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
    klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new CachingStringficationStrategyFactoryAdapter(new DefaultStringficationStrategyFactory(null, new DefaultPropertyAccessStrategyFactory(new ReflectionBasedPropertyGettingStrategyFactory(), new ReflectionBasedPropertySettingStrategyFactory()))));
  }

  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
    klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new Object [] {null});
  }

  public ArrayBeanStringficableFunctionalTest(String testName) {
    super(testName);
  }
  
  public void testWithNullArray() {
    assertEquals("[array : null]", new TestBean(null).toString());
  }
  public void testIdentityWithNullArray() {
    assertEquals("[array : null]", new IdentityTestBean(null).toString());
  }
  public void testMultidimensionalWithNullArray() {
    assertEquals("[array : null]", new MultiDimensionalTestBean(null).toString());
  }
  public void testIdentityMultidimensionalWithNullArray() {
    assertEquals("[array : null]", new IdentityMultiDimensionalTestBean(null).toString());
  }

  public void testWithEmptyArray() {
    assertEquals("[array : []]", new TestBean(new Object[0]).toString());
  }
  public void testIdentityWithEmptyArray() {
    Object [] object = new Object[0];
    assertEquals("[array : [Ljava.lang.Object;@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityTestBean(object).toString());
  }
  public void testMultidimensionalWithEmptyArray() {
    assertEquals("[array : []]", new MultiDimensionalTestBean(new Object[0][0]).toString());
  }
  public void testIdentityMultidimensionalWithEmptyArray() {
    Object [][] object = new Object[0][0];
    assertEquals("[array : [[Ljava.lang.Object;@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityMultiDimensionalTestBean(object).toString());
  }
  public void testArray() {
    assertEquals("[array : [X, Y]]", new TestBean(new Object[] { "X", "Y" }).toString());
  }
  public void testIdentityArray() {
    Object [] object = new Object[] { "X", "Y" };
    assertEquals("[array : [Ljava.lang.Object;@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityTestBean(object).toString());
  }
  public void testMultidimensionalArray() {
    assertEquals("[array : [[X, Y], [Y, Z]]]", new MultiDimensionalTestBean(new Object[][] { new Object [] { "X", "Y" }, new Object [] { "Y", "Z"}}).toString());
  }
  public void testIdentityMultidimensionalArray() {
    Object [][] object = new Object[][] { new Object [] { "X", "Y" }, new Object [] { "Y", "Z" }};
    assertEquals("[array : [[Ljava.lang.Object;@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityMultiDimensionalTestBean(object).toString());
  }
}
