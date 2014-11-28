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
import br.com.appworks.runtime.lang.Stringficable;
import br.com.appworks.runtime.lang.StringficationPolicy;
import br.com.appworks.runtime.lang.support.property.PropertyAccessStrategyFactory;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.reflect.ClassUtils;
import br.com.appworks.runtime.lang.support.template.TemplateCompilationException;
import br.com.appworks.runtime.lang.support.template.TemplateFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * <p>Stringfication strategy factory implementation.</p>
 * 
 * <p>This stringfication strategy factory implementation uses annotated 
 * metadata to realizes the supplied type stringfication semantics and construct 
 * the stringfication strategy reflecting these semantics (Consult 
 * <tt>@Stringficable</tt> documentation on how express the stringfication 
 * semantics). Some high specialized stringfication strategies can be mapped on 
 * the factory in a class to class basis.</p>
 *
 * <p>A property strategy factory is associated with this stringfication 
 * strategy factory to provide a agnostic way of creating strategies to access 
 * properties of the to be stringficated instances (See 
 * <tt>PropertyAccessStrategyFactory</tt> documentation for info on the property
 * access strategy creation process). Likewise, a template factory is associated
 * with this strategy factory to provide a agnostic way of creating templates
 * reflecting supplied types stringfication semantics (See <tt>TemplateFactory</tt>
 * documentation for info on the template creation process).</p>
 *
 * @see br.com.appworks.runtime.lang.Stringficable
 * @see br.com.appworks.runtime.lang.support.property.PropertyAccessStrategyFactory
 * @see br.com.appworks.runtime.lang.support.template.TemplateFactory
 * @author Bruno Sofiato
 */

public class DefaultStringficationStrategyFactory implements StringficationStrategyFactory {
  
  /**
   * <p>Associated template factory.</p>
   */
  
  private TemplateFactory templateFactory;

  /**
   * <p>Associated property access strategy factory.</p>
   */

  private PropertyAccessStrategyFactory propertyAccessStrategyFactory;
  
  /**
   * <p>Custom stringfication strategy by type mapping.</p>
   */
  
  private Map <Class, StringficationStrategy> customTypeStringficationStrategyMapping = Collections.EMPTY_MAP;

  /**
   * <p>Constructs a <tt>toString()</tt> based stringfication strategy 
   * reflecting the supplied type stringfication semantics.</p>
   *
   * <p>This method may, based on the supplied type, return three distints kinds
   * of stringfication strategy. If the supplied class is an <tt>array</tt>, the 
   * return stringfication strategy is a instance of <tt>ArrayStringficationStrategy</tt>. 
   * A specialized stringfication strategy may be returned if there is one 
   * mapped to the supplied type. Finally if none of the previous stringfication
   * strategies creation scenarios are applicably, an regular <tt>toString()</tt> 
   * based stringfication strategy is returned.</p>
   *
   * @param  type Type for the <tt>toString()</tt> based stringfication strategy
   *              be constructed 
   * @return <tt>toString()</tt> based comparation strategy reflecting the 
   *         supplied type stringfication semantics
   * @see ArrayStringficationStrategy
   * @see ToStringStringficationStrategy
   */
  
