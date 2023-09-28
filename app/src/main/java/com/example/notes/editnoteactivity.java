package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editnoteactivity extends AppCompatActivity {

    Intent data;
    EditText medittitleofnote,meditcontentofnote;

    FloatingActionButton msaveeditnote;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnoteactivity);
        medittitleofnote = (EditText) findViewById(R.id.edittitleofnote);
        meditcontentofnote = (EditText) findViewById(R.id.editcontentofnote);
        msaveeditnote = findViewById(R.id.saveeditnote);
        data = getIntent();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        msaveeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newcontent = meditcontentofnote.getText().toString();
                String newtitle = medittitleofnote.getText().toString();
                if(newtitle.isEmpty() || newcontent.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Something is empty!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String, Object> note=new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Note updated",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(editnoteactivity.this,noteActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to update",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(editnoteactivity.this,noteActivity.class));
                            finish();
                        }
                    });
                }
            }
        });

        String notetitle = data.getStringExtra("title");
        String notecontent = data.getStringExtra("content");


        meditcontentofnote.setText(notecontent); // error is here
        medittitleofnote.setText(notetitle);
//
    }
}