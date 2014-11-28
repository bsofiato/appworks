/*
 * DirectFieldAccessStrategyTest.java
 * JUnit based test
 *
 * Created on 16 de Junho de 2005, 00:02
 */

package br.com.appworks.runtime.lang.support.property.getting.reflection.test;

import junit.framework.*;
import java.lang.reflect.Field;

import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.property.getting.reflection.DirectFieldPropertyGettingStrategy;

/**
 *
 * @author Bubu
 */
public class DirectFieldPropertyGettingStrategyTest extends TestCase {
  
  public static class BaseTestBean {
    private String privateBase;
    protected String protectedBase;
    String packageBase;
    public String publicBase;
  }
  
  public static class ChildTestBean extends BaseTestBean {
    private String privateChild;
    protected String protectedChild;
    String packageChild;
    public String publicChild;
  }
  
  public DirectFieldPropertyGettingStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testAccessBaseClassFields() {
    try {
      Field privateBaseField = BaseTestBean.class.getDeclaredField("privateBase");
      Field protectedBaseField = BaseTestBean.class.getDeclaredField("protectedBase");
      Field packageBaseField = BaseTestBean.class.getDeclaredField("packageBase");
      Field publicBaseField = BaseTestBean.class.getDeclaredField("publicBase");
      PropertyGettingStrategy <BaseTestBean, String> privateBasePropertyAccessStrategy = new DirectFieldPropertyGettingStrategy <BaseTestBean, String> (privateBaseField);
      PropertyGettingStrategy <BaseTestBean, String> protectedBasePropertyAccessStrategy = new DirectFieldPropertyGettingStrategy <BaseTestBean, String> (protectedBaseField);
      PropertyGettingStrategy <BaseTestBean, String> packageBasePropertyAccessStrategy = new DirectFieldPropertyGettingStrategy <BaseTestBean, String> (packageBaseField);
      PropertyGettingStrategy <BaseTestBean, String> publicBasePropertyAccessStrategy = new DirectFieldPropertyGettingStrategy <BaseTestBean, String> (publicBaseField);
      BaseTestBean base = new BaseTestBean();
      base.privateBase = "BASE.PRIVATE";
      base.protectedBase = "BASE.PROTECTED";
      base.packageBase = "BASE.PACKAGE";
      base.publicBase = "BASE.PUBLIC";
      assertTrue(privateBaseField.isAccessible());
      assertTrue(protectedBaseField.isAccessible());
      assertTrue(packageBaseField.isAccessible());
      assertTrue(publicBaseField.isAccessible());
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
      Field privateBaseField = BaseTestBean.class.getDeclaredField("privateBase");
      PropertyGettingStrategy privateBasePropertyAccessStrategy = new DirectFieldPropertyGettingStrategy (privateBaseField);
      privateBasePropertyAccessStrategy.get(new Object());
      fail();
    } catch (RuntimeException ex) {
      assertTrue(ex instanceof IllegalArgumentException);
    } catch (Exception ex) {
      fail();
    }
  }
  
   public void testIllegalAccessFields() {
    try {
      Field privateBaseField = BaseTestBean.class.getDeclaredField("privateBase");
      PropertyGettingStrategy privateBasePropertyAccessStrategy = new DirectFieldPropertyGettingStrategy (privateBaseField);
      privateBaseField.setAccessible(false);
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
      Field privateBaseField = BaseTestBean.class.getDeclaredField("privateBase");
      Field protectedBaseField = BaseTestBean.class.getDeclaredField("protectedBase");
      Field packageBaseField = BaseTestBean.class.getDeclaredField("packageBase");
      Field publicBaseField = BaseTestBean.class.getDeclaredField("publicBase");
      Field privateChildField = ChildTestBean.class.getDeclaredField("privateChild");
      Field protectedChildField = ChildTestBean.class.getDeclaredField("protectedChild");
      Field packageChildField = ChildTestBean.class.getDeclaredField("packageChild");
      Field publicChildField = ChildTestBean.class.getDeclaredField("publicChild");
      PropertyGettingStrategy <BaseTestBean, String> privateBasePropertyAccessStrategy = new DirectFieldPropertyGettingStrategy <BaseTestBean, String> (privateBaseField);
      PropertyGettingStrategy <BaseTestBean, String> protectedBasePropertyAccessStrategy = new DirectFieldPropertyGettingStrategy <BaseTestBean, String> (protectedBaseField);
      PropertyGettingStrategy <BaseTestBean, String> packageBasePropertyAccessStrategy = new DirectFieldPropertyGettingStrategy <BaseTestBean, String> (packageBaseField);
      PropertyGettingStrategy <BaseTestBean, String> publicBasePropertyAccessStrategy = new DirectFieldPropertyGettingStrategy <BaseTestBean, String> (publicBaseField);
      PropertyGettingStrategy <ChildTestBean, String> privateChildPropertyAccessStrategy = new DirectFieldPropertyGettingStrategy <ChildTestBean, String> (privateChildField);
      PropertyGettingStrategy <ChildTestBean, String> protectedChildPropertyAccessStrategy = new DirectFieldPropertyGettingStrategy <ChildTestBean, String> (protectedChildField);
      PropertyGettingStrategy <ChildTestBean, String> packageChildPropertyAccessStrategy = new DirectFieldPropertyGettingStrategy <ChildTestBean, String> (packageChildField);
      PropertyGettingStrategy <ChildTestBean, String> publicChildPropertyAccessStrategy = new DirectFieldPropertyGettingStrategy <ChildTestBean, String> (publicChildField);
      ChildTestBean child = new ChildTestBean();
      ((BaseTestBean) (child)).privateBase = "BASE.PRIVATE";
      child.protectedBase = "BASE.PROTECTED";
      child.packageBase = "BASE.PACKAGE";
      child.publicBase = "BASE.PUBLIC";
      child.privateChild = "CHILD.PRIVATE";
      child.protectedChild = "CHILD.PROTECTED";
      child.packageChild = "CHILD.PACKAGE";
      child.publicChild = "CHILD.PUBLIC";
      assertTrue(privateBaseField.isAccessible());
      assertTrue(protectedBaseField.isAccessible());
      assertTrue(packageBaseField.isAccessible());
      assertTrue(publicBaseField.isAccessible());
      assertTrue(privateChildField.isAccessible());
      assertTrue(protectedChildField.isAccessible());
      assertTrue(packageChildField.isAccessible());
      assertTrue(publicChildField.isAccessible());
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
  
  public void testGetProperty() {
    try {
      Field privateBaseField = BaseTestBean.class.getDeclaredField("privateBase");
      PropertyGettingStrategy privateBasePropertyAccessStrategy = new DirectFieldPropertyGettingStrategy (privateBaseField);
      assertEquals(privateBaseField.getName(), privateBasePropertyAccessStrategy.getProperty());
    } catch (Exception ex) {
      fail();
    }
  }

}
