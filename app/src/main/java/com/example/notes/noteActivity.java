package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class noteActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    FloatingActionButton mcreatenotesfab;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        firebaseAuth = FirebaseAuth.getInstance();


        mcreatenotesfab=findViewById(R.id.createnotefab);

//        getSupportActionBar().setTitle("All notes");
        mcreatenotesfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(noteActivity.this,createnote.class));

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
// kadya begins
        int z = item.getItemId();

        if(z==R.id.logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(noteActivity.this, createnote.class));
        }

//        if(item.getItemId()=="true")
//        {
//            case R.id.logout:
//                firebaseAuth.signOut();
//                finish();
//                startActivity(new Intent(noteActivity.this, createnote.class));
//        }

        return super.onOptionsItemSelected(item);
    }
}