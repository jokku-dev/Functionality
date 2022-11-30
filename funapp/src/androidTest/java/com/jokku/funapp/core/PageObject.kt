package com.jokku.funapp.core

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.jokku.funapp.R

class Button {
    companion object {
        fun clickWithText(text: String) {
            onView(withText(text)).perform(click())
        }
        fun clickWithId(id: Int) {
            onView(withId(id)).perform(click())
        }
    }
}

class Recycler {
    companion object {
        fun checkNoItems() {
            onView(RecyclerViewMatcher(R.id.fun_recycler_view).atPosition(0, R.id.list_favorite_tv))
                .check(matches(withText("No favorites! Add one tapping heart icon")))
        }
    }
}

class View {
    companion object {
        fun checkWithText(text: String) {
            onView(withText(text)).check(matches(isDisplayed()))
        }
        fun checkWithText(text: String, id: Int) {
            onView(withId(id)).check(matches(withText(text)))
        }
    }
}
