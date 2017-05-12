package de.bst.example;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.UUID;

import org.junit.Test;
import org.springframework.http.MediaType;

import de.bst.example.api.ImmutablePeople;
import de.bst.example.api.MediaTypesWithVersion;
import io.restassured.RestAssured;

public class RestEndpointIT {

	static {
		RestAssured.port = Integer.valueOf(System.getProperty("container.port"));
	}

	@Test
	public void test_people_endpoint_get() {
		given().auth().basic("user", "password").when().get("/people/abc-123").then().statusCode(404);
	}

	@Test
	public void test_people_endpoint_post_201() {
		final String id = UUID.randomUUID().toString();
		given().auth().basic("user", "password").request().contentType(MediaTypesWithVersion.PEOPLE_V1_JSON_MEDIATYPE)
				.body(ImmutablePeople.builder().id(id).name("Test").age(10L).build()).when().post("/people").then().statusCode(201)
				.header("location", is(equalTo("/people/" + id)));
	}

	@Test
	public void test_info_endpoint_get_200() {
		given().auth().basic("user", "password").when().get("/info").then().statusCode(200);
	}

	@Test
	public void test_health_endpoint_get_200() {
		given().auth().basic("user", "password").when().get("/health").then().statusCode(200);
	}

	@Test
	public void test_metrics_endpoint_get_200() {
		given().auth().basic("user", "password").when().get("/metrics").then().statusCode(200);
	}

	@Test
	public void test_api_doc_endpoint_get_200() {
		given().when().get("/api.html").then().statusCode(200).contentType(MediaType.TEXT_HTML_VALUE);
	}
}
