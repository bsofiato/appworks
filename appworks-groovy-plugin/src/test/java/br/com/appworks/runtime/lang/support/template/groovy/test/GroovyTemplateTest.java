/*
 * GroovyTemplateTest.java
 * JUnit based test
 *
 * Created on 24 de Novembro de 2005, 01:46
 */

package br.com.appworks.runtime.lang.support.template.groovy.test;

import br.com.appworks.runtime.lang.support.expression.Expression;
import br.com.appworks.runtime.lang.support.expression.ExpressionEvaluationException;
import br.com.appworks.runtime.lang.support.template.TemplateCompilationException;
import br.com.appworks.runtime.lang.support.template.TemplateProcessingException;
import br.com.appworks.runtime.lang.support.template.groovy.GroovyTemplate;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class GroovyTemplateTest extends TestCase {

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

  public GroovyTemplateTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testSourceCodeConstructorTemplateCompilationException() {
    try {
      new GroovyTemplate("${new xxxx}");
      fail();
    } catch (TemplateCompilationException ex) {
    }
  }

  public void testGetSourceCode() {
    MockControl expressionControl = MockControl.createControl(Expression.class);
    Expression expression = (Expression)(expressionControl.getMock());
    expression.getSourceCode();
    expressionControl.setReturnValue("\"TESTE\"");
    
    expressionControl.replay();
    assertEquals("TESTE", new GroovyTemplate(expression).getSourceCode());
    expressionControl.verify();
  }

  public void testEvaluate() {
    MockControl expressionControl = MockControl.createControl(Expression.class);
    try {
      Expression expression = (Expression)(expressionControl.getMock());
      Map <String, Object> map = new HashMap <String, Object> ();
      expression.evaluate(map);
      expressionControl.setReturnValue("TESTE");
    
      expressionControl.replay();
      assertEquals("TESTE", new GroovyTemplate(expression).process(map));
    } catch (Exception ex) {
      fail();
    } finally {
      expressionControl.verify();
    }
  }
  public void testEvaluateWithRuntimeException() {
    MockControl expressionControl = MockControl.createControl(Expression.class);
    RuntimeException error = new RuntimeException();
    try {
      Expression expression = (Expression)(expressionControl.getMock());
      Map <String, Object> map = new HashMap <String, Object> ();
      expression.evaluate(map);
      expressionControl.setThrowable(error);
    
      expressionControl.replay();
      assertEquals("TESTE", new GroovyTemplate(expression).process(map));
      fail();
    } catch (RuntimeException ex) {
      assertSame(error, ex);
    } catch (Exception ex) {
      fail();
    } finally {
      expressionControl.verify();
    }
  }
  
  public void testEvaluateWithExpressionEvaluationException() {
    MockControl expressionControl = MockControl.createControl(Expression.class);
    ExpressionEvaluationException error = new ExpressionEvaluationException();
    try {
      Expression expression = (Expression)(expressionControl.getMock());
      Map <String, Object> map = new HashMap <String, Object> ();
      expression.evaluate(map);
      expressionControl.setThrowable(error);
    
      expressionControl.replay();
      assertEquals("TESTE", new GroovyTemplate(expression).process(map));
    } catch (TemplateProcessingException ex) {
      assertSame(error, ex.getCause());
    } catch (Exception ex) {
      fail();
    } finally {
      expressionControl.verify();
    }
  }
  
  public void testMixedContentEvaluation() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("test", "TEST");
      GroovyTemplate template = new GroovyTemplate("Mixed context ${test}");
      assertEquals("Mixed context TEST", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testThisKeywordMixedContentEvaluation() {
    try {
      GroovyTemplate template = new GroovyTemplate("this");
      assertEquals("this", template.process(new HashMap <String, Object> ()));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testReturnKeywordMixedContentEvaluation() {
    try {
      GroovyTemplate template = new GroovyTemplate("return");
      assertEquals("return", template.process(new HashMap <String, Object> ()));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  
  public void testUnreferencedUnknowPropertyEvaluationException() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${uknownProperty}");
      template.process(map);
      fail();
    } catch (TemplateProcessingException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  public void testUnreferencedFieldProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${privateProperty}");
      assertEquals("privateProperty", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testUnreferencedGetterProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${getterProperty}");
      assertEquals("getterProperty", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testUnreferencedPropertyOverride() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      map.put("privateProperty", "parameter");
      GroovyTemplate template = new GroovyTemplate("${privateProperty}");
      assertEquals("parameter", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testUnreferencedUnknowMethodEvaluationException() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate expression = new GroovyTemplate("${uknownMethod()}");
      expression.process(map);
      fail();
    } catch (TemplateProcessingException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  public void testUnreferencedUnknowMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${unknowMethod()}");
      template.process(map);
      fail();
    } catch (TemplateProcessingException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testUnreferencedCommonMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${getClass()}");
      assertEquals(TestBean.class.toString(), template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testUnreferencedNoArgsMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${privateMethod()}");
      assertEquals("privateMethodNoArgs", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testUnreferencedMethodWithNullParameters() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${privateMethod(null, null)}");
      assertEquals("privateMethod", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testUnreferencedMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${privateMethod(\"X\", \"Y\")}");
      assertEquals("privateMethod", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  
   public void testUnreferencedMethodWithWrongParameterNumbers() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${privateMethod(\"X\", \"Y\", \"Z\")}");
      template.process(map);
      fail();
    } catch (TemplateProcessingException ex) {
    } catch (Exception ex) {
      fail();
    }
  }

  public void testUnreferencedMethodWithWrongParameterTypes() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${privateMethod(0, 1)}");
      template.process(map);
      fail();
    } catch (TemplateProcessingException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  public void testUnreferencedMethodReturnFieldProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${nested().privateProperty}");
      assertEquals("privateProperty", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testUnreferencedMethodReturnGetterProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${nested().getterProperty}");
      assertEquals("getterProperty", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testUnreferencedMethodReturnMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${nested().privateMethod(\"X\", \"Y\")}");
      assertEquals("privateMethod", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testReferencedUnknowPropertyEvaluationException() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${this.uknownProperty}");
      template.process(map);
      fail();
    } catch (TemplateProcessingException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
  public void testReferencedUnknowMethodEvaluationException() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${this.uknownMethod()}");
      template.process(map);
      fail();
    } catch (TemplateProcessingException ex) {
    } catch (Exception ex) {
      fail();
    }
  }

  public void testReferencedFieldProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${this.privateProperty}");
      assertEquals("privateProperty", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testReferencedGetterProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${this.getterProperty}");
      assertEquals("getterProperty", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testReferencedCommonMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${this.getClass()}");
      assertEquals(TestBean.class.toString(), template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  
  
  public void testReturnValue() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("return", "test");
      GroovyTemplate template = new GroovyTemplate("${return}");
      assertEquals("test", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testReturnFieldProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("return", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${return.privateProperty}");
      assertEquals("privateProperty", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testReturnGetterProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("return", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${return.getterProperty}");
      assertEquals("getterProperty", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testReturnMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("return", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${return.privateMethod()}");
      assertEquals("privateMethodNoArgs", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testReferencedNoArgsMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${this.privateMethod()}");
      assertEquals("privateMethodNoArgs", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testReferencedMethodWithNullParameters() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${this.privateMethod(null, null)}");
      assertEquals("privateMethod", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testReferencedMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${this.privateMethod(\"X\", \"Y\")}");
      assertEquals("privateMethod", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
   public void testReferencedMethodWithWrongParameterNumbers() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${this.privateMethod(\"X\", \"Y\", \"Z\")}");
      template.process(map);
      fail();
    } catch (TemplateProcessingException ex) {
    } catch (Exception ex) {
      fail();
    }
  }
   public void testReferencedMethodWithWrongParameterTypes() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${this.privateMethod(0, 1)}");
      template.process(map);
      fail();
    } catch (TemplateProcessingException ex) {
    } catch (Exception ex) {
      fail();
    }
  }

  public void testReferencedMethodReturnFieldProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${this.nested().privateProperty}");
      assertEquals("privateProperty", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testReferencedMethodReturnGetterProperty() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${this.nested().getterProperty}");
      assertEquals("getterProperty", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testReferencedMethodReturnMethod() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      GroovyTemplate template = new GroovyTemplate("${this.nested().privateMethod(\"X\", \"Y\")}");
      assertEquals("privateMethod", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testAPIUse() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      GroovyTemplate template = new GroovyTemplate("${new java.lang.String(\"X\")}");
      assertEquals("X", template.process(map));
    } catch (TemplateProcessingException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }

  public void testPrimitiveTypeAritmeticOperatorSanityCheck() {
    try {
      Binding binding  = new Binding();
      binding.setVariable("x", 1);
      assertEquals("2", new GroovyShell(binding).evaluate("\"${x+x}\"").toString());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testBigDecimalTypeAritmeticOperatorSanityCheck() {
    try {
      Binding binding  = new Binding();
      binding.setVariable("x", new BigDecimal(1));
      assertEquals("2", new GroovyShell(binding).evaluate("\"${x+x}\"").toString());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testBigDecimalTypeAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new BigDecimal(1));
      GroovyTemplate template = new GroovyTemplate("${x + x}");
      assertEquals("2", template.process(map));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testBigDecimalTypeFieldAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean(new BigDecimal(1)));
      GroovyTemplate template = new GroovyTemplate("${x.y + x.y}");
      assertEquals("2", template.process(map));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testBigDecimalTypePropertyAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean(new BigDecimal(1)));
      GroovyTemplate template = new GroovyTemplate("${x.b + x.b}");
      assertEquals("2", template.process(map));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

   public void testBigDecimalTypeMethodReturnAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean(new BigDecimal(1)));
      GroovyTemplate template = new GroovyTemplate("${x.y()+x.y()}");
      assertEquals("2", template.process(map));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testPrimitiveTypeFieldAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean(1));
      GroovyTemplate template = new GroovyTemplate("${x.x + x.x}");
      assertEquals("2", template.process(map));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  public void testPrimitiveTypePropertyAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean(1));
      GroovyTemplate template = new GroovyTemplate("${x.a + x.a}");
      assertEquals("2", template.process(map));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testPrimitiveTypeMethodReturnAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean(1));
      GroovyTemplate template = new GroovyTemplate("${x.x() + x.x()}");
      assertEquals("2", template.process(map));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

    public void testStringTypeFieldConcatenation() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean("x"));
      GroovyTemplate template = new GroovyTemplate("${x.z+x.z}");
      assertEquals("xx", template.process(map));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testStringTypePropertyConcatenation() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean("x"));
      GroovyTemplate template = new GroovyTemplate("${x.c+x.c}");
      assertEquals("xx", template.process(map));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testStringTypeMethodReturnConcatenation() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", new PrimitiveAttributesBean("x"));
      GroovyTemplate template = new GroovyTemplate("${x.z() + x.z()}");
      assertEquals("xx", template.process(map));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testStringConstantConcatenationSanityCheck() {
    try {
      Binding binding  = new Binding();
      assertEquals("xx", new GroovyShell(binding).evaluate("\"${\"x\" + \"x\"}\"").toString());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testStringConcatenationSanityCheck() {
    try {
      Binding binding  = new Binding();
      binding.setVariable("x", "x");
      assertEquals("xx", new GroovyShell(binding).evaluate("\"${x + x}\"").toString());
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testStringConcatenation() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", "x");
      GroovyTemplate template = new GroovyTemplate("${x+x}");
      assertEquals("xx", template.process(map));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testStringConstantConcatenation() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      GroovyTemplate template = new GroovyTemplate("${\"x\"+\"x\"}");
      assertEquals("xx", template.process(map));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testPrimitiveTypeAritmeticOperator() {
    try {
      Map <String, Object> map = new HashMap <String, Object> ();
      map.put("x", 1);
      GroovyTemplate template = new GroovyTemplate("${x+x}");
      assertEquals("2", template.process(map));
    } catch (Exception ex) {
      fail();
    }
  }
}