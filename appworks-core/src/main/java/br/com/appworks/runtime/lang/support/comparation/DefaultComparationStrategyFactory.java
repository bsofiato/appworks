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

package br.com.appworks.runtime.lang.support.comparation;

import br.com.appworks.runtime.lang.Comparable;
import br.com.appworks.runtime.lang.ComparationPolicy;
import br.com.appworks.runtime.lang.support.reflect.ClassUtils;
import br.com.appworks.runtime.lang.support.property.PropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * <p>Comparation strategy factory implementation.</p>
 * 
 * <p>This comparation strategy factory implementation uses annotated metadata 
 * to realizes the supplied type comparation semantics and construct the 
 * comparation strategy reflecting these semantics (Consult <tt>@Comparable</tt> 
 * documentation on how express the comparation semantics). Some high 
 * specialized comparation strategies can be mapped on the factory in a class to 
 * class basis.</p>
 *
 * <p>A property strategy factory is associated with this comparation strategy 
 * factory to provide a agnostic way of creating strategies to access properties
 * of the to be comparated instances (See <tt>PropertyAccessStrategyFactory</tt>
 * documentation for info on the property access strategy creation processs).</p>
 *
 * @see br.com.appworks.runtime.lang.Comparable
 * @see br.com.appworks.runtime.lang.support.property.PropertyAccessStrategyFactory
 * @author Bruno Sofiato
 */

public class DefaultComparationStrategyFactory implements ComparationStrategyFactory {
  
  /**
   * <p>Custom comparation strategy by type mapping.</p>
   */

  private Map <Class, ComparationStrategy> customTypeValueComparationStrategyMapping = Collections.EMPTY_MAP;
  
  /**
   * <p>Associated property access strategy factory.</p>
   */
  
  private PropertyAccessStrategyFactory propertyAccessStrategyFactory;

  /**
   * <p>Constructs a value based comparation strategy reflecting the supplied 
   * type comparation semantics.</p>
   *
   * <p>This method may, based on the supplied type, return three distints kinds
   * of comparation strategy. If the supplied class is an <tt>array</tt>, the 
   * return comparation strategy is a instance of <tt>ArrayValueComparationStrategy</tt>. 
   * A specialized comparation strategy may be returned if there is one mapped 
   * to the supplied type. Finally if none of the previous comparation 
   * strategies creation scenarios are applicably, an regular value based 
   * comparation strategy is returned.</p>
   *
   * @param  type Type for the value based comparation strategy be constructed 
   * @return Value based comparation strategy reflecting the supplied type 
   *         comparation semantics
   * @see ArrayValueComparationStrategy
   * @see ValueComparationStrategy
   */

