/*
 * SortedSetBeanCloneableFunctionalTest.java
 * JUnit based test
 *
 * Created on 13 de Agosto de 2005, 20:20
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
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class SortedSetBeanCloneableFunctionalTest extends TestCase {
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
  public static class ShallowSortedSetTestBean {
    private SortedSet <Object> x;
    public ShallowSortedSetTestBean(SortedSet <Object> x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.DEEP)
  public static class StaticDeepSortedSetTestBean {
    private static SortedSet <Object> x;
    public StaticDeepSortedSetTestBean(SortedSet <Object> x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  
  @Cloneable(CloningPolicy.DEEP)
  public static class DeepSortedSetTestBean {
    private SortedSet <Object> x;
    public DeepSortedSetTestBean(SortedSet <Object> x) {
      this.x = x;
    }
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }
  public SortedSetBeanCloneableFunctionalTest(String testName) {
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
  
  public void testShallowSortedSetTestBeanNullSortedSet() {
    try {
      ShallowSortedSetTestBean bean = new ShallowSortedSetTestBean(null);
      ShallowSortedSetTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testShallowSortedSetTestBeanNotNullSortedSet() {
    try {
      Comparator <Object> comparator = new Comparator <Object> () {
        public int compare(Object op1, Object op2) {
          int op1HashCode = (op1 == null) ? 0 : op1.hashCode();
          int op2HashCode = (op2 == null) ? 0 : op2.hashCode();
          return op1HashCode - op2HashCode;
        }
      };  
      for (Collection collection : new Collection [] {
          new ArrayList(),
          new ArrayList(Arrays.asList(new Object[] { null })),
          new ArrayList(Arrays.asList(new Object[] { "X", "Y" })),
          new ArrayList(Arrays.asList(new Object[] { Calendar.getInstance(), Calendar.getInstance() })),
          new ArrayList(Arrays.asList(new Object[] { null, "X", Calendar.getInstance()}))}) {
        SortedSet <Object> sortedSet = new TreeSet <Object> (comparator);
        sortedSet.addAll(collection);
        ShallowSortedSetTestBean bean = new ShallowSortedSetTestBean(sortedSet);
        ShallowSortedSetTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertSame(bean.x, clone.x);
        assertSame(bean.x.comparator(), clone.x.comparator());
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testStaticDeepSortedSetTestBeanNullSortedSet() {
    try {
      StaticDeepSortedSetTestBean bean = new StaticDeepSortedSetTestBean(null);
      StaticDeepSortedSetTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testStaticDeepSortedSetTestBeanNotNullSortedSet() {
    try {
      Comparator <Object> comparator = new Comparator <Object> () {
        public int compare(Object op1, Object op2) {
          int op1HashCode = (op1 == null) ? 0 : op1.hashCode();
          int op2HashCode = (op2 == null) ? 0 : op2.hashCode();
          return op1HashCode - op2HashCode;
        }
      };  
      for (Collection collection : new Collection [] {
          new ArrayList(),
          new ArrayList(Arrays.asList(new Object[] { null })),
          new ArrayList(Arrays.asList(new Object[] { "X", "Y" })),
          new ArrayList(Arrays.asList(new Object[] { Calendar.getInstance(), Calendar.getInstance() })),
          new ArrayList(Arrays.asList(new Object[] { null, "X", Calendar.getInstance()}))}) {
        SortedSet <Object> sortedSet = new TreeSet <Object> (comparator);
        sortedSet.addAll(collection);
        StaticDeepSortedSetTestBean bean = new StaticDeepSortedSetTestBean(sortedSet);
        StaticDeepSortedSetTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertSame(bean.x, clone.x);
        assertSame(bean.x.comparator(), clone.x.comparator());
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testDeepSortedSetTestBeanNullSortedSet() {
    try {
      DeepSortedSetTestBean bean = new DeepSortedSetTestBean(null);
      DeepSortedSetTestBean clone = clone(bean);
      assertNotSame(bean, clone);
      assertEquals(bean.x, clone.x);
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testDeepSortedSetTestBeanNotNullSortedSet() {
    try {
      Comparator <Object> comparator = new Comparator <Object> () {
        public int compare(Object op1, Object op2) {
          int op1HashCode = (op1 == null) ? 0 : op1.hashCode();
          int op2HashCode = (op2 == null) ? 0 : op2.hashCode();
          return op1HashCode - op2HashCode;
        }
      };  
      for (Collection collection : new Collection [] {
          new ArrayList(),
          new ArrayList(Arrays.asList(new Object[] { null })),
          new ArrayList(Arrays.asList(new Object[] { "X", "Y" })),
          new ArrayList(Arrays.asList(new Object[] { Calendar.getInstance(), Calendar.getInstance() })),
          new ArrayList(Arrays.asList(new Object[] { null, "X", Calendar.getInstance()}))}) {
        SortedSet <Object> sortedSet = new TreeSet <Object> (comparator);
        sortedSet.addAll(collection);
        DeepSortedSetTestBean bean = new DeepSortedSetTestBean(sortedSet);
        DeepSortedSetTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertNotSame(bean.x, clone.x);
        assertEquals(bean.x, clone.x);
        assertEquals(bean.x.size(), clone.x.size());
        assertSame(bean.x.comparator(), clone.x.comparator());
        Iterator i = bean.x.iterator();
        Iterator i2 = clone.x.iterator();
        while (i.hasNext()) {
          Object o1 = i.next();
          Object o2 = i2.next();
          if (o1 instanceof Calendar) {
            assertNotSame(o1, o2);
            assertEquals(o1, o2);
          } else {
            assertSame(o1, o2);
          }
        }
      }
    } catch (CloneNotSupportedException ex) {
      fail();
    }
  }

  public void testDeepSortedSetTestBeanNotNullSortedSetCloneableComparator() {
    try {
      for (Collection collection : new Collection [] {
          new ArrayList(),
          new ArrayList(Arrays.asList(new Object[] { null })),
          new ArrayList(Arrays.asList(new Object[] { "X", "Y" })),
          new ArrayList(Arrays.asList(new Object[] { Calendar.getInstance(), Calendar.getInstance() })),
          new ArrayList(Arrays.asList(new Object[] { null, "X", Calendar.getInstance()}))}) {
        SortedSet <Object> sortedSet = new TreeSet <Object> (new CloneableComparator());
        sortedSet.addAll(collection);
        DeepSortedSetTestBean bean = new DeepSortedSetTestBean(sortedSet);
        DeepSortedSetTestBean clone = clone(bean);
        assertNotSame(bean, clone);
        assertNotSame(bean.x, clone.x);
        assertEquals(bean.x, clone.x);
        assertEquals(bean.x.size(), clone.x.size());
        assertNotSame(bean.x.comparator(), clone.x.comparator());
        assertTrue(clone.x.comparator() instanceof CloneableComparator);
        Iterator i = bean.x.iterator();
        Iterator i2 = clone.x.iterator();
        while (i.hasNext()) {
          Object o1 = i.next();
          Object o2 = i2.next();
          if (o1 instanceof Calendar) {
            assertNotSame(o1, o2);
            assertEquals(o1, o2);
          } else {
            assertSame(o1, o2);
          }
        }
      }
    } catch (CloneNotSupportedException ex) {
      ex.printStackTrace();
      fail();
    }
  }

}
