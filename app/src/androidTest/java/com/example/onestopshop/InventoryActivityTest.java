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
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/*
    NOTE TO TA: YOU MAY NEED TO RUN TESTS AGAIN IF TEST FAILS DUE TO ASYNCHRONOUS CALLS
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class InventoryActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenario = new
            ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void testSelectMode() {
        // Login
        onView(withId(R.id.textViewLogin)).perform(click());
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

    /*@Test
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
    }*/
    @Test
    public void testTotalValue_isDisplayed() {
        onView(withId(R.id.textViewLogin)).perform(click());
        double totalValue = 0.0;
        onView(withId(R.id.total_value_layout)).check(matches((isDisplayed())));
        addTestItem();
        //from test item
        totalValue = 500.00;
        onView(withId(R.id.total_estimated_value)).
                check(matches(withText("$" + String.format("%.2f", 500.00))));
        deleteTestItem();
    }


    public void addTestItem() {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.itemName)).perform(typeText("Test"), pressKey(KeyEvent.KEYCODE_TAB));
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

    private void deleteTestItem() {
        //int positionToDelete = getPositionOfTestItem();
        onView(withId(R.id.item_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.btnDelete)).perform(click());
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

    /*private int getPositionOfTestItem() {
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
    }*/

    //}



    @Before
    public void signIn(){
        signOut();
        final CountDownLatch latch = new CountDownLatch(1);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("testuser@gmail.com", "password")
                .addOnCompleteListener(ApplicationProvider.getApplicationContext().getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            latch.countDown();
                        } else {
                            // If sign in fails, display a message to the user.
                        }
                    }
                });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void signOut(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null) {
            onView(withId(R.id.profile_button)).perform(click());
            // Click the logout button
            onView(withId(R.id.btnLogout)).perform(click());
        }
    }
    // Delete items in testuser DB
    @After
    public void tearDown() {
        InventoryController inventoryController = new InventoryController();
        inventoryController.clearInventory();
    }

}