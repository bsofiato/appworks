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
import static junit.framework.TestCase.assertEquals;

public class ShortArrayBeanStringficableFunctionalTest extends TestCase {
  
  @Stringficable
  private static class TestBean {
    @Stringficable
    private short [] array;
    public TestBean(short [] array) {
      this.array = array;
    }
  }
  
  @Stringficable
  private static class MultiDimensionalTestBean {
    @Stringficable
    private short [][] array;
    public MultiDimensionalTestBean(short [][] array) {
      this.array = array;
    }
  }

  @Stringficable
  private static class IdentityTestBean {
    @Stringficable(StringficationPolicy.IDENTITY)
    private short [] array;
    public IdentityTestBean(short [] array) {
      this.array = array;
    }
  }
  
  @Stringficable
  private static class IdentityMultiDimensionalTestBean {
    @Stringficable(StringficationPolicy.IDENTITY)
    private short [][] array;
    public IdentityMultiDimensionalTestBean(short [][] array) {
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

  public ShortArrayBeanStringficableFunctionalTest(String testName) {
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
    assertEquals("[array : []]", new TestBean(new short[0]).toString());
  }
  public void testIdentityWithEmptyArray() {
    short [] object = new short[0];
    assertEquals("[array : [S@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityTestBean(object).toString());
  }
  public void testMultidimensionalWithEmptyArray() {
    assertEquals("[array : []]", new MultiDimensionalTestBean(new short[0][0]).toString());
  }
  public void testIdentityMultidimensionalWithEmptyArray() {
    short [][] object = new short[0][0];
    assertEquals("[array : [[S@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityMultiDimensionalTestBean(object).toString());
  }
  public void testArray() {
    assertEquals("[array : [1, 2]]", new TestBean(new short[] { 1, 2 }).toString());
  }
  public void testIdentityArray() {
    short [] object = new short[] { 1, 2 };
    assertEquals("[array : [S@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityTestBean(object).toString());
  }
  public void testMultidimensionalArray() {
    assertEquals("[array : [[1, 2], [2, 3]]]", new MultiDimensionalTestBean(new short[][] { new short [] { 1, 2 }, new short [] { 2, 3}}).toString());
  }
  public void testIdentityMultidimensionalArray() {
    short [][] object = new short[][] { new short [] { 1, 2 }, new short [] { 2, 3 }};
    assertEquals("[array : [[S@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityMultiDimensionalTestBean(object).toString());
  }
}