  private ComparationStrategy createValueComparationStrategy(final Class type) {
    try {
      if (getCustomTypeValueComparationStrategyMapping().containsKey(type)) {
        return (ComparationStrategy) (getCustomTypeValueComparationStrategyMapping().get(type).clone());
      } else if (type.isArray()) {
        return new ArrayValueComparationStrategy(type.getComponentType());
      } else {
        return new ValueComparationStrategy();
      }
    } catch (CloneNotSupportedException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * <p>Creates a comparation strategy for a given comparation policy and 
   * type.</p>
   *
   * <p>If the supplied policy is the <tt>identity</tt> policy, then the 
   * returned comparation strategy is an instance of <tt>IdentityComparationStrategy</tt>,
   * However, if the supplied policy is the <tt>value</tt> policy, then the 
   * comparation strategy creation process is delegated to the <tt>createValueComparationStrategy</tt>
   * method.</p>
   * 
   * @param  policy Comparation policy
   * @param  type Type for the comparation strategy be constructed
   * @return Comparation strategy reflecting the supplied type comparation 
   *         semantics
   *
   * @see IdentityComparationStrategy
   */
  private ComparationStrategy createComparationStrategy(final Class type, 
                                                        final ComparationPolicy policy) {
    switch (policy) {
      case IDENTITY :  
        return new IdentityComparationStrategy();
      default :  
        return createValueComparationStrategy(type);
    }
  }
  
  /**
   * <p>Creates a new comparation strategy implementing the comparation 
   * semantics from the supplied <tt>@Comparable</tt> annotation.</p>
   *
   * @param  type Compared type of the soon to be created comparation strategy
   * @param  comparableAnnotation Annotation representing the comparation 
   *                              semantics to be implemented
   * @param  <Type> Parametrized type for the comparation strategy be 
   *                constructed
   * @return Comparation strategy implementing the supplied comparation 
   *         semantics
   * @throws InstantiationException If there's an error on the custom 
   *                                comparation strategy instantiation
   * @throws IllegalAccessException If the custom comparation strategy has a 
   *                                <tt>private</tt> default constructor
   */
  
  private <Type> ComparationStrategy <Type> createNewComparationStrategy(final Class <Type> type, 
                                                                         final Comparable comparableAnnotation) throws InstantiationException, IllegalAccessException {
    ComparationStrategy <Type> comparationStrategy = null;
    if (!(ComparationStrategy.class.equals(comparableAnnotation.strategy()))) {
      comparationStrategy = comparableAnnotation.strategy().newInstance();
    } else {
      comparationStrategy = createComparationStrategy(type, comparableAnnotation.value());
    }
    comparationStrategy.setOrderPolicy(comparableAnnotation.order());
    return comparationStrategy;
  }
  
  /**
   * <p>Creates a new comparation strategy implementing the supplied class 
   * comparation semantics.</p>
   *
   * <p>If any property based comparation strategy doesn't have an configured
   * evaluation order, a synthetic evaluation order number will be generated to 
   * the given strategy (this synthetic evalutation order index is based on the
   * declaration order of the associated property on the class file itself, 
   * making this order persistent).</p>
   * 
   * <p>To be elegibly to a comparation strategy, at least one property from the
   * given type, or the type itself, must be annotated with the <tt>@Comparable</tt> 
   * annotation. If there is a custom comparation strategy supplied on this 
   * annotation, an instance from this custom strategy is returned, however, 
   * if there isn't a supplied custom comparation strategy, a specialized 
   * comparation strategy is constructed by processing all properties from the 
   * supplied class and creating property based comparation strategies for all
   * annotated properties from the given class.</p>
   * 
   * @param  klass Type for the comparation strategy be constructed 
   * @param  <Type> Parametrized type for the comparation strategy be 
   *                constructed
   * @return Comparation strategy reflecting the supplied type comparation
   *         semantics, or <tt>null</tt> if the supplied type is not elegibly of
   *         a comparation strategy
   */
  
  private <Type> ComparationStrategy <Type> createNewComparationStrategy(final Class <Type> klass) {
    try {
      SortedSet <ComparationStrategy <Type>> delegatedComparationStrategies = new TreeSet<ComparationStrategy <Type>>();
      int index = Integer.MIN_VALUE;
      for (Field field : ClassUtils.getDeclaredFields(klass)) {
        Comparable comparableAnnotation = field.getAnnotation(Comparable.class);
        if (comparableAnnotation != null) {
          PropertyGettingStrategy <Type, Object> propertyAccessStrategy = getPropertyAccessStrategyFactory().create(field, comparableAnnotation.getter(), null);
          ComparationStrategy comparationStrategy = createNewComparationStrategy(field.getType(), comparableAnnotation);
          int evaluationOrder = (comparableAnnotation.evaluationOrder() == -1) ? ++index : comparableAnnotation.evaluationOrder();
          delegatedComparationStrategies.add(new PropertyComparationStrategy(evaluationOrder, propertyAccessStrategy, comparationStrategy));
        }
      }
      for (Method method : ClassUtils.getDeclaredMethods(klass)) {
        Comparable comparableAnnotation = ClassUtils.getInheritedAnnotation(klass, method, Comparable.class);
        if (comparableAnnotation != null) {
          PropertyGettingStrategy <Type, Object> propertyAccessStrategy = getPropertyAccessStrategyFactory().create(method, null);
          ComparationStrategy comparationStrategy = createNewComparationStrategy(method.getReturnType(), comparableAnnotation);
          int evaluationOrder = (comparableAnnotation.evaluationOrder() == -1) ? ++index : comparableAnnotation.evaluationOrder();
          delegatedComparationStrategies.add(new PropertyComparationStrategy(evaluationOrder, propertyAccessStrategy, comparationStrategy));
        }
      }
      return new ComparationHandler<Type>(klass, delegatedComparationStrategies);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    } catch (InstantiationException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * <p>Checks if a supplied class is elegible to have a comparation strategy, 
   * based by it's methods and fields.</p>
   * 
   * <p>To be elegibly to a comparation strategy, at least one property or 
   * method of the given type must be annotated with the <tt>@Comparable</tt>
   * annotation.</p>
   *
   * @param  klass Class to check for comparation strategy elegibility
   * @return <tt>true</tt> if the supplied class is elegible to have a 
   *         comparation strategy, <tt>false</tt> otherwise
   */
  private boolean isElegible(final Class klass) {
    for (Field field : klass.getDeclaredFields()) {
      if (field.getAnnotation(Comparable.class) != null) {
        return true;
      }
    }
    for (Method method : ClassUtils.getDeclaredMethods(klass)) {
      if (ClassUtils.getInheritedAnnotation(klass, method, Comparable.class) != null) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * <p>Constructs a new comparation strategy factory implementation.</p>
   *
   * @param propertyAccessStrategyFactory Associated property access strategy 
   *                                      factory
   */

  public DefaultComparationStrategyFactory(final PropertyAccessStrategyFactory propertyAccessStrategyFactory) {
    setPropertyAccessStrategyFactory(propertyAccessStrategyFactory);
  }

  /**
   * <p>Constructs a new comparation strategy factory implementation.</p>
   *
   * @param propertyAccessStrategyFactory Associated property access strategy 
   *                                      factory
   * @param customTypeValueComparationStrategyMapping Custom comparation 
   *                                                  strategy by type mapping
   */

  public DefaultComparationStrategyFactory(final PropertyAccessStrategyFactory propertyAccessStrategyFactory, 
                                           final Map <Class, ComparationStrategy> customTypeValueComparationStrategyMapping) {
    setPropertyAccessStrategyFactory(propertyAccessStrategyFactory);
    setCustomTypeValueComparationStrategyMapping(customTypeValueComparationStrategyMapping);
  }
  
  /**
   * <p>Constructs a comparation strategy reflecting the supplied type 
   * comparation semantics.</p>
   *
   * @param  klass Type for the comparation strategy be constructed
   * @param  <Type> Parametrized type for the comparation strategy be 
   *                constructed
   * @return Comparation strategy reflecting the supplied type comparation 
   *         semantics
   */

  public <Type> ComparationStrategy <Type> create(final Class <Type> klass) {
    try {
      Comparable classAnnotation = ClassUtils.getInheritedAnnotation(klass, Comparable.class);
      if (classAnnotation != null) {
        if (!ComparationStrategy.class.equals(classAnnotation.strategy())) {
          ComparationStrategy comparationStrategy = classAnnotation.strategy().newInstance();
          comparationStrategy.setOrderPolicy(classAnnotation.order());
          return comparationStrategy;
        } else if (isElegible(klass)) {
          return createNewComparationStrategy(klass);
        } else if (klass.getSuperclass() != null) {
          return (ComparationStrategy) (create(klass.getSuperclass()));
        }
      }
      return null;
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
   * <p>Gets the custom comparation strategy by type mapping.</p>
   *
   * @return Custom comparation strategy by type mapping
   */

  public Map <Class, ComparationStrategy> getCustomTypeValueComparationStrategyMapping() {
    return customTypeValueComparationStrategyMapping;
  }

  /**
   * <p>Sets the custom comparation strategy by type mapping.</p>
   *
   * @param customTypeValueComparationStrategyMapping Custom comparation 
   *                                                  strategy by type mapping
   */

  public void setCustomTypeValueComparationStrategyMapping(final Map <Class, ComparationStrategy> customTypeValueComparationStrategyMapping) {
    if (customTypeValueComparationStrategyMapping != null) {
      this.customTypeValueComparationStrategyMapping = customTypeValueComparationStrategyMapping;
    } else {
      this.customTypeValueComparationStrategyMapping = Collections.EMPTY_MAP;
    }
  }
}
