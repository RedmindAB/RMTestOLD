package test.java.se.redmind.rmtest.selenium.example;

import org.junit.AfterClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.BeforeClass;
import org.openqa.selenium.Platform;

import se.redmind.rmtest.selenium.example.CopyOfGoogleExample;
import se.redmind.rmtest.selenium.example.GoogleExample;
//import se.aftonbladet.abtest.tests.mobile.menu.AbMobileNavTest;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.DriverProviderNew;




@RunWith(Suite.class)
@Suite.SuiteClasses( {CopyOfGoogleExample.class})
public class CopyOfGoogleTests {
	
	@ClassRule
	  public static ExternalResource resource = new ExternalResource() {
	    
	    protected void before() throws Throwable {
//	    	DriverProviderNew.clearAndFetchDrivers();
//	    	DriverProviderNew.addDrivers(Platform.MAC);
//	    	DriverProviderNew.addDrivers();
	    	System.out.println("BEFORECLASS!!");
	    };

	    
	    protected void after() {
	    	DriverProvider.stopDrivers();
	    };
	  };
	
    @BeforeClass
    public static void beforeAllTests(){

    }

    
    @AfterClass
    public static void afterAllTests(){
        
    }
}