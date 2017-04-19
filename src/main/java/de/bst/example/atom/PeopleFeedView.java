package de.bst.example.atom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.view.feed.AbstractAtomFeedView;

import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.atom.Link;

import de.bst.example.api.People;

public class PeopleFeedView extends AbstractAtomFeedView {

	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Feed feed, HttpServletRequest request) {
		feed.setId(UUID.randomUUID().toString());
		feed.setTitle("People Feed");
		final List<Link> links = new ArrayList<>();
		final Link link = new Link();
		link.setHref(request.getRequestURL().toString());
		link.setType(MediaType.APPLICATION_ATOM_XML_VALUE);
		links.add(link);
		feed.setAlternateLinks(links);
	}

	@Override
	protected List<Entry> buildFeedEntries(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		final List<Entry> entries = new ArrayList<>();
		final Object ob = model.get("people-feed");
		if (ob instanceof List) {
			for (int i = 0; i < ((List<?>) ob).size(); i++) {
				final Object feedObj = ((List<?>) ob).get(i);
				final People people = (People) feedObj;
				final Entry entry = new Entry();
				entry.setId(people.id());
				entry.setPublished(Date.from(people.created()));
				final List<Link> links = new ArrayList<>();
				final Link link = new Link();
				link.setHref(request.getRequestURL().toString() + "/" + people.id());
				link.setType(MediaType.APPLICATION_ATOM_XML_VALUE);
				links.add(link);
				entry.setAlternateLinks(links);
				final Content content = new Content();
				// TODO XML machen statt json
				content.setValue(people.toString());
				entry.setSummary(content);
				entries.add(entry);
			}
		} else {
			throw new RuntimeException("Mandatory object 'people-feed' not provided.");
		}
		return entries;
	}
}
