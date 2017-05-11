package de.bst.example.rest;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

import de.bst.example.api.ImmutablePeople;
import de.bst.example.api.MediaTypesWithVersion;
import de.bst.example.service.NotFoundException;
import de.bst.example.service.PeopleService;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class PeopleRestGetMockMvcTest {

	private static String ID = UUID.randomUUID().toString();
	private static String ID_NOT_FOUND = UUID.randomUUID().toString();

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

		// Given
		Mockito.when(peopleService.findBy(Mockito.eq(ID))).thenReturn(ImmutablePeople.builder().age(11L).id(ID).name("Bastian").build());
		Mockito.when(peopleService.findBy(Mockito.eq(ID_NOT_FOUND))).thenThrow(new NotFoundException("Id nicht gefunden"));
		Mockito.when(peopleService.findAll()).thenReturn(Arrays.asList(ImmutablePeople.builder().age(11L).id(ID).name("Bastian").build()));
	}

	@Test
	public void test_get_json_people_http_200() throws Exception {
		// When - Then
		mockMvc.perform(get(PeopleRest.URL_PEOPLE_W_ID, ID).with(httpBasic("user", "password"))
				.accept(MediaTypesWithVersion.PEOPLE_V1_JSON_MEDIATYPE)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaTypesWithVersion.PEOPLE_V1_JSON_MEDIATYPE))
				.andExpect(content().json("{\"name\":\"Bastian\",\"age\":11,\"id\":\"" + ID + "\"}", false))
				.andDo(document("people-get-json-v1",
						responseFields(fieldWithPath("id").description("The id of the people"),
								fieldWithPath("name").description("The name of the people"),
								fieldWithPath("age").description("The age of the people"),
								fieldWithPath("created").description("The creation timestamp of the people"))));
	}

	@Test
	public void test_get_json_people_http_404() throws Exception {
		// When - Then
		mockMvc.perform(get(PeopleRest.URL_PEOPLE_W_ID, ID_NOT_FOUND).with(httpBasic("user", "password"))
				.accept(MediaTypesWithVersion.PEOPLE_V1_JSON_MEDIATYPE)).andExpect(status().isNotFound());
	}

	@Test
	public void test_get_json_people_http_401() throws Exception {
		// When - Then
		mockMvc.perform(get(PeopleRest.URL_PEOPLE_W_ID, ID_NOT_FOUND)).andExpect(status().isUnauthorized());
	}

	@Test
	public void test_get_json_people_http_406() throws Exception {
		// When - Then
		mockMvc.perform(
				get(PeopleRest.URL_PEOPLE_W_ID, ID_NOT_FOUND).with(httpBasic("user", "password")).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable());
	}

	@Test
	public void test_get_atom_people_http_200() throws Exception {
		// When - Then
		mockMvc.perform(get(PeopleRest.URL_PEOPLE).with(httpBasic("user", "password")).accept(MediaType.APPLICATION_ATOM_XML))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_ATOM_XML));
	}
}
