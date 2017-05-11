package de.bst.example.atom;

import static de.bst.example.PeopleApplication.FEED_PEOPLE_LIST;
import static de.bst.example.PeopleApplication.FEED_PEOPLE_UPDATED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
		feed.setId(request.getRequestURL().toString());
		feed.setTitle("People Feed");
		final List<Link> links = new ArrayList<>();
		final Link link = new Link();
		link.setHref(request.getRequestURL().toString());
		link.setType(MediaType.APPLICATION_ATOM_XML_VALUE);
		links.add(link);
		feed.setAlternateLinks(links);
		feed.setUpdated(typedGet(model.get(FEED_PEOPLE_UPDATED), Date.class));
	}

	@Override
	protected List<Entry> buildFeedEntries(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		final List<Entry> entries = new ArrayList<>();
		for (final People people : typedListGet(model.get(FEED_PEOPLE_LIST), People.class)) {
			final Entry entry = new Entry();
			entry.setId(people.getId());
			entry.setPublished(Date.from(people.getCreated()));
			entry.setCreated(Date.from(people.getCreated()));
			final List<Link> links = new ArrayList<>();
			final Link link = new Link();
			link.setHref(request.getRequestURL().toString() + "/" + people.getId());
			link.setType(MediaType.APPLICATION_ATOM_XML_VALUE);
			links.add(link);
			entry.setAlternateLinks(links);
			final Content content = new Content();
			content.setSrc(request.getRequestURL().toString() + "/" + people.getId());
			content.setType(MediaType.APPLICATION_JSON_VALUE);
			entry.setContents(Arrays.asList(content));
			final Content summary = new Content();
			summary.setValue("This is people " + people.getId());
			entry.setSummary(summary);
			entries.add(entry);
		}
		return entries;
	}

	private <T> T typedGet(Object object, Class<T> clazz) {
		return Optional.ofNullable(object).map(clazz::cast).orElse(null);
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> typedListGet(Object object, Class<T> clazz) {
		return Optional.ofNullable(object).map(List.class::cast).orElse(null);
	}
}
