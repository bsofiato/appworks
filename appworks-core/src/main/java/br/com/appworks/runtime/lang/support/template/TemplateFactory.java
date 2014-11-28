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

/**
 * <p>Factory of processable template.</p>
 *
 * <p>A template factory creates templates that will be merged against supplied
 * data sets by the client code, it provides an agnostic way of creating 
 * template regardless of it's processing environment, shielding the client code
 * from any dependency with the processing environment or schema.</p>
 *
 * @author Bruno Sofiato
 */

public interface TemplateFactory {
  
  /**
   * <p>Creates a new template.</p>
   *
   * @param  sourceCode Templates's source code
   * @return Template reflecting the supplied source code
   * @throws TemplateCompilationException If there's an error on the template
   *                                      creation process
   */
  Template create(String sourceCode) throws TemplateCompilationException;
}
