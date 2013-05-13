package com.nitorcreations.matchers;

import org.junit.Before;
import org.junit.Test;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.matchers.HasCorrespondingTag.hasTag;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class HasCorrespondingTagTest {
    WicketTester wicketTester = new WicketTester();

    WebMarkupContainer container;
    Component withMarkupId;
    Component withoutMarkupId;
    Component notEvenAdded;

    @Before
    public void setup() {
        withMarkupId = new Label("withMarkupId", "Label")
                .add(AttributeModifier.append("class", "addedClass")).setOutputMarkupId(true);
        withoutMarkupId = new Label("withoutMarkupId", "Other label");

        container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        container.add(withMarkupId, withoutMarkupId);

        notEvenAdded = new Label("id").add(AttributeModifier.append("class", "added"));
        notEvenAdded.getMarkupId();

        wicketTester.startComponentInPage(container, Markup.of("<div wicket:id=\"container\">" +
                "<span wicket:id=\"withMarkupId\" class=\"existingClass\" />" +
                "<span wicket:id=\"withoutMarkupId\" class=\"otherClass\"/>" +
                "</div>"));
    }

    @Test
    public void matchesWhenTagHasAttribute() {
        assertThat(withMarkupId, hasTag(wicketTester).with("class", "existingClass addedClass"));
    }

    @Test
    public void matchesWithMatchingAttributes() {
        assertThat(withMarkupId, hasTag(wicketTester)
                .with("class", containsString("addedClass"))
                .with("class", containsString("existingClass"))
                .with("nonExistingAttribute", nullValue())
        );
    }

    @Test
    public void doesNotMatchIfNoSuchComponent() {
        assertThat(notEvenAdded, not(hasTag(wicketTester).with("class", "added")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotMatchWhenListIsEmpty() {
        assertThat(container, hasTag(wicketTester));
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionWhenNoMarkupId() {
        assertThat(withoutMarkupId, hasTag(wicketTester).with("class", "otherClass"));
    }

}
