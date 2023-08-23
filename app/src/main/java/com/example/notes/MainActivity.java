package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText mloginemail, mloginppassword;
    RelativeLayout mlogin, mgotosignup;
    TextView mgotoforgotpassword;
    private FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().hide();
        mloginemail = findViewById(R.id.loginemail);
        mloginppassword = findViewById(R.id.loginpassword);
        mlogin = findViewById(R.id.login);
        mgotoforgotpassword = findViewById(R.id.gotoforgotpassword);
        mgotosignup = findViewById(R.id.signup);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            finish();
            startActivity(new Intent(MainActivity.this, noteActivity.class));
        }

        mgotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, signup.class);
                startActivity(intent);
            }
        });

        mgotoforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, forgotpassword.class));
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mloginemail.getText().toString().trim();
                String password = mloginppassword.getText().toString().trim();
                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"All Fields are required", Toast.LENGTH_SHORT).show();

                }
                else{
                    // login the user
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                checkmailverification();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Account doesnt exists!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }



    private void checkmailverification(){
        FirebaseUser firebaseuser = firebaseAuth.getCurrentUser();
        if(firebaseuser.isEmailVerified()==true){
            Toast.makeText(getApplicationContext(),"Logged In", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, noteActivity.class));
        }
        else{
            Toast.makeText(getApplicationContext(),"Please verify your email first!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

    }
}