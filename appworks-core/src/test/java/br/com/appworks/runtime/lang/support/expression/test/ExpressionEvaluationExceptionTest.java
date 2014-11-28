/*
 * ExpressionEvaluationExceptionTest.java
 * JUnit based test
 *
 * Created on 18 de Dezembro de 2005, 16:56
 */

package br.com.appworks.runtime.lang.support.expression.test;

import br.com.appworks.runtime.lang.support.expression.ExpressionEvaluationException;
import junit.framework.*;

public class ExpressionEvaluationExceptionTest extends TestCase {
  
  public ExpressionEvaluationExceptionTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void test() {
    assertNull(new ExpressionEvaluationException().getCause());
  }

  public void testWithCause() {
    RuntimeException ex = new RuntimeException();
    assertSame(ex, new ExpressionEvaluationException(ex).getCause());
  }
}
