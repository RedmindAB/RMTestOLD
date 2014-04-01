using System;
using OpenQA.Selenium;
using OpenQA.Selenium.Remote;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using RMTest;


namespace RMTest.Tests 
{
	[TestClass]
	public class UnitTest1
	{
		[TestMethod]
		public void NavigateToUrlWithFirstDriver()
		{
			DriverNamingWrapper driverWrapper =	DriverProvider.getFirstDriver();

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
			//testStuffsHere
		}
	}
}