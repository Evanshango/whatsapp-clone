package com.evans.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    private static final String TAG = "HomePage";
    private FirebaseAuth mAuth;
    private Button mLogOut, mFindUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mAuth = FirebaseAuth.getInstance();

        initViews();

        getPermissions();

        mFindUsers.setOnClickListener(v -> goToFindUsers());
        mLogOut.setOnClickListener(v -> doLogOut());
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.READ_CONTACTS}, 1);
        } else {
            Log.d(TAG, "getPermissions: No permissions granted");
        }
    }

    private void goToFindUsers() {
        startActivity(new Intent(this, FindUsers.class));
    }

    private void doLogOut() {
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void initViews() {
        mLogOut = findViewById(R.id.btnLogOut);
        mFindUsers = findViewById(R.id.btnFindUsers);
    }
}
