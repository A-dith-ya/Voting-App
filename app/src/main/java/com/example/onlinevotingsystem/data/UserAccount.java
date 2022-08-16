package com.example.onlinevotingsystem.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.onlinevotingsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserAccount {

    // User current information details
    private String Name;
    private String Password;
    private String Role;

    UserAccount() {
    }

    UserAccount(String Role, String Password, String Name) {
        this.Name = Name;
        this.Password = Password;
        this.Role = Role;
    }

    // Setter and getter methods for accessing user information
    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }

    public String getRole() {
        return Role;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setRole(String role) {
        Role = role;
    }
}
