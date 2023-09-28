package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class noteActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    RecyclerView mrecyclerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<firebasemodel,NoteViewHolder> noteAdapter;



    FloatingActionButton mcreatenotesfab, msignoutfab;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        firebaseAuth = FirebaseAuth.getInstance();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        mcreatenotesfab = findViewById(R.id.createnotefab);
        msignoutfab = findViewById(R.id.signoutfab);

//        getSupportActionBar().setTitle("All notes");
        mcreatenotesfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(noteActivity.this, createnote.class));

            }
        });


        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<firebasemodel> allusernotes = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query, firebasemodel.class).build();
        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusernotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull firebasemodel firebasemodel) {


                ImageView popupbutton = noteViewHolder.itemView.findViewById(R.id.menupopupbutton);
                int colorcode = getRandomColor();
                noteViewHolder.mnote.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colorcode,null));

                noteViewHolder.notetitle.setText(firebasemodel.getTitle());
            noteViewHolder.notecontent.setText(firebasemodel.getContent());

            String docID = noteAdapter.getSnapshots().getSnapshot(i).getId();

            noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //we have to open note detail activity

                    Intent intent = new Intent(view.getContext(),notedetails.class);
                    intent.putExtra("title",firebasemodel.getTitle());
                    intent.putExtra("content",firebasemodel.getContent());
                    intent.putExtra("noteId",docID);
                    view.getContext().startActivity(intent);
//                    Toast.makeText(getApplicationContext(),"Kaam kr raha hain",Toast.LENGTH_SHORT).show();
                }
            });


            popupbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                    popupMenu.setGravity(Gravity.END);
                    popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                            Intent intent = new Intent(v.getContext(),editnoteactivity.class);

                            intent.putExtra("title",firebasemodel.getTitle());
                            intent.putExtra("content",firebasemodel.getContent());
                            intent.putExtra("noteId",docID);
                            v.getContext().startActivity(intent);
                            return false;
                        }
                    });


                    popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
//                            Toast.makeText(v.getContext(),"This is delete",Toast.LENGTH_SHORT).show();

                            DocumentReference documentReference= firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docID);
                            documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(v.getContext(),"Note Deleted",Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(v.getContext(),"Failed to Delete",Toast.LENGTH_SHORT).show();

                                }
                            });

                            return false;
                        }
                    });

                    popupMenu.show();
                }
            });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };


        mrecyclerview = findViewById(R.id.recyclerview);
        mrecyclerview.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);


        mrecyclerview.setAdapter(noteAdapter);



        msignoutfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(noteActivity.this, MainActivity.class));
            }
        });
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView notetitle;
        private TextView notecontent;
        LinearLayout mnote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notetitle = itemView.findViewById(R.id.notetitle);
            notecontent = itemView.findViewById(R.id.notecontent);
            mnote = itemView.findViewById(R.id.note);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter!=null){
            noteAdapter.stopListening();
        }
    }


    private int getRandomColor(){
        List<Integer> colorcode = new ArrayList<>();
        colorcode.add(R.color.Blue);
        colorcode.add(R.color.Red);
        colorcode.add(R.color.Green);
        colorcode.add(R.color.Yellow);

        Random random = new Random();
        int number = random.nextInt(colorcode.size());
        return colorcode.get(number);

    }
}