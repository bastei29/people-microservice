package de.bst.example.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.bst.example.api.ImmutablePeople;
import de.bst.example.api.People;
import de.bst.example.persistence.PeopleEntity;

@Component
public class PeopleService {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public People findBy(String id) {
		People people = ImmutablePeople.builder().id(id).age(11L).name("Bastian").build();

		PeopleEntity entity = entityManager.merge(new PeopleEntity(people));
		return entity.asObject();
	}
}
