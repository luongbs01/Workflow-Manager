package com.app.workflowmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.workflowmanager.entity.AccessToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    private Button buttonSignIn;
    private boolean goToMain = false;

    private String clientId = "45952397fecad6e25e33";
    private String clientSecret = "44f8baa05cb20db188ed2b93e47bc669876d3d53";
    private String redirectUri = "futurestudio://callback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        goToMain();
        buttonSignIn = findViewById(R.id.bt_sign_in);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/login/oauth/authorize" + "?client_id=" + clientId + "&redirect_uri=" + redirectUri));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!goToMain) {
            goToMain();
        }
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            String code = uri.getQueryParameter("code");

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://github.com")
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();

            GithubClient client = retrofit.create(GithubClient.class);
            Call<AccessToken> accessTokenCall = client.getAccessToken(clientId, clientSecret, code);
            accessTokenCall.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.preference_file_key),
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("access_token", response.body().getAccessToken());
                    editor.apply();

                    Toast.makeText(SignInActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                    goToMain();
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.d("luong", t.toString());
                }
            });
        }
    }

    private void goToMain() {
        SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        String accessToken = sharedPref.getString("access_token", "");
        if (accessToken != null && !accessToken.equals("")) {
            goToMain = true;
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}