package de.bst.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import de.bst.example.api.People;
import de.bst.example.rest.PeopleRest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PeopleApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void init() {
		this.restTemplate = this.restTemplate.withBasicAuth("user", "password");
	}

	@Test
	public void test_people_endpoint_get_200() {
		// Given
		String id = UUID.randomUUID().toString();

		// When
		ResponseEntity<People> entity = this.restTemplate.getForEntity(PeopleRest.GET_PEOPLE.replace("{id}", id),
				People.class);

		// Then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void test_info_endpoint_get_200() {
		// When
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> entity = this.restTemplate.getForEntity("/info", Map.class);

		// Then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void test_health_endpoint_get_200() {
		// When
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> entity = this.restTemplate.getForEntity("/health", Map.class);

		// Then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void test_metrics_endpoint_get_200() {
		// When
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> entity = this.restTemplate.getForEntity("/metrics", Map.class);

		// Then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}
