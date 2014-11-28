/*
 * ArrayValueComparationStrategyTest.java
 * JUnit based test
 *
 * Created on 29 de Julho de 2005, 01:12
 */

package br.com.appworks.runtime.lang.support.comparation.test;
import br.com.appworks.runtime.lang.support.comparation.ArrayValueComparationStrategy;
import java.util.ArrayList;

/**
 *
 * @author Bubu
 */
public class ArrayValueComparationStrategyTest extends AbstractComparationStrategyTest {
  
  public ArrayValueComparationStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  public void testArrayEqualsSanityCheck() {
    assertFalse(new String[0].equals(new String[0]));
  }
  public void testArrayHashCodeSanityCheck() {
    assertFalse(new String[0].hashCode() == new String[0].hashCode());
  }
  public void testArrayCompareToSanityCheck() {
    Object array = new String [0];
    assertTrue("X" instanceof Comparable);
    assertFalse(array instanceof Comparable);
  }
  public void test() {
    ArrayValueComparationStrategy <String> comparationStrategy = new ArrayValueComparationStrategy <String> (String.class);
    String [] op1 = new String [0];
    String [] op2 = new String [0];
    String [] op3 = new String [] { "X", "Y" };
    String [] op4 = new String [] { null };
    String [] op5 = new String [] { "Y", "X" };
    String [] op6 = new String [] { "Y", null };
    String [] op7 = new String [] { null, "Y"};
    Integer [] op8 = new Integer [0];
         
    checkContract(comparationStrategy, op1, op1, op1);
    checkContract(comparationStrategy, op1, op2, op3);
    checkContract(comparationStrategy, op2, op3, op4);
    checkContract(comparationStrategy, op4, op5, op6);
    checkContract(comparationStrategy, op6, op7, op1);
    checkContract(comparationStrategy, op6, op7, op4);
    checkContract(comparationStrategy, op1, op8, op1);
    checkContract(comparationStrategy, op1, op8, op7);
    checkContract(comparationStrategy, op5, op8, op3);
  }
  
  public void testNonComparableCompareTo() {
    ArrayValueComparationStrategy <ArrayList> comparationStrategy = new ArrayValueComparationStrategy <ArrayList> (ArrayList.class);
    try {
      comparationStrategy.compare(null, null);
      fail();
    } catch (ClassCastException ex) {
    }
  }

}
