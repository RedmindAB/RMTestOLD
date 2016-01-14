using OpenQA.Selenium.Remote;
using System;

namespace RMTest
{
    public interface DriverConfig
    {
        bool eval(DesiredCapabilities capabilities, String description);

        void config(DesiredCapabilities capabilities);
    }
}
