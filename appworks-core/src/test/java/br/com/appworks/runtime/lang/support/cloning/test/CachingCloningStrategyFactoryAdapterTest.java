/*
 * CachingCloningStrategyFactoryAdapterTest.java
 * JUnit based test
 *
 * Created on 19 de Novembro de 2005, 16:23
 */

package br.com.appworks.runtime.lang.support.cloning.test;
import br.com.appworks.runtime.lang.support.cloning.CachingCloningStrategyFactoryAdapter;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategy;
import br.com.appworks.runtime.lang.support.cloning.CloningStrategyFactory;
import junit.framework.*;
import org.easymock.MockControl;

/**
 *
 * @author Bubu
 */
public class CachingCloningStrategyFactoryAdapterTest extends TestCase {
  
  public CachingCloningStrategyFactoryAdapterTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    System.gc();
  }

  protected void tearDown() throws Exception {
  }
  
  
  public void testCloningStrategyCaching() {
    try {
      MockControl cloningStrategyFactoryControl = MockControl.createControl(CloningStrategyFactory.class);
      MockControl cloningStrategyControl = MockControl.createControl(CloningStrategy.class);

      CloningStrategyFactory cloningStrategyFactory = (CloningStrategyFactory)(cloningStrategyFactoryControl.getMock());
      CloningStrategy cloningStrategy = (CloningStrategy)(cloningStrategyControl.getMock());
      
      cloningStrategyFactory.create(getClass());
      cloningStrategyFactoryControl.setReturnValue(cloningStrategy);
      
      cloningStrategyFactoryControl.replay();
      cloningStrategyControl.replay();
   
      CloningStrategyFactory factory = new CachingCloningStrategyFactoryAdapter(cloningStrategyFactory);
      assertSame(factory.create(getClass()), factory.create(getClass()));
      
      cloningStrategyFactoryControl.verify();
      cloningStrategyControl.verify();

    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
  
  public void testCloningStrategyCreation() {
    try {
      MockControl cloningStrategyFactoryControl = MockControl.createControl(CloningStrategyFactory.class);
      MockControl cloningStrategyControl = MockControl.createControl(CloningStrategy.class);

      CloningStrategyFactory cloningStrategyFactory = (CloningStrategyFactory)(cloningStrategyFactoryControl.getMock());
      CloningStrategy cloningStrategy = (CloningStrategy)(cloningStrategyControl.getMock());
      
      cloningStrategyFactory.create(getClass());
      cloningStrategyFactoryControl.setReturnValue(cloningStrategy);
      
      cloningStrategyFactoryControl.replay();
      cloningStrategyControl.replay();
   
      CloningStrategyFactory factory = new CachingCloningStrategyFactoryAdapter(cloningStrategyFactory);
      assertSame(cloningStrategy, factory.create(getClass()));
      
      cloningStrategyFactoryControl.verify();
      cloningStrategyControl.verify();

    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    }
  }
}
