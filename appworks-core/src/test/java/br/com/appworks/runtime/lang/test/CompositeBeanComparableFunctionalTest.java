/*
 * CompositeBeanComparebleFunctionalTest.java
 * JUnit based test
 *
 * Created on 26 de Julho de 2005, 00:46
 */

package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.support.comparation.DefaultComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
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
public class CompositeBeanComparableFunctionalTest extends TestCase {
  @Comparable
  private static class TestBean {
    @Comparable(evaluationOrder=0)
    private String x;
    @Comparable(evaluationOrder=1)
    private String y;
    public TestBean(String x, String y) {
      this.x = x;
      this.y = y;
    }
  }
  @Comparable
  private static class InverseTestBean {
    @Comparable(evaluationOrder=1)
    private String x;
    @Comparable(evaluationOrder=0)
    private String y;
    public InverseTestBean(String x, String y) {
      this.x = x;
      this.y = y;
    }
  }

  public CompositeBeanComparableFunctionalTest(String testName) {
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

  public void testTestBeanEquivalentOperandEquals() {
    String [] xs = new String [] { null, "X", "Y" };
    String [] ys = new String [] { null, "X", "Y" };
    for (String x : xs) {
      for (String y : ys) {
        assertTrue(new TestBean(x, y).equals(new TestBean(x, y)));
      }
    }
  }

  public void testTestBeanEquivalentOperandCompareTo() {
    String [] xs = new String [] { null, "X", "Y" };
    String [] ys = new String [] { null, "X", "Y" };
    for (String x : xs) {
      for (String y : ys) {
        assertEquals(0, ((java.lang.Comparable)(new TestBean(x, y))).compareTo(new TestBean(x, y)));
      }
    }
  }

  public void testTestBeanEquivalentOperandHashCode() {
    String [] xs = new String [] { null, "X", "Y" };
    String [] ys = new String [] { null, "X", "Y" };
    for (String x : xs) {
      for (String y : ys) {
        assertEquals(new TestBean(x, y).hashCode(), new TestBean(x, y).hashCode());
      }
    }
  }

  public void testTestBeanNonEquivalentOperandEquals() {
    String [] xs = new String [] { null, "X", "Y" };
    String [] ys = new String [] { null, "X", "Y" };
    for (String x : xs) {
      for (String y : ys) {
        for (String x2 : xs) {
          for (String y2 : ys) {
            if ((x2 != x) || (y2 != y)) {
              assertFalse(new TestBean(x, y).equals(new TestBean(x2, y2)));
            }
          }
        }
      }
    }
  }
  
  public void testTestBeanNonEquivalentComapareToEquals() {
    String [] xs = new String [] { null, "X", "Y" };
    String [] ys = new String [] { null, "X", "Y" };
    for (String x : xs) {
      for (String y : ys) {
        for (String x2 : xs) {
          for (String y2 : ys) {
            if ((x2 != x) || (y2 != y)) {
              if (x == x2) {
                if (y == null) {
                  assertTrue(((java.lang.Comparable)(new TestBean(x, y))).compareTo(new TestBean(x2, y2)) < 0);
                } else if (y2 == null) {
                  assertTrue(0 < ((java.lang.Comparable)(new TestBean(x, y))).compareTo(new TestBean(x2, y2)));
                } else if (y.compareTo(y2) < 0) {
                  assertTrue(((java.lang.Comparable)(new TestBean(x, y))).compareTo(new TestBean(x2, y2)) < 0);
                } else {
                  assertTrue(0 < ((java.lang.Comparable)(new TestBean(x, y))).compareTo(new TestBean(x2, y2)));
                }
              } else {
                if (x == null) {
                  assertTrue(((java.lang.Comparable)(new TestBean(x, y))).compareTo(new TestBean(x2, y2)) < 0);
                } else if (x2 == null) {
                  assertTrue(0 < ((java.lang.Comparable)(new TestBean(x, y))).compareTo(new TestBean(x2, y2)));
                } else if (x.compareTo(x2) < 0) {
                  assertTrue(((java.lang.Comparable)(new TestBean(x, y))).compareTo(new TestBean(x2, y2)) < 0);
                } else {
                  assertTrue(0 < ((java.lang.Comparable)(new TestBean(x, y))).compareTo(new TestBean(x2, y2)));
                }
              }
            }
          }
        }
      }
    }
  }

  public void testInverseTestBeanEquivalentOperandEquals() {
    String [] xs = new String [] { null, "X", "Y" };
    String [] ys = new String [] { null, "X", "Y" };
    for (String x : xs) {
      for (String y : ys) {
        assertTrue(new InverseTestBean(x, y).equals(new InverseTestBean(x, y)));
      }
    }
  }
  
  public void testInverseTestBeanEquivalentOperandCompareTo() {
    String [] xs = new String [] { null, "X", "Y" };
    String [] ys = new String [] { null, "X", "Y" };
    for (String x : xs) {
      for (String y : ys) {
        assertEquals(0, ((java.lang.Comparable)(new InverseTestBean(x, y))).compareTo(new InverseTestBean(x, y)));
      }
    }
  }

  public void testInverseTestBeanEquivalentOperandHashCode() {
    String [] xs = new String [] { null, "X", "Y" };
    String [] ys = new String [] { null, "X", "Y" };
    for (String x : xs) {
      for (String y : ys) {
        assertEquals(new InverseTestBean(x, y).hashCode(), new InverseTestBean(x, y).hashCode());
      }
    }
  }

  public void testInverseTestBeanNonEquivalentOperandEquals() {
    String [] xs = new String [] { null, "X", "Y" };
    String [] ys = new String [] { null, "X", "Y" };
    for (String x : xs) {
      for (String y : ys) {
        for (String x2 : xs) {
          for (String y2 : ys) {
            if ((x2 != x) || (y2 != y)) {
              assertFalse(new InverseTestBean(x, y).equals(new InverseTestBean(x2, y2)));
            }
          }
        }
      }
    }
  }

  public void testInverseTestBeanNonEquivalentComapareToEquals() {
    String [] xs = new String [] { null, "X", "Y" };
    String [] ys = new String [] { null, "X", "Y" };
    for (String x : xs) {
      for (String y : ys) {
        for (String x2 : xs) {
          for (String y2 : ys) {
            if ((x2 != x) || (y2 != y)) {
              if (y == y2) {
                if (x == null) {
                  assertTrue(((java.lang.Comparable)(new InverseTestBean(x, y))).compareTo(new InverseTestBean(x2, y2)) < 0);
                } else if (x2 == null) {
                  assertTrue(0 < ((java.lang.Comparable)(new InverseTestBean(x, y))).compareTo(new InverseTestBean(x2, y2)));
                } else if (x.compareTo(x2) < 0) {
                  assertTrue(((java.lang.Comparable)(new InverseTestBean(x, y))).compareTo(new InverseTestBean(x2, y2)) < 0);
                } else {
                  assertTrue(0 < ((java.lang.Comparable)(new InverseTestBean(x, y))).compareTo(new InverseTestBean(x2, y2)));
                }
              } else {
                if (y == null) {
                  assertTrue(((java.lang.Comparable)(new InverseTestBean(x, y))).compareTo(new InverseTestBean(x2, y2)) < 0);
                } else if (y2 == null) {
                  assertTrue(0 < ((java.lang.Comparable)(new InverseTestBean(x, y))).compareTo(new InverseTestBean(x2, y2)));
                } else if (y.compareTo(y2) < 0) {
                  assertTrue(((java.lang.Comparable)(new InverseTestBean(x, y))).compareTo(new InverseTestBean(x2, y2)) < 0);
                } else {
                  assertTrue(0 < ((java.lang.Comparable)(new InverseTestBean(x, y))).compareTo(new InverseTestBean(x2, y2)));
                }
              }
            }
          }
        }
      }
    }
  }
}
