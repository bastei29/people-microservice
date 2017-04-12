package de.bst.example.rest;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import de.bst.example.service.PeopleService;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class PeopleRestMockMvcTest {

	private static String ID = UUID.randomUUID().toString();

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@MockBean
	private PeopleService peopleService;

	@Before
	public void setUp() throws Exception {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation)).alwaysDo(document("{method-name}/{step}/"))
				.build();

		// Given
		Mockito.when(peopleService.findBy(Mockito.anyString()))
				.thenReturn(ImmutablePeople.builder().age(11L).id(ID).name("Bastian").build());
	}

	@Test
	public void test_get_people() throws Exception {
		// When - Then
		mockMvc.perform(get(PeopleRest.GET_PEOPLE, ID).with(httpBasic("user", "password"))).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(content().json("{\"name\":\"Bastian\",\"age\":11,\"id\":\"" + ID + "\"}", false))
				.andDo(document("get-people",
						responseFields(fieldWithPath("id").description("The id of the people"),
								fieldWithPath("name").description("The name of the people"),
								fieldWithPath("age").description("The age of the people"))));
	}

	@Test
	public void test_get_people_http_401() throws Exception {
		// When - Then
		mockMvc.perform(get(PeopleRest.GET_PEOPLE, ID)).andExpect(status().isOk());
	}
}
