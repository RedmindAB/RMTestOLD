using System;
using OpenQA.Selenium;
using OpenQA.Selenium.Remote;
//using Microsoft.VisualStudio.TestTools.UnitTesting;
using NUnit.Framework;
using RMTest;


namespace RMTest.Tests
{
    //[TestClass]
    [TestFixture]
    [Parallelizable(ParallelScope.Children)]
    public class UnitTest1 : TestBase
    {
        //public IWebDriver webDriver;
        //public static void Main()
        //{
        //    UnitTest1 myTest = new UnitTest1();
        //    myTest.NavigateToUrlWithFirstDriver(); //NavigateToUrlWithFirstDriver();
        //}

        //public UnitTest1(DriverNamingWrapper driverWrapper, String driverDescription) : base(driverWrapper, driverDescription)
        //{
        //driverWrapper.addDriverConfig(new TestConfig());
        //}

        public UnitTest1()
        {

        }

        //[SetUp]
        public void BeforeTestCase()
        {
            //Object[] drivers = DriverProvider.getDrivers();
            //DriverNamingWrapper driverNamingWrapper = new DriverNamingWrapper( List<Object[]>(DriverProvider.getDrivers();
            //this.webDriver = driverNamingWrapper.startDriver();
        }

        public Object[][] TestCases1()
        {
            //String[] numbers = new String[] { "1", "2", "3" };
            //foreach (var i in numbers)
            //{Console.WriteLine("number: " + i};
            return new Object[][] { new Object[] { "TC1.Chrome", "TC2.Firefox" }, new Object[] { "TC2", 2 } };
        }

        //[TestMethod]
        //[TestCase]
        //[Parallelizable(ParallelScope.Self)]
        [TestCaseSource(typeof(TestBase), "TestData")]
        public void NavigateToUrlWithFirstDriver(DriverNamingWrapper driverNamingWrapper, String Desc)
        {
            driverNamingWrapper.startDriver();
            IWebDriver webDriver = driverNamingWrapper.getDriver();
            webDriver.Navigate().GoToUrl("http://www.twitter.com/dedmau5");
            Console.WriteLine("url loaded! ");
            System.Threading.Thread.Sleep(1000);
            webDriver.Quit();
            Console.WriteLine("driver quit ");
        }

        //[TestMethod]
        //[Parallelizable(ParallelScope.Self), TestCaseSource(typeof(TestBase), "TestData")]
        public void TestMethod2(DriverNamingWrapper driverNamingWrapper, String Desc)
        {
            //IWebDriver driver = driverNamingWrapper.getDriver();
            try
            {
                driverNamingWrapper.startDriver();

                IWebDriver driver = driverNamingWrapper.getDriver();
                driver.Navigate().GoToUrl("http://www.redmind.se");
                //System.Threading.Thread.Sleep(5000);
                IWebElement myElement = (IWebElement)driver.FindElement(By.Id("menu-item-256")).FindElement(By.XPath("./a"));
                Console.Out.Write(myElement.Text);
                Assert.IsTrue(myElement.GetAttribute("href").Contains("redmind"));
            }
            finally
            {
                driverNamingWrapper.getDriver().Quit();
            }
        }

        //[TestCase]
        public void TestMethod3()
        {
            DesiredCapabilities dc = DesiredCapabilities.Chrome();
            dc.IsJavaScriptEnabled = false;
            String ChromePath = Environment.GetEnvironmentVariable("webdriver.chrome.driver", EnvironmentVariableTarget.Machine);
            //IWebDriver driver = new OpenQA.Selenium.Chrome.ChromeDriver(ChromePath); // (new Uri("http://127.0.0.1:4444/wd/hub"), dc);
            IWebDriver driver = new RemoteWebDriver(new Uri("http://127.0.0.1:4444/wd/hub"), dc);
            driver.Navigate().GoToUrl("http://www.redmind.se");
            System.Threading.Thread.Sleep(5000);
            IWebElement myElement = (IWebElement)driver.FindElement(By.XPath("//p"));
            driver.Close();
        }

        //[TearDown]
        public void TearDown()
        {
            
        }
	}
}