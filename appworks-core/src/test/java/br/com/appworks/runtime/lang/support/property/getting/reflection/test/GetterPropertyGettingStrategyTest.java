/*
 * GetterPropertyAccessStrategyTest.java
 * JUnit based test
 *
 * Created on 16 de Junho de 2005, 00:23
 */

package br.com.appworks.runtime.lang.support.property.getting.reflection.test;

import junit.framework.*;
import java.lang.reflect.Method;
import br.com.appworks.runtime.lang.support.property.getting.reflection.GetterPropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;

/**
 *
 * @author Bubu
 */
public class GetterPropertyGettingStrategyTest extends TestCase {
  
  public GetterPropertyGettingStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public static class BaseTestBean {
    private String getPrivateBase() {
      return "BASE.PRIVATE";
    }
    protected String getProtectedBase() {
      return "BASE.PROTECTED";
    }
    String getPackageBase() {
      return "BASE.PACKAGE";
    }
    public String getPublicBase() {
      return "BASE.PUBLIC";
    }
  }
  
  public static class ChildTestBean extends BaseTestBean {
    private String getPrivateChild() {
      return "CHILD.PRIVATE";
    }
    protected String getProtectedChild() {
      return "CHILD.PROTECTED";
    }
    String getPackageChild() {
      return "CHILD.PACKAGE";
    }
    public String getPublicChild() {
      return "CHILD.PUBLIC";
    }
  }
  
  public static class ExceptionGetterTestBean {
    private final Exception exception;
    private String value;
    public ExceptionGetterTestBean(Exception exception) {
      this.exception = exception;
    }
    private String getValue() throws Exception {
      throw exception;
    }
  }
 
