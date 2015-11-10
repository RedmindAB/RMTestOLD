using System;
using OpenQA.Selenium;
using OpenQA.Selenium.Remote;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using 
using RMTest;


namespace RMTest.Tests
{
	[TestClass]
    [TestFixture]
	public class UnitTest1
	{
        public static void Main()
        {
            UnitTest1 myTest = new UnitTest1();
            myTest.NavigateToUrlWithFirstDriver(); //NavigateToUrlWithFirstDriver();
        }

        [TestMethod]
        public void NavigateToUrlWithFirstDriver()
        {
            DriverNamingWrapper driverWrapper = (DriverNamingWrapper)DriverProvider.getDrivers()[0]; //getFirstDriver();
            driverWrapper.startDriver();
            IWebDriver webDriver = driverWrapper.getDriver();
            webDriver.Navigate().GoToUrl("http://www.twitter.com/dedmau5");
            Console.WriteLine("url loaded! ");
            System.Threading.Thread.Sleep(10000);
            webDriver.Quit();
            Console.WriteLine("driver quit ");
        }

        [TestMethod]
		public void TestMethod2()
		{
            //System.Console.Write(NodeInfoFromHub.main("localhost", 4444).ToString());
            //System.Threading.Thread.Sleep(1000);

            object[] drivers = DriverProvider.getDrivers();
            foreach(DriverNamingWrapper driver in drivers)
            {
                driver.startDriver();
                IWebDriver myDriver = ((DriverNamingWrapper)driver).getDriver();
                myDriver.Navigate().GoToUrl("http://www.redmind.se");
                System.Threading.Thread.Sleep(5000);
                IWebElement myElement = (IWebElement)myDriver.FindElement(By.Id("seed-csp4-headline"));
                Assert.IsTrue(myElement.Text.Contains("uppdaterar"));
                myDriver.Close();
            }
            
			//testStuffsHere
		}

        public void TestMethod3()
        {
            DesiredCapabilities dc = DesiredCapabilities.Chrome();
            dc.IsJavaScriptEnabled = false;
            String ChromePath = Environment.GetEnvironmentVariable("webdriver.chrome.driver", EnvironmentVariableTarget.Machine);
            //IWebDriver driver = new OpenQA.Selenium.Chrome.ChromeDriver(ChromePath); // (new Uri("http://127.0.0.1:4444/wd/hub"), dc);
            IWebDriver driver = new RemoteWebDriver(new Uri("http://127.0.0.1:4444/wd/hub"), dc);
            driver.Navigate().GoToUrl("http://www.redmind.se");
            System.Threading.Thread.Sleep(5000);
            IWebElement myElement = (IWebElement)driver.FindElement(By.Id("seed-csp4-headline"));
            driver.Close();
        }
	}
}