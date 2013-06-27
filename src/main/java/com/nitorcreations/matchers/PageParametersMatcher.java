package com.nitorcreations.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import static org.hamcrest.Matchers.is;

public final class PageParametersMatcher extends TypeSafeDiagnosingMatcher<PageParameters> {

    private final String key;
    private final Matcher<? super String> value;

    private PageParametersMatcher(String key, Matcher<? super String> value) {
        this.key = key;
        this.value = value;
    }

    @Factory
    public static Matcher<PageParameters> hasParam(String key, String value) {
           return hasParam(key, is(value));
    }

    @Factory
    public static Matcher<PageParameters> hasParam(String key, Matcher<? super String> value) {
        return new PageParametersMatcher(key, value);
    }

    @Override
    protected boolean matchesSafely(PageParameters item, Description mismatchDescription) {
        String stringValue = item.get(key).toString();

        if (stringValue == null) {
            mismatchDescription.appendText("No parameter with key ").appendValue(key);
            return false;
        }
        mismatchDescription.appendText("Parameter ").appendValue(key).appendText(" ");
        value.describeMismatch(stringValue, mismatchDescription);
        return value.matches(stringValue);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("parameter ").appendValue(key).appendText(" that ").appendDescriptionOf(value);
    }
}
