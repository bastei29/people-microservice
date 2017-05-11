package de.bst.example.api;

import java.time.Instant;
import java.util.UUID;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;
import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = ImmutablePeople.class)
@JsonDeserialize(as = ImmutablePeople.class)
@Value.Immutable
@Value.Style(defaultAsDefault = true, jdkOnly = true)
public interface People {

	default String getId() {
		return UUID.randomUUID().toString();
	}

	@Pattern(regexp = "[a-zA-Z]{1,50}")
	String getName();

	@Range(min = 1, max = 199)
	Long getAge();

	default Instant getCreated() {
		return Instant.now();
	}
}
