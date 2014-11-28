/*
 * PrivateMemberAwareMetaClassTest.java
 * JUnit based test
 *
 * Created on 2 de Outubro de 2005, 17:38
 */

package br.com.appworks.runtime.lang.support.expression.groovy.test;

import br.com.appworks.runtime.lang.support.expression.groovy.ExpressionBinding;
import br.com.appworks.runtime.lang.support.expression.groovy.PrivateMemberAwareMetaClass;
import groovy.lang.MetaClass;
import org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl;
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
public class PrivateMemberAwareMetaClassTest extends TestCase {
   public class PrimitiveAttributeTestBean {
     public int x;
     public BigDecimal y;
     public PrimitiveAttributeTestBean z;
     public String w;
     public PrimitiveAttributeTestBean(int x) {
       this.x = x;
     }
     public PrimitiveAttributeTestBean(BigDecimal y) {
       this.y = y;
     }
     public PrimitiveAttributeTestBean(PrimitiveAttributeTestBean z) {
       this.z = z;
     }
     public PrimitiveAttributeTestBean(String w) {
       this.w = w;
     }
     public int x() {
       return this.x;
     }
     public BigDecimal y() {
       return this.y;
     }
     public PrimitiveAttributeTestBean z() {
       return this.z;
     }
     public String w() {
       return this.w;
     }
   }

   public class TestBean {
    private RuntimeException error = new RuntimeException();
    private String privateProperty = "privateProperty";
    private String getterBasedProperty = "null";

    public String getGetterBasedProperty() {
      return "getterBasedProperty";
    }
    
    public String getErrorDerivedProperty() {
      throw error;
    }
    public String getDerivedProperty() {
      return "derivedProperty";
    }
    private String privateMethod() {
      return "privateMethodNoArgs";
    }
    private String exceptionMethod() {
      throw error;
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
  
  public PrivateMemberAwareMetaClassTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  public void testChildMethodInvoking() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      assertEquals("childPrivateMethod", metaClass.invokeMethod(new ChildTestBean(), "childPrivateMethod", new String [] { "XXXX", "XXXX" }));
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testChildMethodParameterTypeMismatch() {
    MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
    try {
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      metaClass.invokeMethod(new ChildTestBean(), "childPrivateMethod", new Integer [] { 1, 2 });
      fail();
    } catch (MissingMethodException ex) {
      assertEquals("childPrivateMethod", ex.getMethod());
    } catch (Exception ex) {
      fail();
    }
  }

   public void testChildMethodParameterNumberMismatch() {
    MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
    try {
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      metaClass.invokeMethod(new ChildTestBean(), "childPrivateMethod", new String [] { "1", "2" , "3" });
      fail();
    } catch (MissingMethodException ex) {
      assertEquals("childPrivateMethod", ex.getMethod());
    } catch (Exception ex) {
      fail();
    }
  }
   
  public void testExceptionMethodInvoking() {
    MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
    ChildTestBean bean = new ChildTestBean();
    try {
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      metaClass.invokeMethod(bean, "exceptionMethod", new String [0]);
      fail();
    } catch (RuntimeException ex) {
      assertSame(((TestBean)(bean)).error, ex.getCause());
    } catch (Exception ex) {
      fail();
    }
  }
   
   
  public void testParentMethodInvoking() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      assertEquals("privateMethod", metaClass.invokeMethod(new ChildTestBean(), "privateMethod", new String [] { "XXXX", "XXXX" }));
    } catch (Exception ex) {
      fail();
    } 
  }
   
