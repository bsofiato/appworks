/*
 * IdentityComparationStrategyTest.java
 * JUnit based test
 *
 * Created on 5 de Julho de 2005, 00:46
 */

package br.com.appworks.runtime.lang.support.comparation.test;
import br.com.appworks.runtime.lang.support.comparation.IdentityComparationStrategy;

/**
 *
 * @author Bubu
 */
public class IdentityComparationStrategyTest extends AbstractComparationStrategyTest {
  
  public IdentityComparationStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void test() {
    IdentityComparationStrategy <String> strategy = new IdentityComparationStrategy <String> ();
    checkContract(strategy, "OP1", "OP2", "OP3");
    checkContract(strategy, "OP1", "OP1", "OP1");
    checkContract(strategy, new String("OP1"), new String("OP1"), new String("OP1"));
  }
}
