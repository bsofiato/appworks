/*
 * ArrayStringficationStrategyTest.java
 * JUnit based test
 *
 * Created on 22 de Novembro de 2005, 23:58
 */

package br.com.appworks.runtime.lang.support.stringfication.test;

import br.com.appworks.runtime.lang.support.stringfication.ArrayStringficationStrategy;
import br.com.appworks.runtime.lang.support.stringfication.IdentityStringficationStrategy;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class ArrayStringficationStrategyTest extends TestCase {
  
  public ArrayStringficationStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  public void testArrayToStringSanityCheck() {
    String [] array = new String [] { "TESTE1", "TESTE2" };
    StringBuilder sb = new StringBuilder();
    new IdentityStringficationStrategy().toString(array, sb);
    assertEquals(sb.toString(), array.toString());
  }
  
  public void testNullObjectStringfication() {
    ArrayStringficationStrategy strategy = new ArrayStringficationStrategy (new String[0].getClass());
    StringBuilder sb = new StringBuilder();
    strategy.toString(null, sb);
    assertEquals("null", sb.toString());
  }
  
  public void testFlatArrayStringfication() {
    ArrayStringficationStrategy strategy = new ArrayStringficationStrategy (new String[0].getClass());
    StringBuilder sb = new StringBuilder();
    strategy.toString(new String [] { "TESTE1", "TESTE2" }, sb);
    assertTrue(sb.toString().contains("TESTE1"));
    assertTrue(sb.toString().contains("TESTE2"));
  }
  
  public void testMultidimensionalNullArrayStringfication() {
    ArrayStringficationStrategy strategy = new ArrayStringficationStrategy (new String[0].getClass());
    StringBuilder sb = new StringBuilder();
    strategy.toString(new String [][] { new String [] { null }}, sb);
    assertTrue(sb.toString().contains("null"));
  }
  
  public void testMultidimensionaArrayStringfication() {
    ArrayStringficationStrategy strategy = new ArrayStringficationStrategy (new String[0].getClass());
    StringBuilder sb = new StringBuilder();
    strategy.toString(new String [][] { new String [] { "TESTE1", "TESTE2" }, new String [] { "TESTE3", "TESTE4" }}, sb);
    assertTrue(sb.toString().contains("TESTE1"));
    assertTrue(sb.toString().contains("TESTE2"));
    assertTrue(sb.toString().contains("TESTE3"));
    assertTrue(sb.toString().contains("TESTE4"));
  }
  
  public void testClone() {
    try {
      assertTrue(new ArrayStringficationStrategy(new String[0].getClass()).clone() instanceof ArrayStringficationStrategy);
    } catch (Exception ex) {
      fail();
    }
  }
}
