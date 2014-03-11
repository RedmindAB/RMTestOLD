using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;


namespace RMTest.Tests 
{
	[TestClass]
	public class UnitTest1
	{
		[TestMethod]
		public void TestMethod1()
		{
			DriverProvider.getDrivers();
		}
	}
}