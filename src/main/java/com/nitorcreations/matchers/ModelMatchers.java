package com.nitorcreations.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import org.apache.wicket.model.IModel;

import static org.hamcrest.Matchers.is;

public final class ModelMatchers {

    private ModelMatchers() {}

    /** @see #modelThat(org.hamcrest.Matcher) */
    @Factory
    public static <T> Matcher<IModel<?>> modelThat(T modelObject) {
        return modelThat(is(modelObject));
    }

    /**
     * Matches a {@link org.apache.wicket.model.IModel} whose {@link org.apache.wicket.model.IModel#getObject()}
     * return an object matching the given {@code modelObject} matcher.
     * @param modelObject the matcher for the model object.
     */
    @Factory
    public static Matcher<IModel<?>> modelThat(final Matcher<?> modelObject) {
        return new TypeSafeDiagnosingMatcher<IModel<?>>() {
            @Override
            protected boolean matchesSafely(IModel<?> item, Description mismatchDescription) {
                Object object = item.getObject();
                modelObject.describeMismatch(object, mismatchDescription);
                return modelObject.matches(object);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has model object ").appendDescriptionOf(modelObject);
            }
        };
    }
}
