package br.com.appworks.runtime.lang.test;

import br.com.appworks.runtime.lang.OrderPolicy;
import java.util.Comparator;
import junit.framework.TestCase;

public class OrderPolicyTest extends TestCase {
  public OrderPolicyTest(String name) {
    super(name);
  }
  protected void setUp() throws Exception {
    super.setUp();
  }
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  public void testAscendingAdjustComparationResult() {
    assertEquals(0, OrderPolicy.NATURAL.adjustComparationResult(0));
    assertEquals(-1, OrderPolicy.NATURAL.adjustComparationResult(-1));
    assertEquals(1, OrderPolicy.NATURAL.adjustComparationResult(1));
  }
  public void testDescendingAdjustComparationResult() {
    assertEquals(0, OrderPolicy.INVERSE.adjustComparationResult(0));
    assertEquals(1, OrderPolicy.INVERSE.adjustComparationResult(-1));
    assertEquals(-1, OrderPolicy.INVERSE.adjustComparationResult(1));
  }
}
