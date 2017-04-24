package de.bst.example.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.bst.example.api.People;
import de.bst.example.persistence.PeopleEntity;

@Component
public class PeopleService {

	@Autowired
	private PeopleRepository peopleRepository;

	public People findBy(String id) {
		return Optional.ofNullable(peopleRepository.findOne(id)).map(PeopleEntity::asObject)
				.orElseThrow(() -> new NotFoundException("Daten nicht gefunden!"));
	}

	@Transactional
	public void add(People people) {
		peopleRepository.save(new PeopleEntity(people));
	}

	public List<People> findAll() {
		return StreamSupport.stream(peopleRepository.findAll().spliterator(), false).map(PeopleEntity::asObject)
				.collect(Collectors.toList());
	}

	public List<People> findAsListBy(String id) {
		return StreamSupport.stream(peopleRepository.findAll().spliterator(), false).map(PeopleEntity::asObject)
				.collect(Collectors.toList());
	}

	public Date lastUpdate() {
		return peopleRepository.lastUpdate();
	}
}
