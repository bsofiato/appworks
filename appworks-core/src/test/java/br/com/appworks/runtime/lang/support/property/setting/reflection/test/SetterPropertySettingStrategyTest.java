/*
 * SetterPropertySettingStrategyTest.java
 * JUnit based test
 *
 * Created on 13 de Julho de 2005, 17:28
 */

package br.com.appworks.runtime.lang.support.property.setting.reflection.test;

import br.com.appworks.runtime.lang.support.property.setting.PropertySettingStrategy;
import br.com.appworks.runtime.lang.support.property.setting.reflection.SetterPropertySettingStrategy;
import java.lang.reflect.Method;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
/**
 *
 * @author Bubu
 */
public class SetterPropertySettingStrategyTest extends TestCase {
  
  public SetterPropertySettingStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  public static class ExceptionSetterTestBean {
    private final Exception exception;
    private String value;
    public ExceptionSetterTestBean(Exception exception) {
      this.exception = exception;
    }
    private void setValue(String value) throws Exception {
      throw exception;
    }
  }
  public static class BaseTestBean {
    private String privateBase;
    private String protectedBase;
    private String packageBase;
    private String publicBase;
    
    private void setPrivateBase(String privateBase) {
      this.privateBase = privateBase;
    }
    protected void setProtectedBase(String protectedBase) {
      this.protectedBase = protectedBase;
    }
    void setPackageBase(String packageBase) {
      this.packageBase = packageBase;
    }
    public void setPublicBase(String publicBase) {
      this.publicBase = publicBase;
    }
  }
  
  public static class ChildTestBean extends BaseTestBean {
    private String privateChild;
    private String protectedChild;
    private String packageChild;
    private String publicChild;
    
    private void setPrivateChild(String privateChild) {
      this.privateChild = privateChild;
    }
    protected void setProtectedChild(String protectedChild) {
      this.protectedChild = protectedChild;
    }
    void setPackageChild(String packageChild) {
      this.packageChild = packageChild;
    }
    public void setPublicChild(String publicChild) {
      this.publicChild = publicChild;
    }
  }
 
