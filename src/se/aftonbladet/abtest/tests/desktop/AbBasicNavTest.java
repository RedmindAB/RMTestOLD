package se.aftonbladet.abtest.tests.desktop;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import se.aftonbladet.abtest.navigation.AbBasicNav;


/**
 * @author petost
 *
 */
public class AbBasicNavTest {

    /**
     * 
     */
    @Test
    public void test() {
        DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
        WebDriver tDriver;
        try {
            tDriver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);

          AbBasicNav tNavPage = new AbBasicNav(tDriver);
          
          
          tNavPage.clickOnMenuItem("TV");


          Assert.assertEquals(tNavPage.getDriver().getTitle(), "Aftonbladet TV: Webbtv");
          tDriver.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
