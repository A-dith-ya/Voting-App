package com.example.onlinevotingsystem.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.onlinevotingsystem.LoginActivity;
import com.example.onlinevotingsystem.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class ReusedMethods {

    public static void displayError(TextInputLayout usernameLayout, String errorMessage) {
        usernameLayout.setError(errorMessage);
        usernameLayout.setErrorIconDrawable(R.drawable.error_img);
        usernameLayout.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.RED));
        usernameLayout.setErrorIconTintList(ColorStateList.valueOf(Color.RED));
    }

    public static void clickError(TextInputLayout usernameLayout, Drawable errorImage) {
        usernameLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                usernameLayout.setError(null);
                usernameLayout.setErrorIconDrawable(null);
                usernameLayout.setStartIconDrawable(errorImage);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public static void randomAvatars(ImageView randomAvatarImage) {
        int[] images = {R.drawable.ava1,R.drawable.ava2,R.drawable.ava3,R.drawable.ava4};
        Random rand = new Random();
        randomAvatarImage.setBackgroundResource(images[rand.nextInt(images.length)]);
    }

    public static String passwordPattern = "(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$&*]).{8,20}";    // Require 1 number, 1 lower case, 1 uppercase, 1 special character, minimum 8 char, maximum 20 chars

    // Information aid user shown on screen.
    public static void messageToast(String displayMessage, Context context) {
        Toast toast;
        // Short toast message
        toast = Toast.makeText(context.getApplicationContext(), displayMessage, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void messageToast2(Context context, String displayMessage) {
        Toast toast;
        // Long toast message
        toast = Toast.makeText(context.getApplicationContext(), displayMessage, Toast.LENGTH_LONG);
        toast.show();
    }

    public static String optionPattern = "(([A-z 0-9]+)*,[A-z 0-9]+)+"; // Checks comma seperated list and no empty options
    public static Boolean optionIsValid(String optionName){
        // Sends back
        if (optionName.matches(optionPattern))
            return true;
        return false;
    }
}
