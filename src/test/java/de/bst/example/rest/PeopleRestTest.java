package de.bst.example.rest;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.bst.example.api.ImmutablePeople;
import de.bst.example.service.PeopleService;

@RunWith(SpringRunner.class)
public class PeopleRestTest {

	private static String ID = UUID.randomUUID().toString();

	@Mock
	private PeopleService peopleService;

	@InjectMocks
	private PeopleRest peopleRest;

	private ResultActions response;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		// Given
		Mockito.when(peopleService.findBy(Mockito.anyString()))
				.thenReturn(ImmutablePeople.builder().age(11L).id(ID).name("Bastian").build());
		final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(peopleRest).build();
		final RequestBuilder request = MockMvcRequestBuilders.get(PeopleRest.URL_PEOPLE_W_ID, ID).accept(MediaType.APPLICATION_JSON);

		// When
		response = mockMvc.perform(request);
	}

	@Test
	public void test_get_people_http_200() throws Exception {
		// Then
		response.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
	}

	@Test
	public void test_get_people_http_content_typ() throws Exception {
		// Then
		response.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	public void test_get_people_http_content() throws Exception {
		// Then
		response.andExpect(MockMvcResultMatchers.content().json("{\"name\":\"Bastian\",\"age\":11,\"id\":\"" + ID + "\"}", false));
	}
}
