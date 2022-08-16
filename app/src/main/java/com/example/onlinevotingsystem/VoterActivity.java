package com.example.onlinevotingsystem;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinevotingsystem.data.RealTimeDatabase;
import com.example.onlinevotingsystem.util.ReusedMethods;
import com.google.firebase.database.DatabaseReference;

public class VoterActivity extends AppCompatActivity {
    private TextView userNameDisplay;
    private LinearLayout topicContainer;

    private DatabaseReference mDatabase;
    private static Context context2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter);

        // Receive voter name display.
        Intent intent = getIntent();
        userNameDisplay = findViewById(R.id.user_name_tv);

        topicContainer = findViewById(R.id.voter_container_ll);  // Container for holding topic information
        context2 = this;    // Current screen layout

        userNameDisplay.setText(intent.getStringExtra("Name")); // Display user name received from intent
        ReusedMethods.randomAvatars(findViewById(R.id.user_avatar_tv));   // User Avatar

        // Display available voting topics
        RealTimeDatabase.displayVoterOption(userNameDisplay.getText().toString(),topicContainer,context2);
    }

    // Change UI screens.
    public void logOutUser(View view) {
        changeScreen(LoginActivity.class);
    }

    // Delete user account
    public void deleteAccount(View view) {
        RealTimeDatabase.deleteAccount(this, userNameDisplay.getText().toString());
    }

    public void changeScreen(Class screenName) {
        Intent intent = new Intent(this, screenName);
        startActivity(intent);
    }

    // Go VotingHistory view
    public void goVoteHistory(View view) {
        Intent intent = new Intent(this, VoterHistoryActivity.class);
        intent.putExtra("Name", userNameDisplay.getText().toString());
        startActivity(intent);
    }
}