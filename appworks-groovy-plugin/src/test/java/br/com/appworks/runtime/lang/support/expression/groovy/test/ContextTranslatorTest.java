/*
 * ContextTranslatorTest.java
 * JUnit based test
 *
 * Created on 1 de Outubro de 2005, 15:31
 */

package br.com.appworks.runtime.lang.support.expression.groovy.test;

import br.com.appworks.runtime.lang.support.expression.groovy.ContextTranslator;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class ContextTranslatorTest extends TestCase {
  
  public ContextTranslatorTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNullContextTranslation() {
    assertTrue(new ContextTranslator().translate((Map <String, Object>)(null)).isEmpty());
  }
  public void testEmptyContextTranslation() {
    Map <String, Object> context =  new HashMap <String, Object> ();
    assertNotSame(context, new ContextTranslator().translate(context));
    assertEquals(context , new ContextTranslator().translate(context));
  }
  public void testWithoutThisWithoutReturnContextTranslation() {
    Map <String, Object> context =  new HashMap <String, Object> ();
    context.put("x", new Object());
    context.put("y", new Object());
    context.put("z", null);
    
    assertNotSame(context, new ContextTranslator().translate(context));
    assertEquals(context , new ContextTranslator().translate(context));
  }
  
  public void testWithoutThisWithNullReturnContextTranslation() {
    Map <String, Object> context =  new HashMap <String, Object> ();
    context.put("x", new Object());
    context.put("y", new Object());
    context.put("z", null);
    context.put("return", null);
    Map <String, Object> translatedContext = new ContextTranslator().translate(context);
    assertNotSame(context, translatedContext);
    assertEquals(context.size() + 1, translatedContext.size());
    for (Map.Entry <String, Object> entry : translatedContext.entrySet()) {
      if (!entry.getKey().startsWith("return")) {
        assertTrue(context.containsKey(entry.getKey()));
        assertSame(entry.getValue(), context.get(entry.getKey()));
      } else {
        assertSame(entry.getValue(), context.get("return"));
      }
    }
  }
  
  public void testWithoutThisContextTranslation() {
    Map <String, Object> context =  new HashMap <String, Object> ();
    context.put("x", new Object());
    context.put("y", new Object());
    context.put("z", null);
    context.put("return", new Object());
    Map <String, Object> translatedContext = new ContextTranslator().translate(context);
    assertNotSame(context, translatedContext);
    assertEquals(context.size() + 1, translatedContext.size());
    for (Map.Entry <String, Object> entry : translatedContext.entrySet()) {
      if (!entry.getKey().startsWith("return")) {
        assertTrue(context.containsKey(entry.getKey()));
        assertSame(entry.getValue(), context.get(entry.getKey()));
      } else {
        assertSame(entry.getValue(), context.get("return"));
      }
    }
  }

  public void testWithNullThisContextTranslation() {
    Map <String, Object> context =  new HashMap <String, Object> ();
    context.put("x", new Object());
    context.put("y", new Object());
    context.put("z", null);
    context.put("return", new Object());
    context.put("this", null);
    Map <String, Object> translatedContext = new ContextTranslator().translate(context);
    assertNotSame(context, translatedContext);
    assertEquals(context.size() + 2, translatedContext.size());
    for (Map.Entry <String, Object> entry : translatedContext.entrySet()) {
      if (entry.getKey().startsWith("return")) {
        assertSame(entry.getValue(), context.get("return"));
      } else if (entry.getKey().startsWith("this")) {
        assertSame(entry.getValue(), context.get("this"));
      } else {
        assertTrue(context.containsKey(entry.getKey()));
        assertSame(entry.getValue(), context.get(entry.getKey()));
      }
    }
  }

  public void testContextTranslation() {
    Map <String, Object> context =  new HashMap <String, Object> ();
    context.put("x", new Object());
    context.put("y", new Object());
    context.put("z", null);
    context.put("return", new Object());
    context.put("this", new Object());
    Map <String, Object> translatedContext = new ContextTranslator().translate(context);
    assertNotSame(context, translatedContext);
    assertEquals(context.size() + 2, translatedContext.size());
    for (Map.Entry <String, Object> entry : translatedContext.entrySet()) {
      if (entry.getKey().startsWith("return")) {
        assertSame(entry.getValue(), context.get("return"));
      } else if (entry.getKey().startsWith("this")) {
        assertSame(entry.getValue(), context.get("this"));
      } else {
        assertTrue(context.containsKey(entry.getKey()));
        assertSame(entry.getValue(), context.get(entry.getKey()));
      }
    }
  }

  public void testContextTranslationIdentifierCaching() {
    Map <String, Object> context =  new HashMap <String, Object> ();
    context.put("x", new Object());
    context.put("y", new Object());
    context.put("z", null);
    context.put("return", new Object());
    context.put("this", new Object());
    ContextTranslator translator = new ContextTranslator();
    assertNotSame(translator.translate(context), translator.translate(context));
    assertEquals(translator.translate(context), translator.translate(context));
  }
  
  public void testNullExpressionTranslation() {
    assertNull(new ContextTranslator().translate((String)(null)));
  }
  public void testWithoutKeyWordsExpressionTranslation() {
    assertEquals("true", new ContextTranslator().translate("true"));
  }
  public void testStartWithThisOnlyExpressionTranslation() {
    assertEquals("thisX + x", new ContextTranslator().translate("thisX + x"));
    assertEquals("thisX+x", new ContextTranslator().translate("thisX+x"));
  }
  public void testEndWithThisOnlyExpressionTranslation() {
    assertEquals("xthis + x", new ContextTranslator().translate("xthis + x"));
    assertEquals("xthis+x", new ContextTranslator().translate("xthis+x"));
  }
  public void testThisAloneExpressionTranslation() {
    assertFalse("this ".equals(new ContextTranslator().translate("this ")));
    assertFalse("(this)".equals(new ContextTranslator().translate("(this)")));
    assertFalse("this++".equals(new ContextTranslator().translate("this++")));
    assertFalse("--this".equals(new ContextTranslator().translate("--this")));
    assertFalse("this".equals(new ContextTranslator().translate("this")));
    assertFalse("this[0]".equals(new ContextTranslator().translate("this[0]")));
    assertFalse(" this".equals(new ContextTranslator().translate(" this ")));
    assertFalse(" this ".equals(new ContextTranslator().translate(" this ")));
  }
  public void testStartWithThisReferenceTranslation() {
    assertEquals("xthis.x", new ContextTranslator().translate("xthis.x"));
  }
  
  public void testEndWithThisReferenceTranslation() {
    assertEquals("thisX.x", new ContextTranslator().translate("thisX.x"));
  }
  
  public void testThisBetweenReferenceTranslation() {
    assertEquals("xthisX.x", new ContextTranslator().translate("xthisX.x"));
  }

  public void testStartWithThisPointerTranslation() {
    assertEquals("xthis->x", new ContextTranslator().translate("xthis->x"));
  }
  
  public void testEndWithThisPointerTranslation() {
    assertEquals("thisX->x", new ContextTranslator().translate("thisX->x"));
  }
  
  public void testThisBetweenPointerTranslation() {
    assertEquals("xthisX->x", new ContextTranslator().translate("xthisX->x"));
  }
  
  public void testThisReferenceTranslation() {
    assertFalse("this.x".equals(new ContextTranslator().translate("this.x")));
  }
  public void testThisPointerTranslation() {
    assertFalse("this->x".equals(new ContextTranslator().translate("this->x")));
  }
  public void testThisReferenceTranslationBetweenParenthesis() {
    assertFalse("(this.x)".equals(new ContextTranslator().translate("(this.x)")));
    assertTrue(-1 != new ContextTranslator().translate("(this.x)").indexOf("("));
    assertTrue(-1 != new ContextTranslator().translate("(this.x)").indexOf(")"));
  }
  public void testThisPointerTranslationBetweenParenthesis() {
    assertFalse("(this->x)".equals(new ContextTranslator().translate("(this->x)")));
    assertTrue(-1 != new ContextTranslator().translate("(this->x)").indexOf("("));
    assertTrue(-1 != new ContextTranslator().translate("(this->x)").indexOf(")"));
  }

  public void testThisTranslationContextConsistence() {
    Map <String, Object> context =  new HashMap <String, Object> ();
    context.put("this", new Object());
    ContextTranslator translator = new ContextTranslator();
    assertSame(context.get("this"), translator.translate(context).get(translator.translate("this")));
  }

  
  public void testStartWithReturnOnlyExpressionTranslation() {
    assertEquals("returnX + x", new ContextTranslator().translate("returnX + x"));
    assertEquals("returnX+x", new ContextTranslator().translate("returnX+x"));
  }
  public void testEndWithReturnOnlyExpressionTranslation() {
    assertEquals("xreturn + x", new ContextTranslator().translate("xreturn + x"));
    assertEquals("xreturn+x", new ContextTranslator().translate("xreturn+x"));
  }
  public void testReturnAloneExpressionTranslation() {
    assertFalse("return ".equals(new ContextTranslator().translate("return ")));
    assertFalse("(return)".equals(new ContextTranslator().translate("(return)")));
    assertFalse("return++".equals(new ContextTranslator().translate("return++")));
    assertFalse("--return".equals(new ContextTranslator().translate("--return")));
    assertFalse("return".equals(new ContextTranslator().translate("return ")));
    assertFalse(" return".equals(new ContextTranslator().translate(" return ")));
    assertFalse(" return ".equals(new ContextTranslator().translate(" return ")));
    assertFalse("return[]".equals(new ContextTranslator().translate("return[]")));

  }
  public void testStartWithReturnReferenceTranslation() {
    assertEquals("xreturn.x", new ContextTranslator().translate("xreturn.x"));
  }
  
  public void testEndWithReturnReferenceTranslation() {
    assertEquals("returnX.x", new ContextTranslator().translate("returnX.x"));
  }
  
  public void testReferenceBetweenReferenceTranslation() {
    assertEquals("xreturnX.x", new ContextTranslator().translate("xreturnX.x"));
  }

  public void testStartWithReturnPointerTranslation() {
    assertEquals("xreturn->x", new ContextTranslator().translate("xreturn->x"));
  }
  
  public void testEndWithReturnPointerTranslation() {
    assertEquals("returnX->x", new ContextTranslator().translate("returnX->x"));
  }
  
  public void testReturnBetweenPointerTranslation() {
    assertEquals("xreturnX->x", new ContextTranslator().translate("xreturnX->x"));
  }
  
  public void testReturnReferenceTranslation() {
    assertFalse("return.x".equals(new ContextTranslator().translate("return.x")));
  }
  public void testReturnPointerTranslation() {
    assertFalse("return->x".equals(new ContextTranslator().translate("return->x")));
  }
  public void testReturnReferenceTranslationBetweenParenthesis() {
    assertFalse("(return.x)".equals(new ContextTranslator().translate("(return.x)")));
    assertTrue(-1 != new ContextTranslator().translate("(return.x)").indexOf("("));
    assertTrue(-1 != new ContextTranslator().translate("(return.x)").indexOf(")"));
  }
  public void testReturnPointerTranslationBetweenParenthesis() {
    assertFalse("(return->x)".equals(new ContextTranslator().translate("(return->x)")));
    assertTrue(-1 != new ContextTranslator().translate("(return->x)").indexOf("("));
    assertTrue(-1 != new ContextTranslator().translate("(return->x)").indexOf(")"));
  }
  public void testWithReferenceMethodTranslation() {
    assertEquals("x.method()", new ContextTranslator().translate("x.method()"));
  }
  public void testWithPointerMethodTranslation() {
    assertEquals("x->method()", new ContextTranslator().translate("x->method()"));
  }
  public void testWithoutReferenceMethodTranslation() {
    ContextTranslator translator = new ContextTranslator();
    assertEquals(translator.translate("method()"), translator.translate("this.method()"));
  }
  public void testWithoutReferenceSubMethodTranslation() {
    ContextTranslator translator = new ContextTranslator();
    assertEquals(translator.translate("method().test"), translator.translate("this.method().test"));
    assertEquals(translator.translate("method().test.test"), translator.translate("this.method().test.test"));
  }
  public void testWithoutReferenceWithOperandTranslation() {
    ContextTranslator translator = new ContextTranslator();
    assertEquals(translator.translate("method()==method()"), translator.translate("this.method()==this.method()"));
    assertEquals(translator.translate("method().test==method().test"), translator.translate("this.method().test==this.method().test"));
  }

  public void testWithoutReferenceSubMethodPointerTranslation() {
    ContextTranslator translator = new ContextTranslator();
    assertEquals(translator.translate("method()->test"), translator.translate("this.method()->test"));
    assertEquals(translator.translate("method()->test->test"), translator.translate("this.method()->test->test"));
  }
  public void testWithoutReferenceSubMethodWithOperandTranslation() {
    ContextTranslator translator = new ContextTranslator();
    assertEquals(translator.translate("method()->test==method()->test"), translator.translate("this.method()->test==this.method()->test"));
    assertEquals(translator.translate("method()->test->test==method()->test->test"), translator.translate("this.method()->test->test==this.method()->test->test"));
  }
  public void testWithReferenceAritimethicOperation() {
    ContextTranslator translator = new ContextTranslator();
    assertEquals("x.z()+x.z()", translator.translate("x.z()+x.z()"));
    assertEquals("x.z() + x.z()", translator.translate("x.z() + x.z()"));
  }

  public void testReturnTranslationContextConsistence() {
    Map <String, Object> context =  new HashMap <String, Object> ();
    context.put("return", new Object());
    ContextTranslator translator = new ContextTranslator();
    assertSame(context.get("return"), translator.translate(context).get(translator.translate("return")));
  }
  public void testReturnBeforeOperator() {
    ContextTranslator translator = new ContextTranslator();
    assertFalse("return!=x".equals(translator.translate("return!=x")));
  }
  public void testReturnAfterOperator() {
    ContextTranslator translator = new ContextTranslator();
    assertFalse("x!=return".equals(translator.translate("x!=return")));
  }
  public void testThisString() {
    ContextTranslator translator = new ContextTranslator();
    assertEquals("\"this\"", translator.translate("\"this\""));
  }
  public void testThisGStringExpression() {
    ContextTranslator translator = new ContextTranslator();
    assertFalse("\"${this}\"".equals(translator.translate("\"${this}\"")));
  }
 
  public void testReturnString() {
    ContextTranslator translator = new ContextTranslator();
    assertEquals("\"return\"", translator.translate("\"return\""));
  }

  public void testReturnGStringExpression() {
    ContextTranslator translator = new ContextTranslator();
    assertFalse("\"${return}\"".equals(translator.translate("\"${return}\"")));
  }
  
}
