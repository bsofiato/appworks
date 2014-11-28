/*
 * InheritanceBeanCloneableFuncionalTest.java
 * JUnit based test
 *
 * Created on 3 de Agosto de 2005, 23:40
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

/**
 *
 * @author Bubu
 */
public class InheritanceBeanCloneableFuncionalTest extends TestCase {
  
  private static abstract class NonCloneableBaseBean {
    Calendar x;
    NonCloneableBaseBean(Calendar x) {
      this.x = x;
    }
  }

  @Cloneable(CloningPolicy.DEEP)
  private static class NonCloneableChildBean extends AbstractBaseBean {
    NonCloneableChildBean(Calendar x) {
      super(x);
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }

  @Cloneable(CloningPolicy.DEEP)
  private static abstract class AbstractBaseBean {
    Calendar x;
    AbstractBaseBean(Calendar x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }


  private static class ChildBean extends AbstractBaseBean {
    ChildBean(Calendar x) {
      super(x);
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.SHALLOW)
  private static class OverridedCloneablePolicyChildBean extends AbstractBaseBean {
    Calendar y;
    OverridedCloneablePolicyChildBean(Calendar x, Calendar y) {
      super(x);
      this.y = y;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  private static class PropertyChildBean extends AbstractBaseBean {
    Calendar y;
    PropertyChildBean(Calendar x, Calendar y) {
      super(x);
      this.y = y;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  public InheritanceBeanCloneableFuncionalTest(String testName) {
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
  
  public void testChildBeanCloning() {
    try {
      for (ChildBean childBean : new ChildBean [] {new ChildBean(null), new ChildBean(Calendar.getInstance())}) {
        ChildBean clone = clone(childBean);
        assertNotSame(childBean, clone);
        if (childBean.x != null) {
          assertNotSame(childBean.x, clone.x);
        }
        assertEquals(childBean.x, clone.x);
      }
    } catch (CloneNotSupportedException ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testPropertyChildBeanCloning() {
    try {
      for (PropertyChildBean childBean : new PropertyChildBean [] {new PropertyChildBean(null, null), 
                                                                   new PropertyChildBean(Calendar.getInstance(), null), 
                                                                   new PropertyChildBean(null, Calendar.getInstance()), 
                                                                   new PropertyChildBean(Calendar.getInstance(), Calendar.getInstance())}) {
        PropertyChildBean clone = clone(childBean);
        assertNotSame(childBean, clone);
        if (childBean.x != null) {
          assertNotSame(childBean.x, clone.x);
        }
        assertEquals(childBean.x, clone.x);
        if (childBean.y != null) {
          assertNotSame(childBean.y, clone.y);
        }
        assertEquals(childBean.y, clone.y);
      }
    } catch (CloneNotSupportedException ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testOverridedCloneablePolicyChildBeanCloning() {
    try {
      for (OverridedCloneablePolicyChildBean childBean : new OverridedCloneablePolicyChildBean [] {new OverridedCloneablePolicyChildBean(null, null),
                                                                                                   new OverridedCloneablePolicyChildBean(Calendar.getInstance(), null),
                                                                                                   new OverridedCloneablePolicyChildBean(null, Calendar.getInstance()),
                                                                                                   new OverridedCloneablePolicyChildBean(Calendar.getInstance(), Calendar.getInstance())}) {
        OverridedCloneablePolicyChildBean clone = clone(childBean);
        assertNotSame(childBean, clone);
        if (childBean.x != null) {
          assertNotSame(childBean.x, clone.x);
        }
        assertEquals(childBean.x, clone.x);
        assertSame(childBean.y, clone.y);
      }
    } catch (CloneNotSupportedException ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testNonCloneableChildBeanCloning() {
    try {
      for (NonCloneableChildBean childBean : new NonCloneableChildBean [] {new NonCloneableChildBean(null), new NonCloneableChildBean(Calendar.getInstance())}) {
        NonCloneableChildBean clone = clone(childBean);
        assertNotSame(childBean, clone);
        if (childBean.x != null) {
          assertNotSame(childBean.x, clone.x);
        }
        assertEquals(childBean.x, clone.x);
      }
    } catch (CloneNotSupportedException ex) {
      ex.printStackTrace();
      fail();
    }
  }

}
