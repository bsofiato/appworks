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
import java.util.Map;
import java.util.WeakHashMap;

/**
 * <p>Cache comparation strategy factory adapter.</p>
 * 
 * <p>All comparation strategies created by this factory adapted factory are 
 * cached to maximize runtime performance (the whole metadata reading/comparation 
 * strategy creation process would be too crubersome to be executed on all 
 * comparation strategy requests).</p>
 * 
 * @author Bruno Sofiato
 */

public class CachingComparationStrategyFactoryAdapter implements ComparationStrategyFactory {
  
  /**
   * <p>Adapted comparation strategy factory.</p>
   */
  
  private ComparationStrategyFactory adaptedComparationStrategyFactory;

  /**
   * <p>Cached comparation strategies.</p>
   */
  
  private final Map <Class, ComparationStrategy <? extends Object>> classComparationStrategyCache = new WeakHashMap<Class, ComparationStrategy <? extends Object>>();

  /**
   * <p>Gets the cached comparation strategies.</p>
   *
   * @return Cached comparation strategies
   */
  
  private Map <Class, ComparationStrategy <? extends Object>> getClassComparationStrategyCache() {
    return classComparationStrategyCache;
  }

  /**
   * <p>Constructs a new comparation strategy factory adapter.</p>
   *
   * @param adaptedComparationStrategyFactory Adapted comparation strategy factory
   */

  public CachingComparationStrategyFactoryAdapter(final ComparationStrategyFactory adaptedComparationStrategyFactory) {
    setAdaptedComparationStrategyFactory(adaptedComparationStrategyFactory);
  }
  
  /**
   * <p>Constructs a comparation strategy reflecting the supplied type 
   * comparation semantics.</p>
   *
   * <p>All created comparation strategies are cached to maximize runtime 
   * performance (the whole metadata reading/comparation strategy creation 
   * process would be too crubersome to be executed on all comparation strategy 
   * requests). If there is no cached comparation strategy mapped to this type, 
   * the creation process will be carried over by the adapted comparation 
   * strategy factory.</p>
   *
   * @param  <Type> Parametrized type for the comparation strategy be 
   *                constructed
   * @param  klass Type for the comparation strategy be constructed
   * @return Comparation strategy reflecting the supplied type comparation 
   *         semantics
   */

  public <Type> ComparationStrategy <Type> create(final Class <Type> klass) {
    ComparationStrategy <Type> comparationStrategy = null;
    if (!getClassComparationStrategyCache().containsKey(klass)) {
      comparationStrategy = getAdaptedComparationStrategyFactory().create(klass);
      getClassComparationStrategyCache().put(klass, comparationStrategy);
    } else {
      comparationStrategy = (ComparationStrategy <Type>) (getClassComparationStrategyCache().get(klass));
    }
    return comparationStrategy;
  }
  
  /**
   * <p>Gets the adapted comparation strategy factory.</p>
   *
   * @return Adapted comparation strategy factory
   */
  
  public ComparationStrategyFactory getAdaptedComparationStrategyFactory() {
    return adaptedComparationStrategyFactory;
  }
  
  /**
   * <p>Modify the adapted comparation strategy factory.</p>
   *
   * @param adaptedComparationStrategyFactory Adapted comparation strategy factory
   */
  public void setAdaptedComparationStrategyFactory(final ComparationStrategyFactory adaptedComparationStrategyFactory) {
    this.adaptedComparationStrategyFactory = adaptedComparationStrategyFactory;
  }
}

