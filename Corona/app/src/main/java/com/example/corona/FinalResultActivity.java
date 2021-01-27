package com.example.corona;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    protected TextView first;
    protected TextView second;
    protected TextView third;
    protected TextView fourth;
    protected Button Exit;
    DatabaseReference databaseReference;
    scoreboard score;
    void setScoreboard(scoreboard score) {
        this.score = score;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result);

        first = (TextView) findViewById(R.id.player1_total);
        second = (TextView) findViewById(R.id.player2_total);
        third = (TextView) findViewById(R.id.player3_total);
        fourth = (TextView) findViewById(R.id.player4_total);
        Exit = (Button) findViewById(R.id.exitButton);
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinalResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        score = new scoreboard();
        Intent intent = getIntent();
        final String key = (String) intent.getStringExtra("Key");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("bl");
        databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scoreboard board = dataSnapshot.getValue(scoreboard.class);
                setScoreboard(board);
                int [] totalArr = new int[4];
                totalArr[0] = board.getSum1();
                totalArr[1] = board.getSum2();
                totalArr[2] = board.getSum3();
                totalArr[3] = board.getSum4();
                Arrays.sort(totalArr);
                //First
                if(totalArr[3] == board.getSum1()){
                    first.setText("Winner " +score.getPlayer1_name() + ": " + board.getSum1());
                }else if(totalArr[3] == board.getSum2()){
                    first.setText("Winner " +score.getPlayer2_name() + ": " + board.getSum2());
                }else if(totalArr[3] == board.getSum3()){
                    first.setText("Winner " +score.getPlayer3_name() + ": " + board.getSum3());
                }else if(totalArr[3] == board.getSum4()){
                    first.setText("Winner " +score.getPlayer4_name() + ": " + board.getSum4());
                }
                //Second
                if(totalArr[2] == board.getSum1()){
                    second.setText("2nd " +score.getPlayer1_name() + ": " + board.getSum1());
                }else if(totalArr[2] == board.getSum2()){
                    second.setText("2nd " +score.getPlayer2_name() + ": "  + board.getSum2());
                }else if(totalArr[2] == board.getSum3()){
                    second.setText("2nd " +score.getPlayer3_name() + ": "  + board.getSum3());
                }else if(totalArr[2] == board.getSum4()){
                    second.setText("2nd " +score.getPlayer4_name() + ": "  + board.getSum4());
                }
                //Third
                if(totalArr[1] == board.getSum1()){
                    third.setText("3rd " +score.getPlayer1_name() + ": " + board.getSum1());
                }else if(totalArr[1] == board.getSum2()){
                    third.setText("3rd " +score.getPlayer2_name() + ": " + board.getSum2());
                }else if(totalArr[1] == board.getSum3()){
                    third.setText("3rd " +score.getPlayer3_name() + ": " + board.getSum3());
                }else if(totalArr[1] == board.getSum4()){
                    third.setText("3rd " +score.getPlayer4_name() + ": " + board.getSum4());
                }
                //Fourth
                if(totalArr[0] == board.getSum1()){
                    fourth.setText("4th " +score.getPlayer1_name() + ": "  + board.getSum1());
                }else if(totalArr[0] == board.getSum2()){
                    fourth.setText("4th " +score.getPlayer2_name() + ": "  + board.getSum2());
                }else if(totalArr[0] == board.getSum3()){
                    fourth.setText("4th " +score.getPlayer3_name() + ": "  + board.getSum3());
                }else if(totalArr[0] == board.getSum4()){
                    fourth.setText("4th " +score.getPlayer4_name() + ": "  + board.getSum4());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FinalResultActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}