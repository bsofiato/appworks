/*
 * SortedMapBeanCloneableFunctionalTest.java
 * JUnit based test
 *
 * Created on 14 de Agosto de 2005, 20:19
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
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

public class SortedMapBeanCloneableFunctionalTest extends TestCase {
  private static class CloneableComparator implements Comparator<Object>, java.lang.Cloneable {
    public int compare(Object op1, Object op2) {
      int op1HashCode = (op1 == null) ? 0 : op1.hashCode();
      int op2HashCode = (op2 == null) ? 0 : op2.hashCode();
      return op1HashCode - op2HashCode;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
   
  @Cloneable(CloningPolicy.SHALLOW)
  public static class ShallowSortedMapTestBean {
    private SortedMap <Object, Object> x;
    public ShallowSortedMapTestBean(SortedMap <Object, Object> x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.DEEP)
  public static class StaticDeepSortedMapTestBean {
    private static SortedMap <Object, Object> x;
    public StaticDeepSortedMapTestBean(SortedMap <Object, Object> x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.DEEP)
  public static class DeepSortedMapTestBean {
    private SortedMap <Object, Object> x;
    public DeepSortedMapTestBean(SortedMap <Object, Object> x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  public SortedMapBeanCloneableFunctionalTest(String testName) {
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
  
  public void testShallowSortedMapTestBeanNullMap() {
    try {
      ShallowSortedMapTestBean bean = new ShallowSortedMapTestBean(null);
      ShallowSortedMapTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testShallowSortedMapTestBeanNotNullMap() {
    try {
      Calendar key1 = Calendar.getInstance();
      Calendar key2 = Calendar.getInstance();
      key2.add(Calendar.MONTH, 1);
      Calendar key3 = Calendar.getInstance();
      key3.add(Calendar.MONTH, 2);
      Collection <SortedMap<Object, Object>> maps  = new ArrayList <SortedMap<Object, Object>>();
      maps.add(new TreeMap(new CloneableComparator()));
      SortedMap <Object, Object> fullMap = new TreeMap <Object, Object> (new CloneableComparator());
      fullMap.put("A", null);
      fullMap.put("B", "A");
      fullMap.put("C", Calendar.getInstance());
      fullMap.put(key1, null);
      fullMap.put(key2, "X");
      fullMap.put(key3, Calendar.getInstance());
      maps.add(fullMap);
      for (SortedMap <Object, Object> map : maps) {
        ShallowSortedMapTestBean bean = new ShallowSortedMapTestBean(map);
        ShallowSortedMapTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertSame(bean.x, clone.x);
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testStaticDeepSortedMapTestBeanNullMap() {
    try {
      StaticDeepSortedMapTestBean bean = new StaticDeepSortedMapTestBean(null);
      StaticDeepSortedMapTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  
  public void testStaticDeepSortedMapTestBeanNotNullMap() {
    try {
      Calendar key1 = Calendar.getInstance();
      Calendar key2 = Calendar.getInstance();
      key2.add(Calendar.MONTH, 1);
      Calendar key3 = Calendar.getInstance();
      key3.add(Calendar.MONTH, 2);
      Collection <SortedMap<Object, Object>> maps  = new ArrayList <SortedMap<Object, Object>>();
      maps.add(new TreeMap(new CloneableComparator()));
      SortedMap <Object, Object> fullMap = new TreeMap <Object, Object> (new CloneableComparator());
      fullMap.put("A", null);
      fullMap.put("B", "A");
      fullMap.put("C", Calendar.getInstance());
      fullMap.put(key1, null);
      fullMap.put(key2, "X");
      fullMap.put(key3, Calendar.getInstance());
      maps.add(fullMap);
      for (SortedMap <Object, Object> map : maps) {
        StaticDeepSortedMapTestBean bean = new StaticDeepSortedMapTestBean(map);
        StaticDeepSortedMapTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertSame(bean.x, clone.x);
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testDeepSortedMapTestBeanNullMap() {
    try {
      DeepSortedMapTestBean bean = new DeepSortedMapTestBean(null);
      DeepSortedMapTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }
  
  public void testDeepSortedMapTestBeanNotNullMap() {
    try {
      Calendar key1 = Calendar.getInstance();
      Calendar key2 = Calendar.getInstance();
      key2.add(Calendar.MONTH, 1);
      Calendar key3 = Calendar.getInstance();
      key3.add(Calendar.MONTH, 2);
      Collection <SortedMap<Object, Object>> maps  = new ArrayList <SortedMap<Object, Object>>();
      maps.add(new TreeMap(new CloneableComparator()));
      SortedMap <Object, Object> fullMap = new TreeMap <Object, Object> (new CloneableComparator());
      fullMap.put("A", null);
      fullMap.put("B", "A");
      fullMap.put("C", Calendar.getInstance());
      fullMap.put(key1, null);
      fullMap.put(key2, "X");
      fullMap.put(key3, Calendar.getInstance());
      maps.add(fullMap);
      for (SortedMap <Object, Object> map : maps) {
        DeepSortedMapTestBean bean = new DeepSortedMapTestBean(map);
        DeepSortedMapTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertNotSame(bean.x, clone.x);
        assertNotSame(bean.x.comparator(), clone.x.comparator());
        assertTrue(clone.x.comparator() instanceof CloneableComparator);
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
