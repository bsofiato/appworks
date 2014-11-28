/*
 * StringficationHandlerTest.java
 * JUnit based test
 *
 * Created on 23 de Novembro de 2005, 00:56
 */

package br.com.appworks.runtime.lang.support.stringfication.test;

import br.com.appworks.runtime.lang.support.stringfication.StringficationHandler;
import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategy;
import java.util.Arrays;
import java.util.Collection;
import junit.framework.*;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class StringficationHandlerTest extends TestCase {
  
  public StringficationHandlerTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNullStringfication() {
    StringBuilder sb = new StringBuilder();
    new StringficationHandler <Object> ().toString(null, sb);
    assertEquals("null", sb.toString());
  }
  public void testEmptyStringfication() {
    StringBuilder sb = new StringBuilder();
    new StringficationHandler <Object> ().toString(new Object(), sb);
    assertEquals("[]", sb.toString());
  }
  public void testStringfication() {
    Object object = new Object();
    
    MockControl strategyControl1 = MockControl.createControl(StringficationStrategy.class);
    MockControl strategyControl2 = MockControl.createControl(StringficationStrategy.class);

    StringficationStrategy <Object> strategy1 = (StringficationStrategy <Object>)(strategyControl1.getMock());
    StringficationStrategy <Object> strategy2 = (StringficationStrategy <Object>)(strategyControl2.getMock());

    StringBuilder sb = new StringBuilder();

    strategy1.toString(object, sb);
    strategy2.toString(object, sb);
    
    strategyControl1.replay();
    strategyControl2.replay();

    new StringficationHandler <Object> ((Collection <StringficationStrategy<Object>>)((Collection)Arrays.asList(new StringficationStrategy [] {strategy1, strategy2}))).toString(object, sb);
    
    strategyControl1.verify();
    strategyControl2.verify();
  }

   public void testStringficationWithRuntimeException() {
    Object object = new Object();
    
    MockControl strategyControl1 = MockControl.createControl(StringficationStrategy.class);
    MockControl strategyControl2 = MockControl.createControl(StringficationStrategy.class);
    RuntimeException error = new RuntimeException();
    try { 
      StringficationStrategy <Object> strategy1 = (StringficationStrategy <Object>)(strategyControl1.getMock());
      StringficationStrategy <Object> strategy2 = (StringficationStrategy <Object>)(strategyControl2.getMock());

      StringBuilder sb = new StringBuilder();

      strategy1.toString(object, sb);
      strategy2.toString(object, sb);
      strategyControl2.setThrowable(error);
    
      strategyControl1.replay();
      strategyControl2.replay();

      new StringficationHandler <Object> ((Collection <StringficationStrategy<Object>>)((Collection)Arrays.asList(new StringficationStrategy [] {strategy1, strategy2}))).toString(object, sb);
    } catch (RuntimeException ex) {
      assertSame(error, ex);
    } finally {
      strategyControl1.verify();
      strategyControl2.verify();
    }
  }

   public void testClone() {
    try {
      new StringficationHandler(null).clone();
      fail();
    } catch (CloneNotSupportedException ex) {
    }
  }

}
