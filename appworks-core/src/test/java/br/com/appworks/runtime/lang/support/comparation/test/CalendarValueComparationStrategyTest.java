/*
 * CalendarValueComparationStrategyTest.java
 * JUnit based test
 *
 * Created on 28 de Julho de 2005, 22:12
 */

package br.com.appworks.runtime.lang.support.comparation.test;
import br.com.appworks.runtime.lang.support.comparation.CalendarValueComparationStrategy;
import java.util.Calendar;
import java.util.TimeZone;


/**
 *
 * @author Bubu
 */
public class CalendarValueComparationStrategyTest extends AbstractComparationStrategyTest {
  
  public CalendarValueComparationStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  public void testCalendarEqualsSanityCheck() {
    Calendar op1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar op2 = (Calendar)(op1.clone());
    assertEquals(op1, op2);
    assertFalse(op1.before(op2));
    assertFalse(op1.after(op2));
    op2.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    assertFalse(op1.equals(op2));
    assertFalse(op1.before(op2));
    assertFalse(op1.after(op2));
  }
  
  public void testCalendarHashCodeSanityCheck() {
    Calendar op1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar op2 = (Calendar)(op1.clone());
    assertEquals(op1.hashCode(), op2.hashCode());
    assertEquals(op1.getTimeInMillis(), op2.getTimeInMillis());
    op2.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    assertFalse(op1.hashCode() == op2.hashCode());
    assertEquals(op1.getTimeInMillis(), op2.getTimeInMillis());
  }
  
  public void testCalendarCompareToSanityCheck() {
    Calendar op1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar op2 = (Calendar)(op1.clone());
    assertEquals(0, op1.compareTo(op2));
    op2.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    assertEquals(0, op1.compareTo(op2));
  }
  
  public void test() {
    Calendar op1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    Calendar op2 = (Calendar)(op1.clone());
    op2.setTimeZone(TimeZone.getTimeZone("GMT-3"));
    Calendar op3 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    op3.add(Calendar.MONTH, 1);
    Calendar op4 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    op4.add(Calendar.MONTH, -1);
    checkContract(new CalendarValueComparationStrategy(), op1, op2, op3);
    checkContract(new CalendarValueComparationStrategy(), op1, op2, op4);
    checkContract(new CalendarValueComparationStrategy(), op1, op3, op4);
    checkContract(new CalendarValueComparationStrategy(), op2, op3, op4);
    checkContract(new CalendarValueComparationStrategy(), op1, op1.clone(), op1);
  }
}
