package com.nitorcreations.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import org.apache.wicket.Component;

/**
 * Matches that the component has a parent matching the given matcher.
 * @param <T>
 */
public class ComponentWithParent<T extends Component> extends TypeSafeDiagnosingMatcher<T> {

    private final Matcher<?> parent;

    private ComponentWithParent(Matcher<?> parent) {
        this.parent = parent;
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        parent.describeMismatch(item.getParent(), mismatchDescription);
        return parent.matches(item.getParent());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("has parent ").appendDescriptionOf(parent);
    }

    @Factory
    public static <T extends Component> Matcher<T> hasParent(Matcher<?> parent) {
        return new ComponentWithParent<T>(parent);
    }

}
