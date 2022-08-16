package com.example.onlinevotingsystem.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.onlinevotingsystem.R;

public class VoterHistory extends RelativeLayout {
    private TextView titleTextView;
    private TextView informationTextView;

    public VoterHistory(Context context) {
        super(context);
        inflate(context, R.layout.fragment_voting_history, this);
        titleTextView = findViewById(R.id.voting_topic_history_tv);
        informationTextView = findViewById(R.id.voting_topic_options_history_tv);
    }

    public VoterHistory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VoterHistory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Setting topic title
    public void setTitleTextView(String topicTitle) {
        titleTextView.setText(topicTitle);
        invalidate();
        requestLayout();
    }

    // Setting option name
    public void setInformationTextView(String topicOption) {
        informationTextView.setText(topicOption);
        invalidate();
        requestLayout();
    }
}
