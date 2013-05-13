package com.nitorcreations.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import org.apache.wicket.Component;
import org.apache.wicket.util.tester.BaseWicketTester;
import org.apache.wicket.util.tester.TagTester;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

public final class HasCorrespondingTag<T extends Component> extends TypeSafeDiagnosingMatcher<T> {

    private final String response;

    private final List<Matcher<? super TagTester>> attributesList = new ArrayList<Matcher<? super TagTester>>();

    private HasCorrespondingTag(String response) {
        this.response = response;
    }

    /**
     * Check the given response's markup for the corresponding tag
     *
     * @param response
     *         the response to check
     * @param <T>
     *         type of the component
     * @return the matcher
     */
    @Factory
    public static <T extends Component> HasCorrespondingTag<T> hasTag(String response) {
        return new HasCorrespondingTag<T>(response);
    }

    /**
     * The same as {@link #hasTag(String)}, but uses {@link org.apache.wicket.util.tester.BaseWicketTester#getLastResponseAsString()}
     * to get the last response.
     *
     * @param wicketTester
     *         the wicket tester to use
     * @param <T>
     *         type of the component
     * @return the matcher
     */
    @Factory
    public static <T extends Component> HasCorrespondingTag<T> hasTag(BaseWicketTester wicketTester) {
        return new HasCorrespondingTag<T>(wicketTester.getLastResponseAsString());
    }

    @Override
    protected boolean matchesSafely(Component item, Description mismatchDescription) {
        final String itemMarkupId = item.getMarkupId(false);
        checkMarkupIdNotNull(itemMarkupId);
        checkAttributesListNotEmpty(attributesList);

        final TagTester tagTester = TagTester.createTagByAttribute(response, "id", itemMarkupId);
        if (tagTester == null) {
            mismatchDescription.appendText("Matching tag was not found in the given response");
            return false;
        }

        Matcher<TagTester> tagTesterMatcher = allOf(attributesList);
        tagTesterMatcher.describeMismatch(tagTester, mismatchDescription);
        return tagTesterMatcher.matches(tagTester);
    }

    private static void checkMarkupIdNotNull(String itemMarkupId) {
        if (itemMarkupId == null) {
            throw new IllegalArgumentException("Can only match components that have markup ids set. Consider using Component#setOutputMarkupId().");
        }
    }

    private static void checkAttributesListNotEmpty(List<Matcher<? super TagTester>> attributesList) {
        if (attributesList.isEmpty()) {
            throw new IllegalArgumentException("Attributes list was empty. Please add at least one attribute with HasCorrespondingTag#with(...)");
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with matching tag that ");
        for (Matcher<? super TagTester> attributeMatcher : attributesList) {
            description.appendDescriptionOf(attributeMatcher);
        }
    }

    public HasCorrespondingTag<T> with(String attribute, Matcher<? super String> value) {
        attributesList.add(new HasAttribute(attribute, value));
        return this;
    }

    public HasCorrespondingTag<T> with(String attribute, String value) {
        return with(attribute, equalTo(value));
    }

    /**
     * Matches a tag tester by verifying a tag's attribute
     */
    private static class HasAttribute extends TypeSafeDiagnosingMatcher<TagTester> {
        private final String attribute;

        private final Matcher<? super String> valueMatcher;

        public HasAttribute(String attribute, Matcher<? super String> value) {
            this.attribute = attribute;
            this.valueMatcher = value;
        }

        @Override
        protected boolean matchesSafely(TagTester tagTester, Description mismatchDescription) {
            final String value = tagTester.getAttribute(attribute);
            if (!valueMatcher.matches(value)) {
                mismatchDescription.appendText("attribute ").appendValue(attribute);
                valueMatcher.describeMismatch(value, mismatchDescription);
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("has attribute ").appendValue(attribute).appendText("=")
                    .appendDescriptionOf(valueMatcher);
        }
    }
}
