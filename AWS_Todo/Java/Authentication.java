package com.yogenp.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;

public class Authentication extends AppCompatActivity {

    private EditText edtuser, edtpass;
    private Button sign, reg;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        edtuser = findViewById(R.id.edtUser);
        edtpass = findViewById(R.id.edtPass);
        sign = findViewById(R.id.btnSignin);
        reg = findViewById(R.id.btnReg);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtuser.getText().toString();
                password = edtpass.getText().toString();

                Amplify.Auth.signIn(
                        username,
                        password,
                        result -> Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );

                Intent intent = new Intent(Authentication.this, MainActivity.class);
                startActivity(intent);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Authentication.this, Register.class);
                startActivity(intent);
            }
        });
    }
}
