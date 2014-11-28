/*
 * SimpleBeanCloneableFunctionalTest.java
 * JUnit based test
 *
 * Created on 2 de Agosto de 2005, 23:49
 */

package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.Cloneable;
import br.com.appworks.runtime.lang.CloningPolicy;
import br.com.appworks.runtime.lang.support.cloning.CachingCloningStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategyFactory;
import br.com.appworks.runtime.lang.support.cloning.DefaultCloningStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import java.io.Serializable;
import java.util.Calendar;
import junit.framework.*;


public class SimpleBeanCloneableFunctionalTest extends TestCase {
  
  @Cloneable(CloningPolicy.DEEP)
  private static class CloneablePropertyDeepCloneableBean implements Serializable  {
    private Calendar x;
    public CloneablePropertyDeepCloneableBean() {
      this(null);
    }
    public CloneablePropertyDeepCloneableBean(Calendar x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.DEEP)
  private static class CloneableStaticPropertyDeepCloneableBean implements Serializable  {
    private static Calendar x;
    public CloneableStaticPropertyDeepCloneableBean() {
      this(null);
    }
    public CloneableStaticPropertyDeepCloneableBean(Calendar x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  @Cloneable(CloningPolicy.DEEP)
  private static class CloneableFinalPropertyDeepCloneableBean implements Serializable  {
    private final Calendar x;
    public CloneableFinalPropertyDeepCloneableBean() {
      this(null);
    }
    public CloneableFinalPropertyDeepCloneableBean(Calendar x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.SHALLOW)
  private static class CloneablePropertyShallowCloneableBean implements Serializable  {
    private Calendar x;
    public CloneablePropertyShallowCloneableBean() {
      this(null);
    }
    public CloneablePropertyShallowCloneableBean(Calendar x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }

  @Cloneable(CloningPolicy.DEEP)
  private static class NonCloneablePropertyDeepCloneableBean implements Serializable  {
    private String x;
    public NonCloneablePropertyDeepCloneableBean() {
      this(null);
    }
    public NonCloneablePropertyDeepCloneableBean(String x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.SHALLOW)
  private static class NonCloneablePropertyShallowCloneableBean implements Serializable  {
    private String x;
    public NonCloneablePropertyShallowCloneableBean() {
      this(null);
    }
    public NonCloneablePropertyShallowCloneableBean(String x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }

  @Cloneable(CloningPolicy.SHALLOW)
  private static class OverridedCloneablePropertyShallowCloneableBean implements Serializable  {
    @Cloneable(CloningPolicy.DEEP)
    private Calendar x;
    public OverridedCloneablePropertyShallowCloneableBean() {
      this(null);
    }
    public OverridedCloneablePropertyShallowCloneableBean(Calendar x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.DEEP)
  private static class NonFinalNonCloneablePropertyDeepCloneableBean implements Serializable  {
    private Object x;
    public NonFinalNonCloneablePropertyDeepCloneableBean() {
      this(null);
    }
    public NonFinalNonCloneablePropertyDeepCloneableBean(Object x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }

  private <Type> Type clone(Type object) throws CloneNotSupportedException {
    Type clone = null;
    if (object != null) {
      clone = (Type)((br.com.appworks.runtime.lang.support.cloning.Cloneable)(object)).clone();
    }
    return clone;
  }
  
  public SimpleBeanCloneableFunctionalTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.cloning.aop.CloningAspect");
    klass.getDeclaredMethod("setStaticCloningStrategyFactory", CloningStrategyFactory.class).invoke(null, new CachingCloningStrategyFactoryAdapter(new DefaultCloningStrategyFactory(new DefaultPropertyAccessStrategyFactory(new ReflectionBasedPropertyGettingStrategyFactory(), new ReflectionBasedPropertySettingStrategyFactory()))));
  }

  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.cloning.aop.CloningAspect");
    klass.getDeclaredMethod("setStaticCloningStrategyFactory", CloningStrategyFactory.class).invoke(null, new Object [] { null });
  }

  public void testCloneableFinalPropertyDeepCloneableBeanNullPropertyCloning() {
    try {
      CloneableFinalPropertyDeepCloneableBean bean = new CloneableFinalPropertyDeepCloneableBean();
      CloneableFinalPropertyDeepCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testCloneableFinalPropertyDeepCloneableBeanNotNullPropertyCloning() {
    try {
      CloneableFinalPropertyDeepCloneableBean bean = new CloneableFinalPropertyDeepCloneableBean(Calendar.getInstance());
      CloneableFinalPropertyDeepCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testCloneablePropertyDeepCloneableBeanNullPropertyCloning() {
    try {
      CloneablePropertyDeepCloneableBean bean = new CloneablePropertyDeepCloneableBean();
      CloneablePropertyDeepCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testCloneablePropertyDeepCloneableBeanNotNullPropertyCloning() {
    try {
      CloneablePropertyDeepCloneableBean bean = new CloneablePropertyDeepCloneableBean(Calendar.getInstance());
      CloneablePropertyDeepCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testCloneablePropertyShallowCloneableBeanNullPropertyCloning() {
    try {
      CloneablePropertyShallowCloneableBean bean = new CloneablePropertyShallowCloneableBean();
      CloneablePropertyShallowCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertSame(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testCloneablePropertyShallowCloneableBeanNotNullPropertyCloning() {
    try {
      CloneablePropertyShallowCloneableBean bean = new CloneablePropertyShallowCloneableBean(Calendar.getInstance());
      CloneablePropertyShallowCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertSame(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testNonCloneablePropertyDeepCloneableBeanNullPropertyCloning() {
    try {
      NonCloneablePropertyDeepCloneableBean bean = new NonCloneablePropertyDeepCloneableBean();
      NonCloneablePropertyDeepCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertSame(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testNonCloneablePropertyDeepCloneableBeanNotNullPropertyCloning() {
    try {
      NonCloneablePropertyDeepCloneableBean bean = new NonCloneablePropertyDeepCloneableBean("X");
      NonCloneablePropertyDeepCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertSame(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testNonCloneablePropertyShallowCloneableBeanNullPropertyCloning() {
    try {
      NonCloneablePropertyShallowCloneableBean bean = new NonCloneablePropertyShallowCloneableBean();
      NonCloneablePropertyShallowCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertSame(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testNonCloneablePropertyShallowCloneableBeanNotNullPropertyCloning() {
    try {
      NonCloneablePropertyShallowCloneableBean bean = new NonCloneablePropertyShallowCloneableBean("X");
      NonCloneablePropertyShallowCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertSame(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testOverridedCloneablePropertyShallowCloneableBeanNullPropertyCloning() {
    try {
      OverridedCloneablePropertyShallowCloneableBean bean = new OverridedCloneablePropertyShallowCloneableBean();
      OverridedCloneablePropertyShallowCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testOverridedCloneablePropertyShallowCloneableBeanNotNullPropertyCloning() {
    try {
      OverridedCloneablePropertyShallowCloneableBean bean = new OverridedCloneablePropertyShallowCloneableBean(Calendar.getInstance());
      OverridedCloneablePropertyShallowCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

   public void testCloneableStaticPropertyDeepCloneableBeanNullPropertyCloning() {
    try {
      CloneableStaticPropertyDeepCloneableBean bean = new CloneableStaticPropertyDeepCloneableBean();
      CloneableStaticPropertyDeepCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertSame(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testCloneableStaticPropertyDeepCloneableBeanNotNullPropertyCloning() {
    try {
      Calendar calendar = Calendar.getInstance();
      CloneableStaticPropertyDeepCloneableBean bean = new CloneableStaticPropertyDeepCloneableBean(calendar);
      CloneableStaticPropertyDeepCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertSame(bean.x, clone.x);
      assertSame(calendar, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  
  public void testNonFinalNonCloneablePropertyDeepCloneableBeanNullPropertyCloning() {
    try {
      NonFinalNonCloneablePropertyDeepCloneableBean bean = new NonFinalNonCloneablePropertyDeepCloneableBean();
      NonFinalNonCloneablePropertyDeepCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertSame(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testNonFinalNonCloneablePropertyDeepCloneableBeanNonCloneablePropertyCloning() {
    try {
      NonFinalNonCloneablePropertyDeepCloneableBean bean = new NonFinalNonCloneablePropertyDeepCloneableBean("X");
      NonFinalNonCloneablePropertyDeepCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertSame(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testNonFinalNonCloneablePropertyDeepCloneableBeanCloneablePropertyCloning() {
    try {
      NonFinalNonCloneablePropertyDeepCloneableBean bean = new NonFinalNonCloneablePropertyDeepCloneableBean(Calendar.getInstance());
      NonFinalNonCloneablePropertyDeepCloneableBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertNotSame(bean.x, clone.x);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

}
