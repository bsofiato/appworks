/*
 * CalendarBeanComparableFunctionalTest.java
 * JUnit based test
 *
 * Created on 28 de Julho de 2005, 23:14
 */

package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.support.comparation.CalendarValueComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.DefaultComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.Comparable;
import br.com.appworks.runtime.lang.OrderPolicy;
import br.com.appworks.runtime.lang.ComparationPolicy;
import br.com.appworks.runtime.lang.support.HashTypeMap;
import br.com.appworks.runtime.lang.support.TypeMap;
import br.com.appworks.runtime.lang.support.comparation.CachingComparationStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import java.util.Calendar;
import java.util.TimeZone;
import junit.framework.*;
import org.aspectj.lang.Aspects;

/**
 *
 * @author Bubu
 */
public class CalendarBeanComparableFunctionalTest extends TestCase {
  @Comparable
  private static class ValueCalendarTestBean {
    @Comparable
    Calendar calendar;
    public ValueCalendarTestBean(Calendar calendar) {
      this.calendar = calendar;
    }
  }
  
  @Comparable
  private static class DescendingValueCalendarTestBean {
    @Comparable(value = ComparationPolicy.VALUE, order = OrderPolicy.INVERSE)
    Calendar calendar;
    public DescendingValueCalendarTestBean(Calendar calendar) {
      this.calendar = calendar;
    }
  }
  @Comparable
  private static class IdentityCalendarTestBean {
    @Comparable(value = ComparationPolicy.IDENTITY, order = OrderPolicy.INVERSE)
    Calendar calendar;
    public IdentityCalendarTestBean(Calendar calendar) {
      this.calendar = calendar;
    }
  }

  public CalendarBeanComparableFunctionalTest(String testName) {
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
  }  

  public void testEquivalentValueCalendarTestBeanEquals() {
    Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar calendar2 = (Calendar)(calendar1.clone());
    calendar2.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    assertEquals(new ValueCalendarTestBean(null), new ValueCalendarTestBean(null));
    assertEquals(new ValueCalendarTestBean(calendar1), new ValueCalendarTestBean(calendar1));
    assertEquals(new ValueCalendarTestBean(calendar2), new ValueCalendarTestBean(calendar2));
    assertEquals(new ValueCalendarTestBean(calendar1), new ValueCalendarTestBean(calendar2));
    assertEquals(new ValueCalendarTestBean(calendar2), new ValueCalendarTestBean(calendar1));
  }

  public void testEquivalentValueCalendarTestBeanHashCode() {
    Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar calendar2 = (Calendar)(calendar1.clone());
    calendar2.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    assertEquals(new ValueCalendarTestBean(null).hashCode(), new ValueCalendarTestBean(null).hashCode());
    assertEquals(new ValueCalendarTestBean(calendar1).hashCode(), new ValueCalendarTestBean(calendar1).hashCode());
    assertEquals(new ValueCalendarTestBean(calendar2).hashCode(), new ValueCalendarTestBean(calendar2).hashCode());
    assertEquals(new ValueCalendarTestBean(calendar1).hashCode(), new ValueCalendarTestBean(calendar2).hashCode());
    assertEquals(new ValueCalendarTestBean(calendar2).hashCode(), new ValueCalendarTestBean(calendar1).hashCode());
  }
  
  public void testEquivalentValueCalendarTestBeanCompareTo() {
    Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar calendar2 = (Calendar)(calendar1.clone());
    calendar2.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    assertEquals(0, ((java.lang.Comparable)(new ValueCalendarTestBean(null))).compareTo(new ValueCalendarTestBean(null)));
    assertEquals(0, ((java.lang.Comparable)(new ValueCalendarTestBean(calendar1))).compareTo(new ValueCalendarTestBean(calendar1)));
    assertEquals(0, ((java.lang.Comparable)(new ValueCalendarTestBean(calendar2))).compareTo(new ValueCalendarTestBean(calendar2)));
    assertEquals(0, ((java.lang.Comparable)(new ValueCalendarTestBean(calendar1))).compareTo(new ValueCalendarTestBean(calendar2)));
    assertEquals(0, ((java.lang.Comparable)(new ValueCalendarTestBean(calendar2))).compareTo(new ValueCalendarTestBean(calendar1)));
  }

