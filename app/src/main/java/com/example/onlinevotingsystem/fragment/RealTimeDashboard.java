package com.example.onlinevotingsystem.fragment;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;

import androidx.annotation.NonNull;

import com.example.onlinevotingsystem.data.RealTimeDatabase;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RealTimeDashboard {

    public static void setPieData(Context cs1, PieChart pieChart, String topicName) {
        // Initialize Pie Chart Shape.
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText(topicName);
        pieChart.setEntryLabelTextSize(13);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setDrawEntryLabels(true);

        // Hole radius.
        pieChart.setHoleRadius(60f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);


        // Center Title Size.
        pieChart.setCenterTextSize(20);
        pieChart.getDescription().setEnabled(false);

        // PieChart Legend.
        Legend l = pieChart.getLegend();
        l.setTextSize(13);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setTextColor(Color.BLACK);

        // Entering Animation.
        l.setYOffset(10f);
        pieChart.animateY(1600, Easing.EaseInOutQuad);
        addPieData(cs1, pieChart, topicName);
    }

    public static void addPieData(Context cs1, PieChart pieChart, String topicName) {
        HashMap<String, Double> optionsData = new HashMap<String, Double>();

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child(RealTimeDatabase.VOTING_TOPICS);

        // Retrieving Topic Option votes
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> snapshot) {
                for (DataSnapshot snap : snapshot.getResult().child(topicName).getChildren()) {
                    optionsData.put(snap.getKey(), Math.floor((Double) snap.getValue()));
                }

                // Total Vote Count
                int totalVotes = 0;
                for (Double optionVote : optionsData.values()) {
                    totalVotes += optionVote;
                }

                // Setting PieChart Values
                ArrayList<PieEntry> individualVoteCount = new ArrayList<>();
                if (totalVotes != 0) {
                    for (Map.Entry<String, Double> currentOption : optionsData.entrySet()) {
                        String key = currentOption.getKey();
                        Double value = currentOption.getValue();
                        float x = (float) (value / (float) totalVotes);
                        individualVoteCount.add(new PieEntry(x, key));

                    }
                } else {
                    // Indicate no current data
                    pieChart.setCenterText(Html.fromHtml(topicName + "<font color=red><br>" + "No Data Exists" + "<br></font>"));
                    for (String key : optionsData.keySet()) {
                        individualVoteCount.add(new PieEntry(0f, key));
                    }
                }

                // Colors for different parts of PieChart.
                ArrayList<Integer> colourList = new ArrayList<>();
                for (int colorVal : ColorTemplate.MATERIAL_COLORS) {
                    colourList.add(colorVal);
                }

                // Setting PieChart data
                PieDataSet dataSet = new PieDataSet(individualVoteCount, "Results");
                dataSet.setDrawIcons(false);
                dataSet.setColors(colourList);
                dataSet.setSliceSpace(3.5f);

                PieData data = new PieData(dataSet);
                data.setValueFormatter(new PercentFormatter(pieChart));
                pieChart.setUsePercentValues(true);
                data.setValueTextSize(12);
                data.setValueTextColor(Color.BLACK);

                pieChart.setData(data);
                pieChart.invalidate();
            }
        });
    }
}
