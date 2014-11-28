/*
 * GroovyTemplateFactoryTest.java
 * JUnit based test
 *
 * Created on 29 de Novembro de 2005, 00:08
 */

package br.com.appworks.runtime.lang.support.template.groovy.test;

import br.com.appworks.runtime.lang.support.template.Template;
import br.com.appworks.runtime.lang.support.template.TemplateCompilationException;
import br.com.appworks.runtime.lang.support.template.TemplateException;
import br.com.appworks.runtime.lang.support.template.groovy.GroovyTemplateFactory;
import junit.framework.*;

public class GroovyTemplateFactoryTest extends TestCase {
  
  public GroovyTemplateFactoryTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  public void testNullSourceCodeExpressionCreation() {
    try {
      new GroovyTemplateFactory().create(null);
      fail();
    } catch (TemplateCompilationException ex) {
    } catch (TemplateException ex) {
      fail();
    }
  }

  public void testSourceCodeExpressionCreation() {
    try {
      Template template = new GroovyTemplateFactory().create("TESTE");
      assertEquals("TESTE", template.getSourceCode());
    } catch (TemplateException ex) {
      fail();
    }
  }
}