  public void testNonEquivalentValueCalendarTestBeanEquals() {
    Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar calendar2 = (Calendar)(calendar1.clone());
    calendar2.add(Calendar.MONTH, 1);
    Calendar calendar3 = (Calendar)(calendar1.clone());
    calendar3.add(Calendar.HOUR, -3);
    calendar3.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    assertFalse(new ValueCalendarTestBean(null).equals(new ValueCalendarTestBean(calendar1)));
    assertFalse(new ValueCalendarTestBean(null).equals(new ValueCalendarTestBean(calendar2)));
    assertFalse(new ValueCalendarTestBean(null).equals(new ValueCalendarTestBean(calendar3)));
    assertFalse(new ValueCalendarTestBean(calendar1).equals(new ValueCalendarTestBean(null)));
    assertFalse(new ValueCalendarTestBean(calendar1).equals(new ValueCalendarTestBean(calendar2)));
    assertFalse(new ValueCalendarTestBean(calendar1).equals(new ValueCalendarTestBean(calendar3)));
    assertFalse(new ValueCalendarTestBean(calendar2).equals(new ValueCalendarTestBean(null)));
    assertFalse(new ValueCalendarTestBean(calendar2).equals(new ValueCalendarTestBean(calendar1)));
    assertFalse(new ValueCalendarTestBean(calendar2).equals(new ValueCalendarTestBean(calendar3)));
    assertFalse(new ValueCalendarTestBean(calendar3).equals(new ValueCalendarTestBean(null)));
    assertFalse(new ValueCalendarTestBean(calendar3).equals(new ValueCalendarTestBean(calendar1)));
    assertFalse(new ValueCalendarTestBean(calendar3).equals(new ValueCalendarTestBean(calendar2)));
  }
  
