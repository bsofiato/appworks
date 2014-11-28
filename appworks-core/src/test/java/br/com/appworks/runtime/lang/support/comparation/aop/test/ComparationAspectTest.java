/*
 * ComparationMixinTest.java
 * JUnit based test
 *
 * Created on 6 de Julho de 2005, 22:48
 */

package br.com.appworks.runtime.lang.support.comparation.aop.test;

import junit.framework.*;
import br.com.appworks.runtime.lang.Comparable;
import br.com.appworks.runtime.lang.support.comparation.AbstractComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategyFactory;
import org.aspectj.lang.Aspects;
import org.easymock.MockControl;
/**
 *
 * @author Bubu
 */
public class ComparationAspectTest extends TestCase {
  public static class MockComparationStrategy <Type extends Object> extends AbstractComparationStrategy <Type> {
    public boolean equals(Type op1, Type op2) {
      throw new UnsupportedOperationException();
    }
    public int hashCode(Type op1) {
      throw new UnsupportedOperationException();
    }
    protected int doCompare(Type op1, Type op2) {
      throw new UnsupportedOperationException();
    }
  }
  
  @Comparable
  public static class AdvisedPropertyTestBean {
    @Comparable
    private String x;
  }
  
  @Comparable
  public static class AdvisedDerivedPropertyTestBean {
    @Comparable
    public int getValue() {
      return 0;
    }
  }

  public static class ChildAdvisedTestBean extends AdvisedPropertyTestBean {
    @Comparable
    private String y;
  }
  
  public static class NonAdvisedComparableTestBean implements java.lang.Comparable <NonAdvisedComparableTestBean> {
    public int compareTo(NonAdvisedComparableTestBean nonAdvisedComparableTestBean) {
      return System.identityHashCode(this) - System.identityHashCode(nonAdvisedComparableTestBean);
    }
  }
  
  public static class NonAdvisedTestBean implements java.lang.Comparable <NonAdvisedTestBean> {
    private String x;
    public int hashCode() {
      throw new UnsupportedOperationException();
    }
    public boolean equals(Object o) {
      throw new UnsupportedOperationException();
    }
    public int compareTo(NonAdvisedTestBean bean) {
      throw new UnsupportedOperationException();
    }
  }

  @Comparable(strategy=MockComparationStrategy.class)
  public static class CustomStrategyAdvisedTestBean { 
    @Comparable
    private String x;
  }

  @Comparable(strategy=MockComparationStrategy.class)
    public static class CustomStrategyChildAdvisedTestBean extends CustomStrategyAdvisedTestBean {
  }

