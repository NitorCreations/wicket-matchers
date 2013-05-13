package com.nitorcreations.matchers;

import org.junit.Before;
import org.junit.Test;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.matchers.HasFeedbackMessage.hasError;
import static com.nitorcreations.matchers.HasFeedbackMessage.hasFeedback;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class HasFeedbackMessageTest {

    Component c;

    @Before
    public void setupWicket() {
        new WicketTester();
        c = new Label("foo");
    }

    @Test
    public void hasFeedback_matchesWithCorrectLevelAndMessage() {
        c.error("Message");
        assertThat(c, hasFeedback(FeedbackMessage.ERROR, "Message"));
    }

    @Test
    public void hasFeedback_matchesOneOfMultiple() {
        c.error("ERROR");
        c.info("INFO");
        c.fatal("FATAL");
        assertThat(c, hasFeedback(FeedbackMessage.INFO, "INFO"));
    }

    @Test
    public void hasFeedback_doesNotMatchIfNoFeedback() {
        assertThat(c, not(hasFeedback(FeedbackMessage.INFO, "INFO")));
    }

    @Test
    public void hasFeedback_doesNotMatchIfIncorrectLevel() {
        c.error("Message");
        assertThat(c, not(hasFeedback(FeedbackMessage.INFO, "Message")));
    }

    @Test
    public void hasFeedback_doesNotMatchIfIncorrectMessage() {
        c.error("NOT THE MESSAGE");
        assertThat(c, not(hasFeedback(FeedbackMessage.ERROR, "Message")));
    }

    @Test
    public void hasError_matchesComponentWithErrorMessage() {
        c.error("ERROR");
        assertThat(c, hasError());
    }

    @Test
    public void hasError_doesNotMatchComponentWithInfoMessage() {
        c.info("INFO");
        assertThat(c, not(hasError()));
    }
}
