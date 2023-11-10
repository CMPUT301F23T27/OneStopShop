package com.example.onestopshop;

import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;

@LargeTest
public class UserProfileActivityTest {

    @Rule
    public ActivityScenarioRule<UserProfileActivity> activityRule =
            new ActivityScenarioRule<>(UserProfileActivity.class);

    @Test
    public void testEmail(){
        Espresso.onView(ViewMatchers.withId(R.id.tvEmail))
                .check(ViewAssertions.matches(withText("Email: vehla996@gmail.com")));
    }

    @Test
    public void testName(){
        Espresso.onView(ViewMatchers.withId(R.id.tvName))
                .check(ViewAssertions.matches(withText("Name: Anha")));
    }

    @Test
    public void testlButton() {
        // Click on the logout button
        Espresso.onView(ViewMatchers.withId(R.id.btnLogout))
                .perform(ViewActions.click());

        // Verify that LoginActivity is launched after logout
        Espresso.onView(ViewMatchers.withId(R.id.login))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

}
