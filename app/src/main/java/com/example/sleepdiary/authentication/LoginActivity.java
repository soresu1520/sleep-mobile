package com.example.sleepdiary.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sleepdiary.MainActivity;
import com.example.sleepdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private EditText editEmail, editPassword;

    private final String TAG = "sleepdiary0613";
    private final String loginMessage = "Niepoprawny login lub hasło";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if(currentUser != null){
            goToMenu();
        }

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
    }

    //TODO add error handling when logging in
    public void onLogInClick(View view){
        //trim - removes whitespace
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        //TODO add user to firebase
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //sign in successful
                            Log.d(TAG, "signInWithEmail success");
                            goToMenu();
                        } else {
                            //failed sign in
                            Log.w(TAG, "signInWithEmail failure", task.getException());
                            Toast.makeText(getApplicationContext(), loginMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onRegisterClick(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void onRemindPasswordClick(View view){
        Intent intent = new Intent(this, RemindPasswordActivity.class);
        startActivity(intent);
    }

    private void goToMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}