  public void testNonEquivalentValueCalendarTestBeanCompareTo() {
    Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar calendar2 = (Calendar)(calendar1.clone());
    calendar2.add(Calendar.MONTH, 1);
    Calendar calendar3 = (Calendar)(calendar1.clone());
    calendar3.add(Calendar.HOUR, -3);
    calendar3.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    assertTrue(((java.lang.Comparable)(new ValueCalendarTestBean(null))).compareTo(new ValueCalendarTestBean(calendar1)) < 0);
    assertTrue(((java.lang.Comparable)(new ValueCalendarTestBean(null))).compareTo(new ValueCalendarTestBean(calendar2)) < 0);
    assertTrue(((java.lang.Comparable)(new ValueCalendarTestBean(null))).compareTo(new ValueCalendarTestBean(calendar3)) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new ValueCalendarTestBean(calendar1))).compareTo(new ValueCalendarTestBean(null)));
    assertTrue(((java.lang.Comparable)(new ValueCalendarTestBean(calendar1))).compareTo(new ValueCalendarTestBean(calendar2)) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new ValueCalendarTestBean(calendar1))).compareTo(new ValueCalendarTestBean(calendar3)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueCalendarTestBean(calendar2))).compareTo(new ValueCalendarTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueCalendarTestBean(calendar2))).compareTo(new ValueCalendarTestBean(calendar1)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueCalendarTestBean(calendar3))).compareTo(new ValueCalendarTestBean(null)));
    assertTrue(((java.lang.Comparable)(new ValueCalendarTestBean(calendar3))).compareTo(new ValueCalendarTestBean(calendar1)) < 0);
    assertTrue(((java.lang.Comparable)(new ValueCalendarTestBean(calendar3))).compareTo(new ValueCalendarTestBean(calendar2)) < 0);
  }
  
  public void testEquivalentDescendingValueCalendarTestBeanEquals() {
    Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar calendar2 = (Calendar)(calendar1.clone());
    calendar2.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    assertEquals(new DescendingValueCalendarTestBean(null), new DescendingValueCalendarTestBean(null));
    assertEquals(new DescendingValueCalendarTestBean(calendar1), new DescendingValueCalendarTestBean(calendar1));
    assertEquals(new DescendingValueCalendarTestBean(calendar2), new DescendingValueCalendarTestBean(calendar2));
    assertEquals(new DescendingValueCalendarTestBean(calendar1), new DescendingValueCalendarTestBean(calendar2));
    assertEquals(new DescendingValueCalendarTestBean(calendar2), new DescendingValueCalendarTestBean(calendar1));
  }

  public void testEquivalentDescendingValueCalendarTestBeanHashCode() {
    Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar calendar2 = (Calendar)(calendar1.clone());
    calendar2.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    assertEquals(new DescendingValueCalendarTestBean(null).hashCode(), new DescendingValueCalendarTestBean(null).hashCode());
    assertEquals(new DescendingValueCalendarTestBean(calendar1).hashCode(), new DescendingValueCalendarTestBean(calendar1).hashCode());
    assertEquals(new DescendingValueCalendarTestBean(calendar2).hashCode(), new DescendingValueCalendarTestBean(calendar2).hashCode());
    assertEquals(new DescendingValueCalendarTestBean(calendar1).hashCode(), new DescendingValueCalendarTestBean(calendar2).hashCode());
    assertEquals(new DescendingValueCalendarTestBean(calendar2).hashCode(), new DescendingValueCalendarTestBean(calendar1).hashCode());
  }
  
  public void testEquivalentDescendingValueCalendarTestBeanCompareTo() {
    Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar calendar2 = (Calendar)(calendar1.clone());
    calendar2.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueCalendarTestBean(null))).compareTo(new DescendingValueCalendarTestBean(null)));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueCalendarTestBean(calendar1))).compareTo(new DescendingValueCalendarTestBean(calendar1)));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueCalendarTestBean(calendar2))).compareTo(new DescendingValueCalendarTestBean(calendar2)));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueCalendarTestBean(calendar1))).compareTo(new DescendingValueCalendarTestBean(calendar2)));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueCalendarTestBean(calendar2))).compareTo(new DescendingValueCalendarTestBean(calendar1)));
  }

  public void testNonEquivalentDescendingValueCalendarTestBeanEquals() {
    Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar calendar2 = (Calendar)(calendar1.clone());
    calendar2.add(Calendar.MONTH, 1);
    Calendar calendar3 = (Calendar)(calendar1.clone());
    calendar3.add(Calendar.HOUR, -3);
    calendar3.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    assertFalse(new DescendingValueCalendarTestBean(null).equals(new DescendingValueCalendarTestBean(calendar1)));
    assertFalse(new DescendingValueCalendarTestBean(null).equals(new DescendingValueCalendarTestBean(calendar2)));
    assertFalse(new DescendingValueCalendarTestBean(null).equals(new DescendingValueCalendarTestBean(calendar3)));
    assertFalse(new DescendingValueCalendarTestBean(calendar1).equals(new DescendingValueCalendarTestBean(null)));
    assertFalse(new DescendingValueCalendarTestBean(calendar1).equals(new DescendingValueCalendarTestBean(calendar2)));
    assertFalse(new DescendingValueCalendarTestBean(calendar1).equals(new DescendingValueCalendarTestBean(calendar3)));
    assertFalse(new DescendingValueCalendarTestBean(calendar2).equals(new DescendingValueCalendarTestBean(null)));
    assertFalse(new DescendingValueCalendarTestBean(calendar2).equals(new DescendingValueCalendarTestBean(calendar1)));
    assertFalse(new DescendingValueCalendarTestBean(calendar2).equals(new DescendingValueCalendarTestBean(calendar3)));
    assertFalse(new DescendingValueCalendarTestBean(calendar3).equals(new DescendingValueCalendarTestBean(null)));
    assertFalse(new DescendingValueCalendarTestBean(calendar3).equals(new DescendingValueCalendarTestBean(calendar1)));
    assertFalse(new DescendingValueCalendarTestBean(calendar3).equals(new DescendingValueCalendarTestBean(calendar2)));
  }
  
  public void testNonEquivalentDescendingValueCalendarTestBeanCompareTo() {
    Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar calendar2 = (Calendar)(calendar1.clone());
    calendar2.add(Calendar.MONTH, 1);
    Calendar calendar3 = (Calendar)(calendar1.clone());
    calendar3.add(Calendar.HOUR, -3);
    calendar3.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueCalendarTestBean(null))).compareTo(new DescendingValueCalendarTestBean(calendar1)));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueCalendarTestBean(null))).compareTo(new DescendingValueCalendarTestBean(calendar2)));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueCalendarTestBean(null))).compareTo(new DescendingValueCalendarTestBean(calendar3)));
    assertTrue(((java.lang.Comparable)(new DescendingValueCalendarTestBean(calendar1))).compareTo(new DescendingValueCalendarTestBean(null)) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueCalendarTestBean(calendar1))).compareTo(new DescendingValueCalendarTestBean(calendar2)));
    assertTrue(((java.lang.Comparable)(new DescendingValueCalendarTestBean(calendar1))).compareTo(new DescendingValueCalendarTestBean(calendar3)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueCalendarTestBean(calendar2))).compareTo(new DescendingValueCalendarTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueCalendarTestBean(calendar2))).compareTo(new DescendingValueCalendarTestBean(calendar1)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueCalendarTestBean(calendar3))).compareTo(new DescendingValueCalendarTestBean(null)) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueCalendarTestBean(calendar3))).compareTo(new DescendingValueCalendarTestBean(calendar1)));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueCalendarTestBean(calendar3))).compareTo(new DescendingValueCalendarTestBean(calendar2)));
  }
  
  public void testIdentityCalendarTestBeanSanityCheck() {
    Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar calendar2 = (Calendar)(calendar1.clone());
    assertEquals(new IdentityCalendarTestBean(null), new IdentityCalendarTestBean(null));
    assertEquals(new IdentityCalendarTestBean(calendar1), new IdentityCalendarTestBean(calendar1));
    assertEquals(new IdentityCalendarTestBean(calendar2), new IdentityCalendarTestBean(calendar2));
    assertFalse(new IdentityCalendarTestBean(null).equals(new IdentityCalendarTestBean(calendar1)));
    assertFalse(new IdentityCalendarTestBean(null).equals(new IdentityCalendarTestBean(calendar2)));
    assertFalse(new IdentityCalendarTestBean(calendar1).equals(new IdentityCalendarTestBean(null)));
    assertFalse(new IdentityCalendarTestBean(calendar1).equals(new IdentityCalendarTestBean(calendar2)));
    assertFalse(new IdentityCalendarTestBean(calendar2).equals(new IdentityCalendarTestBean(null)));
    assertFalse(new IdentityCalendarTestBean(calendar2).equals(new IdentityCalendarTestBean(calendar1)));
  }
}
