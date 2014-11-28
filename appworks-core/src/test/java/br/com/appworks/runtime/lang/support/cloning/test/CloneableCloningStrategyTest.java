/*
 * CloneableCloningStrategyTest.java
 * JUnit based test
 *
 * Created on 14 de Julho de 2005, 00:11
 */

package br.com.appworks.runtime.lang.support.cloning.test;

import junit.framework.*;
import br.com.appworks.runtime.lang.support.cloning.Cloneable;
import br.com.appworks.runtime.lang.support.cloning.CloneableCloningStrategy;
import org.easymock.MockControl;
/**
 *
 * @author Bubu
 */
public class CloneableCloningStrategyTest extends TestCase {
  public CloneableCloningStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }

  public void testNullCloning() {
    try {
      assertNull(new CloneableCloningStrategy <Cloneable> ().clone(null));
    } catch (Exception ex) {
      fail();
    }
  }
  public void testCloneableCloning() {
    try {
      MockControl control = MockControl.createControl(Cloneable.class);
      Cloneable cloneable = (Cloneable)(control.getMock());
      cloneable.clone();
      control.setReturnValue(cloneable);
      control.replay();
      assertSame(cloneable, new CloneableCloningStrategy <Cloneable> ().clone(cloneable));
      control.verify();
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testClone() {
    try {
      CloneableCloningStrategy strategy = new CloneableCloningStrategy();
      assertTrue(strategy.clone() instanceof CloneableCloningStrategy);
      assertNotSame(strategy, strategy.clone());
    } catch (Exception ex) {
      fail();
    }
  }
}
