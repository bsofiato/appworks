/*
 * CloningAspectTest.java
 * JUnit based test
 *
 * Created on 21 de Julho de 2005, 22:11
 */

package br.com.appworks.runtime.lang.support.cloning.aop.test;

import junit.framework.*;

/**
 *
 * @author Bubu
 */

import br.com.appworks.runtime.lang.Cloneable;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategyFactory;
import java.lang.reflect.Method;
import org.easymock.MockControl;

public class CloningAspectTest extends TestCase {
  
  @Cloneable
  public static class AdvisedTestBean {
    private String x;
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable
  public static class CloneableAdvisedTestBean implements java.lang.Cloneable {
    private String x;
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  public static class NonAdvisedTestBean implements java.lang.Cloneable {
    private String x;
    public Object clone() throws CloneNotSupportedException {
      throw new UnsupportedOperationException();
    }
  }

  
  public CloningAspectTest(String testName) {
    super(testName);
  }
  protected void setUp() throws Exception {
  }
  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.cloning.aop.CloningAspect");
    klass.getDeclaredMethod("setStaticCloningStrategyFactory", CloningStrategyFactory.class).invoke(null, new Object [] { null });
  }

  public void testNonAdvisedClone() {
    MockControl cloningStrategyFactoryControl = null;
    try {
      cloningStrategyFactoryControl = MockControl.createControl(CloningStrategyFactory.class);
      CloningStrategyFactory cloningStrategyFactory = (CloningStrategyFactory)(cloningStrategyFactoryControl.getMock());
      cloningStrategyFactoryControl.replay();
      Class klass = Class.forName("br.com.appworks.runtime.lang.support.cloning.aop.CloningAspect");
      klass.getDeclaredMethod("setStaticCloningStrategyFactory", CloningStrategyFactory.class).invoke(null, new Object [] { cloningStrategyFactory });
      NonAdvisedTestBean bean = new NonAdvisedTestBean();
      bean.clone();
      fail();
    } catch (UnsupportedOperationException ex) {
    } catch (Exception ex) {
      fail();
    } finally {
      cloningStrategyFactoryControl.verify();
    }
  }
  
  public void testNonConfiguredNonCloneableCloningAspectClone() {
    try {
      AdvisedTestBean bean = new AdvisedTestBean();
      assertTrue(bean instanceof br.com.appworks.runtime.lang.support.cloning.Cloneable);
      ((br.com.appworks.runtime.lang.support.cloning.Cloneable)(bean)).clone();
    } catch (CloneNotSupportedException ex) {
    } catch (Exception ex) {
      fail();
    }
  }

  public void testNonConfiguredCloneableCloningAspectClone() {
    try {
      CloneableAdvisedTestBean bean = new CloneableAdvisedTestBean();
      assertTrue(bean instanceof br.com.appworks.runtime.lang.support.cloning.Cloneable);
      ((br.com.appworks.runtime.lang.support.cloning.Cloneable)(bean)).clone();
    } catch (CloneNotSupportedException ex) {
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testAdvisedClone() {
    MockControl cloningStrategyFactoryControl = null;
    MockControl cloningStrategyControl = null;
    try {
      AdvisedTestBean bean = new AdvisedTestBean();
      cloningStrategyControl = MockControl.createControl(CloningStrategy.class);
      cloningStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      cloningStrategyFactoryControl = MockControl.createControl(CloningStrategyFactory.class);
      CloningStrategyFactory cloningStrategyFactory = (CloningStrategyFactory)(cloningStrategyFactoryControl.getMock());
      CloningStrategy <AdvisedTestBean> cloningStrategy = (CloningStrategy <AdvisedTestBean>)(cloningStrategyControl.getMock());
      cloningStrategyFactory.create(AdvisedTestBean.class);
      cloningStrategyFactoryControl.setReturnValue(cloningStrategy);
      cloningStrategy.clone(bean);
      cloningStrategyControl.setReturnValue(bean);
      cloningStrategyFactoryControl.replay();
      cloningStrategyControl.replay();
      Class klass = Class.forName("br.com.appworks.runtime.lang.support.cloning.aop.CloningAspect");
      klass.getDeclaredMethod("setStaticCloningStrategyFactory", CloningStrategyFactory.class).invoke(null, new Object [] { cloningStrategyFactory });
      assertSame(bean, ((br.com.appworks.runtime.lang.support.cloning.Cloneable)(bean)).clone());
    } catch (UnsupportedOperationException ex) {
      ex.printStackTrace();
      fail();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      cloningStrategyFactoryControl.verify();
      cloningStrategyControl.verify();
    }
  }
}
