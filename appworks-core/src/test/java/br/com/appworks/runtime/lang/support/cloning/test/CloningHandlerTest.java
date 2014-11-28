/*
 * CloningHandlerTest.java
 * JUnit based test
 *
 * Created on 15 de Julho de 2005, 01:23
 */

package br.com.appworks.runtime.lang.support.cloning.test;

import br.com.appworks.runtime.lang.support.cloning.CloningHandler;
import br.com.appworks.runtime.lang.support.cloning.CloningProcessor;
import br.com.appworks.runtime.lang.support.cloning.PropertyCloningProcessor;
import java.util.ArrayList;
import java.util.Collection;
import junit.framework.*;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class CloningHandlerTest extends TestCase {
  public static class TestBean implements Cloneable {
  }
  public CloningHandlerTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNullCloning() {
    try {
      assertNull(null, new CloningHandler <Object> ().clone(null));
    } catch (CloneNotSupportedException ex) {
      ex.printStackTrace();
      fail();
    }
  }
  public void testNonCloneableCloning() {
    try {
      Object object = new Object();
      assertSame(object, new CloningHandler <Object> ().clone(object));
    } catch (CloneNotSupportedException ex) {
      fail();
    } 
  }

  public void testCloneableCloning() {
    try {
      TestBean bean = new TestBean();
      assertSame(bean, new CloningHandler <TestBean> ().clone(bean));
    } catch (CloneNotSupportedException ex) {
      ex.printStackTrace();
      fail();
    } 
  }
  
  public void testCloneableCloningWithCloneNotSupportedException() {
    MockControl cloningProcessorControl1 = MockControl.createControl(CloningProcessor.class);
    MockControl cloningProcessorControl2 = MockControl.createControl(CloningProcessor.class);
    CloneNotSupportedException exp = new CloneNotSupportedException();
    cloningProcessorControl1.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
    cloningProcessorControl2.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
    try {
      TestBean bean = new TestBean();

      CloningProcessor <TestBean> cloningProcessor1 = (CloningProcessor <TestBean>)(cloningProcessorControl1.getMock());
      CloningProcessor <TestBean> cloningProcessor2 = (CloningProcessor <TestBean>)(cloningProcessorControl2.getMock());
      
      cloningProcessor1.process(bean);
      cloningProcessorControl1.setThrowable(exp);
      
      cloningProcessorControl1.replay();
      cloningProcessorControl2.replay();
      
      Collection <CloningProcessor <TestBean>> cloningProcessors = new ArrayList <CloningProcessor <TestBean>> ();
      cloningProcessors.add(cloningProcessor1);
      cloningProcessors.add(cloningProcessor2);
      
      new CloningHandler <TestBean> (cloningProcessors).clone(bean);
      fail();
    } catch (CloneNotSupportedException ex) {
      assertSame(exp, ex);
    } finally {
      cloningProcessorControl1.verify();
      cloningProcessorControl2.verify();
    }
  }

  public void testCloneableCloningWithRuntimeException() {
    MockControl cloningProcessorControl1 = MockControl.createControl(CloningProcessor.class);
    MockControl cloningProcessorControl2 = MockControl.createControl(CloningProcessor.class);
    RuntimeException exp = new RuntimeException("TEST");
    cloningProcessorControl1.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
    cloningProcessorControl2.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
    try {
      TestBean bean = new TestBean();

      CloningProcessor <TestBean> cloningProcessor1 = (CloningProcessor <TestBean>)(cloningProcessorControl1.getMock());
      CloningProcessor <TestBean> cloningProcessor2 = (CloningProcessor <TestBean>)(cloningProcessorControl2.getMock());
      
      cloningProcessor1.process(bean);
      cloningProcessorControl1.setThrowable(exp);
      
      cloningProcessorControl1.replay();
      cloningProcessorControl2.replay();
      
      Collection <CloningProcessor <TestBean>> cloningProcessors = new ArrayList <CloningProcessor <TestBean>> ();
      cloningProcessors.add(cloningProcessor1);
      cloningProcessors.add(cloningProcessor2);
      
      new CloningHandler <TestBean> (cloningProcessors).clone(bean);
      fail();
    } catch (CloneNotSupportedException ex) {
      fail();
    } catch (RuntimeException ex) {
      assertSame(exp, ex);
    } finally {
      cloningProcessorControl1.verify();
      cloningProcessorControl2.verify();
    }
  }
  public void testCloneableSucessfullCloning() {
    MockControl cloningProcessorControl1 = MockControl.createControl(CloningProcessor.class);
    MockControl cloningProcessorControl2 = MockControl.createControl(CloningProcessor.class);
    cloningProcessorControl1.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
    cloningProcessorControl2.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
    try {
      TestBean bean = new TestBean();

      CloningProcessor <TestBean> cloningProcessor1 = (CloningProcessor <TestBean>)(cloningProcessorControl1.getMock());
      CloningProcessor <TestBean> cloningProcessor2 = (CloningProcessor <TestBean>)(cloningProcessorControl2.getMock());
      
      cloningProcessor1.process(bean);
      cloningProcessorControl1.setReturnValue(bean);
      cloningProcessor2.process(bean);
      cloningProcessorControl2.setReturnValue(bean);
      
      cloningProcessorControl1.replay();
      cloningProcessorControl2.replay();
      
      Collection <CloningProcessor <TestBean>> cloningProcessors = new ArrayList <CloningProcessor <TestBean>> ();
      cloningProcessors.add(cloningProcessor1);
      cloningProcessors.add(cloningProcessor2);
      
      assertSame(bean, new CloningHandler <TestBean> (cloningProcessors).clone(bean));
    } catch (CloneNotSupportedException ex) {
      fail();
    } finally {
      cloningProcessorControl1.verify();
      cloningProcessorControl2.verify();
    }
  }
  
  public void testClone() {
    try {
      CloningHandler handler = new CloningHandler();
      assertTrue(handler.clone() instanceof CloningHandler);
      assertNotSame(handler, handler.clone());
    } catch (Exception ex) {
      fail();
    }
  }
}
