package se.redmind.rmtest.aftonbladet.webviewtest;

import io.appium.java_client.android.AndroidDriver;
import se.redmind.rmtest.selenium.framework.RMReportScreenshot;
import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.DriverProvider;
import se.redmind.rmtest.selenium.grid.Parallelized;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Parallelized.class)
public class AftonbladetWebView {

    private static final Logger LOG = LoggerFactory.getLogger(AftonbladetWebView.class);

    private AndroidDriver tDriver;
    private final DriverNamingWrapper urlContainer;
    private final String driverDescription;
    private final RMReportScreenshot rmrScreenshot;
    private AftonbladetWebViewNav nav;

    public AftonbladetWebView(final DriverNamingWrapper driverWrapper, final String driverDescription) {
        this.urlContainer = driverWrapper;
        this.driverDescription = driverDescription;
        this.rmrScreenshot = new RMReportScreenshot(urlContainer);
    }

    private static Object[] getDrivers() {
        return DriverProvider.getDrivers();
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> drivers() {
        ArrayList<Object[]> returnList = new ArrayList<Object[]>();
        Object[] wrapperList = getDrivers();
        for(int i = 0; i < wrapperList.length; i++) {
            returnList.add(new Object[]{wrapperList[i], wrapperList[i].toString()});
        }
        return returnList;
    }

    @AfterClass
    public static void afterTest() {
        DriverProvider.stopDrivers();
    }

    @Before
    public void beforeTest() {
        this.tDriver = (AndroidDriver) this.urlContainer.startDriver();
        try {
            Thread.sleep(10000);
        }
        catch(InterruptedException e) {
            LOG.debug("", e);
        }
        this.nav = new AftonbladetWebViewNav(tDriver);
    }

    @Test
    public void getWebView() throws Exception {
        nav.getWebView();
    }
}