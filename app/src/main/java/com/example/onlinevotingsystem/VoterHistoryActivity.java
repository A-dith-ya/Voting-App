package com.example.onlinevotingsystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.onlinevotingsystem.data.RealTimeDatabase;
import com.google.firebase.database.DatabaseReference;

public class VoterHistoryActivity extends AppCompatActivity {
    private LinearLayout topicContainer;
    private String voterName;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_history);

        // Receive voter name
        Intent intent = getIntent();
        voterName = intent.getStringExtra("Name");
        topicContainer = findViewById(R.id.voting_history_ll); // Contain voting topic history

        // Display Voter History
        RealTimeDatabase.displayVoterHistory(voterName,this,topicContainer);
    }

    // Go Voter view
    public void voterScreen(View view) {
        startActivity(passIntent(VoterActivity.class));
    }

    // Clear voter history
    public void deleteVotingHistory(View view){
        RealTimeDatabase.deleteVotingHistory(voterName);
        // Refresh layout
        finish();
        startActivity(passIntent(VoterHistoryActivity.class));
    }

    // Hold user name via intent
    public Intent passIntent(Class screenName){
        Intent intent = new Intent(this, screenName);
        intent.putExtra("Name", voterName);
        return intent;
    }
}