/*
 * SimpleBeanComparableFunctionalTest.java
 * JUnit based test
 *
 * Created on 23 de Julho de 2005, 00:43
 */

package br.com.appworks.runtime.lang.test;

import junit.framework.*;
import br.com.appworks.runtime.lang.Comparable;
import br.com.appworks.runtime.lang.OrderPolicy;
import br.com.appworks.runtime.lang.ComparationPolicy;
import br.com.appworks.runtime.lang.support.comparation.CachingComparationStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.comparation.DefaultComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.aspectj.lang.Aspects;

/**
 *
 * @author Bubu
 */
public class SimpleBeanComparableFunctionalTest extends TestCase {
  
  @Comparable  
  private static class ValuePropertyBean implements Serializable  {
    @Comparable
    private String x;
    public ValuePropertyBean() {
      this(null);
    }
    public ValuePropertyBean(String x) {
      this.x = x;
    }
  }
  
  @Comparable
  private static class DescendingOrderValuePropertyBean implements Serializable  {
    @Comparable(order = OrderPolicy.INVERSE)
    private String x;
    public DescendingOrderValuePropertyBean() {
      this(null);
    }
    public DescendingOrderValuePropertyBean(String x) {
      this.x = x;
    }
  }
  
  @Comparable
  private static class IdentityPropertyBean implements Serializable  {
    @Comparable(ComparationPolicy.IDENTITY)
    private String x;
    public IdentityPropertyBean() {
      this(null);
    }
    public IdentityPropertyBean(String x) {
      this.x = x;
    }
  }
  
  @Comparable
  private static class DescendingOrderIdentityPropertyBean implements Serializable  {
    @Comparable(value=ComparationPolicy.IDENTITY, order=OrderPolicy.INVERSE)
    private String x;
    public DescendingOrderIdentityPropertyBean() {
      this(null);
    }
    public DescendingOrderIdentityPropertyBean(String x) {
      this.x = x;
    }
  }
  
