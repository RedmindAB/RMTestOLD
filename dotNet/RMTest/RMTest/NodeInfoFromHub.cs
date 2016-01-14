using System;
using System.IO;
using System.Net;
using NUnit.Framework;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json.Converters;
using System.Runtime.Serialization;
using Newtonsoft.Json.Linq;


namespace RMTest
{
	public class NodeInfoFromHub
	{
		public NodeInfoFromHub(){}


        /*		public string makeRequest(String hubHost, int hubPort)
                {
                    var url = ("http://" + hubHost + ":" + hubPort + "/grid/admin/GridQueryServlet");
                    WebResponse objResponse;
                    WebRequest objRequest = HttpWebRequest.Create(url);
                    objResponse = objRequest.GetResponse();
                    using(StreamReader reader = new StreamReader(objResponse.GetResponseStream()))
                    {
                        string json = reader.ReadToEnd();
                        //JArray jArray = JArray.Parse(json);

                        //Here's where I'm stumped
                        //foreach(JObject o in jArray.Children<JObject>())
                        //{
                        //	foreach(JProperty p in o.Properties())
                        //	{
                        //		string name = p.Name;
                        //		string value = p.Value.ToString();
                        //		Console.WriteLine(name + ": " + value);
                        //	}
                        //}		
                        return json;
                    }
                }
        */

        //var url = ("http://" + hubHost + ":" + hubPort + "/grid/admin/GridQueryServlet");

        //try
        //{
        //	var request = WebRequest.Create(url) as HttpWebRequest;
        //	if (request != null)
        //		using (var response = request.GetResponse() as HttpWebResponse)
        //		{
        //			if (response != null && response.StatusCode != HttpStatusCode.OK)
        //			{
        //				throw new Exception(String.Format("Server error (HTTP {0}: {1}).", response.StatusCode,
        //					response.StatusDescription));
        //			}

        //			//var jsonSerializer = new DataContractJsonSerializer(typeof(TResponse));
        //			if (response != null)
        //			{
        //				object objResponse = /*jsonSerializer.ReadObject(*/ response.GetResponseStream() /*)*/;
        //				var jsonResponse = objResponse as TResponse;
        //				return jsonResponse;
        //			}
        //		}
        //	else
        //	{
        //		Console.WriteLine("tiddelipom");
        //	}
        //}
        //catch (Exception e)
        //{
        //	Console.WriteLine(e.Message);
        //	return null;
        //}



        public static JObject main(String pHost, int pPort)
        {
            Uri uri = new Uri("http://" + pHost + ":" + pPort + "/grid/admin/GridQueryServlet");
            HttpWebRequest request = HttpWebRequest.CreateHttp(uri); //   "GET", proxyApi.ToExternalForm());
            //HttpHost host = new HttpHost(pHost, pPort);
            HttpWebResponse response = (HttpWebResponse)request.GetResponse(); //client.execute(host, httpReq);
            Assert.AreEqual(HttpStatusCode.OK, response.StatusCode);
        	JObject o = extractObject(response);
            return o;
        }

        private static JObject extractObject(HttpWebResponse response)
        {
            StreamReader sr = new StreamReader(response.GetResponseStream()); //   getEntity().getContent()));
            StringBuilder s = new StringBuilder();
            String line;
    		while ((line = sr.ReadLine()) != null) {
    		  s.Append(line);
    		}
            sr.Close();
            Console.WriteLine(s.ToString());
            return JObject.Parse(s.ToString());
    		//return new JObject(s.ToString());
    	}
    }
}