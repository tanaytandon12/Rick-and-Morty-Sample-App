package com.weather.willy.willyweathersample.util

import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility

fun ViewInteraction.isVisible() =
    this.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

fun ViewInteraction.isGone() =
    this.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

fun ViewInteraction.isInvisible() =
    this.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
