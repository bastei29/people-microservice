package de.bst.example.rest;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.bst.example.api.People;
import de.bst.example.service.NotFoundException;
import de.bst.example.service.PeopleService;

@RestController
public class PeopleRest {

	public static final String URL_PEOPLE = "/people";
	public static final String URL_PEOPLE_W_ID = URL_PEOPLE + "/{id}";

	@Autowired
	private PeopleService peopleService;

	@GetMapping(value = URL_PEOPLE_W_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public People people(@PathVariable String id) {
		return peopleService.findBy(id);
	}

	@PostMapping(value = URL_PEOPLE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> people_post(@RequestBody People people) {
		peopleService.add(people);
		return ResponseEntity.created(URI.create(URL_PEOPLE_W_ID.replace("{id}", people.id()))).build();
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Nicht gefunden")
	@ExceptionHandler(NotFoundException.class)
	public void notFound() {
	}
}