  public SimpleBeanComparableFunctionalTest(String testName) {
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
  
  public void testValuePropertyBeanNullOperandEquals() {
    assertFalse(new ValuePropertyBean().equals(null));
  }
  public void testValuePropertyBeanNullOperandCompareTo() {
    try {
      ((java.lang.Comparable)(new ValuePropertyBean())).compareTo(null);
      fail();
    } catch (NullPointerException ex) {
    }
  }
  public void testValuePropertyBeanTypeMismatchOperandEquals() {
    assertFalse(new ValuePropertyBean().equals("XXX"));
  }
  public void testValuePropertyBeanTypeMismatchOperandCompareTo() {
    try {
      ((java.lang.Comparable)(new ValuePropertyBean())).compareTo("XXX");
      fail();
    } catch (ClassCastException ex) {
    }
  }

  public void testValuePropertyBeanSameOperandEquals() {
    ValuePropertyBean testBean = new ValuePropertyBean();
    assertTrue(testBean.equals(testBean));
  }
  public void testValuePropertyBeanSameOperandCompareTo() {
    ValuePropertyBean testBean = new ValuePropertyBean();
    assertEquals(0, ((java.lang.Comparable)(testBean)).compareTo(testBean));
  }
  public void testValuePropertyBeanEquivalentOperandEquals() {
    assertTrue(new ValuePropertyBean().equals(new ValuePropertyBean()));
    assertTrue(new ValuePropertyBean("X").equals(new ValuePropertyBean("X")));
    assertTrue(new ValuePropertyBean(new String("X")).equals(new ValuePropertyBean(new String("X"))));
  }
  public void testValuePropertyBeanEquivalentOperandCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new ValuePropertyBean())).compareTo(new ValuePropertyBean()));
    assertEquals(0, ((java.lang.Comparable)(new ValuePropertyBean("X"))).compareTo(new ValuePropertyBean("X")));
    assertEquals(0, ((java.lang.Comparable)(new ValuePropertyBean(new String("X")))).compareTo(new ValuePropertyBean(new String("X"))));
  }
  
  public void testValuePropertyBeanEquivalentOperandHashCode() {
    assertEquals(new ValuePropertyBean().hashCode(), new ValuePropertyBean().hashCode());
    assertEquals(new ValuePropertyBean("X").hashCode(), new ValuePropertyBean("X").hashCode());
    assertEquals(new ValuePropertyBean(new String("X")).hashCode(), new ValuePropertyBean(new String("X")).hashCode());
  }

  public void testValuePropertyBeanNonEquivalentOperandEquals() {
    assertFalse(new ValuePropertyBean().equals(new ValuePropertyBean("X")));
    assertFalse(new ValuePropertyBean("X").equals(new ValuePropertyBean("Y")));
    assertFalse(new ValuePropertyBean("Y").equals(new ValuePropertyBean("X")));
    assertFalse(new ValuePropertyBean("X").equals(new ValuePropertyBean("Y")));
    assertFalse(new ValuePropertyBean(new String("X")).equals(new ValuePropertyBean(new String("Y"))));
    assertFalse(new ValuePropertyBean(new String("Y")).equals(new ValuePropertyBean(new String("X"))));
  }
  public void testValuePropertyBeanNonEquivalentOperandCompareTo() {
    assertTrue(((java.lang.Comparable)(new ValuePropertyBean())).compareTo(new ValuePropertyBean("X")) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new ValuePropertyBean("X"))).compareTo(new ValuePropertyBean()));
    assertTrue(((java.lang.Comparable)(new ValuePropertyBean("A"))).compareTo(new ValuePropertyBean("B")) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new ValuePropertyBean("B"))).compareTo(new ValuePropertyBean("A")));
    assertTrue(((java.lang.Comparable)(new ValuePropertyBean(new String("A")))).compareTo(new ValuePropertyBean(new String("B"))) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new ValuePropertyBean(new String("B")))).compareTo(new ValuePropertyBean(new String("A"))));
  }
  
  public void testValuePropertyBeanSerialization() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      ValuePropertyBean testBean = new ValuePropertyBean("X");
      oos.writeObject(testBean);
      oos.flush();
      oos.close();
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bais);
      ValuePropertyBean serializedTestBean = (ValuePropertyBean)(ois.readObject());
      ois.close();
      assertNotSame(testBean, serializedTestBean);
      assertEquals(testBean, serializedTestBean);
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testIdentityPropertyBeanNullOperandEquals() {
    assertFalse(new IdentityPropertyBean().equals(null));
  }
  public void testIdentityPropertyBeanNullOperandCompareTo() {
    try {
      ((java.lang.Comparable)(new IdentityPropertyBean())).compareTo(null);
      fail();
    } catch (NullPointerException ex) {
    }
  }
  public void testIdentityPropertyBeanTypeMismatchOperandEquals() {
    assertFalse(new IdentityPropertyBean().equals("XXX"));
  }
  public void testIdentityPropertyBeanTypeMismatchOperandCompareTo() {
    try {
      ((java.lang.Comparable)(new IdentityPropertyBean())).compareTo("XXX");
      fail();
    } catch (ClassCastException ex) {
    }
  }

  public void testIdentityPropertyBeanSameOperandEquals() {
    IdentityPropertyBean testBean = new IdentityPropertyBean();
    assertTrue(testBean.equals(testBean));
  }
  public void testIdentityPropertyBeanSameOperandCompareTo() {
    IdentityPropertyBean testBean = new IdentityPropertyBean();
    assertEquals(0, ((java.lang.Comparable)(testBean)).compareTo(testBean));
  }
  public void testIdentityPropertyBeanEquivalentOperandEquals() {
    assertTrue(new IdentityPropertyBean().equals(new IdentityPropertyBean()));
    assertTrue(new IdentityPropertyBean("X").equals(new IdentityPropertyBean("X")));
  }
  public void testIdentityPropertyBeanEquivalentOperandCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new IdentityPropertyBean())).compareTo(new IdentityPropertyBean()));
    assertEquals(0, ((java.lang.Comparable)(new IdentityPropertyBean("X"))).compareTo(new IdentityPropertyBean("X")));
  }
  
  public void testIdentityPropertyBeanEquivalentOperandHashCode() {
    assertEquals(new IdentityPropertyBean().hashCode(), new IdentityPropertyBean().hashCode());
    assertEquals(new IdentityPropertyBean("X").hashCode(), new IdentityPropertyBean("X").hashCode());
  }

  public void testIdentityPropertyBeanNonEquivalentOperandEquals() {
    assertFalse(new IdentityPropertyBean().equals(new IdentityPropertyBean("X")));
    assertFalse(new IdentityPropertyBean("X").equals(new IdentityPropertyBean("Y")));
    assertFalse(new IdentityPropertyBean("X").equals(new IdentityPropertyBean(new String("X"))));
    assertFalse(new IdentityPropertyBean(new String("X")).equals(new IdentityPropertyBean("X")));
    assertFalse(new IdentityPropertyBean("Y").equals(new IdentityPropertyBean("X")));
    assertFalse(new IdentityPropertyBean("X").equals(new IdentityPropertyBean("Y")));
    assertFalse(new IdentityPropertyBean(new String("X")).equals(new IdentityPropertyBean(new String("Y"))));
    assertFalse(new IdentityPropertyBean(new String("Y")).equals(new IdentityPropertyBean(new String("X"))));
  }
  public void testIdentityPropertyBeanNonEquivalentOperandCompareTo() {
    String newX = new String("X");
    if (System.identityHashCode(newX) < System.identityHashCode("X")) {
      assertTrue(((java.lang.Comparable)(new IdentityPropertyBean(newX))).compareTo(new IdentityPropertyBean("X")) < 0);
      assertTrue(0 < ((java.lang.Comparable)(new IdentityPropertyBean("X"))).compareTo(new IdentityPropertyBean(newX)));
    } else {
      assertTrue(0 < ((java.lang.Comparable)(new IdentityPropertyBean(newX))).compareTo(new IdentityPropertyBean("X")));
      assertTrue(((java.lang.Comparable)(new IdentityPropertyBean("X"))).compareTo(new IdentityPropertyBean(newX)) < 0);
    }
  }

  public void testDescendingOrderValuePropertyBeanNullOperandEquals() {
    assertFalse(new DescendingOrderValuePropertyBean().equals(null));
  }
  public void testDescendingOrderValuePropertyBeanNullOperandCompareTo() {
    try {
      ((java.lang.Comparable)(new DescendingOrderValuePropertyBean())).compareTo(null);
      fail();
    } catch (NullPointerException ex) {
    }
  }
  
  public void testDescendingOrderValuePropertyBeanTypeMismatchOperandEquals() {
    assertFalse(new DescendingOrderValuePropertyBean().equals("XXX"));
  }
  public void testDescendingOrderValuePropertyBeanTypeMismatchOperandCompareTo() {
    try {
      ((java.lang.Comparable)(new DescendingOrderValuePropertyBean())).compareTo("XXX");
      fail();
    } catch (ClassCastException ex) {
    }
  }

  public void testDescendingOrderValuePropertyBeanSameOperandEquals() {
    DescendingOrderValuePropertyBean testBean = new DescendingOrderValuePropertyBean();
    assertTrue(testBean.equals(testBean));
  }
  public void testDescendingOrderValuePropertyBeanSameOperandCompareTo() {
    DescendingOrderValuePropertyBean testBean = new DescendingOrderValuePropertyBean();
    assertEquals(0, ((java.lang.Comparable)(testBean)).compareTo(testBean));
  }
  public void testDescendingOrderValuePropertyBeanEquivalentOperandEquals() {
    assertTrue(new DescendingOrderValuePropertyBean().equals(new DescendingOrderValuePropertyBean()));
    assertTrue(new DescendingOrderValuePropertyBean("X").equals(new DescendingOrderValuePropertyBean("X")));
    assertTrue(new DescendingOrderValuePropertyBean(new String("X")).equals(new DescendingOrderValuePropertyBean(new String("X"))));
  }
  public void testDescendingOrderValuePropertyBeanEquivalentOperandCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new DescendingOrderValuePropertyBean())).compareTo(new DescendingOrderValuePropertyBean()));
    assertEquals(0, ((java.lang.Comparable)(new DescendingOrderValuePropertyBean("X"))).compareTo(new DescendingOrderValuePropertyBean("X")));
    assertEquals(0, ((java.lang.Comparable)(new DescendingOrderValuePropertyBean(new String("X")))).compareTo(new DescendingOrderValuePropertyBean(new String("X"))));
  }
  
  public void testDescendingOrdertValuePropertyBeanEquivalentOperandHashCode() {
    assertEquals(new DescendingOrderValuePropertyBean().hashCode(), new DescendingOrderValuePropertyBean().hashCode());
    assertEquals(new DescendingOrderValuePropertyBean("X").hashCode(), new DescendingOrderValuePropertyBean("X").hashCode());
    assertEquals(new DescendingOrderValuePropertyBean(new String("X")).hashCode(), new DescendingOrderValuePropertyBean(new String("X")).hashCode());
  }

  public void testDescendingOrderValuePropertyBeanNonEquivalentOperandEquals() {
    assertFalse(new DescendingOrderValuePropertyBean().equals(new DescendingOrderValuePropertyBean("X")));
    assertFalse(new DescendingOrderValuePropertyBean("X").equals(new DescendingOrderValuePropertyBean("Y")));
    assertFalse(new DescendingOrderValuePropertyBean("Y").equals(new DescendingOrderValuePropertyBean("X")));
    assertFalse(new DescendingOrderValuePropertyBean("X").equals(new DescendingOrderValuePropertyBean("Y")));
    assertFalse(new DescendingOrderValuePropertyBean(new String("X")).equals(new DescendingOrderValuePropertyBean(new String("Y"))));
    assertFalse(new DescendingOrderValuePropertyBean(new String("Y")).equals(new DescendingOrderValuePropertyBean(new String("X"))));
  }
  public void testDescendingOrderValuePropertyBeanNonEquivalentOperandCompareTo() {
    assertTrue(0 < ((java.lang.Comparable)(new DescendingOrderValuePropertyBean())).compareTo(new DescendingOrderValuePropertyBean("X")));
    assertTrue(((java.lang.Comparable)(new DescendingOrderValuePropertyBean("X"))).compareTo(new DescendingOrderValuePropertyBean()) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingOrderValuePropertyBean("A"))).compareTo(new DescendingOrderValuePropertyBean("B")));
    assertTrue(((java.lang.Comparable)(new DescendingOrderValuePropertyBean("B"))).compareTo(new DescendingOrderValuePropertyBean("A")) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingOrderValuePropertyBean(new String("A")))).compareTo(new DescendingOrderValuePropertyBean(new String("B"))));
    assertTrue(((java.lang.Comparable)(new DescendingOrderValuePropertyBean(new String("B")))).compareTo(new DescendingOrderValuePropertyBean(new String("A"))) < 0);
  }

  public void testDescendingOrderIdentityPropertyBeanNullOperandEquals() {
    assertFalse(new DescendingOrderIdentityPropertyBean().equals(null));
  }
  public void testDescendingOrderIdentityPropertyBeanNullOperandCompareTo() {
    try {
      ((java.lang.Comparable)(new DescendingOrderIdentityPropertyBean())).compareTo(null);
      fail();
    } catch (NullPointerException ex) {
    }
  }
  public void testDescendingOrderIdentityPropertyBeanTypeMismatchOperandEquals() {
    assertFalse(new DescendingOrderIdentityPropertyBean().equals("XXX"));
  }
  public void testDescendingOrderIdentityPropertyBeanTypeMismatchOperandCompareTo() {
    try {
      ((java.lang.Comparable)(new DescendingOrderIdentityPropertyBean())).compareTo("XXX");
      fail();
    } catch (ClassCastException ex) {
    }
  }

  public void testDescendingOrderIdentityPropertyBeanSameOperandEquals() {
    DescendingOrderIdentityPropertyBean testBean = new DescendingOrderIdentityPropertyBean();
    assertTrue(testBean.equals(testBean));
  }
  public void testDescendingOrderIdentityPropertyBeanSameOperandCompareTo() {
    DescendingOrderIdentityPropertyBean testBean = new DescendingOrderIdentityPropertyBean();
    assertEquals(0, ((java.lang.Comparable)(testBean)).compareTo(testBean));
  }
  public void testDescendingOrderIdentityPropertyBeanEquivalentOperandEquals() {
    assertTrue(new DescendingOrderIdentityPropertyBean().equals(new DescendingOrderIdentityPropertyBean()));
    assertTrue(new DescendingOrderIdentityPropertyBean("X").equals(new DescendingOrderIdentityPropertyBean("X")));
  }
  public void testDescendingOrderIdentityPropertyBeanEquivalentOperandCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new DescendingOrderIdentityPropertyBean())).compareTo(new DescendingOrderIdentityPropertyBean()));
    assertEquals(0, ((java.lang.Comparable)(new DescendingOrderIdentityPropertyBean("X"))).compareTo(new DescendingOrderIdentityPropertyBean("X")));
  }
  
  public void testDescendingOrderIdentityPropertyBeanEquivalentOperandHashCode() {
    assertEquals(new DescendingOrderIdentityPropertyBean().hashCode(), new DescendingOrderIdentityPropertyBean().hashCode());
    assertEquals(new DescendingOrderIdentityPropertyBean("X").hashCode(), new DescendingOrderIdentityPropertyBean("X").hashCode());
  }

  public void testDescendingOrderIdentityPropertyBeanNonEquivalentOperandEquals() {
    assertFalse(new DescendingOrderIdentityPropertyBean().equals(new DescendingOrderIdentityPropertyBean("X")));
    assertFalse(new DescendingOrderIdentityPropertyBean("X").equals(new DescendingOrderIdentityPropertyBean("Y")));
    assertFalse(new DescendingOrderIdentityPropertyBean("X").equals(new DescendingOrderIdentityPropertyBean(new String("X"))));
    assertFalse(new DescendingOrderIdentityPropertyBean(new String("X")).equals(new DescendingOrderIdentityPropertyBean("X")));
    assertFalse(new DescendingOrderIdentityPropertyBean("Y").equals(new DescendingOrderIdentityPropertyBean("X")));
    assertFalse(new DescendingOrderIdentityPropertyBean("X").equals(new DescendingOrderIdentityPropertyBean("Y")));
    assertFalse(new DescendingOrderIdentityPropertyBean(new String("X")).equals(new DescendingOrderIdentityPropertyBean(new String("Y"))));
    assertFalse(new DescendingOrderIdentityPropertyBean(new String("Y")).equals(new DescendingOrderIdentityPropertyBean(new String("X"))));
  }
  public void testDescendingOrderIdentityPropertyBeanNonEquivalentOperandCompareTo() {
    String newX = new String("X");
    if (System.identityHashCode(newX) < System.identityHashCode("X")) {
      assertTrue(0 < ((java.lang.Comparable)(new DescendingOrderIdentityPropertyBean(newX))).compareTo(new DescendingOrderIdentityPropertyBean("X")));
      assertTrue(((java.lang.Comparable)(new DescendingOrderIdentityPropertyBean("X"))).compareTo(new DescendingOrderIdentityPropertyBean(newX)) < 0);
    } else {
      assertTrue(((java.lang.Comparable)(new DescendingOrderIdentityPropertyBean(newX))).compareTo(new DescendingOrderIdentityPropertyBean("X")) < 0);
      assertTrue(0 < ((java.lang.Comparable)(new DescendingOrderIdentityPropertyBean("X"))).compareTo(new DescendingOrderIdentityPropertyBean(newX)));
    }
  }

  public void testDefaultComparisionFactory() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.comparation.aop.ComparationAspect");
    klass.getDeclaredMethod("setComparationStrategyFactory", ComparationStrategyFactory.class).invoke(Aspects.aspectOf(klass), new Object [] { null });
    
    ValuePropertyBean testBean = new ValuePropertyBean();
    assertTrue(testBean.equals(testBean));
  }
  
  
}
