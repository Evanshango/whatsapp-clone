package com.evans.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText mPhone, mCode;
    private Button mVerify;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        userIsLoggedIn();

        initViews();

        mVerify.setOnClickListener(v -> {
            if (mVerificationId != null)
                verifyPhoneWithCode();
            else
                startPhoneVerification();

        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken
                    forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                mVerify.setText(R.string.verify_code);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG, "onVerificationFailed: Error " + e);
            }
        };
    }

    private void verifyPhoneWithCode() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(
                mVerificationId, mCode.getText().toString()
        );
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userIsLoggedIn();
            }
        });
    }

    private void userIsLoggedIn() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(this, HomePage.class));
            finish();
        }
    }

    private void startPhoneVerification() {
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(mPhone.getText().toString().trim(), 60, TimeUnit.SECONDS,
                        this, mCallbacks);
    }

    private void initViews() {
        mPhone = findViewById(R.id.phoneNumber);
        mCode = findViewById(R.id.verificationCode);
        mVerify = findViewById(R.id.btnVerify);
    }
}
