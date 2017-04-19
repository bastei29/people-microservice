package de.bst.example.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

	public List<People> findAll() {
		return entityManager.createNamedQuery(PeopleEntity.FIND_ALL, PeopleEntity.class).getResultList().stream()
				.map(PeopleEntity::asObject).collect(Collectors.toList());
	}

	public List<People> findAsListBy(String id) {
		return entityManager.createNamedQuery(PeopleEntity.FIND_BY_ID, PeopleEntity.class).setParameter(PeopleEntity.PARAM_ID, id)
				.getResultList().stream().map(PeopleEntity::asObject).collect(Collectors.toList());
	}
}
