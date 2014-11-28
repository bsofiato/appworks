/*
 * ExpressionExceptionTest.java
 * JUnit based test
 *
 * Created on 18 de Dezembro de 2005, 16:53
 */

package br.com.appworks.runtime.lang.support.expression.test;

import br.com.appworks.runtime.lang.support.expression.ExpressionException;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class ExpressionExceptionTest extends TestCase {
  
  public ExpressionExceptionTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void test() {
    assertNull(new ExpressionException().getCause());
  }

  public void testWithCause() {
    RuntimeException ex = new RuntimeException();
    assertSame(ex, new ExpressionException(ex).getCause());
  }

  // TODO add test methods here. The name must begin with 'test'. For example:
  // public void testHello() {}

}
