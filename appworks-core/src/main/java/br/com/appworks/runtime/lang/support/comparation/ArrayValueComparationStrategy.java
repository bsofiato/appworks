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

import br.com.appworks.runtime.lang.OrderPolicy;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * <p>Comparation strategy for arrays.</p>
 *
 * <p>The array value comparation strategy is a high specialized value 
 * comparation strategy that deal with arrays based on theirs <tt>deep 
 * content</tt>.</p>
 *
 * @param  <Type> Component type of the array associated with this comparation 
 *                strategy
 * @author Bruno Sofiato
 */

public class ArrayValueComparationStrategy <Type> extends AbstractComparationStrategy <Type> {
  
  /**
   * <p>Associated array component type.</p>
   */
  
  private final Class <? extends Type> componentType;

  /**
   * <p>Gets the associated array compoent type.</p>
   *
   * @return Associated array component type
   */
  
  private Class <? extends Type> getComponentType() {
    return componentType;
  }
  
  /**
   * <p>Compares its two arrays for natural order.</p>
   *
   * <p>Returns a negative integer, zero, or a positive integer as the first 
   * argument is less than, equal to, or greater than the second.</p>
   * 
   * <p>The natural order factor calculation is delegated to the 
   * <tt>compare()</tt> method define in the <tt>AbstractComparationStrategy</tt>
   * if the component type of the supplied arrays are an <tt>Comparable</tt> 
   * instance.</p>
   *
   * @param  op1 First operand
   * @param  op2 Second operand
   * @return A negative integer, zero, or a positive integer as the
   *         first argument is less than, equal to, or greater than the
   *         second
   * @throws ClassCastException If the component type of the supplied array 
   *                            isn't an <tt>Comparable</tt> instance.
   */

  public int compare(final Type op1, final Type op2) {
    if (getComponentType().isPrimitive() || java.lang.Comparable.class.isAssignableFrom(getComponentType())) {
      return super.compare(op1, op2);
    }
    throw new ClassCastException();
  }
 
  /**
   * <p>Compares two arrays.</p>
   *
   * <p>This method implements the array's natural order comparation algorithm. 
   * The evaluation order is defined by the index of the comparable element 
   * on the array (elements with a lower index are processed before elements 
   * with a higher index).</p>
   *
   * @param  op1 First array
   * @param  op2 Second array
   * @return A negative integer, zero, or a positive integer as first operand
   *         is less than, equal to, or greater than the second operand
   */

  protected int doCompare(final Type op1, final Type op2) {
    int l1 = Array.getLength(op1);
    int l2 = Array.getLength(op2);
    if (l1 == l2) {
      for (int i = 0; i < l1; i++) {
        Object e1 = Array.get(op1, i);
        Object e2 = Array.get(op2, i);
        if (e1 == null)  {
          return -1;
        } else if (e2 == null) {
          return 1;
        } else {
          int compareTo = ((java.lang.Comparable) (e1)).compareTo(e2);
          if (compareTo != 0) {
            return compareTo;
          }
        }
      }
    }
    return l1 - l2;
  }
  /**
   * <p>Constructs a new array's comparation strategy.</p>
   *
   * @param componentType Component type of the associated array
   */
  public ArrayValueComparationStrategy(final Class <? extends Type> componentType) {
    this(componentType, OrderPolicy.NATURAL);
  }

  /**
   * <p>Constructs a new array's comparation strategy.</p>
   *
   * @param componentType Component type of the associated array
   * @param policy Natural order policy
   */

  public ArrayValueComparationStrategy(final Class <? extends Type> componentType, 
                                       final OrderPolicy policy) {
    this(componentType, policy, -1);
  }

  /**
   * <p>Constructs a new array's comparation strategy.</p>
   *
   * @param componentType Component type of the associated array
   * @param policy Natural order policy
   * @param index Evaluation order
   */

  public ArrayValueComparationStrategy(final Class <? extends Type> componentType, 
                                       final OrderPolicy policy, 
                                       final int index) {
    this.componentType = componentType;
    setOrderPolicy(policy);
    setIndex(index);
  }

  /**
   * <p>Compares two arrays.</p>
   *
   * <p>This method implements the algorithm to compare the supplied arrays. The
   * implemented algorithm is based on the <tt>deep contents</tt>
   * of the array.</p>
   *
   * <p>For more info on the comparation algorithm, see 
   * <tt>Arrays.deepEquals()</tt> method.</p>
   *
   * @param  op1 First array
   * @param  op2 Second array
   * @return <tt>true</tt> if the supplied arrays are considered equivalent
   *         <tt>false</tt> otherwise
   */

  public boolean equals(final Type op1, final Type op2) {
    if (getComponentType().isPrimitive()) {
      if (getComponentType() == Boolean.TYPE) {
        return Arrays.equals((boolean [])(op1), (boolean [])(op2));
      } else if (getComponentType() == Byte.TYPE) {
        return Arrays.equals((byte [])(op1), (byte [])(op2));
      } else if (getComponentType() == Character.TYPE) {
        return Arrays.equals((char [])(op1), (char [])(op2));
      } else if (getComponentType() == Double.TYPE) {
        return Arrays.equals((double [])(op1), (double [])(op2));
      } else if (getComponentType() == Float.TYPE) {
        return Arrays.equals((float [])(op1), (float [])(op2));
      } else if (getComponentType() == Integer.TYPE) {
        return Arrays.equals((int [])(op1), (int [])(op2));
      } else if (getComponentType() == Long.TYPE) {
        return Arrays.equals((long [])(op1), (long [])(op2));
      } else if (getComponentType() == Short.TYPE) {
        return Arrays.equals((short [])(op1), (short [])(op2));
      }
    } 
    return Arrays.deepEquals((Object [])(op1), (Object [])(op2));
  }

  /**
   * <p>Calculates the array hash code.</p>
   *
   * <p>This method implements the algorithm to calculate the supplied array 
   * hash code. The implemented algorithm is based on the <tt>deep contents</tt>
   * of the array.</p>
   *
   * <p>For more info on the hash code calculation algorithm, see 
   * <tt>Arrays.deepHashCode()</tt> method.</p>
   *
   * @param  object Supplied array
   * @return The supplied array hash code
   */

  public int hashCode(final Type object) {
    if (getComponentType().isPrimitive()) {
       if (getComponentType() == Boolean.TYPE) {
        return Arrays.hashCode((boolean [])(object));
      } else if (getComponentType() == Byte.TYPE) {
        return Arrays.hashCode((byte [])(object));
      } else if (getComponentType() == Character.TYPE) {
        return Arrays.hashCode((char [])(object));
      } else if (getComponentType() == Double.TYPE) {
        return Arrays.hashCode((double [])(object));
      } else if (getComponentType() == Float.TYPE) {
        return Arrays.hashCode((float [])(object));
      } else if (getComponentType() == Integer.TYPE) {
        return Arrays.hashCode((int[])(object));
      } else if (getComponentType() == Long.TYPE) {
        return Arrays.hashCode((long[])(object));
      } else if (getComponentType() == Short.TYPE) {
        return Arrays.hashCode((short[])(object));
      }
    } 
    return Arrays.deepHashCode((Object [])(object));
  }
}