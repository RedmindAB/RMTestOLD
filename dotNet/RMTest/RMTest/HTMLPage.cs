using System;
using System.Threading;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using OpenQA.Selenium;
using OpenQA.Selenium.Support.UI;
using OpenQA.Selenium.Support;
using NUnit.Framework;

namespace RMTest
{
    class HTMLPage
    {
        protected IWebDriver driver;

        /**
         * @param driver WebDriver
         */
        public HTMLPage(IWebDriver pDriver)
        {
            this.driver = pDriver;
        }

        /**
         * @return WebDriver
         */
        public IWebDriver getDriver()
        {
            return this.driver;
        }

        /**
         * @param timeoutInSeconds int
         * @return
         */
        private WebDriverWait driverWait(int timeoutInSeconds)
        {
            return new WebDriverWait(this.driver, System.TimeSpan.FromSeconds((Double)timeoutInSeconds));
        }

        //public FluentWait<IWebDriver> driverFluentWait(int timeoutInSeconds)
        //{
        //    FluentWait<IWebDriver> fw = null;
        //    int i = 0;
        //    //PrintStream quietErr;
        //    while (i < 10)
        //    {
        //        try
        //        {
        //            /*
        //             * Stops prinouts of ExpectedConditions.findElement()
        //             * This can be removed when printouts are removed
        //             */
        //            Logger.getLogger("org.openqa.selenium.support.ui.ExpectedConditions").setLevel(Level.SEVERE);

        //            fw = new FluentWait<>(this.driver).withTimeout(timeoutInSeconds, TimeUnit.SECONDS);
        //            fw.ignoring(WebDriverException.class, ClassCastException.class);
        //            fw.ignoring(NoSuchElementException.class);
        //            return fw;
        //        }
        //        catch (Exception e) {
        //            if (i >= 9) {
        //                System.Console.WriteLine("driverFluentWait Failed attempt : " + i + "/n" + e);
        //            }
        //            i++;
        //        }
        //    }
        //    if (fw == null) {
        //        throw new WebDriverException("driverFluentWait failed after ten attempts");
        //    }
        //    else
        //    {
        //        return fw;
        //    }
        //}


        /**
         * @param locator
         * @param timeoutInSeconds
         */
        public void driverWaitClickable(By locator, int timeoutInSeconds)
        {
            int i = 0;
            while (i < 10)
            {
                try
                {
                    driverWait(timeoutInSeconds).Until(ExpectedConditions.ElementToBeClickable(locator));
                    // driverFluentWait(timeoutInSeconds).until(ExpectedConditions.ElementToBeClickable(locator));   // changed to driverFluentWait to ignore WebDriverExceptions braking the wait
                    break;
                }
                catch (Exception e)
                {
                    if (i >= 9)
                    {
                        System.Console.WriteLine("driverWaitClickable exception: " + e);
                    }
                    i++;
                }
            }
        }

        /**
         * @param locator
         * @param timeoutInSeconds
         */
        //public bool driverFluentWaitForCondition(ExpectedCondition<?> condition, int timeoutInSeconds)
        //{
        //    int i = 0;
        //    while (i < 10)
        //    {
        //        try
        //        {
        //            driverFluentWait(timeoutInSeconds).Until(condition);   // changed to driverFluentWait to ignore WebDriverExceptions braking the wait
        //            return true;
        //        }
        //        catch (WebDriverException e)
        //        {
        //            System.Console.WriteLine("Caught a webdriveresception on driverFluentWaitForCondition try: " + i);
        //            System.Console.WriteLine(e);
        //            i++;
        //        }
        //        catch (Exception e)
        //        {
        //            if (i >= 9)
        //            {
        //                System.Console.WriteLine("This is another exception?" + e);
        //            }
        //            i++;
        //        }
        //    }
        //    return false;
        //}

        /**
         * @param pBy
         * @param timeoutInSeconds
         */
        public void driverWaitElementPresent(By pBy, int timeoutInSeconds)
        {
            driverWait(timeoutInSeconds).Until(ExpectedConditions.PresenceOfAllElementsLocatedBy(pBy));
        }

        /**
         * NB: might not work as expected: the predicate passed to until seems to be called once, and only once.
         */
        //protected void waitUntilDomReady() //throws Exception
        //{
        //    driverFluentWait(45).until((org.openqa.selenium.WebDriver webDriver) -> {
        //        JavascriptExecutor js = (JavascriptExecutor)webDriver;
        //        String result = (String)js.executeScript("return document.readyState");
        //        return "complete".equalsIgnoreCase(result);
        //    });
        //}

