package de.bst.example.api;

import java.time.Instant;
import java.util.UUID;

import javax.validation.constraints.Pattern;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = ImmutablePeople.class)
@JsonDeserialize(as = ImmutablePeople.class)
@Value.Immutable
@Value.Style(defaultAsDefault = true, jdkOnly = true)
public interface People {

	default String id() {
		return UUID.randomUUID().toString();
	}

	@Pattern(regexp = "\\w")
	String name();

	Long age();

	default Instant created() {
		return Instant.now();
	}
}
