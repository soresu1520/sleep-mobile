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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private EditText editEmail, editPassword, editPassword2;

    private final String TAG = "sleepdiary0613";
    private final String registerMessage = "Wystąpił błąd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editPassword2 = findViewById(R.id.editPassword2);
    }

    //TODO add error handling when registering
    public void onRegisterClick(View view) {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String password2 = editPassword2.getText().toString().trim();

        if(!email.equals("") && !password.equals("") && password.equals(password2)) {

            database.collection("patients").whereEqualTo("email", email)
                    .limit(1).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getDocuments().size() > 0){
                                    registerPatient(email, password);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Zapytaj swojego lekarza o możliwość rejestracji", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });

        } else if (!password.equals(password2)) {
            Toast.makeText(getApplicationContext(), "Hasła nie są takie same", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Pola nie mogą być puste", Toast.LENGTH_SHORT).show();
        }
    }

    public void onLoginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void registerPatient(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                                String id = UUID.randomUUID().toString();
//                                User user = new User(id, email);
//                                database.collection("users").add(user);

                            Log.d(TAG, "signUpWithEmail success");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w(TAG, "signUpWithEmail failure", task.getException());
                            Toast.makeText(getApplicationContext(), registerMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}