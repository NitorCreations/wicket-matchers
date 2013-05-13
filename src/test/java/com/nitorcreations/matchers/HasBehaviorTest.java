package com.nitorcreations.matchers;

import org.junit.Before;
import org.junit.Test;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.tester.WicketTester;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class HasBehaviorTest {

    static WicketTester wicketTester = new WicketTester();

    Component c;

    @Before
    public void setupWicket() {
        c = new Label("foo");
    }

    @Test
    public void matchesCorrectBehavior() {
        c.add(new AttributeAppender("class", "foo"));
        assertThat(c, HasBehavior.hasBehavior(instanceOf(AttributeAppender.class)));
    }

    @Test
    public void doesNotMatchWhenNoBehaviors() {
        assertThat(c, not(HasBehavior.hasBehavior(AttributeAppender.class)));
    }

    @Test
    public void matchesSameInstance() {
        AttributeAppender b = new AttributeAppender("class", "foo");
        c.add(b);
        assertThat(c, HasBehavior.hasBehavior(b));
    }

    @Test
    public void doesNotMatchWrongClass() {
        c.add(new AttributeModifier("class", "foo"));
        assertThat(c, not(HasBehavior.hasBehavior(AttributeAppender.class)));
    }

}
