package com.example.nishi.water_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PrevData extends AppCompatActivity {
RecyclerView recyclerView;
List<String> dateList;
    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_data);
        recyclerView=findViewById(R.id.recycler);
        dateList=new ArrayList<String>();
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = mFirebaseDatabase.getReference().child("logs");
        databaseReference.keepSynced(true);
    }
    public void dbcall(){
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dateList.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);
    }
    @Override
    protected void onStart() {
        super.onStart();
        dbcall();
        loadview();
    }
    public static class dateViewHolder extends RecyclerView.ViewHolder{
        View mview;
        public dateViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
        }
        public void setDate(String date){
            TextView dateDATE=mview.findViewById(R.id.data_textview);
            dateDATE.setText(date);
        }

    }

    public void loadview(){
        FirebaseRecyclerAdapter mAdapter =
                new FirebaseRecyclerAdapter<dateData, dateViewHolder>(dateData.class,R.layout.list_layout ,dateViewHolder.class, databaseReference) {
                    @Override
                    public void populateViewHolder(dateViewHolder viewHolder, dateData model, final int position) {
                        final String datel=dateList.get(position);
                        viewHolder.setDate(dateList.get(position));
                        viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(PrevData.this,SelectiveData.class);
                                intent.putExtra("selDate",datel);
                                startActivity(intent);
                            }
                        });
                    }
                };
        recyclerView.setAdapter(mAdapter);
    }

}