  public void testAccessBaseClassFields() {
    try {
      Method privateBaseMethod = BaseTestBean.class.getDeclaredMethod("setPrivateBase", String.class);
      Method protectedBaseMethod = BaseTestBean.class.getDeclaredMethod("setProtectedBase", String.class);
      Method packageBaseMethod = BaseTestBean.class.getDeclaredMethod("setPackageBase", String.class);
      Method publicBaseMethod = BaseTestBean.class.getDeclaredMethod("setPublicBase", String.class);
      PropertySettingStrategy <BaseTestBean, String> privateBasePropertyAccessStrategy = new SetterPropertySettingStrategy <BaseTestBean, String> (privateBaseMethod, null);
      PropertySettingStrategy <BaseTestBean, String> protectedBasePropertyAccessStrategy = new SetterPropertySettingStrategy <BaseTestBean, String> (protectedBaseMethod, null);
      PropertySettingStrategy <BaseTestBean, String> packageBasePropertyAccessStrategy = new SetterPropertySettingStrategy <BaseTestBean, String> (packageBaseMethod, null);
      PropertySettingStrategy <BaseTestBean, String> publicBasePropertyAccessStrategy = new SetterPropertySettingStrategy <BaseTestBean, String> (publicBaseMethod, null);
      BaseTestBean base = new BaseTestBean();
      privateBasePropertyAccessStrategy.set(base, "BASE.PRIVATE");
      protectedBasePropertyAccessStrategy.set(base, "BASE.PROTECTED");
      packageBasePropertyAccessStrategy.set(base, "BASE.PACKAGE");
      publicBasePropertyAccessStrategy.set(base, "BASE.PUBLIC");
      assertTrue(privateBaseMethod.isAccessible());
      assertTrue(protectedBaseMethod.isAccessible());
      assertTrue(packageBaseMethod.isAccessible());
      assertTrue(publicBaseMethod.isAccessible());
      assertEquals("BASE.PRIVATE", base.privateBase);
      assertEquals("BASE.PROTECTED", base.protectedBase);      
      assertEquals("BASE.PACKAGE", base.packageBase);
      assertEquals("BASE.PUBLIC", base.publicBase);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }
  
  public void testAccessWrongClassFields() {
    try {
      Method privateBaseMethod = BaseTestBean.class.getDeclaredMethod("setPrivateBase", String.class);
      PropertySettingStrategy privateBasePropertyAccessStrategy = new SetterPropertySettingStrategy (privateBaseMethod, null);
      privateBasePropertyAccessStrategy.set(new Object(), "XXX");
      fail();
    } catch (IllegalArgumentException ex) {
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }
  
  public void testIllegalAccessFields() {
    try {
      Method privateBaseMethod = BaseTestBean.class.getDeclaredMethod("setPrivateBase", String.class);
      PropertySettingStrategy privateBasePropertyAccessStrategy = new SetterPropertySettingStrategy (privateBaseMethod, null);
      privateBaseMethod.setAccessible(false);
      privateBasePropertyAccessStrategy.set(new Object(), "XXX");
      fail();
    } catch (RuntimeException ex) {
      assertTrue(ex.getCause() instanceof IllegalAccessException);
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }


  public void testExceptionSetter() {
    Exception ex = new Exception();
    try {
      Method exceptionSetterMethod = ExceptionSetterTestBean.class.getDeclaredMethod("setValue", String.class);
      PropertySettingStrategy <ExceptionSetterTestBean, String> exceptionPropertyAccessStrategy = new SetterPropertySettingStrategy <ExceptionSetterTestBean, String> (exceptionSetterMethod, null);
      exceptionPropertyAccessStrategy.set(new ExceptionSetterTestBean(ex), "TEST");
      fail();
    } catch (RuntimeException ex2) {
      assertSame(ex, ex2.getCause());
    } catch (Exception ex2) {
      fail();
    }
  }
  
  public void testAccessChildClassFields() {
    try {
      Method privateBaseMethod = BaseTestBean.class.getDeclaredMethod("setPrivateBase", String.class);
      Method protectedBaseMethod = BaseTestBean.class.getDeclaredMethod("setProtectedBase", String.class);
      Method packageBaseMethod = BaseTestBean.class.getDeclaredMethod("setPackageBase", String.class);
      Method publicBaseMethod = BaseTestBean.class.getDeclaredMethod("setPublicBase", String.class);
      Method privateChildMethod = ChildTestBean.class.getDeclaredMethod("setPrivateChild", String.class);
      Method protectedChildMethod = ChildTestBean.class.getDeclaredMethod("setProtectedChild", String.class);
      Method packageChildMethod = ChildTestBean.class.getDeclaredMethod("setPackageChild", String.class);
      Method publicChildMethod = ChildTestBean.class.getDeclaredMethod("setPublicChild", String.class);
      PropertySettingStrategy <BaseTestBean, String> privateBasePropertyAccessStrategy = new SetterPropertySettingStrategy <BaseTestBean, String> (privateBaseMethod, null);
      PropertySettingStrategy <BaseTestBean, String> protectedBasePropertyAccessStrategy = new SetterPropertySettingStrategy <BaseTestBean, String> (protectedBaseMethod, null);
      PropertySettingStrategy <BaseTestBean, String> packageBasePropertyAccessStrategy = new SetterPropertySettingStrategy <BaseTestBean, String> (packageBaseMethod, null);
      PropertySettingStrategy <BaseTestBean, String> publicBasePropertyAccessStrategy = new SetterPropertySettingStrategy <BaseTestBean, String> (publicBaseMethod, null);
      PropertySettingStrategy <ChildTestBean, String> privateChildPropertyAccessStrategy = new SetterPropertySettingStrategy <ChildTestBean, String> (privateChildMethod, null);
      PropertySettingStrategy <ChildTestBean, String> protectedChildPropertyAccessStrategy = new SetterPropertySettingStrategy <ChildTestBean, String> (protectedChildMethod, null);
      PropertySettingStrategy <ChildTestBean, String> packageChildPropertyAccessStrategy = new SetterPropertySettingStrategy <ChildTestBean, String> (packageChildMethod, null);
      PropertySettingStrategy <ChildTestBean, String> publicChildPropertyAccessStrategy = new SetterPropertySettingStrategy <ChildTestBean, String> (publicChildMethod, null);
      ChildTestBean child = new ChildTestBean();
      privateBasePropertyAccessStrategy.set(child, "BASE.PRIVATE");
      protectedBasePropertyAccessStrategy.set(child, "BASE.PROTECTED");
      packageBasePropertyAccessStrategy.set(child, "BASE.PACKAGE");
      publicBasePropertyAccessStrategy.set(child, "BASE.PUBLIC");
      privateChildPropertyAccessStrategy.set(child, "CHILD.PRIVATE");
      protectedChildPropertyAccessStrategy.set(child, "CHILD.PROTECTED");
      packageChildPropertyAccessStrategy.set(child, "CHILD.PACKAGE");
      publicChildPropertyAccessStrategy.set(child, "CHILD.PUBLIC");
      assertTrue(privateBaseMethod.isAccessible());
      assertTrue(protectedBaseMethod.isAccessible());
      assertTrue(packageBaseMethod.isAccessible());
      assertTrue(publicBaseMethod.isAccessible());
      assertTrue(privateChildMethod.isAccessible());
      assertTrue(protectedChildMethod.isAccessible());
      assertTrue(packageChildMethod.isAccessible());
      assertTrue(publicChildMethod.isAccessible());
      assertEquals("BASE.PRIVATE", ((BaseTestBean)(child)).privateBase);
      assertEquals("BASE.PROTECTED", ((BaseTestBean)(child)).protectedBase);      
      assertEquals("BASE.PACKAGE", ((BaseTestBean)(child)).packageBase);
      assertEquals("BASE.PUBLIC", ((BaseTestBean)(child)).publicBase);
      assertEquals("CHILD.PRIVATE", child.privateChild);
      assertEquals("CHILD.PROTECTED", child.protectedChild);      
      assertEquals("CHILD.PACKAGE", child.packageChild);
      assertEquals("CHILD.PUBLIC", child.publicChild);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }
  
  public void testGetProperty() {
    try {
      Method privateBaseMethod = BaseTestBean.class.getDeclaredMethod("setPrivateBase", String.class);
      PropertySettingStrategy <BaseTestBean, String> privateBasePropertyAccessStrategy = new SetterPropertySettingStrategy <BaseTestBean, String> (privateBaseMethod, "TESTE");
      assertEquals("TESTE", privateBasePropertyAccessStrategy.getProperty());
    } catch (Exception ex2) {
      fail();
    }
  }
}
