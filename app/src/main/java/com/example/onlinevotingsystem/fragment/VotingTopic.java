package com.example.onlinevotingsystem.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.data.RealTimeDatabase;
import com.github.mikephil.charting.charts.PieChart;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class VotingTopic extends RelativeLayout {
    private TextView votingTopicTitle;
    private TextView optionsInfo;
    private TextView topicDate;
    private Button deleteTopic;
    private ImageButton displayDashboard;

    PieChart votingTopicChart;

    // Topic details.
    public VotingTopic(Context context) {
        super(context);

        // Connecting all layout elements
        inflate(context, R.layout.fragment_created_topic, this);
        votingTopicTitle = findViewById(R.id.voting_topic_title_tv);
        optionsInfo = findViewById(R.id.voting_topic_options_tv);
        topicDate = findViewById(R.id.voting_topic_date_creation_tv);
        deleteTopic = findViewById(R.id.voting_topic_delete_btn);
        displayDashboard = findViewById(R.id.voting_topic_dashboard_imgbtn);

        // Real time dashboard access
        displayDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflateDashboard = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dashboardView = inflateDashboard.inflate(R.layout.fragment_realtime_dashboard, null);
                votingTopicChart = dashboardView.findViewById(R.id.dashboard_pc);

                // Initialize real time dashboard data
                RealTimeDashboard.setPieData(context, votingTopicChart, votingTopicTitle.getText().toString());

                // Display real time dashboard
                final PopupWindow popupWindow = new PopupWindow(dashboardView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setOutsideTouchable(true);

                // Dashboard location on view
                popupWindow.showAtLocation(dashboardView, Gravity.CENTER, 0, 0);
            }
        });

        // Delete voting topic information
        deleteTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize delete dialog
                AlertDialog deleteTopicDialogBox = new AlertDialog.Builder(context)
                        .setTitle("Delete Voting Topic!")
                        .setMessage("Will result in losing all selected Topic-Related information.")
                        .setIcon(R.drawable.trash_can)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                RealTimeDatabase.deleteVotingTopicNode(votingTopicTitle.getText().toString());

                                // Remove voting topic elements
                                displayDashboard.setVisibility(View.GONE);
                                votingTopicTitle.setVisibility(View.GONE);
                                optionsInfo.setVisibility(View.GONE);
                                topicDate.setVisibility(View.GONE);
                                deleteTopic.setVisibility(View.GONE);
                                dialog.dismiss();
                            }
                        })
                        // Cancel deleting voting topic information
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                deleteTopicDialogBox.show();
            }
        });
    }

    public VotingTopic(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.fragment_created_topic, this);
        votingTopicTitle = findViewById(R.id.voting_topic_title_tv);
        optionsInfo = findViewById(R.id.voting_topic_options_tv);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomView,
                0, 0);
        try {
            setTitle(a.getString(R.styleable.CustomView_setTitle));
            setInformation(a.getString(R.styleable.CustomView_setSubTitle));
        } finally {
            a.recycle();
        }
    }

    // Set Topic Title.
    public void setTitle(String title) {
        votingTopicTitle.setText(title);
        invalidate();
        requestLayout();
    }

    // Set Topic Information.
    public void setInformation(String information) {
        optionsInfo.setText(information);
        invalidate();
        requestLayout();
    }

    // Set Topic Date.
    public void setDate() {
        String date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        topicDate.setText("Created On " + date);
    }
}

