/*
 * GroovyExpressionTest.java
 * JUnit based test
 *
 * Created on 2 de Outubro de 2005, 15:47
 */

package br.com.appworks.runtime.lang.support.expression.groovy.test;

import br.com.appworks.runtime.lang.support.expression.ExpressionCompilationException;
import br.com.appworks.runtime.lang.support.expression.ExpressionEvaluationException;
import br.com.appworks.runtime.lang.support.expression.ExpressionException;
import br.com.appworks.runtime.lang.support.expression.groovy.GroovyExpression;
import groovy.lang.Binding;
import groovy.lang.GString;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class GroovyExpressionTest extends TestCase {
  private static class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private boolean exceptionOccurred;
    public boolean isExceptionOcurred() {
      return exceptionOccurred;
    }
    public void uncaughtException(Thread t, Throwable e) {
      this.exceptionOccurred = true;
    }
  }
  public class PrimitiveAttributesBean {
    public int x;
    public BigDecimal y;
    public String z;
    public PrimitiveAttributesBean(int x) {
      this.x = x;
    }
    public PrimitiveAttributesBean(BigDecimal y) {
      this.y = y;
    }
    public PrimitiveAttributesBean(String z) {
      this.z = z;
    }
    public int x() {
      return x;
    }
    public BigDecimal y() {
      return y;
    }
    public String z() {
      return z;
    }
    public int getA() {
      return x;
    }
    public BigDecimal getB() {
      return y;
    }
    public String getC() {
      return z;
    }

  }

  public class TestBean {
    private String privateProperty = "privateProperty";
    private String privateMethod() {
      return "privateMethodNoArgs";
    }
    private String privateMethod(String arg1, String arg2) {
      return "privateMethod";
    }
    private TestBean nested() {
      return new TestBean();
    }
    
    private String getGetterProperty() {
      return "getterProperty";
    }
  }
  public GroovyExpressionTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  public void testGetSourceCode() {
    try {
      assertEquals("true", new GroovyExpression("true").getSourceCode());
    } catch (ExpressionException ex) {
      fail();
    }
  }
  public void testExpressionCompilationException() {
    try {
      GroovyExpression expression = new GroovyExpression("{");
      expression.evaluate(Collections.EMPTY_MAP);
      fail();
    } catch (ExpressionCompilationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  public void testExpressionEvaluationException() {
    GroovyRuntimeException error = new GroovyRuntimeException("XXX");
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", error);
      GroovyExpression expression = new GroovyExpression("throw x");
      expression.evaluate(map);
      fail();
    } catch (ExpressionEvaluationException ex) {
      assertSame(error, ex.getCause());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testUnreferencedUnknowPropertyEvaluationException() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("uknownProperty");
      expression.evaluate(map);
      fail();
    } catch (ExpressionEvaluationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  public void testUnreferencedFieldProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("privateProperty");
      assertEquals("privateProperty", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testUnreferencedGetterProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("getterProperty");
      assertEquals("getterProperty", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testUnreferencedPropertyWithParameter() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      map.put("privateProperty", "parameter");
      GroovyExpression expression = new GroovyExpression("privateProperty");
      assertEquals("parameter", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  

  public void testUnreferencedUnknowMethodEvaluationException() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("uknownMethod()");
      expression.evaluate(map);
      fail();
    } catch (ExpressionEvaluationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  public void testUnreferencedUnknowPropertyWithParameterEvaluationException() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      map.put("parameter", "x");
      GroovyExpression expression = new GroovyExpression("parameter");
      assertEquals("x", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testUnreferencedUnknowMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("unknowMethod()");
      expression.evaluate(map);
      fail();
    } catch (ExpressionEvaluationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  public void testUnreferencedCommonMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("getClass()");
      assertEquals(TestBean.class, expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testUnreferencedNoArgsMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("privateMethod()");
      assertEquals("privateMethodNoArgs", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testUnreferencedMethodWithNullParameters() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("privateMethod(null, null)");
      assertEquals("privateMethod", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testUnreferencedMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("privateMethod(\"X\", \"Y\")");
      assertEquals("privateMethod", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  
   public void testUnreferencedMethodWithWrongParameterNumbers() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("privateMethod(\"X\", \"Y\", \"Z\")");
      expression.evaluate(map);
      fail();
    } catch (ExpressionEvaluationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }

  public void testUnreferencedMethodWithWrongParameterTypes() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("privateMethod(0, 1)");
      expression.evaluate(map);
      fail();
    } catch (ExpressionEvaluationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  public void testUnreferencedMethodReturnFieldProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("nested().privateProperty");
      assertEquals("privateProperty", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testUnreferencedMethodReturnGetterProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("nested().getterProperty");
      assertEquals("getterProperty", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testUnreferencedMethodReturnMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("nested().privateMethod(\"X\", \"Y\")");
      assertEquals("privateMethod", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testReferencedUnknowPropertyEvaluationException() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("this.uknownProperty");
      expression.evaluate(map);
      fail();
    } catch (ExpressionEvaluationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  public void testReferencedUnknowMethodEvaluationException() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("this.uknownMethod()");
      expression.evaluate(map);
      fail();
    } catch (ExpressionEvaluationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }

  public void testReferencedUnknowPropertyWithParameterEvaluationException() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      map.put("parameter", "x");
      GroovyExpression expression = new GroovyExpression("this.parameter");
      expression.evaluate(map);
      fail();
    } catch (ExpressionEvaluationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  public void testReferencedFieldProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("this.privateProperty");
      assertEquals("privateProperty", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testReferencedGetterProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("this.getterProperty");
      assertEquals("getterProperty", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testReferencedPropertyWithParameter() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      map.put("privateProperty", "parameter");
      GroovyExpression expression = new GroovyExpression("this.privateProperty");
      assertEquals("privateProperty", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testReferencedCommonMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("this.getClass()");
      assertEquals(TestBean.class, expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testReturnValue() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("return", "return");
      GroovyExpression expression = new GroovyExpression("return");
      assertEquals("return", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testReturnFieldProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("return", new TestBean());
      GroovyExpression expression = new GroovyExpression("return.privateProperty");
      assertEquals("privateProperty", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testReturnGetterProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("return", new TestBean());
      GroovyExpression expression = new GroovyExpression("return.getterProperty");
      assertEquals("getterProperty", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testReturnMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("return", new TestBean());
      GroovyExpression expression = new GroovyExpression("return.privateMethod()");
      assertEquals("privateMethodNoArgs", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testReferencedNoArgsMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("this.privateMethod()");
      assertEquals("privateMethodNoArgs", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testReferencedMethodWithNullParameters() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("this.privateMethod(null, null)");
      assertEquals("privateMethod", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testReferencedMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("this.privateMethod(\"X\", \"Y\")");
      assertEquals("privateMethod", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
   public void testReferencedMethodWithWrongParameterNumbers() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("this.privateMethod(\"X\", \"Y\", \"Z\")");
      expression.evaluate(map);
      fail();
    } catch (ExpressionEvaluationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
   public void testReferencedMethodWithWrongParameterTypes() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("this.privateMethod(0, 1)");
      expression.evaluate(map);
      fail();
    } catch (ExpressionEvaluationException ex) {
    } catch (Exception ex) {
      fail();
    }
  }

  public void testReferencedMethodReturnFieldProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("this.nested().privateProperty");
      assertEquals("privateProperty", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testReferencedMethodReturnGetterProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("this.nested().getterProperty");
      assertEquals("getterProperty", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testReferencedMethodReturnMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyExpression expression = new GroovyExpression("this.nested().privateMethod(\"X\", \"Y\")");
      assertEquals("privateMethod", expression.evaluate(map));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testAPIUse() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      GroovyExpression expression = new GroovyExpression("new java.util.ArrayList()");
      assertTrue((Object)(expression.evaluate(map)) instanceof ArrayList);
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testPrimitiveTypeAritmeticOperatorSanityCheck() {
    try {
      Binding binding  = new Binding();
      binding.setVariable("x", 1);
      assertEquals(2, new GroovyShell(binding).evaluate("x+x"));
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testBigDecimalTypeAritmeticOperatorSanityCheck() {
    try {
      Binding binding  = new Binding();
      binding.setVariable("x", new BigDecimal(1));
      assertEquals(new BigDecimal(2), new GroovyShell(binding).evaluate("x+x"));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testBigDecimalTypeAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new BigDecimal(1));
      GroovyExpression expression = new GroovyExpression("x + x");
      assertEquals(new BigDecimal(2), expression.evaluate(map));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testBigDecimalTypeFieldAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean(new BigDecimal(1)));
      GroovyExpression expression = new GroovyExpression("x.y + x.y");
      assertEquals(new BigDecimal(2), expression.evaluate(map));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testBigDecimalTypePropertyAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean(new BigDecimal(1)));
      GroovyExpression expression = new GroovyExpression("x.b + x.b");
      assertEquals(new BigDecimal(2), expression.evaluate(map));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

   public void testBigDecimalTypeMethodReturnAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean(new BigDecimal(1)));
      GroovyExpression expression = new GroovyExpression("x.y()+x.y()");
      assertEquals(new BigDecimal(2), expression.evaluate(map));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

    public void testPrimitiveTypeFieldAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean(1));
      GroovyExpression expression = new GroovyExpression("x.x + x.x");
      assertEquals(2, expression.evaluate(map));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  public void testPrimitiveTypePropertyAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean(1));
      GroovyExpression expression = new GroovyExpression("x.a + x.a");
      assertEquals(2, expression.evaluate(map));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testPrimitiveTypeMethodReturnAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean(1));
      GroovyExpression expression = new GroovyExpression("x.x() + x.x()");
      assertEquals(2, expression.evaluate(map));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

    public void testStringTypeFieldConcatenation() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean("x"));
      GroovyExpression expression = new GroovyExpression("x.z+x.z");
      assertEquals("xx", expression.evaluate(map));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testStringTypePropertyConcatenation() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean("x"));
      GroovyExpression expression = new GroovyExpression("x.c+x.c");
      assertEquals("xx", expression.evaluate(map));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testStringTypeMethodReturnConcatenation() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean("x"));
      GroovyExpression expression = new GroovyExpression("x.z() + x.z()");
      assertEquals("xx", expression.evaluate(map));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testStringConstantConcatenationSanityCheck() {
    try {
      Binding binding  = new Binding();
      assertEquals("xx", new GroovyShell(binding).evaluate("\"x\" + \"x\""));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testStringConcatenationSanityCheck() {
    try {
      Binding binding  = new Binding();
      binding.setVariable("x", "x");
      assertEquals("xx", new GroovyShell(binding).evaluate("x + x"));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testStringConcatenation() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", "x");
      GroovyExpression expression = new GroovyExpression("x+x");
      assertEquals("xx", expression.evaluate(map));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testStringConstantConcatenation() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      GroovyExpression expression = new GroovyExpression("\"x\"+\"x\"");
      assertEquals("xx", expression.evaluate(map));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testPrimitiveTypeAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", 1);
      GroovyExpression expression = new GroovyExpression("x+x");
      assertEquals(2, expression.evaluate(map));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testMultiThreadedExecution() {
    try {
      ExceptionHandler handler = new ExceptionHandler();
      final GroovyExpression expression = new GroovyExpression("x+y");
      Runnable runnnable = new Runnable() {
        public void run() {
          try {
            Map <String, Object> context = new HashMap <String, Object>();
            context.put("x", 1);
            context.put("y", 0);
            assertEquals(1, expression.evaluate(context));
          } catch (Exception ex) {
            fail();
          }
        }
      };
      Runnable runnnable2 = new Runnable() {
        public void run() {
          try {
            while (true) {
              Map <String, Object> context = new HashMap <String, Object>();
              context.put("x", 1);
              context.put("y", 0);
              expression.evaluate(context);
            }
          } catch (Exception ex) {
            fail();
          }
        }
      };

      Thread [] threads2 = new Thread[5];
      Thread [] threads = new Thread[5];
      for (int i=0; i<threads2.length; i++) {
        threads2[i] = new Thread(runnnable2);
        threads2[i].setUncaughtExceptionHandler(handler);
        threads2[i].start();
      }

      for (int i=0; i<threads.length; i++) {
        threads[i] = new Thread(runnnable);
        threads[i].setUncaughtExceptionHandler(handler);
        threads[i].start();
      }
      for (Thread thread : threads) {
        thread.join();
      }
      for (Thread thread : threads2) {
        thread.interrupt();
      }
      assertFalse(handler.isExceptionOcurred());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testGStringEvaluation() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("teste", "xxx");
      GroovyExpression expression = new GroovyExpression("\"${teste}\"");
      assertFalse(((Object)(expression.evaluate(map))) instanceof GString);
      assertEquals("xxx", expression.evaluate(map));
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testStringWithThisKeywordMixedContentEvaluation() {
    try {
      GroovyExpression expression = new GroovyExpression("\"this\"");
      assertEquals("this", expression.evaluate(new HashMap <String, Object> ()));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testStringWithReturnKeywordMixedContentEvaluation() {
    try {
      GroovyExpression expression = new GroovyExpression("\"return\"");
      assertEquals("return", expression.evaluate(new HashMap <String, Object> ()));
    } catch (ExpressionEvaluationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
}
