package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    // Layout variables
    EditText passwordCreate, passwordConfirm, passwordInput;
    // Internal variable
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("prefs", 0);
        // Fade in view to act as a loading screen
        ObjectAnimator animator = ObjectAnimator.ofFloat(findViewById(R.id.loadingScreen), "alpha", 0f, 1f).setDuration(TimeUnit.SECONDS.toMillis(2));
        animator.start();
        // Display appropriate popUp view (either signIn or signUp)
        new Handler().postDelayed(() -> {
            // Determine if password exists or not
            if (preferences.getString("password", "").equals("")) { // Password does not exist
                View signUp = getLayoutInflater().inflate(R.layout.popup_signup, null);
                passwordCreate = (EditText) signUp.findViewById(R.id.create);
                passwordConfirm = (EditText) signUp.findViewById(R.id.confirm);
                // Display a 'sign up' alert/popup
                new AlertDialog.Builder(this).setView(signUp).create().show();
            } else { // Password does exist
                View signIn = getLayoutInflater().inflate(R.layout.popup_signin, null);
                passwordInput = (EditText) signIn.findViewById(R.id.password);
                // Display a 'sign in' alert/popup
                new AlertDialog.Builder(this, R.style.Theme_Dialog).setView(signIn).create().show();
            }
        }, 2500);
    }

    // Function to handle signing up
    public void signUp(View view) {
        String password = passwordCreate.getText().toString();
        String confirm = passwordConfirm.getText().toString();
        // Determine if edit texts are empty
        if (password.equals("") || confirm.equals("")) {
            Toast.makeText(MainActivity.this, "Please enter a desired password twice.", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirm)) { // Determine if password input matches confirm password input
            Toast.makeText(MainActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            passwordCreate.setText("");
            passwordConfirm.setText("");
        } else { // All good
            // Update password and save it to preferences
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("password", password);
            editor.apply();
            Toast.makeText(MainActivity.this, "Password successfully set.", Toast.LENGTH_SHORT).show();
            // Move to diary activity
            startActivity(new Intent(getApplicationContext(), DiaryActivity.class));
            finish();
        }
    }

    // Function to handle signing in
    public void signIn(View view) {
        String password = passwordInput.getText().toString();
        // Determine if password field is empty
        if (password.equals("")) {
            Toast.makeText(MainActivity.this, "Please enter the password.", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(preferences.getString("password", ""))) { // Determine if password is incorrect
            Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise move to diary activity
            startActivity(new Intent(getApplicationContext(), DiaryActivity.class));
            finish();
        }
    }
}