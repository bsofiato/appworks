/*
 * MapBeanCloneableFunctionalTest.java
 * JUnit based test
 *
 * Created on 14 de Agosto de 2005, 01:15
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
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

public class MapBeanCloneableFunctionalTest extends TestCase {
  
  @Cloneable(CloningPolicy.SHALLOW)
  public static class ShallowMapTestBean {
    private Map <Object, Object> x;
    public ShallowMapTestBean(Map <Object, Object> x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.DEEP)
  public static class StaticDeepMapTestBean {
    private static Map <Object, Object> x;
    public StaticDeepMapTestBean(Map <Object, Object> x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.DEEP)
  public static class DeepMapTestBean {
    private Map <Object, Object> x;
    public DeepMapTestBean(Map <Object, Object> x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  public MapBeanCloneableFunctionalTest(String testName) {
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
  
  public void testShallowMapTestBeanNullMap() {
    try {
      ShallowMapTestBean bean = new ShallowMapTestBean(null);
      ShallowMapTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  
  public void testShallowMapTestBeanNotNullMap() {
    try {
      Calendar key1 = Calendar.getInstance();
      Calendar key2 = Calendar.getInstance();
      key2.add(Calendar.MONTH, 1);
      Calendar key3 = Calendar.getInstance();
      key3.add(Calendar.MONTH, 2);
      Collection <Map<Object, Object>> maps  = new ArrayList <Map<Object, Object>>();
      maps.add(new HashMap());
      Map <Object, Object> fullMap = new HashMap <Object, Object> ();
      fullMap.put("A", null);
      fullMap.put("B", "A");
      fullMap.put("C", Calendar.getInstance());
      fullMap.put(key1, null);
      fullMap.put(key2, "X");
      fullMap.put(key3, Calendar.getInstance());
      maps.add(fullMap);
      for (Map <Object, Object> map : maps) {
        ShallowMapTestBean bean = new ShallowMapTestBean(map);
        ShallowMapTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertSame(bean.x, clone.x);
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testStaticDeepMapTestBeanNullMap() {
    try {
      StaticDeepMapTestBean bean = new StaticDeepMapTestBean(null);
      StaticDeepMapTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  
  public void testStaticDeepMapTestBeanNotNullMap() {
    try {
      Calendar key1 = Calendar.getInstance();
      Calendar key2 = Calendar.getInstance();
      key2.add(Calendar.MONTH, 1);
      Calendar key3 = Calendar.getInstance();
      key3.add(Calendar.MONTH, 2);
      Collection <Map<Object, Object>> maps  = new ArrayList <Map<Object, Object>>();
      maps.add(new HashMap());
      Map <Object, Object> fullMap = new HashMap <Object, Object> ();
      fullMap.put("A", null);
      fullMap.put("B", "A");
      fullMap.put("C", Calendar.getInstance());
      fullMap.put(key1, null);
      fullMap.put(key2, "X");
      fullMap.put(key3, Calendar.getInstance());
      maps.add(fullMap);
      for (Map <Object, Object> map : maps) {
        StaticDeepMapTestBean bean = new StaticDeepMapTestBean(map);
        StaticDeepMapTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertSame(bean.x, clone.x);
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testDeepMapTestBeanNullMap() {
    try {
      DeepMapTestBean bean = new DeepMapTestBean(null);
      DeepMapTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  
  public void testDeepMapTestBeanNotNullMap() {
    try {
      Calendar key1 = Calendar.getInstance();
      Calendar key2 = Calendar.getInstance();
      key2.add(Calendar.MONTH, 1);
      Calendar key3 = Calendar.getInstance();
      key3.add(Calendar.MONTH, 2);
      Collection <Map<Object, Object>> maps  = new ArrayList <Map<Object, Object>>();
      maps.add(new HashMap());
      Map <Object, Object> fullMap = new HashMap <Object, Object> ();
      fullMap.put("A", null);
      fullMap.put("B", "A");
      fullMap.put("C", Calendar.getInstance());
      fullMap.put(key1, null);
      fullMap.put(key2, "X");
      fullMap.put(key3, Calendar.getInstance());
      maps.add(fullMap);
      for (Map <Object, Object> map : maps) {
        DeepMapTestBean bean = new DeepMapTestBean(map);
        DeepMapTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertNotSame(bean.x, clone.x);
        assertEquals(bean.x, clone.x);
        for (Object k1 : bean.x.keySet()) {
          assertTrue(clone.x.keySet().contains(k1));
          if (k1 instanceof java.lang.Cloneable) {
            for (Object k2 : clone.x.keySet()) {
              assertNotSame(k1, k2);
            }
          } 
          Object v1 = bean.x.get(k1);
          Object v2 = clone.x.get(k1);
          if (v1 instanceof java.lang.Cloneable) {
            assertEquals(v1, v2);
            assertNotSame(v1, v2);
          } else {
            assertSame(v1, v2);
          }
        }
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
}
