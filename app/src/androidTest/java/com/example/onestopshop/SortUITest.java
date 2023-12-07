package com.example.onestopshop;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/*
    NOTE TO TA: YOU MAY NEED TO RUN TESTS AGAIN IF TEST FAILS DUE TO ASYNCHRONOUS CALLS
 */
public class SortUITest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenario = new
            ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testSortByDateAscending() {
        onView(withId(R.id.textViewLogin)).perform(click());
        // Add multiple items with different dates
        addTestItem("2023-01-01");
        addTestItem("2023-02-01");
        addTestItem("2023-03-01");

        // Sort by Date (Ascending)
        onView(withId(R.id.sort_spinner)).perform(click());
        onData(anything()).atPosition(1).perform(click()); // Select "Date Ascending"
        onView(withId(R.id.switch_sort)).perform(click());
        onView(withId(R.id.switch_sort)).perform(click());// Toggle to ascending if not already

        // Verify the order in the RecyclerView
        onView(withId(R.id.item_list)).check(matches(isSortedByDateAscending()));
        deleteTestItem();
        deleteTestItem();
        deleteTestItem();
    }

    @Test
    public void testSortByDateDescending() {
        onView(withId(R.id.textViewLogin)).perform(click());
        // Add multiple items with different dates
        addTestItem("2023-01-01");
        addTestItem("2023-02-01");
        addTestItem("2023-03-01");

        // Sort by Date (Descending)
        onView(withId(R.id.sort_spinner)).perform(click());
        onData(anything()).atPosition(1).perform(click()); // Select "Date Ascending"
        onView(withId(R.id.switch_sort)).perform(click()); // Toggle to descending

        // Verify the order in the RecyclerView
        onView(withId(R.id.item_list)).check(matches(isSortedByDateDescending()));
        deleteTestItem();
        deleteTestItem();
        deleteTestItem();
    }

// Similar tests can be created for other sorting criteria
// ...

    private void addTestItem(String purchaseDate) {
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.itemName)).perform(typeText("Test"), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.description)).perform(typeText("Test Item Description"), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.purchaseDate)).perform(replaceText(purchaseDate), pressKey(KeyEvent.KEYCODE_TAB));
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

    private static Matcher<View> isSortedByDateAscending() {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                RecyclerView recyclerView = (RecyclerView) item;
                RecyclerView.Adapter adapter = recyclerView.getAdapter();

                if (adapter != null) {
                    for (int i = 0; i < adapter.getItemCount() - 1; i++) {
                        Item current = ((CustomList) adapter).getItemList().get(i);
                        Item next = ((CustomList) adapter).getItemList().get(i + 1);

                        // Logging statements to check data
                        Log.d("Matcher", "Current Date: " + current.getPurchaseDate());
                        Log.d("Matcher", "Next Date: " + next.getPurchaseDate());

                        if (current.getPurchaseDate().compareTo(next.getPurchaseDate()) > 0) {
                            return false; // Not sorted in ascending order
                        }
                    }
                }

                return true; // Sorted in ascending order
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("RecyclerView is sorted by date in ascending order");
            }
        };
    }



    private static Matcher<View> isSortedByDateDescending() {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                RecyclerView recyclerView = (RecyclerView) item;
                RecyclerView.Adapter adapter = recyclerView.getAdapter();

                if (adapter != null) {
                    for (int i = 0; i < adapter.getItemCount() - 1; i++) {
                        Item current = ((CustomList) adapter).getItemList().get(i);
                        Item next = ((CustomList) adapter).getItemList().get(i + 1);

                        if (current.getPurchaseDate().compareTo(next.getPurchaseDate()) < 0) {
                            return false; // Not sorted in descending order
                        }
                    }
                }

                return true; // Sorted in descending order
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("RecyclerView is sorted by date in descending order");
            }
        };
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
