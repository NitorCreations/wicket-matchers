package com.nitorcreations.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import org.apache.wicket.markup.html.form.FormComponent;

import static org.hamcrest.Matchers.is;

public final class FormComponentMatchers {

    private FormComponentMatchers() {}

    @Factory
    public static <T extends FormComponent<?>> Matcher<T> hasLabel(final Matcher<String> label) {
        return new TypeSafeDiagnosingMatcher<T>() {
            @Override
            protected boolean matchesSafely(T item, Description mismatchDescription) {
                String labelText = item.getLabel().getObject();
                label.describeMismatch(labelText, mismatchDescription);
                return label.matches(labelText);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with label ").appendDescriptionOf(label);
            }
        };
    }

    @Factory
    public static Matcher<FormComponent<?>> hasLabel(String label) {
        return hasLabel(is(label));
    }
}
