package com.example.onestopshop;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static androidx.test.espresso.assertion.ViewAssertions.matches;

import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class InventoryActivityTest {
    @Rule
    public ActivityScenarioRule<InventoryActivity> scenario = new
            ActivityScenarioRule<InventoryActivity>(InventoryActivity.class);
    @Test
    public void testTopToolbar_isDisplayed() {
        onView(withId(R.id.toolbar_top)).check(matches(isDisplayed()));
    }

    @Test
    public void testBottomToolbar_isDisplayed() {
        onView(withId(R.id.toolbar_bottom)).check(matches(isDisplayed()));
    }

    @Test
    public void testTotalValue_isDisplayed() {
        onView(withId(R.id.total_value_layout)).check(matches((isDisplayed())));
    }

    //}
}
