package com.example.onestopshop;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

import android.app.Activity;
import androidx.test.espresso.contrib.RecyclerViewActions;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class InventoryActivityTest {
    @Rule
    public ActivityScenarioRule<InventoryActivity> activityScenario = new
            ActivityScenarioRule<InventoryActivity>(InventoryActivity.class);


    @Test
    public void testTopToolbar_isDisplayed() {
        onView(withId(R.id.toolbar_top)).check(matches(isDisplayed()));
    }

    @Test
    public void testSelectMode() {
        // add item
        addTestItem();

        // press select button
        onView(withId(R.id.select_button)).perform(click());
        // Verify buttons are visible and total value is not visible
        onView(withId(R.id.multipleSelectBtns)).check(matches(isDisplayed()));
        onView(withId(R.id.total_value_layout)).check(matches(not(isDisplayed())));
        onView(withId(R.id.item_list)).perform(RecyclerViewActions.scrollToPosition(0))
                .check(matches(hasDescendant(withId(R.id.itemCheckBox))))
                .check(matches(isDisplayed()));

        // Deselect
        onView(withId(R.id.select_button)).perform(click());
        // Verify buttons are not visible and total value is visible
        onView(withId(R.id.total_value_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.multipleSelectBtns)).check(matches(not(isDisplayed())));
        deleteTestItem();
    }

    @Test
    public void testBottomToolbar_isDisplayed() {
        onView(withId(R.id.toolbar_bottom)).check(matches(isDisplayed()));
    }

    @Test
    public void testTotalValue_isDisplayed() {
        onView(withId(R.id.total_value_layout)).check(matches((isDisplayed())));
        ActivityScenario<InventoryActivity> scenario = activityScenario.getScenario();
        onView(withId(R.id.total_estimated_value)).check(matches(withText("$0.00")));
        addTestItem();
        onView(withId(R.id.total_estimated_value)).check(matches(withText("$500.00")));
        deleteTestItem();
    }

    private void addTestItem() {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.itemName)).perform(typeText("Test"), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.description)).perform(typeText("Test Item Description"), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.purchaseDate)).perform(replaceText("2023-12-03"), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.make)).perform(typeText("Test Make"), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.model)).perform(typeText("Test Model"), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.estimatedValue)).perform(typeText("500"), pressKey(KeyEvent.KEYCODE_TAB),
                pressKey(KeyEvent.KEYCODE_TAB), pressKey(KeyEvent.KEYCODE_TAB), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.tags)).perform(typeText("test"), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.btn_add_item)).perform(click());
    }

    private void deleteTestItem() {
        onView(withId(R.id.item_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.btnDelete)).perform(click());
    }

    //}
}