  public void testParentNoArgsMethodInvoking() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      assertEquals("privateMethodNoArgs", metaClass.invokeMethod(new ChildTestBean(), "privateMethod", new String [0]));
    } catch (Exception ex) {
      fail();
    }
  }
  public void testUnknownMethodInvoking() {
    MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
    try {
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      metaClass.invokeMethod(new ChildTestBean(), "unknowMethod", new Object [0]);
      fail();
    } catch (MissingMethodException ex) {
      assertEquals("unknowMethod", ex.getMethod());
    } catch (Exception ex) {
      fail();
    } 
  }

  public void testUnknownPropertyFetchingByGetProperty() {
    MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
    try {
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      metaClass.getProperty(new ChildTestBean(), "unknowProperty");
      fail();
    } catch (MissingFieldException ex) {
      assertEquals("unknowProperty", ex.getField());
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } 
  }
  public void testChildPropertyFetchingByGetProperty() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      assertEquals("childPrivateProperty",  metaClass.getProperty(new ChildTestBean(), "childPrivateProperty"));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  public void testPropertyFetchingByGetProperty() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      assertEquals("privateProperty",  metaClass.getProperty(new ChildTestBean(), "privateProperty"));
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testUnknownPropertyFetchingByGetAttribute() {
    MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
    try {
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      metaClass.getAttribute(new ChildTestBean(), "unknowProperty");
      fail();
    } catch (MissingFieldException ex) {
      assertEquals("unknowProperty", ex.getField());
    } catch (Exception ex) {
      fail();
    }
  }

  public void testChildPropertyFetchingByGetAttribute() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      assertEquals("childPrivateProperty",  metaClass.getAttribute(new ChildTestBean(), "childPrivateProperty"));
    } catch (Exception ex) {
      fail();
    }
  }
  public void testPropertyFetchingByGetAttribute() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      assertEquals("privateProperty",  metaClass.getAttribute(new ChildTestBean(), "privateProperty"));
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testChildDerivedPropertyFetching() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      assertEquals("derivedProperty",  metaClass.getProperty(new ChildTestBean(), "derivedProperty"));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testChildErrorDerivedPropertyFetching() {
    MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
    ChildTestBean bean = new ChildTestBean();
    try {
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      metaClass.getProperty(bean, "errorDerivedProperty");
      fail();
    } catch (RuntimeException ex) {
    } catch (Exception ex) {
      fail();
    } 
  }

  public void testChildGetterBasedPropertyFetching() {
    MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
    ChildTestBean bean = new ChildTestBean();
    try {
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, ChildTestBean.class);
      assertEquals("getterBasedProperty", metaClass.getProperty(bean, "getterBasedProperty"));
    } catch (RuntimeException ex) {
    } catch (Exception ex) {
      fail();
    } 
  }
  
  public void testObjectPropertyMetaClassRegistryFieldAccess() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, PrimitiveAttributeTestBean.class);
      assertNotNull(metaClass.getProperty(new PrimitiveAttributeTestBean(new PrimitiveAttributeTestBean(0)), "z"));
      assertEquals(PrivateMemberAwareMetaClass.class, metaClassRegistry.getMetaClass(PrimitiveAttributeTestBean.class).getClass());
    } catch (Exception ex) {
      fail();
    }
  }
  
   public void testPrimitivePropertyMetaClassRegistryFieldAccess() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, PrimitiveAttributeTestBean.class);
      assertEquals(1, metaClass.getProperty(new PrimitiveAttributeTestBean(1), "x"));
      assertTrue(metaClassRegistry.getMetaClass(int.class) instanceof MetaClass);
    } catch (Exception ex) {
      fail();
    }
  }
  
   public void testNumberPropertyMetaClassRegistryFieldAccess() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, PrimitiveAttributeTestBean.class);
      assertEquals(new BigDecimal(1), metaClass.getProperty(new PrimitiveAttributeTestBean(new BigDecimal(1)), "y"));
      assertTrue(metaClassRegistry.getMetaClass(BigDecimal.class) instanceof MetaClass);
    } catch (Exception ex) {
      fail();
    }
  }  

   public void testStringPropertyMetaClassRegistryFieldAccess() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, PrimitiveAttributeTestBean.class);
      assertEquals("x", metaClass.getProperty(new PrimitiveAttributeTestBean("x"), "w"));
      assertTrue(metaClassRegistry.getMetaClass(String.class) instanceof MetaClass);
    } catch (Exception ex) {
      fail();
    }
  }  


   
  public void testObjectPropertyMetaClassRegistryMethodAccess() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, PrimitiveAttributeTestBean.class);
      assertNotNull(metaClass.invokeMethod(new PrimitiveAttributeTestBean(new PrimitiveAttributeTestBean(0)), "z", new Object[0]));
      assertEquals(PrivateMemberAwareMetaClass.class, metaClassRegistry.getMetaClass(PrimitiveAttributeTestBean.class).getClass());
    } catch (Exception ex) {
      fail();
    }
  }
  
   public void testPrimitivePropertyMetaClassRegistryMethodAccess() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, PrimitiveAttributeTestBean.class);
      assertEquals(1, metaClass.invokeMethod(new PrimitiveAttributeTestBean(1), "x", new Object[0]));
      assertTrue(metaClassRegistry.getMetaClass(int.class) instanceof MetaClass);
    } catch (Exception ex) {
      fail();
    }
  }
  
   public void testNumberPropertyMetaClassRegistryMethodAccess() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, PrimitiveAttributeTestBean.class);
      assertEquals(new BigDecimal(1), metaClass.invokeMethod(new PrimitiveAttributeTestBean(new BigDecimal(1)), "y", new Object[0]));
      assertTrue(metaClassRegistry.getMetaClass(BigDecimal.class) instanceof MetaClass);
    } catch (Exception ex) {
      fail();
    }
  }  

  public void testStringPropertyMetaClassRegistryMethodAccess() {
    try {
      MetaClassRegistryImpl metaClassRegistry = new MetaClassRegistryImpl();
      MetaClass metaClass = new PrivateMemberAwareMetaClass(metaClassRegistry, PrimitiveAttributeTestBean.class);
      assertEquals("x", metaClass.invokeMethod(new PrimitiveAttributeTestBean("x"), "w", new Object[0]));
      assertTrue(metaClassRegistry.getMetaClass(String.class) instanceof MetaClass);
    } catch (Exception ex) {
      fail();
    }
  }  

   
}
