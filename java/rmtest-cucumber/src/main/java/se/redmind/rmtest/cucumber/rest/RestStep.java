package se.redmind.rmtest.cucumber.rest;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;
import com.jayway.restassured.specification.RequestSpecification;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.math.NumberUtils;

public class RestStep {
	
	private ValidatableResponse vResponse;
	private Response response;
	private RequestSpecification requestSpecification;
	
	public RestStep() {
		requestSpecification = given();
	}
	
	@Given("^that url is \"([^\"]*)\"$")
	public void that_url_is(String url) throws Throwable {
		requestSpecification.baseUri(url);
	}

	@Given("^port is (\\d+)$")
	public void port_is(int port) throws Throwable {
		requestSpecification.port(port);
	}
	
	@Given("^we get \"([^\"]*)\"$")
	public void we_get(String path) throws Throwable {
		response = requestSpecification.get(path);
		this.vResponse = response.then();
	}
	
	@Given("^we send param \"([^\"]*)\" with ?(?:value)? \"([^\"]*)\"$")
	public void we_send_param_with_value(String key, String value) throws Throwable {
		requestSpecification.param(key, value);
	}
	
	@Given("^we send:$")
	public void we_send(String content) throws Throwable {
		requestSpecification = requestSpecification.body(content);
	}

	@Then("^status is (\\d+)$")
	public void status_is(int status) throws Throwable {
		vResponse.assertThat().statusCode(status);
	}
	
	@Then("^content is \"([^\"]*)\"$")
	public void content_is(String contentContains) throws Throwable {
		String body = response.body().asString();
		assertEquals(contentContains, body);
	}
	
	@Then("^json key \"([^\"]*)\" is \"([^\"]*)\"$")
	public void json_key_is(String key, String value) throws Throwable {
		vResponse.body(key, is(value));
	}
	
	@Then("^header \"([^\"]*)\" is \"([^\"]*)\"$")
	public void header_is(String headerKey, String headerValue) throws Throwable {
		vResponse.header(headerKey, headerValue);
	}
	
	@Then("^index (\\d+) has the key \"([^\"]*)\" and value \"([^\"]*)\"$")
	public void index_has_the_key_and_value(int index, String key, String value) throws Throwable {
		vResponse.body("["+index+"]."+key, is(value));
	}

	@Then("^parameter \"([^\"]*)\" (?:is?( not)?) \"?([^\"]*|\\d+.\\d+|\\d+)?\"?$")
	public void object_has_the_value(String query, Boolean not, String value) throws Throwable {
		not = not == null ? false : not;
		if(NumberUtils.isNumber(value)){
			if(value.contains(".")){
				vResponse.body(query, equalTo(Float.valueOf((String)value)));
			}
			else{
				vResponse.body(query, equalTo(Integer.valueOf((String)value)));
			}
		}
		else {
			vResponse.body(query, equalTo(value));
		}
	}
	
	@Then("^size of \"([^\"]*)\" is (\\d+)$")
	public void array_of_size_is(String key, int size) throws Throwable {
		vResponse.body(key, hasSize(size));
	}
	
	@Then("^time is below (\\d+) ?(milliseconds|seconds)?$")
	public void time_is_below_milliseconds(int maxTime, TimeUnit timeUnit) throws Throwable {
		if(timeUnit == null){
			timeUnit = TimeUnit.MILLISECONDS;
		}
		long time = response.timeIn(timeUnit);
		assertTrue("the request took "+time+" "+timeUnit+" and the timeout was "+maxTime, time <= maxTime);
	}
	
}
