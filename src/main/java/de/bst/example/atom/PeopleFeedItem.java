package de.bst.example.atom;

import java.time.Instant;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.migesok.jaxb.adapter.javatime.InstantXmlAdapter;

import de.bst.example.api.People;

@XmlRootElement
public class PeopleFeedItem {

	private String id;
	private String name;
	private Long age;
	private Instant created;

	public PeopleFeedItem() {
	}

	public PeopleFeedItem(People people) {
		this.id = people.id();
		this.name = people.name();
		this.age = people.age();
		this.created = people.created();
	}

	@XmlElement
	public String getId() {
		return id;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	@XmlElement
	public Long getAge() {
		return age;
	}

	@XmlElement
	@XmlJavaTypeAdapter(InstantXmlAdapter.class)
	public Instant getCreated() {
		return created;
	}
}
