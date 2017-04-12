package de.bst.example.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import de.bst.example.api.ImmutablePeople;
import de.bst.example.api.People;

@Entity
public class PeopleEntity {

	@Id
	private String id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Long age;

	public PeopleEntity() {
	}

	public PeopleEntity(People people) {
		id = people.id();
		name = people.name();
		age = people.age();
	}

	public People asObject() {
		return ImmutablePeople.builder().age(age).id(id).name(name).build();
	}
}
