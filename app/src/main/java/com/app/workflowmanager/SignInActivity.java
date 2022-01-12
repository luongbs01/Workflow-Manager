package com.app.workflowmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class SignInActivity extends AppCompatActivity {

    private Button buttonSignIn;
    private TextInputEditText tokenInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        buttonSignIn = findViewById(R.id.bt_sign_in);
        tokenInputEditText = findViewById(R.id.token_input_edit_text);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.preference_file_key),
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("access_token", tokenInputEditText.getText().toString());
                editor.apply();
                goToMain();
            }
        });
    }

    private void goToMain() {
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        String accessToken = sharedPref.getString("access_token", "");
        if (accessToken != null && !accessToken.equals("")) {
            Log.d("luong", "sign in: " + getSharedPreferences(getResources().getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE).getString("access_token", ""));
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}