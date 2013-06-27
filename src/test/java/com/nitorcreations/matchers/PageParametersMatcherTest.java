package com.nitorcreations.matchers;

import org.junit.Test;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;

import static com.nitorcreations.matchers.PageParametersMatcher.hasParam;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class PageParametersMatcherTest {

    private WicketTester wicketTester = new WicketTester();

    @Test
    public void mismatchWhenKeyNotFound() {
        assertThat(
                new PageParameters(),
                not(hasParam("Foo", "Bar"))
        );
    }

    @Test
    public void mismatchWhenIncorrectValue() {
        assertThat(
                new PageParameters().add("Foo", "Qux"),
                not(hasParam("Foo", is("Bar")))
        );
    }

    @Test
    public void matchWhenValueCorrect() {
        assertThat(
                new PageParameters().add("Foo", "Bar"),
                hasParam("Foo", "Bar")
        );
    }

}
