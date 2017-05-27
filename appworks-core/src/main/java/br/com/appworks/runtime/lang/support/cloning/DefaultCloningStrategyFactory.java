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

package br.com.appworks.runtime.lang.support.cloning;

import br.com.appworks.runtime.lang.support.property.PropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.Cloneable;
import br.com.appworks.runtime.lang.CloningPolicy;
import br.com.appworks.runtime.lang.support.reflect.ClassUtils;
import br.com.appworks.runtime.lang.support.property.PropertyAccessStrategy;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * <p>Cloning strategy factory implementation.</p>
 * 
 * <p>This cloning strategy factory implementation uses annotated metadata to 
 * realizes the supplied type cloning semantics and construct the cloning 
 * strategy reflecting these semantics (Consult <tt>@Cloneable</tt> 
 * documentation on how express the cloning semantics). Some high specialized 
 * cloning strategies can be mapped on the factory in a class to class 
 * basis.</p>
 *
 * <p>A property strategy factory is associated with this cloning strategy 
 * factory to provide a agnostic way of creating strategies to access or modify
 * properties of the to be cloned instances (See <tt>PropertyAccessStrategyFactory</tt> documentation for info on the property 
 * access strategy creation processs).</p>
 *
 * @see br.com.appworks.runtime.lang.Cloneable
 * @see br.com.appworks.runtime.lang.support.property.PropertyAccessStrategyFactory
 * @author Bruno Sofiato
 */

public class DefaultCloningStrategyFactory implements CloningStrategyFactory {
  /**
   * <p>Custom cloning strategy by type mapping.</p>
   */
  private Map <Class, CloningStrategy> customTypeCloningStrategyMapping = Collections.EMPTY_MAP;
  
  /**
   * <p>Associated property access strategy factory.</p>
   */
  
  private PropertyAccessStrategyFactory propertyAccessStrategyFactory;
  
  /**
   * <p>Constructs a cloning strategy reflecting the supplied type cloning 
   * semantics.</p>
   *
   * <p>This method may, based on the supplied type return four distints kinds
   * of cloning strategy. If the {@link Cloneable} interface is realized by the
   * supplied class, a <tt>CloneableCloningStrategy</tt> is return, however 
   * if the supplied class is an <tt>array</tt>, the return cloning strategy is
   * a instance of <tt>ArrayCloningStrategy</tt>. A specialized cloning 
   * strategy may be return if there is one mapped to the supplied type. Finally
   * if none of the previous cloning strategies creation scenarios are 
   * applicably, an reflection based cloning strategy is returned.</p>
   * 
   * @param  <Type> Parametrized type for the cloning strategy be constructed
   * @param  klass Type for the cloning strategy be constructed 
   * @return Cloning strategy reflecting the supplied type cloning semantics
   * @throws CloneNotSupportedException If there's a problem cloning a custom 
   *                                    type cloning strategy for use
   * @see CloneableCloningStrategy
   * @see ArrayCloningStrategy
   * @see ReflectionCloningStrategy
   */
  private <Type> CloningStrategy <Type> createCloningStrategy(final Class <Type> klass) throws CloneNotSupportedException {
    if (br.com.appworks.runtime.lang.support.cloning.Cloneable.class.isAssignableFrom(klass)) {
      return new CloneableCloningStrategy();
    } else if (klass.isArray()) {
      return new ArrayCloningStrategy(klass.getComponentType());
    } else if (getCustomTypeCloningStrategyMapping().containsKey(klass)) {
      return (CloningStrategy<Type>) (getCustomTypeCloningStrategyMapping().get(klass).clone());
    } else {
      return new ReflectionCloningStrategy();
    }
  }
  
