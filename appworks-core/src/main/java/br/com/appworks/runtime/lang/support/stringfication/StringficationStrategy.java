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

/**
 * <p>Defines a stringfication strategy.</p>
 * 
 * <p>A stringfication strategy responsibility is to encapsulate a set of 
 * string representation creation semantics.</p>
 * 
 * <p>The stringfication strategy encapsulate all required task or processes to 
 * create a string representation from a supplied object. This facilitates code
 * reuse and minimizes code complexity.</p>
 *
 * @see java.lang.Object#toString()
 * @param  <Type> Type associated with this stringfication strategy
 * @author Bruno Sofiato
 */
public interface StringficationStrategy <Type> extends Cloneable {
  
  /**
   * <p>Create a string representation from an supplied instance.</p>
   *
   * <p>If the source object is <tt>null</tt>, the string representation 
   * <tt>may not</tt> be <tt>null</tt>. The client code using the string 
   * representation must check the return string representation for a 
   * <tt>null</tt> reference.</p>
   *
   * @param object Object from which the string representation will be created
   * @param sb StringBuilder to append the string representation from the
   *           supplied instance.
   */
  
  void toString(Type object, StringBuilder sb);
  
  /**
   * <p>Clones a stringfication strategy.</p>
   *
   * <p>A stringfication strategy instance may be clone if it's used as an
   * high specialized stringfication strategy mapped to an type.</p>
   *
   * @throws CloneNotSupportedException If there's an error on the 
   *                                    stringfication strategy cloning process
   * @return The stringfication strategy clone
   */
  Object clone() throws CloneNotSupportedException;
}
