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

package br.com.appworks.runtime.lang.support;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>A type based map.</p>
 *
 * <p>A type based map must be <tt>inheritance aware</tt>. A <tt>inheritance 
 * aware</tt> map must be able to fetch mapped values based on the type 
 * hierarchy. More formally, if a type map contains at a mapping <tt>type, 
 * value</tt> and <tt>subtype</tt> is a subtype from <tt>type</tt>, then this
 * map must contains the <tt>subtype, value</tt> mapping.</p>
 * 
 * @author Bruno Sofiato
 * @param <Type> Parametrized value type for type map
 */

public interface TypeMap <Type> extends Map <Class, Type>, Serializable {
}
