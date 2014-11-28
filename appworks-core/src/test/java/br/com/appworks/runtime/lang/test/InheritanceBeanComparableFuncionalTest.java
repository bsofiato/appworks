/*
 * InhretanceBeanComparableFuncionalTest.java
 * JUnit based test
 *
 * Created on 24 de Julho de 2005, 16:20
 */

package br.com.appworks.runtime.lang.test;

import junit.framework.*;

/**
 *
 * @author Bubu
 */

import br.com.appworks.runtime.lang.Comparable;
import br.com.appworks.runtime.lang.support.comparation.CachingComparationStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.comparation.DefaultComparationStrategyFactory;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import org.aspectj.lang.Aspects;


public class InheritanceBeanComparableFuncionalTest extends TestCase {
  @Comparable
  private static abstract class AbstractBaseBean {
    @Comparable
    private String x;
    AbstractBaseBean(String x) {
      this.x = x;
    }
  }
  private static class BaseBean extends AbstractBaseBean {
    public BaseBean() {
      this(null);
    }
    public BaseBean(String x) {
      super(x);
    }
  }

  private static class ChildBean1 extends BaseBean {
    public ChildBean1() {
      this(null);
    }
    public ChildBean1(String x) {
      super(x);
    }
  }
  private static class ChildBean2 extends BaseBean {
    public ChildBean2() {
      this(null);
    }
    public ChildBean2(String x) {
      super(x);
    }
  }
  @Comparable
  private static class FieldChildBean extends BaseBean {
    @Comparable
    private String y;
    public FieldChildBean() {
      this(null, null);
    }
    public FieldChildBean(String x, String y) {
      super(x);
      this.y = y;
    }
  }
  
  @Comparable
  public static interface TestInterface {
    @Comparable
    String getX();
  }
  public static abstract class AbstractTestBean {
    protected String x;
    public String getX() {
      return x;
    }
  }
  

  public static class TestInterfaceAbstractTestBeanChild extends AbstractTestBean implements TestInterface {
    public TestInterfaceAbstractTestBeanChild(String x) {
      this.x = x;
    }
  }
  
