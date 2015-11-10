using System;
using OpenQA.Selenium.Remote;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RMTest
{
    class DescriptionBuilder
    {
        //private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param capabilities
     * @return
     */
    public String buildDescriptionFromCapabilities(DesiredCapabilities capabilities)
        {
            String os = getOS(capabilities);
            String osVer = getOsVersion(capabilities);
            String device = getDevice(capabilities);
            String browser = getBrowser(capabilities);
            String browserVersion = "UNKNOWN";

            String description = new DeviceDescription(os, osVer, device, browser, browserVersion).getDeviceDescription();
            //logger.debug("Description of driver is: " + description);
            return description;
        }

        private String getBrowser(DesiredCapabilities capability)
        {
            String browser;
            if (isCapabilitySet("browserName", capability))
            {
                browser = getSafeCapability("browserName", capability);
            }
            else if (isCapabilitySet("appPackage", capability))
            {
                browser = getSafeCapability("appPackage", capability);
            }
            else
            {
                browser = "UNKNOWN";
            }
            return browser;
        }

        private String getDevice(DesiredCapabilities capability)
        {
            String device;
            if (isCapabilitySet("deviceName", capability))
            {
                device = getSafeCapability("deviceName", capability);
            }
            else
            {
                device = "UNKNOWN";
            }
            return device;
        }

        private String getOsVersion(DesiredCapabilities capability)
        {
            String osVer;
            if (isCapabilitySet("platformVersion", capability))
            {
                osVer = getSafeCapability("platformVersion", capability);
            }
            else
            {
                osVer = "UNKNOWN";
            }
            return osVer;
        }

        private String getOS(DesiredCapabilities capability)
        {
            String os;
            if (isCapabilitySet("rmOsName", capability))
            {
                os = getSafeCapability("rmOsName", capability);
            }
            else if (isCapabilitySet("platformName", capability))
            {
                os = getSafeCapability("platformName", capability);
            }
            else if (isCapabilitySet("osname", capability))
            {
                os = getSafeCapability("osname", capability);
            }
            else if (isCapabilitySet("platform", capability))
            {
                os = getSafeCapability("platform", capability);
            }
            else
            {
                os = "UNKNOWN";
            }
            return os;
        }

        private Boolean isCapabilitySet(String capName, DesiredCapabilities currentCapability)
        {
            return !String.IsNullOrEmpty(getSafeCapability(capName, currentCapability));
        }

        private String getSafeCapability(String capName, DesiredCapabilities currentCapability)
        {
            Object currCap = currentCapability.GetCapability(capName);
            if (currCap != null)
            {
                return currCap.ToString();
            }
            else
            {
                return "";
            }
            
        }

    }
}
