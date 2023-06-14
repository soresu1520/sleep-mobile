package com.example.sleepdiary.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;

public class RemindPasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText editEmail;

    private final String TAG = "sleepdiary0613";
    private final String remindMessage = "Wysłano mail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_password);
        auth = FirebaseAuth.getInstance();

        editEmail = findViewById(R.id.editEmail);
    }

    //TODO add error handling when sending password reset email
    public void onRemindClick(View view) {
        String email = editEmail.getText().toString().trim();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "sendPasswordResetEmail success");
                            Toast.makeText(getApplicationContext(), remindMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onLoginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}