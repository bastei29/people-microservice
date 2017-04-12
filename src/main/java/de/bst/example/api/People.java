package de.bst.example.api;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = ImmutablePeople.class)
@JsonDeserialize(as = ImmutablePeople.class)
@Value.Immutable
@Value.Style(jdkOnly = true)
public interface People {

	String id();
	
	String name();
	
	Long age();
}
