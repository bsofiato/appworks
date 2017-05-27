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

public class BooleanArrayBeanStringficableFunctionalTest extends TestCase {
  
  @Stringficable
  private static class TestBean {
    @Stringficable
    private boolean [] array;
    public TestBean(boolean [] array) {
      this.array = array;
    }
  }
  
  @Stringficable
  private static class MultiDimensionalTestBean {
    @Stringficable
    private boolean [][] array;
    public MultiDimensionalTestBean(boolean [][] array) {
      this.array = array;
    }
  }

  @Stringficable
  private static class IdentityTestBean {
    @Stringficable(StringficationPolicy.IDENTITY)
    private boolean [] array;
    public IdentityTestBean(boolean [] array) {
      this.array = array;
    }
  }
  
  @Stringficable
  private static class IdentityMultiDimensionalTestBean {
    @Stringficable(StringficationPolicy.IDENTITY)
    private boolean [][] array;
    public IdentityMultiDimensionalTestBean(boolean [][] array) {
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

  public BooleanArrayBeanStringficableFunctionalTest(String testName) {
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
    assertEquals("[array : []]", new TestBean(new boolean[0]).toString());
  }
  public void testIdentityWithEmptyArray() {
    boolean [] object = new boolean[0];
    assertEquals("[array : [Z@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityTestBean(object).toString());
  }
  public void testMultidimensionalWithEmptyArray() {
    assertEquals("[array : []]", new MultiDimensionalTestBean(new boolean[0][0]).toString());
  }
  public void testIdentityMultidimensionalWithEmptyArray() {
    boolean [][] object = new boolean[0][0];
    assertEquals("[array : [[Z@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityMultiDimensionalTestBean(object).toString());
  }
  public void testArray() {
    assertEquals("[array : [true, false]]", new TestBean(new boolean[] { true, false }).toString());
  }
  public void testIdentityArray() {
    boolean [] object = new boolean[] { true, false };
    assertEquals("[array : [Z@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityTestBean(object).toString());
  }
  public void testMultidimensionalArray() {
    assertEquals("[array : [[true, false], [false, true]]]", new MultiDimensionalTestBean(new boolean[][] { new boolean [] { true, false }, new boolean [] { false, true} }).toString());
  }
  public void testIdentityMultidimensionalArray() {
    boolean [][] object = new boolean[][] { new boolean [] { true, false }, new boolean [] { true, false }};
    assertEquals("[array : [[Z@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityMultiDimensionalTestBean(object).toString());
  }
}
