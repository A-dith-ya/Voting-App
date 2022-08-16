package com.example.onlinevotingsystem;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinevotingsystem.data.RealTimeDatabase;
import com.example.onlinevotingsystem.util.ReusedMethods;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.plattysoft.leonids.ParticleSystem;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout userNameLayout; // Main Identification ID.
    private TextInputLayout userPassLayout; // Authorization Password for account.
    private EditText loginName;          // Account ID.
    private EditText loginPassword;      // Account authorization.

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameLayout = findViewById(R.id.login_username_il);
        userPassLayout = findViewById(R.id.login_pass_il);
        loginName = findViewById(R.id.login_username_tv);
        loginPassword = findViewById(R.id.login_userpass_tv);

        // Successful account creation display.
        String accountSuccess = getIntent().getStringExtra("EXTRA_SESSION_ID");
        if (accountSuccess != null)
            if (accountSuccess.equals("Success"))
                congratsAccount();

        ReusedMethods.clickError(userNameLayout, this.getResources().getDrawable(R.drawable.profile_img));
        ReusedMethods.clickError(userPassLayout, this.getResources().getDrawable(R.drawable.pass_key));
    }

    // Account control directive- directs user to voter/manager view based on credentials.
    public void userScreen(View view) {
        RealTimeDatabase.verifyAccount(loginName.getText().toString(),this, userNameLayout, userPassLayout, loginPassword.getText().toString());
    }

    public void goAccountView(Class screenName, String userName) {
        Intent intent = new Intent(this, screenName);
        // Sending user name
        intent.putExtra("Name", userName);
        startActivity(intent);
    }

    // Shower user with glitter.
    public void congratsAccount() {
        ReusedMethods.messageToast2(this, "Successful Account Creation");

        // Setting shower drawable
        ParticleSystem part1 = new ParticleSystem(this, 100, R.drawable.silver, 10000);
        ParticleSystem part2 = new ParticleSystem(this, 100, R.drawable.gold, 10000);

        // Setting shower display delay
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Activate shower after half a second
                part1.setSpeedModuleAndAngleRange(0.3f, 0.3f, 50, 80)
                        .setRotationSpeed(140)
                        .emit(findViewById(R.id.login_holder_top_left_tv), 10);

                part2.setSpeedModuleAndAngleRange(0.3f, 0.3f, 100, 160)
                        .setRotationSpeed(140)
                        .emit(findViewById(R.id.login_holder_top_right_tv), 10);
            }
        }, 500);

        // Setting shower display cancel
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Cancel shower after 6 seconds
                part1.cancel();
                part2.cancel();
            }
        }, 6000);
    }

    // Go Register view
    public void changeScreen(View view) {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}