  private StringficationStrategy createToStringStringficationStrategy(final Class type) {
    try {
      if (getCustomTypeStringficationStrategyMapping().containsKey(type)) {
        return (StringficationStrategy) (getCustomTypeStringficationStrategyMapping().get(type).clone());
      } else if (type.isArray()) {
        return new ArrayStringficationStrategy();
      } else {
        return new ToStringStringficationStrategy();
      }
    } catch (CloneNotSupportedException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * <p>Creates a stringfication strategy for a given stringfication policy and 
   * type.</p>
   *
   * <p>If the supplied policy is the <tt>identity</tt> policy, then the 
   * returned stringfication strategy is an instance of <tt>IdentityStringficationStrategy</tt>,
   * However, if the supplied policy is the <tt>toString</tt> policy, then the 
   * stringfication strategy creation process is delegated to the 
   * <tt>createToStringStringficationStrategy</tt> method.</p>
   * 
   * @param  policy Stringfication policy
   * @param  type Type for the stringfication strategy be constructed
   * @return Stringfication strategy reflecting the supplied type stringfication 
   *         semantics
   *
   * @see IdentityStringficationStrategy
   */
  
  private StringficationStrategy createStringficationStrategy(final Class type, 
                                                              final StringficationPolicy policy) {
    switch (policy) {
      case IDENTITY :  
        return new IdentityStringficationStrategy();
      default :  
        return createToStringStringficationStrategy(type);
    }
  }
  
  /**
   * <p>Creates a new stringfication strategy implementing the stringfication
   * semantics from the supplied <tt>@Stringficable</tt> annotation.</p>
   *
   * @param  type Stringficable type of the soon to be created stringfication
   *              strategy
   * @param  stringficableAnnotation Annotation representing the stringfication
   *                                 semantics to be implemented
   * @param  <Type> Parametrized type for the stringfication strategy be 
   *                constructed
   * @return Stringfication strategy implementing the supplied stringfication 
   *         semantics
   * @throws InstantiationException If there's an error on the custom 
   *                                stringfication strategy instantiation
   * @throws IllegalAccessException If the custom stringfication strategy has a 
   *                                <tt>private</tt> default constructor
   */
  private <Type> StringficationStrategy <Type> createNewStringficationStrategy(final Class <Type> type, 
                                                                               final Stringficable stringficableAnnotation) throws InstantiationException, IllegalAccessException {
    if (!(StringficationStrategy.class.equals(stringficableAnnotation.strategy()))) {
      return stringficableAnnotation.strategy().newInstance();
    } else {
      return createStringficationStrategy(type, stringficableAnnotation.value());
    }
  }
  
  /**
   * <p>Creates a new stringfication strategy implementing the supplied class 
   * stringfication semantics.</p>
   *
   * <p>To be elegibly to a stringfication strategy, at least one property from 
   * the given type, or the type itself, must be annotated with the <tt>@Stringficable</tt> 
   * annotation. If there is a custom stringfication strategy supplied on this 
   * annotation, an instance from this custom strategy is returned, if there's 
   * an template associated with this annotation, an strategy reflecting this 
   * template semantics is returned.</p>
   *
   * <p>However, if there isn't a supplied custom stringfication strategy or 
   * template, a specialized stringfication strategy is constructed by 
   * processing all properties from the supplied class and creating property 
   * based stringfication strategies for all annotated properties from the given class.</p>
   *
   * @param  klass Type for the stringfication strategy be constructed 
   * @param  <Type> Parametrized type for the stringfication strategy be 
   *                constructed
   * @return Stringfication strategy reflecting the supplied type stringfication
   *         semantics, or <tt>null</tt> if the supplied type is not elegibly of
   *         a stringfication strategy
   */
  
  private <Type> StringficationStrategy <Type> createNewStringficationStrategy(final Class <Type> klass) {
    try {
      Collection <StringficationStrategy <Type>> delegatedStringficationStrategies = new ArrayList <StringficationStrategy <Type>>();
      for (Field field : ClassUtils.getDeclaredFields(klass)) {
        Stringficable stringficableAnnotation = field.getAnnotation(Stringficable.class);
        if (stringficableAnnotation != null) {
          PropertyGettingStrategy <Type, Object> propertyAccessStrategy = getPropertyAccessStrategyFactory().create(field, stringficableAnnotation.getter(), null);
          StringficationStrategy stringficationStrategy = createNewStringficationStrategy(field.getType(), stringficableAnnotation);
          delegatedStringficationStrategies.add(new PropertyStringficationStrategy(propertyAccessStrategy, stringficationStrategy));
        }
      }
      for (Method method : ClassUtils.getDeclaredMethods(klass)) {
        Stringficable stringficableAnnotation = ClassUtils.getInheritedAnnotation(klass, method, Stringficable.class);
        if (stringficableAnnotation != null) {
          PropertyGettingStrategy <Type, Object> propertyAccessStrategy = getPropertyAccessStrategyFactory().create(method, null);
          StringficationStrategy stringficationStrategy = createNewStringficationStrategy(method.getReturnType(), stringficableAnnotation);
          delegatedStringficationStrategies.add(new PropertyStringficationStrategy(propertyAccessStrategy, stringficationStrategy));
        }
      }
      return new StringficationHandler<Type>(delegatedStringficationStrategies);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    } catch (InstantiationException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * <p>Checks if a supplied class is elegible to have a stringfication 
   * strategy, based by it's methods and fields.</p>
   * 
   * <p>To be elegibly to a stringfication strategy, at least one property or 
   * method of the given type must be annotated with the <tt>@Stringficable</tt>
   * annotation.</p>
   *
   * @param  klass Class to check for stringfication strategy elegibility
   * @return <tt>true</tt> if the supplied class is elegible to have a 
   *         stringfication strategy, <tt>false</tt> otherwise
   */
  private boolean isElegible(final Class klass) {
    for (Field field : klass.getDeclaredFields()) {
      if (field.getAnnotation(Stringficable.class) != null) {
        return true;
      }
    }
    for (Method method : ClassUtils.getDeclaredMethods(klass)) {
      if (ClassUtils.getInheritedAnnotation(klass, method, Stringficable.class) != null) {
        return true;
      }
    }
    return false;
  }

  /**
   * <p>Constructs a new stringfication strategy factory implementation.</p>
   *
   * @param templateFactory Associated template factory
   * @param propertyAccessStrategyFactory Associated property access strategy 
   *                                      factory
   */

  public DefaultStringficationStrategyFactory(final TemplateFactory templateFactory, 
                                              final PropertyAccessStrategyFactory propertyAccessStrategyFactory) {
    this(templateFactory, propertyAccessStrategyFactory, null);
  }
 
  /**
   * <p>Constructs a new stringfication strategy factory implementation.</p>
   *
   * @param templateFactory Associated template factory
   * @param propertyAccessStrategyFactory Associated property access strategy 
   *                                      factory
   * @param customTypeStringficationStrategyMapping Custom stringfication 
   *                                                strategy by type mapping
   */

  public DefaultStringficationStrategyFactory(final TemplateFactory templateFactory, 
                                              final PropertyAccessStrategyFactory propertyAccessStrategyFactory, 
                                              final Map <Class, StringficationStrategy> customTypeStringficationStrategyMapping) {
    setTemplateFactory(templateFactory);
    setPropertyAccessStrategyFactory(propertyAccessStrategyFactory);
    setCustomTypeStringficationStrategyMapping(customTypeStringficationStrategyMapping);
  }
  
  /**
   * <p>Constructs a stringfication strategy reflecting the supplied type 
   * stringfication semantics.</p>
   *
   * @param  type Type for the stringfication strategy be constructed
   * @param  <Type> Parametrized type for the stringfication strategy be 
   *                constructed
   * @return Stringfication strategy reflecting the supplied type stringfication 
   *         semantics
   */
  
  public <Type> StringficationStrategy <Type> create(final Class <Type> type) {
    try {
      Stringficable stringficable = ClassUtils.getInheritedAnnotation(type, Stringficable.class);
      if (stringficable != null) {
        if (!(StringficationStrategy.class.equals(stringficable.strategy()))) {
          return stringficable.strategy().newInstance();
        } else if (!"".equals(stringficable.template())) {
          return new TemplateStringficationStrategy(getTemplateFactory().create(stringficable.template()));
        } else if (isElegible(type)) {
          return createNewStringficationStrategy(type);
        } else if (type.getSuperclass() != null) {
          return (StringficationStrategy) (create(type.getSuperclass()));
        } 
      }
      return null;
    } catch (InstantiationException ex) {
      throw new RuntimeException(ex);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    } catch (TemplateCompilationException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  /**
   * <p>Modify the associated property access strategy factory.</p>
   *
   * @param propertyAccessStrategyFactory Associated property access strategy 
   *                                      factory
   */
  
  public void setPropertyAccessStrategyFactory(final PropertyAccessStrategyFactory propertyAccessStrategyFactory) {
    this.propertyAccessStrategyFactory = propertyAccessStrategyFactory;
  }

  /**
   * <p>Obtain the associated property access strategy factory.</p>
   *
   * @return Associated property access strategy factory
   */

  public PropertyAccessStrategyFactory getPropertyAccessStrategyFactory() {
    return propertyAccessStrategyFactory;
  }

  /**
   * <p>Modify the associated template factory.</p>
   *
   * @param templateFactory Associated template factory
   */

  public void setTemplateFactory(final TemplateFactory templateFactory) {
    this.templateFactory = templateFactory;
  }

  /**
   * <p>Obtain the associated template factory.</p>
   *
   * @return Associated template factory
   */

  public TemplateFactory getTemplateFactory() {
    return templateFactory;
  }

  /**
   * <p>Modify the custom stringfication strategy by type mapping.</p>
   *
   * @param customTypeStringficationStrategyMapping Custom stringfication 
   *                                                strategy by type mapping
   */

  public void setCustomTypeStringficationStrategyMapping(final Map <Class, StringficationStrategy> customTypeStringficationStrategyMapping) {
    if (customTypeStringficationStrategyMapping != null) {
      this.customTypeStringficationStrategyMapping = customTypeStringficationStrategyMapping;
    } else {
      this.customTypeStringficationStrategyMapping = Collections.EMPTY_MAP;
    } 
  }
  
  /**
   * <p>Obtain the custom stringfication strategy by type mapping.</p>
   *
   * @return Custom stringfication strategy by type mapping
   */
  public Map <Class, StringficationStrategy> getCustomTypeStringficationStrategyMapping() {
    return customTypeStringficationStrategyMapping;
  }


}