        public void assertPageTitle(String expPageTitle) //throws Exception
        {
            System.Console.WriteLine("Try to assert page title: " + expPageTitle);
            String expPageTitleLow = expPageTitle.ToLower();
            String pageTitle = "--- Page not loaded ---";
            int i = 0;
            while (i < 10)
            {
                try
                {
                    driverWait(6).Until(ExpectedConditions.TitleContains(expPageTitle));
                    pageTitle = driver.Title.ToLower();
                    System.Console.WriteLine(">>>Compare to page title: " + pageTitle);  // pageTitle
                    break;
                }
                catch (Exception e)
                {
                    if (i >= 9)
                    {
                        System.Console.WriteLine("pageTitle: " + pageTitle);
                        System.Console.WriteLine("----- AssertPageTitle Exception: " + e);
                    }
                    i = i + 1;
                    Thread.Sleep(50);
                }
            }
            Assert.IsTrue(pageTitle.Contains(expPageTitleLow)); //AssertTrue(pageTitle.contains(expPageTitleLow));
        }

        public bool pageTitleContains(String expPageTitle) //throws Exception
        {
            System.Console.WriteLine("Try to assert page title: " + expPageTitle);
                int i = 0;
                while (i < 10) {
                try
                {
                    System.Console.WriteLine(">>>Compare to page title: " + driver.Title);
                    bool b = driver.Title.Contains(expPageTitle);
                    return b;
                }
                catch (Exception e)
                {
                    if (i >= 9)
                    {
                        System.Console.WriteLine("pageTitleContains exception: " + e);
                    }
                    i = i + 1;
                    Thread.Sleep(50);
                }
            }
                return false;
        }

        public bool pageUrlContains(String articleId) //throws Exception
        {
            System.Console.WriteLine("Try to assert page url: " + articleId);
            int i = 0;
            while (i < 10)
            {
                    try
                    {
                        System.Console.WriteLine(">>>Compare to page url: ");   // TODO: concatenate articleId
                        bool b = driver.Title.Contains(articleId);
                        return b;
                    }
                    catch (Exception e)
                    {
                        if (i >= 9)
                        {
                            System.Console.WriteLine("pageTitleContains exception: " + e);
                        }
                        i = i + 1;
                        Thread.Sleep(50);
                    }
            }
            return false;
        }

        public void assertPageContains(By locator, String expText) //throws Exception
        {
            System.Console.WriteLine("Try to assert page contains: " + expText);

            int i = 0;
            while (i < 10)
            {
                try
                {
                    driverWait(1).Until(ExpectedConditions.TextToBePresentInElementLocated(locator, expText));
                    driver.FindElement(locator).Text.Contains(expText);
                    break;
                }
                catch (Exception e)
                {
                    if (i >= 9)
                    {
                        System.Console.WriteLine("----- assertPageContains Exception: " + e);
                    }
                    i = i + 1;
                    Thread.Sleep(50);
                }
            }
            Assert.IsTrue(driver.FindElement(locator).Text.Contains(expText));
        }

        public void spinnerClickBy(By path) //throws Exception
        {
            System.Console.WriteLine("By: " + path);
            IWebElement menuItem;
                int i = 0;
                while (i < 10) {
                try
                {
                    //menuItem = driver.FindElement(path);
                    //menuItem.Location;

                            //List<IWebElement> menuItems = new List<IWebElement>();
                            //menuItems.Add(menuItem);
                            //ReadOnlyCollection<IWebElement> webElements = new ReadOnlyCollection<IWebElement>(menuItems);

                    driverWait(1).Until(ExpectedConditions.ElementIsVisible(path));
                    //menuItem.Location;
                    menuItem = driver.FindElement(path);
                    menuItem.Click();
                    break;
                }
                catch (Exception e)
                {
                    if (i >= 9)
                    {
                        System.Console.WriteLine("spinnerClickBy exception: " + e);
                    }
                    i = i + 1;
                    Thread.Sleep(50);
                }
            }
        }

        public void navigateStartUrl()
        {
            String bUrl = TestParams.getBaseUrl();
            driver.Url = bUrl;
            //driverFluentWait(10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//footer")));
        }

        public String getTitle()
        {
            return driver.Title;
        }

    /**
     * @param fileName Path to filename without extension. example: /tmp/thefilename
     */
    }
}
