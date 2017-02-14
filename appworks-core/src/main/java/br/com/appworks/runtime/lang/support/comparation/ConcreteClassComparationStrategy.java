package br.com.appworks.runtime.lang.support.comparation;

import br.com.appworks.runtime.lang.OrderPolicy;
import java.util.Objects;

public class ConcreteClassComparationStrategy <Type extends Object> extends AbstractComparationStrategy<Type> implements Comparable<ComparationStrategy> {
  
  /**
   * <p>Returns the class of the given object. Return <tt>null</tt> if the 
   * given object is <tt>null</tt>.
   * 
   * @param o The object whose class should be fetched.
   * @return The <tt>o</tt> object class, or <tt>null</tt> if <tt>o</tt> is 
   *         <tt>null</tt>
   */
  private Class safeGetClass(Type o) {
    return (o == null) ? null : o.getClass();
  }

    /**
   * <p>Compares two operands.</p>
   *
   * <p>This method implements the natural order comparation algorithm based
   * on the given operand classes.</p>
   *
   * @param  op1 First operand
   * @param  op2 Second operand
   * @return A negative integer, zero, or a positive integer as first operand
   *         is less than, equal to, or greater than the second operand
   */
  @Override
  protected int doCompare(Type op1, Type op2) {
    return op1.getClass().getName().compareTo(op2.getClass().getName());
  }

  /**
   * <p>Compares two supplied objects.</p>
   *
   * <p>To be considered equal, both operands should be instance of the same 
   * concrete class.</p>
   *
   * @param  op1 First operand
   * @param  op2 Second operand
   * @return <tt>true</tt> if the supplied operands are considered equivalent, 
   *         or <tt>false</tt> otherwise
   */
  
  public boolean equals(Type op1, Type op2) {
    return Objects.equals(safeGetClass(op1), safeGetClass(op2));
  }

  /**
   * <p>Calculates the supplied object hash code.</p>
   *
   * <p>This method uses the given operand class to calculate its hashcode.</p>
   *
   * @param  object Supplied object
   * @return The supplied object hash code
   */
  public int hashCode(Type object) {
    return (object == null) ? 0 : object.getClass().hashCode();
  }

  /**
   * <p>Compare two property based comparation strategy implementation 
   * instances.</p>
   *
   * <p>Instances of <tt>ConcreteClassComparationStrategy</tt> should always 
   * have the greatest salience (i.e. it should be executed first)</p>
   *
   * @param  operand Property based comparation strategy to be compared to the 
   *                 current instance
   * @return Zero if the operands have the same evaluation order, a negative 
   *         integer if the current instance should be evaluated first, or a 
   *         positive integer if the operand should be evaluated first
   */
  public int compareTo(ComparationStrategy o) {
    if (o instanceof ConcreteClassComparationStrategy) {
      return 0;
    }
    return -1;
  }
}
