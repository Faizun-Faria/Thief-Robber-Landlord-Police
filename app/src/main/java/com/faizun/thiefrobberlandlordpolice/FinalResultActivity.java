package com.faizun.thiefrobberlandlordpolice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class FinalResultActivity extends AppCompatActivity {

    protected TextView first_name;
    protected TextView second_name;
    protected TextView third_name;
    protected TextView fourth_name;
    protected TextView first_score;
    protected TextView second_score;
    protected TextView third_score;
    protected TextView fourth_score;
    protected Button next_game;

    protected TextView winner_name;

    DatabaseReference databaseReference;
    scoreboard score;

    void setScoreboard(scoreboard score) {
        this.score = score;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result);

        first_name = (TextView) findViewById(R.id.first_name);
        second_name = (TextView) findViewById(R.id.second_name);
        third_name = (TextView) findViewById(R.id.third_name);
        fourth_name = (TextView) findViewById(R.id.fourth_name);
        first_score = (TextView) findViewById(R.id.first_score);
        second_score = (TextView) findViewById(R.id.second_score);
        third_score = (TextView) findViewById(R.id.third_score);
        fourth_score = (TextView) findViewById(R.id.fourth_score);

        winner_name = findViewById(R.id.winner_name);

        next_game = (Button) findViewById(R.id.next_game);
        next_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinalResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        score = new scoreboard();

        Intent intent = getIntent();
        final String key = (String) intent.getStringExtra("Key");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("play");
        databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scoreboard board = dataSnapshot.getValue(scoreboard.class);
                setScoreboard(board);
                int [] sortedArr = new int[4];
                sortedArr[0] = board.getSum1();
                sortedArr[1] = board.getSum2();
                sortedArr[2] = board.getSum3();
                sortedArr[3] = board.getSum4();
                Arrays.sort(sortedArr);
                //First
                Integer [] total_score = new Integer[4];
                total_score[0] = board.getSum1();
                total_score[1] = board.getSum2();
                total_score[2] = board.getSum3();
                total_score[3] = board.getSum4();

                try {
                    String first = "";
                    if(sortedArr[3] == total_score[0]){
                        total_score[0] = 0;
                        first = score.getPlayer1_name();
                        first_score.setText((board.getSum1())+"");
                    }else if(sortedArr[3] == total_score[1]){
                        total_score[1] = 0;
                        first = score.getPlayer2_name();
                        first_score.setText(board.getSum2()+"");
                    }else if(sortedArr[3] == total_score[2]){
                        total_score[2] = 0;
                        first = score.getPlayer3_name();
                        first_score.setText(board.getSum3()+"");
                    }else if(sortedArr[3] == total_score[3]){
                        total_score[3] = 0;
                        first = score.getPlayer4_name();
                        first_score.setText(board.getSum4()+"");
                    }
                    first_name.setText(first);
                    winner_name.setText(first);

                    //Second
                    if(sortedArr[2] == total_score[0]){
                        total_score[0] = 0;
                        second_name.setText(score.getPlayer1_name());
                        second_score.setText(board.getSum1()+"");
                    }else if(sortedArr[2] == total_score[1]){
                        total_score[1] = 0;
                        second_name.setText(score.getPlayer2_name());
                        second_score.setText(board.getSum2()+"");
                    }else if(sortedArr[2] == total_score[2]){
                        total_score[2] = 0;
                        second_name.setText(score.getPlayer3_name());
                        second_score.setText(board.getSum3()+"");
                    }else if(sortedArr[2] == total_score[3]){
                        total_score[3] = 0;
                        second_name.setText(score.getPlayer4_name());
                        second_score.setText(board.getSum4()+"");
                    }
                    //Third
                    if(sortedArr[1] == total_score[0]){
                        total_score[0] = 0;
                        third_name.setText(score.getPlayer1_name());
                        third_score.setText(board.getSum1()+"");
                    }else if(sortedArr[1] == total_score[1]){
                        total_score[1] = 0;
                        third_name.setText(score.getPlayer2_name());
                        third_score.setText(board.getSum2()+"");
                    }else if(sortedArr[1] == total_score[2]){
                        total_score[2] = 0;
                        third_name.setText(score.getPlayer3_name());
                        third_score.setText(board.getSum3()+"");
                    }else if(sortedArr[1] == total_score[3]){
                        total_score[3] = 0;
                        third_name.setText(score.getPlayer4_name());
                        third_score.setText(board.getSum4()+"");
                    }
                    //Fourth
                    if(sortedArr[0] == total_score[0]){
                        total_score[0] = 0;
                        fourth_name.setText(score.getPlayer1_name());
                        fourth_score.setText(board.getSum1()+"");
                    }else if(sortedArr[0] == total_score[1]){
                        total_score[1] = 0;
                        fourth_name.setText(score.getPlayer2_name());
                        fourth_score.setText(board.getSum2()+"");
                    }else if(sortedArr[0] == total_score[2]){
                        total_score[2] = 0;
                        fourth_name.setText(score.getPlayer3_name());
                        fourth_score.setText(board.getSum3()+"");
                    }else if(sortedArr[0] == total_score[3]){
                        total_score[3] = 0;
                        fourth_name.setText(score.getPlayer4_name());
                        fourth_score.setText(board.getSum4()+"");
                    }
                } catch (Exception e) {
                    Toast.makeText(FinalResultActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FinalResultActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}