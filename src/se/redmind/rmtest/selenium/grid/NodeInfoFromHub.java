package se.redmind.rmtest.selenium.grid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHttpRequest;
import org.junit.Assert;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
//import org.json.JSONException;
//import org.json.JSONObject;

public class NodeInfoFromHub {
//    static String hubHost = "localhost";
//    static int hubPort = 4444;

    public static JsonObject main(String pHost, int pPort) throws ClientProtocolException, IOException {
        URL proxyApi = new URL("http://" + pHost + ":" + pPort + "/grid/admin/GridQueryServlet");
        HttpClient client = HttpClientBuilder.create().build();
        BasicHttpRequest r = new BasicHttpRequest("GET", proxyApi.toExternalForm());
        HttpHost host = new HttpHost(pHost, pPort);
        HttpResponse response = client.execute(host, r);
        Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        JsonObject o = extractObject(response);
        return o;
    }

    private static JsonObject extractObject(HttpResponse resp) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
        StringBuilder s = new StringBuilder();
        String line;
        while((line = rd.readLine()) != null) {
            s.append(line);
        }
        rd.close();
        return new Gson().fromJson(s.toString(), JsonObject.class);
    }
}