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
import java.util.Map;
import java.util.WeakHashMap;

/**
 * <p>Cache stringfication strategy factory adapter.</p>
 * 
 * <p>All stringfication strategies created by this factory adapted factory are
 * cached to maximize runtime performance (the whole metadata reading/stringfication
 * strategy creation process would be too crubersome to be executed on all 
 * stringfication strategy requests).</p>
 * 
 * @author Bruno Sofiato
 */

public class CachingStringficationStrategyFactoryAdapter implements StringficationStrategyFactory {
  
  /**
   * <p>Adapted stringfication strategy factory.</p>
   */
  
  private StringficationStrategyFactory adaptedStringficationStrategyFactory;

  /**
   * <p>Cached stringfication strategies.</p>
   */
  
  private final Map <Class, StringficationStrategy <? extends Object>> classStringficationStrategyCache = new WeakHashMap<Class, StringficationStrategy <? extends Object>>();

  /**
   * <p>Gets the cached stringfication strategies.</p>
   *
   * @return Cached stringfication strategies
   */
  
  private Map <Class, StringficationStrategy <? extends Object>> getClassStringficationStrategyCache() {
    return classStringficationStrategyCache;
  }

  /**
   * <p>Constructs a new stringfication strategy factory adapter.</p>
   *
   * @param adaptedStringficationStrategyFactory Adapted stringfication strategy 
   *                                             factory
   */

  public CachingStringficationStrategyFactoryAdapter(final StringficationStrategyFactory adaptedStringficationStrategyFactory) {
    setAdaptedStringficationStrategyFactory(adaptedStringficationStrategyFactory);
  }
  
  /**
   * <p>Constructs a stringfication strategy reflecting the supplied type 
   * stringfication semantics.</p>
   *
   * <p>All created stringfication strategies are cached to maximize runtime 
   * performance (the whole metadata reading/stringfication strategy creation 
   * process would be too crubersome to be executed on all stringfication 
   * strategy requests). If there is no cached stringfication strategy mapped to
   * this type, the creation process will be carried over by the adapted 
   * stringfication strategy factory.</p>
   *
   * @param  <Type> Parametrized type for the stringfication strategy be 
   *                constructed
   * @param  klass Type for the stringfication strategy be constructed
   * @return Stringfication strategy reflecting the supplied type stringfication 
   *         semantics
   */

  public <Type> StringficationStrategy <Type> create(final Class <Type> klass) {
    StringficationStrategy <Type> stringficationStrategy = null;
    if (!getClassStringficationStrategyCache().containsKey(klass)) {
      stringficationStrategy = getAdaptedStringficationStrategyFactory().create(klass);
      getClassStringficationStrategyCache().put(klass, stringficationStrategy);
    } else {
      stringficationStrategy = (StringficationStrategy <Type>) (getClassStringficationStrategyCache().get(klass));
    }
    return stringficationStrategy;
  }
  
  /**
   * <p>Gets the adapted stringfication strategy factory.</p>
   *
   * @return Adapted stringfication strategy factory
   */
  
  public StringficationStrategyFactory getAdaptedStringficationStrategyFactory() {
    return adaptedStringficationStrategyFactory;
  }
  
  /**
   * <p>Modify the adapted stringfication strategy factory.</p>
   *
   * @param adaptedStringficationStrategyFactory Adapted stringfication strategy 
   *                                             factory
   */
  public void setAdaptedStringficationStrategyFactory(final StringficationStrategyFactory adaptedStringficationStrategyFactory) {
    this.adaptedStringficationStrategyFactory = adaptedStringficationStrategyFactory;
  }
}

