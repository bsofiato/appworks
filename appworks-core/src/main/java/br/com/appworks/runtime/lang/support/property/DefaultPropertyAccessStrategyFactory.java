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

import java.lang.reflect.Field;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategy;
import br.com.appworks.runtime.lang.support.property.getting.PropertyGettingStrategyFactory;
import br.com.appworks.runtime.lang.support.property.setting.PropertySettingStrategy;
import br.com.appworks.runtime.lang.support.property.setting.PropertySettingStrategyFactory;
import br.com.appworks.runtime.lang.support.reflect.ClassUtils;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * <p>Property access strategy factory implementation.</p>
 * 
 * <p>Based on the associated property getting strategy, and the associated 
 * property setting strategy, creates property access strategy to supplied 
 * classes properties.</p>
 *
 * @author Bruno Sofiato
 */

public class DefaultPropertyAccessStrategyFactory implements PropertyAccessStrategyFactory {
  
  /**
   * <p>Property access strategy creation request.</p>
   * 
   * <p>The property access strategy creation request is used to identify a 
   * property access strategy on the property access strategy cache.</p>
   * 
   * @author Bruno Sofiato
   */
  public static interface Request {
  }
  
  /**
   * <p>Field based property access strategy creation request.</p>
   *
   * <p>This request stores a field based property access strategy request data
   * (the source field and the overrrided getter and setter). This request is
   * used to retreive from the cache property access strategies created by a 
   * field based request.</p>
   *
   * @author Bruno Sofiato
   */
  private static final class FieldBasedRequest implements Request {
    
    /**
     * <p>Associated field.</p>
     */
    
    private final Field field;

    /**
     * <p>Associated overrided getter.</p>
     */
    
    private final String overridedGetter;

    /**
     * <p>Associated overrided setter.</p>
     */

    private final String overridedSetter;
    
    /**
     * <p>Gets the associated field.</p>
     *
     * @return Associated field
     */

    private Field getField() {
      return field;
    }

    /**
     * <p>Gets the associated overrided getter.</p>
     *
     * @return Associated overrided getter
     */

    private String getOverridedGetter() {
      return overridedGetter;
    }

    /**
     * <p>Gets the associated overrided setter.</p>
     *
     * @return Associated overrided setter
     */

    private String getOverridedSetter() {
      return overridedSetter;
    }
    
    /**
     * <p>Constructs a field based property access strategy creation 
     * request.</p>
     *
     * @param field Associated field
     * @param overridedGetter Associated overrided getter
     * @param overridedSetter Associated overrided setter
     */
    private FieldBasedRequest(final Field field, 
                              final String overridedGetter, 
                              final String overridedSetter) {
      this.field = field;
      this.overridedGetter = overridedGetter;
      this.overridedSetter = overridedSetter;
    }
    
    /**
     * <p>Calculates the field based property access strategy creation request
     * hash code.</p>
     *
     * <p>The field based property access strategy creation request hash code
     * value is based on the associated field's, overrided getter's and 
     * overrided setter's hash code value.</p>
     *
     * @return Field based property access strategy creation request hash code
     */
    
    public int hashCode() {
      int hashCode = (getField() == null) ? 0 : getField().hashCode();
      hashCode += (getOverridedGetter() == null) ? 0 : getOverridedGetter().hashCode();
      return hashCode + ((getOverridedSetter() == null) ? 0 : getOverridedSetter().hashCode());
    }
    
    /**
     * <p>Compare the field based property access strategy creation request
     * with an supplied instance for equality.</p>
     * 
     * <p>To be considered equals to a field based property access strategy 
     * creation request instance, the supplied instance must be a method based 
     * property access strategy creation request also, and they's associated 
     * field, overrided getter and setter must be considered equivalent.</p>
     *
     * @param  o Instance to be compared for equality
     * @return <tt>true</tt> if the given instance is considered to be equals to
     *         the field based property access strategy creation request, 
     *         <tt>false</tt> otherwise
     */
    
    public boolean equals(final Object o) {
      if (o instanceof FieldBasedRequest) {
        FieldBasedRequest operand = (FieldBasedRequest) o;
        if (!getField().equals(operand.getField())) {
          return false;
        }
        if (getOverridedGetter() == null) {
          if (operand.getOverridedGetter() != null) {
            return false;
          }
        } else if (!getOverridedGetter().equals(operand.getOverridedGetter())) {
          return false;
        }
        if (getOverridedSetter() == null) {
          if (operand.getOverridedSetter() != null) {
            return false;
          } 
        } else {
          return getOverridedSetter().equals(operand.getOverridedSetter());
        }
        return true;
      }
      return false;
    }
  }
  
  /**
   * <p>Method based property access strategy creation request.</p>
   *
   * <p>This request stores a method based property access strategy request data
   * (the getter method and the setter method). This request is used to retreive
   * from the cache property access strategies created by a method based 
   * request.</p>
   *
   * @author Bruno Sofiato
   */
  
  private static final class MethodBasedRequest implements Request {
    
    /**
     * <p>Associated getter method.</p>
     */
    
    private final Method getter;

    /**
     * <p>Associated setter method.</p>
     */

