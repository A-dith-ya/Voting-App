package com.example.onlinevotingsystem;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class UserAccountCreationTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void createAccount() {
        // Account Credentials
        String username = "Alfredo";
        String password = "Al123$";

        // Find view by Id (Go to log in view)
        ViewInteraction loginScreen = onView(withId(R.id.login_username_tv));
        //Click on the register button
        onView(withId(R.id.login_go_register_btn)).perform(click());

        //Select the username in the register
        ViewInteraction newRegisterUserName = onView(withId(R.id.register_username_tv));
        //Type the new username
        newRegisterUserName.perform(typeText(username), closeSoftKeyboard());
        //Go to the password in register view
        ViewInteraction newRegisterPassWord = onView(withId(R.id.register_user_pass_tv));
        //Type the new password
        newRegisterPassWord.perform(typeText(password), closeSoftKeyboard());
        //Click on the manager option
        onView(withId(R.id.register_manager_rb)).perform(click());
        //Click on the register button
        onView(withId(R.id.register_create_account_btn)).perform(click());
    }
}
