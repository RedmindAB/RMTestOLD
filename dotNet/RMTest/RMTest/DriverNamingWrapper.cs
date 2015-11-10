using System;
using System.Collections.Generic;
using OpenQA.Selenium.Remote;
using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Firefox;
using OpenQA.Selenium.PhantomJS;
using OpenQA.Selenium.Support.UI;
using NUnit.Framework;
using OpenQA.Selenium.Appium;


namespace RMTest
{
    public class DriverNamingWrapper
    {
    private List<DriverConfig> driverConfigs = new List<DriverConfig>(); // ArrayList();
    private String driverDescription = "";
    private IWebDriver driver;
    private DesiredCapabilities capabilities;
    private Uri uri;
    private Browser? browser = null;
    private bool imAFailure;

    public DriverNamingWrapper(String pDriverDesciption, IWebDriver pDriver, DesiredCapabilities pCapabilities, Uri pUrl) {
        this.driverDescription = pDriverDesciption;
        this.driver = pDriver;
        this.capabilities = pCapabilities;
        this.uri = pUrl;
    }
    
    public DesiredCapabilities getCapabilities() {
        return capabilities;
    }
    
    public String getDriverDescription() {
        return driverDescription;
    }
    
    public IWebDriver getDriver() {
        return this.driver;
    }

    public Uri getUrl() {
        return uri;
    }

    public override String ToString() {
        return driverDescription;
    }




        public DriverNamingWrapper(Uri uri, DesiredCapabilities capabilities, String description)
        {
            this.uri = uri;
            this.capabilities = capabilities;
            this.driverDescription = description;
            this.imAFailure = false;
        }

        public DriverNamingWrapper(Browser browser, String description)
        {
            this.browser = browser;
            this.driverDescription = description;
            this.capabilities = getLocalcapabilities();
        }

        private DesiredCapabilities getLocalcapabilities()
        {
            switch (browser)
            {
                case Browser.Chrome:
                    return DesiredCapabilities.Chrome();
                case Browser.Firefox:
                    return DesiredCapabilities.Firefox();
                case Browser.PhantomJS:
                    return DesiredCapabilities.PhantomJS();
                default:
                    return null;
            }
        }

        public String getDescription()
        {
            return driverDescription;
        }

        public void ignoreAtNoConnectivityById(String url, String id)
        {
            ignoreAtNoConnectivityTo(url, By.Id(id));
        }

        public void ignoreAtNoConnectivityByClass(String url, String className)
        {
            ignoreAtNoConnectivityTo(url, By.ClassName(className));
        }

        public void ignoreAtNoConnectivityByXpath(String url, String xpath)
        {
            ignoreAtNoConnectivityTo(url, By.XPath(xpath));
        }

        public void ignoreAtNoConnectivityTo(String url, By by)
        {
            try
            {
                getDriver().Navigate().GoToUrl(url); // get(url);
                driverWaitElementPresent(by, 10);
            }
            catch (Exception e) when (e is NoSuchElementException || e is TimeoutException)
            {
                this.imAFailure = true;
                Assume.That(false,"This driver doesn't seem to have connectivity to: " + url);
            }
        }

        public void addDriverConfig(DriverConfig conf)
        {
            driverConfigs.Add(conf);
        }

        /**
         * @param pBy
         * @param timeoutInSeconds
         */
        public void driverWaitElementPresent(By pBy, int timeoutInSeconds)
        {
            new WebDriverWait(getDriver(), TimeSpan.FromSeconds(timeoutInSeconds)).Until<IWebElement>(ExpectedConditions.ElementIsVisible(pBy));//until(ExpectedConditions.presenceOfElementLocated(pBy));
        }

