package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class forgotpassword extends AppCompatActivity {

    private EditText mforgotpassword;  // variable for edittext
    private Button mpasswordrecoverbutton; // variable for button
    private TextView mgobacktologin; // variable for text view




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

//        getSupportActionBar().hide(); // to hide action bar
        mforgotpassword = findViewById(R.id.forgotpassword);
        mpasswordrecoverbutton = findViewById(R.id.passwordrecoverbutton);
        mgobacktologin = findViewById(R.id.gobacktologin);

        mgobacktologin.setOnClickListener(new View.OnClickListener() { // click listner for going back to login
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(forgotpassword.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        mpasswordrecoverbutton.setOnClickListener(new View.OnClickListener() { // for checking password recovery
            @Override
            public void onClick(View view) {
                String mail = mforgotpassword.getText().toString().trim(); // for getting text from the entered field
                if(mail.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter your mail first",Toast.LENGTH_SHORT).show();
                }
                else{
                    // we have to send mail
                }
            }
        });

    }
}