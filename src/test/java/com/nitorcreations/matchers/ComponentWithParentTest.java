package com.nitorcreations.matchers;

import org.junit.Before;
import org.junit.Test;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.matchers.ComponentWithParent.hasParent;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class ComponentWithParentTest {

    WebMarkupContainer ancestor;
    WebMarkupContainer parent;
    Component child;

    @Before
    public void setup() {
        WicketTester wicketTester = new WicketTester();
        ancestor = new WebMarkupContainer("ancestor");
        parent = new WebMarkupContainer("parent");
        child = new Label("child", "child");
        ancestor.add(parent.add(child));
        wicketTester.startComponent(ancestor);
    }

    @Test
    public void matchesChildsParent() {
        assertThat(child, hasParent(is(parent)));
    }

    @Test
    public void doesNotMatchAncestor() {
        assertThat(child, not(hasParent(is(ancestor))));
    }

}
