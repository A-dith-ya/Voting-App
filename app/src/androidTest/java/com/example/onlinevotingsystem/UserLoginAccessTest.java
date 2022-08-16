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

public class UserLoginAccessTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);
    @Test
    public void logAccount() {
        // Account Credentials
        String userName = "Alfredo";
        String password = "Al123$";

        //Select the username in the register
        ViewInteraction newLoginUserName = onView(withId(R.id.login_username_tv));
        //Type the new username
        newLoginUserName.perform(typeText(userName), closeSoftKeyboard());
        //Go to the password in register view
        ViewInteraction newLoginPassWord = onView(withId(R.id.login_userpass_tv));
        //Type the new password
        newLoginPassWord.perform(typeText(password), closeSoftKeyboard());
        //Click on the login button
        onView(withId(R.id.login_go_user_screen)).perform(click());
    }
}
