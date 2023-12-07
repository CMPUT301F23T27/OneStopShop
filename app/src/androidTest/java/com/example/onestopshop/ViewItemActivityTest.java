package com.example.onestopshop;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/*
    NOTE TO TA: YOU MAY NEED TO RUN TESTS AGAIN IF TEST FAILS DUE TO ASYNCHRONOUS CALLS
 */
@LargeTest
public class ViewItemActivityTest {
    private FirebaseUser user;
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
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
    String itemName = "Test";
    String description = "this is a test";
    String purchaseDate = "2023-01-01";
    String make = "testMake";
    String model = "testModel";
    Double estimatedValue = 200.0;
    String tag = "testTag";
    //ArrayList<String> tags = new ArrayList<>(Arrays.asList("testTag"));


    // add an item and test whether the details are displayed
    @Test
    public void testViewItems() {
        onView(withId(R.id.textViewLogin)).perform(click());
        addTestItem();
        int itemCount = getItemCount(R.id.item_list);
        onView(withId(R.id.item_list)).perform(RecyclerViewActions.actionOnItemAtPosition(itemCount, click()));
        onView(withId(R.id.tvDescriptionContent)).check(matches(withText(description)));
        onView(withId(R.id.date)).check(matches(withText("Date of Purchase:  "+purchaseDate)));
        onView(withId(R.id.make)).check(matches(withText("Make:  " +make)));
        onView(withId(R.id.model)).check(matches(withText("Model:  " +model)));
        onView(withId(R.id.value)).check(matches(withText("Estimated Value:  $" + String.format("%.2f", estimatedValue))));
        onView(withId(R.id.itemName)).check(matches(withText("Product Name: "+itemName)));
        deleteItem();
    }
    public void addTestItem() {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.itemName)).perform(typeText(itemName), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.description)).perform(typeText(description), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.purchaseDate)).perform(replaceText(purchaseDate), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.make)).perform(typeText(make), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.model)).perform(typeText(model), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.estimatedValue)).perform(typeText(""+ estimatedValue), pressKey(KeyEvent.KEYCODE_TAB),
                pressKey(KeyEvent.KEYCODE_TAB), pressKey(KeyEvent.KEYCODE_TAB), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.add_tag_button)).perform(click());
        onView(withId(R.id.editTextTagInput)).perform(typeText(tag), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.buttonCreate)).perform(click());
        onView(withId(R.id.buttonDone)).perform(click());
        onView(withId(R.id.btn_add_item)).perform(click());
    }
    private int getItemCount(@IdRes int recyclerViewId) {
        final int[] itemCount = {0};
        activityRule.getScenario().onActivity(activity -> {
            RecyclerView recyclerView = activity.findViewById(recyclerViewId);
            if (recyclerView != null && recyclerView.getAdapter() != null ) {
                itemCount[0] = recyclerView.getAdapter().getItemCount();
            }
        });
        return itemCount[0];
    }
    private void deleteItem() {
        onView(withId(R.id.btnDelete)).perform(click());
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
