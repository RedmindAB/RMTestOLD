using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using OpenQA.Selenium.Remote;
using OpenQA.Selenium;

namespace RMTest
{
	public class DriverNamingWrapper {
    private String driverDescription = "";
    private IWebDriver driver;
    private DesiredCapabilities capabilities;
    private Uri driverUrl;
    
    public DriverNamingWrapper(String pDriverDesciption, IWebDriver pDriver, DesiredCapabilities pCapabilities, Uri pUrl) {
        driverDescription = pDriverDesciption;
        driver = pDriver;
        capabilities = pCapabilities;
        driverUrl = pUrl;
    }
    
    public DesiredCapabilities getCapabilities() {
        return capabilities;
    }
    
    public String getDriverDescription() {
        return driverDescription;
    }
    
    public IWebDriver getDriver() {
        return driver;
    }
    
    public Uri getUrl() {
        return driverUrl;
    }
    
    
    public String toString() {
        return driverDescription;
    }
}
}