    private final Method setter;
    
    /**
     * <p>Gets the associated getter method.</p>
     * 
     * @return Associated getter method
     */

    private Method getGetter() {
      return getter;
    }

    /**
     * <p>Gets the associated setter method.</p>
     * 
     * @return Associated setter method
     */

    private Method getSetter() {
      return setter;
    }
    
    /**
     * <p>Constructs a method based property access strategy creation 
     * request.</p>
     *
     * @param getter Associated getter method
     * @param setter Associated setter method
     */
    
    private MethodBasedRequest(final Method getter, final Method setter) {
      this.getter = getter;
      this.setter = setter;
    }

    /**
     * <p>Calculates the method based property access strategy creation request
     * hash code.</p>
     *
     * <p>The method based property access strategy creation request hash code
     * value is based on the associated getter method's and the associated 
     * setter method's hash code value.</p>
     *
     * @return Method based property access strategy creation request hash code
     */

    public int hashCode() {
      int hashCode = (getGetter() == null) ? 0 : getGetter().hashCode();
      return hashCode + ((getSetter() == null) ? 0 : getSetter().hashCode());
    }

    /**
     * <p>Compare the method based property access strategy creation request
     * with an supplied instance for equality.</p>
     * 
     * <p>To be considered equals to a method based property access strategy 
     * creation request instance, the supplied instance must be a method based 
     * property access strategy creation request also, and they's associated 
     * getter and setter method must be considered equivalent.</p>
     *
     * @param  o Instance to be compared for equality
     * @return <tt>true</tt> if the given instance is considered to be equals to
     *         the method based property access strategy creation request, 
     *         <tt>false</tt> otherwise
     */

    public boolean equals(final Object o) {
      if (o instanceof MethodBasedRequest) {
        MethodBasedRequest operand = (MethodBasedRequest) o;
        if (getGetter() != operand.getGetter()) {
          if (getGetter() == null) {
            return false;
          } else if (!getGetter().equals(operand.getGetter())) {
            return false;
          }
        }
        if (getSetter() != operand.getSetter()) {
          if (getSetter() == null) {
            return false;
          } 
          return getSetter().equals(operand.getSetter());
        }
        return true;
      }
      return false;
    }
  }
  
  /**
   * <p>Cached property access strategies strategies.</p>
   */
  
  private final Map <Request, PropertyAccessStrategy> propertyAccessStrategyCache = new WeakHashMap<Request, PropertyAccessStrategy>();
  
  /**
   * <p>Associated property getting strategy factory.</p>
   */
  
  private PropertyGettingStrategyFactory propertyGettingStrategyFactory;
  
  /**
   * <p>Associated property setting strategy factory.</p>
   */
  
  private PropertySettingStrategyFactory propertySettingStrategyFactory;

  /**
   * <p>Gets the cached property access strategies strategies.</p>
   *
   * @return Cached property access strategies strategies
   */

  private Map <Request, PropertyAccessStrategy> getPropertyAccessStrategyCache() {
    return propertyAccessStrategyCache;
  }
  
  /**
   * <p>Constructs the property access strategy factory implementation.</p>
   *
   * @param propertyGettingStrategyFactory Associated property getting strategy 
   *                                       factory
   * @param propertySettingStrategyFactory Associated property setting strategy 
   *                                       factory
   */
  
  public DefaultPropertyAccessStrategyFactory(final PropertyGettingStrategyFactory propertyGettingStrategyFactory, 
                                                     final PropertySettingStrategyFactory propertySettingStrategyFactory) {
    setPropertyGettingStrategyFactory(propertyGettingStrategyFactory);
    setPropertySettingStrategyFactory(propertySettingStrategyFactory);
  }
  
  /**
   * <p>Constructs a property access strategy reflecting the supplied property
   * access semantics.</p>
   * 
   * <p>This method delegates the property getting strategy creation and the 
   * property setting creation request to the associated property getting 
   * strategy factory and property setting strategy factory, and aggregates the 
   * obtained strategies on a <tt>DefaultPropertyAccessStrategy</tt> 
   * instance.</p>
   * 
   * <p>All created property access strategies are cached to maximize runtime 
   * performance (the whole creation process would be too crubersome to be 
   * executed on all property accessc strategy requests). If there is no cached 
   * property access strategy mapped to this type, the creation process will be 
   * carried over.</p>
   * 
   * @param  field Class field associated with the property
   * @param  getter Getter method name (optional)
   * @param  setter Setter method name (optional)
   * @param <AccessedType> Parametrized property owner type for the property 
   *                       access strategy
   * @param <PropertyType> Parametrized property type for the property access
   *                       strategy
   * @return Property access strategy reflecting the supplied property access 
   *         semantics
   * @see DefaultPropertyAccessStrategy
   */

