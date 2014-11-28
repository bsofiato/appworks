/*
 * ExpressionCompilationExceptionTest.java
 * JUnit based test
 *
 * Created on 18 de Dezembro de 2005, 16:54
 */

package br.com.appworks.runtime.lang.support.expression.test;

import br.com.appworks.runtime.lang.support.expression.ExpressionCompilationException;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class ExpressionCompilationExceptionTest extends TestCase {
  
  public ExpressionCompilationExceptionTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void test() {
    assertNull(new ExpressionCompilationException().getCause());
  }

  public void testWithCause() {
    RuntimeException ex = new RuntimeException();
    assertSame(ex, new ExpressionCompilationException(ex).getCause());
  }
}
