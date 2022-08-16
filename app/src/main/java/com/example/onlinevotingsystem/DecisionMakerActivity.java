package com.example.onlinevotingsystem;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinevotingsystem.fragment.VotingTopic;
import com.example.onlinevotingsystem.data.RealTimeDatabase;
import com.example.onlinevotingsystem.util.ReusedMethods;

public class DecisionMakerActivity extends AppCompatActivity {
    private LinearLayout dashboardContainer;
    private TextView displayName;

    private static Context context1;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision_maker);

        // Receive decision maker name display.
        Intent intent = getIntent();
        displayName = findViewById(R.id.decision_user_name);
        displayName.setText(intent.getStringExtra("Name"));
        dashboardContainer = findViewById(R.id.decision_topic_ll);

        ReusedMethods.randomAvatars(findViewById(R.id.decisionAvatar));

        context1 = this;
        RealTimeDatabase.displayCreatedTopics(context1, dashboardContainer);
    }

    public void createTopic(View view) {
        onCreateTopic();
    }

    // Create topic.
    public void onCreateTopic() {
        // Identify elements of popup.
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.fragment_create_topic_popup, null);
        EditText popupTopic = popupView.findViewById(R.id.topic_popup_title_tv);
        EditText popupOptions = popupView.findViewById(R.id.topic_popup_description_tv);
        Button exitTopic = popupView.findViewById(R.id.topic_popup_cancel_btn);
        Button addTopic = popupView.findViewById(R.id.topic_popup_confirm_btn);

        // Set up popup window.
        PopupWindow popupWindow = new PopupWindow(popupView, 1000, 600, true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        addTopic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                // Topic name validity.
                if (popupTopic.getText().toString().isEmpty() || popupOptions.getText().toString().isEmpty()) {
                    ReusedMethods.messageToast("Fill Out Fields!", getApplicationContext());
                // Option names validity.
                } else if (!popupOptions.getText().toString().matches("(([A-z 0-9]+)*,[A-z 0-9]+)+")) {
                    ReusedMethods.messageToast("No Empty Options!", getApplicationContext());
                } else {
                    // Save topic information
                    RealTimeDatabase.createVotingTopic(popupOptions,popupTopic);

                    // Display created topic
                    saveTopicData(popupTopic.getText().toString(), popupOptions.getText().toString());
                    popupWindow.dismiss();
                }
            }
        });
        exitTopic.setOnClickListener(new View.OnClickListener() {
            // Cancel topic creation.
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveTopicData(String s1, String s2) {
        // Displays created topic on screen.
        VotingTopic cs1 = new VotingTopic(this);
        dashboardContainer.addView(cs1, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        cs1.setTitle(s1);
        cs1.setInformation(s2);
        cs1.setDate();
    }

    // Change UI screen.
    public void logOutManager(View view) {
        nameScreen(LoginActivity.class);
    }

    public void nameScreen(Class screenName) {
        Intent intent = new Intent(this, screenName);
        startActivity(intent);
    }
    public void deleteAccount2(View view){
        RealTimeDatabase.deleteAccount(this,displayName.getText().toString());
    }
}