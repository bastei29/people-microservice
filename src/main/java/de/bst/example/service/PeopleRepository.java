package de.bst.example.service;

import org.springframework.data.repository.CrudRepository;

import de.bst.example.persistence.PeopleEntity;

public interface PeopleRepository extends CrudRepository<PeopleEntity, String> {

}
