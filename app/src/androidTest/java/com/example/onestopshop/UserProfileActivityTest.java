package com.example.onestopshop;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
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

import java.util.concurrent.CountDownLatch;

/*
    NOTE TO TA: YOU MAY NEED TO RUN TESTS AGAIN IF TEST FAILS DUE TO ASYNCHRONOUS CALLS
 */
@LargeTest
public class UserProfileActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        signIn();
        Intents.init();
    }

    // Delete items in testuser DB
    @After
    public void tearDown() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            InventoryController inventoryController = new InventoryController();
            inventoryController.clearInventory();
        }
        Intents.release();
    }



    @Test
    public void testEmail() {
        onView(withId(R.id.textViewLogin)).perform(click());
        onView(withId(R.id.profile_button)).perform(click());
        InventoryController inventoryController = new InventoryController();
        String email = inventoryController.getUserEmail();
        onView(withId(R.id.tvEmail))
                .check(matches(withText("Email: " + email)));
    }

    @Test
    public void testName() {
        onView(withId(R.id.textViewLogin)).perform(click());
        onView(withId(R.id.profile_button)).perform(click());
        InventoryController inventoryController = new InventoryController();
        String name = inventoryController.getUserName();
        onView(withId(R.id.tvName))
                .check(matches(withText("Name: " + name)));
    }

    @Test
    public void testLogoutProcess() {
        onView(withId(R.id.textViewLogin)).perform(click());
        onView(withId(R.id.profile_button)).perform(click());
        // Click the logout button
        onView(withId(R.id.btnLogout)).perform(click());

        // Since we cannot verify the Toast directly, we assume the logout process is successful if the LoginActivity is launched
        intended(allOf(hasComponent(LoginActivity.class.getName())));

    }

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

}