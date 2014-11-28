/*
 * ReflectionCloningStrategyTest.java
 *
 * Created on 13 de Julho de 2005, 21:35
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package br.com.appworks.runtime.lang.support.cloning.test;
import br.com.appworks.runtime.lang.support.cloning.ReflectionCloningStrategy;
import junit.framework.TestCase;
import org.easymock.MockControl;

public class ReflectionCloningStrategyTest extends TestCase {
  public static interface TestCloneable extends Cloneable {
    public Object clone() throws CloneNotSupportedException;
  }
  
  private static class PrivateTestCloneable implements Cloneable {
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }

  public class ProtectedCloneTestCloneable implements Cloneable {
    protected Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
  }

  public ReflectionCloningStrategyTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }

  public void testNullCloning() {
    try {
      assertNull(new ReflectionCloningStrategy <Object> ().clone(null));
    } catch (Exception ex) {
      fail();
    }
  }
  public void testNonCloneableCloning() {
    try {
      Object object = new Object();
      assertSame(object, new ReflectionCloningStrategy <Object> ().clone(object));
    } catch (CloneNotSupportedException ex) {
      fail();
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testCloneableSucessfullCloning() {
    try {
      Object returnValue = new Object();
      MockControl control = MockControl.createControl(TestCloneable.class);
      TestCloneable cloneable = (TestCloneable)(control.getMock());
      cloneable.clone();
      control.setReturnValue(returnValue);
      control.replay();
      assertSame(returnValue, new ReflectionCloningStrategy <Object> ().clone(cloneable));
      control.verify();
    } catch (Exception ex) {
      fail();
    }
  }
  public void testCloneableCloningWithCloneNotSupportedException() {
    MockControl control = MockControl.createControl(TestCloneable.class);
    CloneNotSupportedException exp = new CloneNotSupportedException();
    try {
      TestCloneable cloneable = (TestCloneable)(control.getMock());
      cloneable.clone();
      control.setThrowable(exp);
      control.replay();
      new ReflectionCloningStrategy <Object> ().clone(cloneable);
      fail();
    } catch (CloneNotSupportedException ex) {
      assertSame(exp, ex);
    } catch (Exception ex) {
      fail();
    } finally {
      control.verify();
    }
  }

  public void testCloneableCloningWithRuntimeException() {
    MockControl control = MockControl.createControl(TestCloneable.class);
    UnsupportedOperationException exp = new UnsupportedOperationException("ERROR");
    try {
      TestCloneable cloneable = (TestCloneable)(control.getMock());
      cloneable.clone();
      control.setThrowable(exp);
      control.replay();
      new ReflectionCloningStrategy <Object> ().clone(cloneable);
      fail();
    } catch (CloneNotSupportedException ex) {
      assertSame(exp.getMessage(), ex.getMessage());
    } catch (Exception ex) {
      fail();
    } finally {
      control.verify();
    }
  }
  
  public void testNonOverridedCloneCloneableSucessfullCloning() {
    try {
      java.lang.Cloneable cloneable = new java.lang.Cloneable () {};
      assertNotNull(new ReflectionCloningStrategy <java.lang.Cloneable> ().clone(cloneable));
      assertNotSame(cloneable, new ReflectionCloningStrategy <java.lang.Cloneable> ().clone(cloneable));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testPrivateTestCloneableCloning() {
    try {
      PrivateTestCloneable source = new PrivateTestCloneable();
      assertNotSame(source, new ReflectionCloningStrategy <PrivateTestCloneable> ().clone(source));
    } catch (Exception ex) {
      fail();
    }
  }

  public void testProtectedCloneTestCloneable() {
    try {
      ProtectedCloneTestCloneable source = new ProtectedCloneTestCloneable();
      assertNotSame(source, new ReflectionCloningStrategy <ProtectedCloneTestCloneable> ().clone(source));
    } catch (Exception ex) {
      fail();
    }
  }
  
  public void testClone() {
    try {
      ReflectionCloningStrategy strategy = new ReflectionCloningStrategy();
      assertTrue(strategy.clone() instanceof ReflectionCloningStrategy);
      assertNotSame(strategy, strategy.clone());
    } catch (Exception ex) {
      fail();
    }
  }

}
