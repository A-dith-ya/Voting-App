package com.example.onlinevotingsystem.fragment;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.onlinevotingsystem.R;

public class VotingView extends RelativeLayout {
    private TextView topicTitle;
    private LinearLayout buttonContainer;
    static int id = 0;

    public VotingView(Context context) {
        super(context);
        inflate(context, R.layout.fragment_user_voting_topic, this);
        topicTitle = findViewById(R.id.user_voting_topic_title_tv);
    }

    // Set topic Title
    public void setVotingTitle(String topicTitle) {
        this.topicTitle.setText(topicTitle);
        invalidate();
        requestLayout();
    }

    public String getVotingTitle(String s1) {
        return topicTitle.getText().toString();
    }

    // Voting Topic Button.
    public void addButton(String buttonText, Context context, String username) {
        buttonContainer = (LinearLayout) findViewById(R.id.voting_topic_container_ll);
        VotingOption bt1 = new VotingOption(context);
        bt1.setButtonText(buttonText);
        bt1.buttonAction(context, buttonContainer, topicTitle, username);
        buttonContainer.addView(bt1, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }
}
