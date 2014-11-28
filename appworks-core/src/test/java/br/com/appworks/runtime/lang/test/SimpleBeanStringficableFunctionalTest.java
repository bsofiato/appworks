/*
 * SimpleBeanStringficableFunctionalTest.java
 * JUnit based test
 *
 * Created on 13 de Dezembro de 2005, 23:57
 */

package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.Stringficable;
import br.com.appworks.runtime.lang.StringficationPolicy;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import br.com.appworks.runtime.lang.support.stringfication.CachingStringficationStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.stringfication.DefaultStringficationStrategyFactory;
import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategyFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class SimpleBeanStringficableFunctionalTest extends TestCase {
  @Stringficable  
  private static class ToStringPropertyBean implements Serializable  {
    @Stringficable
    private String x;
    public ToStringPropertyBean() {
      this(null);
    }
    public ToStringPropertyBean(String x) {
      this.x = x;
    }
  }
  
  @Stringficable  
  private static class IdentityPropertyBean implements Serializable  {
    @Stringficable(StringficationPolicy.IDENTITY)
    private String x;
    public IdentityPropertyBean() {
      this(null);
    }
    public IdentityPropertyBean(String x) {
      this.x = x;
    }
  }
  public SimpleBeanStringficableFunctionalTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
    klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new CachingStringficationStrategyFactoryAdapter(new DefaultStringficationStrategyFactory(null, new DefaultPropertyAccessStrategyFactory(new ReflectionBasedPropertyGettingStrategyFactory(), new ReflectionBasedPropertySettingStrategyFactory()))));
  }

  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
    klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new Object [] {null});
  }
  
  public void testToStringPropertyBeanWithNullProperties() {
    assertEquals("[x : null]", new ToStringPropertyBean(null).toString());
  }

  public void testToStringPropertyBean() {
    assertEquals("[x : X]", new ToStringPropertyBean("X").toString());
  }

  public void testIdentityPropertyBeanWithNullProperties() {
    assertEquals("[x : null]", new IdentityPropertyBean(null).toString());
  }
  public void testIdentityPropertyBean() {
    String string = "X";
    assertEquals("[x : java.lang.String@" + Integer.toHexString(System.identityHashCode(string)) +"]", new IdentityPropertyBean(string).toString());
  }
  
  public void testToStringPropertyBeanSerialization() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      ToStringPropertyBean testBean = new ToStringPropertyBean("X");
      oos.writeObject(testBean);
      oos.flush();
      oos.close();
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bais);
      ToStringPropertyBean serializedTestBean = (ToStringPropertyBean)(ois.readObject());
      ois.close();
      assertNotSame(testBean, serializedTestBean);
      assertEquals(testBean.x, serializedTestBean.x);
      assertEquals("[x : X]", serializedTestBean.toString());
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testIdentityPropertyBeanSerialization() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      IdentityPropertyBean testBean = new IdentityPropertyBean("X");
      oos.writeObject(testBean);
      oos.flush();
      oos.close();
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bais);
      IdentityPropertyBean serializedTestBean = (IdentityPropertyBean)(ois.readObject());
      ois.close();
      assertNotSame(testBean, serializedTestBean);
      assertEquals(testBean.x, serializedTestBean.x);
      assertEquals("[x : java.lang.String@" + Integer.toHexString(System.identityHashCode(serializedTestBean.x)) +"]", serializedTestBean.toString());
    } catch (Exception ex) {
      fail();
    }
  }
  
}
