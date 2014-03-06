/*
 */
package test.cloudml.core;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertTrue;
import org.cloudml.core.Provider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author Franck Chauvel
 * @since 0.1
 */
@RunWith(JUnit4.class)
public class ProviderTest extends TestCase {

    public static final boolean WITHOUT_WARNING = false;

    @Test
    public void passValidationWithoutWarningsWhenValid() {
        Provider provider = new Provider("Amazon EC2", "src/test/resources/credentials.txt");
        assertTrue(provider.validate().pass(WITHOUT_WARNING));
    }

    @Test
    public void detectMissingCredentials() {
        Provider provider = new Provider("Amazon EC2", "a_file_which_is_not_on_disk.properties");
        assertTrue(provider.validate().hasWarningAbout("credentials"));
    }

    @Test
    public void detectNullName() {
        Provider provider = new Provider(null, "a_file_which_is_not_on_disk.properties");
        assertTrue(provider.validate().hasErrorAbout("name"));
    }

    @Test
    public void detectEmptyName() {
        Provider provider = new Provider("", "a_file_which_is_not_on_disk.properties");
        assertTrue(provider.validate().hasErrorAbout("name"));
    }
}
