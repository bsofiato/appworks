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

package br.com.appworks.runtime.lang.support.property;

import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.property.setting.PropertySettingStrategy;

/**
 * <p>Strategy to access a object's property.</p>
 *
 * <p>The property access strategy encapsulate the logic for fetching and 
 * setting a object's property. Property access strategy implementations must 
 * realizes the <tt>PropertyGettingStrategy</tt> and the 
 * <tt>PropertySettingStrategy</tt> contract (To more info on these contract 
 * consult the <tt>PropertyGettingStrategy</tt> and the 
 * <tt>PropertySettingStrategy</tt> documentation).</p>
 *
 * @param <AccessedType> Parametrized property owner type for this property 
 *                       access strategy
 * @param <PropertyType> Parametrized property type for this property access
 *                       strategy
 * @author Bruno Sofiato
 */

public interface PropertyAccessStrategy <AccessedType extends Object, PropertyType extends Object> extends PropertyGettingStrategy <AccessedType, PropertyType>, PropertySettingStrategy <AccessedType, PropertyType>  {
}
