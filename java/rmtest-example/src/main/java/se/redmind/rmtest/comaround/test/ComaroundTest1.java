package se.redmind.rmtest.comaround.test;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import se.redmind.rmtest.DriverWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;
import se.redmind.utils.LogBackUtil;

@RunWith(Parallelized.class)
public class ComaroundTest1 {

    private final DriverWrapper<?> driverWrapper;
    private final String driverDescription;
    private ComarounHeaderdNav nav;

    public ComaroundTest1(DriverWrapper<?> driverWrapper, String driverDescription) {
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
        this.nav = new ComarounHeaderdNav(driverWrapper.getDriver());
    }

    @Test
    public void clickComAroundZero() {
        nav.clickComAroundZeroNav();
        assertEquals("ComAround Zero - ComAround", nav.getTitle());
    }

    @Test
    public void clickKonceptet() {
        nav.clickKonceptet();
        assertEquals("Konceptet - ComAround", nav.getTitle());
    }

    @Test
    public void clickInspiration() {
        nav.clickInspiration();
        assertEquals("Inspiration - ComAround", nav.getTitle());
    }

    @Test
    public void clickReferenser() {
        nav.clickReferenser();
        assertEquals("Referenser - ComAround", nav.getTitle());
    }

    @Test
    public void clickPrismodell() {
        nav.clickPrismodell();
        assertEquals("Prismodell - ComAround", nav.getTitle());
    }

    @Test
    public void clickSkapaKonto() {
        nav.clickSkapaKonto();
        assertEquals("Skapa konto - ComAround", nav.getTitle());
    }
}
