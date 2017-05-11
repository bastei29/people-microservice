package de.bst.example.rest;

import static de.bst.example.PeopleApplication.FEED_PEOPLE_LIST;
import static de.bst.example.PeopleApplication.FEED_PEOPLE_UPDATED;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import de.bst.example.api.ImmutableVndError;
import de.bst.example.api.ImmutableVndErrors;
import de.bst.example.api.ImmutableVndErrors.EmbeddedErrors;
import de.bst.example.api.MediaTypesWithVersion;
import de.bst.example.api.People;
import de.bst.example.api.VndError;
import de.bst.example.api.VndErrors;
import de.bst.example.service.NotFoundException;
import de.bst.example.service.PeopleService;

@RestController
public class PeopleRest {

	public static final String URL_PEOPLE = "/people";
	public static final String URL_PEOPLE_W_ID = URL_PEOPLE + "/{id}";

	@Autowired
	private PeopleService peopleService;

	@GetMapping(value = URL_PEOPLE_W_ID, produces = MediaTypesWithVersion.PEOPLE_V1_JSON_MEDIATYPE)
	public People peopleGet(@PathVariable String id) {
		return peopleService.findBy(id);
	}

	@PostMapping(value = URL_PEOPLE, consumes = MediaTypesWithVersion.PEOPLE_V1_JSON_MEDIATYPE)
	public ResponseEntity<?> peoplePost(@Valid @RequestBody People people, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return handleValidationError(bindingResult, people.getId());
		} else {
			peopleService.add(people);
			return ResponseEntity.created(URI.create(URL_PEOPLE_W_ID.replace("{id}", people.getId()))).build();
		}
	}

	@GetMapping(value = URL_PEOPLE, produces = MediaType.APPLICATION_ATOM_XML_VALUE)
	public ModelAndView peopleFeed() {
		return createFeedView();
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "nicht gefunden")
	@ExceptionHandler(NotFoundException.class)
	public void notFound() {
	}

	private ModelAndView createFeedView() {
		final ModelAndView mav = new ModelAndView();
		mav.addObject(FEED_PEOPLE_LIST, peopleService.findAll());
		mav.addObject(FEED_PEOPLE_UPDATED, peopleService.lastUpdate());
		return mav;
	}

	private ResponseEntity<?> handleValidationError(BindingResult validationResult, String id) {
		if (validationResult.getAllErrors().size() == 1) {
			final ObjectError err = validationResult.getAllErrors().get(0);
			return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaTypesWithVersion.ERROR_JSON_MEDIATYPE)
					.body(createVndError(id, err));
		} else {
			final VndErrors errors = ImmutableVndErrors.builder().total(Long.valueOf(validationResult.getAllErrors().size()))
					._embedded(createVndErrors(id, validationResult.getAllErrors())).build();
			return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaTypesWithVersion.ERROR_JSON_MEDIATYPE).body(errors);
		}
	}

	private String extractMessage(ObjectError error) {
		final String field = Arrays.stream(error.getArguments())
				.filter(f -> DefaultMessageSourceResolvable.class.isAssignableFrom(f.getClass()))
				.map(item -> ((DefaultMessageSourceResolvable) item).getDefaultMessage()).findFirst()
				.orElseGet(() -> "'unresolvable field'");
		return new StringBuffer(field).append(" ").append(error.getDefaultMessage().replaceAll("\"", "'")).toString();
	}

	private VndError createVndError(String id, ObjectError error) {
		return ImmutableVndError.builder().logref(id).message(extractMessage(error)).path(URL_PEOPLE).build();
	}

	private EmbeddedErrors createVndErrors(String id, Collection<ObjectError> error) {
		return ImmutableVndErrors.EmbeddedErrors.builder()
				.errors(error.stream().map(item -> createVndError(id, item)).collect(Collectors.toList())).build();
	}
}
