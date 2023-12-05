package com.example.onestopshop;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;

/*
    NOTE: YOU MUST LOG IN MANUALLY FIRST BY RUNNING THE APP FOR TEST TO WORK DUE TO AUTH
 */
@LargeTest
public class UserProfileActivityTest {

    @Rule
    public ActivityScenarioRule<UserProfileActivity> activityRule =
            new ActivityScenarioRule<>(UserProfileActivity.class);

    @Rule
    public IntentsTestRule<UserProfileActivity> intentsTestRule =
            new IntentsTestRule<>(UserProfileActivity.class);

    @Test
    public void testEmail() {
        InventoryController inventoryController = new InventoryController();
        String email = inventoryController.getUserEmail();
        onView(withId(R.id.tvEmail))
                .check(matches(withText("Email: " + email)));
    }

    @Test
    public void testName() {
        InventoryController inventoryController = new InventoryController();
        String name = inventoryController.getUserName();
        onView(withId(R.id.tvName))
                .check(matches(withText("Name: " + name)));
    }

    @Test
    public void testLogoutProcess() {
        // Click the logout button
        onView(withId(R.id.btnLogout)).perform(click());

        // Since we cannot verify the Toast directly, we assume the logout process is successful if the LoginActivity is launched
        intended(allOf(hasComponent(LoginActivity.class.getName())));

    }
}