  public <AccessedType, PropertyType> PropertyAccessStrategy<AccessedType, PropertyType> create(final Field field, 
                                                                                                final String getter, 
                                                                                                final String setter) {
    Request request = new FieldBasedRequest(field, getter, setter);
    PropertyAccessStrategy propertyAccessStrategy = getPropertyAccessStrategyCache().get(request);
    if (propertyAccessStrategy == null) {
      PropertyGettingStrategy <AccessedType, PropertyType> gettingStrategy = getPropertyGettingStrategyFactory().create(field, getter);
      PropertySettingStrategy <AccessedType, PropertyType> settingStrategy = getPropertySettingStrategyFactory().create(field, setter);
      propertyAccessStrategy = new DefaultPropertyAccessStrategy<AccessedType, PropertyType>(gettingStrategy, settingStrategy);
      getPropertyAccessStrategyCache().put(request, propertyAccessStrategy);
    }
    return propertyAccessStrategy;
  }
  

  /**
   * <p>Constructs a property access strategy reflecting the supplied property
   * access semantics.</p>
   * 
   * <p>This method delegates the property getting strategy creation and the 
   * property setting creation request to the associated property getting 
   * strategy factory and property setting strategy factory, and aggregates the 
   * obtained strategies on a <tt>DefaultPropertyAccessStrategy</tt> 
   * instance.</p>
   * 
   * <p>All created property access strategies are cached to maximize runtime 
   * performance (the whole creation process would be too crubersome to be 
   * executed on all property accessc strategy requests). If there is no cached 
   * property access strategy mapped to this type, the creation process will be 
   * carried over.</p>
   * 
   * @param  getter Getter method (optional)
   * @param  setter Setter method (optional)
   * @param <AccessedType> Parametrized property owner type for the property 
   *                       access strategy
   * @param <PropertyType> Parametrized property type for the property access
   *                       strategy
   * @return Property access strategy reflecting the supplied property access 
   *         semantics
   * @see DefaultPropertyAccessStrategy
   */

  public <AccessedType, PropertyType> PropertyAccessStrategy<AccessedType, PropertyType> create(final Method getter, final Method setter) {
    Request request = new MethodBasedRequest(getter, setter);
    PropertyAccessStrategy propertyAccessStrategy = getPropertyAccessStrategyCache().get(request);
    if (propertyAccessStrategy == null) {
      PropertyGettingStrategy <AccessedType, PropertyType> gettingStrategy = getPropertyGettingStrategyFactory().create(getter);
      PropertySettingStrategy <AccessedType, PropertyType> settingStrategy = getPropertySettingStrategyFactory().create(setter);
      propertyAccessStrategy = new DefaultPropertyAccessStrategy<AccessedType, PropertyType>(gettingStrategy, settingStrategy);
      getPropertyAccessStrategyCache().put(request, propertyAccessStrategy);
    } 
    return propertyAccessStrategy;
  }

  /**
   * <p>Constructs a property access strategy reflecting the supplied property
   * access semantics.</p>
   *
   * @param  property The property name
   * @param  type The accessed type
   * @param <AccessedType> Parametrized property owner type for the property 
   *                       access strategy
   * @param <PropertyType> Parametrized property type for the property access
   *                       strategy
   * @return Property access strategy reflecting the supplied property access 
   *         semantics
   * @throws IllegalArgumentException If the supplied type doesn't have the 
   *                                  supplied property
   */
  
  public <AccessedType, PropertyType> PropertyAccessStrategy<AccessedType, PropertyType> create(final Class<AccessedType> type, final String property) {
    try {
      BeanInfo info = Introspector.getBeanInfo(type);
      for (PropertyDescriptor propertyDescriptor : info.getPropertyDescriptors()) {
        if (propertyDescriptor.getName().equals(property)) {
          return create(propertyDescriptor.getReadMethod(), propertyDescriptor.getWriteMethod());
        }
      }
      return create(ClassUtils.getDeclaredField(type, property), null, null);
    } catch (IntrospectionException ex) {
      throw new RuntimeException(ex);
    } catch (NoSuchFieldError ex) {
      throw new IllegalArgumentException("There isn't " + property + " property on the " + type.getName() + " type");
    }
  }
  
  
  
  /**
   * <p>Gets the associated property getting strategy factory.</p>
   *
   * @return Associated property getting strategy factory
   */

  public PropertyGettingStrategyFactory getPropertyGettingStrategyFactory() {
    return propertyGettingStrategyFactory;
  }

  /**
   * <p>Gets the associated property setting strategy factory.</p>
   *
   * @return Associated property setting strategy factory
   */

  public PropertySettingStrategyFactory getPropertySettingStrategyFactory() {
    return propertySettingStrategyFactory;
  }
  
  /**
   * <p>Modify the associated property getting strategy factory.</p>
   *
   * @param propertyGettingStrategyFactory Associated property getting strategy factory
   */

  public void setPropertyGettingStrategyFactory(final PropertyGettingStrategyFactory propertyGettingStrategyFactory) {
    this.propertyGettingStrategyFactory = propertyGettingStrategyFactory;
  }

  /**
   * <p>Modify the associated property setting strategy factory.</p>
   *
   * @param propertySettingStrategyFactory Associated property setting strategy factory
   */

  public void setPropertySettingStrategyFactory(final PropertySettingStrategyFactory propertySettingStrategyFactory) {
    this.propertySettingStrategyFactory = propertySettingStrategyFactory;
  }
  
  

}
