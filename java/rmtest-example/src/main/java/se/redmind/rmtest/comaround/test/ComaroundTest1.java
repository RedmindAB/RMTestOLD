package se.redmind.rmtest.comaround.test;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;
import se.redmind.utils.LogBackUtil;

@RunWith(Parallelized.class)
public class ComaroundTest1 {

    private WebDriver tDriver;
    private final DriverNamingWrapper driverWrapper;
    private final String driverDescription;
    private ComarounHeaderdNav cNav;

    public ComaroundTest1(final DriverNamingWrapper driverWrapper, final String driverDescription) {
        LogBackUtil.ifNotInstalled().install();
        this.driverWrapper = driverWrapper;
        this.driverDescription = driverDescription;
    }

    private static Object[] getDrivers() {
        return DriverProvider.getDrivers();

    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        ArrayList<Object[]> returnList = new ArrayList<>();
        Object[] wrapperList = getDrivers();
        for (Object wrapperList1 : wrapperList) {
            returnList.add(new Object[]{wrapperList1, wrapperList1.toString()});
        }

        return returnList;
    }

    @AfterClass
    public static void afterTest() {
        DriverProvider.stopDrivers();
    }

    @Before
    public void beforeTest() {
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
