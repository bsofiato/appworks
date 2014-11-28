/*
 * DirectFieldPropertySettingStrategyTest.java
 * JUnit based test
 *
 * Created on 13 de Julho de 2005, 17:20
 */

package br.com.appworks.runtime.lang.support.property.setting.reflection.test;

import br.com.appworks.runtime.lang.support.property.getting.reflection.DirectFieldPropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.property.setting.reflection.DirectFieldPropertySettingStrategy;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.property.setting.PropertySettingStrategy;
import java.lang.reflect.Field;
import junit.framework.*;

public class DirectFieldPropertySettingStrategyTest extends TestCase {
  
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
  
  public DirectFieldPropertySettingStrategyTest(String testName) {
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
      PropertySettingStrategy <BaseTestBean, String> privateBasePropertyAccessStrategy = new DirectFieldPropertySettingStrategy <BaseTestBean, String> (privateBaseField);
      PropertySettingStrategy <BaseTestBean, String> protectedBasePropertyAccessStrategy = new DirectFieldPropertySettingStrategy <BaseTestBean, String> (protectedBaseField);
      PropertySettingStrategy <BaseTestBean, String> packageBasePropertyAccessStrategy = new DirectFieldPropertySettingStrategy <BaseTestBean, String> (packageBaseField);
      PropertySettingStrategy <BaseTestBean, String> publicBasePropertyAccessStrategy = new DirectFieldPropertySettingStrategy <BaseTestBean, String> (publicBaseField);
      BaseTestBean base = new BaseTestBean();
      privateBasePropertyAccessStrategy.set(base, "BASE.PRIVATE");
      protectedBasePropertyAccessStrategy.set(base, "BASE.PROTECTED");
      packageBasePropertyAccessStrategy.set(base, "BASE.PACKAGE");
      publicBasePropertyAccessStrategy.set(base, "BASE.PUBLIC");
      
      assertTrue(privateBaseField.isAccessible());
      assertTrue(protectedBaseField.isAccessible());
      assertTrue(packageBaseField.isAccessible());
      assertTrue(publicBaseField.isAccessible());
      assertEquals("BASE.PRIVATE", base.privateBase);
      assertEquals("BASE.PROTECTED", base.protectedBase);
      assertEquals("BASE.PACKAGE", base.packageBase);
      assertEquals("BASE.PUBLIC", base.publicBase);
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }

  public void testAccessWrongClassFields() {
    try {
      Field privateBaseField = BaseTestBean.class.getDeclaredField("privateBase");
      PropertySettingStrategy privateBasePropertyAccessStrategy = new DirectFieldPropertySettingStrategy (privateBaseField);
      privateBasePropertyAccessStrategy.set(new Object(), "XXX");
      fail();
    } catch (RuntimeException ex) {
      assertTrue(ex instanceof IllegalArgumentException);
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }
  
  public void testIllegalAccessFields() {
    try {
      Field privateBaseField = BaseTestBean.class.getDeclaredField("privateBase");
      PropertySettingStrategy privateBasePropertyAccessStrategy = new DirectFieldPropertySettingStrategy (privateBaseField);
      privateBaseField.setAccessible(false);
      privateBasePropertyAccessStrategy.set(new Object(), "XXX");
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
      PropertySettingStrategy <BaseTestBean, String> privateBasePropertyAccessStrategy = new DirectFieldPropertySettingStrategy <BaseTestBean, String> (privateBaseField);
      PropertySettingStrategy <BaseTestBean, String> protectedBasePropertyAccessStrategy = new DirectFieldPropertySettingStrategy <BaseTestBean, String> (protectedBaseField);
      PropertySettingStrategy <BaseTestBean, String> packageBasePropertyAccessStrategy = new DirectFieldPropertySettingStrategy <BaseTestBean, String> (packageBaseField);
      PropertySettingStrategy <BaseTestBean, String> publicBasePropertyAccessStrategy = new DirectFieldPropertySettingStrategy <BaseTestBean, String> (publicBaseField);
      PropertySettingStrategy <ChildTestBean, String> privateChildPropertyAccessStrategy = new DirectFieldPropertySettingStrategy <ChildTestBean, String> (privateChildField);
      PropertySettingStrategy <ChildTestBean, String> protectedChildPropertyAccessStrategy = new DirectFieldPropertySettingStrategy <ChildTestBean, String> (protectedChildField);
      PropertySettingStrategy <ChildTestBean, String> packageChildPropertyAccessStrategy = new DirectFieldPropertySettingStrategy <ChildTestBean, String> (packageChildField);
      PropertySettingStrategy <ChildTestBean, String> publicChildPropertyAccessStrategy = new DirectFieldPropertySettingStrategy <ChildTestBean, String> (publicChildField);
      ChildTestBean child = new ChildTestBean();
      privateBasePropertyAccessStrategy.set(child, "BASE.PRIVATE");
      protectedBasePropertyAccessStrategy.set(child, "BASE.PROTECTED");
      packageBasePropertyAccessStrategy.set(child, "BASE.PACKAGE");
      publicBasePropertyAccessStrategy.set(child, "BASE.PUBLIC");
      privateChildPropertyAccessStrategy.set(child, "CHILD.PRIVATE");
      protectedChildPropertyAccessStrategy.set(child, "CHILD.PROTECTED");
      packageChildPropertyAccessStrategy.set(child, "CHILD.PACKAGE");
      publicChildPropertyAccessStrategy.set(child, "CHILD.PUBLIC");
      assertTrue(privateBaseField.isAccessible());
      assertTrue(protectedBaseField.isAccessible());
      assertTrue(packageBaseField.isAccessible());
      assertTrue(publicBaseField.isAccessible());
      assertTrue(privateChildField.isAccessible());
      assertTrue(protectedChildField.isAccessible());
      assertTrue(packageChildField.isAccessible());
      assertTrue(publicChildField.isAccessible());
      assertEquals("BASE.PRIVATE", ((BaseTestBean)(child)).privateBase);
      assertEquals("BASE.PROTECTED", child.protectedBase);
      assertEquals("BASE.PACKAGE", child.packageBase);
      assertEquals("BASE.PUBLIC", child.publicBase);
      assertEquals("CHILD.PRIVATE", child.privateChild);
      assertEquals("CHILD.PROTECTED", child.protectedChild);
      assertEquals("CHILD.PACKAGE", child.packageChild);
      assertEquals("CHILD.PUBLIC", child.publicChild);
    } catch (Exception ex) {
      fail(ex.getMessage());
    }
  }
  
   public void testGetProperty() {
    try {
      Field privateBaseField = BaseTestBean.class.getDeclaredField("privateBase");
      PropertySettingStrategy privateBasePropertyAccessStrategy = new DirectFieldPropertySettingStrategy (privateBaseField);
      assertEquals(privateBaseField.getName(), privateBasePropertyAccessStrategy.getProperty());
    } catch (Exception ex) {
      fail();
    }
  }
}
