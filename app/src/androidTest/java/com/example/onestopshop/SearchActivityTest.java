package com.example.onestopshop;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;

public class SearchActivityTest {
    //Note: SIGN IN THROUGH EMULATOR FIRST TO RUN TEST

    @Rule
    public ActivityScenarioRule<SearchActivity> activityRule = new ActivityScenarioRule<>(SearchActivity.class);

    @Test
    public void searchWithValidKeyword() {
        // Type a valid search keyword
        Espresso.onView(ViewMatchers.withId(R.id.search))
                .perform(ViewActions.typeText("Phone"));

        // Click the search button
        Espresso.onView(ViewMatchers.withId(R.id.bsearch))
                .perform(ViewActions.click());

        // Check if the search results are displayed (replace R.id.itemlist with the actual ID)
        Espresso.onView(ViewMatchers.withId(R.id.itemlist))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // You can add more assertions based on your application's behavior
    }

    @Test
    public void searchWithEmptyKeyword() {
        // Click the search button without entering a search keyword
        Espresso.onView(ViewMatchers.withId(R.id.bsearch))
                .perform(ViewActions.click());


        Espresso.onView(ViewMatchers.withId(R.id.itemlist))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));


    }
}