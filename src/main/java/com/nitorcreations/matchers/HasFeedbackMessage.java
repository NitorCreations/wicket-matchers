package com.nitorcreations.matchers;

import java.io.Serializable;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackCollector;
import org.apache.wicket.feedback.FeedbackMessage;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class HasFeedbackMessage<T extends Component> extends TypeSafeDiagnosingMatcher<T> {
    private final Matcher<? super FeedbackMessage> message;

    @Factory
    public static <T extends Component> Matcher<T> hasError() {
        return hasFeedback(is(FeedbackMessage.ERROR), any(Serializable.class));
    }

    @Factory
    public static <T extends Component> Matcher<T> hasFeedback(Matcher<Integer> level, Matcher<?> message) {
        return new HasFeedbackMessage<T>(
                allOf(
                        instanceOf(FeedbackMessage.class),
                        hasProperty("level", level),
                        hasProperty("message", message)
                )
        );
    }

    @Factory
    public static <T extends Component> Matcher<T> hasFeedback(int level, String message) {
        return hasFeedback(is(level), is(message));
    }

    private HasFeedbackMessage(Matcher<? super FeedbackMessage> message) {
        this.message = message;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("component with feedback message ").appendDescriptionOf(message);
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        List<FeedbackMessage> feedbackMessages = getFeedbackMessages(item);
        for (FeedbackMessage feedbackMessage : feedbackMessages) {
            if (message.matches(feedbackMessage)) {
                return true;
            }
        }

        mismatchDescription.appendText("No matching feedback messages found for ").appendValue(item)
            .appendValueList("actual values were: ", "\n", "", feedbackMessages);

        return false;
    }

    private List<FeedbackMessage> getFeedbackMessages(T item) {
        FeedbackCollector feedbackCollector = new FeedbackCollector(item);
        return feedbackCollector.collect();
    }
}