  private void setComparationStrategyFactory(ComparationStrategyFactory comparationStrategyFactory) throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.comparation.aop.ComparationAspect");
    klass.getDeclaredMethod("setComparationStrategyFactory", ComparationStrategyFactory.class).invoke(Aspects.aspectOf(klass), comparationStrategyFactory);
  }
  public ComparationAspectTest(String testName) {
    super(testName);
  }
  protected void setUp() throws Exception {
  }
  protected void tearDown() throws Exception {
    setComparationStrategyFactory(null);
  }

  public void testNonAdvisedComparableHashCode() {
    NonAdvisedComparableTestBean nonAdvisedComparableTestBean = new NonAdvisedComparableTestBean();
    assertEquals(System.identityHashCode(nonAdvisedComparableTestBean), nonAdvisedComparableTestBean.hashCode());
  }

  public void testNonAdvisedComparableEquals() {
    NonAdvisedComparableTestBean nonAdvisedComparableTestBean1 = new NonAdvisedComparableTestBean();
    NonAdvisedComparableTestBean nonAdvisedComparableTestBean2 = new NonAdvisedComparableTestBean();
    assertTrue(nonAdvisedComparableTestBean1.equals(nonAdvisedComparableTestBean1));
    assertTrue(nonAdvisedComparableTestBean2.equals(nonAdvisedComparableTestBean2));
    assertFalse(nonAdvisedComparableTestBean1.equals(nonAdvisedComparableTestBean2));
    assertFalse(nonAdvisedComparableTestBean2.equals(nonAdvisedComparableTestBean1));
  }

  public void testNonAdvisedComparableCompareTo() {
    NonAdvisedComparableTestBean nonAdvisedComparableTestBean1 = new NonAdvisedComparableTestBean();
    NonAdvisedComparableTestBean nonAdvisedComparableTestBean2 = new NonAdvisedComparableTestBean();
    assertEquals(0, nonAdvisedComparableTestBean1.compareTo(nonAdvisedComparableTestBean1));
    assertEquals(0, nonAdvisedComparableTestBean2.compareTo(nonAdvisedComparableTestBean2));
    assertEquals(System.identityHashCode(nonAdvisedComparableTestBean1) - System.identityHashCode(nonAdvisedComparableTestBean2), nonAdvisedComparableTestBean1.compareTo(nonAdvisedComparableTestBean2));
    assertEquals(System.identityHashCode(nonAdvisedComparableTestBean2) - System.identityHashCode(nonAdvisedComparableTestBean1), nonAdvisedComparableTestBean2.compareTo(nonAdvisedComparableTestBean1));
  }
  
  public void testNonAdvisedHashCode() {
    MockControl comparationStrategyFactoryControl = null;
    try {
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory equalityStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      comparationStrategyFactoryControl.replay();
      setComparationStrategyFactory(equalityStrategyFactory);
      NonAdvisedTestBean bean = new NonAdvisedTestBean();
      bean.hashCode();
      fail();
    } catch (UnsupportedOperationException ex) {
    } catch (Exception ex) {
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
    }
  }

  public void testNonAdvisedEquals() {
    MockControl comparationStrategyFactoryControl = null;
    try {
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory equalityStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      comparationStrategyFactoryControl.replay();
      setComparationStrategyFactory(equalityStrategyFactory);
      NonAdvisedTestBean bean = new NonAdvisedTestBean();
      bean.equals(null);
      fail();
    } catch (UnsupportedOperationException ex) {
    } catch (Exception ex) {
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
    }
  }

  public void testNonAdvisedCompareTo() {
    MockControl comparationStrategyFactoryControl = null;
    try {
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory equalityStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      comparationStrategyFactoryControl.replay();
      setComparationStrategyFactory(equalityStrategyFactory);
      NonAdvisedTestBean bean = new NonAdvisedTestBean();
      bean.compareTo(null);
      fail();
    } catch (UnsupportedOperationException ex) {
    } catch (Exception ex) {
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
    }
  }
  
  public void testNonConfiguredComparationAspectHashCode() {
    try {
      AdvisedPropertyTestBean bean = new AdvisedPropertyTestBean();
      bean.hashCode();
      fail();
    } catch (IllegalStateException ex) {
    } catch (Exception ex) {
      fail();
    }
  }

  public void testNonConfiguredComparationAspectEquals() {
    try {
      AdvisedPropertyTestBean bean = new AdvisedPropertyTestBean();
      bean.equals(null);
      fail();
    } catch (IllegalStateException ex) {
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testNonConfiguredComparationAspectCompareTo() {
    try {
      AdvisedPropertyTestBean bean = new AdvisedPropertyTestBean();
      assertTrue(bean instanceof java.lang.Comparable);
      ((java.lang.Comparable)(bean)).compareTo(null);
      fail();
    } catch (IllegalStateException ex) {
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

  public void testDerivedPropertyAdvisedHashCode() {
    MockControl comparationStrategyFactoryControl = null;
    MockControl comparationStrategyControl = null;
    try {
      AdvisedDerivedPropertyTestBean bean = new AdvisedDerivedPropertyTestBean();
      comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
      comparationStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy <AdvisedDerivedPropertyTestBean> comparationStrategy = (ComparationStrategy <AdvisedDerivedPropertyTestBean>)(comparationStrategyControl.getMock());
      comparationStrategyFactory.create(AdvisedDerivedPropertyTestBean.class);
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      comparationStrategy.hashCode(bean);
      comparationStrategyControl.setReturnValue(1);
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
      setComparationStrategyFactory(comparationStrategyFactory);
      assertEquals(1, bean.hashCode());
    } catch (UnsupportedOperationException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();
    }
  }

  public void testPropertyAdvisedHashCode() {
    MockControl comparationStrategyFactoryControl = null;
    MockControl comparationStrategyControl = null;
    try {
      AdvisedPropertyTestBean bean = new AdvisedPropertyTestBean();
      comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
      comparationStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy <AdvisedPropertyTestBean> comparationStrategy = (ComparationStrategy <AdvisedPropertyTestBean>)(comparationStrategyControl.getMock());
      comparationStrategyFactory.create(AdvisedPropertyTestBean.class);
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      comparationStrategy.hashCode(bean);
      comparationStrategyControl.setReturnValue(1);
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
      setComparationStrategyFactory(comparationStrategyFactory);
      assertEquals(1, bean.hashCode());
    } catch (UnsupportedOperationException ex) {
      ex.printStackTrace();
      fail();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();
    }
  }
  
  public void testDerivedPropertyAdvisedEquals() {
    MockControl comparationStrategyFactoryControl = null;
    MockControl comparationStrategyControl = null;
    try {
      AdvisedDerivedPropertyTestBean bean = new AdvisedDerivedPropertyTestBean();
      comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
      comparationStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy <AdvisedDerivedPropertyTestBean> comparationStrategy = (ComparationStrategy <AdvisedDerivedPropertyTestBean>)(comparationStrategyControl.getMock());
      comparationStrategyFactory.create(AdvisedDerivedPropertyTestBean.class);
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      comparationStrategy.equals(bean, bean);
      comparationStrategyControl.setReturnValue(false);
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
      setComparationStrategyFactory(comparationStrategyFactory);
      assertFalse(bean.equals(bean));
    } catch (UnsupportedOperationException ex) {
      ex.printStackTrace();
      fail();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();
    }
  }
  
  public void testPropertyAdvisedEquals() {
    MockControl comparationStrategyFactoryControl = null;
    MockControl comparationStrategyControl = null;
    try {
      AdvisedPropertyTestBean bean = new AdvisedPropertyTestBean();
      comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
      comparationStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy <AdvisedPropertyTestBean> comparationStrategy = (ComparationStrategy <AdvisedPropertyTestBean>)(comparationStrategyControl.getMock());
      comparationStrategyFactory.create(AdvisedPropertyTestBean.class);
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      comparationStrategy.equals(bean, bean);
      comparationStrategyControl.setReturnValue(false);
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
      setComparationStrategyFactory(comparationStrategyFactory);
      assertFalse(bean.equals(bean));
    } catch (UnsupportedOperationException ex) {
      ex.printStackTrace();
      fail();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();
    }
  }
  
  public void testDerivedPropertyAdvisedCompareTo() {
    MockControl comparationStrategyFactoryControl = null;
    MockControl comparationStrategyControl = null;
    try {
      AdvisedDerivedPropertyTestBean bean = new AdvisedDerivedPropertyTestBean();
      comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
      comparationStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy <AdvisedDerivedPropertyTestBean> comparationStrategy = (ComparationStrategy <AdvisedDerivedPropertyTestBean>)(comparationStrategyControl.getMock());
      comparationStrategyFactory.create(AdvisedDerivedPropertyTestBean.class);
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      comparationStrategy.compare(bean, bean);
      comparationStrategyControl.setReturnValue(-1);
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
      setComparationStrategyFactory(comparationStrategyFactory);
      assertEquals(-1, ((java.lang.Comparable)(bean)).compareTo(bean));
    } catch (UnsupportedOperationException ex) {
      ex.printStackTrace();
      fail();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();
    }
  }

  public void testPropertyAdvisedCompareTo() {
    MockControl comparationStrategyFactoryControl = null;
    MockControl comparationStrategyControl = null;
    try {
      AdvisedPropertyTestBean bean = new AdvisedPropertyTestBean();
      comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
      comparationStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy <AdvisedPropertyTestBean> comparationStrategy = (ComparationStrategy <AdvisedPropertyTestBean>)(comparationStrategyControl.getMock());
      comparationStrategyFactory.create(AdvisedPropertyTestBean.class);
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      comparationStrategy.compare(bean, bean);
      comparationStrategyControl.setReturnValue(-1);
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
      setComparationStrategyFactory(comparationStrategyFactory);
      assertEquals(-1, ((java.lang.Comparable)(bean)).compareTo(bean));
    } catch (UnsupportedOperationException ex) {
      ex.printStackTrace();
      fail();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();
    }
  }

  public void testChildAdvisedEquals() {
    MockControl comparationStrategyFactoryControl = null;
    MockControl comparationStrategyControl = null;
    try {
      AdvisedPropertyTestBean bean = new AdvisedPropertyTestBean();
      ChildAdvisedTestBean childBean = new ChildAdvisedTestBean();
      comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
      comparationStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy <Object> comparationStrategy = (ComparationStrategy <Object>)(comparationStrategyControl.getMock());
      comparationStrategyFactory.create(ChildAdvisedTestBean.class);
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      comparationStrategy.equals(bean, childBean);
      comparationStrategyControl.setReturnValue(false);
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
      setComparationStrategyFactory(comparationStrategyFactory);
      assertFalse(bean.equals(childBean));
    } catch (UnsupportedOperationException ex) {
      ex.printStackTrace();
      fail();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();
    }
  }

  public void testChildAdvisedCompareTo() {
    MockControl comparationStrategyFactoryControl = null;
    MockControl comparationStrategyControl = null;
    try {
      AdvisedPropertyTestBean bean = new AdvisedPropertyTestBean();
      ChildAdvisedTestBean childBean = new ChildAdvisedTestBean();
      comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
      comparationStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy <Object> comparationStrategy = (ComparationStrategy <Object>)(comparationStrategyControl.getMock());
      comparationStrategyFactory.create(ChildAdvisedTestBean.class);
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      comparationStrategy.compare(bean, childBean);
      comparationStrategyControl.setReturnValue(0);
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
      setComparationStrategyFactory(comparationStrategyFactory);
      assertEquals(0, ((java.lang.Comparable)(bean)).compareTo(childBean));
    } catch (UnsupportedOperationException ex) {
      ex.printStackTrace();
      fail();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();
    }
  }
  
  public void testCustomStrategyAdvisedHashCode() {
    MockControl comparationStrategyFactoryControl = null;
    MockControl comparationStrategyControl = null;
    try {
      CustomStrategyAdvisedTestBean bean = new CustomStrategyAdvisedTestBean();
      comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
      comparationStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy <CustomStrategyAdvisedTestBean> comparationStrategy = (ComparationStrategy <CustomStrategyAdvisedTestBean>)(comparationStrategyControl.getMock());
      comparationStrategyFactory.create(CustomStrategyAdvisedTestBean.class);
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      comparationStrategy.hashCode(bean);
      comparationStrategyControl.setReturnValue(1);
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
      setComparationStrategyFactory(comparationStrategyFactory);
      assertEquals(1, bean.hashCode());
    } catch (UnsupportedOperationException ex) {
      ex.printStackTrace();
      fail();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();
    }
  }
  
  public void testCustomStrategyAdvisedEquals() {
    MockControl comparationStrategyFactoryControl = null;
    MockControl comparationStrategyControl = null;
    try {
      CustomStrategyAdvisedTestBean bean = new CustomStrategyAdvisedTestBean();
      comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
      comparationStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy <CustomStrategyAdvisedTestBean> comparationStrategy = (ComparationStrategy <CustomStrategyAdvisedTestBean>)(comparationStrategyControl.getMock());
      comparationStrategyFactory.create(CustomStrategyAdvisedTestBean.class);
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      comparationStrategy.equals(bean, bean);
      comparationStrategyControl.setReturnValue(true);
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
      setComparationStrategyFactory(comparationStrategyFactory);
      assertTrue(bean.equals(bean));
    } catch (UnsupportedOperationException ex) {
      ex.printStackTrace();
      fail();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();
    }
  }

  public void testCustomStrategyAdvisedCompareTo() {
    MockControl comparationStrategyFactoryControl = null;
    MockControl comparationStrategyControl = null;
    try {
      CustomStrategyAdvisedTestBean bean = new CustomStrategyAdvisedTestBean();
      comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
      comparationStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy <CustomStrategyAdvisedTestBean> comparationStrategy = (ComparationStrategy <CustomStrategyAdvisedTestBean>)(comparationStrategyControl.getMock());
      comparationStrategyFactory.create(CustomStrategyAdvisedTestBean.class);
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      comparationStrategy.compare(bean, bean);
      comparationStrategyControl.setReturnValue(-1);
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
      setComparationStrategyFactory(comparationStrategyFactory);
      assertTrue(bean instanceof java.lang.Comparable);
      assertEquals(-1, ((java.lang.Comparable)(bean)).compareTo(bean));
    } catch (UnsupportedOperationException ex) {
      ex.printStackTrace();
      fail();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();
    }
  }

  public void testCustomStrategyChildAdvisedHashCode() {
    MockControl comparationStrategyFactoryControl = null;
    MockControl comparationStrategyControl = null;
    try {
      CustomStrategyChildAdvisedTestBean bean = new CustomStrategyChildAdvisedTestBean();
      comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
      comparationStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy <CustomStrategyChildAdvisedTestBean> comparationStrategy = (ComparationStrategy <CustomStrategyChildAdvisedTestBean>)(comparationStrategyControl.getMock());
      comparationStrategyFactory.create(CustomStrategyChildAdvisedTestBean.class);
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      comparationStrategy.hashCode(bean);
      comparationStrategyControl.setReturnValue(1);
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
      setComparationStrategyFactory(comparationStrategyFactory);
      assertEquals(1, bean.hashCode());
    } catch (IllegalStateException ex) {
      ex.printStackTrace();
      fail();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();
    }
  }

  public void testCustomStrategyChildAdvisedEquals() {
    MockControl comparationStrategyFactoryControl = null;
    MockControl comparationStrategyControl = null;
    try {
      CustomStrategyChildAdvisedTestBean bean = new CustomStrategyChildAdvisedTestBean();
      comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
      comparationStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy <CustomStrategyChildAdvisedTestBean> comparationStrategy = (ComparationStrategy <CustomStrategyChildAdvisedTestBean>)(comparationStrategyControl.getMock());
      comparationStrategyFactory.create(CustomStrategyChildAdvisedTestBean.class);
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      comparationStrategy.equals(bean, bean);
      comparationStrategyControl.setReturnValue(true);
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
      setComparationStrategyFactory(comparationStrategyFactory);
      assertTrue(bean.equals(bean));
    } catch (UnsupportedOperationException ex) {
      ex.printStackTrace();
      fail();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();
    }
  }

  public void testCustomStrategyChildAdvisedCompareTo() {
    MockControl comparationStrategyFactoryControl = null;
    MockControl comparationStrategyControl = null;
    try {
      CustomStrategyChildAdvisedTestBean bean = new CustomStrategyChildAdvisedTestBean();
      comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);
      comparationStrategyControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
      comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy <CustomStrategyChildAdvisedTestBean> comparationStrategy = (ComparationStrategy <CustomStrategyChildAdvisedTestBean>)(comparationStrategyControl.getMock());
      comparationStrategyFactory.create(CustomStrategyChildAdvisedTestBean.class);
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      comparationStrategy.compare(bean, bean);
      comparationStrategyControl.setReturnValue(-1);
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
      setComparationStrategyFactory(comparationStrategyFactory);
      assertEquals(-1, ((java.lang.Comparable)(bean)).compareTo(bean));
    } catch (UnsupportedOperationException ex) {
      ex.printStackTrace();
      fail();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();
    }
  }
}
