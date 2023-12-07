package com.example.onestopshop;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/*
    NOTE TO TA: YOU MAY NEED TO RUN TESTS AGAIN IF TEST FAILS DUE TO ASYNCHRONOUS CALLS
 */
public class SearchActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> searchActivityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void searchWithValidKeyword() {
        onView(withId(R.id.textViewLogin)).perform(click());
        // Type a valid search keyword
        addTestItem();
        onView(withId(R.id.search_button)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.search))
                .perform(ViewActions.typeText("Test Item Description"));

        // Click the search button
        Espresso.onView(ViewMatchers.withId(R.id.bsearch))
                .perform(ViewActions.click());

        // Check if the search results are displayed
        Espresso.onView(ViewMatchers.withId(R.id.itemlist))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(withId(R.id.back)).perform(click());
        deleteTestItem();
    }

    @Test
    public void searchWithEmptyKeyword() {
        onView(withId(R.id.textViewLogin)).perform(click());
        onView(withId(R.id.search_button)).perform(click());
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
    private void deleteTestItem() {
        //int positionToDelete = getPositionOfTestItem();
        onView(withId(R.id.item_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.btnDelete)).perform(click());
    }
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