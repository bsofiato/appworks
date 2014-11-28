/*
 * ArrayBeanCloneableFunctionalTest.java
 * JUnit based test
 *
 * Created on 11 de Agosto de 2005, 00:28
 */

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
import java.util.Calendar;

public class ArrayBeanCloneableFunctionalTest extends TestCase {
  
  @Cloneable(CloningPolicy.SHALLOW)
  public static class ShallowArrayTestBean {
    private Object [] x;
    public ShallowArrayTestBean(Object [] x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.DEEP)
  public static class StaticDeepArrayTestBean {
    private static Object [] x;
    public StaticDeepArrayTestBean(Object [] x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.DEEP)
  public static class DeepArrayTestBean {
    private Object [] x;
    public DeepArrayTestBean(Object [] x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  public ArrayBeanCloneableFunctionalTest(String testName) {
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
      for (Object [] array : new Object [][] {
          new Object[0],
          new Object[] { null },
          new Object[] { "X", "Y"},
          new Object[] { Calendar.getInstance(), Calendar.getInstance() },
          new Object[] { null, "X", Calendar.getInstance() } }) {
        ShallowArrayTestBean bean = new ShallowArrayTestBean(array);
        ShallowArrayTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertSame(bean.x, clone.x);
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  
  public void testStaticDeepArrayTestBeanNullArray() {
    try {
      StaticDeepArrayTestBean bean = new StaticDeepArrayTestBean(null);
      StaticDeepArrayTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testStaticDeepArrayTestBeanNotNullArray() {
    try {
      for (Object [] array : new Object [][] {
          new Object[0],
          new Object[] { null },
          new Object[] { "X", "Y"},
          new Object[] { Calendar.getInstance(), Calendar.getInstance() },
          new Object[] { null, "X", Calendar.getInstance() } }) {
        StaticDeepArrayTestBean bean = new StaticDeepArrayTestBean(array);
        StaticDeepArrayTestBean clone = clone(bean);
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
      for (Object [] array : new Object [][] {
          new Object[0],
          new Object[] { null },
          new Object[] { "X", "Y"},
          new Object[] { Calendar.getInstance(), Calendar.getInstance() },
          new Object[] { null, "X", Calendar.getInstance() } }) {
        DeepArrayTestBean bean = new DeepArrayTestBean(array);
        DeepArrayTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertNotSame(bean.x, clone.x);
        assertEquals(bean.x.length, clone.x.length);
        for (int i=0; i<bean.x.length; i++) {
          if (bean.x[i] instanceof Calendar) {
            assertNotSame(bean.x[i], clone.x[i]);
            assertEquals(bean.x[i], clone.x[i]);
          } else {
            assertSame(bean.x[i], clone.x[i]);
          }
        }
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

}
