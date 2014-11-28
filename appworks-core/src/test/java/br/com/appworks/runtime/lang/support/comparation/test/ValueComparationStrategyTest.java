/*
 * ValueComparationStrategyTest.java
 * JUnit based test
 *
 * Created on 4 de Julho de 2005, 23:42
 */

package br.com.appworks.runtime.lang.support.comparation.test;

import br.com.appworks.runtime.lang.support.comparation.ValueComparationStrategy;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class ValueComparationStrategyTest extends AbstractComparationStrategyTest {
  
  public ValueComparationStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void test() {
    ValueComparationStrategy <String> strategy = new ValueComparationStrategy <String> ();
    checkContract(strategy, "OP1", "OP2", "OP3");
    checkContract(strategy, "OP1", "OP1", "OP1");
    checkContract(strategy, new String("OP1"), new String("OP1"), new String("OP1"));
  }
}
