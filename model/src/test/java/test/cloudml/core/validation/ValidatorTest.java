/*
 */

package test.cloudml.core.validation;

import junit.framework.TestCase;
import org.cloudml.core.validation.CanBeValidated;
import org.cloudml.core.validation.Report;
import org.cloudml.core.validation.ValidationCollector;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class ValidatorTest extends TestCase {

      private final Mockery context = new JUnit4Mockery();

      
      @Test
      public void validatorCallsValidate() {
          final CanBeValidated toValidate = context.mock(CanBeValidated.class);
          
          ValidationCollector collector = new ValidationCollector();
          context.checking(new Expectations() {{
              exactly(1).of(toValidate).validate(); will(returnValue(new Report()));
          }});
          
          collector.collect(toValidate);
      }
    
}
