package de.bst.example.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import de.bst.example.api.ImmutablePeople;
import de.bst.example.api.People;
import de.bst.example.persistence.PeopleEntity;

@RunWith(SpringRunner.class)
@AutoConfigureDataJpa
@EntityScan(basePackages = "de.bst.example.persistence")
@ContextConfiguration(classes = PeopleService.class)
public class PeopleServiceTest {

	@Autowired
	private PeopleService peopleService;

	@Autowired
	private EntityManager entityManager;

	@Transactional
	@Test
	public void test_find_people_by_id() {
		// Given
		final String id = UUID.randomUUID().toString();
		final People data = ImmutablePeople.builder().age(11L).id(id).name("Bastian").created(Instant.now()).build();
		entityManager.persist(new PeopleEntity(data));

		// When
		final People people = peopleService.findBy(id);

		// Then
		assertThat(people).isEqualTo(data);
	}

	@Test
	public void test_add_people() {
		// Given
		final String id = UUID.randomUUID().toString();
		final People data = ImmutablePeople.builder().age(11L).id(id).name("Bastian").created(Instant.now()).build();

		// When
		peopleService.add(data);

		// Then
		assertThat(entityManager.find(PeopleEntity.class, id).asObject()).isEqualToComparingFieldByField(data);
	}
}
