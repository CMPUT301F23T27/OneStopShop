package com.example.onestopshop;


import androidx.test.ext.junit.runners.AndroidJUnit4;
;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.widget.TextView;

import java.util.Collections;

@RunWith(AndroidJUnit4.class)
public class ViewItemActivityTest {

    @Rule
    public ActivityTestRule<ViewItemActivity> activityRule = new ActivityTestRule<>(ViewItemActivity.class);
//    public ActivityTestRule<ViewItemActivity> activityRule = new ActivityTestRule<>(ViewItemActivity.class, true, false);



    @Test
    public void testDisplayedItemInformation() {
        // Manually launch the activity
        ViewItemActivity activity = activityRule.getActivity();

        // Set the test data directly on the activity's views
        activity.runOnUiThread(() -> {
            TextView itemNameView = activity.findViewById(R.id.itemName);
            TextView descriptionView = activity.findViewById(R.id.tvDescriptionContent);
            TextView dateView = activity.findViewById(R.id.date);
            TextView makeView = activity.findViewById(R.id.make);
            TextView modelView = activity.findViewById(R.id.model);
            TextView serialNumberView = activity.findViewById(R.id.serialnumber);
            TextView estimatedValueView = activity.findViewById(R.id.value);
            TextView tagsView = activity.findViewById(R.id.tags);
            TextView commentsView = activity.findViewById(R.id.comment);

            itemNameView.setText("Product Name: Pants");
            descriptionView.setText("Black Cargo pants");
            dateView.setText("Date of Purchase: 2023-02-15");
            makeView.setText("Make: Zara");
            modelView.setText("Model: High waisted");
            serialNumberView.setText("Serial Number: ");
            estimatedValueView.setText("Estimated Value: $99.90");
            tagsView.setText("Tags: [true to size]");
            commentsView.setText("Comments: size xs");
        });

        // Now use Espresso to check if the TextViews contain the expected values
        onView(withId(R.id.itemName)).check(matches(withText("Product Name: Pants")));
        onView(withId(R.id.tvDescriptionContent)).check(matches(withText("Black Cargo pants")));
        onView(withId(R.id.date)).check(matches(withText("Date of Purchase: 2023-02-15")));
        onView(withId(R.id.make)).check(matches(withText("Make: Zara")));
        onView(withId(R.id.model)).check(matches(withText("Model: High waisted")));
        onView(withId(R.id.serialnumber)).check(matches(withText("Serial Number: ")));
        onView(withId(R.id.value)).check(matches(withText("Estimated Value: $99.90")));
        onView(withId(R.id.tags)).check(matches(withText("Tags: [true to size]")));
        onView(withId(R.id.comment)).check(matches(withText("Comments: size xs")));
    }
}
