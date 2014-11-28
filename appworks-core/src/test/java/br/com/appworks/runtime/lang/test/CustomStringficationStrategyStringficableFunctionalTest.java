/*
 * CustomStringficationStrategyStringficableFunctionalTest.java
 * JUnit based test
 *
 * Created on 3 de Dezembro de 2005, 22:02
 */

package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.Stringficable;
import br.com.appworks.runtime.lang.support.HashTypeMap;
import br.com.appworks.runtime.lang.support.TypeMap;
import br.com.appworks.runtime.lang.support.property.DefaultPropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.reflection.ReflectionBasedPropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.reflection.ReflectionBasedPropertySettingStrategyFactory;
import br.com.appworks.runtime.lang.support.stringfication.CachingStringficationStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.stringfication.CalendarStringficationStrategy;
import br.com.appworks.runtime.lang.support.stringfication.DefaultStringficationStrategyFactory;
import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategy;
import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategyFactory;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import junit.framework.*;
import org.easymock.MockControl;
import static org.easymock.EasyMock.*;

/**
 *
 * @author Bubu
 */
public class CustomStringficationStrategyStringficableFunctionalTest extends TestCase {
  
  public static class CustomStringficationStrategy implements StringficationStrategy <Object> {
    private static MockControl control = MockControl.createControl(StringficationStrategy.class);
    public static MockControl getControl() {
      return control;
    }
    public void toString(Object object, StringBuilder sb) {
      ((StringficationStrategy)(getControl().getMock())).toString(object, sb);
    }
    public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
    }
  }

  @Stringficable(strategy=CustomStringficationStrategy.class)
  private static interface TestInterface {
  }
  
  private static class TestInterfaceChild implements TestInterface {
  }
  
  @Stringficable
  private static class CustomPropertyStringficationStrategyTestBean {
    @Stringficable(strategy=CustomStringficationStrategy.class)
    private String x;
    @Stringficable(strategy=CustomStringficationStrategy.class)
    private Calendar y;
    @Stringficable(strategy=CustomStringficationStrategy.class)
    private Collection z;
    public CustomPropertyStringficationStrategyTestBean(String x, Calendar y, Collection z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }
  }
  
  @Stringficable(strategy=CustomStringficationStrategy.class)
  private static class CustomStringficationStrategyTestBean {
  }
  public CustomStringficationStrategyStringficableFunctionalTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    TypeMap <StringficationStrategy> customStrategyMapping = new HashTypeMap <StringficationStrategy> ();
    customStrategyMapping.put(Calendar.class, new CalendarStringficationStrategy());
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
    klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new CachingStringficationStrategyFactoryAdapter(new DefaultStringficationStrategyFactory(null, new DefaultPropertyAccessStrategyFactory(new ReflectionBasedPropertyGettingStrategyFactory(), new ReflectionBasedPropertySettingStrategyFactory()), customStrategyMapping)));
  }

  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
    klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new Object [] {null});
    CustomStringficationStrategy.getControl().reset();
  }

  public void testCustomPropertyStringficationStrategyTestBeanStringfication() {
    CustomStringficationStrategy customPropertyStringficationStrategy = new CustomStringficationStrategy();
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(0);
    customPropertyStringficationStrategy.toString(eq("X"), isA(StringBuilder.class));
    customPropertyStringficationStrategy.toString(eq(c), isA(StringBuilder.class));
    customPropertyStringficationStrategy.toString(isA(ArrayList.class), isA(StringBuilder.class));
    customPropertyStringficationStrategy.getControl().replay();
    CustomPropertyStringficationStrategyTestBean bean = new CustomPropertyStringficationStrategyTestBean("X", c, new ArrayList());
    assertEquals("[x : , y : , z : ]", bean.toString());
    customPropertyStringficationStrategy.getControl().verify();
  }
  
  public void testCustomStringficationStrategyTestBeanStringfication() {
    CustomStringficationStrategyTestBean bean = new CustomStringficationStrategyTestBean();
    CustomStringficationStrategy customStringficationStrategy = new CustomStringficationStrategy();
    customStringficationStrategy.toString(eq(bean), isA(StringBuilder.class));
    customStringficationStrategy.getControl().replay();
    assertEquals("", bean.toString());
    customStringficationStrategy.getControl().verify();
  }
  
   public void testCustomStringficationStrategyTestInterfaceChildStringfication() {
    TestInterfaceChild bean = new TestInterfaceChild();
    CustomStringficationStrategy customStringficationStrategy = new CustomStringficationStrategy();
    customStringficationStrategy.toString(eq(bean), isA(StringBuilder.class));
    customStringficationStrategy.getControl().replay();
    assertEquals("", bean.toString());
    customStringficationStrategy.getControl().verify();
  }
}
