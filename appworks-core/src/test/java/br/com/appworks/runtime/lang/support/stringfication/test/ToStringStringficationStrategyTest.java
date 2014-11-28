/*
 * ToStringStringficationStrategyTest.java
 * JUnit based test
 *
 * Created on 22 de Novembro de 2005, 23:40
 */

package br.com.appworks.runtime.lang.support.stringfication.test;

import br.com.appworks.runtime.lang.support.stringfication.ToStringStringficationStrategy;
import java.io.Serializable;
import junit.framework.*;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class ToStringStringficationStrategyTest extends TestCase {
  public static class TestBean {
    private boolean ok = false;
    public String toString() {
      ok = true;
      return "TESTE";
    }
  }
  public ToStringStringficationStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNullObjectStringfication() {
    ToStringStringficationStrategy <String> strategy = new ToStringStringficationStrategy <String> ();
    StringBuilder sb = new StringBuilder();
    strategy.toString(null, sb);
    assertEquals("null", sb.toString());
  }
  public void testObjectStringfication() {
    ToStringStringficationStrategy <TestBean> strategy = new ToStringStringficationStrategy <TestBean> ();
    TestBean x = new TestBean();
    assertFalse(x.ok);
    assertEquals("TESTE", x.toString());
    assertTrue(x.ok);
  }
  
  public void testClone() {
    try {
      assertTrue(new ToStringStringficationStrategy().clone() instanceof ToStringStringficationStrategy);
    } catch (Exception ex) {
      fail();
    }
  }

}
