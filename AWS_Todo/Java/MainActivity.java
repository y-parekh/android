package com.yogenp.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Priority;
import com.amplifyframework.datastore.generated.model.Todo;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Amplify.DataStore.query(
                Todo.class,
                todos -> {
                    while (todos.hasNext()) {
                        Todo todo = todos.next();
                        Log.i("TodoItems", "==== Todo ====");
                        Log.i("TodoItems", "Name: " + todo.getName());

                        if (todo.getPriority() != null) {
                            Log.i("TodoItems", "Priority: " + todo.getPriority().toString());
                        }

                        if (todo.getDescription() != null) {
                            Log.i("TodoItems", "Description: " + todo.getDescription());
                        }
                    }
                },
                failure -> Log.e("TodoItems", "Could not query DataStore", failure)
        );

    }
}
