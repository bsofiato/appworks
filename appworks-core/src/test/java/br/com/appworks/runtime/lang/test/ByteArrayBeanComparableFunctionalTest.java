package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.ComparationPolicy;
import br.com.appworks.runtime.lang.OrderPolicy;
import br.com.appworks.runtime.lang.support.comparation.CachingComparationStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.comparation.DefaultComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import junit.framework.TestCase;
import org.aspectj.lang.Aspects;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class ByteArrayBeanComparableFunctionalTest extends TestCase {
  @br.com.appworks.runtime.lang.Comparable
  private static class ValueArrayTestBean {
    @br.com.appworks.runtime.lang.Comparable
    byte [] array;
    public ValueArrayTestBean(byte [] array) {
      this.array = array;
    }
  }
  
  @br.com.appworks.runtime.lang.Comparable
  private static class DescendingValueArrayTestBean {
    @br.com.appworks.runtime.lang.Comparable(value = ComparationPolicy.VALUE, order = OrderPolicy.INVERSE)
    byte [] array;
    public DescendingValueArrayTestBean(byte [] array) {
      this.array = array;
    }
  }

  @br.com.appworks.runtime.lang.Comparable
  private static class IdentityArrayTestBean {
    @br.com.appworks.runtime.lang.Comparable(value = ComparationPolicy.IDENTITY, order = OrderPolicy.INVERSE)
    byte [] array;
    public IdentityArrayTestBean(byte [] array) {
      this.array = array;
    }
  }

  public ByteArrayBeanComparableFunctionalTest(String testName) {
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
  
  public void testEquivalentValueArrayTestBeanEquals() {
    assertEquals(new ValueArrayTestBean(null), new ValueArrayTestBean(null));
    assertEquals(new ValueArrayTestBean(new byte [0]), new ValueArrayTestBean(new byte [0]));
    assertEquals(new ValueArrayTestBean(new byte [] { 1 }), new ValueArrayTestBean(new byte [] { 1 }));
    assertEquals(new ValueArrayTestBean(new byte [] { 2 }), new ValueArrayTestBean(new byte [] { 2 }));
    assertEquals(new ValueArrayTestBean(new byte [] { 1, 1 }), new ValueArrayTestBean(new byte [] { 1, 1 }));
  }

  public void testEquivalentValueArrayTestBeanHashCode() {
    assertEquals(new ValueArrayTestBean(null).hashCode(), new ValueArrayTestBean(null).hashCode());
    assertEquals(new ValueArrayTestBean(new byte [0]).hashCode(), new ValueArrayTestBean(new byte [0]).hashCode());
    assertEquals(new ValueArrayTestBean(new byte [] { 1 }).hashCode(), new ValueArrayTestBean(new byte [] { 1 }).hashCode());
    assertEquals(new ValueArrayTestBean(new byte [] { 2 }).hashCode(), new ValueArrayTestBean(new byte [] { 2 }).hashCode());
    assertEquals(new ValueArrayTestBean(new byte [] { 1, 1 }).hashCode(), new ValueArrayTestBean(new byte [] { 1, 1 }).hashCode());
  }
  
  public void testEquivalentValueArrayTestBeanCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(null)));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new byte [0]))).compareTo(new ValueArrayTestBean(new byte [0])));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 1 }))).compareTo(new ValueArrayTestBean(new byte [] { 1 })));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 2 }))).compareTo(new ValueArrayTestBean(new byte [] { 2 })));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 1, 1 }))).compareTo(new ValueArrayTestBean(new byte [] { 1, 1 })));
  }

  public void testNonEquivalentValueArrayTestBeanEquals() {
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new byte [0])));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new byte [] { 1 })));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new byte [] { 2 })));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new byte [] { 1, 1 })));

    assertFalse(new ValueArrayTestBean(new byte [0]).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new byte [0]).equals(new ValueArrayTestBean(new byte [] { 1 })));
    assertFalse(new ValueArrayTestBean(new byte [0]).equals(new ValueArrayTestBean(new byte [] { 2 })));
    assertFalse(new ValueArrayTestBean(new byte [0]).equals(new ValueArrayTestBean(new byte [] { 1, 1 })));

    assertFalse(new ValueArrayTestBean(new byte [] { 1 }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new byte [] { 1 }).equals(new ValueArrayTestBean(new byte [0])));
    assertFalse(new ValueArrayTestBean(new byte [] { 1 }).equals(new ValueArrayTestBean(new byte [] { 2 })));
    assertFalse(new ValueArrayTestBean(new byte [] { 1 }).equals(new ValueArrayTestBean(new byte [] { 1, 1 })));

    assertFalse(new ValueArrayTestBean(new byte [] { 2 }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new byte [] { 2 }).equals(new ValueArrayTestBean(new byte [0])));
    assertFalse(new ValueArrayTestBean(new byte [] { 2 }).equals(new ValueArrayTestBean(new byte [] { 1 })));
    assertFalse(new ValueArrayTestBean(new byte [] { 2 }).equals(new ValueArrayTestBean(new byte [] { 1, 1 })));

    assertFalse(new ValueArrayTestBean(new byte [] { 1, 1 }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new byte [] { 1, 1 }).equals(new ValueArrayTestBean(new byte [0])));
    assertFalse(new ValueArrayTestBean(new byte [] { 1, 1 }).equals(new ValueArrayTestBean(new byte [] { 1 })));
    assertFalse(new ValueArrayTestBean(new byte [] { 1, 1 }).equals(new ValueArrayTestBean(new byte [] { 2 })));

  }
  
  public void testNonEquivalentValueArrayTestBeanCompareTo() {
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new byte [0])) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new byte [] { 1 })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new byte [] { 2 })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new byte [] { 1, 1 })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new byte [0]))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new byte [0]))).compareTo(new ValueArrayTestBean(new byte [] { 1 })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new byte [0]))).compareTo(new ValueArrayTestBean(new byte [] { 2 })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new byte [0]))).compareTo(new ValueArrayTestBean(new byte [] { 1, 1 })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 1 }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 1 }))).compareTo(new ValueArrayTestBean(new byte [0])));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 1 }))).compareTo(new ValueArrayTestBean(new byte [] { 2 })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 1 }))).compareTo(new ValueArrayTestBean(new byte [] { 1, 1 })) < 0);
    
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 2 }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 2 }))).compareTo(new ValueArrayTestBean(new byte [0])));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 2 }))).compareTo(new ValueArrayTestBean(new byte [] { 1 })));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 2 }))).compareTo(new ValueArrayTestBean(new byte [] { 1, 1 })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 1, 1 }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 1, 1 }))).compareTo(new ValueArrayTestBean(new byte [0])));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 1, 1 }))).compareTo(new ValueArrayTestBean(new byte [] { 1 })));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new byte [] { 1, 1 }))).compareTo(new ValueArrayTestBean(new byte [] { 2 })));
  }
  
   public void testEquivalentDescendingValueArrayTestBeanEquals() {
    assertEquals(new DescendingValueArrayTestBean(null), new DescendingValueArrayTestBean(null));
    assertEquals(new DescendingValueArrayTestBean(new byte [0]), new DescendingValueArrayTestBean(new byte [0]));
    assertEquals(new DescendingValueArrayTestBean(new byte [] { 1 }), new DescendingValueArrayTestBean(new byte [] { 1 }));
    assertEquals(new DescendingValueArrayTestBean(new byte [] { 2 }), new DescendingValueArrayTestBean(new byte [] { 2 }));
    assertEquals(new DescendingValueArrayTestBean(new byte [] { 1, 1 }), new DescendingValueArrayTestBean(new byte [] { 1, 1 }));
  }

  public void testDescendingValueArrayTestBeanBeanHashCode() {
    assertEquals(new DescendingValueArrayTestBean(null).hashCode(), new DescendingValueArrayTestBean(null).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new byte [0]).hashCode(), new DescendingValueArrayTestBean(new byte [0]).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new byte [] { 1 }).hashCode(), new DescendingValueArrayTestBean(new byte [] { 1 }).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new byte [] { 2 }).hashCode(), new DescendingValueArrayTestBean(new byte [] { 2 }).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new byte [] { 1, 1 }).hashCode(), new DescendingValueArrayTestBean(new byte [] { 1, 1 }).hashCode());
  }
  
  public void testEquivalentDescendingValueArrayTestBeanCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(null)));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [0]))).compareTo(new DescendingValueArrayTestBean(new byte [0])));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 1 }))).compareTo(new DescendingValueArrayTestBean(new byte [] { 1 })));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 2 }))).compareTo(new DescendingValueArrayTestBean(new byte [] { 2 })));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 1, 1 }))).compareTo(new DescendingValueArrayTestBean(new byte [] { 1, 1 })));
  }

  public void testNonEquivalentDescendingValueArrayTestBeanEquals() {
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new byte [0])));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new byte [] { 1 })));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new byte [] { 2 })));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new byte [] { 1, 1 })));

    assertFalse(new DescendingValueArrayTestBean(new byte [0]).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new byte [0]).equals(new DescendingValueArrayTestBean(new byte [] { 1 })));
    assertFalse(new DescendingValueArrayTestBean(new byte [0]).equals(new DescendingValueArrayTestBean(new byte [] { 2 })));
    assertFalse(new DescendingValueArrayTestBean(new byte [0]).equals(new DescendingValueArrayTestBean(new byte [] { 1, 1 })));

    assertFalse(new DescendingValueArrayTestBean(new byte [] { 1 }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new byte [] { 1 }).equals(new DescendingValueArrayTestBean(new byte [0])));
    assertFalse(new DescendingValueArrayTestBean(new byte [] { 1 }).equals(new DescendingValueArrayTestBean(new byte [] { 2 })));
    assertFalse(new DescendingValueArrayTestBean(new byte [] { 1 }).equals(new DescendingValueArrayTestBean(new byte [] { 1, 1 })));

    assertFalse(new DescendingValueArrayTestBean(new byte [] { 2 }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new byte [] { 2 }).equals(new DescendingValueArrayTestBean(new byte [] { 1 })));
    assertFalse(new DescendingValueArrayTestBean(new byte [] { 2 }).equals(new DescendingValueArrayTestBean(new byte [] { 1, 1 })));

    assertFalse(new DescendingValueArrayTestBean(new byte [] { 1, 1 }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new byte [] { 1, 1 }).equals(new DescendingValueArrayTestBean(new byte [0])));
    assertFalse(new DescendingValueArrayTestBean(new byte [] { 1, 1 }).equals(new DescendingValueArrayTestBean(new byte [] { 1 })));
    assertFalse(new DescendingValueArrayTestBean(new byte [] { 1, 1 }).equals(new DescendingValueArrayTestBean(new byte [] { 2 })));
  }
  
  public void testNonEquivalentDescendingValueArrayTestBeanCompareTo() {
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new byte [0])));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new byte [] { 1 })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new byte [] { 2 })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new byte [] { 1, 1 })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [0]))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [0]))).compareTo(new DescendingValueArrayTestBean(new byte [] { 1 })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [0]))).compareTo(new DescendingValueArrayTestBean(new byte [] { 2 })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [0]))).compareTo(new DescendingValueArrayTestBean(new byte [] { 1, 1 })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 1 }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 1 }))).compareTo(new DescendingValueArrayTestBean(new byte [0])) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 1 }))).compareTo(new DescendingValueArrayTestBean(new byte [] { 2 })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 1 }))).compareTo(new DescendingValueArrayTestBean(new byte [] { 1, 1 })));
    
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 2 }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 2 }))).compareTo(new DescendingValueArrayTestBean(new byte [0])) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 2 }))).compareTo(new DescendingValueArrayTestBean(new byte [] { 1 })) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 2 }))).compareTo(new DescendingValueArrayTestBean(new byte [] { 1, 1 })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 1, 1 }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 1, 1 }))).compareTo(new DescendingValueArrayTestBean(new byte [0])) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 1, 1 }))).compareTo(new DescendingValueArrayTestBean(new byte [] { 1 })) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new byte [] { 1, 1 }))).compareTo(new DescendingValueArrayTestBean(new byte [] { 2 })) < 0);
  }

  public void testEquivalentIdentityArrayTestBeanEquals() {
    byte [] array1 = new byte [] { 1 };
    byte [] array2 = new byte [] { 1, 3 };
    assertEquals(new IdentityArrayTestBean(null), new IdentityArrayTestBean(null));
    assertEquals(new IdentityArrayTestBean(array1), new IdentityArrayTestBean(array1));
    assertEquals(new IdentityArrayTestBean(array2), new IdentityArrayTestBean(array2));
  }
  
  public void testEquivalentIdentityArrayTestBeanHashCode() {
    byte [] array1 = new byte [] { 1 };
    byte [] array2 = new byte [] { 1, 3 };
    assertEquals(new IdentityArrayTestBean(null).hashCode(), new IdentityArrayTestBean(null).hashCode());
    assertEquals(new IdentityArrayTestBean(array1).hashCode(), new IdentityArrayTestBean(array1).hashCode());
    assertEquals(new IdentityArrayTestBean(array2).hashCode(), new IdentityArrayTestBean(array2).hashCode());
  }

  public void testEquivalentIdentityArrayTestBeanCompareTo() {
    byte [][] arrays = new byte [][] { null,
                                     new byte [] { 1 },
                                     new byte [] { 1, 3 }};
    for (byte [] array : arrays) {
      assertEquals(0, ((java.lang.Comparable)(new IdentityArrayTestBean(array))).compareTo(new IdentityArrayTestBean(array)));
    }
  }

  public void testNonEquivalentIdentityArrayTestBeanEquals() {
    byte [] array1 = new byte [] { 1 };
    byte [] array2 = new byte [] { 1, 3 };
    assertFalse(new IdentityArrayTestBean(null).equals(new IdentityArrayTestBean(array1)));
    assertFalse(new IdentityArrayTestBean(null).equals(new IdentityArrayTestBean(array2)));
    assertFalse(new IdentityArrayTestBean(array1).equals(new IdentityArrayTestBean(null)));
    assertFalse(new IdentityArrayTestBean(array1).equals(new IdentityArrayTestBean(array1.clone())));
    assertFalse(new IdentityArrayTestBean(array1).equals(new IdentityArrayTestBean(array2)));
    assertFalse(new IdentityArrayTestBean(array2).equals(new IdentityArrayTestBean(null)));
    assertFalse(new IdentityArrayTestBean(array2).equals(new IdentityArrayTestBean(array1)));
    assertFalse(new IdentityArrayTestBean(array2).equals(new IdentityArrayTestBean(array2.clone())));
  }

  public void testNonEquivalentIdentityArrayTestBeanCompareTo() {
    byte [][] arrays = new byte [][] { null,
                                     new byte [] { 1 },
                                     new byte [] { 1, 3 }};
    for (byte [] array1 : arrays) {
      for (byte [] array2 : arrays) {
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