  /**
   * <p>Checks if a field may elegible for deep cloning.</p>
   *
   * <p>To be deep cloned, a field must not be a static field (because these 
   * fields are shared across instances, it's not necessary to clone them), 
   * final (they're immutable) or synthetic (synthetic field are not associated
   * with the class semantics).</p>
   * 
   * <p>Besides that, a field that have a custom cloning strategy mapped to it's
   * type will be considered elegible for deep cloning. If a field is an array
   * it's also considered elegible. If the type of the supplied field is a 
   * <tt>cloneable</tt> type, the field can be deep cloned also. If the field 
   * type isn't a <tt>cloneable</tt> type, but isn't a <tt>final</tt> class 
   * either (if the field type can be extended) then the supplied field can be 
   * deep cloned also.</p>
   *
   * @param  field Field to be tested
   * @return <tt>True</tt> if the supplied field is elegible to deep cloning, 
   *         <tt>false</tt> otherwise
   */
  private boolean isDeepCloneable(final Field field) {
    if ((!Modifier.isStatic(field.getModifiers())) &&
        (!Modifier.isFinal(field.getModifiers())) &&
        (!field.isSynthetic())) {
      if (getCustomTypeCloningStrategyMapping().containsKey(field.getType())) {
        return true;
      } else if (java.lang.Cloneable.class.isAssignableFrom(field.getType())) {
        return true;
      } else if (!Modifier.isFinal(field.getType().getModifiers())) {
        return true;
      }
    }
    return false;
  }
  
  
  /**
   * <p>Creates a new cloning handler implementing the supplied class 
   * cloning semantics.</p>
   *
   * <p>This method process all properties from the supplied class and creates 
   * cloning processors to process all properties elegibly for cloning 
   * processing.</p>
   * 
   * @param  <Type> Parametrized type for the cloning strategy be constructed
   * @param  klass Type for the cloning strategy be constructed 
   * @param  defaultClassCloneableAnnotation Default <tt>@Cloneable</tt> 
   *                                         annotation
   * @return Cloning handler reflecting the supplied type cloning semantics
   * @throws CloneNotSupportedException If there's a problem cloning a custom 
   *                                    type cloning strategy for use
   * @throws IllegalAccessException If the constructor of a custom cloning 
   *                                strategy is not accessible
   * @throws InstantiationException If there is an error on the custom cloning
   *                                strategy instantiation process
   */
  private <Type> CloningHandler <Type> createNewCloningHandler(final Class <Type> klass, final Cloneable defaultClassCloneableAnnotation) throws InstantiationException, IllegalAccessException, CloneNotSupportedException {
    Collection <CloningProcessor <Type>> delegatedCloningProcessors = new ArrayList<CloningProcessor <Type>>();
    for (Field field : ClassUtils.getDeclaredFields(klass)) {
      Cloneable classAnnotation = (field.getDeclaringClass().getAnnotation(Cloneable.class) == null) ? defaultClassCloneableAnnotation : field.getDeclaringClass().getAnnotation(Cloneable.class);
      Cloneable cloneableAnnotation = (field.getAnnotation(Cloneable.class) == null) ? classAnnotation : field.getAnnotation(Cloneable.class);
      CloningStrategy <Object> cloningStrategy = null;
      Class <? extends CloningStrategy> customCloningStrategy = (field.getAnnotation(Cloneable.class) == null) ? CloningStrategy.class : field.getAnnotation(Cloneable.class).strategy();
      if (!(CloningStrategy.class.equals(customCloningStrategy))) {
        cloningStrategy = (CloningStrategy <Object>) (customCloningStrategy.newInstance());
      } else if (isDeepCloneable(field) && CloningPolicy.DEEP.equals(cloneableAnnotation.value())) {
        cloningStrategy = createCloningStrategy((Class) (field.getType()));
      }
      if (cloningStrategy != null) {
        PropertyAccessStrategy <Type, Object> propertyAccessStrategy = getPropertyAccessStrategyFactory().create(field, cloneableAnnotation.getter(), cloneableAnnotation.setter());
        delegatedCloningProcessors.add(new PropertyCloningProcessor<Type, Object>(propertyAccessStrategy, cloningStrategy));
      }
    }
    return new CloningHandler(delegatedCloningProcessors);
  }
  
