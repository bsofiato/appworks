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

import br.com.appworks.runtime.lang.support.cloning.CloningStrategy;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Marks as cloneable.</p>
 *
 * <p>This annotation is used to mark an class as <tt>cloneable</tt>. When a 
 * classes is marked as cloneable, it's possible to do a field-to-field
 * copy of instanceof of that type.</p>
 * 
 * <p>When an type is marked as clonable, it will realize the Cloneable 
 * interface automaticaly.</p>
 * 
 * @author Bruno Sofiato
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Inherited

public @interface Cloneable {
  
  /**
   * <p>The getter method used to fetch the property to be cloned.</p>
   *
   * <p>This property is only used when the <tt>@Cloneable</tt> annotation is
   * annotating <tt>fields</tt>.</p>
   */
  String getter() default "";

  /**
   * <p>The getter method used to set the property to be cloned.</p>
   *
   * <p>This property is only used when the <tt>@Cloneable</tt> annotation is
   * annotating <tt>fields</tt>.</p>
   */
  String setter() default "";
  
  /**
   * <p>The policy used to clone the annotated property.</p>
   * 
   * <p>The policy defined on the type annotation becomes the <tt>default</tt> 
   * policy used to clone the annotation type properties.</p>
   */
  
  CloningPolicy value() default CloningPolicy.SHALLOW;
  
  /**
   * <p>The custom cloning strategy.</p>
   *
   * <p>This property defines a custom cloning strategy used in the cloning 
   * process. When used on a type annotation, the custom strategy is used to 
   * execute the cloning process of the <tt>instance as a whole</tt>.
   * When used on a property annotation, the custom stategy is used to execute 
   * the cloning process of the <tt>property alone</tt>.</p> 
   */
  
  Class <? extends CloningStrategy> strategy() default CloningStrategy.class;
}
