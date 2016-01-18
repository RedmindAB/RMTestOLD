package se.redmind.rmtest.cucumber.rest;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;
import com.jayway.restassured.specification.RequestSpecification;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.junit.Cucumber;

import static org.junit.Assert.*;

public class RestStep {
	
	private ValidatableResponse vResponse;
	private Response response;
	private String content;
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

	@Then("^parameter \"([^\"]*)\" has the value \"([^\"]*)\"$")
	public void object_has_the_value(String query, String value) throws Throwable {
		vResponse.body(query, is(value));
	}
	
}