  /**
   * <p>Creates a new cloning strategy implementing the supplied class 
   * cloning semantics.</p>
   * 
   * <p>To be elegibly to a cloning strategy, a type must be annotated with the 
   * <tt>@Cloneable</tt> annotation. If there is a custom cloning strategy 
   * supplied on this annotation, an instance from this custom strategy is 
   * returned. However, if there isn't a supplied custom cloning strategy, 
   * a specialized cloning strategy is constructed, by processing all properties
   * from the supplied class and creating cloning processors to process all
   * properties elegibly for cloning processing.</p>
   * 
   * @param  <Type> Parametrized type for the cloning strategy be constructed
   * @param  klass Type for the cloning strategy be constructed 
   * @return Cloning strategy reflecting the supplied type cloning semantics, 
   *         or <tt>null</tt> if the supplied type is not elegibly of a cloning
   *         strategy
   */
  
  public <Type> CloningStrategy <Type> create(final Class <Type> klass) {
    try {
      Cloneable defaultClassCloneableAnnotation = ClassUtils.getInheritedAnnotation(klass, Cloneable.class);
      if (defaultClassCloneableAnnotation != null) {
        if (!(CloningStrategy.class.equals(defaultClassCloneableAnnotation.strategy()))) {
          return defaultClassCloneableAnnotation.strategy().newInstance();
        } 
        return createNewCloningHandler(klass, defaultClassCloneableAnnotation);
      }
      return null;
    } catch (CloneNotSupportedException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    } catch (InstantiationException ex) {
      throw new RuntimeException(ex);
    }
  }  
  
  /**
   * <p>Gets the associated property access strategy factory.</p>
   *
   * @return Associated property access strategy factory
   */
  
  public PropertyAccessStrategyFactory getPropertyAccessStrategyFactory() {
    return propertyAccessStrategyFactory;
  }
  
  /**
   * <p>Sets the associated property access strategy factory.</p>
   *
   * @param propertyAccessStrategyFactory Associated property access strategy 
   *                                      factory
   */
  public void setPropertyAccessStrategyFactory(final PropertyAccessStrategyFactory propertyAccessStrategyFactory) {
    this.propertyAccessStrategyFactory = propertyAccessStrategyFactory;
  }
  
  /**
   * <p>Constructs a new cloning strategy factory implementation.</p>
   *
   * @param propertyAccessStrategyFactory Associated property access strategy 
   *                                      factory
   */

  public DefaultCloningStrategyFactory(final PropertyAccessStrategyFactory propertyAccessStrategyFactory) {
    this(propertyAccessStrategyFactory, null);
  }

  /**
   * <p>Constructs a new cloning strategy factory implementation.</p>
   *
   * @param propertyAccessStrategyFactory Associated property access strategy 
   *                                      factory
   * @param customTypeCloningStrategyMapping Custom cloning strategy by type 
   *                                         mapping
   */

  public DefaultCloningStrategyFactory(final PropertyAccessStrategyFactory propertyAccessStrategyFactory, 
                                              final Map <Class, CloningStrategy> customTypeCloningStrategyMapping) {
    setPropertyAccessStrategyFactory(propertyAccessStrategyFactory);
    setCustomTypeCloningStrategyMapping(customTypeCloningStrategyMapping);
  }
  
  /**
   * <p>Gets the custom cloning strategy by type mapping.</p>
   *
   * @return Custom cloning strategy by type mapping
   */

  public Map <Class, CloningStrategy> getCustomTypeCloningStrategyMapping() {
    return customTypeCloningStrategyMapping;
  }

  /**
   * <p>Sets the custom cloning strategy by type mapping.</p>
   *
   * @param customTypeCloningStrategyMapping Custom cloning strategy by type 
   *                                         mapping
   */

  public void setCustomTypeCloningStrategyMapping(final Map <Class, CloningStrategy> customTypeCloningStrategyMapping) {
    if (customTypeCloningStrategyMapping != null) {
      this.customTypeCloningStrategyMapping = customTypeCloningStrategyMapping;
    } else {
      this.customTypeCloningStrategyMapping = Collections.EMPTY_MAP;
    }
  }
}