  public InheritanceBeanComparableFuncionalTest(String testName) {
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

  public void testTestInterface2AbstractClassChildEquals() {
    assertEquals(new TestInterfaceAbstractTestBeanChild("X"), new TestInterfaceAbstractTestBeanChild("X"));
  }

  public void testTestInterface2AbstractClassChildHashCode() {
    assertEquals(new TestInterfaceAbstractTestBeanChild("X").hashCode(), new TestInterfaceAbstractTestBeanChild("X").hashCode());
  }

  public void testTestInterface2AbstractClassChildCompareTo() {
    assertEquals(0,((java.lang.Comparable)(new TestInterfaceAbstractTestBeanChild("X"))).compareTo(new TestInterfaceAbstractTestBeanChild("X")));
  }

  public void testBaseBeanOperandsEquals() {
    BaseBean op1 = new BaseBean("X");
    BaseBean op2 = new BaseBean("X");
    assertTrue(op1.equals(op2));
    assertTrue(op2.equals(op1));
  }
  
  public void testBaseBeanOperandsCompareTo() {
    BaseBean op1 = new BaseBean("X");
    BaseBean op2 = new BaseBean("X");
    assertEquals(0, ((java.lang.Comparable)(op1)).compareTo(op2));
    assertEquals(0, ((java.lang.Comparable)(op2)).compareTo(op1));
  }

  public void testChildBean1OperandsEquals() {
    ChildBean1 op1 = new ChildBean1("X");
    ChildBean1 op2 = new ChildBean1("X");
    assertTrue(op1.equals(op2));
    assertTrue(op2.equals(op1));
  }
  
  public void testChildBean1OperandsCompareTo() {
    ChildBean1 op1 = new ChildBean1("X");
    ChildBean1 op2 = new ChildBean1("X");
    assertEquals(0, ((java.lang.Comparable)(op1)).compareTo(op2));
    assertEquals(0, ((java.lang.Comparable)(op2)).compareTo(op1));
  }
  
   public void testChildBean2OperandsEquals() {
    ChildBean2 op1 = new ChildBean2("X");
    ChildBean2 op2 = new ChildBean2("X");
    assertTrue(op1.equals(op2));
    assertTrue(op2.equals(op1));
  }
  
  public void testChildBean2OperandsCompareTo() {
    ChildBean2 op1 = new ChildBean2("X");
    ChildBean2 op2 = new ChildBean2("X");
    assertEquals(0, ((java.lang.Comparable)(op1)).compareTo(op2));
    assertEquals(0, ((java.lang.Comparable)(op2)).compareTo(op1));
  }

  public void testFieldChildBeanOperandsEquals() {
    FieldChildBean op1 = new FieldChildBean("X", "Y");
    FieldChildBean op2 = new FieldChildBean("X", "Y");
    assertTrue(op1.equals(op2));
    assertTrue(op2.equals(op1));
  }
  
  public void testFieldChildBeanOperandsCompareTo() {
    FieldChildBean op1 = new FieldChildBean("X", "Y");
    FieldChildBean op2 = new FieldChildBean("X", "Y");
    assertEquals(0, ((java.lang.Comparable)(op1)).compareTo(op2));
    assertEquals(0, ((java.lang.Comparable)(op2)).compareTo(op1));
  }

  public void testBaseBeanChildBean1OperandsEquals() {
    BaseBean op1 = new BaseBean("X");
    ChildBean1 op2 = new ChildBean1("X");
    assertTrue(op1.equals(op2));
    assertTrue(op2.equals(op1));
  }

  public void testBaseBeanChildBean1OperandsCompareTo() {
    BaseBean op1 = new BaseBean("X");
    ChildBean1 op2 = new ChildBean1("X");
    assertEquals(0, ((java.lang.Comparable)(op1)).compareTo(op2));
    assertEquals(0, ((java.lang.Comparable)(op2)).compareTo(op1));  
  }
  
  public void testBaseBeanChildBean2OperandsEquals() {
    BaseBean op1 = new BaseBean("X");
    ChildBean2 op2 = new ChildBean2("X");
    assertTrue(op1.equals(op2));
    assertTrue(op2.equals(op1));
  }

  public void testBaseBeanChildBean2OperandsCompareTo() {
    BaseBean op1 = new BaseBean("X");
    ChildBean2 op2 = new ChildBean2("X");
    assertEquals(0, ((java.lang.Comparable)(op1)).compareTo(op2));
    assertEquals(0, ((java.lang.Comparable)(op2)).compareTo(op1));  
  }

  public void testChildBean1ChildBean2OperandsEquals() {
    ChildBean1 op1 = new ChildBean1("X");
    ChildBean2 op2 = new ChildBean2("X");
    assertTrue(op1.equals(op2));
    assertTrue(op2.equals(op1));
  }

  public void testChildBean1ChildBean2OperandsCompareTo() {
    ChildBean1 op1 = new ChildBean1("X");
    ChildBean2 op2 = new ChildBean2("X");
    assertEquals(0, ((java.lang.Comparable)(op1)).compareTo(op2));
    assertEquals(0, ((java.lang.Comparable)(op2)).compareTo(op1));  
  }

  public void testBaseBeanFieldChildBeanOperandsEquals() {
    BaseBean op1 = new BaseBean("X");
    FieldChildBean op2 = new FieldChildBean("X", null);
    assertFalse(op1.equals(op2));
    assertFalse(op2.equals(op1));
  }

  public void testBaseBeanFieldChildBeanOperandsCompareTo() {
    BaseBean op1 = new BaseBean("X");
    FieldChildBean op2 = new FieldChildBean("X", null);
    try {
      ((java.lang.Comparable)(op1)).compareTo(op2);
      fail();
    } catch (ClassCastException ex) {
    }
    try {
      ((java.lang.Comparable)(op2)).compareTo(op1);
      fail();
    } catch (ClassCastException ex) {
    }
  }
}
