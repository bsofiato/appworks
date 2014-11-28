/*
 * CustomCloningStrategyCloneableFunctionalTest.java
 * JUnit based test
 *
 * Created on 4 de Agosto de 2005, 23:36
 */

package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.Cloneable;
import br.com.appworks.runtime.lang.support.HashTypeMap;
import br.com.appworks.runtime.lang.support.TypeMap;
import br.com.appworks.runtime.lang.support.cloning.CachingCloningStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategyFactory;
import br.com.appworks.runtime.lang.support.cloning.DefaultCloningStrategyFactory;
import br.com.appworks.runtime.lang.support.cloning.CollectionCloningStrategy;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import junit.framework.*;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class CustomCloningStrategyCloneableFunctionalTest extends TestCase {
  
  public static class CustomPropertyCloningStrategy implements CloningStrategy <Object> {
    private static MockControl control = MockControl.createControl(CloningStrategy.class);
    public static MockControl getControl() {
      return control;
    }
    public Object clone(Object object) throws CloneNotSupportedException {
      return ((CloningStrategy)(getControl().getMock())).clone(object);
    }
    public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
    }
  }

  @Cloneable(strategy=CustomPropertyCloningStrategy.class)
  private static interface TestInterface {
  }
  
  private static class TestInterfaceChild implements TestInterface {
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable
  private static class CustomPropertyCloningStrategyTestBean {
    @Cloneable(strategy=CustomPropertyCloningStrategy.class)
    private String x;
    @Cloneable(strategy=CustomPropertyCloningStrategy.class)
    private Calendar y;
    @Cloneable(strategy=CustomPropertyCloningStrategy.class)
    private Collection z;
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(strategy=CustomPropertyCloningStrategy.class)
  private static class CustomCloningStrategyTestBean {
    public CustomCloningStrategyTestBean() {
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  public CustomCloningStrategyCloneableFunctionalTest(String testName) {
    super(testName);
  }

   private <Type> Type clone(Type object) throws CloneNotSupportedException {
    Type clone = null;
    if (object != null) {
      clone = (Type)((br.com.appworks.runtime.lang.support.cloning.Cloneable)(object)).clone();
    }
    return clone;
  }
  
  protected void setUp() throws Exception {
    TypeMap <CloningStrategy> typeMap = new HashTypeMap <CloningStrategy> ();
    typeMap.put(Collection.class, new CollectionCloningStrategy());
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.cloning.aop.CloningAspect");
    klass.getDeclaredMethod("setStaticCloningStrategyFactory", CloningStrategyFactory.class).invoke(null, new CachingCloningStrategyFactoryAdapter(new DefaultCloningStrategyFactory(new DefaultPropertyAccessStrategyFactory(new ReflectionBasedPropertyGettingStrategyFactory(), new ReflectionBasedPropertySettingStrategyFactory()), typeMap)));
  }

  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.cloning.aop.CloningAspect");
    klass.getDeclaredMethod("setStaticCloningStrategyFactory", CloningStrategyFactory.class).invoke(null, new Object [] { null });
    CustomPropertyCloningStrategy.getControl().reset();
  }
  
  public void testCustomPropertyCloningStrategyTestBeanCloning() {
    try {
      Calendar calendar = Calendar.getInstance();
      CustomPropertyCloningStrategy customPropertyCloningStrategy = new CustomPropertyCloningStrategy();
      customPropertyCloningStrategy.clone(null);
      customPropertyCloningStrategy.getControl().setReturnValue("TEST");
      customPropertyCloningStrategy.clone(null);
      customPropertyCloningStrategy.getControl().setReturnValue(calendar);
      customPropertyCloningStrategy.clone(null);
      customPropertyCloningStrategy.getControl().setReturnValue(new ArrayList());
      customPropertyCloningStrategy.getControl().replay();
      CustomPropertyCloningStrategyTestBean bean = new CustomPropertyCloningStrategyTestBean();
      CustomPropertyCloningStrategyTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals("TEST", clone.x);
      assertEquals(calendar, clone.y);
      assertEquals(new ArrayList(), clone.z);
      customPropertyCloningStrategy.getControl().verify();
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testCustomCloningStrategyTestBeanCloning() {
    try {
      CustomCloningStrategyTestBean bean = new CustomCloningStrategyTestBean();
      CustomCloningStrategyTestBean bean2 = new CustomCloningStrategyTestBean();
      CustomPropertyCloningStrategy customPropertyCloningStrategy = new CustomPropertyCloningStrategy();
      customPropertyCloningStrategy.clone(bean);
      customPropertyCloningStrategy.getControl().setMatcher(MockControl.ALWAYS_MATCHER);
      customPropertyCloningStrategy.getControl().setReturnValue(bean2);
      customPropertyCloningStrategy.getControl().replay();
      CustomCloningStrategyTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertSame(bean2, clone);
      customPropertyCloningStrategy.getControl().verify();
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testChildTestInterfaceCloning() {
    try {
      TestInterfaceChild bean = new TestInterfaceChild();
      TestInterfaceChild bean2 = new TestInterfaceChild();
      CustomPropertyCloningStrategy customPropertyCloningStrategy = new CustomPropertyCloningStrategy();
      customPropertyCloningStrategy.clone(bean);
      customPropertyCloningStrategy.getControl().setMatcher(MockControl.ALWAYS_MATCHER);
      customPropertyCloningStrategy.getControl().setReturnValue(bean2);
      customPropertyCloningStrategy.getControl().replay();
      TestInterfaceChild clone = clone(bean);
      assertNotSame(bean, clone);
      assertSame(bean2, clone);
      customPropertyCloningStrategy.getControl().verify();
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
}
