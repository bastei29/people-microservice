package de.bst.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import de.bst.example.api.ImmutablePeople;
import de.bst.example.api.People;
import de.bst.example.persistence.PeopleEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestEntityManager
@Transactional
public class PeopleServiceTest {

	@Autowired
	private PeopleService peopleService;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void test_find_people_by_id() {
		// Given
		final People data = createTestdata();

		// When
		final People people = peopleService.findBy(data.id());

		// Then
		assertThat(people).isEqualTo(data);
	}

	@Test
	public void test_find_people_not_found() {
		// Given
		final String id = UUID.randomUUID().toString();

		// When - Then
		assertThatThrownBy(() -> peopleService.findBy(id)).isInstanceOf(NotFoundException.class);
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

	@Test
	public void test_findAll_people() {
		// Given
		final List<People> data = createRandomTestdata(7);

		// When
		final List<People> peoples = peopleService.findAll();

		// Then
		assertThat(peoples.size()).isEqualTo(7);
		assertThat(peoples).containsExactlyElementsOf(data);
	}

	@Test
	public void test_findAsListBy_people() {
		// Given
		final People data = createTestdata();

		// When
		final List<People> peoples = peopleService.findAsListBy(data.id());

		// Then
		assertThat(peoples.size()).isEqualTo(1);
		assertThat(peoples.get(0)).isEqualTo(data);
	}

	private People createTestdata() {
		final People data = ImmutablePeople.builder().age(11L).id(UUID.randomUUID().toString()).name("Bastian").created(Instant.now())
				.build();
		return entityManager.persistFlushFind(new PeopleEntity(data)).asObject();
	}

	private List<People> createRandomTestdata(int anzahl) {
		final List<People> dataSet = new ArrayList<>();

		for (int i = 0; i < anzahl; i++) {
			final People data = ImmutablePeople.builder().age(Long.valueOf(i)).name(randomString()).created(Instant.now()).build();
			dataSet.add(entityManager.persistFlushFind(new PeopleEntity(data)).asObject());
		}

		return dataSet;
	}

	private String randomString() {
		return Long.toHexString(Double.doubleToLongBits(Math.random()));
	}
}