  public void testAccessBaseClassFields() {
    try {
      Method privateBaseMethod = BaseTestBean.class.getDeclaredMethod("getPrivateBase");
      Method protectedBaseMethod = BaseTestBean.class.getDeclaredMethod("getProtectedBase");
      Method packageBaseMethod = BaseTestBean.class.getDeclaredMethod("getPackageBase");
      Method publicBaseMethod = BaseTestBean.class.getDeclaredMethod("getPublicBase");
      PropertyGettingStrategy <BaseTestBean, String> privateBasePropertyAccessStrategy = new GetterPropertyGettingStrategy <BaseTestBean, String> (privateBaseMethod, null);
      PropertyGettingStrategy <BaseTestBean, String> protectedBasePropertyAccessStrategy = new GetterPropertyGettingStrategy <BaseTestBean, String> (protectedBaseMethod, null);
      PropertyGettingStrategy <BaseTestBean, String> packageBasePropertyAccessStrategy = new GetterPropertyGettingStrategy <BaseTestBean, String> (packageBaseMethod, null);
      PropertyGettingStrategy <BaseTestBean, String> publicBasePropertyAccessStrategy = new GetterPropertyGettingStrategy <BaseTestBean, String> (publicBaseMethod, null);
      BaseTestBean base = new BaseTestBean();
      assertTrue(privateBaseMethod.isAccessible());
      assertTrue(protectedBaseMethod.isAccessible());
      assertTrue(packageBaseMethod.isAccessible());
      assertTrue(publicBaseMethod.isAccessible());
      assertEquals("BASE.PRIVATE", privateBasePropertyAccessStrategy.get(base));
      assertEquals("BASE.PROTECTED", protectedBasePropertyAccessStrategy.get(base));
      assertEquals("BASE.PACKAGE", packageBasePropertyAccessStrategy.get(base));
      assertEquals("BASE.PUBLIC", publicBasePropertyAccessStrategy.get(base));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }
  
  public void testAccessWrongClassField() {
    try {
      Method privateBaseMethod = BaseTestBean.class.getDeclaredMethod("getPrivateBase");
      PropertyGettingStrategy privateBasePropertyAccessStrategy = new GetterPropertyGettingStrategy  (privateBaseMethod, null);
      privateBasePropertyAccessStrategy.get(new Object());
      fail();
    } catch (IllegalArgumentException ex) {
    } catch (Exception ex) {
      fail();
    }
  }

   public void testIllegalAccessFields() {
    try {
      Method privateBaseMethod = BaseTestBean.class.getDeclaredMethod("getPrivateBase");
      PropertyGettingStrategy privateBasePropertyAccessStrategy = new GetterPropertyGettingStrategy  (privateBaseMethod, null);
      privateBaseMethod.setAccessible(false);
      privateBasePropertyAccessStrategy.get(new Object());
      fail();
    } catch (RuntimeException ex) {
      assertTrue(ex.getCause() instanceof IllegalAccessException);
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }
  public void testAccessChildClassFields() {
    try {
      Method privateBaseMethod = BaseTestBean.class.getDeclaredMethod("getPrivateBase");
      Method protectedBaseMethod = BaseTestBean.class.getDeclaredMethod("getProtectedBase");
      Method packageBaseMethod = BaseTestBean.class.getDeclaredMethod("getPackageBase");
      Method publicBaseMethod = BaseTestBean.class.getDeclaredMethod("getPublicBase");
      Method privateChildMethod = ChildTestBean.class.getDeclaredMethod("getPrivateChild");
      Method protectedChildMethod = ChildTestBean.class.getDeclaredMethod("getProtectedChild");
      Method packageChildMethod = ChildTestBean.class.getDeclaredMethod("getPackageChild");
      Method publicChildMethod = ChildTestBean.class.getDeclaredMethod("getPublicChild");
      PropertyGettingStrategy <BaseTestBean, String> privateBasePropertyAccessStrategy = new GetterPropertyGettingStrategy <BaseTestBean, String> (privateBaseMethod, null);
      PropertyGettingStrategy <BaseTestBean, String> protectedBasePropertyAccessStrategy = new GetterPropertyGettingStrategy <BaseTestBean, String> (protectedBaseMethod, null);
      PropertyGettingStrategy <BaseTestBean, String> packageBasePropertyAccessStrategy = new GetterPropertyGettingStrategy <BaseTestBean, String> (packageBaseMethod, null);
      PropertyGettingStrategy <BaseTestBean, String> publicBasePropertyAccessStrategy = new GetterPropertyGettingStrategy <BaseTestBean, String> (publicBaseMethod, null);
      PropertyGettingStrategy <ChildTestBean, String> privateChildPropertyAccessStrategy = new GetterPropertyGettingStrategy <ChildTestBean, String> (privateChildMethod, null);
      PropertyGettingStrategy <ChildTestBean, String> protectedChildPropertyAccessStrategy = new GetterPropertyGettingStrategy <ChildTestBean, String> (protectedChildMethod, null);
      PropertyGettingStrategy <ChildTestBean, String> packageChildPropertyAccessStrategy = new GetterPropertyGettingStrategy <ChildTestBean, String> (packageChildMethod, null);
      PropertyGettingStrategy <ChildTestBean, String> publicChildPropertyAccessStrategy = new GetterPropertyGettingStrategy <ChildTestBean, String> (publicChildMethod, null);
      ChildTestBean child = new ChildTestBean();
      assertTrue(privateBaseMethod.isAccessible());
      assertTrue(protectedBaseMethod.isAccessible());
      assertTrue(packageBaseMethod.isAccessible());
      assertTrue(publicBaseMethod.isAccessible());
      assertTrue(privateChildMethod.isAccessible());
      assertTrue(protectedChildMethod.isAccessible());
      assertTrue(packageChildMethod.isAccessible());
      assertTrue(publicChildMethod.isAccessible());
      assertEquals("BASE.PRIVATE", privateBasePropertyAccessStrategy.get(child));
      assertEquals("BASE.PROTECTED", protectedBasePropertyAccessStrategy.get(child));
      assertEquals("BASE.PACKAGE", packageBasePropertyAccessStrategy.get(child));
      assertEquals("BASE.PUBLIC", publicBasePropertyAccessStrategy.get(child));
      assertEquals("CHILD.PRIVATE", privateChildPropertyAccessStrategy.get(child));
      assertEquals("CHILD.PROTECTED", protectedChildPropertyAccessStrategy.get(child));
      assertEquals("CHILD.PACKAGE", packageChildPropertyAccessStrategy.get(child));
      assertEquals("CHILD.PUBLIC", publicChildPropertyAccessStrategy.get(child));
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }
    
  public void testExceptionGetter() {
    Exception ex = new Exception();
    try {
      Method exceptionGetterMethod = ExceptionGetterTestBean.class.getDeclaredMethod("getValue");
      PropertyGettingStrategy <ExceptionGetterTestBean, String> exceptionPropertyAccessStrategy = new GetterPropertyGettingStrategy <ExceptionGetterTestBean, String> (exceptionGetterMethod, null);
      exceptionPropertyAccessStrategy.get(new ExceptionGetterTestBean(ex));
      fail();
    } catch (RuntimeException ex2) {
      assertSame(ex, ex2.getCause());
    } catch (Exception ex2) {
      fail();
    }
  }
  public void testGetProperty() {
    try {
      Method exceptionGetterMethod = ExceptionGetterTestBean.class.getDeclaredMethod("getValue");
      PropertyGettingStrategy <ExceptionGetterTestBean, String> exceptionPropertyAccessStrategy = new GetterPropertyGettingStrategy <ExceptionGetterTestBean, String> (exceptionGetterMethod, "TESTE");
      assertEquals("TESTE", exceptionPropertyAccessStrategy.getProperty());
    } catch (Exception ex2) {
      fail();
    }
  }

}
