package com.infusiblecoders.simplenotesapp;

import android.content.Intent;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.infusiblecoders.simplenotesapp.NoteModel;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private RecyclerView mNotesList;
    private GridLayoutManager gridLayoutManager;

    private DatabaseReference fNotesDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotesList = (RecyclerView) findViewById(R.id.notes_list);

        gridLayoutManager = new GridLayoutManager(this, 2 );

        mNotesList.setHasFixedSize(true);
        mNotesList.setLayoutManager(gridLayoutManager);
        //gridLayoutManager.setReverseLayout(true);
        //gridLayoutManager.setStackFromEnd(true);
        mNotesList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        }

        updateUI();

        loadData();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void loadData() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notes").child(FirebaseAuth.getInstance().getUid());
        FirebaseRecyclerOptions<NoteModel> options = new FirebaseRecyclerOptions.Builder<NoteModel>().setQuery(ref,NoteModel.class).build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NoteModel , NoteViewHolder>(options) {
            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_note_layout, parent , false);
                return  new NoteViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final NoteViewHolder noteViewHolder, final int i, @NonNull final NoteModel noteModel) {
                noteViewHolder.setNoteTitle(noteModel.getTitle());
                noteViewHolder.setNoteTime(noteModel.getTimestamp());
                noteViewHolder.setContent(noteModel.getContent());

                final String getKey = getRef(i).getKey();



                noteViewHolder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                        intent.putExtra("title" , noteModel.getTitle());
                        intent.putExtra("topic", noteModel.getContent());
                        intent.putExtra("key",getKey);
                        startActivity(intent);

                        Toast.makeText(MainActivity.this, "Clicked position "+i, Toast.LENGTH_SHORT).show();
                    }
                });




            }
        };
//        Query query = fNotesDatabase.orderByValue();
//        FirebaseRecyclerAdapter<NoteModel, NoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NoteModel, NoteViewHolder>(
//                NoteModel.class,
//                R.layout.single_note_layout,
//                NoteViewHolder.class,
//                query
//
//        ) {
//
//
//            @NonNull
//            @Override
//            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                return null;
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull NoteModel noteModel) {
//
//            }
//
//            @Override
//            protected void populateViewHolder(final NoteViewHolder viewHolder, NoteModel model, int position) {
//                final String noteId = getRef(position).getKey();
//
//                fNotesDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("timestamp")) {
//                            String title = dataSnapshot.child("title").getValue().toString();
//                            String timestamp = dataSnapshot.child("timestamp").getValue().toString();
//
//                            viewHolder.setNoteTitle(title);
//                            //viewHolder.setNoteTime(timestamp);
//
//                            GetTimeAgo getTimeAgo = new GetTimeAgo();
//                            viewHolder.setNoteTime(getTimeAgo.getTimeAgo(Long.parseLong(timestamp), getApplicationContext()));
//
//                            viewHolder.noteCard.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
//                                    intent.putExtra("noteId", noteId);
//                                    startActivity(intent);
//                                }
//                            });
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//        };
        mNotesList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    private void updateUI(){

        if (fAuth.getCurrentUser() != null){
            Log.i("MainActivity", "fAuth != null");
        } else {
            Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(startIntent);
            finish();
            Log.i("MainActivity", "fAuth == null");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.main_new_note_btn:
                Intent newIntent = new Intent(MainActivity.this, NewNoteActivity.class);
                startActivity(newIntent);
                break;
        }

        return true;
    }



    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}