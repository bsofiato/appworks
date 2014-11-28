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

import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategy;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Marks as stringficable.</p>
 *
 * <p>This annotation is used to mark an class as <tt>stringficable</tt>. When a 
 * classes is marked as stringficable, it's possible to obtain, via the 
 * <tt>Object.toString()</tt> method, an string representing the internal state
 * from it's instances.</p>
 * 
 * <p>The string obtained by this stringfication facility <tt>should be only
 * used for debugging purposes</tt>, the obtained string isn't localized in
 * anyway.</p>
 *
 * @author Bruno Sofiato
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Inherited
        
public @interface Stringficable {

  /**
   * <p>The getter method used to fetch the property to be compared.</p>
   *
   * <p>This property is only used when the <tt>@Stringficable</tt> annotation 
   * is annotating <tt>fields</tt>.</p>
   */
  
  String getter() default "";

  /**
   * <p>The policy used to create a string representation for the annotated
   * property.</p>
   *
   * <p>This property is only used when the <tt>@Stringficable</tt> annotation 
   * is annotating <tt>fields</tt> or <tt>methods</tt>.</p>
   */
  
  StringficationPolicy value() default StringficationPolicy.TOSTRING;

  /**
   * <p>The template used to generate the string representation for the 
   * annotated type.</p>
   *
   * <p>This property is only used when the <tt>@Stringficable</tt> annotation 
   * is annotating <tt>types</tt>.</p>
   */

  String template() default "";
  
  /**
   * <p>The custom stringfication strategy.</p>
   *
   * <p>This property defines a custom stringfication strategy used in the 
   * stringfication process. When used on a type annotation, the custom 
   * strategy is used to execute the stringfication process of the <tt>instance
   * as a whole</tt>. When used on a property annotation, the custom stategy is 
   * used to execute the stringfication process of the <tt>property 
   * alone</tt>.</p> 
   */
  Class <? extends StringficationStrategy> strategy() default StringficationStrategy.class;
}
