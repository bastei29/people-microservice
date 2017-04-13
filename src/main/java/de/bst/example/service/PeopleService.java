package de.bst.example.service;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.bst.example.api.People;
import de.bst.example.persistence.PeopleEntity;

@Component
public class PeopleService {

	@PersistenceContext
	private EntityManager entityManager;

	public People findBy(String id) {
		return Optional.ofNullable(entityManager.find(PeopleEntity.class, id)).map(PeopleEntity::asObject)
				.orElseThrow(() -> new NotFoundException("Daten nicht gefunden!"));
	}

	@Transactional
	public void add(People people) {
		entityManager.persist(new PeopleEntity(people));
	}
}
