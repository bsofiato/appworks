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


package br.com.appworks.runtime.lang.support.template;

import java.util.Map;

/**
 * <p>Encapsulated template.</p>
 *
 * <p>A template encapsulate all the processing logic to generated string based
 * on the merging process of a predefined template and a supplied set of data. 
 * This common interface define mecanism to process those templates regardless
 * of it's processing environment.</p>
 *
 * @author Bruno Sofiato
 */

public interface Template {
  
  /**
   * <p>Merges the template against the supplied data.</p>
   *
   * <p>On the template processing process, the supplied processing context 
   * is processed by the encapsulated template merging process, returning to the 
   * client code the result value from the template merging against the supplied
   * data. If there's any error on the merging process, a <tt>TemplateProcessingException</tt> 
   * will be throw.</p>
   *
   * @param  context Supplied processing context
   * @return Template evaluated value
   * @throws TemplateProcessingException If there's any error on the template
   *                                     merging process
   */

  String process(Map <String, Object> context) throws TemplateProcessingException;
  
  /**
   * <p>Gets the template's source code.</p>
   *
   * @return Template's source code
   */
  
  String getSourceCode();
}
