/*
 * TemplateProcessingExceptionTest.java
 * JUnit based test
 *
 * Created on 18 de Dezembro de 2005, 17:01
 */

package br.com.appworks.runtime.lang.support.template.test;

import br.com.appworks.runtime.lang.support.template.TemplateProcessingException;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class TemplateProcessingExceptionTest extends TestCase {
  
  public TemplateProcessingExceptionTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void test() {
    assertNull(new TemplateProcessingException().getCause());
  }

  public void testWithCause() {
    RuntimeException ex = new RuntimeException();
    assertSame(ex, new TemplateProcessingException(ex).getCause());
  }
}

