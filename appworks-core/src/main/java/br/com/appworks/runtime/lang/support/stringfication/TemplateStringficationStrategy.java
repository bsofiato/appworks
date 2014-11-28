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

import br.com.appworks.runtime.lang.support.template.Template;
import br.com.appworks.runtime.lang.support.template.TemplateProcessingException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Stringfication strategy based on template.</p>
 *  
 * <p>This stringfication strategy creates string representations based on a 
 * associated template, which performs all steps required to create a string 
 * representation from the supplied object.</p>
 *
 * @param  <Type> Type associated with this stringfication strategy
 * @author Bruno Sofiato
 */
public class TemplateStringficationStrategy<Type> extends AbstractStringficationStrategy<Type> {

  /**
   * <p>Source object identitifer.</p>
   *
   * <p>This identitifer is used to identify the source object within the 
   * template processing context.</p>
   */
  private static final String THIS_IDENTIFICATOR = "this";
  /**
   * <p>Associated template.</p>
   */
  private final Template template;

  /**
   * <p>Gets the associated template.</p>
   *
   * @return Associated template
   */
  private Template getTemplate() {
    return template;
  }

  /**
   * <p>Constructs a new stringfication strategy based on template.</p>
   *
   * @param template Associated template
   */
  public TemplateStringficationStrategy(final Template template) {
    this.template = template;
  }

  /**
   * <p>Gets the supplied object's string representation created by the 
   * associated template.</p>
   * 
   * <p>This method creates an template processing context and associates the 
   * supplied object to it, using the source object identifier. The string 
   * representation creation process is delegated to the associated template, 
   * obtaining the string representation.</p>
   *
   * @param object Object to obtain the string representation
   * @param sb StringBuilder to append the string representation from the
   *           supplied instance.
   */
  protected void safeToString(final Type object, final StringBuilder sb) {
    try {
      Map<String, Object> context = new HashMap<String, Object>();
      context.put(THIS_IDENTIFICATOR, object);
      sb.append(getTemplate().process(context));
    } catch (TemplateProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }
}
