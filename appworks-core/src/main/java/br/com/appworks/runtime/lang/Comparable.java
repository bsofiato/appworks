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

package br.com.appworks.runtime.lang;

import br.com.appworks.runtime.lang.support.comparation.ComparationStrategy;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Marks as comparable.</p>
 *
 * <p>This annotation is used to mark an class as <tt>comparable</tt>. When a 
 * classes is marked as comparable, it's possible to execute to compare 
 * instances from that classe in a field-to-field basis and calculate an 
 * <tt>hashcode</tt> value consistent to that comparation.</p>
 * 
 * <p>When an type is marked as comparable, it will realize the Comparable 
 * interface automaticaly, making possible to use these type on a ordered 
 * collections as is.</p>
 * 
 * @author Bruno Sofiato
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Inherited
        
public @interface Comparable {
  /**
   * <p>The getter method used to fetch the property to be compared.</p>
   *
   * <p>This property is only used when the <tt>@Comparable</tt> annotation is
   * annotating <tt>fields</tt>.</p>
   */

  String getter() default "";
  
  /**
   * <p>The policy used to compare the annotated property.</p>
   *
   * <p>This property is only used when the <tt>@Comparable</tt> annotation is
   * annotating <tt>fields</tt> or <tt>methods</tt>.</p>
   */
  
  ComparationPolicy value() default ComparationPolicy.VALUE;

  /**
   * <p>The policy used to compute the natural order.</p>
   */

  OrderPolicy order() default OrderPolicy.NATURAL;

  /**
   * <p>The evaluation order of the property on the comparation process.</p>
   * 
   * <p>If the evaluation order is <tt>uninitialized</tt>, the annotated
   * property will receive an synthetic, deterministic evaluation order.
   *
   * <p>This property is only used when the <tt>@Comparable</tt> annotation is
   * annotating <tt>fields</tt> or <tt>methods</tt>.</p>
   */

  int evaluationOrder() default -1;
  
  /**
   * <p>The custom comparation strategy.</p>
   *
   * <p>This property defines a custom comparation strategy used in the 
   * comparation process. When used on a type annotation, the custom strategy is
   * used to execute the comparation process of the <tt>instance as a 
   * whole</tt>. When used on a property annotation, the custom stategy is 
   * used to execute the comparation process of the <tt>property 
   * alone</tt>.</p> 
   */
  Class <? extends ComparationStrategy> strategy() default ComparationStrategy.class;
  
  /**
   * <p>Flag indicating whether two operands should be instance of the same 
   * concrete class to be considered equivalent.</p>
   */
  boolean concreteClassesMustMatch() default false;
}
