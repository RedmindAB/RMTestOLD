package nottest;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import se.aftonbladet.abtest.navigation.mobil.AbMobileNav;
import se.redmind.rmtest.selenium.framework.TestParams;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

@RunWith(Parallelized.class)
public class TestGoogleExampleAndroidParallel {
    private WebDriver tDriver;
    private AbMobileNav tNavPage;
    private String startUrl = TestParams.getBaseUrl();

    private final DriverNamingWrapper driverWrapper;
    private final String driverDescription;
    
    public TestGoogleExampleAndroidParallel(final DriverNamingWrapper driverWrapper, final String driverDescription) {
       this.driverWrapper = driverWrapper;
       this.driverDescription = driverDescription;
    }
    
    private static Object[] getDrivers() {
        return DriverProvider.getDrivers(Platform.ANDROID);
        
    }
    
    
    @Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        ArrayList<Object[]> returnList = new ArrayList<Object[]>();
        Object[] wrapperList = getDrivers();
        for (int i = 0; i < wrapperList.length; i++) {
            returnList.add(new Object[]{wrapperList[i], wrapperList[i].toString()});
        }
        return returnList;
    }
    
    @Test
    @Ignore
    public void testGoogle() throws Exception {
        WebDriver driver = driverWrapper.getDriver();
        
        Capabilities capability = driverWrapper.getCapabilities();
        
        // And now use this to visit Google
        driver.get("http://www.google.com");
        
       
        // Find the text input element by its name
//        WebElement element = driver.findElement(By.name("q"));
        System.out.println("Page title is: " + driver.getTitle());
        
        assertTrue(driver.getTitle().contains("Google"));
//        assertFalse(driver.getTitle().startsWith("Goo"));
    }

    @Test
    @Ignore
    public void testAOS() throws Exception {
        WebDriver driver = driverWrapper.getDriver();
        // And now use this to visit Google
        driver.get("http://www.aos.se");

        // Find the text input element by its name
//        WebElement element = driver.findElement(By.name("q"));
        System.out.println("Page title is: " + driver.getTitle());
        assertTrue(driver.getTitle().startsWith("Allt om Stockholm"));
    }
    
    @Test
    public void ensure_mobile_nav_navigates_to_clicked_items() throws Exception {
        tDriver = driverWrapper.getDriver();
        System.out.println("Driver: " + tDriver);
        tNavPage = new AbMobileNav(tDriver, startUrl);
        tNavPage.driverFluentWait(15).until(ExpectedConditions.presenceOfElementLocated(By.id("abMeasure")));

        tNavPage.clickLeftMenuItem("Nyheter");
        tNavPage.assertPageTitle("Nyheter");
        
        tNavPage.clickLeftMenuItem("Sport");
        tNavPage.assertPageTitle("Sport");
        
        tNavPage.clickLeftMenuItem("Nöje");
        tNavPage.assertPageTitle("Nöje");
        
        tNavPage.clickLeftMenuItem("Ledare");
        tNavPage.assertPageTitle("Ledare");
        
        tNavPage.clickLeftMenuItem("Kultur");
        tNavPage.assertPageTitle("Kultur");
        
        tNavPage.clickLeftMenuItem("Debatt");
        tNavPage.assertPageTitle("Debatt");
        
        tNavPage.clickLeftMenuItem("Kolumnister");
        tNavPage.assertPageTitle("Kolumnister");
        
        tNavPage.clickLeftMenuItem("Plus");
        tNavPage.assertPageTitle("Plus");
        
        tNavPage.clickLeftMenuItem("Wendela");
        tNavPage.assertPageTitle("Wendela");
    }

}