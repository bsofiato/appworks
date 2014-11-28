/*
 * CalendarStringficationStrategyTest.java
 * JUnit based test
 *
 * Created on 23 de Novembro de 2005, 00:12
 */

package br.com.appworks.runtime.lang.support.stringfication.test;

import br.com.appworks.runtime.lang.support.stringfication.CalendarStringficationStrategy;
import java.util.Calendar;
import java.util.TimeZone;
import junit.framework.*;

public class CalendarStringficationStrategyTest extends TestCase {
  
  public CalendarStringficationStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    TimeZone.setDefault(TimeZone.getTimeZone("GMT-00:00"));
  }

  protected void tearDown() throws Exception {
    TimeZone.setDefault(null);
  }
  
  public void testNullObjectStringfication() {
    CalendarStringficationStrategy strategy = new CalendarStringficationStrategy ();
    StringBuilder sb = new StringBuilder();
    strategy.toString(null, sb);
    assertEquals("null", sb.toString());
  }
  
  public void testObjectStringfication() {
    CalendarStringficationStrategy strategy = new CalendarStringficationStrategy ();
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(0);
    StringBuilder sb = new StringBuilder();
    strategy.toString(calendar, sb);
    assertEquals("1/1/1970 0:0:0", sb.toString());
  }
}
