/*
 * CachingStringficationStrategyFactoryAdapterTest.java
 * JUnit based test
 *
 * Created on 24 de Novembro de 2005, 00:35
 */

package br.com.appworks.runtime.lang.support.stringfication.test;

import br.com.appworks.runtime.lang.support.stringfication.CachingStringficationStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategy;
import br.com.appworks.runtime.lang.support.stringfication.StringficationStrategyFactory;
import junit.framework.*;
import org.easymock.MockControl;

public class CachingStringficationStrategyFactoryAdapterTest extends TestCase {
  
  public CachingStringficationStrategyFactoryAdapterTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    System.gc();
  }

  protected void tearDown() throws Exception {
  }
  
  
  public void testStringficationStrategyCaching() {
    try {
      MockControl stringficationStrategyFactoryControl = MockControl.createControl(StringficationStrategyFactory.class);
      MockControl stringficationStrategyControl = MockControl.createControl(StringficationStrategy.class);

      StringficationStrategyFactory stringficationStrategyFactory = (StringficationStrategyFactory)(stringficationStrategyFactoryControl.getMock());
      StringficationStrategy stringficationStrategy = (StringficationStrategy)(stringficationStrategyControl.getMock());
      
      stringficationStrategyFactory.create(getClass());
      stringficationStrategyFactoryControl.setReturnValue(stringficationStrategy);
      
      stringficationStrategyFactoryControl.replay();
      stringficationStrategyControl.replay();
   
      StringficationStrategyFactory factory = new CachingStringficationStrategyFactoryAdapter(stringficationStrategyFactory);
      assertSame(factory.create(getClass()), factory.create(getClass()));
      
      stringficationStrategyFactoryControl.verify();
      stringficationStrategyControl.verify();

    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }

    public void testStringficationStrategyCreation() {
    try {
      MockControl stringficationStrategyFactoryControl = MockControl.createControl(StringficationStrategyFactory.class);
      MockControl stringficationStrategyControl = MockControl.createControl(StringficationStrategy.class);

      StringficationStrategyFactory stringficationStrategyFactory = (StringficationStrategyFactory)(stringficationStrategyFactoryControl.getMock());
      StringficationStrategy stringficationStrategy = (StringficationStrategy)(stringficationStrategyControl.getMock());
      
      stringficationStrategyFactory.create(getClass());
      stringficationStrategyFactoryControl.setReturnValue(stringficationStrategy);
      
      stringficationStrategyFactoryControl.replay();
      stringficationStrategyControl.replay();
   
      StringficationStrategyFactory factory = new CachingStringficationStrategyFactoryAdapter(stringficationStrategyFactory);
      assertSame(stringficationStrategy, factory.create(getClass()));
      
      stringficationStrategyFactoryControl.verify();
      stringficationStrategyControl.verify();

    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
}
