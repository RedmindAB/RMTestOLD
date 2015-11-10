using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RMTest
{
    class DeviceDescription
    {
    private String os;
    private String osVersion;
    private String device;
    private String browser;
    private String browserVersion;

    public DeviceDescription(String os, String osVersion, String device, String browser, String browserVersion)
        {
            this.os = os;
            this.osVersion = osVersion;
            this.device = device;
            this.browser = browser;
            this.browserVersion = browserVersion;
        }

        public String getOs()
        {
            return os;
        }

        public String getOsVersion()
        {
            return osVersion;
        }

        public String getDevice()
        {
            return device;
        }

        public String getBrowser()
        {
            return browser;
        }

        public String getBrowserVersion()
        {
            return browserVersion;
        }

        public String getDeviceDescription()
        {
            return os + "_" + osVersion + "_" + device + "_" + browser + "_" + browserVersion;
        }
    }
}
