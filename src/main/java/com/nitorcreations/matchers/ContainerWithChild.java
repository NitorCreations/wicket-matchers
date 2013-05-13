package com.nitorcreations.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;

public final class ContainerWithChild extends TypeSafeDiagnosingMatcher<WebMarkupContainer> {
    private final Matcher<? super Component> child;

    private ContainerWithChild(Matcher<? super Component> child) {
        this.child = child;
    }

    /**
     * Matches, if the {@link WebMarkupContainer} has a child that matches the given {@code child} matcher. Will not
     * match any grand-children
     *
     * @param child
     * @return
     */
    @Factory
    public static Matcher<WebMarkupContainer> hasChild(final Matcher<? super Component> child) {
        return new ContainerWithChild(child);
    }

    @Override
    protected boolean matchesSafely(WebMarkupContainer item, Description mismatchDescription) {
        for (Component c : item) {
            if (child.matches(c)) {
                return true;
            }
        }
        mismatchDescription.appendText("no child that matches ").appendDescriptionOf(child);
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(" has child: ").appendDescriptionOf(child);
    }


}