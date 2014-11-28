/*
 * TemplateExceptionTest.java
 * JUnit based test
 *
 * Created on 18 de Dezembro de 2005, 16:59
 */

package br.com.appworks.runtime.lang.support.template.test;

import br.com.appworks.runtime.lang.support.template.TemplateException;
import junit.framework.*;

public class TemplateExceptionTest extends TestCase {
  
  public TemplateExceptionTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void test() {
    assertNull(new TemplateException().getCause());
  }

  public void testWithCause() {
    RuntimeException ex = new RuntimeException();
    assertSame(ex, new TemplateException(ex).getCause());
  }

  // TODO add test methods here. The name must begin with 'test'. For example:
  // public void testHello() {}

}
