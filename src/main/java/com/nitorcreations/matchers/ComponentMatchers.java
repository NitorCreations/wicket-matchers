package com.nitorcreations.matchers;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class ComponentMatchers {

    @Factory
    public static <T extends Component> Matcher<T> hasId(String id) {
        return hasId(equalTo(id));
    }

    @Factory
    public static <T extends Component> Matcher<T> hasId(Matcher<String> idMatcher) {
        return hasProperty("id", idMatcher);
    }

    /**
     * Matches that the component is visible in hierarchy.
     *
     * @return matcher for component visibility
     * @see {@link Component#isVisibleInHierarchy()}
     */
    @Factory
    public static <T extends Component> Matcher<T> visibleInHierarchy() {
        return new TypeSafeDiagnosingMatcher<T>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("visible");
            }

            @Override
            protected boolean matchesSafely(T item, Description mismatchDescription) {
                if (!item.isVisibleInHierarchy()) {
                    mismatchDescription.appendText("visible: ").appendValue(item.isVisibleInHierarchy());
                    return false;
                }
                return true;
            }
        };
    }

    /**
     * Matches that the component is enabled in hierarchy.
     *
     * @return matcher for component enabled status
     * @see {@link Component#isEnabledInHierarchy()}
     */
    @Factory
    public static <T extends Component> Matcher<T> enabledInHierarchy() {
        return new TypeSafeDiagnosingMatcher<T>() {
            @Override
            protected boolean matchesSafely(T item, Description mismatchDescription) {
                if (!item.isEnabledInHierarchy()) {
                    mismatchDescription.appendText("enabled: ").appendValue(item.isEnabledInHierarchy());
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("enabled");
            }
        };
    }

    /**
     * Matches a {@link org.apache.wicket.markup.html.basic.Label} with the given model object.
     *
     * @param value
     *         the matcher for the model object
     * @return matcher for label
     */
    @Factory
    public static <T extends Component> Matcher<T> label(final Matcher<?> value) {
        return new TypeSafeDiagnosingMatcher<T>() {

            @Override
            protected boolean matchesSafely(T item, Description mismatchDescription) {
                if (!value.matches(item.getDefaultModelObjectAsString())) {
                    mismatchDescription.appendText("label ");
                    value.describeMismatch(item.getDefaultModelObjectAsString(), mismatchDescription);
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("label ").appendDescriptionOf(value);
            }
        };
    }

    /** @see #label(org.hamcrest.Matcher) */
    @Factory
    public static <T extends Component> Matcher<T> label(String value) {
        return label(is(value));
    }

    /**
     * Matches a component whose {@link org.apache.wicket.Component#getDefaultModelObject()} matches
     * the given matcher {@code m}.
     * @param m the matcher to match the model object against.
     * @return the matcher
     */
    @Factory
    public static <T extends Component> Matcher<T> hasModelObject(Matcher<?> m) {
        return hasProperty("defaultModelObject", m);
    }

    /** @see #hasModelObject(org.hamcrest.Matcher) */
    @Factory
    public static <T extends Component> Matcher<T> hasModelObject(Object o) {
        return hasModelObject(is(o));
    }

    /**
     * Matches list view whose {@link org.apache.wicket.markup.html.list.ListView#getList()} matches the given value
     * matcher.
     *
     * @param items
     *         the value matcher
     * @return matcher for {@link org.apache.wicket.markup.html.list.ListView}
     */
    @SuppressWarnings("unchecked")
    @Factory
    public static <T extends Component> Matcher<T> listViewThat(final Matcher<?> items) {
        return (Matcher<T>) new TypeSafeDiagnosingMatcher<ListView<?>>() {
            @Override
            protected boolean matchesSafely(ListView<?> item, Description mismatchDescription) {
                List<?> list = item.getList();
                items.describeMismatch(list, mismatchDescription);
                return items.matches(list);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("ListView with items ").appendDescriptionOf(items);
            }
        };
    }

    /**
     * Matches a {@link org.apache.wicket.markup.html.basic.MultiLineLabel} with the given
     * default model object.
     * @param value the value to match
     */
    @SuppressWarnings("unchecked")
    @Factory
    public static <T extends Component> Matcher<T> multiLineLabel(final Matcher<String> value) {
        return (Matcher<T>) new TypeSafeDiagnosingMatcher<MultiLineLabel>() {

            @Override
            protected boolean matchesSafely(MultiLineLabel item, Description mismatchDescription) {
                value.describeMismatch(item, mismatchDescription);
                return value.matches(item.getDefaultModelObjectAsString());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("multiline label: ").appendDescriptionOf(value);
            }
        };
    }

    /** @see #multiLineLabel(org.hamcrest.Matcher) */
    @Factory
    public static <T extends Component> Matcher<T> multiLineLabel(final String value) {
        return multiLineLabel(is(value));
    }

    @Factory
    public static  <T extends Component> Matcher<T> required() {
        return allOf(instanceOf(Component.class), hasProperty("required", is(true)));
    }

    @Factory
    public static <T extends Component, S extends Page> Matcher<T> bookmarkableLink(Class<S> clazz) {
        return allOf(
                instanceOf(BookmarkablePageLink.class),
                hasProperty("pageClass", equalTo(clazz))
        );
    }

    @Factory
    public static <T extends Component, S extends Page> Matcher<T> bookmarkableLink(Class<S> clazz, PageParameters params) {
        return allOf(
                instanceOf(BookmarkablePageLink.class),
                hasProperty("pageClass", equalTo(clazz)),
                hasProperty("pageParameters", equalTo(params))
        );
    }
}
