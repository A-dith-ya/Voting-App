package com.example.onlinevotingsystem.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.onlinevotingsystem.DecisionMakerActivity;
import com.example.onlinevotingsystem.LoginActivity;
import com.example.onlinevotingsystem.R;
import com.example.onlinevotingsystem.VoterActivity;
import com.example.onlinevotingsystem.fragment.VotingTopic;
import com.example.onlinevotingsystem.fragment.VoterHistory;
import com.example.onlinevotingsystem.fragment.VotingView;
import com.example.onlinevotingsystem.util.ReusedMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealTimeDatabase {
    private static DatabaseReference mDatabase; // Reference real time firebase node

    // All the root nodes for storing information
    public final static String USER_ACCOUNTS = "UserAccounts";
    public final static String VOTING_TOPICS = "VotingTopics";
    public final static String VOTE_HISTORY = "VoteHistory";
    public final static String VOTE_TRACK = "VoteTrack";

    public RealTimeDatabase() {
    }

    public static void createUserAccount(String Name, String Password, String Role) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(USER_ACCOUNTS);

        // Initializing user information
        mDatabase.child(Name).child("Password").setValue(Password);
        mDatabase.child(Name).child("Role").setValue(Role);
    }

    public static void updateVoteCount(String username, String TopicName, String Option, double number) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(VOTING_TOPICS);

        // Increasing vote count
        mDatabase.child(TopicName).child(Option).setValue(number);
        mDatabase.child(VOTE_TRACK).child(TopicName + username).setValue("Voted");

        // Logging voter history
        FirebaseDatabase.getInstance().getReference().child(VOTE_HISTORY).child(username).child(TopicName).setValue(Option);
    }

    public static void deleteAccount(String accountName) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(USER_ACCOUNTS).child(accountName);

        // Delete user account
        mDatabase.removeValue();
    }

    public static void deleteAccount(Context context, String accountName){
        // User confirmation for account deletion
        AlertDialog deleteAccountDialogBox = new AlertDialog.Builder(context)
                .setTitle("Delete Account Information!")
                .setMessage("Will result in clearing username, password, voting history.")
                .setIcon(R.drawable.closeaccount)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteVotingHistory(accountName);   // Delete user voter history
                        deleteAccount(accountName); // Delete user account

                        // Directing user back to login
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                })
                // User doesn't close account
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        deleteAccountDialogBox.show();
    }

    public static void deleteVotingHistory(String userName) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(VOTE_HISTORY).child(userName);

        // Delete voter history
        mDatabase.removeValue();
    }

    // Account control directive- directs user to voter/manager view based on credentials.
    public static void verifyAccount(String userName, Context context, TextInputLayout userNameLayout, TextInputLayout userPassLayout, String userPassword) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(USER_ACCOUNTS).child(userName);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check that user name exists
                if (snapshot.getKey() == null) {
                    ReusedMethods.displayError(userNameLayout, "User Name Does Not Exist!");
                }
                // Check password valid
                if (snapshot.getValue() != null) {
                    UserAccount loginAccount = snapshot.getValue(UserAccount.class);
                    if (!loginAccount.getPassword().equals(userPassword)) {
                        ReusedMethods.displayError(userPassLayout, "Incorrect Password!");
                        return;
                    }
                    // Go to voter/decision maker view
                    if (loginAccount.getRole().equals("Voter")) {
                        goAccountView(context,VoterActivity.class, userName);
                        return;
                    } else {
                        goAccountView(context,DecisionMakerActivity.class, userName);
                        return;
                    }

                } else {
                    ReusedMethods.displayError(userNameLayout, "User Name Does Not Exist!");
                }
            }
            // Firebase fails to read value
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public static void goAccountView(Context context, Class screenName, String userName) {
        Intent intent = new Intent(context, screenName);
        // Sending user name
        intent.putExtra("Name", userName);
        context.startActivity(intent);
    }

    public static void createVotingTopic(EditText popupOptions, EditText popupTopic){
        mDatabase = FirebaseDatabase.getInstance().getReference().child(VOTING_TOPICS);

        String delimiter = ",";
        String[] optionsDelimited = popupOptions.getText().toString().split(delimiter);

        for (String i: optionsDelimited) {
            mDatabase.child(popupTopic.getText().toString()).child(i).setValue(0.1);
            mDatabase.child(popupTopic.getText().toString()).child(i).setValue(0.1);
            mDatabase.child(popupTopic.getText().toString()).child(i).setValue(0.1);
        }
    }

    public static void displayCreatedTopics(Context context1, LinearLayout dashboardContainer){
        // Retrieve
        mDatabase = FirebaseDatabase.getInstance().getReference().child(VOTING_TOPICS);
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> snapshot) {
                for(DataSnapshot snap: snapshot.getResult().getChildren()) {
                    String key = snap.getKey();

                    if(snap.getKey().equals(VOTE_TRACK))
                        continue;

                    String x = "";
                    for(DataSnapshot snap2: snapshot.getResult().child(key).getChildren()) {
                        x = x + "," + snap2.getKey();
                    }
                    String value = String.join(",", x);
                    VotingTopic cs1 = new VotingTopic(context1);
                    dashboardContainer.addView(cs1, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    cs1.setTitle(key);
                    cs1.setInformation(value);
                    cs1.setDate();
                }
            }
        });
    }
    
    public static void displayVoterOption(String userName, LinearLayout topicContainer, Context context){
        // Display available voting topics
        mDatabase = FirebaseDatabase.getInstance().getReference().child(VOTING_TOPICS);
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> snapshot) {
                List<String> userTopics = new ArrayList<String>();
                // Retrieve topics voter voted on
                for (DataSnapshot snap : snapshot.getResult().child(VOTE_TRACK).getChildren()) {
                    if (snap.getKey().contains(userName))
                        userTopics.add(snap.getKey());
                }

                // Retrieve topic title and options information
                for (DataSnapshot snap : snapshot.getResult().getChildren()) {
                    String key = snap.getKey();
                    if (key.equals(VOTE_TRACK))
                        continue;

                    if (userTopics.contains(key + userName))
                        continue;

                    VotingView cs1 = new VotingView(context);
                    topicContainer.addView(cs1, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                    // Display topic title
                    cs1.setVotingTitle(key);
                    for (DataSnapshot ent : snapshot.getResult().child(key).getChildren()) {
                        // Display option name
                        cs1.addButton(ent.getKey(), context, userName.toString());
                    }
                }
            }
        });
    }

    public static void displayVoterHistory(String voterName, Context context, LinearLayout topicContainer){
        HashMap<String,String> voterHistoryActive = new HashMap<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(VOTE_HISTORY).child(voterName);

        // Retrieve all topics voter took part in
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> snapshot) {
                // Store all topics
                for (DataSnapshot snap : snapshot.getResult().getChildren()) {
                    voterHistoryActive.put(snap.getKey(), String.valueOf(snap.getValue()));
                }

                // Display voting topic history
                for (Map.Entry<String, String> entry : voterHistoryActive.entrySet()) {
                    VoterHistory cs1 = new VoterHistory(context);
                    topicContainer.addView(cs1, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    cs1.setTitleTextView("Topic: "+entry.getKey());
                    cs1.setInformationTextView("Vote: "+entry.getValue());
                }
            }
        });
    }
    
    public static void deleteVotingTopicNode(String votingTopicTitle){
        // Delete topic in "VotingTopics" node
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(VOTING_TOPICS).child(votingTopicTitle);
        mDatabase.removeValue();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(VOTING_TOPICS).child(VOTE_TRACK);
        FirebaseDatabase.getInstance().getReference().child(VOTE_TRACK).child(votingTopicTitle).removeValue();

        ArrayList<String> topicDataList = new ArrayList<String>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appleSnapshot : snapshot.getChildren()) {
                    if (appleSnapshot.getKey().contains(votingTopicTitle)) {
                        topicDataList.add(appleSnapshot.getKey());
                        for (int counter = 0; counter < topicDataList.size(); counter++) {
                            FirebaseDatabase.getInstance().getReference().child(VOTING_TOPICS).child(VOTE_TRACK).child(topicDataList.get(counter)).removeValue();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static void voterVotes(String optionName, String username, String buttonName){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child(VOTING_TOPICS).child(optionName);
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> snapshot) {
                for (DataSnapshot optionVal : snapshot.getResult().getChildren()) {
                    double optionCount = (double) optionVal.getValue();
                    // Set option value
                    if (optionVal.getKey().equals(buttonName)) {
                        RealTimeDatabase.updateVoteCount(username, optionName, buttonName, optionCount + 1);
                        return;
                    }
                }
            }
        });
    }
}
