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

package br.com.appworks.runtime.lang.support.stringfication;
import java.util.Calendar;


/**
 * <p>Stringfication strategy for calendar instances.</p>
 *
 * <p>The calendar stringfication strategy is a high specialized stringfication
 * strategy that formats an calendar instance in a string representation 
 * containing it's date and time.</p>
 * 
 * <p>The supplied calendar instance is formated using the 
 * <code>MM/dd/yyyy hh:mm:ss.SSS aa</code> formating pattern (for more info on 
 * date formating pattern semantics, consult the <tt>SimpleDateFormat</tt> class
 * documentation).</p>
 *
 * @author Bruno Sofiato
 */
public class CalendarStringficationStrategy extends AbstractStringficationStrategy <Calendar> {
  
  /**
   * <p>Gets the supplied calendar instance's string representation.</p>
   *
   * <p>The string representation returned by this strategy implementation is  
   * formated by the <code>MM/dd/yyyy hh:mm:ss.SSS aa</code> date formating 
   * pattern.</p>
   *
   * <p>For more info on the formating pattern semantics, consult the 
   * <tt>SimpleDateFormat</tt> class documentation.</p>
   *
   * @param object Calendar to obtain the string representation
   * @param sb StringBuilder to append the string representation from the
   *           supplied instance.
   */
  protected void safeToString(final Calendar object, final StringBuilder sb) {
    if (object != null) {
      sb.append(object.get(Calendar.DAY_OF_MONTH));
      sb.append('/');
      sb.append(object.get(Calendar.MONTH) + 1);
      sb.append('/');
      sb.append(object.get(Calendar.YEAR));
      sb.append(' ');
      sb.append(object.get(Calendar.HOUR_OF_DAY));
      sb.append(':');
      sb.append(object.get(Calendar.MINUTE));
      sb.append(':');
      sb.append(object.get(Calendar.SECOND));
    }
  }
}
