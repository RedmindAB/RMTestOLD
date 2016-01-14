using System;
using System.Collections.Generic;
using NUnit.Framework;
using OpenQA.Selenium;
using OpenQA.Selenium.Remote;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RMTest
{
    public abstract class TestBase
    {
        //protected ICollection<Object[]> allDrivers;
        protected DriverNamingWrapper driverNamingWrapper;
	    protected IWebDriver webDriver;

        public static void main()
        {

        }

        private static List<Object> getDrivers()
        {
            return new List<Object>(DriverProvider.getDrivers());
        }

        //@Parameterized.Parameters(name = "{1}")
	    public static ICollection<Object[]> drivers()
        {
            List<Object[]> driverList = new List<object[]>();
            
            //return getDrivers().stream().map(obj-> new Object[] { obj, obj.toString() }).collect(Collectors.toList());
            foreach (var driver in getDrivers())
            {
                driverList.Add(new object[] { driver, driver.ToString() });
            }
            
            return driverList;
        }

        public static IEnumerable<TestCaseData> TestData()
        {
            {
                DriverNamingWrapper dnw;
                foreach (var driver in drivers())
                {
                    dnw = (DriverNamingWrapper)driver[0]; 
                    yield return new TestCaseData(dnw, driver[1])
                        .SetName(dnw.getCapabilities().BrowserName.ToUpper() + ": (" + dnw.getDescription() + ")")
                        .SetCategory("TestCases");
                }
                
            }
        }
        public TestBase()
        {
            
            //this.allDrivers = drivers();
        }

        //public TestBase(DriverNamingWrapper driverWrapper, String driverDescription)
        //{
        //   this.driverNamingWrapper = driverWrapper;
            // this.deviceType = deviceType;
            // this.initi|alUrl = initialUrl;
        //}
    }
}
