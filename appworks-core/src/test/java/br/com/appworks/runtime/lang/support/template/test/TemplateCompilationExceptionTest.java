/*
 * TemplateCompilationExceptionTest.java
 * JUnit based test
 *
 * Created on 18 de Dezembro de 2005, 17:00
 */

package br.com.appworks.runtime.lang.support.template.test;

import br.com.appworks.runtime.lang.support.template.TemplateCompilationException;
import junit.framework.TestCase;

public class TemplateCompilationExceptionTest extends TestCase {
  
  public TemplateCompilationExceptionTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void test() {
    assertNull(new TemplateCompilationException().getCause());
  }

  public void testWithCause() {
    RuntimeException ex = new RuntimeException();
    assertSame(ex, new TemplateCompilationException(ex).getCause());
  }
}

