using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json.Converters;
using System.Runtime.Serialization;
using System.Net;
using Newtonsoft.Json.Linq;


namespace RMTest
{
	public class NodeInfoFromHub
	{
		public NodeInfoFromHub(){}


		public string makeRequest(String hubHost, int hubPort)
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
		}

//	public void jsonObject(String pHost, int pPort) throws ClientProtocolException, IOException, JSONException {
//		var proxyApi = ("http://" + pHost + ":" + pPort + "/grid/admin/GridQueryServlet");
//		HttpClient client = new DefaultHttpClient();
//		var r = new HttpWebRequest("GET", proxyApi.toExternalForm());
//		HttpHost host = new HttpHost(pHost, pPort);
//		HttpWebResponse response = client.execute(host, r);
//		Assert.assertEquals(200, response.getStatusLine().getStatusCode());
//		JSONObject o = extractObject(response);
//		return o;

//	}

//	private void jsonObject extractObject(HttpWebResponse response)throws IOException, JSONException {
//		BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
//		StringBuilder s = new StringBuilder();
//		String line;
//		while ((line = rd.readLine()) != null) {
//		  s.append(line);
//		}
//		rd.close();
////        System.out.println(s.toString());
//		return new JSONObject(s.toString());
//	  }
	}
}