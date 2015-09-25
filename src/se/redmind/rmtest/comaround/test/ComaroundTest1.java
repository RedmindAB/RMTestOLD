package se.redmind.rmtest.comaround.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

@RunWith(Parallelized.class)
public class ComaroundTest1 {


	   private WebDriver tDriver;
	    private final DriverNamingWrapper driverWrapper;
	    private final String driverDescription;
		private ComarounHeaderdNav cNav;

	    public ComaroundTest1(final DriverNamingWrapper driverWrapper, final String driverDescription) {
	        this.driverWrapper = driverWrapper;
	        this.driverDescription = driverDescription;
	    }
	    
	    private static Object[] getDrivers() {
//	        return DriverProvider.getDrivers("rmDeviceType", "mobile");
//	    	return DriverProvider.getDrivers(Platform.ANDROID);
	    	return DriverProvider.getDrivers();

	    }

	    @Parameterized.Parameters(name = "{1}")
	    public static Collection<Object[]> drivers() {
	        ArrayList<Object[]> returnList = new ArrayList<Object[]>();
	        Object[] wrapperList = getDrivers();
	        for (int i = 0; i < wrapperList.length; i++) {
	            returnList.add(new Object[]{wrapperList[i], wrapperList[i].toString()});
	        }

	        return returnList;
	    }

	    @AfterClass
	    public static void afterTest(){
	    	DriverProvider.stopDrivers();
	    }
	    

	    @Before
	    public void beforeTest(){
	    	this.tDriver = this.driverWrapper.startDriver();
	    	this.cNav = new ComarounHeaderdNav(tDriver);
	    }
	@Test
	public void clickComAroundZero() {
		cNav.clickComAroundZeroNav();
		assertEquals("ComAround Zero - ComAround", cNav.getTitle());
	}
	
	@Test
	public void clickKonceptet() {
		cNav.clickKonceptet();
		assertEquals("Konceptet - ComAround", cNav.getTitle());
	}
	
	@Test
	public void clickInspiration() {
		cNav.clickInspiration();
		assertEquals("Inspiration - ComAround", cNav.getTitle());
	}
	
	@Test
	public void clickReferenser() {
		cNav.clickReferenser();
		assertEquals("Referenser - ComAround", cNav.getTitle());
	}

	@Test
	public void clickPrismodell() {
		cNav.clickPrismodell();
		assertEquals("Prismodell - ComAround", cNav.getTitle());
	}
	
	@Test
	public void clickSkapaKonto() {
		cNav.clickSkapaKonto();
		assertEquals("Skapa konto - ComAround", cNav.getTitle());
	}
}
