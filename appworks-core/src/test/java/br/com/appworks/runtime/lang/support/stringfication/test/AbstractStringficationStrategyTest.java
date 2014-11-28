package br.com.appworks.runtime.lang.support.stringfication.test;

import br.com.appworks.runtime.lang.support.stringfication.AbstractStringficationStrategy;
import junit.framework.TestCase;

public class AbstractStringficationStrategyTest extends TestCase {
  public class MockAbstractStringficationStrategy extends AbstractStringficationStrategy {
    @Override
    protected void safeToString(Object object, StringBuilder sb) {
      sb.append(object);
    }
  }
  public AbstractStringficationStrategyTest(String testName) {
    super(testName);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public void testToStringNull() {
    StringBuilder sb = new StringBuilder();
    new MockAbstractStringficationStrategy().toString(null, sb);
    assertEquals("null", sb.toString());
  }

  public void testToString() {
    StringBuilder sb = new StringBuilder();
    new MockAbstractStringficationStrategy().toString("a", sb);
    assertEquals("a", sb.toString());
  }

  public void testClone() throws CloneNotSupportedException {
    MockAbstractStringficationStrategy strategy = new MockAbstractStringficationStrategy();
    MockAbstractStringficationStrategy clone = (MockAbstractStringficationStrategy) strategy.clone();
    assertNotSame(strategy, clone);
    StringBuilder sb = new StringBuilder();
    clone.toString("A", sb);
    assertEquals("A", sb.toString());
  }
}

