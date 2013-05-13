package com.nitorcreations.matchers;

import org.junit.Before;
import org.junit.Test;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class FormComponentMatchersTest {

    WicketTester wicketTester = new WicketTester();

    FormComponent<?> formComponent;

    @Before
    public void setup() {
        formComponent = new TextField<String>("field", Model.of(""), String.class);
    }

    @Test
    public void hasLabelMatchesCorrectLabel() {
        formComponent.setLabel(Model.of("FOO"));
        assertThat(formComponent, FormComponentMatchers.hasLabel("FOO"));
    }

    @Test
    public void hasLabelDoesNotMatchIncorrectLabel() {
        formComponent.setLabel(Model.of("BAR"));
        assertThat(formComponent, not(FormComponentMatchers.hasLabel(equalTo("FOO"))));
    }

}
