package com.nitorcreations.matchers;

import java.io.Serializable;
import java.util.Arrays;

import org.junit.Test;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.matchers.ComponentMatchers.bookmarkableLink;
import static com.nitorcreations.matchers.ComponentMatchers.enabledInHierarchy;
import static com.nitorcreations.matchers.ComponentMatchers.hasId;
import static com.nitorcreations.matchers.ComponentMatchers.hasModelObject;
import static com.nitorcreations.matchers.ComponentMatchers.label;
import static com.nitorcreations.matchers.ComponentMatchers.listViewThat;
import static com.nitorcreations.matchers.ComponentMatchers.multiLineLabel;
import static com.nitorcreations.matchers.ComponentMatchers.visibleInHierarchy;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ComponentMatchersTest {
    static WicketTester wicketTester = new WicketTester();

    Component c;

    @Test
    public void hasIdWorks() {
        assertThat(new Label("id"), hasId("id"));
        assertThat(new Label("id"), not(hasId("id2")));
        assertThat(new Label("foobarbaz"), hasId(startsWith("foo")));
        assertThat(new Label("foobarbaz"), not(hasId(endsWith("foo"))));
    }

    @Test
    public void visibleMatchesWhenVisible() {
        Component c = new Label("foo");
        c.setVisible(true);
        assertTrue(visibleInHierarchy().matches(c));
    }

    @Test
    public void visibleMismatchWhenHidden() {
        Component c = new Label("foo");
        c.setVisible(false);
        assertFalse(visibleInHierarchy().matches(c));
    }

    @Test
    public void enabledMatchesEnabled() {
        c = new Label("foo");
        assertTrue(enabledInHierarchy().matches(c));
    }

    @Test
    public void enabledDoesNotMatchDisabled() {
        c = new Label("foo");
        c.setEnabled(false);
        assertFalse(enabledInHierarchy().matches(c));
    }

    @Test
    public void matchesLabelWithCorrectText() {
        Component c = new Label("foo", "This is text");
        assertTrue(label("This is text").matches(c));
        assertFalse(label("This is not text").matches(c));
    }

    @Test
    public void matchesLabelWithMatcher() {
        Component c = new Label("foo", "This is text");
        assertTrue(label(startsWith("This")).matches(c));
    }

    @Test
    public void labelMatchesDefaultModelObjectAsString() {
        Component c = new Label("id", Model.of(new TestObject()));
        assertThat(c, label("TEST_VALUE"));
    }

    private static class TestObject implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public String toString() {
            return "TEST_VALUE";
        }
    }

    @Test
    public void doesNotMatchLink() {
        Component c = new BookmarkablePageLink<Void>("foo", WebPage.class);
        assertFalse(label("foo").matches(c));
    }

    @Test
    public void doesNotMatchLabelWithIncorrectText() {
        Component c = new Label("foo", "This is text");
        assertFalse(label("This is not text you are looking for").matches(c));
    }

    @Test
    public void listViewMatchesListWithCorrectItems() {
        Component c = newListView("One", "Two");
        assertThat(c, listViewThat(contains("One", "Two")));
    }

    @Test
    public void listViewDoesNotMatchListWithIncorrectItems() {
        Component c = newListView("One", "Two");
        assertThat(c, not(listViewThat(hasSize(3))));
    }

    @Test
    public void listViewDoesNotMatchOtherComponent() {
        assertThat(new Label("foo"), not(listViewThat(anything())));
    }

    @Test
    public void multiLineLabelMatches() {
        assertThat(new MultiLineLabel("foo", "Bar\nBar"), multiLineLabel("Bar\nBar"));
    }

    @Test
    public void bookmarkableLinkMatches() {
        assertThat(new BookmarkablePageLink<Void>("foo", Page.class), bookmarkableLink(Page.class));
    }

    @Test
    public void bookmarkableLinkWithWrongPage() {
        assertThat(new BookmarkablePageLink<Void>("foo", WebPage.class), not(bookmarkableLink(Page.class)));
    }

    @Test
    public void bookmarkableLinkWithParams() {
        assertThat(new BookmarkablePageLink<Void>("foo", Page.class, new PageParameters().add("foo", "bar")),
                bookmarkableLink(Page.class, new PageParameters().add("foo", "bar")));
    }

    @Test
    public void bookmarkableLinkWithWrongParams() {
        assertThat(new BookmarkablePageLink<Void>("foo", Page.class, new PageParameters().add("faz", "foz")),
                not(bookmarkableLink(Page.class, new PageParameters().add("foo", "bar"))));
    }

    @Test
    public void hasModelObject_matchingObject() {
        TestObject testObject = new TestObject();
        assertThat(new Label("label", Model.of(testObject)), hasModelObject(testObject));
    }

    @Test
    public void hasModelObject_notMatchingObject() {
        assertThat(new Label("label", "test"), not(hasModelObject(new TestObject())));
    }

    private ListView<String> newListView(String... values) {
        return new ListView<String>("foo", Arrays.asList(values)) {
            @Override
            protected void populateItem(ListItem<String> item) {
            }
        };
    }

}
