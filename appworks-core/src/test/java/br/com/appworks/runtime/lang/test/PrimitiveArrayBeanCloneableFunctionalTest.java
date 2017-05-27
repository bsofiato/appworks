package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.support.cloning.DefaultCloningStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import junit.framework.*;
import br.com.appworks.runtime.lang.Cloneable;
import br.com.appworks.runtime.lang.CloningPolicy;
import br.com.appworks.runtime.lang.support.cloning.CachingCloningStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import java.util.Arrays;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;
import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.fail;

public class PrimitiveArrayBeanCloneableFunctionalTest extends TestCase {
  
  @Cloneable(CloningPolicy.SHALLOW)
  public static class ShallowArrayTestBean {
    private int [] x;
    public ShallowArrayTestBean(int [] x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  @Cloneable(CloningPolicy.DEEP)
  public static class DeepArrayTestBean {
    private int [] x;
    public DeepArrayTestBean(int [] x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  public PrimitiveArrayBeanCloneableFunctionalTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.cloning.aop.CloningAspect");
    klass.getDeclaredMethod("setStaticCloningStrategyFactory", CloningStrategyFactory.class).invoke(null, new CachingCloningStrategyFactoryAdapter(new DefaultCloningStrategyFactory(new DefaultPropertyAccessStrategyFactory(new ReflectionBasedPropertyGettingStrategyFactory(), new ReflectionBasedPropertySettingStrategyFactory()))));
  }

  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.cloning.aop.CloningAspect");
    klass.getDeclaredMethod("setStaticCloningStrategyFactory", CloningStrategyFactory.class).invoke(null, new Object [] { null });
  }
  
   private <Type> Type clone(Type object) throws CloneNotSupportedException {
    Type clone = null;
    if (object != null) {
      clone = (Type)((br.com.appworks.runtime.lang.support.cloning.Cloneable)(object)).clone();
    }
    return clone;
  }
  
  public void testShallowArrayTestBeanNullArray() {
    try {
      ShallowArrayTestBean bean = new ShallowArrayTestBean(null);
      ShallowArrayTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testShallowArrayTestBeanNotNullArray() {
    try {
      for (int [] array : new int [][] {
          new int[0],
          new int[] { 1 },
          new int[] { 1, 2 }}) {
        ShallowArrayTestBean bean = new ShallowArrayTestBean(array);
        ShallowArrayTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertSame(bean.x, clone.x);
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  
  public void testDeepArrayTestBeanNullArray() {
    try {
      DeepArrayTestBean bean = new DeepArrayTestBean(null);
      DeepArrayTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testDeepArrayTestBeanNotNullArray() {
    try {
      for (int [] array : new int [][] {
          new int[0],
          new int[] { 1 },
          new int[] { 1, 2 }}) {
        DeepArrayTestBean bean = new DeepArrayTestBean(array);
        DeepArrayTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertNotSame(bean.x, clone.x);
        assertTrue(Arrays.equals(bean.x, clone.x));
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
}
