using System;
using System.Threading;
using OpenQA.Selenium;
using OpenQA.Selenium.Remote;
using NUnit.Framework;
using OpenQA.Selenium.Firefox;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.IE;
using RMTest;

namespace MyNumbers.Tests
{
    //[TestFixture]
    class MyNumbers_DEMO
    {
        IWebDriver driver = null;
        //[TestCaseSource(typeof(TestBase), "TestData")]
        //[Test]
        public void Test1(DriverNamingWrapper driverNamingWrapper, String driverDescription)
        {
            driverNamingWrapper.startDriver();
            //driver = new FirefoxDriver();
            //driver = new ChromeDriver("C:\\Users\\xtompe\\.RmTest\\lib\\Chromedriver\\");
            //driver = new InternetExplorerDriver("C:\\Users\\xtompe\\.RmTest\\lib\\IEDriver\\");
            driver = driverNamingWrapper.getDriver();
            driver.Navigate().GoToUrl("https://epmweb-st.azurewebsites.net");
            Assert.IsTrue(driver.Title.ToLower().Contains("Ainloggning"));
            Console.WriteLine(driver.Title);
            Thread.Sleep(2000);

        }

        //[TestCaseSource(typeof(TestBase), "TestData")]
        public void Test2(DriverNamingWrapper driverNamingWrapper, String driverDescription)
        {
            driverNamingWrapper.startDriver();
            //driver = new FirefoxDriver();
            //driver = new ChromeDriver("C:\\Users\\xtompe\\.RmTest\\lib\\Chromedriver\\");
            //driver = new InternetExplorerDriver("C:\\Users\\xtompe\\.RmTest\\lib\\IEDriver\\");
            driver = driverNamingWrapper.getDriver();
            driver.Navigate().GoToUrl("https://epmweb-st.azurewebsites.net");

            driver.FindElement(By.Id("Email")).SendKeys("superman@exopen.test");
            driver.FindElement(By.Id("password")).SendKeys("kalleanka");
            driver.FindElement(By.Id("btnLogin")).Click();

            //Assert.IsTrue(driver.Title.ToLower().Contains("Ainloggning"));
            Console.WriteLine(driver.Title);
            Thread.Sleep(2000);
        }

        //[TearDown]
        public void After()
        {
            if (driver != null)
            {
                driver.Quit();
            }
        }

    }
}
