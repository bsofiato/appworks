/*
 * IdentityStringficationStrategyTest.java
 * JUnit based test
 *
 * Created on 22 de Novembro de 2005, 23:28
 */

package br.com.appworks.runtime.lang.support.stringfication.test;

import br.com.appworks.runtime.lang.support.stringfication.IdentityStringficationStrategy;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class IdentityStringficationStrategyTest extends TestCase {
  
  public IdentityStringficationStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNullObjectStringfication() {
    IdentityStringficationStrategy <String> strategy = new IdentityStringficationStrategy <String> ();
    StringBuilder sb = new StringBuilder();
    strategy.toString(null, sb);
    assertEquals("null", sb.toString());
  }
  public void testNotOverridedStringObjectStringfication() {
    IdentityStringficationStrategy <IdentityStringficationStrategy> strategy = new IdentityStringficationStrategy <IdentityStringficationStrategy> ();
    StringBuilder sb = new StringBuilder();
    strategy.toString(strategy, sb);
    assertEquals(strategy.toString(), sb.toString());
  }

  public void testOverridedStringObjectStringfication() {
    IdentityStringficationStrategy <Number> strategy = new IdentityStringficationStrategy <Number> ();
    Integer i = new Integer(0);
    StringBuilder sb = new StringBuilder();
    strategy.toString(i, sb);
    assertTrue(!(i.toString().equals(sb.toString())));
    assertEquals(Integer.class.getName() + "@" + Integer.toHexString(System.identityHashCode(i)), sb.toString());
  }
  
  public void testClone() {
    try {
      assertTrue(new IdentityStringficationStrategy().clone() instanceof IdentityStringficationStrategy);
    } catch (Exception ex) {
      fail();
    }
  }

}
