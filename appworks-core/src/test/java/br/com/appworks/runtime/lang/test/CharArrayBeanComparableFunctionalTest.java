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

public class CharArrayBeanComparableFunctionalTest extends TestCase {
  @br.com.appworks.runtime.lang.Comparable
  private static class ValueArrayTestBean {
    @br.com.appworks.runtime.lang.Comparable
    char [] array;
    public ValueArrayTestBean(char [] array) {
      this.array = array;
    }
  }
  
  @br.com.appworks.runtime.lang.Comparable
  private static class DescendingValueArrayTestBean {
    @br.com.appworks.runtime.lang.Comparable(value = ComparationPolicy.VALUE, order = OrderPolicy.INVERSE)
    char [] array;
    public DescendingValueArrayTestBean(char [] array) {
      this.array = array;
    }
  }

  @br.com.appworks.runtime.lang.Comparable
  private static class IdentityArrayTestBean {
    @br.com.appworks.runtime.lang.Comparable(value = ComparationPolicy.IDENTITY, order = OrderPolicy.INVERSE)
    char [] array;
    public IdentityArrayTestBean(char [] array) {
      this.array = array;
    }
  }

  public CharArrayBeanComparableFunctionalTest(String testName) {
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
    assertEquals(new ValueArrayTestBean(new char [0]), new ValueArrayTestBean(new char [0]));
    assertEquals(new ValueArrayTestBean(new char [] { 'a' }), new ValueArrayTestBean(new char [] { 'a'  }));
    assertEquals(new ValueArrayTestBean(new char [] { 'b' }), new ValueArrayTestBean(new char [] { 'b' }));
    assertEquals(new ValueArrayTestBean(new char [] { 'a' , 'a'  }), new ValueArrayTestBean(new char [] { 'a' , 'a'  }));
  }

  public void testEquivalentValueArrayTestBeanHashCode() {
    assertEquals(new ValueArrayTestBean(null).hashCode(), new ValueArrayTestBean(null).hashCode());
    assertEquals(new ValueArrayTestBean(new char [0]).hashCode(), new ValueArrayTestBean(new char [0]).hashCode());
    assertEquals(new ValueArrayTestBean(new char [] { 'a'  }).hashCode(), new ValueArrayTestBean(new char [] { 'a'  }).hashCode());
    assertEquals(new ValueArrayTestBean(new char [] { 'b' }).hashCode(), new ValueArrayTestBean(new char [] { 'b' }).hashCode());
    assertEquals(new ValueArrayTestBean(new char [] { 'a' , 'a'  }).hashCode(), new ValueArrayTestBean(new char [] { 'a' , 'a'  }).hashCode());
  }
  
  public void testEquivalentValueArrayTestBeanCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(null)));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new char [0]))).compareTo(new ValueArrayTestBean(new char [0])));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'a'  }))).compareTo(new ValueArrayTestBean(new char [] { 'a'  })));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'b' }))).compareTo(new ValueArrayTestBean(new char [] { 'b' })));
    assertEquals(0, ((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'a' , 'a'  }))).compareTo(new ValueArrayTestBean(new char [] { 'a' , 'a'  })));
  }

  public void testNonEquivalentValueArrayTestBeanEquals() {
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new char [0])));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new char [] { 'a'  })));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new char [] { 'b' })));
    assertFalse(new ValueArrayTestBean(null).equals(new ValueArrayTestBean(new char [] { 'a' , 'a'  })));

    assertFalse(new ValueArrayTestBean(new char [0]).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new char [0]).equals(new ValueArrayTestBean(new char [] { 'a'  })));
    assertFalse(new ValueArrayTestBean(new char [0]).equals(new ValueArrayTestBean(new char [] { 'b' })));
    assertFalse(new ValueArrayTestBean(new char [0]).equals(new ValueArrayTestBean(new char [] { 'a' , 'a'  })));

    assertFalse(new ValueArrayTestBean(new char [] { 'a'  }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new char [] { 'a'  }).equals(new ValueArrayTestBean(new char [0])));
    assertFalse(new ValueArrayTestBean(new char [] { 'a'  }).equals(new ValueArrayTestBean(new char [] { 'b' })));
    assertFalse(new ValueArrayTestBean(new char [] { 'a'  }).equals(new ValueArrayTestBean(new char [] { 'a' , 'a'  })));

    assertFalse(new ValueArrayTestBean(new char [] { 'b' }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new char [] { 'b' }).equals(new ValueArrayTestBean(new char [0])));
    assertFalse(new ValueArrayTestBean(new char [] { 'b' }).equals(new ValueArrayTestBean(new char [] { 'a'  })));
    assertFalse(new ValueArrayTestBean(new char [] { 'b' }).equals(new ValueArrayTestBean(new char [] { 'a' , 'a'  })));

    assertFalse(new ValueArrayTestBean(new char [] { 'a' , 'a'  }).equals(new ValueArrayTestBean(null)));
    assertFalse(new ValueArrayTestBean(new char [] { 'a' , 'a'  }).equals(new ValueArrayTestBean(new char [0])));
    assertFalse(new ValueArrayTestBean(new char [] { 'a' , 'a'  }).equals(new ValueArrayTestBean(new char [] { 'a'  })));
    assertFalse(new ValueArrayTestBean(new char [] { 'a' , 'a'  }).equals(new ValueArrayTestBean(new char [] { 'b' })));

  }
  
  public void testNonEquivalentValueArrayTestBeanCompareTo() {
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new char [0])) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new char [] { 'a'  })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new char [] { 'b' })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(null))).compareTo(new ValueArrayTestBean(new char [] { 'a' , 'a'  })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new char [0]))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new char [0]))).compareTo(new ValueArrayTestBean(new char [] { 'a'  })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new char [0]))).compareTo(new ValueArrayTestBean(new char [] { 'b' })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new char [0]))).compareTo(new ValueArrayTestBean(new char [] { 'a' , 'a'  })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'a'  }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'a'  }))).compareTo(new ValueArrayTestBean(new char [0])));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'a'  }))).compareTo(new ValueArrayTestBean(new char [] { 'b' })) < 0);
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'a'  }))).compareTo(new ValueArrayTestBean(new char [] { 'a' , 'a'  })) < 0);
    
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'b' }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'b' }))).compareTo(new ValueArrayTestBean(new char [0])));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'b' }))).compareTo(new ValueArrayTestBean(new char [] { 'a'  })));
    assertTrue(((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'b' }))).compareTo(new ValueArrayTestBean(new char [] { 'a' , 'a'  })) < 0);

    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'a' , 'a'  }))).compareTo(new ValueArrayTestBean(null)));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'a' , 'a'  }))).compareTo(new ValueArrayTestBean(new char [0])));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'a' , 'a'  }))).compareTo(new ValueArrayTestBean(new char [] { 'a'  })));
    assertTrue(0 < ((java.lang.Comparable)(new ValueArrayTestBean(new char [] { 'a' , 'a'  }))).compareTo(new ValueArrayTestBean(new char [] { 'b' })));
  }
  
   public void testEquivalentDescendingValueArrayTestBeanEquals() {
    assertEquals(new DescendingValueArrayTestBean(null), new DescendingValueArrayTestBean(null));
    assertEquals(new DescendingValueArrayTestBean(new char [0]), new DescendingValueArrayTestBean(new char [0]));
    assertEquals(new DescendingValueArrayTestBean(new char [] { 'a'  }), new DescendingValueArrayTestBean(new char [] { 'a'  }));
    assertEquals(new DescendingValueArrayTestBean(new char [] { 'b' }), new DescendingValueArrayTestBean(new char [] { 'b' }));
    assertEquals(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  }), new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  }));
  }

  public void testDescendingValueArrayTestBeanBeanHashCode() {
    assertEquals(new DescendingValueArrayTestBean(null).hashCode(), new DescendingValueArrayTestBean(null).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new char [0]).hashCode(), new DescendingValueArrayTestBean(new char [0]).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new char [] { 'a'  }).hashCode(), new DescendingValueArrayTestBean(new char [] { 'a'  }).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new char [] { 'b' }).hashCode(), new DescendingValueArrayTestBean(new char [] { 'b' }).hashCode());
    assertEquals(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  }).hashCode(), new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  }).hashCode());
  }
  
  public void testEquivalentDescendingValueArrayTestBeanCompareTo() {
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(null)));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [0]))).compareTo(new DescendingValueArrayTestBean(new char [0])));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'a'  }))).compareTo(new DescendingValueArrayTestBean(new char [] { 'a'  })));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'b' }))).compareTo(new DescendingValueArrayTestBean(new char [] { 'b' })));
    assertEquals(0, ((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  }))).compareTo(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  })));
  }

  public void testNonEquivalentDescendingValueArrayTestBeanEquals() {
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new char [0])));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new char [] { 'a'  })));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new char [] { 'b' })));
    assertFalse(new DescendingValueArrayTestBean(null).equals(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  })));

    assertFalse(new DescendingValueArrayTestBean(new char [0]).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new char [0]).equals(new DescendingValueArrayTestBean(new char [] { 'a'  })));
    assertFalse(new DescendingValueArrayTestBean(new char [0]).equals(new DescendingValueArrayTestBean(new char [] { 'b' })));
    assertFalse(new DescendingValueArrayTestBean(new char [0]).equals(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  })));

    assertFalse(new DescendingValueArrayTestBean(new char [] { 'a'  }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new char [] { 'a'  }).equals(new DescendingValueArrayTestBean(new char [0])));
    assertFalse(new DescendingValueArrayTestBean(new char [] { 'a'  }).equals(new DescendingValueArrayTestBean(new char [] { 'b' })));
    assertFalse(new DescendingValueArrayTestBean(new char [] { 'a'  }).equals(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  })));

    assertFalse(new DescendingValueArrayTestBean(new char [] { 'b' }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new char [] { 'b' }).equals(new DescendingValueArrayTestBean(new char [] { 'a'  })));
    assertFalse(new DescendingValueArrayTestBean(new char [] { 'b' }).equals(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  })));

    assertFalse(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  }).equals(new DescendingValueArrayTestBean(null)));
    assertFalse(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  }).equals(new DescendingValueArrayTestBean(new char [0])));
    assertFalse(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  }).equals(new DescendingValueArrayTestBean(new char [] { 'a'  })));
    assertFalse(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  }).equals(new DescendingValueArrayTestBean(new char [] { 'b' })));
  }
  
  public void testNonEquivalentDescendingValueArrayTestBeanCompareTo() {
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new char [0])));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new char [] { 'a'  })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new char [] { 'b' })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(null))).compareTo(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [0]))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [0]))).compareTo(new DescendingValueArrayTestBean(new char [] { 'a'  })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [0]))).compareTo(new DescendingValueArrayTestBean(new char [] { 'b' })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [0]))).compareTo(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'a'  }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'a'  }))).compareTo(new DescendingValueArrayTestBean(new char [0])) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'a'  }))).compareTo(new DescendingValueArrayTestBean(new char [] { 'b' })));
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'a'  }))).compareTo(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  })));
    
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'b' }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'b' }))).compareTo(new DescendingValueArrayTestBean(new char [0])) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'b' }))).compareTo(new DescendingValueArrayTestBean(new char [] { 'a'  })) < 0);
    assertTrue(0 < ((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'b' }))).compareTo(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  })));

    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  }))).compareTo(new DescendingValueArrayTestBean(null)) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  }))).compareTo(new DescendingValueArrayTestBean(new char [0])) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  }))).compareTo(new DescendingValueArrayTestBean(new char [] { 'a'  })) < 0);
    assertTrue(((java.lang.Comparable)(new DescendingValueArrayTestBean(new char [] { 'a' , 'a'  }))).compareTo(new DescendingValueArrayTestBean(new char [] { 'b' })) < 0);
  }

  public void testEquivalentIdentityArrayTestBeanEquals() {
    char [] array1  = new char [] { 'a' };
    char [] array2 = new char [] { 'a', 'b' };
    assertEquals(new IdentityArrayTestBean(null), new IdentityArrayTestBean(null));
    assertEquals(new IdentityArrayTestBean(array1), new IdentityArrayTestBean(array1));
    assertEquals(new IdentityArrayTestBean(array2), new IdentityArrayTestBean(array2));
  }
  
  public void testEquivalentIdentityArrayTestBeanHashCode() {
    char [] array1 = new char [] { 'a' };
    char [] array2 = new char [] { 'a', 'b' };
    assertEquals(new IdentityArrayTestBean(null).hashCode(), new IdentityArrayTestBean(null).hashCode());
    assertEquals(new IdentityArrayTestBean(array1).hashCode(), new IdentityArrayTestBean(array1).hashCode());
    assertEquals(new IdentityArrayTestBean(array2).hashCode(), new IdentityArrayTestBean(array2).hashCode());
  }

  public void testEquivalentIdentityArrayTestBeanCompareTo() {
    char [][] arrays = new char [][] { null,
                                     new char [] { 'a' },
                                     new char [] { 'a', 'b' }};
    for (char [] array : arrays) {
      assertEquals(0, ((java.lang.Comparable)(new IdentityArrayTestBean(array))).compareTo(new IdentityArrayTestBean(array)));
    }
  }

  public void testNonEquivalentIdentityArrayTestBeanEquals() {
    char [] array1 = new char [] { 'a' };
    char [] array2 = new char [] { 'a', 'b' };
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
    char [][] arrays = new char [][] { null,
                                     new char [] { 'a' },
                                     new char [] { 'a', 'b' }};
    for (char [] array1 : arrays) {
      for (char [] array2 : arrays) {
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