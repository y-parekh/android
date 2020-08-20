package com.yogenp.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class Register extends AppCompatActivity {

    private EditText edtuser, edtemail, edtpass, edtcode;
    private Button code, reg;
    private String username, email, password, otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtuser = findViewById(R.id.edtRegUser);
        edtemail = findViewById(R.id.edtRegEmail);
        edtpass = findViewById(R.id.edtRegPass);
        edtcode = findViewById(R.id.edtRegCode);

        code = findViewById(R.id.btnGetCode);
        reg = findViewById(R.id.btnRegUser);

        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtuser.getText().toString();
                email = edtemail.getText().toString();
                password = edtpass.getText().toString();

                Amplify.Auth.signUp(
                        username,
                        password,
                        AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), email).build(),
                        result -> Log.i("AuthQuickStart", "Sign up: " + result.toString()),
                        error -> Log.e("AuthQuickStart", "Sign up failed", error)
                );
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = edtcode.getText().toString();

                Amplify.Auth.confirmSignUp(
                        username,
                        otp,
                        result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp" : "Confirm sign up not complete"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );

                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
