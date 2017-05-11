package de.bst.example.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import de.bst.example.api.ImmutableVndError;
import de.bst.example.api.MediaTypesWithVersion;

@ControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<?> handlerRuntimeException(RuntimeException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.header(HttpHeaders.CONTENT_TYPE, MediaTypesWithVersion.ERROR_JSON_MEDIATYPE)
				.body(ImmutableVndError.builder().message(e.getClass().getSimpleName() + ": " + e.getMessage()).build());
	}
}
