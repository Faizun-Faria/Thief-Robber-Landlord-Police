package com.faizun.thiefrobberlandlordpolice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    protected ImageView P1;
    protected ImageView P2;
    protected ImageView P3;
    protected ImageView P4;
    protected Button  next;
    protected LinearLayout roundInfoLayout;
    protected TextView roundNo;
    protected TextView textView;
    protected Integer [] new_score = {-1,-1,-1,-1};
    protected ImageView landlord, police, robber, thief;
    final Integer mark[] = {100,90,80,70};
    int round_no;
    DatabaseReference databaseReference;
    scoreboard score;
    String msg = "";

    protected void setScoreboard(final scoreboard board) {
        score = board;
    }

    protected void initialize(){
        P1 = (ImageView) findViewById(R.id.player1);
        P2 = (ImageView) findViewById(R.id.player2);
        P3 = (ImageView) findViewById(R.id.player3);
        P4 = (ImageView) findViewById(R.id.player4);
        roundInfoLayout = findViewById(R.id.round_info_layout);
        roundNo =  (TextView) findViewById(R.id.round_no);
        textView = (TextView) findViewById(R.id.textView);
        next = (Button) findViewById(R.id.next);
        P1.setClickable(false);
        P2.setClickable(false);
        P3.setClickable(false);
        P4.setClickable(false);
        P1.setEnabled(false);
        P2.setEnabled(false);
        P3.setEnabled(false);
        P4.setEnabled(false);
    }

    protected void setRole(Integer[] mark){
        if(mark[0] == 100) landlord = P1;
        else if(mark[0] == 90) police = P1;
        else if(mark[0] == 80)  robber = P1;
        else if(mark[0] == 70)  thief = P1;

        if(mark[1] == 100) landlord = P2;
        else if(mark[1] == 90) police = P2;
        else if(mark[1] == 80)  robber = P2;
        else if(mark[1] == 70)  thief = P2;

        if(mark[2] == 100) landlord = P3;
        else if(mark[2] == 90) police = P3;
        else if(mark[2] == 80)  robber = P3;
        else if(mark[2] == 70)  thief = P3;

        if(mark[3] == 100) landlord = P4;
        else if(mark[3] == 90) police = P4;
        else if(mark[3] == 80)  robber = P4;
        else if(mark[3] == 70)  thief = P4;

        landlord.setImageResource(R.drawable.landlord);
        police.setImageResource(R.drawable.policeman);
        // If the user is police
        if (police == P1){
            robber.setImageResource(R.drawable.choose);
            thief.setImageResource(R.drawable.choose);
        }else{
            robber.setImageResource(R.drawable.robber);
            thief.setImageResource(R.drawable.thief);
        }
    }

    protected void setMessage(){

        if(P1 == landlord){
            textView.setText(getString(R.string.landlord_msg));
        }else if(P1 == police){
            if(round_no%2==0)  textView.setText(getString(R.string.police_thief_msg));
            else textView.setText(getString(R.string.police_robber_msg));
        }else if(P1 == robber){
            if(round_no%2==0) textView.setText(getString(R.string.robber_police_safe_msg));
            else textView.setText(getString(R.string.robber_police_danger_msg));
        }else if(P1 == thief){
            if(round_no%2==0) textView.setText(getString(R.string.thief_police_danger_msg));
            else textView.setText(getString(R.string.thief_police_safe_msg));
        }
    }

    protected void isPolice(){
        next.setClickable(false);
        next.setEnabled(false);
        robber.setClickable(true);
        robber.setEnabled(true);
        thief.setClickable(true);
        thief.setEnabled(true);

        thief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robber.setEnabled(false);
                robber.setEnabled(false);
                thief.setClickable(false);
                thief.setClickable(false);
                next.setClickable(true);
                next.setEnabled(true);
                if(round_no%2 == 0){
                    if( thief == P2) new_score[1] = 0;
                    else if( thief == P3) new_score[2] = 0;
                    else if( thief == P4) new_score[3] = 0;
                    Toast.makeText(GameActivity.this, getString(R.string.success_caught_thief_msg), Toast.LENGTH_SHORT).show();
                }else{
                    new_score[0] = 0;
                    Toast.makeText(GameActivity.this, getString(R.string.fail_caught_robber_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });

        robber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robber.setEnabled(false);
                robber.setEnabled(false);
                thief.setClickable(false);
                thief.setClickable(false);
                next.setClickable(true);
                next.setEnabled(true);
                if(round_no%2 != 0){
                    if( robber == P2) new_score[1] = 0;
                    else if( robber == P3) new_score[2] = 0;
                    else if( robber == P4) new_score[3] = 0;
                    Toast.makeText(GameActivity.this, getString(R.string.success_caugth_robber_msg), Toast.LENGTH_SHORT).show();
                }else{
                    new_score[0] = 0;
                    Toast.makeText(GameActivity.this, getString(R.string.fail_caugth_thief_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void isLandlordRobberThief(){
        if( round_no%2 == 0){
            if (P2 == police || P2 == thief) new_score[1] = 0;
            else if(P1 == thief) new_score[0] = 0;
            else if (P3 == police || P3 == thief) new_score[2] = 0;
            else if (P4 == police || P4 == thief) new_score[3] = 0;
        }else{
            if (P3 == police || P3 == robber) new_score[2] = 0;
            else if (P4 == police || P4 == robber) new_score[3] = 0;
            else if(P1 == robber) new_score[0] = 0;
            else if (P2 == police || P2 == robber) new_score[1] = 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initialize();
        score = new scoreboard();
        Intent intent = getIntent();
        final String key = (String) intent.getStringExtra("Key");

        try {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("play");
            databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    score = dataSnapshot.getValue(scoreboard.class);
                    setScoreboard(score);

                    round_no = score.getGame_no() + 1;
                    if (msg == "") {
                        msg = "Round :"+round_no;
                        roundNo.setText(msg);
                        List<Integer> intList = Arrays.asList(mark);
                        Collections.shuffle(intList);
                        intList.toArray(mark);
                        setRole(mark);
                        setMessage();
                        new_score[0] = mark[0];
                        new_score[1] = mark[1];
                        new_score[2] = mark[2];
                        new_score[3] = mark[3];
                        roundInfoLayout.setVisibility(View.VISIBLE);
                        if(P1 == police) isPolice();
                        else isLandlordRobberThief();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(GameActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent current_scoreboard = new Intent(GameActivity.this, ShowResultActivity.class);
                    final Intent final_scoreboard = new Intent(GameActivity.this, FinalResultActivity.class);
                    score.setGame_no(score.getGame_no()+1);

                    score.setPlayer1(new_score[0]);
                    score.setPlayer2(new_score[1]);
                    score.setPlayer3(new_score[2]);
                    score.setPlayer4(new_score[3]);

                    score.setSum1(new_score[0] + score.getSum1());
                    score.setSum2(new_score[1] + score.getSum2());
                    score.setSum3(new_score[2] + score.getSum3());
                    score.setSum4(new_score[3] + score.getSum4());

                    try {
                        databaseReference.child(key).setValue(score).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if(score.getGame_no()>=10) {
                                        final_scoreboard.putExtra("Key", key);
                                        startActivity(final_scoreboard);
                                    }else{
                                        current_scoreboard.putExtra("Key", key);
                                        startActivity(current_scoreboard);
                                    }
                                }
                                // Error
                                else {
                                    Toast.makeText(GameActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(GameActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}