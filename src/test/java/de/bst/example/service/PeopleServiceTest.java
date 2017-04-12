package de.bst.example.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import de.bst.example.PeopleApplication;
import de.bst.example.api.ImmutablePeople;
import de.bst.example.api.People;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PeopleApplication.class)
public class PeopleServiceTest {

	@Autowired
	private PeopleService peopleService;

	@Test
	public void test_find_people_by_id() {
		// Given
		String id = UUID.randomUUID().toString();

		// When
		People people = peopleService.findBy(id);

		// Then
		assertThat(people).isEqualTo(ImmutablePeople.builder().age(11L).id(id).name("Bastian").build());
	}
}
