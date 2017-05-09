package de.bst.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import de.bst.example.api.ImmutablePeople;
import de.bst.example.api.MediaTypesWithVersion;
import de.bst.example.api.People;
import de.bst.example.rest.PeopleRest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PeopleApplicationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void init() {
		this.restTemplate = this.restTemplate.withBasicAuth("user", "password");
	}

	@Sql(
			statements = "insert into People_Entity(id,name,age,created,updated) values('abc-123','Test',10,'2017-04-19 12:23:44','2017-04-19 12:23:44')")
	@Test
	public void test_people_endpoint_get_json_200() {
		// Given
		final String id = "abc-123";
		final People people = ImmutablePeople.builder().id(id).name("Test").age(10L)
				.created(ZonedDateTime.of(2017, 4, 19, 12, 23, 44, 0, ZoneId.systemDefault()).toInstant()).build();

		// When
		final People entity = this.restTemplate.getForObject(PeopleRest.URL_PEOPLE_W_ID, People.class, id);

		// Then
		assertThat(entity).isEqualTo(people);
	}

	@Test
	public void test_people_endpoint_get_atom_200() {

		// When
		final ResponseEntity<String> entity = this.restTemplate.getForEntity(PeopleRest.URL_PEOPLE, String.class);

		// Then
		assertThat(entity.getHeaders().getContentType().getType()).isEqualTo(MediaType.APPLICATION_ATOM_XML.getType());
		assertThat(entity.getBody()).startsWith("<");
	}

	@Test
	public void test_people_endpoint_post_201() {
		// Given
		final String id = UUID.randomUUID().toString();
		final People people = ImmutablePeople.builder().id(id).name("Test").age(10L).build();
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaTypesWithVersion.PEOPLE_V1_JSON_MEDIATYPE);
		final HttpEntity<People> entity = new HttpEntity<>(people, headers);

		// When
		final URI location = this.restTemplate.postForLocation(PeopleRest.URL_PEOPLE, entity);

		// Then
		assertThat(location).isEqualTo(URI.create(PeopleRest.URL_PEOPLE_W_ID.replace("{id}", id)));
	}
}
