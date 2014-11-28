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
import java.util.Map;
import java.util.WeakHashMap;

/**
 * <p>Cache cloning strategy factory adapter.</p>
 * 
 * <p>All cloning strategies created by this factory adapted factory are cached 
 * to maximize runtime performance (the whole metadata reading/cloning strategy 
 * creation process would be too crubersome to be executed on all cloning 
 * strategy requests).</p>
 * 
 * @author Bruno Sofiato
 */

public class CachingCloningStrategyFactoryAdapter implements CloningStrategyFactory {
  
  /**
   * <p>Adapted cloning strategy factory.</p>
   */
  
  private CloningStrategyFactory adaptedCloningStrategyFactory;

  /**
   * <p>Cached cloning strategies.</p>
   */
  
  private final Map <Class, CloningStrategy <? extends Object>> classCloningStrategyCache = new WeakHashMap<Class, CloningStrategy <? extends Object>>();

  /**
   * <p>Gets the cached cloning strategies.</p>
   *
   * @return Cached cloning strategies
   */
  
  private Map <Class, CloningStrategy <? extends Object>> getClassCloningStrategyCache() {
    return classCloningStrategyCache;
  }

  /**
   * <p>Constructs a new cloning strategy factory adapter.</p>
   *
   * @param adaptedCloningStrategyFactory Adapted cloning strategy factory
   */

  public CachingCloningStrategyFactoryAdapter(final CloningStrategyFactory adaptedCloningStrategyFactory) {
    setAdaptedCloningStrategyFactory(adaptedCloningStrategyFactory);
  }
  
  /**
   * <p>Constructs a cloning strategy reflecting the supplied type cloning 
   * semantics.</p>
   *
   * <p>All created cloning strategies are cached to maximize runtime 
   * performance (the whole metadata reading/cloning strategy creation process 
   * would be too crubersome to be executed on all cloning strategy 
   * requests). If there is no cached cloning strategy mapped to this type, the 
   * creation process will be carried over by the adapted cloning strategy 
   * factory.</p>
   *
   * @param  <Type> Parametrized type for the cloning strategy be constructed
   * @param  klass Type for the cloning strategy be constructed
   * @return Cloning strategy reflecting the supplied type cloning semantics
   */

  public <Type> CloningStrategy <Type> create(final Class <Type> klass) {
    CloningStrategy <Type> cloningStrategy = null;
    if (!getClassCloningStrategyCache().containsKey(klass)) {
      cloningStrategy = getAdaptedCloningStrategyFactory().create(klass);
      getClassCloningStrategyCache().put(klass, cloningStrategy);
    } else {
      cloningStrategy = (CloningStrategy <Type>) (getClassCloningStrategyCache().get(klass));
    }
    return cloningStrategy;
  }
  
  /**
   * <p>Gets the adapted cloning strategy factory.</p>
   *
   * @return Adapted cloning strategy factory
   */
  
  public CloningStrategyFactory getAdaptedCloningStrategyFactory() {
    return adaptedCloningStrategyFactory;
  }
  
  /**
   * <p>Modify the adapted cloning strategy factory.</p>
   *
   * @param adaptedCloningStrategyFactory Adapted cloning strategy factory
   */
  public void setAdaptedCloningStrategyFactory(final CloningStrategyFactory adaptedCloningStrategyFactory) {
    this.adaptedCloningStrategyFactory = adaptedCloningStrategyFactory;
  }
}
