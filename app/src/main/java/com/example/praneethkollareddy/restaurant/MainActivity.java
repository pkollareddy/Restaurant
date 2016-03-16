package com.example.praneethkollareddy.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);


        Button newAccount = (Button) findViewById(R.id.newAccountButton);
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameInput = (EditText) findViewById(R.id.usernamePrompt);
                String username = usernameInput.getText().toString();
                EditText passwordInput = (EditText) findViewById(R.id.passwordPrompt);
                String password = passwordInput.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter valid credentials.", Toast.LENGTH_SHORT).show();
                } else {
                    createNewAccount(username, password);
                }
            }
        });

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameInput = (EditText) findViewById(R.id.usernamePrompt);
                String username = usernameInput.getText().toString();
                EditText passwordInput = (EditText) findViewById(R.id.passwordPrompt);
                String password = passwordInput.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter valid credentials.", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(username, password);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNewAccount(String username, String password) {
        Firebase ref = new Firebase("https://resplendent-heat-2353.firebaseio.com");
        System.out.println("in create");
        ref.createUser(username, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                Toast.makeText(getApplicationContext(), "Account created. Please login.", Toast.LENGTH_SHORT).show();
                EditText usernameInput = (EditText) findViewById(R.id.usernamePrompt);
                usernameInput.setText("");
                EditText passwordInput = (EditText) findViewById(R.id.passwordPrompt);
                passwordInput.setText("");
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                System.out.println(firebaseError.getDetails());
            }
        });
    }

    private void loginUser(String username, String password) {
        Firebase ref = new Firebase("https://resplendent-heat-2353.firebaseio.com");
        System.out.println("in login");
        ref.authWithPassword(username, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                Intent intent = new Intent(MainActivity.this, AddRestaurant.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                System.out.println(firebaseError.getDetails());
            }
        });
    }
}