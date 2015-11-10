using System;
using System.Collections;
using System.Collections.Generic;
using OpenQA.Selenium;
using OpenQA.Selenium.Remote;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RMTest
{
    public class RegistrationRequest
    {


        private String id;
        private String name;
        private String description;

        //private GridRole.GridRoleType role;
        private List<DesiredCapabilities> capabilities = new List<DesiredCapabilities>();
        private Dictionary<String, Object> configuration = new Dictionary<string, object>();
        //private Map<String, Object> configuration = new HashMap<>();

        private String[] args;

        //private static readonly Logger LOG = Logger.getLogger(RegistrationRequest.class.getName());

        // some special param for capability
        public static readonly String APP = "applicationName";
        public static readonly String MAX_INSTANCES = "maxInstances";
        // see enum SeleniumProtocol
        public static readonly String SELENIUM_PROTOCOL = "seleniumProtocol";
        public static readonly String PATH = "path";
        public static readonly String BROWSER = CapabilityType.BrowserName;
        public static readonly String PLATFORM = CapabilityType.Platform;
        public static readonly String VERSION = CapabilityType.Version;

        // some special param for config
        public static readonly String REGISTER_CYCLE = "registerCycle";
        public static readonly String PROXY_CLASS = CapabilityType.Proxy;
        public static readonly String CLEAN_UP_CYCLE = "cleanUpCycle";
        // Client timeout
        public static readonly String TIME_OUT = "timeout";
        public static readonly String BROWSER_TIME_OUT = "browserTimeout";

        // TODO delete to keep only HUB_HOST and HUB_PORT
        public static readonly String REMOTE_HOST = "remoteHost";

        public static readonly String MAX_SESSION = "maxSession";
        public static readonly String AUTO_REGISTER = "register";

        // polling nodes params
        public static readonly String NODE_POLLING = "nodePolling";
        public static readonly String UNREGISTER_IF_STILL_DOWN_AFTER = "unregisterIfStillDownAfter";
        public static readonly String DOWN_POLLING_LIMIT = "downPollingLimit";
        public static readonly String STATUS_CHECK_TIMEOUT = "nodeStatusCheckTimeout";

        public static readonly String MAX_TESTS_BEFORE_CLEAN = "maxTestBeforeClean";
        public static readonly String CLEAN_SNAPSHOT = "cleanSnapshot";

        public static readonly String HOST = "host";
        public static readonly String PORT = "port";

        public static readonly String HUB_HOST = "hubHost";
        public static readonly String HUB_PORT = "hubPort";

        public static readonly String SERVLETS = "servlets";
        public static readonly String ID = "id";

        public RegistrationRequest()
        {
            args = new String[0];
        }

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public List<DesiredCapabilities> getCapabilities()
        {
            return capabilities;
        }

        public void addDesiredCapability(DesiredCapabilities c)
        {
            this.capabilities.Add(c);
        }

        public void addDesiredCapability(Dictionary<String, Object> c)
        {
            this.capabilities.Add(new DesiredCapabilities(c));
        }

        public void setCapabilities(List<DesiredCapabilities> capabilities)
        {
            this.capabilities = capabilities;
        }

        public Dictionary<String, Object> getConfiguration()
        {
            return configuration;
        }

        public void setConfiguration(Dictionary<String, Object> configuration)
        {
            this.configuration = configuration;
        }

        public String toJSON()
        {
            return getAssociatedJSON().ToString();
        }

        public JObject getAssociatedJSON()
        {
            JObject res = new JObject();

            res.Add("class", this.GetType().FullName); //getClass().getCanonicalName());
            res.Add("id", id);
            res.Add("name", name);
            res.Add("description", description);
            res.Add("configuration", new JObject(configuration));
            JArray caps = new JArray();
            foreach (DesiredCapabilities c in capabilities)
            {
                caps.Add(new JObject(c));
            }
            res.Add("capabilities", caps);

            return res;
        }

        public String getConfigAsString(String param)
        {
            Object res = configuration[param]; //.Where(p => p.Key == param);
            return res == null ? null : res.ToString();

        }

        public int getConfigAsInt(String param, int defaultValue)
        {
            Object o = configuration.Where(p => p.Key == param);
            if (o == null)
            {
                return defaultValue;
            }
            if (o.GetType() == typeof(Int32))
            {
                return (Int32)o;
            }
            try
            {
                return Int32.Parse(o.ToString());
            }
            catch (FormatException)
            {
                // LOG.warning
                System.Console.WriteLine(String.Format(
                  "Parameter %s has value '%s', but it is supposed to be an int. Keeping default of %s",
                  param, o, defaultValue));
                return defaultValue;
            }

        }


        /**
        * fixing a backward compatibility issue causing #2738 After 2.9 release, the remoteProxy for a
        * node changed for 2 type of nodes to single node answering both sel1 and webdriver protocol.
        * <p>
        * That means the hub now need to handle registration request containing
        * "url":"http://ip:port/selenium-server/driver" ( < v2.9 , RC ),"url":"http://ip:port/wd/hub"
        * (< v2.9, wb)
        * <p>
        * and "remoteHost":"http://ip:port" ( > v2.9 ).
        *
        * The pre 2.9 registration requests need to be updated and take the "url" config param and
        * generate the "remoteHost" out of it.
        */
        private void ensureBackwardCompatibility()
        {
            // new param after 2.9
            String url = configuration[REMOTE_HOST].ToString();

            if (url != null)
            {
                return;
            }
            else
            {
                // could be a pre 2.9 node
                url = configuration["url"].ToString();
                if (url == null)
                {
                    return;
                }
                else
                {
                    // was a legacy RC node. Needs to set that on the capabilities, as webdriver is the default.
                    if (url.Contains("selenium-server/driver"))
                    {
                        foreach (DesiredCapabilities capability in capabilities)
                        {
                            capability.SetCapability(SELENIUM_PROTOCOL, "Selenium");
                        }
                    }
                    Uri tmp;
                    try
                    {
                        tmp = new Uri(url);
                    }
                    catch (UriFormatException)
                    {
                        throw new UriFormatException("specified URL for the node is invalid :" + url);
                    }
                    configuration.Add(REMOTE_HOST, "http://" + tmp.Host + ":" + tmp.Port);
                }
            }
        }


        /**
         * Create an object from a registration request formatted as a json string.
         *
         * @param json
         * @return create a request from the JSON request received.
         */

        // JSON lib
        public static RegistrationRequest getNewInstance(String json)
        {
            RegistrationRequest request = new RegistrationRequest();
            try
            {
                JObject o = JObject.Parse(json);

                if (o.GetValue("id") != null) request.setId(o.GetValue("id").ToString());
                if (o.GetValue("name") != null) request.setName(o.GetValue("name").ToString());
                if (o.GetValue("description") != null) request.setDescription(o.GetValue("description").ToString());

                /*** CONFIGURATION ***/
                JObject config = (JObject)o.GetValue("configuration"); //.getAsJsonObject();
                Dictionary<String, Object> configuration = (Dictionary<String, Object>)config.Values(); // new JsonToBeanConverter().convert(Dictionary.class, config.);
                //Dictionary<String, Object> configuration = new Dictionary<string, object>();
                // For backward compatibility numbers should be converted to integers
                foreach (KeyValuePair<String, Object> keyValuePair in configuration)
                {
                    String key = keyValuePair.Key;
                    Object value = keyValuePair.Value;
                    if (value.GetType() == typeof(long))
                    {
                        configuration.Add(key, (int)value);
                    }
                }
                request.setConfiguration(configuration);

                /*** CAPABILITIES ***/
                JArray capabilities = (JArray)o.GetValue("capabilities");
                foreach (var capability in capabilities)//(int i = 0; i < capabilities.Count; i++)
                {
                    DesiredCapabilities cap = (DesiredCapabilities)capability.Values();//new JsonToBeanConverter().convert(DesiredCapabilities.class, capabilities.get(i));
                
                    request.capabilities.Add(cap);
                }

                request.ensureBackwardCompatibility();
                return request;
                } catch (JsonException) {
                    // Check if it was a Selenium Grid 1.0 request.
                    return parseGrid1Request(json);
                }
            }

  /**
   * if a PROXY_CLASS is specified in the request, the proxy created following this request will be
   * of that type. If nothing is specified, it will use RemoteProxy
   *
   * @return null if no class was specified.
   */
  public String getRemoteProxyClass()
{
    Object o = getConfiguration()[PROXY_CLASS];
    return o == null ? null : o.ToString();
}

private static RegistrationRequest parseGrid1Request(String clientRequest)
{
            // Check if it's a Selenium Grid 1.0 node connecting.
            // If so, the string will be of the format:
            // host=localhost&port=5000&environment=linux_firefox_3_6
            Dictionary<String, String> registrationInfo = new Dictionary<String, String>(); // Maps.newHashMap();

    // Attempt to parse the client request string.
    String[] parts = clientRequest.Split('&');
    foreach (String part in parts)
    {
        String[] configItem = part.Split('=');

        // Do some basic taint checking so we can exit early if it's not
        // really a key=value pair.
        if (configItem.Length != 2)
        {
                    throw new System.ArgumentOutOfRangeException(configItem.ToString(), "ParseGrid1Request failed to parse querystring");// InvalidParameterException();
        }

        try
        {
            registrationInfo.Add(System.Uri.EscapeDataString(configItem[0]), System.Uri.EscapeDataString(configItem[1]));
        }
        catch (Exception e) when (e is UriFormatException)// UnsupportedEncodingException e)
        {
           Console.WriteLine(String.Format("Unable to decode registration request portion: %s", part));
        }
    }

    // Now validate the query string.
    if ((registrationInfo["port"] != null) && (registrationInfo["environment"] != null))
    {
        RegistrationRequest request = new RegistrationRequest();

        Dictionary<String, Object> configuration = new Dictionary<string, object>(); // Maps.newHashMap();
        configuration.Add(SELENIUM_PROTOCOL, "Selenium");
        configuration.Add(REMOTE_HOST, String.Format("http://%s:%s", registrationInfo["host"], registrationInfo["port"]));
        request.setConfiguration(configuration);

        DesiredCapabilities cap = new DesiredCapabilities();
        // cap.put(CapabilityType.PLATFORM, "LINUX");
        // TODO freynaud envt or browser ?
        cap.SetCapability(BROWSER, registrationInfo["environment"]);
        cap.SetCapability("environment", registrationInfo["environment"]);
        request.capabilities.Add(cap);

        return request;
    }
    else
    {
                    throw new System.ArgumentException("RegistrationRequest: Registration info for 'Port' or 'environment' is null"); //InvalidParameterException();
    }
    //return new RegistrationRequest();
}


/*        public static RegistrationRequest build(String[] args)
        {
            RegistrationRequest res = new RegistrationRequest();
            res.args = args;

            CommandLineOptionHelper helper = new CommandLineOptionHelper(args);

            res.role = GridRole.find(args);

            String defaultConfig = "defaults/DefaultNode.json";
            String nodeType = helper.getParamValue("-role");
            if (GridRole.isRC(nodeType))
            {
                defaultConfig = "defaults/DefaultNodeSelenium.json";
            }
            if (GridRole.isWebDriver(nodeType))
            {
                defaultConfig = "defaults/DefaultNodeWebDriver.json";
            }

            res.loadFromJSON(defaultConfig);

            // -file *.json ?
            if (helper.isParamPresent("-nodeConfig"))
            {
                String value = helper.getParamValue("-nodeConfig");
                res.loadFromJSON(value);
            }

            // from command line
            res.loadFromCommandLine(args);

            for (DesiredCapabilities cap : res.capabilities)
            {
                if (SeleniumProtocol.Selenium.toString().equals(cap.getCapability(SELENIUM_PROTOCOL)))
                {
                    if (!BrowserLauncherFactory.isBrowserSupported(cap.getBrowserName()))
                    {
                        throw new GridConfigurationException("browser " + cap.getBrowserName()
                                                             + " is not supported, supported browsers are:\n"
                                                             + BrowserLauncherFactory.getSupportedBrowsersAsString());
                    }
                }
                if (cap.getCapability(SELENIUM_PROTOCOL) == null)
                {
                    cap.setCapability(SELENIUM_PROTOCOL,
                      GridRole.isRC(nodeType)
                        ? SeleniumProtocol.Selenium.toString() : SeleniumProtocol.WebDriver.toString());
                }
            }

            res.configuration.put(HOST, guessHost((String)res.configuration.get(HOST)));
            res.configuration.put(HUB_HOST, guessHost((String)res.configuration.get(HUB_HOST)));

            // some values can be calculated.
            if (res.configuration.get(REMOTE_HOST) == null)
            {
                String url = "http://" + res.configuration.get(HOST) + ":" + res.configuration.get(PORT);
                res.configuration.put(REMOTE_HOST, url);
            }

            // The hub in < v2.9 expects a "url" param, not "remoteHost".  While the configuration option was updated to
            // reflect its new intent, they're logically equivalent for the purposes of setting the proxy ID.  I.e., the old hub
            // used the "url" value for the proxy ID, while the new one uses "remoteHost".  So, just set "url" to be "remoteHost"
            // to make things work fine with older hubs.
            res.configuration.put("url", res.configuration.get(REMOTE_HOST));

            String u = (String)res.configuration.get("hub");
            if (u != null)
            {
                try
                {
                    URL ur = new URL(u);
                    res.configuration.put(HUB_HOST, ur.getHost());
                    //If port was not defined after -hub default it to 4444
                    int port = ur.getPort();
                    if (port == -1)
                    {
                        port = 4444;
                        LOG.info("No port was provided in -hub. Defaulting hub port to 4444");
                    }
                    res.configuration.put(HUB_PORT, port);
                }
                catch (MalformedURLException e)
                {
                    throw new GridConfigurationException("the specified hub is not valid : -hub " + u);
                }
            }

            return res;
        }
        */

        /*        private static String guessHost(String host)
                {
                    if ("ip".Equals(host.ToLower()) // equalsIgnoreCase(host))
            {
                        NetworkUtils util = new NetworkUtils();
                        return util.getIp4NonLoopbackAddressOfThisMachine().getHostAddress();
                    }
                    else if ("host".equalsIgnoreCase(host))
                    {
                        NetworkUtils util = new NetworkUtils();
                        return util.getIp4NonLoopbackAddressOfThisMachine().getHostName();
                    }
                    else
                    {
                        return host;
                    }
                }
        */

/*        private void loadFromCommandLine(String[] args)
{
    CommandLineOptionHelper helper = new CommandLineOptionHelper(args);

    // storing them all.
    List < String > params = helper.getKeys();
    for (String param : params)
    {
        String value = helper.getParamValue(param);
        try
        {
            int i = Integer.parseInt(value);
            configuration.put(param.replaceFirst("-", ""), i);
        }
        catch (NumberFormatException e)
        {
            configuration.put(param.replaceFirst("-", ""), value);
        }
    }
    // handle the core config, do a bit of casting.
    // handle the core config, do a bit of casting.
    if (helper.isParamPresent("-hubHost"))
    {
        configuration.put(HUB_HOST, helper.getParamValue("-hubHost"));
    }
    if (helper.isParamPresent("-" + HUB_PORT))
    {
        configuration.put(HUB_PORT, Integer.parseInt(helper.getParamValue("-" + HUB_PORT)));
    }
    if (helper.isParamPresent("-host"))
    {
        configuration.put(HOST, helper.getParamValue("-host"));
    }
    if (helper.isParamPresent("-port"))
    {
        configuration.put(PORT, Integer.parseInt(helper.getParamValue("-port")));
    }
    if (helper.isParamPresent("-cleanUpCycle"))
    {
        configuration.put(CLEAN_UP_CYCLE, Integer.parseInt(helper.getParamValue("-cleanUpCycle")));
    }
    if (helper.isParamPresent("-timeout"))
    {
        configuration.put(TIME_OUT, Integer.parseInt(helper.getParamValue("-timeout")));
    }
    if (helper.isParamPresent("-browserTimeout"))
    {
        configuration.put(BROWSER_TIME_OUT, Integer.parseInt(helper.getParamValue("-browserTimeout")));
    }
    if (helper.isParamPresent("-maxSession"))
    {
        configuration.put(MAX_SESSION, Integer.parseInt(helper.getParamValue("-maxSession")));
    }
    if (helper.isParamPresent("-" + AUTO_REGISTER))
    {
        configuration.put(AUTO_REGISTER,
            Boolean.parseBoolean(helper.getParamValue("-" + AUTO_REGISTER)));
    }

    if (helper.isParamPresent("-servlets"))
    {
        configuration.put(SERVLETS, helper.getParamValue("-servlets"));
    }

    // capabilities parsing.
    List<String> l = helper.getAll("-browser");

    if (!l.isEmpty())
    {
        capabilities.clear();
        for (String s : l)
        {
            DesiredCapabilities c = addCapabilityFromString(s);
            capabilities.add(c);
        }
    }

    addPlatformInfoToCapabilities();
}
*/

private DesiredCapabilities addCapabilityFromString(String capability)
{
    Console.WriteLine("Adding " + capability); // LOG.info("Adding " + capability);
    String[] s = capability.Split(',');
    if (s.Length == 0)
    {
        //throw new GridConfigurationException("-browser must be followed by a browser description");
          throw new Exception(" GridConfigurationException: -browser must be followed by a browser description");
            }
    DesiredCapabilities res = new DesiredCapabilities();
    foreach (String capabilityPair in s)
    {
        capabilityPair.Trim();
        if (capabilityPair.Split('=').Length != 2)
        {
            throw new Exception ("GridConfigurationException: -browser format is key1=value1,key2=value2... '" + capabilityPair + "' doesn't follow that format.");
        }
        String key = capabilityPair.Split('=')[0];
        String value = capabilityPair.Split('=')[1];
        res.SetCapability(key, value);
    }

    if (res.BrowserName == null)
    {
        throw new Exception(
            "GridConfigurationException: You need to specify a browserName using browserName=XXX");
    }
    return res;
}

private void addPlatformInfoToCapabilities()
{
    Platform current = Platform.CurrentPlatform;
    foreach (DesiredCapabilities cap in capabilities)
    {
        if (cap.Platform == null)
        {
            cap.Platform = current;
        }
    }
}

/**
 * add config, but overwrite capabilities.
 *
 * @param resource
 */
public void loadFromJSON(String resource)
{
    try
    {
        JObject Base = JObject.Parse(resource);

        if (Base["capabilities"] != null)
        {
            capabilities = new List<DesiredCapabilities>();//ArrayList<>();
            JArray caps = (JArray)Base["capabilities"];
            foreach (var cap in caps)
            {
                DesiredCapabilities c = (DesiredCapabilities)cap.Values() ;//new JsonToBeanConverter().convert(DesiredCapabilities.class, a.get(i));
                capabilities.Add(c);
            }
        addPlatformInfoToCapabilities();
        }

      JObject o = (JObject)Base["configuration"];
      foreach (var entry in o) {
                    //Object value = new JsonToBeanConverter().convert(Object.Class, entry.getValue());
                    String key = entry.Key;
                    object value = entry.Value;
        // For backward compatibility numbers should be converted to integers
        if (value.GetType() == typeof(long)) {
          value = (int)value;
        }
        configuration.Add(key, value);
      }

    }
    catch (Exception e)
    {
      throw new Exception ("GridConfigurationException: Error with the JSON of the config : " + e.Message, e);
    }
  }

/*public GridRole.GridRoleType getRole()
{
    return role;
}
*/

/*public void setRole(GridRole.GridRoleType role)
{
    this.role = role;
}
*/

        /*public RemoteControlConfiguration getRemoteControlConfiguration()
        {
            List < String > params = new ArrayList<>();
            for (String key : configuration.keySet())
            {
              params.add("-" + key);

                if (!configuration.get(key).toString().trim().isEmpty())
                {
                params.add("" + configuration.get(key));
                }
            }
            return RemoteControlLauncher.parseLauncherOptions(params.toArray(new String[params.size()]));
        }
        */

public String[] getArgs()
{
    return args;
}

/**
 * Validate the current setting and throw a config exception is an invalid setup is detected.
 *
 * @throws GridConfigurationException
 */
public void validate() // throws GridConfigurationException
{
    String hub = (String) configuration[HUB_HOST];
    int? port = (int?)configuration[HUB_PORT];
    if (hub == null || port == null)
    {
        throw new Exception ("GridConfigurationException: You need to specify a hub to register to using -"
            + HUB_HOST + " X -" + HUB_PORT + " 5555. The specified config was -" + HUB_HOST + " "
            + hub + " -" + HUB_PORT + " " + port);
    }
    }



}
}
