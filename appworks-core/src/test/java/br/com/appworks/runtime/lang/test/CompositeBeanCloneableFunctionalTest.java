/*
 * CompositeBeanCloneableFunctionalTest.java
 * JUnit based test
 *
 * Created on 4 de Agosto de 2005, 23:18
 */

package br.com.appworks.runtime.lang.test;

import junit.framework.*;
import br.com.appworks.runtime.lang.Cloneable;
import br.com.appworks.runtime.lang.CloningPolicy;
import br.com.appworks.runtime.lang.support.cloning.CachingCloningStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategyFactory;
import br.com.appworks.runtime.lang.support.cloning.DefaultCloningStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import java.util.Calendar;


/**
 *
 * @author Bubu
 */
public class CompositeBeanCloneableFunctionalTest extends TestCase {
  @Cloneable(CloningPolicy.DEEP)
  private static class DeepCloneableBean {
    private Calendar x;
    private Calendar y;
    public DeepCloneableBean(Calendar x, Calendar y) {
      this.x = x;
      this.y = y;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.SHALLOW)
  private static class ShallowCloneableBean {
    private Calendar x;
    private Calendar y;
    public ShallowCloneableBean(Calendar x, Calendar y) {
      this.x = x;
      this.y = y;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  public CompositeBeanCloneableFunctionalTest(String testName) {
    super(testName);
  }
  
  private <Type> Type clone(Type object) throws CloneNotSupportedException {
    Type clone = null;
    if (object != null) {
      clone = (Type)((br.com.appworks.runtime.lang.support.cloning.Cloneable)(object)).clone();
    }
    return clone;
  }
  
  protected void setUp() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.cloning.aop.CloningAspect");
    klass.getDeclaredMethod("setStaticCloningStrategyFactory", CloningStrategyFactory.class).invoke(null, new CachingCloningStrategyFactoryAdapter(new DefaultCloningStrategyFactory(new DefaultPropertyAccessStrategyFactory(new ReflectionBasedPropertyGettingStrategyFactory(), new ReflectionBasedPropertySettingStrategyFactory()))));
  }

  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.cloning.aop.CloningAspect");
    klass.getDeclaredMethod("setStaticCloningStrategyFactory", CloningStrategyFactory.class).invoke(null, new Object [] { null });
  }
  
  public void testDeepCloneableBeanCloning() {
    try {
      for (DeepCloneableBean bean : new DeepCloneableBean [] {
        new DeepCloneableBean(null, null),
        new DeepCloneableBean(Calendar.getInstance(), null),
        new DeepCloneableBean(null, Calendar.getInstance()),
        new DeepCloneableBean(Calendar.getInstance(), Calendar.getInstance())}) {
        DeepCloneableBean clone = clone(bean);
        assertNotSame(bean, clone);
        if (bean.x != null) {
          assertEquals(bean.x, clone.x);
          assertNotSame(bean.x, clone.x);
        }
        if (bean.y != null) {
          assertEquals(bean.y, clone.y);
          assertNotSame(bean.y, clone.y);
        }
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  } 
  
  public void testShallowCloneableBeanCloning() {
    try {
      for (ShallowCloneableBean bean : new ShallowCloneableBean [] {
        new ShallowCloneableBean(null, null),
        new ShallowCloneableBean(Calendar.getInstance(), null),
        new ShallowCloneableBean(null, Calendar.getInstance()),
        new ShallowCloneableBean(Calendar.getInstance(), Calendar.getInstance())}) {
        ShallowCloneableBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertSame(bean.x, clone.x);
        assertSame(bean.y, clone.y);
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
}
