package de.bst.example.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date updated;

	public PeopleEntity() {
	}

	public PeopleEntity(People people) {
		id = people.id();
		name = people.name();
		age = people.age();
		created = Date.from(people.created());
		updated = Date.from(people.created());
	}

	public People asObject() {
		return ImmutablePeople.builder().created(created.toInstant()).age(age).id(id).name(name).build();
	}
}
