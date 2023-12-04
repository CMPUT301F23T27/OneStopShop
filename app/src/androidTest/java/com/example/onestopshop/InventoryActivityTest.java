package com.example.onestopshop;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

import android.app.Activity;

import androidx.annotation.IdRes;
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

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class InventoryActivityTest {
    @Rule
    public ActivityScenarioRule<InventoryActivity> activityScenario = new
            ActivityScenarioRule<InventoryActivity>(InventoryActivity.class);


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
    public void testItemIsDisplayed() {
        int itemCount = getItemCount(R.id.item_list);
        // Should be no items displayed to begin with
        onView(withId(R.id.item_list)).check(matches(isDisplayed()))
                .check(matches(hasChildCount(itemCount)));
        // add an item
        addTestItem();
        // There should now be 1 item visible
        onView(withId(R.id.item_list)).check(matches(isDisplayed()))
                .check(matches(hasChildCount(itemCount + 1)));
        // delete test item
        deleteTestItem();
    }
    @Test
    public void testTotalValue_isDisplayed() {
        double totalValue = calculateTotalValue();
        onView(withId(R.id.total_value_layout)).check(matches((isDisplayed())));
        ActivityScenario<InventoryActivity> scenario = activityScenario.getScenario();
        onView(withId(R.id.total_estimated_value)).check(matches(withText("$" + String.format("%.2f", totalValue))));
        addTestItem();
        onView(withId(R.id.total_estimated_value)).
                check(matches(withText("$" + String.format("%.2f", totalValue + 500.00))));
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
        int positionToDelete = getPositionOfTestItem();
        onView(withId(R.id.item_list)).perform(RecyclerViewActions.actionOnItemAtPosition(positionToDelete, click()));
        onView(withId(R.id.btnDelete)).perform(click());
    }

    private double calculateTotalValue() {
        final double[] totalValue = {0.0};
        activityScenario.getScenario().onActivity(activity -> {
            totalValue[0] = activity.calculateTotalEstimatedValue();
        });
        return totalValue[0];
    }

    private int getItemCount(@IdRes int recyclerViewId) {
        final int[] itemCount = {0};
        activityScenario.getScenario().onActivity(activity -> {
            RecyclerView recyclerView = activity.findViewById(recyclerViewId);
            if (recyclerView != null && recyclerView.getAdapter() != null ) {
                itemCount[0] = recyclerView.getAdapter().getItemCount();
            }
        });
        return itemCount[0];
    }

    private int getPositionOfTestItem() {
        final int[] position = {-1};
        activityScenario.getScenario().onActivity(activity -> {
            RecyclerView recyclerView = activity.findViewById(R.id.item_list);
            RecyclerView.Adapter adapter = recyclerView.getAdapter();

            ArrayList<Item> itemList = ((CustomList) adapter).getItemList();
            String testName = "Test";
            String testDate = "2023-12-03";

            for (int i = 0; i < itemList.size(); i++) {
                if (itemList.get(i).getItemName().equals(testName) && itemList.get(i).getPurchaseDate().equals(testDate)) {
                    position[0] = i;
                    break;
                }
            }
        });
        return position[0];
    }

    //}
}
