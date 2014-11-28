/*
 * TemplateStringficationStrategyTest.java
 * JUnit based test
 *
 * Created on 25 de Novembro de 2005, 00:12
 */

package br.com.appworks.runtime.lang.support.stringfication.test;

import br.com.appworks.runtime.lang.support.stringfication.TemplateStringficationStrategy;
import br.com.appworks.runtime.lang.support.template.Template;
import br.com.appworks.runtime.lang.support.template.TemplateProcessingException;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class TemplateStringficationStrategyTest extends TestCase {
  
  public TemplateStringficationStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNullStringfication() {
    StringBuilder sb = new StringBuilder();
    new TemplateStringficationStrategy(null).toString(null, sb);
    assertEquals("null", sb.toString());
  }
  public void testStringfication() {
    MockControl templateControl = MockControl.createControl(Template.class);
    try {
      Template template = (Template)(templateControl.getMock());
      Map <String, Object> context = new HashMap <String, Object> ();
      context.put("this", "X");
      template.process(context);
      templateControl.setReturnValue("TESTE");
      templateControl.replay();
      StringBuilder sb = new StringBuilder();
      new TemplateStringficationStrategy(template).toString("X", sb);
      assertEquals("TESTE", sb.toString());
    } catch (Exception ex) {
      fail();
    } finally {
      templateControl.verify();
    }
  }
  
  public void testStringficationWithRuntimeException() {
    MockControl templateControl = MockControl.createControl(Template.class);
    RuntimeException error = new RuntimeException();
    try {
      Template template = (Template)(templateControl.getMock());
      Map <String, Object> context = new HashMap <String, Object> ();
      context.put("this", "X");
      template.process(context);
      templateControl.setThrowable(error);
      templateControl.replay();
      StringBuilder sb = new StringBuilder();
      new TemplateStringficationStrategy(template).toString("X", sb);
      assertEquals("TESTE", sb.toString());
      fail();
    } catch (RuntimeException ex) {
      assertSame(error, ex);
    } catch (Exception ex) {
      fail();
    } finally {
      templateControl.verify();
    }
  }
  
  public void testStringficationWithTemplateEvaluationException() {
    MockControl templateControl = MockControl.createControl(Template.class);
    TemplateProcessingException error = new TemplateProcessingException();
    try {
      Template template = (Template)(templateControl.getMock());
      Map <String, Object> context = new HashMap <String, Object> ();
      context.put("this", "X");
      template.process(context);
      templateControl.setThrowable(error);
      templateControl.replay();
      StringBuilder sb = new StringBuilder();
      new TemplateStringficationStrategy(template).toString("X", sb);
      fail();
    } catch (RuntimeException ex) {
      assertSame(error, ex.getCause());
    } catch (Exception ex) {
      fail();
    } finally {
      templateControl.verify();
    }
  }
  
  public void testClone() {
    MockControl templateControl = MockControl.createControl(Template.class);
    try {
      Template template = (Template)(templateControl.getMock());
      Map <String, Object> context = new HashMap <String, Object> ();
      context.put("this", "X");
      template.process(context);
      templateControl.setReturnValue("TESTE");
      templateControl.replay();
      StringBuilder sb = new StringBuilder();
      TemplateStringficationStrategy strategy = (TemplateStringficationStrategy)(new TemplateStringficationStrategy(template).clone());
      strategy.toString("X", sb);
      assertEquals("TESTE", sb.toString());
    } catch (Exception ex) {
      fail();
    } finally {
      templateControl.verify();
    }
  }
}
