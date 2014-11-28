/**
 * The contents of this file are subject to the terms of the Common Development 
 * and Distribution License, Version 1.0 only (the "License"). You may not use 
 * this file except in compliance with the License.
 *
 * You can obtain a copy of the license at LICENSE.html or 
 * http://www.sun.com/cddl/cddl.html. See the License for the specific language 
 * governing permissions and limitations under the License.
 *
 * When distributing Covered Code, include this disclaimer in each file and 
 * include the License file at LICENSE.html. If applicable, add the following 
 * below this disclamer, with the fields enclosed by brackets "[]" replaced with 
 * your own identifying information: 
 * 
 * Portions Copyright [yyyy] [name of copyright owner]
 *
 * Copyright 2005 AppWorks, Inc.  All rights reserved.
 * Use is subject to license terms.
 */

package br.com.appworks.runtime.lang.support.comparation;

import br.com.appworks.runtime.lang.OrderPolicy;
import java.util.Calendar;

/**
 * <p>Comparation strategy for calendar instances.</p>
 *
 * <p>The calendar value comparation strategy is a high specialized value 
 * comparation strategy that deal with calendar instances. The comparation 
 * semantics defined by this strategy defines that to be considered equivalent, 
 * two calendar instances must relates to the same <tt>time instant</tt> (All 
 * other info, like timezone, are considered to be irrelevant to the comparation
 * process).</p>
 *
 * @author Bruno Sofiato
 */
public class CalendarValueComparationStrategy extends AbstractComparationStrategy <Calendar> {
  
  /**
   * <p>Compares two calendars.</p>
   *
   * <p>This method delegates the comparation processing to the 
   * <tt>Calendar.compareTo()</tt> method.</p>
   *
   * @param  op1 First calendar
   * @param  op2 Second calendar
   * @return A negative integer, zero, or a positive integer as first operand
   *         is less than, equal to, or greater than the second operand
   */
  protected int doCompare(final Calendar op1, final Calendar op2) {
    return op1.compareTo(op2);
  }
  
  /**
   * <p>Constructs a new calendar's comparation strategy.</p>
   */

  public CalendarValueComparationStrategy() {
    this(OrderPolicy.NATURAL);
  }

  /**
   * <p>Constructs a new calendar's comparation strategy.</p>
   *
   * @param policy Natural order policy
   */

  public CalendarValueComparationStrategy(final OrderPolicy policy) {
    this(policy, -1);
  }

  /**
   * <p>Constructs a new calendar's comparation strategy.</p>
   *
   * @param policy Natural order policy
   * @param index Evaluation order
   */

  public CalendarValueComparationStrategy(final OrderPolicy policy, 
                                          final int index) {
    setOrderPolicy(policy);
    setIndex(index);
  }

  /**
   * <p>Compares two supplied calendar.</p>
   *
   * <p>This method implements the algorithm to compare the supplied calendars.
   * The calendar's comparation algorithm is based on the calendar's encapsuled 
   * <tt>time instant</tt> (All other info, like timezone, are considered to be 
   * irrelevant to the comparation process).</p>
   * 
   * @param  op1 First calendar instance
   * @param  op2 Second calendar instance
   * @return <tt>true</tt> if the supplied calendar instance are considered 
   *         equivalent, or <tt>false</tt> otherwise
   */

  public boolean equals(final Calendar op1, 
                        final Calendar op2) {
    if (op1 != op2) {
      if ((op1 == null) || (op2 == null)) {
        return false;
      } else {
        return !((op1.before(op2)) || (op1.after(op2)));
      }
    }
    return true;
  }
  
  /**
   * <p>Calculates the supplied calendar hash code.</p>
   *
   * <p>This method implements the algorithm to calculate the supplied calendar 
   * hash code. The hash code calculation algorithm is based on the calendar's 
   * encapsuled <tt>time instant</tt> (All other info, like timezone, are 
   * considered to be irrelevant to the hash code calculation process).</p>
   * 
   * @param  object Supplied calendar instance
   * @return The supplied calendar instance hash code, or <tt>zero</tt> if the
   *         supplied instance is <tt>null</tt>
   */
  public int hashCode(final Calendar object) {
    return (object == null) ? 0 : (int) (object.getTimeInMillis());
  }
}