package com.example.onestopshop;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.KeyEvent;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
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
    public ActivityScenarioRule<SearchActivity> searchActivityRule = new ActivityScenarioRule<>(SearchActivity.class);

    @Test
    public void searchWithValidKeyword() {
        // Type a valid search keyword
        try(ActivityScenario<InventoryActivity> inventoryActivityScenario = ActivityScenario.launch(InventoryActivity.class)) {
            addTestItem();
        }

        ActivityScenario<SearchActivity> searchActivityScenario = ActivityScenario.launch(SearchActivity.class);

        Espresso.onView(ViewMatchers.withId(R.id.search))
                .perform(ViewActions.typeText("Test Item Description"));

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

    public void addTestItem() {
        // Start the InventoryActivity


        // Add the test item
        onView(ViewMatchers.withId(R.id.add_button)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.itemName)).perform(ViewActions.typeText("Test"), ViewActions.pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.description)).perform(typeText("Test Item Description"), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.purchaseDate)).perform(replaceText("2023-12-03"), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.make)).perform(typeText("Test Make"), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.model)).perform(typeText("Test Model"), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.estimatedValue)).perform(typeText("500"), pressKey(KeyEvent.KEYCODE_TAB),
                pressKey(KeyEvent.KEYCODE_TAB), pressKey(KeyEvent.KEYCODE_TAB), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.add_tag_button)).perform(click());
        onView(withId(R.id.editTextTagInput)).perform(typeText("test"), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.buttonCreate)).perform(click());
        onView(withId(R.id.buttonDone)).perform(click());
        onView(withId(R.id.btn_add_item)).perform(click());


    }
}