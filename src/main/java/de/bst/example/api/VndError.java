package de.bst.example.api;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.immutables.value.Value;
import org.immutables.value.Value.Enclosing;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = ImmutableVndError.class)
@JsonDeserialize(as = ImmutableVndError.class)
@Value.Immutable
@Value.Style(defaultAsDefault = true, jdkOnly = true)
@Enclosing
public interface VndError {

	String message();

	Optional<String> path();

	Optional<String> logref();

	default Collection<LinkRelation> _links() {
		return Collections.emptyList();
	}

	@JsonSerialize(as = ImmutableVndError.Link.class)
	@JsonDeserialize(as = ImmutableVndError.Link.class)
	@Value.Immutable
	@Value.Style(defaultAsDefault = true, jdkOnly = true)
	interface Link {
		String href();
	}

	@JsonSerialize(as = ImmutableVndError.LinkRelation.class)
	@JsonDeserialize(as = ImmutableVndError.LinkRelation.class)
	@Value.Immutable
	@Value.Style(defaultAsDefault = true, jdkOnly = true)
	interface LinkRelation {

		Optional<Link> about();

		Optional<Link> describes();

		Optional<Link> help();
	}
}
