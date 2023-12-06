package com.example.onestopshop;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;

import android.view.KeyEvent;

import androidx.annotation.IdRes;
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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;


/*
    NOTE TO TA: YOU MUST LOG OUT MANUALLY FIRST BY RUNNING THE APP FOR TEST TO WORK DUE TO ONE TAP SIGN IN
 */
public class TagsTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private String itemName = "Test";
    private String description = "this is a test";
    private String purchaseDate = "2023-01-01";
    private String make = "testMake";
    private String model = "testModel";
    private Double estimatedValue = 200.0;
    private String tag = "testTag";


    // add tag to an item, check that chips are displayed
    @Test
    public void testAddTag(){
        onView(withId(R.id.textViewLogin)).perform(click());
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.itemName)).perform(typeText(itemName), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.description)).perform(typeText(description), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.purchaseDate)).perform(replaceText(purchaseDate), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.make)).perform(typeText(make), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.model)).perform(typeText(model), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.estimatedValue)).perform(typeText(""+ estimatedValue), pressKey(KeyEvent.KEYCODE_TAB),
                pressKey(KeyEvent.KEYCODE_TAB), pressKey(KeyEvent.KEYCODE_TAB), pressKey(KeyEvent.KEYCODE_TAB));
        // add tag
        onView(withId(R.id.add_tag_button)).perform(click());
        onView(withId(R.id.editTextTagInput)).perform(typeText(tag), pressKey(KeyEvent.KEYCODE_TAB));
        onView(withId(R.id.buttonCreate)).perform(click());
        onView(withId(R.id.buttonDone)).perform(click());
        onView(withId(R.id.addFragmentTagsChipGroup)).check(matches(hasChildCount(1)));
        onView(withId(R.id.addFragmentTagsChipGroup))
                .check(matches(hasDescendant(withText(tag))));


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
        onView(withId(R.id.editTextTagInput)).perform(typeText("deleteTag"));
        onView(withId(R.id.buttonCreate)).perform(click());
        onView(withId(R.id.buttonDone)).perform(click());
        onView(withId(R.id.btn_add_item)).perform(click());
    }
    @Before
    public void signIn(){
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
        int itemCount = getItemCount(R.id.item_list);
        onView(withId(R.id.item_list)).perform(RecyclerViewActions.actionOnItemAtPosition(itemCount, click()));
        onView(withId(R.id.btnDelete)).perform(click());
    }
}
