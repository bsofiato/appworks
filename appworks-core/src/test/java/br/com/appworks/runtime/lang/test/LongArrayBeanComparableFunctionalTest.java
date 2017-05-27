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

public class LongArrayBeanComparableFunctionalTest extends TestCase {
  @br.com.appworks.runtime.lang.Comparable
  private static class ValueArrayTestBean {
    @br.com.appworks.runtime.lang.Comparable
    long [] array;
    public ValueArrayTestBean(long [] array) {
      this.array = array;
    }
  }
  
  @br.com.appworks.runtime.lang.Comparable
  private static class DescendingValueArrayTestBean {
    @br.com.appworks.runtime.lang.Comparable(value = ComparationPolicy.VALUE, order = OrderPolicy.INVERSE)
    long [] array;
    public DescendingValueArrayTestBean(long [] array) {
      this.array = array;
    }
  }

  @br.com.appworks.runtime.lang.Comparable
  private static class IdentityArrayTestBean {
    @br.com.appworks.runtime.lang.Comparable(value = ComparationPolicy.IDENTITY, order = OrderPolicy.INVERSE)
    long [] array;
    public IdentityArrayTestBean(long [] array) {
      this.array = array;
    }
  }

  public LongArrayBeanComparableFunctionalTest(String testName) {
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
    assertEquals(new ValueArrayTestBean(new long [0]), new ValueArrayTestBean(new long [0]));
    assertEquals(new ValueArrayTestBean(new long [] { 1 }), new ValueArrayTestBean(new long [] { 1 }));
    assertEquals(new ValueArrayTestBean(new long [] { 2 }), new ValueArrayTestBean(new long [] { 2 }));
    assertEquals(new ValueArrayTestBean(new long [] { 1, 1 }), new ValueArrayTestBean(new long [] { 1, 1 }));
  }

  public void testEquivalentValueArrayTestBeanHashCode() {
    assertEquals(new ValueArrayTestBean(null).hashCode(), new ValueArrayTestBean(null).hashCode());
    assertEquals(new ValueArrayTestBean(new long [0]).hashCode(), new ValueArrayTestBean(new long [0]).hashCode());
    assertEquals(new ValueArrayTestBean(new long [] { 1 }).hashCode(), new ValueArrayTestBean(new long [] { 1 }).hashCode());
    assertEquals(new ValueArrayTestBean(new long [] { 2 }).hashCode(), new ValueArrayTestBean(new long [] { 2 }).hashCode());
    assertEquals(new ValueArrayTestBean(new long [] { 1, 1 }).hashCode(), new ValueArrayTestBean(new long [] { 1, 1 }).hashCode());
  }
  
  public void testEquivalentValueArrayTestBeanCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(null)));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new long [0]))).compareTo(new ValueArrayTestBean(new long [0])));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 1 }))).compareTo(new ValueArrayTestBean(new long [] { 1 })));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 2 }))).compareTo(new ValueArrayTestBean(new long [] { 2 })));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 1, 1 }))).compareTo(new ValueArrayTestBean(new long [] { 1, 1 })));
  }

  public void testNonEquivalentValueArrayTestBeanEquals() {
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new long [0])));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new long [] { 1 })));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new long [] { 2 })));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new long [] { 1, 1 })));

    assertFalse(new ValueArrayTestBean(new long [0]).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new long [0]).equals(new ValueArrayTestBean(new long [] { 1 })));
    assertFalse(new ValueArrayTestBean(new long [0]).equals(new ValueArrayTestBean(new long [] { 2 })));
    assertFalse(new ValueArrayTestBean(new long [0]).equals(new ValueArrayTestBean(new long [] { 1, 1 })));

    assertFalse(new ValueArrayTestBean(new long [] { 1 }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new long [] { 1 }).equals(new ValueArrayTestBean(new long [0])));
    assertFalse(new ValueArrayTestBean(new long [] { 1 }).equals(new ValueArrayTestBean(new long [] { 2 })));
    assertFalse(new ValueArrayTestBean(new long [] { 1 }).equals(new ValueArrayTestBean(new long [] { 1, 1 })));

    assertFalse(new ValueArrayTestBean(new long [] { 2 }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new long [] { 2 }).equals(new ValueArrayTestBean(new long [0])));
    assertFalse(new ValueArrayTestBean(new long [] { 2 }).equals(new ValueArrayTestBean(new long [] { 1 })));
    assertFalse(new ValueArrayTestBean(new long [] { 2 }).equals(new ValueArrayTestBean(new long [] { 1, 1 })));

    assertFalse(new ValueArrayTestBean(new long [] { 1, 1 }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new long [] { 1, 1 }).equals(new ValueArrayTestBean(new long [0])));
    assertFalse(new ValueArrayTestBean(new long [] { 1, 1 }).equals(new ValueArrayTestBean(new long [] { 1 })));
    assertFalse(new ValueArrayTestBean(new long [] { 1, 1 }).equals(new ValueArrayTestBean(new long [] { 2 })));

  }
  
  public void testNonEquivalentValueArrayTestBeanCompareTo() {
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new long [0])) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new long [] { 1 })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new long [] { 2 })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new long [] { 1, 1 })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new long [0]))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new long [0]))).compareTo(new ValueArrayTestBean(new long [] { 1 })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new long [0]))).compareTo(new ValueArrayTestBean(new long [] { 2 })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new long [0]))).compareTo(new ValueArrayTestBean(new long [] { 1, 1 })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 1 }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 1 }))).compareTo(new ValueArrayTestBean(new long [0])));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 1 }))).compareTo(new ValueArrayTestBean(new long [] { 2 })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 1 }))).compareTo(new ValueArrayTestBean(new long [] { 1, 1 })) < 0);
    
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 2 }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 2 }))).compareTo(new ValueArrayTestBean(new long [0])));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 2 }))).compareTo(new ValueArrayTestBean(new long [] { 1 })));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 2 }))).compareTo(new ValueArrayTestBean(new long [] { 1, 1 })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 1, 1 }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 1, 1 }))).compareTo(new ValueArrayTestBean(new long [0])));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 1, 1 }))).compareTo(new ValueArrayTestBean(new long [] { 1 })));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new long [] { 1, 1 }))).compareTo(new ValueArrayTestBean(new long [] { 2 })));
  }
  
   public void testEquivalentDescendingValueArrayTestBeanEquals() {
    assertEquals(new DescendingValueArrayTestBean(null), new DescendingValueArrayTestBean(null));
    assertEquals(new DescendingValueArrayTestBean(new long [0]), new DescendingValueArrayTestBean(new long [0]));
    assertEquals(new DescendingValueArrayTestBean(new long [] { 1 }), new DescendingValueArrayTestBean(new long [] { 1 }));
    assertEquals(new DescendingValueArrayTestBean(new long [] { 2 }), new DescendingValueArrayTestBean(new long [] { 2 }));
    assertEquals(new DescendingValueArrayTestBean(new long [] { 1, 1 }), new DescendingValueArrayTestBean(new long [] { 1, 1 }));
  }

  public void testDescendingValueArrayTestBeanBeanHashCode() {
    assertEquals(new DescendingValueArrayTestBean(null).hashCode(), new DescendingValueArrayTestBean(null).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new long [0]).hashCode(), new DescendingValueArrayTestBean(new long [0]).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new long [] { 1 }).hashCode(), new DescendingValueArrayTestBean(new long [] { 1 }).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new long [] { 2 }).hashCode(), new DescendingValueArrayTestBean(new long [] { 2 }).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new long [] { 1, 1 }).hashCode(), new DescendingValueArrayTestBean(new long [] { 1, 1 }).hashCode());
  }
  
  public void testEquivalentDescendingValueArrayTestBeanCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(null)));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [0]))).compareTo(new DescendingValueArrayTestBean(new long [0])));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 1 }))).compareTo(new DescendingValueArrayTestBean(new long [] { 1 })));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 2 }))).compareTo(new DescendingValueArrayTestBean(new long [] { 2 })));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 1, 1 }))).compareTo(new DescendingValueArrayTestBean(new long [] { 1, 1 })));
  }

  public void testNonEquivalentDescendingValueArrayTestBeanEquals() {
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new long [0])));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new long [] { 1 })));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new long [] { 2 })));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new long [] { 1, 1 })));

    assertFalse(new DescendingValueArrayTestBean(new long [0]).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new long [0]).equals(new DescendingValueArrayTestBean(new long [] { 1 })));
    assertFalse(new DescendingValueArrayTestBean(new long [0]).equals(new DescendingValueArrayTestBean(new long [] { 2 })));
    assertFalse(new DescendingValueArrayTestBean(new long [0]).equals(new DescendingValueArrayTestBean(new long [] { 1, 1 })));

    assertFalse(new DescendingValueArrayTestBean(new long [] { 1 }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new long [] { 1 }).equals(new DescendingValueArrayTestBean(new long [0])));
    assertFalse(new DescendingValueArrayTestBean(new long [] { 1 }).equals(new DescendingValueArrayTestBean(new long [] { 2 })));
    assertFalse(new DescendingValueArrayTestBean(new long [] { 1 }).equals(new DescendingValueArrayTestBean(new long [] { 1, 1 })));

    assertFalse(new DescendingValueArrayTestBean(new long [] { 2 }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new long [] { 2 }).equals(new DescendingValueArrayTestBean(new long [] { 1 })));
    assertFalse(new DescendingValueArrayTestBean(new long [] { 2 }).equals(new DescendingValueArrayTestBean(new long [] { 1, 1 })));

    assertFalse(new DescendingValueArrayTestBean(new long [] { 1, 1 }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new long [] { 1, 1 }).equals(new DescendingValueArrayTestBean(new long [0])));
    assertFalse(new DescendingValueArrayTestBean(new long [] { 1, 1 }).equals(new DescendingValueArrayTestBean(new long [] { 1 })));
    assertFalse(new DescendingValueArrayTestBean(new long [] { 1, 1 }).equals(new DescendingValueArrayTestBean(new long [] { 2 })));
  }
  
  public void testNonEquivalentDescendingValueArrayTestBeanCompareTo() {
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new long [0])));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new long [] { 1 })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new long [] { 2 })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new long [] { 1, 1 })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [0]))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [0]))).compareTo(new DescendingValueArrayTestBean(new long [] { 1 })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [0]))).compareTo(new DescendingValueArrayTestBean(new long [] { 2 })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [0]))).compareTo(new DescendingValueArrayTestBean(new long [] { 1, 1 })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 1 }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 1 }))).compareTo(new DescendingValueArrayTestBean(new long [0])) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 1 }))).compareTo(new DescendingValueArrayTestBean(new long [] { 2 })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 1 }))).compareTo(new DescendingValueArrayTestBean(new long [] { 1, 1 })));
    
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 2 }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 2 }))).compareTo(new DescendingValueArrayTestBean(new long [0])) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 2 }))).compareTo(new DescendingValueArrayTestBean(new long [] { 1 })) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 2 }))).compareTo(new DescendingValueArrayTestBean(new long [] { 1, 1 })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 1, 1 }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 1, 1 }))).compareTo(new DescendingValueArrayTestBean(new long [0])) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 1, 1 }))).compareTo(new DescendingValueArrayTestBean(new long [] { 1 })) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new long [] { 1, 1 }))).compareTo(new DescendingValueArrayTestBean(new long [] { 2 })) < 0);
  }

  public void testEquivalentIdentityArrayTestBeanEquals() {
    long [] array1 = new long [] { 1 };
    long [] array2 = new long [] { 1, 3 };
    assertEquals(new IdentityArrayTestBean(null), new IdentityArrayTestBean(null));
    assertEquals(new IdentityArrayTestBean(array1), new IdentityArrayTestBean(array1));
    assertEquals(new IdentityArrayTestBean(array2), new IdentityArrayTestBean(array2));
  }
  
  public void testEquivalentIdentityArrayTestBeanHashCode() {
    long [] array1 = new long [] { 1 };
    long [] array2 = new long [] { 1, 3 };
    assertEquals(new IdentityArrayTestBean(null).hashCode(), new IdentityArrayTestBean(null).hashCode());
    assertEquals(new IdentityArrayTestBean(array1).hashCode(), new IdentityArrayTestBean(array1).hashCode());
    assertEquals(new IdentityArrayTestBean(array2).hashCode(), new IdentityArrayTestBean(array2).hashCode());
  }

  public void testEquivalentIdentityArrayTestBeanCompareTo() {
    long [][] arrays = new long [][] { null,
                                     new long [] { 1 },
                                     new long [] { 1, 3 }};
    for (long [] array : arrays) {
      assertEquals(0, ((java.lang.Comparable)(new IdentityArrayTestBean(array))).compareTo(new IdentityArrayTestBean(array)));
    }
  }

  public void testNonEquivalentIdentityArrayTestBeanEquals() {
    long [] array1 = new long [] { 1 };
    long [] array2 = new long [] { 1, 3 };
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
    long [][] arrays = new long [][] { null,
                                     new long [] { 1 },
                                     new long [] { 1, 3 }};
    for (long [] array1 : arrays) {
      for (long [] array2 : arrays) {
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