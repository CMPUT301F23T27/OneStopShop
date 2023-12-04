package com.example.onestopshop;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class UserProfileActivityTest {

    @Rule
    public ActivityScenarioRule<UserProfileActivity> activityRule =
            new ActivityScenarioRule<>(UserProfileActivity.class);

    //Note: You must first log in to the app through the emulator then the tests will run due to one tap sign in

    @Test
    public void testEmail(){
        InventoryController inventoryController = new InventoryController();
        String email = inventoryController.getUserEmail();
        onView(withId(R.id.tvEmail))
                .check(matches(withText("Email: " + email)));
    }

    @Test
    public void testName(){
        onView(withId(R.id.tvName))
                .check(matches(withText("Name: Anha")));
    }

    @Test
    public void testlButton() {
        // Click on the logout button
        onView(withId(R.id.btnLogout))
                .perform(ViewActions.click());

        // Verify that LoginActivity is launched after logout
        onView(withId(R.id.login))
                .check(matches(ViewMatchers.isDisplayed()));
    }

}
