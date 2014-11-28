/*
 * CachingComparationStrategyFactoryAdapterTest.java
 * JUnit based test
 *
 * Created on 19 de Novembro de 2005, 17:09
 */

package br.com.appworks.runtime.lang.support.comparation.test;

import br.com.appworks.runtime.lang.support.comparation.CachingComparationStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategy;
import br.com.appworks.runtime.lang.support.comparation.ComparationStrategyFactory;
import junit.framework.*;
import org.easymock.MockControl;

public class CachingComparationStrategyFactoryAdapterTest extends TestCase {
  
  public CachingComparationStrategyFactoryAdapterTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    System.gc();
  }

  protected void tearDown() throws Exception {
  }
  
  
  public void testComparationStrategyCaching() {
    try {
      MockControl comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      MockControl comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);

      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy comparationStrategy = (ComparationStrategy)(comparationStrategyControl.getMock());
      
      comparationStrategyFactory.create(getClass());
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
   
      ComparationStrategyFactory factory = new CachingComparationStrategyFactoryAdapter(comparationStrategyFactory);
      assertSame(factory.create(getClass()), factory.create(getClass()));
      
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();

    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testComparationStrategyCreation() {
    try {
      MockControl comparationStrategyFactoryControl = MockControl.createControl(ComparationStrategyFactory.class);
      MockControl comparationStrategyControl = MockControl.createControl(ComparationStrategy.class);

      ComparationStrategyFactory comparationStrategyFactory = (ComparationStrategyFactory)(comparationStrategyFactoryControl.getMock());
      ComparationStrategy comparationStrategy = (ComparationStrategy)(comparationStrategyControl.getMock());
      
      comparationStrategyFactory.create(getClass());
      comparationStrategyFactoryControl.setReturnValue(comparationStrategy);
      
      comparationStrategyFactoryControl.replay();
      comparationStrategyControl.replay();
   
      ComparationStrategyFactory factory = new CachingComparationStrategyFactoryAdapter(comparationStrategyFactory);
      assertSame(comparationStrategy, factory.create(getClass()));
      
      comparationStrategyFactoryControl.verify();
      comparationStrategyControl.verify();

    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
}
