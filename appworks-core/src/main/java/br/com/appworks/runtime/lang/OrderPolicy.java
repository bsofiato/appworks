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

package br.com.appworks.runtime.lang;

/**
 * <p>Natural order policies.</p>
 *
 * <p>A natural order policy dictates how the natural order must be calculated 
 * on the comparation process. For more information on how these policies 
 * relates, see their documentation below.</p>
 * 
 * @author Bruno Sofiato
 */

public enum OrderPolicy {
  /**
   * <p>Natural order policy.</p>
   *
   * <p>The natural order policy dictates that the natural ordering must be done
   * from the <tt>lesser to the greater</tt>, as defined on the natural order 
   * implementation.</p>
   */
  NATURAL(1),

  /**
   * <p>Inverse natural order policy.</p>
   *
   * <p>Inverse natural order policy dictates that the natural ordering must be
   * inversed. It must be done from the <tt>greater to the lesser</tt>, as opose
   * as defined on the natural order implementation.</p>
   */

  INVERSE(-1);
  
  /**
   * <p>The natural order multiplier.</p>
   *
   * <p>The natural order multiplier is used to adjust the comparation result to
   * comply with the natural order policy.</p>
   */
  private final int multiplier;
  
  /**
   * <p>Gets the natural order multiplier.</p>
   *
   * @return The natural order multiplier
   */
  
  private int getMultiplier() {
    return multiplier;
  }
  
  /**
   * <p>Creates a natural order policy.</p>
   *
   * @param  multiplier The natural order multiplier
   */
  private OrderPolicy(final int multiplier) {
    this.multiplier = multiplier;
  }
  
  /**
   * <p>Adjusts a unadjusted comparation result.</p>
   *
   * @param  comparationResult Unadjusted comparation result
   * @return Adjusted comparation result
   */

  public int adjustComparationResult(final int comparationResult) {
    return comparationResult * getMultiplier();
  }
}
