package com.jokku.funapp.presentation

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.jokku.funapp.R
import com.jokku.funapp.core.*
import com.jokku.funapp.data.cache.BaseRealmProvider
import com.jokku.funapp.data.cache.JokeRealmModel
import com.jokku.funapp.data.cache.QuoteRealmModel
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
internal class MainActivityTest {
    companion object {
        const val JOKE_ITEM_TEXT_0 = "mock setup 0\nmock punchline 0"
        const val JOKE_ITEM_TEXT_1 = "mock setup 1\nmock punchline 1"
        const val QUOTE_ITEM_TEXT_0 = "mock content 0\nmock author 0"
        const val QUOTE_ITEM_TEXT_1 = "mock content 1\nmock author 1"
    }
    @get:Rule
    val activityTestRule = lazyActivityScenarioRule<MainActivity>(launchActivity = false)

    @Before
    fun before() {
        val realmProvider = BaseRealmProvider(true)

        realmProvider.provideRealm().writeBlocking {
            val jokes: RealmResults<JokeRealmModel> = query<JokeRealmModel>().find()
            val quotes: RealmResults<QuoteRealmModel> = query<QuoteRealmModel>().find()
            delete(jokes)
            delete(quotes)
        }
        activityTestRule.launch(
            Intent(
                ApplicationProvider.getApplicationContext(),
                MainActivity::class.java
            )
        )
    }

    @Test
    fun test_action_btn() {
        Button.clickWithText("JOKES")
        Recycler.checkNoItems()
        Button.clickWithText("GET JOKE")
        View.checkWithText(JOKE_ITEM_TEXT_0)
        Button.clickWithText("GET JOKE")
        View.checkWithText(JOKE_ITEM_TEXT_1)
        Button.clickWithText("QUOTES")
        Recycler.checkNoItems()
        Button.clickWithText("GET QUOTE")
        View.checkWithText(QUOTE_ITEM_TEXT_0)
        Button.clickWithText("GET QUOTE")
        View.checkWithText(QUOTE_ITEM_TEXT_1)
    }

    @Test
    fun test_favorite_fun_addition() {
        Button.clickWithText("JOKES")
        Recycler.checkNoItems()
        Button.clickWithText("GET JOKE")
        View.checkWithText(JOKE_ITEM_TEXT_0)
        Button.clickWithId(R.id.favorite_ib)
        View.checkWithText(JOKE_ITEM_TEXT_0, R.id.list_favorite_tv)
        Button.clickWithText("QUOTE")
        Recycler.checkNoItems()
        Button.clickWithText("GET QUOTE")
        View.checkWithText(QUOTE_ITEM_TEXT_0)
        Button.clickWithId(R.id.favorite_ib)
        View.checkWithText(QUOTE_ITEM_TEXT_0, R.id.list_favorite_tv)
    }

    @Test
    fun test_two_favorite_fun_addition() {
        onView(withText("JOKES")).perform(click())
        onView(RecyclerViewMatcher(R.id.fun_recycler_view).atPosition(0, R.id.list_favorite_tv))
            .check(matches(withText("No favorites! Add one tapping heart icon")))
        onView(withText("GET JOKE")).perform(click())
        onView(withText("mock setup 0\nmock punchline 0")).check(matches(isDisplayed()))
        onView(withId(R.id.favorite_ib)).perform(click())
        onView(RecyclerViewMatcher(R.id.fun_recycler_view).atPosition(0, R.id.list_favorite_tv))
            .check(matches(withText("mock setup 0\nmock punchline 0")))
        onView(withText("GET JOKE")).perform(click())
        onView(withText("mock setup 1\nmock punchline 1")).check(matches(isDisplayed()))
        onView(withId(R.id.favorite_ib)).perform(click())
        onView(RecyclerViewMatcher(R.id.fun_recycler_view).atPosition(1, R.id.list_favorite_tv))
            .check(matches(withText("mock setup 1\nmock punchline 1")))
    }

    @Test
    fun test_favorite_fun_addition_and_deletion() {
        onView(RecyclerViewMatcher(R.id.fun_recycler_view).atPosition(0, R.id.list_favorite_tv))
            .check(matches(withText("No favorites! Add one tapping heart icon")))
        onView(withText("JOKES")).perform(click())
        onView(withText("GET JOKE")).perform(click())
        onView(withText("mock setup 0\nmock punchline 0")).check(matches(isDisplayed()))
        onView(withId(R.id.favorite_ib)).perform(click())
        onView(RecyclerViewMatcher(R.id.fun_recycler_view).atPosition(0, R.id.list_favorite_tv))
            .check(matches(withText("mock setup 0\nmock punchline 0")))
        onView(RecyclerViewMatcher(R.id.fun_recycler_view).atPosition(0, R.id.list_favorite_ib))
            .perform(click())
        onView(withText("YES")).perform(click())
        onView(RecyclerViewMatcher(R.id.fun_recycler_view).atPosition(0, R.id.list_favorite_tv))
            .check(matches(withText("No favorites! Add one tapping heart icon")))
    }
}