package com.example.onlinevotingsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinevotingsystem.data.RealTimeDatabase;
import com.example.onlinevotingsystem.util.ReusedMethods;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Set;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    private TextInputLayout registerNameLayout;
    private TextInputLayout registerPassLayout;
    private EditText registerName;
    private EditText registerPassword;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerName = findViewById(R.id.register_username_tv);
        registerPassword = findViewById(R.id.register_user_pass_tv);
        registerNameLayout = findViewById(R.id.register_username_il);
        registerPassLayout = findViewById(R.id.register_pass_il);

        // Setting images for input errors
        ReusedMethods.clickError(registerNameLayout, this.getResources().getDrawable(R.drawable.profile_img));
        ReusedMethods.clickError(registerPassLayout, this.getResources().getDrawable(R.drawable.pass_key));

        // Keep track of user touch on elements.
        registerName.setOnFocusChangeListener(this);
        registerPassword.setOnFocusChangeListener(this);
    }

    // Tracks user on active/inactive elements.
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            switch (view.getId()) {
                // Inform validity of username.
                case R.id.register_username_tv:
                    if (registerName.getText().toString() == null || registerName.getText().length() < 4)
                        ReusedMethods.displayError(registerNameLayout, "Must be greater than 4 characters.");
                    break;
                // Inform validity of password.
                case R.id.register_user_pass_tv:
                    if (!Pattern.matches(ReusedMethods.passwordPattern, registerPassword.getText().toString()))
                        ReusedMethods.displayError(registerPassLayout, "1 uppercase,1 lowercase, 1 digit, 1 special character!");
                    break;
            }
        }
    }

    // Confirm/Save user account details.
    public void checkRole(View view) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(RealTimeDatabase.USER_ACCOUNTS, MODE_PRIVATE);
        Set<String> userNameExists = settings.getStringSet(registerName.getText().toString(), null);

        // User name existence.
        if (userNameExists != null) {
            ReusedMethods.displayError(registerNameLayout, "User Name Taken!");
            return;
        }

        // User name validity.
        if (registerName.getText().toString().isEmpty() || registerName.getText().length() < 4) {
            ReusedMethods.displayError(registerNameLayout, "Missing UserName!");
            return;
        }
        // Password validity.
        if (!Pattern.matches(ReusedMethods.passwordPattern, registerPassword.getText().toString())) {
            ReusedMethods.displayError(registerPassLayout, "Invalid Password!");
            return;
        }
        // Role selected by user.
        RadioGroup radioGroup = findViewById(R.id.register_user_role_rg);
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            ReusedMethods.messageToast("Missing Role!", getApplicationContext());
            return;
        }
        // Value of user role.
        int radioId = radioGroup.getCheckedRadioButtonId();
        RadioButton roleButton = (RadioButton) findViewById(radioId);

        // Save user data.
        RealTimeDatabase.createUserAccount(registerName.getText().toString(), registerPassword.getText().toString(), roleButton.getText().toString());

        // Successful Toast message.
        ReusedMethods.messageToast("Account Successfully Created", getApplicationContext());
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("EXTRA_SESSION_ID", "Success");
        startActivity(intent);
    }

    // Go login screen.
    public void changeScreen(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}

