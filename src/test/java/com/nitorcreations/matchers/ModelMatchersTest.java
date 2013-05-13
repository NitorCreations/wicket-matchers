package com.nitorcreations.matchers;

import org.junit.Test;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.matchers.ModelMatchers.modelThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class ModelMatchersTest {

    WicketTester wicketTester = new WicketTester();

    IModel<String> model = Model.of("Test string");

    @Test
    public void modelThatMatches() {
        assertThat(model, modelThat("Test string"));
    }

    @Test
    public void modelThatDoesNotMatch() {
        assertThat(model, not(modelThat(endsWith("foo"))));
    }

}
