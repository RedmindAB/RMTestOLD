package se.redmind.rmtest.comaround.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.redmind.rmtest.DriverWrapper;
import se.redmind.rmtest.runners.Parallelize;
import se.redmind.rmtest.runners.RmTestRunner;

@RunWith(RmTestRunner.class)
@Parallelize
public class ComaroundTest1 {

    private final DriverWrapper<?> driverWrapper;
    private final ComarounHeaderdNav nav;

    public ComaroundTest1(DriverWrapper<?> driverWrapper) {
        this.driverWrapper = driverWrapper;
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
