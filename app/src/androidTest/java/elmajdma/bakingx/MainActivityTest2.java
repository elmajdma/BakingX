package elmajdma.bakingx;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest2 {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void mainActivityTest2() {
    ViewInteraction appCompatImageView = onView(
        allOf(withId(R.id.img_favorite_heart),
            childAtPosition(
                childAtPosition(
                    withId(R.id.cardview_recipes_item),
                    0),
                5),
            isDisplayed()));
    appCompatImageView.perform(click());


    ViewInteraction recyclerView = onView(
        allOf(withId(R.id.recyclerview_recipes),
            childAtPosition(
                withClassName(is("android.support.constraint.ConstraintLayout")),
                0)));
    recyclerView.perform(actionOnItemAtPosition(0, click()));

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction appCompatButton = onView(
        allOf(withId(R.id.bt_ingredient_list), withText("INGREDIENTS LIST"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.fragment_recipe_detailed_loader),
                    0),
                1),
            isDisplayed()));
    appCompatButton.perform(click());

    pressBack();

    ViewInteraction recyclerView2 = onView(
        allOf(withId(R.id.recyclerview_steps),
            childAtPosition(
                withClassName(is("android.support.constraint.ConstraintLayout")),
                0)));
    recyclerView2.perform(actionOnItemAtPosition(0, click()));

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction frameLayout = onView(
        allOf(withId(R.id.exo_fullscreen_button),
            childAtPosition(
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    1),
                3),
            isDisplayed()));
    frameLayout.perform(click());

    ViewInteraction frameLayout2 = onView(
        allOf(withId(R.id.exo_fullscreen_button),
            childAtPosition(
                childAtPosition(
                    withClassName(is("android.widget.LinearLayout")),
                    1),
                3),
            isDisplayed()));
    frameLayout2.perform(click());

    pressBack();

    pressBack();

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(700);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    pressBack();

  }

  private static Matcher<View> childAtPosition(
      final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent)
            && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }
}
