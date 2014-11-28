/*
 * CollectionBeanCloneableFunctionalTest.java
 * JUnit based test
 *
 * Created on 11 de Agosto de 2005, 23:58
 */

package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.support.cloning.DefaultCloningStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import junit.framework.*;
import br.com.appworks.runtime.lang.Cloneable;
import br.com.appworks.runtime.lang.CloningPolicy;
import br.com.appworks.runtime.lang.support.HashTypeMap;
import br.com.appworks.runtime.lang.support.TypeMap;
import br.com.appworks.runtime.lang.support.cloning.CachingCloningStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategyFactory;
import br.com.appworks.runtime.lang.support.cloning.CollectionCloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.MapCloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.SortedMapCloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.SortedSetCloningStrategy;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

public class CollectionBeanCloneableFunctionalTest extends TestCase {
  
  @Cloneable(CloningPolicy.SHALLOW)
  public static class ShallowCollectionTestBean {
    private Collection <Object> x;
    public ShallowCollectionTestBean(Collection <Object> x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.DEEP)
  public static class StaticDeepCollectionTestBean {
    private static Collection <Object> x;
    public StaticDeepCollectionTestBean(Collection <Object> x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.DEEP)
  public static class DeepCollectionTestBean {
    private Collection <Object> x;
    public DeepCollectionTestBean(Collection <Object> x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  public CollectionBeanCloneableFunctionalTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    TypeMap <CloningStrategy> typeMap = new HashTypeMap <CloningStrategy> ();
    typeMap.put(Collection.class, new CollectionCloningStrategy());
    typeMap.put(SortedSet.class, new SortedSetCloningStrategy());
    typeMap.put(Map.class, new MapCloningStrategy());
    typeMap.put(SortedMap.class, new SortedMapCloningStrategy());
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.cloning.aop.CloningAspect");
    klass.getDeclaredMethod("setStaticCloningStrategyFactory", CloningStrategyFactory.class).invoke(null, new CachingCloningStrategyFactoryAdapter(new DefaultCloningStrategyFactory(new DefaultPropertyAccessStrategyFactory(new ReflectionBasedPropertyGettingStrategyFactory(), new ReflectionBasedPropertySettingStrategyFactory()), typeMap)));
  }

  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.cloning.aop.CloningAspect");
    klass.getDeclaredMethod("setStaticCloningStrategyFactory", CloningStrategyFactory.class).invoke(null, new Object [] { null });
  }
  
  private <Type> Type clone(Type object) throws CloneNotSupportedException {
    Type clone = null;
    if (object != null) {
      clone = (Type)((br.com.appworks.runtime.lang.support.cloning.Cloneable)(object)).clone();
    }
    return clone;
  }
  
  public void testShallowCollectionTestBeanNullCollection() {
    try {
      ShallowCollectionTestBean bean = new ShallowCollectionTestBean(null);
      ShallowCollectionTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testShallowCollectionTestBeanNotNullCollection() {
    try {
      for (Collection collection : new Collection [] {
          new ArrayList(),
          new ArrayList(Arrays.asList(new Object[] { null })),
          new ArrayList(Arrays.asList(new Object[] { "X", "Y" })),
          new ArrayList(Arrays.asList(new Object[] { Calendar.getInstance(), Calendar.getInstance() })),
          new ArrayList(Arrays.asList(new Object[] { null, "X", Calendar.getInstance()}))}) {
        ShallowCollectionTestBean bean = new ShallowCollectionTestBean(collection);
        ShallowCollectionTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertSame(bean.x, clone.x);
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testStaticDeepCollectionTestBeanNullCollection() {
    try {
      StaticDeepCollectionTestBean bean = new StaticDeepCollectionTestBean(null);
      StaticDeepCollectionTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testStaticDeepCollectionTestBeanNotNullCollection() {
    try {
      for (Collection collection : new Collection [] {
          new ArrayList(),
          new ArrayList(Arrays.asList(new Object[] { null })),
          new ArrayList(Arrays.asList(new Object[] { "X", "Y" })),
          new ArrayList(Arrays.asList(new Object[] { Calendar.getInstance(), Calendar.getInstance() })),
          new ArrayList(Arrays.asList(new Object[] { null, "X", Calendar.getInstance()}))}) {
        StaticDeepCollectionTestBean bean = new StaticDeepCollectionTestBean(collection);
        StaticDeepCollectionTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertSame(bean.x, clone.x);
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testDeepCollectionTestBeanNullCollection() {
    try {
      DeepCollectionTestBean bean = new DeepCollectionTestBean(null);
      DeepCollectionTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testDeepCollectionTestBeanNotNullCollection() {
    try {
      for (Collection collection : new Collection [] {
          new ArrayList(),
          new ArrayList(Arrays.asList(new Object[] { null })),
          new ArrayList(Arrays.asList(new Object[] { "X", "Y" })),
          new ArrayList(Arrays.asList(new Object[] { Calendar.getInstance(), Calendar.getInstance() })),
          new ArrayList(Arrays.asList(new Object[] { null, "X", Calendar.getInstance()}))}) {
        DeepCollectionTestBean bean = new DeepCollectionTestBean(collection);
        DeepCollectionTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertNotSame(bean.x, clone.x);
        assertEquals(bean.x, clone.x);
        assertEquals(bean.x.size(), clone.x.size());
        for (int i=0; i<bean.x.size(); i++) {
          if (((List)(bean.x)).get(i) instanceof Calendar) {
            assertNotSame(((List)(bean.x)).get(i), ((List)(clone.x)).get(i));
            assertEquals(((List)(bean.x)).get(i), ((List)(clone.x)).get(i));
          } else {
            assertSame(((List)(bean.x)).get(i), ((List)(clone.x)).get(i));
          }
        }
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
}