        /**
         * @param filteredUrlCapList
         */
        public IWebDriver startDriver()
        {
            setupCapabilities();
            if (this.driver == null)
            {
                if (browser != null)
                {
                    this.driver = startLocalDriver(this.browser);
                }
                else
                {
                    int maxRetryAttempts = 5;
                    if (this.imAFailure)
                    {
                        Assume.That(false,"Since driver didn't start after  " + maxRetryAttempts + " attempts, it probably won't start now ");
                        return this.driver;
                    }
                    else
                    {
                        int retryAttempts = 1;

                    //startDriverLabel:
                        {
                            while (retryAttempts <= maxRetryAttempts)
                            {
                                try
                                {
                                    if (this.driver != null)
                                    {
                                        this.driver.Close();
                                    }
                                    if (capabilities.GetCapability("rmDeviceType") == null)
                                    {
                                        logger.info("This is a RemoteWebDriver");
                                        this.driver = new RemoteWebDriver(uri, capabilities);
                                        
                                    }
                                    else
                                    {
                                        logger.info("This is an AppiumDriver");
                                        if ("Android".ToLower().Equals(capabilities.Platform.ToString().ToLower())) //capabilities.getCapability("platformName")
                                        {
                                            //DesiredCapabilities dc = DesiredCapabilities.Android();
                                            //dc.SetCapability("emulator", "false");
                                            //dc.SetCapability("screenSize", "1080x1920");
                                            //this.driver = new OpenQA.Selenium.Appium.Android.AndroidDriver<IWebElement>(uri, dc); // capabilities);
                                            this.driver = new OpenQA.Selenium.Appium.Android.AndroidDriver<IWebElement>(uri, capabilities);
                                        }
                                        else
                                        {
                                            this.driver = new OpenQA.Selenium.Appium.iOS.IOSDriver<IWebElement>(uri, capabilities);
                                        }
                                        
                                    }
                                    logger.info("Started driver: " + driverDescription);
                                    break;
                                }
                                catch (Exception e)
                                {
                                    logger.warn("Having trouble starting webdriver for device: " + this.driverDescription, e);
                                    logger.warn("Attempt " + retryAttempts + " of " + maxRetryAttempts);
                                    if (retryAttempts >= maxRetryAttempts)
                                    {
                                        this.imAFailure = true;
                                        Assume.That(false, "Driver failed to start properly after " + retryAttempts + " attempts");
                                    }
                                    retryAttempts++;
                                    //continue;
                                }
                                
                                //break; // startDriverLabel;
                            }
                            
                        }
                    }
                }
            }
            return this.driver;
        }

        private void setupCapabilities()
        {
            //driverConfigs. stream()
            //    .filter(DriverConfig->DriverConfig.eval(capabilities, driverDescription))
            //    .forEach(DriverConfig->DriverConfig.config(capabilities));

            foreach (DriverConfig driverConfig in driverConfigs)
            {
                if (driverConfig.eval(capabilities, driverDescription))
                {
                    driverConfig.config(capabilities);
                }
            }
        }

        private IWebDriver startLocalDriver(Browser? browser)
        {
            IWebDriver localDriver = null;
            switch (browser)
            {
                case Browser.Chrome:
                    // How can we use "capabilities"? Chrome uses "Chrome Options"! Do we need to use it for a local driver?
                    localDriver = new ChromeDriver(getChromePath()); // (ChromeOptions.Capability); //   capabilities);
                    break;
                case Browser.Firefox:
                    localDriver = new FirefoxDriver(capabilities);
                    break;
                case Browser.PhantomJS:
                    // How can we use "capabilities"? Do we need to use it for a local driver?
                    localDriver = new PhantomJSDriver(); // new PhantomJSOptions().AddAdditionalCapability("", "")); //   capabilities);
                    break;
                default:
                    break;
            }
            return localDriver;
        }

        private String getChromePath()
        {
            String osName = System.Environment.GetEnvironmentVariable("OS"); // os.name"); //  getProperty
            String _default = TestHome.main() + "/lib/chromedriver";
            if (osName.StartsWith("Mac"))
            {
                //System.out.println("Setting default chromedriver");
                return _default;
            }
            else if (osName.StartsWith("Linux"))
            {
                //System.out.println("Setting linux chromedriver");
                return TestHome.main() + "/lib/linux/chromedriver";
            }
            return _default;
        }
   
    }
}
