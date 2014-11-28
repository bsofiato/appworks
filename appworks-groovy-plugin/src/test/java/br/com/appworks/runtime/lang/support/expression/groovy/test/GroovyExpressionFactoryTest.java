/*
 * GroovyExpressionFactoryTest.java
 * JUnit based test
 *
 * Created on 12 de Outubro de 2005, 18:41
 */

package br.com.appworks.runtime.lang.support.expression.groovy.test;

import br.com.appworks.runtime.lang.support.expression.Expression;
import br.com.appworks.runtime.lang.support.expression.ExpressionCompilationException;
import br.com.appworks.runtime.lang.support.expression.ExpressionException;
import br.com.appworks.runtime.lang.support.expression.groovy.GroovyExpressionFactory;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class GroovyExpressionFactoryTest extends TestCase {
  
  public GroovyExpressionFactoryTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNullSourceCodeExpressionCreation() {
    try {
      new GroovyExpressionFactory().create(null);
      fail();
    } catch (ExpressionCompilationException ex) {
    } catch (ExpressionException ex) {
      fail();
    }
  }

  public void testSourceCodeExpressionCreation() {
    try {
      Expression expression = new GroovyExpressionFactory().create("TESTE");
      assertEquals("TESTE", expression.getSourceCode());
    } catch (ExpressionException ex) {
      fail();
    }
  }
}
