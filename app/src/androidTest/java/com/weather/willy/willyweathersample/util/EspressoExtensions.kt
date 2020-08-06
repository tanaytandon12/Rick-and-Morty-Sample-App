package com.weather.willy.willyweathersample.util

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.actionWithAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.StringDescription
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.*

fun ViewInteraction.visibilityIsVisible() =
    this.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

fun ViewInteraction.visibilityIsGone() =
    this.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

fun ViewInteraction.visibilityIsInvisible() =
    this.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))

/**
 * Class to match count of recyclerview
 */
fun Any.hasItemCount(matcher: Matcher<Int>): Matcher<View> =
    object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("has count: ")
            matcher.describeTo(description)
        }

        override fun matchesSafely(item: RecyclerView?): Boolean =
            matcher.matches(item?.adapter?.itemCount)
    }

fun Any.waitUnit(matcher: Matcher<View>): ViewAction = actionWithAssertions(object : ViewAction {
    override fun getDescription(): String {
        val stringDescription = StringDescription()
        matcher.describeTo(stringDescription)
        return "wait unit $stringDescription"
    }

    override fun getConstraints(): Matcher<View> = isAssignableFrom(View::class.java)

    override fun perform(uiController: UiController?, view: View?) {
        if (!matcher.matches(view)) {
            val layoutChangedCallback = OnLayoutChangedCallback(matcher = matcher)
            try {
                IdlingRegistry.getInstance().register(layoutChangedCallback)
                view?.addOnLayoutChangeListener(layoutChangedCallback)
                uiController?.loopMainThreadUntilIdle()
            } finally {
                view?.removeOnLayoutChangeListener(layoutChangedCallback)
                IdlingRegistry.getInstance().unregister(layoutChangedCallback)
            }
        }
    }
})

class OnLayoutChangedCallback(
    var mCallback: IdlingResource.ResourceCallback? = null,
    val matcher: Matcher<View>
) : IdlingResource, View.OnLayoutChangeListener {

    private var isIdle = false

    override fun getName(): String = "Recyclerview Idling Resource"

    override fun isIdleNow(): Boolean = isIdle

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        mCallback = callback
    }

    override fun onLayoutChange(
        v: View?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int
    ) {
        isIdle = matcher.matches(v)
        mCallback?.onTransitionToIdle()
    }
}


class RecyclerViewItemMatcher(@IdRes val recyclerViewId: Int) {
    fun atPosition(position: Int, @IdRes targetId: Int = -1): Matcher<View> =
        object : TypeSafeMatcher<View>() {

            var childView: View? = null

            override fun describeTo(description: Description?) {
            }

            override fun matchesSafely(item: View?): Boolean {
                if (childView == null) {
                    val recyclerView = item?.rootView?.findViewById<RecyclerView>(recyclerViewId)
                    if (recyclerView != null) {
                        val viewholder = recyclerView.findViewHolderForAdapterPosition(position)
                        if (viewholder != null) {
                            childView = viewholder.itemView
                        }
                    } else {
                        return false
                    }
                }
                return if (targetId == -1) item == childView else item == childView?.findViewById(
                    targetId
                )
            }
        }
}