package de.bst.example.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import de.bst.example.PeopleApplication;
import de.bst.example.api.ImmutablePeople;
import de.bst.example.api.People;
import de.bst.example.persistence.PeopleEntity;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PeopleApplication.class)
public class PeopleServiceTest {

	@Autowired
	private PeopleService peopleService;

	@Autowired
	private EntityManager entityManager;

	@Transactional
	@Test
	public void test_find_people_by_id() {
		// Given
		String id = UUID.randomUUID().toString();
		People data = ImmutablePeople.builder().age(11L).id(id).name("Bastian").build();
		entityManager.persist(new PeopleEntity(data));

		// When
		People people = peopleService.findBy(id);

		// Then
		assertThat(people).isEqualTo(data);
	}

	@Test
	public void test_add_people() {
		// Given
		String id = UUID.randomUUID().toString();
		People data = ImmutablePeople.builder().age(11L).id(id).name("Bastian").build();

		// When
		peopleService.add(data);

		// Then
		assertThat(entityManager.find(PeopleEntity.class, id)).isEqualToComparingFieldByField(new PeopleEntity(data));
	}
}
