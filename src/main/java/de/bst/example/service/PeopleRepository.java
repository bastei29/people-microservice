package de.bst.example.service;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.bst.example.persistence.PeopleEntity;

public interface PeopleRepository extends CrudRepository<PeopleEntity, String> {

	@Query("SELECT max(p.updated) FROM PeopleEntity p")
	Date lastUpdate();
}
