package de.bst.example.rest;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import de.bst.example.api.MediaTypesWithVersion;
import de.bst.example.service.PeopleService;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class PeopleRestPostMockMvcTest {

	private static final String RESPONSE_ERROR_BODY = "{\"message\":\"must match '[a-zA-Z]{1,50}'\",\"path\":\"/people\",\"logref\":\"%s\"}";

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@MockBean
	private PeopleService peopleService;

	@Before
	public void setUp() throws Exception {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).apply(springSecurity())
				.apply(documentationConfiguration(this.restDocumentation)).alwaysDo(document("{method-name}/{step}/")).build();
	}

	@Test
	public void test_post_people_http_201() throws Exception {
		// Given
		final String id = UUID.randomUUID().toString();
		final String newPeople = "{\"id\":\"%s\",\"name\":\"Neuer\",\"age\":1}";

		// When - Then
		mockMvc.perform(post(PeopleRest.URL_PEOPLE).with(httpBasic("user", "password")).content(String.format(newPeople, id))
				.contentType(MediaTypesWithVersion.PEOPLE_V1_JSON_MEDIATYPE)).andExpect(status().isCreated())
				.andExpect(header().string("Location", PeopleRest.URL_PEOPLE_W_ID.replace("{id}", id)))
				.andDo(document("people-post-v1",
						requestFields(fieldWithPath("id").description("The id of the people"),
								fieldWithPath("name").description("The name of the people"),
								fieldWithPath("age").description("The age of the people"))));
	}

	@Test
	public void test_post_people_http_400_name_empty() throws Exception {
		// Given
		final String id = UUID.randomUUID().toString();
		final String newPeople = "{\"id\":\"%s\",\"name\":\"\",\"age\":1}";

		// When - Then
		mockMvc.perform(post(PeopleRest.URL_PEOPLE).with(httpBasic("user", "password")).content(String.format(newPeople, id))
				.contentType(MediaTypesWithVersion.PEOPLE_V1_JSON_MEDIATYPE)).andDo(print()).andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaTypesWithVersion.ERROR_JSON_MEDIATYPE))
				.andExpect(content().json(String.format(RESPONSE_ERROR_BODY, id)));
	}

	@Test
	public void test_post_people_http_400_name_grt50() throws Exception {
		// Given
		final String newPeople = "{\"age\":\"1\",\"name\":\"aaaaaaaaaabbbbbbbbbbccccccccccddddddddddeeeeeeeeeef\"}";

		// When - Then
		mockMvc.perform(post(PeopleRest.URL_PEOPLE).with(httpBasic("user", "password")).content(newPeople)
				.contentType(MediaTypesWithVersion.PEOPLE_V1_JSON_MEDIATYPE)).andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaTypesWithVersion.ERROR_JSON_MEDIATYPE));
	}

	@Test
	public void test_post_people_http_400_name_only_abc() throws Exception {
		// Given
		final String newPeople = "{\"age\":\"1\",\"name\":\"323143\"}";

		// When - Then
		mockMvc.perform(post(PeopleRest.URL_PEOPLE).with(httpBasic("user", "password")).content(newPeople)
				.contentType(MediaTypesWithVersion.PEOPLE_V1_JSON_MEDIATYPE)).andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaTypesWithVersion.ERROR_JSON_MEDIATYPE));
	}

	@Test
	public void test_post_people_http_400_age_less_1() throws Exception {
		// Given
		final String newPeople = "{\"age\":0,\"name\":\"Hans\"}";

		// When - Then
		mockMvc.perform(post(PeopleRest.URL_PEOPLE).with(httpBasic("user", "password")).content(newPeople)
				.contentType(MediaTypesWithVersion.PEOPLE_V1_JSON_MEDIATYPE)).andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaTypesWithVersion.ERROR_JSON_MEDIATYPE));
	}

	@Test
	public void test_post_people_http_400_age_grt_199() throws Exception {
		// Given
		final String newPeople = "{\"age\":200,\"name\":\"Hans\"}";

		// When - Then
		mockMvc.perform(post(PeopleRest.URL_PEOPLE).with(httpBasic("user", "password")).content(newPeople)
				.contentType(MediaTypesWithVersion.PEOPLE_V1_JSON_MEDIATYPE)).andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaTypesWithVersion.ERROR_JSON_MEDIATYPE));
	}

	@Test
	public void test_post_people_http_401() throws Exception {
		// When - Then
		mockMvc.perform(post(PeopleRest.URL_PEOPLE, new Object())).andExpect(status().isUnauthorized());
	}

	@Test
	public void test_post_people_http_415() throws Exception {
		// Given
		final String id = UUID.randomUUID().toString();
		final String newPeople = "{\"id\":\"%s\",\"name\":\"212dsf3\"}";

		// When - Then
		mockMvc.perform(post(PeopleRest.URL_PEOPLE).with(httpBasic("user", "password")).content(String.format(newPeople, id))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnsupportedMediaType());
	}
}
