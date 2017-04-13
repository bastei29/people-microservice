package de.bst.example.api;

import javax.validation.constraints.Pattern;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = ImmutablePeople.class)
@JsonDeserialize(as = ImmutablePeople.class)
@Value.Immutable
@Value.Style(jdkOnly = true)
public interface People {

	String id();

	@Pattern(regexp = "\\w")
	String name();

	Long age();
}
