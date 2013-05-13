# Wicket 6 / Hamcrest 1.3 utility matchers

The static factory methods for each matcher are generated to `com.nitorcreations.WicketMatchers` class.

The package contains the following matchers:

##### ComponentMatchers
 - `hasId(String)`, `hasId(Matcher<String>)` - matches the components wicket id
 - `visibleInHierarchy()`
 - `enabledInHierarchy()`
 - `label(String)`, `label(Matcher<?>)` - matches a `Label` with the given text
 - `hasModelObject(Matcher<?>)`, `hasModelObject(Object)` - matches `Component#getDefaultModelObject()`
 - `listViewThat(Matcher<?>)` - matches a `ListView` whose ListView#getList() matches the given value
 - `multiLineLabel(Matcher<String>)`, `multiLineLabel(String)` - matches a `MultiLineLabel` with the given value
 - `required()` - matches if `isRequired()` returns `true`
 - `bookmarkableLink(Class<S extends Page>)`, `bookmarkableLink(Class<S extends Page> clazz, PageParameters params)` - matches a `BookmarkablePageLink`
##### ComponentWithParent
 - `hasParent(Matcher<?>)` - matches when `Component#getParent()` matches.
#####  ContainerWithChild
 - `hasChild(Matcher<? super Component>)` -  Matches, if the `WebMarkupContainer` has a child that matches the given child matcher. Will not match any grand-children.
#####  FormComponentMatchers
 - `hasLabel(Matcher<String>)`, `hasLabel(String)` - matches `FormComponent.getLabel().getObject()`
#####  HasBehavior
 - `hasBehavior(Behavior)`, `hasBehavior(Matcher<? super Behavior>)`, `hasBehavior(Class<? extends Behavior>)`
##### HasCorrespondingTag
 - `hasTag(String)`, `hasTag(BaseWicketTester)` - looks up the tag from the given response string or `BaseWicketTester#getLastResponseAsString()` that corresponds to the component.
 - Add tag attributes by `.with(String, String)` or `.with(String, Matcher<? super String>)`
 - Example: `assertThat(label, hasTag(wicketTester).with("class", is("blue")));`
#####  HasFeedbackMessage
 - `hasError()`, `hasFeedback(int, String)`, `hasFeedback(Matcher<Integer>, Matcher<String>)`
 - Matches if the component has a feedback message, e.g., `input.error("Error") => assertThat(input, hasFeedback(FeedbackMessage.ERROR, "Error");`
 - Only works with Wicket 6, since Wicket 1.5 has different feedback mechanism.
#####  ModelMatchers
 - `modelThat(Matcher<?>)` / `modelThat(T)` - matches that the model's object matches

## Creating new matchers

If you add new matcher classes, be sure to add `@Factory` annotations to the static factory methods, and remember to add the new class to the `matchers.xml` file. This will ensure that the factory methods will be accessible in the common `com.nitorcreations.WicketMatchers` class.

Example:

```java
public class HasBehavior<T extends Component> extends TypeSafeMatcher<T> {
    @Factory
    public static <T extends Component> Matcher<T> hasBehavior(Behavior b) {
        return new HasBehavior<T>(Matchers.is(b));
    }
```

And the corresponding row in `matchers.xml`:

```xml
<matchers>
    ...
    <factory class="com.nitorcreations.matchers.HasBehavior"/>
    ...
</matchers>
```
