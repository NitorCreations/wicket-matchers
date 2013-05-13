package com.nitorcreations.matchers;

import org.junit.Test;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.matchers.ContainerWithChild.hasChild;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class ContainerWithChildTest {

    WicketTester wicketTester = new WicketTester();

    @Test
    public void matchesWhenContainsMatchingChild() {
        WebMarkupContainer wmc = new WebMarkupContainer("container");
        wmc.add(new Label("label", "Foobarbaz"));
        assertThat(wmc, hasChild(instanceOf(Label.class)));
    }

    @Test
    public void doesNotMatchWhenNoChildrenMatch() {
        WebMarkupContainer wmc = new WebMarkupContainer("container");
        wmc.add(new Label("label"));
        assertThat(wmc, not(hasChild(instanceOf(Link.class))));
    }

    @Test
    public void doesNotMatchWhenNoChildren() {
        assertThat(new WebMarkupContainer("wmc"),
                not(hasChild(instanceOf(Component.class))));
    }

    @Test
    public void doesNotMatchGrandChildren() {
        WebMarkupContainer wmc1 = new WebMarkupContainer("wmc1");
        WebMarkupContainer wmc2 = new WebMarkupContainer("wmc2");
        wmc2.add(new Label("label"));
        wmc1.add(wmc2);

        assertThat(wmc2, hasChild(instanceOf(Label.class)));
        assertThat(wmc1, not(hasChild(instanceOf(Label.class))));
    }

}
