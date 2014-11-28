/*
 * CalendarBeanStringficableFunctionalTest.java
 * JUnit based test
 *
 * Created on 3 de Dezembro de 2005, 17:50
 */

package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.Stringficable;
import br.com.appworks.runtime.lang.StringficationPolicy;
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
import java.util.Calendar;
import java.util.TimeZone;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class CalendarBeanStringficableFunctionalTest extends TestCase {
  @Stringficable
  private static class ToStringCalendarTestBean {
    @Stringficable
    private Calendar calendar;
    public ToStringCalendarTestBean(Calendar calendar) {
      this.calendar = calendar;
    }
  }
  
  @Stringficable
  private static class IdentityCalendarTestBean {
    @Stringficable(StringficationPolicy.IDENTITY)
    private Calendar calendar;
    public IdentityCalendarTestBean(Calendar calendar) {
      this.calendar = calendar;
    }
  }
  
  public CalendarBeanStringficableFunctionalTest(String testName) {
    super(testName);
  }

 protected void setUp() throws Exception {
    TypeMap <StringficationStrategy> customStrategyMapping = new HashTypeMap <StringficationStrategy> ();
    customStrategyMapping.put(Calendar.class, new CalendarStringficationStrategy());
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
    klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new CachingStringficationStrategyFactoryAdapter(new DefaultStringficationStrategyFactory(null, new DefaultPropertyAccessStrategyFactory(new ReflectionBasedPropertyGettingStrategyFactory(), new ReflectionBasedPropertySettingStrategyFactory()), customStrategyMapping)));
    TimeZone.setDefault(TimeZone.getTimeZone("GMT-00:00"));
  }

  protected void tearDown() throws Exception {
    Class klass = Class.forName("br.com.appworks.runtime.lang.support.stringfication.aop.StringficationAspect");
    klass.getDeclaredMethod("setStaticStringficationStrategyFactory", StringficationStrategyFactory.class).invoke(null, new Object [] {null});
    TimeZone.setDefault(null);
  }
  
  public void testToStringCalendarTestBeanWithNullProperty() {
    assertEquals("[calendar : null]", new ToStringCalendarTestBean(null).toString());
  }
  
  public void testToStringCalendarTestBean() {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(0);
    assertEquals("[calendar : 1/1/1970 0:0:0]", new ToStringCalendarTestBean(c).toString());
  }
  
  public void testIdentityCalendarTestBeanWithNullProperty() {
    assertEquals("[calendar : null]", new IdentityCalendarTestBean(null).toString());
  }

  public void testIdentityCalendarTestBean() {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(0);
    assertEquals("[calendar : java.util.GregorianCalendar@" + Integer.toHexString(System.identityHashCode(c)) + "]" , new IdentityCalendarTestBean(c).toString());
  }
}
