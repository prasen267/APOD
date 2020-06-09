package com.pb.apod


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val appCompatImageButton = onView(
            allOf(
                withId(R.id.iv_calender), withContentDescription("Button for Calendar"),
                childAtPosition(
                    allOf(
                        withId(R.id.title_layout),
                        childAtPosition(
                            withId(R.id.mainLayout),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val linearLayout = onView(
            allOf(
                IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.DatePicker::class.java),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        linearLayout.check(matches(isDisplayed()))

        val linearLayout2 = onView(
            allOf(
                IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.DatePicker::class.java),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        linearLayout2.check(matches(isDisplayed()))

        val appCompatButton = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton.perform(scrollTo(), click())

        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.btn_play), withContentDescription("button for playing media"),
                childAtPosition(
                    allOf(
                        withId(R.id.description_layout),
                        childAtPosition(
                            withId(R.id.mainLayout),
                            3
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        val frameLayout = onView(
            allOf(
                withId(R.id.videoFragment),
                childAtPosition(
                    allOf(
                        withId(R.id.nav_host),
                        childAtPosition(
                            withId(R.id.container),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        frameLayout.check(matches(isDisplayed()))

        val frameLayout2 = onView(
            allOf(
                withId(R.id.videoFragment),
                childAtPosition(
                    allOf(
                        withId(R.id.nav_host),
                        childAtPosition(
                            withId(R.id.container),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        frameLayout2.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.tv_title), withText("ISS Fisheye Fly-Through"),
                childAtPosition(
                    allOf(
                        withId(R.id.title_layout),
                        childAtPosition(
                            withId(R.id.mainLayout),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("ISS Fisheye Fly-Through")))

        val textView2 = onView(
            allOf(
                withId(R.id.textView2), withText("Swipe To View Description"),
                childAtPosition(
                    allOf(
                        withId(R.id.description_layout),
                        childAtPosition(
                            withId(R.id.mainLayout),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Swipe To View Description")))

        val textView3 = onView(
            allOf(
                withId(R.id.textView2), withText("Swipe To View Description"),
                childAtPosition(
                    allOf(
                        withId(R.id.description_layout),
                        childAtPosition(
                            withId(R.id.mainLayout),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Swipe To View Description")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
