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

public class CharArrayBeanStringficableFunctionalTest extends TestCase {
  
  @Stringficable
  private static class TestBean {
    @Stringficable
    private char [] array;
    public TestBean(char [] array) {
      this.array = array;
    }
  }
  
  @Stringficable
  private static class MultiDimensionalTestBean {
    @Stringficable
    private char [][] array;
    public MultiDimensionalTestBean(char [][] array) {
      this.array = array;
    }
  }

  @Stringficable
  private static class IdentityTestBean {
    @Stringficable(StringficationPolicy.IDENTITY)
    private char [] array;
    public IdentityTestBean(char [] array) {
      this.array = array;
    }
  }
  
  @Stringficable
  private static class IdentityMultiDimensionalTestBean {
    @Stringficable(StringficationPolicy.IDENTITY)
    private char [][] array;
    public IdentityMultiDimensionalTestBean(char [][] array) {
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

  public CharArrayBeanStringficableFunctionalTest(String testName) {
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
    assertEquals("[array : []]", new TestBean(new char[0]).toString());
  }
  public void testIdentityWithEmptyArray() {
    char [] object = new char[0];
    assertEquals("[array : [C@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityTestBean(object).toString());
  }
  public void testMultidimensionalWithEmptyArray() {
    assertEquals("[array : []]", new MultiDimensionalTestBean(new char[0][0]).toString());
  }
  public void testIdentityMultidimensionalWithEmptyArray() {
    char [][] object = new char[0][0];
    assertEquals("[array : [[C@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityMultiDimensionalTestBean(object).toString());
  }
  public void testArray() {
    assertEquals("[array : [a, b]]", new TestBean(new char[] { 'a', 'b' }).toString());
  }
  public void testIdentityArray() {
    char [] object = new char[] { 'a', 'b' };
    assertEquals("[array : [C@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityTestBean(object).toString());
  }
  public void testMultidimensionalArray() {
    assertEquals("[array : [[a, b], [b, c]]]", new MultiDimensionalTestBean(new char[][] { new char [] { 'a', 'b' }, new char [] { 'b', 'c' }}).toString());
  }
  public void testIdentityMultidimensionalArray() {
    char [][] object = new char[][] { new char [] { 'a', 'b' }, new char [] { 'b', 'c' }};
    assertEquals("[array : [[C@" + Integer.toHexString(System.identityHashCode(object)) + "]", new IdentityMultiDimensionalTestBean(object).toString());
  }
}
