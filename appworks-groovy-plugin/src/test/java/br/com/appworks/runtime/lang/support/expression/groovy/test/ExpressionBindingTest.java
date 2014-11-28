/*
 * ExpressionContextTest.java
 * JUnit based test
 *
 * Created on 1 de Outubro de 2005, 19:04
 */

package br.com.appworks.runtime.lang.support.expression.groovy.test;
import br.com.appworks.runtime.lang.support.expression.groovy.ExpressionBinding;
import br.com.appworks.runtime.lang.support.expression.groovy.PrivateMemberAwareMetaClass;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;
import groovy.lang.MissingFieldException;
import groovy.lang.MissingMethodException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;
import org.codehaus.groovy.runtime.InvokerHelper;


/**
 *
 * @author Bubu
 */
public class ExpressionBindingTest extends TestCase {
  public class TestBean {
    private String privateProperty = "privateProperty";
    private String getterBasedProperty = "null";

    public String getGetterBasedProperty() {
      return "getterBasedProperty";
    }

    public String getErrorDerivedProperty() {
      throw new RuntimeException();
    }
    public String getDerivedProperty() {
      return "derivedProperty";
    }
    private String privateMethod() {
      return "privateMethodNoArgs";
    }
    private String exceptionMethod() {
      throw new RuntimeException();
    }
    private String privateMethod(String arg1, String arg2) {
      return "privateMethod";
    }
  }
  public class ChildTestBean extends TestBean {
    private String childPrivateProperty = "childPrivateProperty";
    private String childPrivateMethod(String arg1, String arg2) {
      return "childPrivateMethod";
    }
  }
  
