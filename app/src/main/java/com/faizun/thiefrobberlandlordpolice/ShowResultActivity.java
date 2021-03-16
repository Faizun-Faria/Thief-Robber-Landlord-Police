package com.faizun.thiefrobberlandlordpolice;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowResultActivity extends AppCompatActivity {

    protected TextView first_name;
    protected TextView second_name;
    protected TextView third_name;
    protected TextView fourth_name;
    protected TextView first_score;
    protected TextView second_score;
    protected TextView third_score;
    protected TextView fourth_score;

    protected TextView total_first_name;
    protected TextView total_second_name;
    protected TextView total_third_name;
    protected TextView total_fourth_name;
    protected TextView total_first_score;
    protected TextView total_second_score;
    protected TextView total_third_score;
    protected TextView total_fourth_score;

    protected TextView round_no_header;
    protected TextView round_no;
    DatabaseReference databaseReference;
    scoreboard score;
    String key;

    Integer [] current_score = new Integer[4];
    String [] current_player_name = new String[4];
    Integer [] total_score = new Integer[4];
    String [] total_player_name = new String[4];


    void setScoreboard(scoreboard score) {
        this.score = score;
    }

    protected void initialize() {
        current_score[0] = score.getPlayer1();
        current_score[1] = score.getPlayer2();
        current_score[2] = score.getPlayer3();
        current_score[3] = score.getPlayer4();

        current_player_name[0] = score.getPlayer1_name();
        current_player_name[1] = score.getPlayer2_name();
        current_player_name[2] = score.getPlayer3_name();
        current_player_name[3] = score.getPlayer4_name();

        total_score[0] = score.getSum1();
        total_score[1] = score.getSum2();
        total_score[2] = score.getSum3();
        total_score[3] = score.getSum4();

        total_player_name[0] = score.getPlayer1_name();
        total_player_name[1] = score.getPlayer2_name();
        total_player_name[2] = score.getPlayer3_name();
        total_player_name[3] = score.getPlayer4_name();
    }

    protected void sortPlayers(Integer[] score,String[] name, boolean total) {
        for(int i = 0; i < score.length - 1; i++){
            int mx = score[i];
            int indx = i;
            for(int j = i+1; j < score.length; j++){
                if(score[j] > mx){
                    indx = j;
                }
            }
            if(indx != i){
                int temp = score[i];
                score[i] = score[indx];
                score[indx] = temp;

                String tmp = name[i];
                name[i] = name[indx];
                name[indx] = tmp;

            }
        }
        if(!total){
            first_score.setText(score[0].toString());
            second_score.setText(score[1].toString());
            third_score.setText(score[2].toString());
            fourth_score.setText(score[3].toString());

            first_name.setText(name[0]);
            second_name.setText(name[1]);
            third_name.setText(name[2]);
            fourth_name.setText(name[3]);
        }else{

            total_first_score.setText(score[0].toString());
            total_second_score.setText(score[1].toString());
            total_third_score.setText(score[2].toString());
            total_fourth_score.setText(score[3].toString());

            total_first_name.setText(name[0]);
            total_second_name.setText(name[1]);
            total_third_name.setText(name[2]);
            total_fourth_name.setText(name[3]);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        first_name = findViewById(R.id.first_name);
        second_name = findViewById(R.id.second_name);
        third_name = findViewById(R.id.third_name);
        fourth_name = findViewById(R.id.fourth_name);
        first_score = findViewById(R.id.first_score);
        second_score = findViewById(R.id.second_score);
        third_score = findViewById(R.id.third_score);
        fourth_score = findViewById(R.id.fourth_score);

        total_first_name = findViewById(R.id.total_first_name);
        total_second_name = findViewById(R.id.total_second_name);
        total_third_name = findViewById(R.id.total_third_name);
        total_fourth_name = findViewById(R.id.total_fourth_name);
        total_first_score = findViewById(R.id.total_first_score);
        total_second_score = findViewById(R.id.total_second_score);
        total_third_score = findViewById(R.id.total_third_score);
        total_fourth_score = findViewById(R.id.total_fourth_score);

        round_no = (TextView) findViewById(R.id.round_no);
        round_no_header = (TextView) findViewById(R.id.round_no_header);
        score = new scoreboard();
        Intent intent = getIntent();
        key = (String) intent.getStringExtra("Key");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("play");
        databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                score = dataSnapshot.getValue(scoreboard.class);
                setScoreboard(score);
                round_no_header.setText("ROUND " + score.getGame_no());
                round_no.setText("ROUND " + score.getGame_no() + " Result");


                initialize();
                boolean total = false;
                sortPlayers(current_score, current_player_name, total);
                total = true;
                sortPlayers(total_score, total_player_name, total);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ShowResultActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(ShowResultActivity.this, GameActivity.class);
                intent.putExtra("Key", key);
                startActivity(intent);
            }
        }, 3000);   //4 seconds
    }
}