package de.bst.example.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import de.bst.example.api.People;
import de.bst.example.service.PeopleService;

@RestController
public class PeopleRest {

	public static final String GET_PEOPLE = "/people/{id}";

	@Autowired
	private PeopleService peopleService;

	@GetMapping(value = GET_PEOPLE, produces = MediaType.APPLICATION_JSON_VALUE)
	public People people(@PathVariable String id) {
		return peopleService.findBy(id);
	}
}
