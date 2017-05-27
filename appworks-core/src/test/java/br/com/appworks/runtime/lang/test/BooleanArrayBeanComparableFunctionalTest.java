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

public class BooleanArrayBeanComparableFunctionalTest extends TestCase {
  @br.com.appworks.runtime.lang.Comparable
  private static class ValueArrayTestBean {
    @br.com.appworks.runtime.lang.Comparable
    boolean [] array;
    public ValueArrayTestBean(boolean [] array) {
      this.array = array;
    }
  }
  
  @br.com.appworks.runtime.lang.Comparable
  private static class DescendingValueArrayTestBean {
    @br.com.appworks.runtime.lang.Comparable(value = ComparationPolicy.VALUE, order = OrderPolicy.INVERSE)
    boolean [] array;
    public DescendingValueArrayTestBean(boolean [] array) {
      this.array = array;
    }
  }

  @br.com.appworks.runtime.lang.Comparable
  private static class IdentityArrayTestBean {
    @br.com.appworks.runtime.lang.Comparable(value = ComparationPolicy.IDENTITY, order = OrderPolicy.INVERSE)
    boolean [] array;
    public IdentityArrayTestBean(boolean [] array) {
      this.array = array;
    }
  }

  public BooleanArrayBeanComparableFunctionalTest(String testName) {
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
    assertEquals(new ValueArrayTestBean(new boolean [0]), new ValueArrayTestBean(new boolean [0]));
    assertEquals(new ValueArrayTestBean(new boolean [] { true }), new ValueArrayTestBean(new boolean [] { true  }));
    assertEquals(new ValueArrayTestBean(new boolean [] { false }), new ValueArrayTestBean(new boolean [] { false }));
    assertEquals(new ValueArrayTestBean(new boolean [] { true , true  }), new ValueArrayTestBean(new boolean [] { true , true  }));
  }

  public void testEquivalentValueArrayTestBeanHashCode() {
    assertEquals(new ValueArrayTestBean(null).hashCode(), new ValueArrayTestBean(null).hashCode());
    assertEquals(new ValueArrayTestBean(new boolean [0]).hashCode(), new ValueArrayTestBean(new boolean [0]).hashCode());
    assertEquals(new ValueArrayTestBean(new boolean [] { true  }).hashCode(), new ValueArrayTestBean(new boolean [] { true  }).hashCode());
    assertEquals(new ValueArrayTestBean(new boolean [] { false }).hashCode(), new ValueArrayTestBean(new boolean [] { false }).hashCode());
    assertEquals(new ValueArrayTestBean(new boolean [] { true , true  }).hashCode(), new ValueArrayTestBean(new boolean [] { true , true  }).hashCode());
  }
  
  public void testEquivalentValueArrayTestBeanCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(null)));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new boolean [0]))).compareTo(new ValueArrayTestBean(new boolean [0])));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { true  }))).compareTo(new ValueArrayTestBean(new boolean [] { true  })));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { false }))).compareTo(new ValueArrayTestBean(new boolean [] { false })));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { true , true  }))).compareTo(new ValueArrayTestBean(new boolean [] { true , true  })));
  }

  public void testNonEquivalentValueArrayTestBeanEquals() {
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new boolean [0])));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new boolean [] { true  })));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new boolean [] { false })));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new boolean [] { true , true  })));

    assertFalse(new ValueArrayTestBean(new boolean [0]).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new boolean [0]).equals(new ValueArrayTestBean(new boolean [] { true  })));
    assertFalse(new ValueArrayTestBean(new boolean [0]).equals(new ValueArrayTestBean(new boolean [] { false })));
    assertFalse(new ValueArrayTestBean(new boolean [0]).equals(new ValueArrayTestBean(new boolean [] { true , true  })));

    assertFalse(new ValueArrayTestBean(new boolean [] { true  }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new boolean [] { true  }).equals(new ValueArrayTestBean(new boolean [0])));
    assertFalse(new ValueArrayTestBean(new boolean [] { true  }).equals(new ValueArrayTestBean(new boolean [] { false })));
    assertFalse(new ValueArrayTestBean(new boolean [] { true  }).equals(new ValueArrayTestBean(new boolean [] { true , true  })));

    assertFalse(new ValueArrayTestBean(new boolean [] { false }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new boolean [] { false }).equals(new ValueArrayTestBean(new boolean [0])));
    assertFalse(new ValueArrayTestBean(new boolean [] { false }).equals(new ValueArrayTestBean(new boolean [] { true  })));
    assertFalse(new ValueArrayTestBean(new boolean [] { false }).equals(new ValueArrayTestBean(new boolean [] { true , true  })));

    assertFalse(new ValueArrayTestBean(new boolean [] { true , true  }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new boolean [] { true , true  }).equals(new ValueArrayTestBean(new boolean [0])));
    assertFalse(new ValueArrayTestBean(new boolean [] { true , true  }).equals(new ValueArrayTestBean(new boolean [] { true  })));
    assertFalse(new ValueArrayTestBean(new boolean [] { true , true  }).equals(new ValueArrayTestBean(new boolean [] { false })));

  }
  
  public void testNonEquivalentValueArrayTestBeanCompareTo() {
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new boolean [0])) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new boolean [] { true  })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new boolean [] { false })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new boolean [] { true , true  })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new boolean [0]))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new boolean [0]))).compareTo(new ValueArrayTestBean(new boolean [] { true  })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new boolean [0]))).compareTo(new ValueArrayTestBean(new boolean [] { false })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new boolean [0]))).compareTo(new ValueArrayTestBean(new boolean [] { true , true  })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { true  }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { true  }))).compareTo(new ValueArrayTestBean(new boolean [0])));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { false  }))).compareTo(new ValueArrayTestBean(new boolean [] { true})) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { true  }))).compareTo(new ValueArrayTestBean(new boolean [] { true , true  })) < 0);
    
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { false }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { false }))).compareTo(new ValueArrayTestBean(new boolean [0])));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { true }))).compareTo(new ValueArrayTestBean(new boolean [] { false })));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { false }))).compareTo(new ValueArrayTestBean(new boolean [] { true , true  })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { true , true  }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { true , true  }))).compareTo(new ValueArrayTestBean(new boolean [0])));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { true , true  }))).compareTo(new ValueArrayTestBean(new boolean [] { true  })));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new boolean [] { true , true  }))).compareTo(new ValueArrayTestBean(new boolean [] { false })));
  }
  
   public void testEquivalentDescendingValueArrayTestBeanEquals() {
    assertEquals(new DescendingValueArrayTestBean(null), new DescendingValueArrayTestBean(null));
    assertEquals(new DescendingValueArrayTestBean(new boolean [0]), new DescendingValueArrayTestBean(new boolean [0]));
    assertEquals(new DescendingValueArrayTestBean(new boolean [] { true  }), new DescendingValueArrayTestBean(new boolean [] { true  }));
    assertEquals(new DescendingValueArrayTestBean(new boolean [] { false }), new DescendingValueArrayTestBean(new boolean [] { false }));
    assertEquals(new DescendingValueArrayTestBean(new boolean [] { true , true  }), new DescendingValueArrayTestBean(new boolean [] { true , true  }));
  }

  public void testDescendingValueArrayTestBeanBeanHashCode() {
    assertEquals(new DescendingValueArrayTestBean(null).hashCode(), new DescendingValueArrayTestBean(null).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new boolean [0]).hashCode(), new DescendingValueArrayTestBean(new boolean [0]).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new boolean [] { true  }).hashCode(), new DescendingValueArrayTestBean(new boolean [] { true  }).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new boolean [] { false }).hashCode(), new DescendingValueArrayTestBean(new boolean [] { false }).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new boolean [] { true , true  }).hashCode(), new DescendingValueArrayTestBean(new boolean [] { true , true  }).hashCode());
  }
  
  public void testEquivalentDescendingValueArrayTestBeanCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(null)));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [0]))).compareTo(new DescendingValueArrayTestBean(new boolean [0])));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { true  }))).compareTo(new DescendingValueArrayTestBean(new boolean [] { true  })));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { false }))).compareTo(new DescendingValueArrayTestBean(new boolean [] { false })));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { true , true  }))).compareTo(new DescendingValueArrayTestBean(new boolean [] { true , true  })));
  }

  public void testNonEquivalentDescendingValueArrayTestBeanEquals() {
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new boolean [0])));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new boolean [] { true  })));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new boolean [] { false })));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new boolean [] { true , true  })));

    assertFalse(new DescendingValueArrayTestBean(new boolean [0]).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new boolean [0]).equals(new DescendingValueArrayTestBean(new boolean [] { true  })));
    assertFalse(new DescendingValueArrayTestBean(new boolean [0]).equals(new DescendingValueArrayTestBean(new boolean [] { false })));
    assertFalse(new DescendingValueArrayTestBean(new boolean [0]).equals(new DescendingValueArrayTestBean(new boolean [] { true , true  })));

    assertFalse(new DescendingValueArrayTestBean(new boolean [] { true  }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new boolean [] { true  }).equals(new DescendingValueArrayTestBean(new boolean [0])));
    assertFalse(new DescendingValueArrayTestBean(new boolean [] { true  }).equals(new DescendingValueArrayTestBean(new boolean [] { false })));
    assertFalse(new DescendingValueArrayTestBean(new boolean [] { true  }).equals(new DescendingValueArrayTestBean(new boolean [] { true , true  })));

    assertFalse(new DescendingValueArrayTestBean(new boolean [] { false }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new boolean [] { false }).equals(new DescendingValueArrayTestBean(new boolean [] { true  })));
    assertFalse(new DescendingValueArrayTestBean(new boolean [] { false }).equals(new DescendingValueArrayTestBean(new boolean [] { true , true  })));

    assertFalse(new DescendingValueArrayTestBean(new boolean [] { true , true  }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new boolean [] { true , true  }).equals(new DescendingValueArrayTestBean(new boolean [0])));
    assertFalse(new DescendingValueArrayTestBean(new boolean [] { true , true  }).equals(new DescendingValueArrayTestBean(new boolean [] { true  })));
    assertFalse(new DescendingValueArrayTestBean(new boolean [] { true , true  }).equals(new DescendingValueArrayTestBean(new boolean [] { false })));
  }
  
  public void testNonEquivalentDescendingValueArrayTestBeanCompareTo() {
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new boolean [0])));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new boolean [] { true  })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new boolean [] { false })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new boolean [] { true , true  })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [0]))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [0]))).compareTo(new DescendingValueArrayTestBean(new boolean [] { true  })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [0]))).compareTo(new DescendingValueArrayTestBean(new boolean [] { false })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [0]))).compareTo(new DescendingValueArrayTestBean(new boolean [] { true , true  })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { true  }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { true  }))).compareTo(new DescendingValueArrayTestBean(new boolean [0])) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { false  }))).compareTo(new DescendingValueArrayTestBean(new boolean [] { true })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { true  }))).compareTo(new DescendingValueArrayTestBean(new boolean [] { true , true  })));
    
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { false }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { false }))).compareTo(new DescendingValueArrayTestBean(new boolean [0])) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { true }))).compareTo(new DescendingValueArrayTestBean(new boolean [] { false })) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { false }))).compareTo(new DescendingValueArrayTestBean(new boolean [] { true , true  })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { true , true  }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { true , true  }))).compareTo(new DescendingValueArrayTestBean(new boolean [0])) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { true , true  }))).compareTo(new DescendingValueArrayTestBean(new boolean [] { true  })) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new boolean [] { true , true  }))).compareTo(new DescendingValueArrayTestBean(new boolean [] { false })) < 0);
  }

  public void testEquivalentIdentityArrayTestBeanEquals() {
    boolean [] array1  = new boolean [] { true };
    boolean [] array2 = new boolean [] { true, false };
    assertEquals(new IdentityArrayTestBean(null), new IdentityArrayTestBean(null));
    assertEquals(new IdentityArrayTestBean(array1), new IdentityArrayTestBean(array1));
    assertEquals(new IdentityArrayTestBean(array2), new IdentityArrayTestBean(array2));
  }
  
  public void testEquivalentIdentityArrayTestBeanHashCode() {
    boolean [] array1 = new boolean [] { true };
    boolean [] array2 = new boolean [] { true, false };
    assertEquals(new IdentityArrayTestBean(null).hashCode(), new IdentityArrayTestBean(null).hashCode());
    assertEquals(new IdentityArrayTestBean(array1).hashCode(), new IdentityArrayTestBean(array1).hashCode());
    assertEquals(new IdentityArrayTestBean(array2).hashCode(), new IdentityArrayTestBean(array2).hashCode());
  }

  public void testEquivalentIdentityArrayTestBeanCompareTo() {
    boolean [][] arrays = new boolean [][] { null,
                                     new boolean [] { true },
                                     new boolean [] { true, false }};
    for (boolean [] array : arrays) {
      assertEquals(0, ((java.lang.Comparable)(new IdentityArrayTestBean(array))).compareTo(new IdentityArrayTestBean(array)));
    }
  }

  public void testNonEquivalentIdentityArrayTestBeanEquals() {
    boolean [] array1 = new boolean [] { true };
    boolean [] array2 = new boolean [] { true, false };
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
    boolean [][] arrays = new boolean [][] { null,
                                     new boolean [] { true },
                                     new boolean [] { true, false }};
    for (boolean [] array1 : arrays) {
      for (boolean [] array2 : arrays) {
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