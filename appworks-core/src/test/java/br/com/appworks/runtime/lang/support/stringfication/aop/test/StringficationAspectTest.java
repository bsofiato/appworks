/*
 * StringficationAspectTest.java
 * JUnit based test
 *
 * Created on 25 de Novembro de 2005, 00:53
 */



package br.com.appworks.runtime.lang.support.stringfication.aop.test;

import br.com.appworks.runtime.lang.Stringficable;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategyFactory;
import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategy;
import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategyFactory;
import junit.framework.*;
import org.easymock.Capture;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.*;
import org.easymock.MockControl;

public class StringficationAspectTest extends TestCase {
  
  @Stringficable
  public class AdvisedTestBean {
    private String x;
  }
  
  public static class NonAdvisedTestBean {
    private String x;
    public String toString() {
      return "TESTE";
    }
  }

  
  public StringficationAspectTest(String testName) {
    super(testName);
  }
  protected void setUp() throws Exception {
  }
  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
    klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new Object [] { null });
  }

  public void testNonAdvisedToString() {
    MockControl stringficationStrategyFactoryControl = null;
    try {
      stringficationStrategyFactoryControl = MockControl.createControl(StringficationStrategyFactory.class);
      StringficationStrategyFactory stringficationStrategyFactory = (StringficationStrategyFactory)(stringficationStrategyFactoryControl.getMock());
      stringficationStrategyFactoryControl.replay();
      Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
      klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new Object [] { stringficationStrategyFactory });
      NonAdvisedTestBean bean = new NonAdvisedTestBean();
      assertEquals("TESTE", bean.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      stringficationStrategyFactoryControl.verify();
    }
  }
  
  public void testNonConfiguredStringficationAspectToString() {
    try {
      AdvisedTestBean bean = new AdvisedTestBean();
      bean.toString();
      fail();
    } catch (IllegalStateException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testAdvisedToString() {
    MockControl stringficationStrategyFactoryControl = null;
    MockControl stringficationStrategyControl = null;
    AdvisedTestBean bean = new AdvisedTestBean();
    try {
      stringficationStrategyFactoryControl = MockControl.createControl(StringficationStrategyFactory.class);
      stringficationStrategyControl = MockControl.createControl(StringficationStrategy.class);
      StringficationStrategy <AdvisedTestBean> stringficationStrategy = (StringficationStrategy <AdvisedTestBean>)(stringficationStrategyControl.getMock());
      StringficationStrategyFactory stringficationStrategyFactory = (StringficationStrategyFactory)(stringficationStrategyFactoryControl.getMock());
      
      stringficationStrategyFactory.create(AdvisedTestBean.class);
      stringficationStrategyFactoryControl.setReturnValue(stringficationStrategy);

      Capture <StringBuilder> stringBuilderCapture = new Capture <StringBuilder> () {
        @Override
        public void setValue(StringBuilder value) {
          value.append("TESTE");
          super.setValue(value);
        }
      };
      stringficationStrategy.toString(EasyMock.eq(bean), EasyMock.capture(stringBuilderCapture));
      
      stringficationStrategyFactoryControl.replay();
      stringficationStrategyControl.replay();

      Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
      klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new Object [] { stringficationStrategyFactory });
      
      assertEquals("TESTE", bean.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      stringficationStrategyFactoryControl.verify();
      stringficationStrategyControl.verify();
    }
  }
  
  public void testAdvisedToStringWithStringficationStrategyExecutionError() {
    MockControl stringficationStrategyFactoryControl = null;
    MockControl stringficationStrategyControl = null;
    AdvisedTestBean bean = new AdvisedTestBean();
    RuntimeException error = new RuntimeException();
    try {
      stringficationStrategyFactoryControl = MockControl.createControl(StringficationStrategyFactory.class);
      stringficationStrategyControl = MockControl.createControl(StringficationStrategy.class);
      StringficationStrategy <AdvisedTestBean> stringficationStrategy = (StringficationStrategy <AdvisedTestBean>)(stringficationStrategyControl.getMock());
      StringficationStrategyFactory stringficationStrategyFactory = (StringficationStrategyFactory)(stringficationStrategyFactoryControl.getMock());
      
      stringficationStrategyFactory.create(AdvisedTestBean.class);
      stringficationStrategyFactoryControl.setReturnValue(stringficationStrategy);
      
      stringficationStrategy.toString(eq(bean), isA(StringBuilder.class));
      stringficationStrategyControl.setThrowable(error);

      stringficationStrategyFactoryControl.replay();
      stringficationStrategyControl.replay();

      Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
      klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new Object [] { stringficationStrategyFactory });
      
      bean.toString();
      fail();
    } catch (RuntimeException ex) {
      assertSame(error, ex);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      stringficationStrategyFactoryControl.verify();
      stringficationStrategyControl.verify();
    }
  }

  public void testAdvisedToStringWithStringficationStrategyCreationError() {
    MockControl stringficationStrategyFactoryControl = null;
    MockControl stringficationStrategyControl = null;
    AdvisedTestBean bean = new AdvisedTestBean();
    RuntimeException error = new RuntimeException();
    try {
      stringficationStrategyFactoryControl = MockControl.createControl(StringficationStrategyFactory.class);
      stringficationStrategyControl = MockControl.createControl(StringficationStrategy.class);
      StringficationStrategy <AdvisedTestBean> stringficationStrategy = (StringficationStrategy <AdvisedTestBean>)(stringficationStrategyControl.getMock());
      StringficationStrategyFactory stringficationStrategyFactory = (StringficationStrategyFactory)(stringficationStrategyFactoryControl.getMock());
      
      stringficationStrategyFactory.create(AdvisedTestBean.class);
      stringficationStrategyFactoryControl.setThrowable(error);

      stringficationStrategyFactoryControl.replay();
      stringficationStrategyControl.replay();

      Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
      klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new Object [] { stringficationStrategyFactory });
      
      bean.toString();
      fail();
    } catch (RuntimeException ex) {
      assertSame(error, ex);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      stringficationStrategyFactoryControl.verify();
      stringficationStrategyControl.verify();
    }
  }
}
