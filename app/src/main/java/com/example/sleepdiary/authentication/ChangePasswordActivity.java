package com.example.sleepdiary.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sleepdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseUser user;
    private EditText editPassword, editPassword2;

    private final String TAG = "sleepdiary0613";
    private final String changeErrorMessage = "Wystąpił błąd";
    private final String changeDiffMessage = "Hasła nie są takie same";
    private final String changeMessage = "Hasło zostało zmienione";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        editPassword = findViewById(R.id.editPassword);
        editPassword2 = findViewById(R.id.editPassword2);
    }


    //TODO add handling errors when changing password (too short password)
    public void onChangePasswordClick(View view) {
        String password = editPassword.getText().toString().trim();
        String password2 = editPassword2.getText().toString().trim();

        if (password.equals(password2)) {
            user.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User password updated");
                                Toast.makeText(getApplicationContext(), changeMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "Error while updating user password");
                                Toast.makeText(getApplicationContext(), changeErrorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Log.d(TAG, "Different passwords");
            Toast.makeText(getApplicationContext(), changeDiffMessage, Toast.LENGTH_SHORT).show();
        }
    }
}