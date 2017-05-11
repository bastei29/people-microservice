package de.bst.example.api;

import java.util.Collection;

import org.immutables.value.Value;
import org.immutables.value.Value.Enclosing;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = ImmutableVndErrors.class)
@JsonDeserialize(as = ImmutableVndErrors.class)
@Value.Immutable
@Value.Style(defaultAsDefault = true, jdkOnly = true)
@Enclosing
public interface VndErrors {

	Long total();

	EmbeddedErrors _embedded();

	@JsonSerialize(as = ImmutableVndErrors.EmbeddedErrors.class)
	@JsonDeserialize(as = ImmutableVndErrors.EmbeddedErrors.class)
	@Value.Immutable
	@Value.Style(defaultAsDefault = true, jdkOnly = true)
	interface EmbeddedErrors {

		Collection<VndError> errors();
	}
}
