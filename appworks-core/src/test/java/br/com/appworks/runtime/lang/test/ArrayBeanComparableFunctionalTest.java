/*
 * ArrayBeanComparableFunctionalTest.java
 * JUnit based test
 *
 * Created on 31 de Julho de 2005, 23:01
 */

package br.com.appworks.runtime.lang.test;
import br.com.appworks.runtime.lang.Comparable;
import br.com.appworks.runtime.lang.OrderPolicy;
import br.com.appworks.runtime.lang.ComparationPolicy;
import br.com.appworks.runtime.lang.support.comparation.CachingComparationStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.comparation.DefaultComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.*;
import org.aspectj.lang.Aspects;


public class ArrayBeanComparableFunctionalTest extends TestCase {
  @Comparable
  private static class ValueArrayTestBean {
    @Comparable
    String [] array;
    public ValueArrayTestBean(String [] array) {
      this.array = array;
    }
  }
  
  @Comparable
  private static class DescendingValueArrayTestBean {
    @Comparable(value = ComparationPolicy.VALUE, order = OrderPolicy.INVERSE)
    String [] array;
    public DescendingValueArrayTestBean(String [] array) {
      this.array = array;
    }
  }

  @Comparable
  private static class NonComparableValueArrayTestBean {
    @Comparable
    Object [] array;
    public NonComparableValueArrayTestBean(Object [] array) {
      this.array = array;
    }
  }

  @Comparable
  private static class IdentityArrayTestBean {
    @Comparable(value = ComparationPolicy.IDENTITY, order = OrderPolicy.INVERSE)
    Object [] array;
    public IdentityArrayTestBean(Object [] array) {
      this.array = array;
    }
  }

  public ArrayBeanComparableFunctionalTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.comparation.aop.ComparationAspect");
    klass.getDeclaredMethod("setComparationStrategyFactory", ComparationStrategyFactory.class).invoke(Aspects.aspectOf(klass), new CachingComparationStrategyFactoryAdapter(new DefaultComparationStrategyFactory(new DefaultPropertyAccessStrategyFactory(new ReflectionBasedPropertyGettingStrategyFactory(), new ReflectionBasedPropertySettingStrategyFactory()))));
  }

  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.comparation.aop.ComparationAspect");
    klass.getDeclaredMethod("setComparationStrategyFactory", ComparationStrategyFactory.class).invoke(Aspects.aspectOf(klass), new Object [] { null });
  }  
  
  private void checkNonComparableCompareTo(NonComparableValueArrayTestBean op1, NonComparableValueArrayTestBean op2) {
    try {
      ((java.lang.Comparable)(op1)).compareTo(op2);
      fail();
    } catch (ClassCastException ex) {
    }
  }
  
  public void testEquivalentValueArrayTestBeanEquals() {
    assertEquals(new ValueArrayTestBean(null), new ValueArrayTestBean(null));
    assertEquals(new ValueArrayTestBean(new String [0]), new ValueArrayTestBean(new String [0]));
    assertEquals(new ValueArrayTestBean(new String [] { null }), new ValueArrayTestBean(new String [] { null }));
    assertEquals(new ValueArrayTestBean(new String [] { "X" }), new ValueArrayTestBean(new String [] { "X" }));
    assertEquals(new ValueArrayTestBean(new String [] { "Y" }), new ValueArrayTestBean(new String [] { "Y" }));
    assertEquals(new ValueArrayTestBean(new String [] { "X", "X" }), new ValueArrayTestBean(new String [] { "X", "X" }));
    assertEquals(new ValueArrayTestBean(new String [] { new String("X") }), new ValueArrayTestBean(new String [] { new String("X") }));
  }

  public void testEquivalentValueArrayTestBeanHashCode() {
    assertEquals(new ValueArrayTestBean(null).hashCode(), new ValueArrayTestBean(null).hashCode());
    assertEquals(new ValueArrayTestBean(new String [0]).hashCode(), new ValueArrayTestBean(new String [0]).hashCode());
    assertEquals(new ValueArrayTestBean(new String [] { null }).hashCode(), new ValueArrayTestBean(new String [] { null }).hashCode());
    assertEquals(new ValueArrayTestBean(new String [] { "X" }).hashCode(), new ValueArrayTestBean(new String [] { "X" }).hashCode());
    assertEquals(new ValueArrayTestBean(new String [] { "Y" }).hashCode(), new ValueArrayTestBean(new String [] { "Y" }).hashCode());
    assertEquals(new ValueArrayTestBean(new String [] { "X", "X" }).hashCode(), new ValueArrayTestBean(new String [] { "X", "X" }).hashCode());
    assertEquals(new ValueArrayTestBean(new String [] { new String("X") }).hashCode(), new ValueArrayTestBean(new String [] { new String("X") }).hashCode());
  }
  
  public void testEquivalentValueArrayTestBeanCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(null)));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new String [0]))).compareTo(new ValueArrayTestBean(new String [0])));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { null }))).compareTo(new ValueArrayTestBean(new String [] { null })));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "X" }))).compareTo(new ValueArrayTestBean(new String [] { "X" })));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "Y" }))).compareTo(new ValueArrayTestBean(new String [] { "Y" })));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "X", "X" }))).compareTo(new ValueArrayTestBean(new String [] { "X", "X" })));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { new String("X") }))).compareTo( new ValueArrayTestBean(new String [] { new String("X") })));
  }

  public void testNonEquivalentValueArrayTestBeanEquals() {
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new String [0])));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new String [] { null })));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new String [] { "X" })));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new String [] { "Y" })));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new String [] { "X", "X" })));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new String [] { new String("X") })));

    assertFalse(new ValueArrayTestBean(new String [0]).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new String [0]).equals(new ValueArrayTestBean(new String [] { null })));
    assertFalse(new ValueArrayTestBean(new String [0]).equals(new ValueArrayTestBean(new String [] { "X" })));
    assertFalse(new ValueArrayTestBean(new String [0]).equals(new ValueArrayTestBean(new String [] { "Y" })));
    assertFalse(new ValueArrayTestBean(new String [0]).equals(new ValueArrayTestBean(new String [] { "X", "X" })));
    assertFalse(new ValueArrayTestBean(new String [0]).equals(new ValueArrayTestBean(new String [] { new String("X") })));

    assertFalse(new ValueArrayTestBean(new String [] { null }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new String [] { null }).equals(new ValueArrayTestBean(new String [0])));
    assertFalse(new ValueArrayTestBean(new String [] { null }).equals(new ValueArrayTestBean(new String [] { "X" })));
    assertFalse(new ValueArrayTestBean(new String [] { null }).equals(new ValueArrayTestBean(new String [] { "Y" })));
    assertFalse(new ValueArrayTestBean(new String [] { null }).equals(new ValueArrayTestBean(new String [] { "X", "X" })));
    assertFalse(new ValueArrayTestBean(new String [] { null }).equals(new ValueArrayTestBean(new String [] { new String("X") })));

    assertFalse(new ValueArrayTestBean(new String [] { "X" }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new String [] { "X" }).equals(new ValueArrayTestBean(new String [0])));
    assertFalse(new ValueArrayTestBean(new String [] { "X" }).equals(new ValueArrayTestBean(new String [] { null })));
    assertFalse(new ValueArrayTestBean(new String [] { "X" }).equals(new ValueArrayTestBean(new String [] { "Y" })));
    assertFalse(new ValueArrayTestBean(new String [] { "X" }).equals(new ValueArrayTestBean(new String [] { "X", "X" })));

    assertFalse(new ValueArrayTestBean(new String [] { "Y" }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new String [] { "Y" }).equals(new ValueArrayTestBean(new String [0])));
    assertFalse(new ValueArrayTestBean(new String [] { "Y" }).equals(new ValueArrayTestBean(new String [] { null })));
    assertFalse(new ValueArrayTestBean(new String [] { "Y" }).equals(new ValueArrayTestBean(new String [] { "X" })));
    assertFalse(new ValueArrayTestBean(new String [] { "Y" }).equals(new ValueArrayTestBean(new String [] { "X", "X" })));
    assertFalse(new ValueArrayTestBean(new String [] { "Y" }).equals(new ValueArrayTestBean(new String [] { new String("X") })));

    assertFalse(new ValueArrayTestBean(new String [] { "X", "X" }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new String [] { "X", "X" }).equals(new ValueArrayTestBean(new String [0])));
    assertFalse(new ValueArrayTestBean(new String [] { "X", "X" }).equals(new ValueArrayTestBean(new String [] { null })));
    assertFalse(new ValueArrayTestBean(new String [] { "X", "X" }).equals(new ValueArrayTestBean(new String [] { "X" })));
    assertFalse(new ValueArrayTestBean(new String [] { "X", "X" }).equals(new ValueArrayTestBean(new String [] { "Y" })));
    assertFalse(new ValueArrayTestBean(new String [] { "X", "X" }).equals(new ValueArrayTestBean(new String [] { new String("X") })));

    assertFalse(new ValueArrayTestBean(new String [] { new String("X") }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new String [] { new String("X") }).equals(new ValueArrayTestBean(new String [0])));
    assertFalse(new ValueArrayTestBean(new String [] { new String("X") }).equals(new ValueArrayTestBean(new String [] { null })));
    assertFalse(new ValueArrayTestBean(new String [] { new String("X") }).equals(new ValueArrayTestBean(new String [] { "Y" })));
    assertFalse(new ValueArrayTestBean(new String [] { new String("X") }).equals(new ValueArrayTestBean(new String [] { "X", "X" })));
  }
  
  public void testNonEquivalentValueArrayTestBeanCompareTo() {
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new String [0])) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new String [] { null })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new String [] { "X" })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new String [] { "Y" })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new String [] { "X", "X" })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [0]))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new String [0]))).compareTo(new ValueArrayTestBean(new String [] { null })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new String [0]))).compareTo(new ValueArrayTestBean(new String [] { "X" })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new String [0]))).compareTo(new ValueArrayTestBean(new String [] { "Y" })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new String [0]))).compareTo(new ValueArrayTestBean(new String [] { "X", "X" })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { null }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { null }))).compareTo(new ValueArrayTestBean(new String [0])));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new String [] { null }))).compareTo(new ValueArrayTestBean(new String [] { "X" })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new String [] { null }))).compareTo(new ValueArrayTestBean(new String [] { "Y" })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new String [] { null }))).compareTo(new ValueArrayTestBean(new String [] { "X", "X" })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "X" }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "X" }))).compareTo(new ValueArrayTestBean(new String [0])));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "X" }))).compareTo(new ValueArrayTestBean(new String [] { null })));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "X" }))).compareTo(new ValueArrayTestBean(new String [] { "Y" })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "X" }))).compareTo(new ValueArrayTestBean(new String [] { "X", "X" })) < 0);
    
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "Y" }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "Y" }))).compareTo(new ValueArrayTestBean(new String [0])));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "Y" }))).compareTo(new ValueArrayTestBean(new String [] { null })));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "Y" }))).compareTo(new ValueArrayTestBean(new String [] { "X" })));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "Y" }))).compareTo(new ValueArrayTestBean(new String [] { "X", "X" })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "X", "X" }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "X", "X" }))).compareTo(new ValueArrayTestBean(new String [0])));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "X", "X" }))).compareTo(new ValueArrayTestBean(new String [] { null })));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "X", "X" }))).compareTo(new ValueArrayTestBean(new String [] { "X" })));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new String [] { "X", "X" }))).compareTo(new ValueArrayTestBean(new String [] { "Y" })));
  }
  
   public void testEquivalentDescendingValueArrayTestBeanEquals() {
    assertEquals(new DescendingValueArrayTestBean(null), new DescendingValueArrayTestBean(null));
    assertEquals(new DescendingValueArrayTestBean(new String [0]), new DescendingValueArrayTestBean(new String [0]));
    assertEquals(new DescendingValueArrayTestBean(new String [] { null }), new DescendingValueArrayTestBean(new String [] { null }));
    assertEquals(new DescendingValueArrayTestBean(new String [] { "X" }), new DescendingValueArrayTestBean(new String [] { "X" }));
    assertEquals(new DescendingValueArrayTestBean(new String [] { "Y" }), new DescendingValueArrayTestBean(new String [] { "Y" }));
    assertEquals(new DescendingValueArrayTestBean(new String [] { "X", "X" }), new DescendingValueArrayTestBean(new String [] { "X", "X" }));
    assertEquals(new DescendingValueArrayTestBean(new String [] { new String("X") }), new DescendingValueArrayTestBean(new String [] { new String("X") }));
  }

  public void testDescendingValueArrayTestBeanBeanHashCode() {
    assertEquals(new DescendingValueArrayTestBean(null).hashCode(), new DescendingValueArrayTestBean(null).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new String [0]).hashCode(), new DescendingValueArrayTestBean(new String [0]).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new String [] { null }).hashCode(), new DescendingValueArrayTestBean(new String [] { null }).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new String [] { "X" }).hashCode(), new DescendingValueArrayTestBean(new String [] { "X" }).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new String [] { "Y" }).hashCode(), new DescendingValueArrayTestBean(new String [] { "Y" }).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new String [] { "X", "X" }).hashCode(), new DescendingValueArrayTestBean(new String [] { "X", "X" }).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new String [] { new String("X") }).hashCode(), new DescendingValueArrayTestBean(new String [] { new String("X") }).hashCode());
  }
  
  public void testEquivalentDescendingValueArrayTestBeanCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(null)));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [0]))).compareTo(new DescendingValueArrayTestBean(new String [0])));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { null }))).compareTo(new DescendingValueArrayTestBean(new String [] { null })));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "X" }))).compareTo(new DescendingValueArrayTestBean(new String [] { "X" })));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "Y" }))).compareTo(new DescendingValueArrayTestBean(new String [] { "Y" })));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "X", "X" }))).compareTo(new DescendingValueArrayTestBean(new String [] { "X", "X" })));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { new String("X") }))).compareTo( new DescendingValueArrayTestBean(new String [] { new String("X") })));
  }

  public void testNonEquivalentDescendingValueArrayTestBeanEquals() {
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new String [0])));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new String [] { null })));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new String [] { "X" })));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new String [] { "Y" })));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new String [] { "X", "X" })));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new String [] { new String("X") })));

    assertFalse(new DescendingValueArrayTestBean(new String [0]).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new String [0]).equals(new DescendingValueArrayTestBean(new String [] { null })));
    assertFalse(new DescendingValueArrayTestBean(new String [0]).equals(new DescendingValueArrayTestBean(new String [] { "X" })));
    assertFalse(new DescendingValueArrayTestBean(new String [0]).equals(new DescendingValueArrayTestBean(new String [] { "Y" })));
    assertFalse(new DescendingValueArrayTestBean(new String [0]).equals(new DescendingValueArrayTestBean(new String [] { "X", "X" })));
    assertFalse(new DescendingValueArrayTestBean(new String [0]).equals(new DescendingValueArrayTestBean(new String [] { new String("X") })));

    assertFalse(new DescendingValueArrayTestBean(new String [] { null }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new String [] { null }).equals(new DescendingValueArrayTestBean(new String [0])));
    assertFalse(new DescendingValueArrayTestBean(new String [] { null }).equals(new DescendingValueArrayTestBean(new String [] { "X" })));
    assertFalse(new DescendingValueArrayTestBean(new String [] { null }).equals(new DescendingValueArrayTestBean(new String [] { "Y" })));
    assertFalse(new DescendingValueArrayTestBean(new String [] { null }).equals(new DescendingValueArrayTestBean(new String [] { "X", "X" })));
    assertFalse(new DescendingValueArrayTestBean(new String [] { null }).equals(new DescendingValueArrayTestBean(new String [] { new String("X") })));

    assertFalse(new DescendingValueArrayTestBean(new String [] { "X" }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new String [] { "X" }).equals(new DescendingValueArrayTestBean(new String [0])));
    assertFalse(new DescendingValueArrayTestBean(new String [] { "X" }).equals(new DescendingValueArrayTestBean(new String [] { null })));
    assertFalse(new DescendingValueArrayTestBean(new String [] { "X" }).equals(new DescendingValueArrayTestBean(new String [] { "Y" })));
    assertFalse(new DescendingValueArrayTestBean(new String [] { "X" }).equals(new DescendingValueArrayTestBean(new String [] { "X", "X" })));

    assertFalse(new DescendingValueArrayTestBean(new String [] { "Y" }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new String [] { "Y" }).equals(new DescendingValueArrayTestBean(new String [] { null })));
    assertFalse(new DescendingValueArrayTestBean(new String [] { "Y" }).equals(new DescendingValueArrayTestBean(new String [] { "X" })));
    assertFalse(new DescendingValueArrayTestBean(new String [] { "Y" }).equals(new DescendingValueArrayTestBean(new String [] { "X", "X" })));
    assertFalse(new DescendingValueArrayTestBean(new String [] { "Y" }).equals(new DescendingValueArrayTestBean(new String [] { new String("X") })));

    assertFalse(new DescendingValueArrayTestBean(new String [] { "X", "X" }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new String [] { "X", "X" }).equals(new DescendingValueArrayTestBean(new String [0])));
    assertFalse(new DescendingValueArrayTestBean(new String [] { "X", "X" }).equals(new DescendingValueArrayTestBean(new String [] { null })));
    assertFalse(new DescendingValueArrayTestBean(new String [] { "X", "X" }).equals(new DescendingValueArrayTestBean(new String [] { "X" })));
    assertFalse(new DescendingValueArrayTestBean(new String [] { "X", "X" }).equals(new DescendingValueArrayTestBean(new String [] { "Y" })));
    assertFalse(new DescendingValueArrayTestBean(new String [] { "X", "X" }).equals(new DescendingValueArrayTestBean(new String [] { new String("X") })));

    assertFalse(new DescendingValueArrayTestBean(new String [] { new String("X") }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new String [] { new String("X") }).equals(new DescendingValueArrayTestBean(new String [0])));
    assertFalse(new DescendingValueArrayTestBean(new String [] { new String("X") }).equals(new DescendingValueArrayTestBean(new String [] { null })));
    assertFalse(new DescendingValueArrayTestBean(new String [] { new String("X") }).equals(new DescendingValueArrayTestBean(new String [] { "Y" })));
    assertFalse(new DescendingValueArrayTestBean(new String [] { new String("X") }).equals(new DescendingValueArrayTestBean(new String [] { "X", "X" })));
  }
  
  public void testNonEquivalentDescendingValueArrayTestBeanCompareTo() {
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new String [0])));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new String [] { null })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new String [] { "X" })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new String [] { "Y" })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new String [] { "X", "X" })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [0]))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [0]))).compareTo(new DescendingValueArrayTestBean(new String [] { null })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [0]))).compareTo(new DescendingValueArrayTestBean(new String [] { "X" })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [0]))).compareTo(new DescendingValueArrayTestBean(new String [] { "Y" })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [0]))).compareTo(new DescendingValueArrayTestBean(new String [] { "X", "X" })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { null }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { null }))).compareTo(new DescendingValueArrayTestBean(new String [0])) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { null }))).compareTo(new DescendingValueArrayTestBean(new String [] { "X" })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { null }))).compareTo(new DescendingValueArrayTestBean(new String [] { "Y" })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { null }))).compareTo(new DescendingValueArrayTestBean(new String [] { "X", "X" })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "X" }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "X" }))).compareTo(new DescendingValueArrayTestBean(new String [0])) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "X" }))).compareTo(new DescendingValueArrayTestBean(new String [] { null })) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "X" }))).compareTo(new DescendingValueArrayTestBean(new String [] { "Y" })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "X" }))).compareTo(new DescendingValueArrayTestBean(new String [] { "X", "X" })));
    
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "Y" }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "Y" }))).compareTo(new DescendingValueArrayTestBean(new String [0])) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "Y" }))).compareTo(new DescendingValueArrayTestBean(new String [] { null })) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "Y" }))).compareTo(new DescendingValueArrayTestBean(new String [] { "X" })) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "Y" }))).compareTo(new DescendingValueArrayTestBean(new String [] { "X", "X" })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "X", "X" }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "X", "X" }))).compareTo(new DescendingValueArrayTestBean(new String [0])) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "X", "X" }))).compareTo(new DescendingValueArrayTestBean(new String [] { null })) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "X", "X" }))).compareTo(new DescendingValueArrayTestBean(new String [] { "X" })) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new String [] { "X", "X" }))).compareTo(new DescendingValueArrayTestBean(new String [] { "Y" })) < 0);
  }

  public void testArrayListEqualsSanityCheck() {
    assertTrue(new ArrayList <String> ().equals(new ArrayList <String> ()));
    assertTrue(new ArrayList <String> (Arrays.asList(new String [] { "X" })).equals(new ArrayList <String> (Arrays.asList(new String [] { "X" }))));
    assertFalse(new ArrayList <String> (Arrays.asList(new String [] { "X" })).equals(new ArrayList <String> (Arrays.asList(new String [] { "Y" }))));
  }
  
  public void testArrayListHashCodeSanityCheck() {
    assertEquals(new ArrayList <String> ().hashCode(), new ArrayList <String> ().hashCode());
    assertEquals(new ArrayList <String> (Arrays.asList(new String [] { "X" })).hashCode(), new ArrayList <String> (Arrays.asList(new String [] { "X" })).hashCode());
  }

  public void testArrayListCompareToSanityCheck() {
    assertFalse(new ArrayList <String> () instanceof java.lang.Comparable);
  }

  public void testEquivalentNonComparableValueArrayTestBeanEquals() {
    assertEquals(new NonComparableValueArrayTestBean(null), new NonComparableValueArrayTestBean(null));
    assertEquals(new NonComparableValueArrayTestBean(new Object [0]), new NonComparableValueArrayTestBean(new Object [0]));
    assertEquals(new NonComparableValueArrayTestBean(new Object [] { null }), new NonComparableValueArrayTestBean(new Object [] { null }));
    assertEquals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }));
    assertEquals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}));
    assertEquals(new NonComparableValueArrayTestBean(new Object [] {  new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}), 
                 new NonComparableValueArrayTestBean(new Object [] {  new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}));
  }

  public void testEquivalentNonComparableValueArrayTestBeanHashCode() {
    assertEquals(new NonComparableValueArrayTestBean(null).hashCode(), new NonComparableValueArrayTestBean(null).hashCode());
    assertEquals(new NonComparableValueArrayTestBean(new Object [0]).hashCode(), new NonComparableValueArrayTestBean(new Object [0]).hashCode());
    assertEquals(new NonComparableValueArrayTestBean(new Object [] { null }).hashCode(), new NonComparableValueArrayTestBean(new Object [] { null }).hashCode());
    assertEquals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }).hashCode(), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }).hashCode());
    assertEquals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}).hashCode(), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}).hashCode());
    assertEquals(new NonComparableValueArrayTestBean(new Object [] {  new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}).hashCode(), 
                 new NonComparableValueArrayTestBean(new Object [] {  new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}).hashCode());
  }

  public void testEquivalentNonComparableValueArrayTestBeanCompareTo() {
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(null), new NonComparableValueArrayTestBean(null));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [0]), new NonComparableValueArrayTestBean(new Object [0]));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { null }), new NonComparableValueArrayTestBean(new Object [] { null }));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" })) }), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" })) }));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" })) }), 
                                new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" })) }));
  }

  public void testNonEquivalentNonComparableValueArrayTestBeanEquals() {
    assertFalse(new NonComparableValueArrayTestBean(null).equals(new NonComparableValueArrayTestBean(new Object [0])));
    assertFalse(new NonComparableValueArrayTestBean(null).equals(new NonComparableValueArrayTestBean(new Object [] { null })));
    assertFalse(new NonComparableValueArrayTestBean(null).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () })));
    assertFalse(new NonComparableValueArrayTestBean(null).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))})));
    assertFalse(new NonComparableValueArrayTestBean(null).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))})));

    assertFalse(new NonComparableValueArrayTestBean(new Object [0]).equals(new NonComparableValueArrayTestBean(null)));
    assertFalse(new NonComparableValueArrayTestBean(new Object [0]).equals(new NonComparableValueArrayTestBean(new Object [] { null })));
    assertFalse(new NonComparableValueArrayTestBean(new Object [0]).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () })));
    assertFalse(new NonComparableValueArrayTestBean(new Object [0]).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))})));
    assertFalse(new NonComparableValueArrayTestBean(new Object [0]).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))})));

    assertFalse(new NonComparableValueArrayTestBean(new Object [] { null }).equals(new NonComparableValueArrayTestBean(null)));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { null }).equals(new NonComparableValueArrayTestBean(new Object [0])));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { null }).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () })));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { null }).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))})));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { null }).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))})));

    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }).equals(new NonComparableValueArrayTestBean(new Object [] { null })));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }).equals(new NonComparableValueArrayTestBean(new Object [0])));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }).equals(new NonComparableValueArrayTestBean(new Object [] { null })));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))})));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))})));

    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}).equals(new NonComparableValueArrayTestBean(null)));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}).equals(new NonComparableValueArrayTestBean(new Object [0])));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}).equals(new NonComparableValueArrayTestBean(new Object [] { null })));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () })));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))})));

    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}).equals(new NonComparableValueArrayTestBean(null)));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}).equals(new NonComparableValueArrayTestBean(new Object [0])));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}).equals(new NonComparableValueArrayTestBean(new Object [] { null })));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () })));
    assertFalse(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}).equals(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))})));
  }

   public void testNonEquivalentNonComparableValueArrayTestBeanCompareTo() {
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(null), new NonComparableValueArrayTestBean(new Object [0]));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(null), new NonComparableValueArrayTestBean(new Object [] { null }));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(null), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(null), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(null), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}));

    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [0]), new NonComparableValueArrayTestBean(null));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [0]), new NonComparableValueArrayTestBean(new Object [] { null }));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [0]), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [0]), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [0]), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}));

    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { null }), new NonComparableValueArrayTestBean(null));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { null }), new NonComparableValueArrayTestBean(new Object [0]));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { null }), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { null }), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { null }), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}));

    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }), new NonComparableValueArrayTestBean(new Object [] { null }));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }), new NonComparableValueArrayTestBean(new Object [0]));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }), new NonComparableValueArrayTestBean(new Object [] { null }));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}));

    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}), new NonComparableValueArrayTestBean(null));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}), new NonComparableValueArrayTestBean(new Object [0]));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}), new NonComparableValueArrayTestBean(new Object [] { null }));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}));

    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}), new NonComparableValueArrayTestBean(null));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}), new NonComparableValueArrayTestBean(new Object [0]));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}), new NonComparableValueArrayTestBean(new Object [] { null }));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> () }));
    checkNonComparableCompareTo(new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (), new ArrayList <String> (Arrays.asList(new String [] { "X" }))}), new NonComparableValueArrayTestBean(new Object [] { new ArrayList <String> (Arrays.asList(new String [] { "X" }))}));
  }

  public void testEquivalentIdentityArrayTestBeanEquals() {
    Object [] array1 = new Object [] { null };
    Object [] array2 = new Object [] { new Object() };
    Object [] array3 = new Object [] { new Object(), new Object() };
    assertEquals(new IdentityArrayTestBean(null), new IdentityArrayTestBean(null));
    assertEquals(new IdentityArrayTestBean(array1), new IdentityArrayTestBean(array1));
    assertEquals(new IdentityArrayTestBean(array2), new IdentityArrayTestBean(array2));
    assertEquals(new IdentityArrayTestBean(array3), new IdentityArrayTestBean(array3));
  }
  
  public void testEquivalentIdentityArrayTestBeanHashCode() {
    Object [] array1 = new Object [] { null };
    Object [] array2 = new Object [] { new Object() };
    Object [] array3 = new Object [] { new Object(), new Object() };
    assertEquals(new IdentityArrayTestBean(null).hashCode(), new IdentityArrayTestBean(null).hashCode());
    assertEquals(new IdentityArrayTestBean(array1).hashCode(), new IdentityArrayTestBean(array1).hashCode());
    assertEquals(new IdentityArrayTestBean(array2).hashCode(), new IdentityArrayTestBean(array2).hashCode());
    assertEquals(new IdentityArrayTestBean(array3).hashCode(), new IdentityArrayTestBean(array3).hashCode());
  }

  public void testEquivalentIdentityArrayTestBeanCompareTo() {
    Object [][] arrays = new Object [][] { null,
                                           new Object [] { null },
                                           new Object [] { new Object() },
                                           new Object [] { new Object(), new Object() }};
    for (Object [] array : arrays) {
      assertEquals(0, ((java.lang.Comparable)(new IdentityArrayTestBean(array))).compareTo(new IdentityArrayTestBean(array)));
    }
  }

  public void testNonEquivalentIdentityArrayTestBeanEquals() {
    Object [] array1 = new Object [] { null };
    Object [] array2 = new Object [] { new Object() };
    Object [] array3 = new Object [] { new Object(), new Object() };
    assertFalse(new IdentityArrayTestBean(null).equals(new IdentityArrayTestBean(array1)));
    assertFalse(new IdentityArrayTestBean(null).equals(new IdentityArrayTestBean(array2)));
    assertFalse(new IdentityArrayTestBean(null).equals(new IdentityArrayTestBean(array3)));
    assertFalse(new IdentityArrayTestBean(array1).equals(new IdentityArrayTestBean(null)));
    assertFalse(new IdentityArrayTestBean(array1).equals(new IdentityArrayTestBean(new Object [] { null })));
    assertFalse(new IdentityArrayTestBean(array1).equals(new IdentityArrayTestBean(array2)));
    assertFalse(new IdentityArrayTestBean(array1).equals(new IdentityArrayTestBean(array3)));
    assertFalse(new IdentityArrayTestBean(array2).equals(new IdentityArrayTestBean(null)));
    assertFalse(new IdentityArrayTestBean(array2).equals(new IdentityArrayTestBean(array1)));
    assertFalse(new IdentityArrayTestBean(array2).equals(new IdentityArrayTestBean(new Object [] { new Object() })));
    assertFalse(new IdentityArrayTestBean(array2).equals(new IdentityArrayTestBean(new Object [] { array2[0] })));
    assertFalse(new IdentityArrayTestBean(array2).equals(new IdentityArrayTestBean(array3)));
    assertFalse(new IdentityArrayTestBean(array3).equals(new IdentityArrayTestBean(null)));
    assertFalse(new IdentityArrayTestBean(array3).equals(new IdentityArrayTestBean(array1)));
    assertFalse(new IdentityArrayTestBean(array3).equals(new IdentityArrayTestBean(array2)));
    assertFalse(new IdentityArrayTestBean(array3).equals(new IdentityArrayTestBean(new Object [] { new Object(), new Object() })));
    assertFalse(new IdentityArrayTestBean(array3).equals(new IdentityArrayTestBean(new Object [] { array3[0], array3[0] })));
  }

  public void testNonEquivalentIdentityArrayTestBeanCompareTo() {
    Object [][] arrays = new Object [][] { null,
                                           new Object [] { null },
                                           new Object [] { new Object() },
                                           new Object [] { new Object(), new Object() }};
    for (Object [] array1 : arrays) {
      for (Object [] array2 : arrays) {
        if (array1 == array2) {
          if (array1 == null) {
            continue;
          }
          array2 = array1.clone();
        }
        
        if (((array1 == null) ? 0 : array1.hashCode()) < 
            ((array2 == null) ? 0 : array2.hashCode())) {
          assertTrue(0 < ((java.lang.Comparable)(new IdentityArrayTestBean(array1))).compareTo(new IdentityArrayTestBean(array2)));
        } else {
          assertTrue(((java.lang.Comparable)(new IdentityArrayTestBean(array1))).compareTo(new IdentityArrayTestBean(array2)) < 0);
        }
      }
    }
  }

}