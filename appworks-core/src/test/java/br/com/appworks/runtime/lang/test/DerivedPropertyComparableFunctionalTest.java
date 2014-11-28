/*
 * DerivedPropertyComparableFunctionalTest.java
 * JUnit based test
 *
 * Created on 15 de Setembro de 2005, 01:08
 */

package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.support.HashTypeMap;
import br.com.appworks.runtime.lang.support.TypeMap;
import br.com.appworks.runtime.lang.support.comparation.CalendarValueComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.DefaultComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.test.CustomComparationStrategyComparableFunctionalTest.CustomPropertyComparationStrategy;
import java.util.Calendar;
import junit.framework.*;
import br.com.appworks.runtime.lang.Comparable;
import br.com.appworks.runtime.lang.support.comparation.CachingComparationStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import org.aspectj.lang.Aspects;

/**
 *
 * @author Bubu
 */
public class DerivedPropertyComparableFunctionalTest extends TestCase {
  @Comparable
  private static class DerivedPropertyTestBean {
    private int x;
    private int y;
    public DerivedPropertyTestBean(int x, int y) {
      this.x = x;
      this.y = y;
    }
    @Comparable
    public int getValue() {
      return x+y; 
    }
  }
  public DerivedPropertyComparableFunctionalTest(String testName) {
    super(testName);
  }
  
  protected void setUp() throws Exception {
    TypeMap <ComparationStrategy> customTypeComparationStrategyMapping = new HashTypeMap <ComparationStrategy> ();
    customTypeComparationStrategyMapping.put(Calendar.class, new CalendarValueComparationStrategy());
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.comparation.aop.ComparationAspect");
    klass.getDeclaredMethod("setComparationStrategyFactory", ComparationStrategyFactory.class).invoke(Aspects.aspectOf(klass), new CachingComparationStrategyFactoryAdapter(new DefaultComparationStrategyFactory(new DefaultPropertyAccessStrategyFactory(new ReflectionBasedPropertyGettingStrategyFactory(), new ReflectionBasedPropertySettingStrategyFactory()), customTypeComparationStrategyMapping)));
  }

  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.comparation.aop.ComparationAspect");
    klass.getDeclaredMethod("setComparationStrategyFactory", ComparationStrategyFactory.class).invoke(Aspects.aspectOf(klass), new Object [] { null });
    CustomPropertyComparationStrategy.getControl().reset();
  } 
  
  public void testDerivedPropertyTestBeanNullOperandEquals() {
    assertFalse(new DerivedPropertyTestBean(1,1).equals(null));
  }
  public void testDerivedPropertyTestBeanNullOperandCompareTo() {
    try {
      ((java.lang.Comparable)(new DerivedPropertyTestBean(1,1))).compareTo(null);
      fail();
    } catch (NullPointerException ex) {
    }
  }
  public void testDerivedPropertyTestBeanTypeMismatchOperandEquals() {
    assertFalse(new DerivedPropertyTestBean(1,1).equals("XXX"));
  }
  public void testDerivedPropertyTestBeanTypeMismatchOperandCompareTo() {
    try {
      ((java.lang.Comparable)(new DerivedPropertyTestBean(1,1))).compareTo("XXX");
      fail();
    } catch (ClassCastException ex) {
    }
  }

  public void testDerivedPropertyTestBeanSameOperandEquals() {
    DerivedPropertyTestBean testBean = new DerivedPropertyTestBean(1,1);
    assertTrue(testBean.equals(testBean));
  }
  public void testDerivedPropertyTestBeanSameOperandCompareTo() {
    DerivedPropertyTestBean testBean = new DerivedPropertyTestBean(1, 1);
    assertEquals(0, ((java.lang.Comparable)(testBean)).compareTo(testBean));
  }
  public void testDerivedPropertyTestBeanEquivalentOperandEquals() {
    assertTrue(new DerivedPropertyTestBean(1, 1).equals(new DerivedPropertyTestBean(1, 1)));
    assertTrue(new DerivedPropertyTestBean(1, 1).equals(new DerivedPropertyTestBean(0, 2)));
    assertTrue(new DerivedPropertyTestBean(1, 1).equals(new DerivedPropertyTestBean(2, 0)));
  }
  public void testDerivedPropertyTestBeanEquivalentOperandCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new DerivedPropertyTestBean(1, 1))).compareTo(new DerivedPropertyTestBean(1, 1)));
    assertEquals(0, ((java.lang.Comparable)(new DerivedPropertyTestBean(1, 1))).compareTo(new DerivedPropertyTestBean(0, 2)));
    assertEquals(0, ((java.lang.Comparable)(new DerivedPropertyTestBean(1, 1))).compareTo(new DerivedPropertyTestBean(2, 0)));
  }
  
  public void testDerivedPropertyTestBeanEquivalentOperandHashCode() {
    assertEquals(new DerivedPropertyTestBean(1, 1).hashCode(), new DerivedPropertyTestBean(1, 1).hashCode());
    assertEquals(new DerivedPropertyTestBean(1, 1).hashCode(), new DerivedPropertyTestBean(0, 2).hashCode());
    assertEquals(new DerivedPropertyTestBean(1, 1).hashCode(), new DerivedPropertyTestBean(2, 0).hashCode());
  }

  public void testDerivedPropertyTestBeanNonEquivalentOperandEquals() {
    assertFalse(new DerivedPropertyTestBean(1, 1).equals(new DerivedPropertyTestBean(-2, -2)));
    assertFalse(new DerivedPropertyTestBean(1, 1).equals(new DerivedPropertyTestBean(0, 1)));
    assertFalse(new DerivedPropertyTestBean(1, 1).equals(new DerivedPropertyTestBean(1, 0)));
    assertFalse(new DerivedPropertyTestBean(1, 1).equals(new DerivedPropertyTestBean(0, 0)));
    assertFalse(new DerivedPropertyTestBean(1, 1).equals(new DerivedPropertyTestBean(10, 10)));
    assertFalse(new DerivedPropertyTestBean(1, 1).equals(new DerivedPropertyTestBean(10, -10)));
  }
  public void testDerivedPropertyTestBeanNonEquivalentOperandCompareTo() {
    assertTrue(((java.lang.Comparable)(new DerivedPropertyTestBean(1,1))).compareTo(new DerivedPropertyTestBean(2, 2)) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DerivedPropertyTestBean(2, 2))).compareTo(new DerivedPropertyTestBean(1,1)));
    assertTrue(((java.lang.Comparable)(new DerivedPropertyTestBean(1, 1))).compareTo(new DerivedPropertyTestBean(10, 11)) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DerivedPropertyTestBean(10, 11))).compareTo(new DerivedPropertyTestBean(1, 1)));
    assertTrue(((java.lang.Comparable)(new DerivedPropertyTestBean(1, 1))).compareTo(new DerivedPropertyTestBean(1, 2)) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DerivedPropertyTestBean(1, 2))).compareTo(new DerivedPropertyTestBean(1, 1)));
  }
}
