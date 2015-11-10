using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RMTest
{
    class TestParams
    {
        private static String testBaseUrl;
        public static String rmDeviceStype = "desktop";

        public static void setBaseUrl(String baseUrl)
        {
            testBaseUrl = baseUrl;
        }

        public static String getBaseUrl()
        {
            String baseUrl;
            if (testBaseUrl == null)
            {
                baseUrl = "http://www.aftonbladet.se/";
            }
            else
            {
                baseUrl = testBaseUrl;
            }
            return baseUrl;
        }
    }
}
