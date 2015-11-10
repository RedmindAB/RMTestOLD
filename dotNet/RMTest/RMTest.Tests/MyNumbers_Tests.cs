using System;
using System.Threading;
using OpenQA.Selenium;
using OpenQA.Selenium.Remote;
using NUnit.Framework;
using RMTest;

namespace RMTest.Tests
{
    [TestFixture, Parallelizable(ParallelScope.Children)]
    public class MyNumbers_Test
    {
        public MyNumbers_Test()
        {

        }



        //[Parallelizable(ParallelScope.Self),TestCaseSource(typeof(TestBase), "TestData")]
        public void Kontaktforfragan(DriverNamingWrapper driverNamingWrapper, String driverDescription)
        {
            try {
                driverNamingWrapper.startDriver();
                IWebDriver webDriver = driverNamingWrapper.getDriver();

                webDriver.Navigate().GoToUrl("http://www.mynumbers.nu/");
                System.Threading.Thread.Sleep(2000);
                //IWebElement webMenu = webDriver.FindElement(By.Id("mainNavigation"));
                IWebElement mobileMenu = webDriver.FindElement(By.XPath("//label[@class='mobile-nav-toggle-label']"));
                if (mobileMenu.Displayed)
                {
                    mobileMenu.Click();
                    //System.Threading.Thread.Sleep(2000);
                    driverNamingWrapper.driverWaitElementPresent(By.XPath("//nav[@id='mobileNavigation']//a[@href='/kontakt/']"), 5); //XPath("//div[]//a[@href='/kontakt/']"), 5); // //nav[@id='mainNavigation']
                    IWebElement menuKontakt = webDriver.FindElement(By.XPath("//nav[@id='mobileNavigation']//a[@href='/kontakt/']")); //< a href = "/kontakt/" >Kontakt</ a >
                    int i = 1;
                    do
                    {
                        if (menuKontakt.Enabled)
                        {
                            menuKontakt.Click();
                            i = 10;
                        }
                        System.Threading.Thread.Sleep(500);  
                        
                    } while (i < 10);
                }
                else
                {
                    driverNamingWrapper.driverWaitElementPresent(By.XPath("//nav[@id='mainNavigation']//a[@href='/kontakt/']"), 5); // 
                    IWebElement menuKontakt = webDriver.FindElement(By.XPath("//nav[@id='mainNavigation']//a[@href='/kontakt/']")); //< a href = "/kontakt/" >Kontakt</ a >
                    menuKontakt.Click();
                }
                
                driverNamingWrapper.driverWaitElementPresent(By.Name("email"), 5);
                //webDriver.FindElement(By.Id("Drutten"));
                webDriver.FindElement(By.Name("fname")).SendKeys("Tommy");
                webDriver.FindElement(By.Name("lname")).SendKeys("Pettersson");
                webDriver.FindElement(By.Name("email")).SendKeys("tommy.pettersson@redmind.se");
                IWebElement sendButton = webDriver.FindElement(By.XPath("//input[@type='submit']"));
                Console.WriteLine("Send button activated?: " + sendButton.Enabled.ToString());
                
            }
            catch(Exception e)
            {
                Console.WriteLine(e.Message + "/n/r" + e.Message);
                Assert.Fail(e.ToString());
            }
            finally
            {
                driverNamingWrapper.getDriver().Quit();
            }


        }


    }
}
