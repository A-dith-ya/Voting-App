package com.example.onlinevotingsystem.fragment;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.data.RealTimeDatabase;
import com.example.onlinevotingsystem.util.ReusedMethods;
import com.github.mikephil.charting.utils.ColorTemplate;

public class VotingOption extends RelativeLayout {
    private Button individualButton;
    private static int color = 0;

    // Voting Topic Initialization.
    public VotingOption(Context context) {
        super(context);
        inflate(context, R.layout.fragment_voting_option, this);
        individualButton = findViewById(R.id.option_user_btn);

        // Different color for side by side buttons
        if(++color>=ColorTemplate.MATERIAL_COLORS.length)
            color=0;
        individualButton.setBackgroundColor(ColorTemplate.MATERIAL_COLORS[color]);
    }

    // Option name.
    public void setButtonText(String s) {
        individualButton.setText(s);
    }

    public void buttonAction(Context context, View container, TextView optionView, String username) {
        individualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increase vote count
                RealTimeDatabase.voterVotes(optionView.getText().toString(), username, individualButton.getText().toString());

                // Success message
                ReusedMethods.messageToast("Successfully Voted", context);

                // Clears voting topic and options.
                container.setVisibility(View.GONE);
                optionView.setVisibility(View.GONE);
            }
        });
    }


}