  public ExpressionBindingTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testThisFetching() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new Object());
      assertSame(map.get("this"), new ExpressionBinding(map).getVariable("this"));
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testNullThisFetching() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("privateProperty", new Object());
      assertSame(map.get("privateProperty"), new ExpressionBinding(map).getVariable("privateProperty"));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testThisPropertyFetching() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      assertEquals("privateProperty", new ExpressionBinding(map).getVariable("privateProperty"));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } 
  }
  
  public void testChildBeanParentPropertyFetching() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      assertEquals("privateProperty", new ExpressionBinding(map).getVariable("privateProperty"));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } 
  }
  
  public void testChildBeanPropertyFetching() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      assertEquals("childPrivateProperty", new ExpressionBinding(map).getVariable("childPrivateProperty"));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } 
  }
  
  
  public void testThisPropertyFetchingWithUnknownField() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      new ExpressionBinding(map).getVariable("unknownField");
      fail();
    } catch (MissingFieldException ex) {
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } 
  }

  public void testChildPropertyFetchingWithUnknownField() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      new ExpressionBinding(map).getVariable("unknownField");
      fail();
    } catch (MissingFieldException ex) {
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } 
  }

  public void testThisPropertyFetchingOverridingField() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      map.put("privateProperty", "OK");

      assertEquals("OK", new ExpressionBinding(map).getVariable("privateProperty"));
    } catch (Exception ex) {
      fail();
    }
  }

   public void testChildThisParentPropertyFetchingOverridingField() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      map.put("privateProperty", "OK");

      assertEquals("OK", new ExpressionBinding(map).getVariable("privateProperty"));
    } catch (Exception ex) {
      fail();
    }
  }

   public void testChildThisPropertyFetchingOverridingField() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      map.put("childPrivateProperty", "OK");

      assertEquals("OK", new ExpressionBinding(map).getVariable("childPrivateProperty"));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testPrivateMethodNoArgsInvocation() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      assertEquals("privateMethodNoArgs", new ExpressionBinding(map).invokeMethod("privateMethod", new Object [0]));
    } catch (Exception ex) {
      fail();
    }
  }

   public void testChildParentPrivateMethodNoArgsInvocation() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      assertEquals("privateMethodNoArgs", new ExpressionBinding(map).invokeMethod("privateMethod", new Object [0]));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testPrivateMethodInvocation() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      assertEquals("privateMethod", new ExpressionBinding(map).invokeMethod("privateMethod", new Object [] {"XXXX", "XXXX"}));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testChildPrivateMethodInvocation() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      assertEquals("childPrivateMethod", new ExpressionBinding(map).invokeMethod("childPrivateMethod", new Object [] {"XXXX", "XXXX"}));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testPrivateMethodWithNullParametersInvocation() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      assertEquals("privateMethod", new ExpressionBinding(map).invokeMethod("privateMethod", new String [] {null, null}));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testChildPrivateMethodWithNullParametersInvocation() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      assertEquals("childPrivateMethod", new ExpressionBinding(map).invokeMethod("childPrivateMethod", new String [] {null, null}));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testUnknownMethodInvocation() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      new ExpressionBinding(map).invokeMethod("unknowMethod", new Object [0]);
      fail();
    } catch (MissingMethodException ex) {
      assertEquals("unknowMethod", ex.getMethod());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testChildUnknownMethodInvocation() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      new ExpressionBinding(map).invokeMethod("unknowMethod", new Object [0]);
      fail();
    } catch (MissingMethodException ex) {
      assertEquals("unknowMethod", ex.getMethod());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testParentMethodInvocation() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      assertEquals(TestBean.class, new ExpressionBinding(map).invokeMethod("getClass", new Object [0]));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testPrivateMethodInvocationArgumentNumberMismatch() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      new ExpressionBinding(map).invokeMethod("privateMethod", new Object [] { "TESTE" });
      fail();
    } catch (MissingMethodException ex) {
      assertEquals("privateMethod", ex.getMethod());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testPrivateMethodInvocationArgumentTypeMismatch() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new TestBean());
      new ExpressionBinding(map).invokeMethod("privateMethod", new Object [] { 2, 3 });
      fail();
    } catch (MissingMethodException ex) {
      assertEquals("privateMethod", ex.getMethod());
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testChildDerivedPropertyFetching() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      assertEquals("derivedProperty", new ExpressionBinding(map).getProperty("derivedProperty"));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testChildDerivedPropertyFetchingWithParameterWithSameName() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      map.put("derivedProperty", "X");
      assertEquals("X", new ExpressionBinding(map).getProperty("derivedProperty"));
    } catch (Exception ex) {
      fail();
    }
  }


  public void testChildErrorDerivedPropertyFetching() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      assertEquals("errorDerivedProperty", new ExpressionBinding(map).getProperty("errorDerivedProperty"));
      fail();
    } catch (RuntimeException ex) {
    } catch (Exception ex) {
      fail();
    } 
  }
  
  public void testChildErrorDerivedPropertyFetchingWithParameterWithSameName() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      map.put("errorDerivedProperty", "X");
      assertEquals("X", new ExpressionBinding(map).getProperty("errorDerivedProperty"));
    } catch (Exception ex) {
      fail();
    } 
  }
  
   public void testChildGetterBasedPropertyFetching() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      assertEquals("getterBasedProperty", new ExpressionBinding(map).getProperty("getterBasedProperty"));
    } catch (Exception ex) {
      fail();
    }
  }
   
  public void testChildGetterBasedPropertyFetchingWithParameterWithSameName() {
    try {
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("this", new ChildTestBean());
      map.put("getterBasedProperty", "X");
      assertEquals("X", new ExpressionBinding(map).getProperty("getterBasedProperty"));
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testObjectPropertyMetaClassRegistry() {
    try {
      MetaClassRegistry metaClassRegistry = InvokerHelper.getMetaRegistry();
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("x", new TestBean());
      new ExpressionBinding(map).getProperty("x");
      assertEquals(metaClassRegistry.getMetaClass(TestBean.class).getClass() , PrivateMemberAwareMetaClass.class);
    } catch (Exception ex) {
      fail();
    }
  }
  
   public void testPrimitivePropertyMetaClassRegistry() {
    try {
      MetaClassRegistry metaClassRegistry = InvokerHelper.getMetaRegistry();
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("x", 1);
      new ExpressionBinding(map).getProperty("x");
      assertTrue(metaClassRegistry.getMetaClass(int.class) instanceof MetaClass);
    } catch (Exception ex) {
      fail();
    }
  }
   
   public void testNumberPropertyMetaClassRegistry() {
    try {
      MetaClassRegistry metaClassRegistry = InvokerHelper.getMetaRegistry();
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("x", new BigDecimal(1));
      new ExpressionBinding(map).getProperty("x");
      assertTrue(metaClassRegistry.getMetaClass(BigDecimal.class) instanceof MetaClass);
    } catch (Exception ex) {
      fail();
    }
  }

   public void testStringPropertyMetaClassRegistry() {
    try {
      MetaClassRegistry metaClassRegistry = InvokerHelper.getMetaRegistry();
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("x", "x");
      new ExpressionBinding(map).getProperty("x");
      assertTrue(metaClassRegistry.getMetaClass(String.class) instanceof MetaClass);
    } catch (Exception ex) {
      fail();
    }
  }

  public void testObjectVariableMetaClassRegistry() {
    try {
      MetaClassRegistry metaClassRegistry = InvokerHelper.getMetaRegistry();
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("x", new TestBean());
      new ExpressionBinding(map).getVariable("x");
      assertEquals(metaClassRegistry.getMetaClass(TestBean.class).getClass() , PrivateMemberAwareMetaClass.class);
    } catch (Exception ex) {
      fail();
    }
  }
  
   public void testPrimitiveVariableMetaClassRegistry() {
    try {
      MetaClassRegistry metaClassRegistry = InvokerHelper.getMetaRegistry();
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("x", 1);
      new ExpressionBinding(map).getVariable("x");
      assertTrue(metaClassRegistry.getMetaClass(int.class) instanceof MetaClass);
    } catch (Exception ex) {
      fail();
    }
  }
   
   public void testNumberVariableMetaClassRegistry() {
    try {
      MetaClassRegistry metaClassRegistry = InvokerHelper.getMetaRegistry();
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("x", new BigDecimal(1));
      new ExpressionBinding(map).getVariable("x");
      assertTrue(metaClassRegistry.getMetaClass(BigDecimal.class) instanceof MetaClass);
    } catch (Exception ex) {
      fail();
    }
  }

   public void testStringVariableMetaClassRegistry() {
    try {
      MetaClassRegistry metaClassRegistry = InvokerHelper.getMetaRegistry();
      Map <String, Object> map  = new HashMap <String, Object> ();
      map.put("x", "x");
      new ExpressionBinding(map).getVariable("x");
      assertTrue(metaClassRegistry.getMetaClass(String.class) instanceof MetaClass);
    } catch (Exception ex) {
      fail();
    }
  }
   
  public void testSetVariables() {
    Map <String, Object> variables= new HashMap <String, Object> ();
    variables.put("x", "y");
    ExpressionBinding binding = new ExpressionBinding(new HashMap <String, Object> ());
    assertTrue(binding.getVariables().isEmpty());
    binding.setVariables(variables);
    assertEquals(variables, binding.getVariables());
    binding.setVariables(null);
    assertTrue(binding.getVariables().isEmpty());
  }

}
