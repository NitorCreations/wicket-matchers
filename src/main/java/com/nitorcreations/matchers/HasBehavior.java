package com.nitorcreations.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;

public class HasBehavior<T extends Component> extends TypeSafeMatcher<T> {

    private final Matcher<? super Behavior> behavior;

    public HasBehavior(Matcher<? super Behavior> behavior) {
        this.behavior = behavior;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue("Has behavior: ").appendDescriptionOf(behavior);
    }

    @Override
    protected void describeMismatchSafely(T item, Description mismatchDescription) {
        mismatchDescription.appendText("component contained behaviors: ").appendValue(item.getBehaviors());
    }

    @Override
    protected boolean matchesSafely(T item) {
        return Matchers.hasItem(behavior).matches(item.getBehaviors());
    }

    @Factory
    public static <T extends Component> Matcher<T> hasBehavior(Behavior b) {
        return new HasBehavior<T>(Matchers.is(b));
    }

    @Factory
    public static <T extends Component> Matcher<T> hasBehavior(Matcher<? super Behavior> behavior) {
        return new HasBehavior<T>(behavior);
    }

    @Factory
    public static <T extends Component> Matcher<T> hasBehavior(Class<? extends Behavior> behaviorClazz) {
        return new HasBehavior<T>(Matchers.instanceOf(behaviorClazz));
    }

}
