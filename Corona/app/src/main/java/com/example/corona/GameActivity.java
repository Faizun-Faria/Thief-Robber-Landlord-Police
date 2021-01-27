package com.example.corona;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
public class GameActivity extends AppCompatActivity {
    protected Button P1;
    protected Button P2;
    protected Button P3;
    protected Button P4;
    protected Button  next;
    protected TextView textView;
    protected Integer[] final_mark;
    DatabaseReference databaseReference;
    scoreboard score;
    protected void setScoreboard(final scoreboard board) {
        score = board;
    }
    protected void delay(final Long time){
        try {
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this, time);
                }
            };
            handler.post(runnable);
        }catch (Exception e){
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        score = new scoreboard();

        Intent intent = getIntent();
        final String key = (String) intent.getStringExtra("Key");

        try {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("bl");
            databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    scoreboard board = dataSnapshot.getValue(scoreboard.class);
                    setScoreboard(board);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(GameActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            P1 = (Button) findViewById(R.id.player1);
            P2 = (Button) findViewById(R.id.player2);
            P3 = (Button) findViewById(R.id.player3);
            P4 = (Button) findViewById(R.id.player4);
            textView = (TextView) findViewById(R.id.textView);
            next = (Button) findViewById(R.id.next);
            final_mark = new Integer[4];
            final_mark[0] = -1;
            final_mark[1] = -1;
            final_mark[2] = -1;
            final_mark[3] = -1;
            P1.setClickable(false);
            P2.setClickable(false);
            P3.setClickable(false);
            P4.setClickable(false);
            P1.setEnabled(false);
            P2.setEnabled(false);
            P3.setEnabled(false);
            P4.setEnabled(false);

            final Integer mark[] = {100,90,80,70};
            List<Integer> intList = Arrays.asList(mark);
            Collections.shuffle(intList);
            intList.toArray(mark);

            // police it is
            if (mark[0] == 90) {
                int indx = score.getGame_no() + 1;
                if(indx%2==0)  textView.setText("You're police. Try to catch the thief");
                else textView.setText("You're police. Try to catch the robber");
                P1.setText(" Police ");
                next.setClickable(false);
                next.setEnabled(false);
                for (int i = 0; i < mark.length; i++) {
                    if (mark[i] == 70 || mark[i] == 80) {
                        if (i == 1) {
                            P2.setClickable(true);
                            P2.setEnabled(true);
                            P2.setText(" Danger ");
                        }
                        if (i == 2) {
                            P3.setClickable(true);
                            P3.setEnabled(true);
                            P3.setText(" Danger ");
                        }
                        if (i == 3) {
                            P4.setClickable(true);
                            P4.setEnabled(true);
                            P4.setText(" Danger ");
                        }

                    }
                }
                P2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        P3.setEnabled(false);
                        P4.setEnabled(false);
                        P3.setClickable(false);
                        P4.setClickable(false);
                        next.setClickable(true);
                        next.setEnabled(true);
                        int indx = score.getGame_no()+1;
                        if (indx % 2 == 0) {  //Even Thief
                            if (mark[1] == 70) {
                                Toast.makeText(GameActivity.this, "You have successfully caught the thief", Toast.LENGTH_SHORT).show();
                                mark[1] = 0;
                                final_mark[0] = mark[0];
                                final_mark[1] = mark[1];
                                final_mark[2] = mark[2];
                                final_mark[3] = mark[3];
                            } else {
                                Toast.makeText(GameActivity.this, "You aren't smart enough to catch the thief", Toast.LENGTH_SHORT).show();
                                mark[0] = 0;
                                final_mark[0] = mark[0];
                                final_mark[1] = mark[1];
                                final_mark[2] = mark[2];
                                final_mark[3] = mark[3];
                            }
                        } else {  //Odd Robber
                            if (mark[1] == 80) {
                                Toast.makeText(GameActivity.this, "You have successfully caught the robber", Toast.LENGTH_SHORT).show();
                                mark[1] = 0;
                                final_mark[0] = mark[0];
                                final_mark[1] = mark[1];
                                final_mark[2] = mark[2];
                                final_mark[3] = mark[3];
                            } else {
                                Toast.makeText(GameActivity.this, "You aren't smart enough to catch the robber", Toast.LENGTH_SHORT).show();
                                mark[0] = 0;
                                final_mark[0] = 0;
                                final_mark[1] = mark[1];
                                final_mark[2] = mark[2];
                                final_mark[3] = mark[3];
                            }
                        }
                        P2.setEnabled(false);
                        P2.setClickable(false);

                    }
                });
                P3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        P2.setEnabled(false);
                        P2.setClickable(false);
                        P4.setEnabled(false);
                        P4.setClickable(false);
                        next.setEnabled(true);
                        next.setClickable(true);
                        int indx = score.getGame_no() + 1;

                        if (indx % 2 == 0) { //Even Thief
                            if (mark[2] == 70) {
                                Toast.makeText(GameActivity.this, "You have successfully caught the thief", Toast.LENGTH_SHORT).show();
                                mark[2] = 0;
                                final_mark[0] = mark[0];
                                final_mark[1] = mark[1];
                                final_mark[2] = mark[2];
                                final_mark[3] = mark[3];

                            } else {
                                Toast.makeText(GameActivity.this, "You aren't smart enough to catch the thief", Toast.LENGTH_SHORT).show();
                                mark[0] = 0;
                                final_mark[0] = mark[0];
                                final_mark[1] = mark[1];
                                final_mark[2] = mark[2];
                                final_mark[3] = mark[3];
                            }
                        } else {  // Odd Robber
                            if (mark[2] == 80) {
                                Toast.makeText(GameActivity.this, "You have successfully caught the robber", Toast.LENGTH_SHORT).show();
                                mark[2] = 0;
                                final_mark[0] = mark[0];
                                final_mark[1] = mark[1];
                                final_mark[2] = mark[2];
                                final_mark[3] = mark[3];
                            } else {
                                mark[0] = 0;
                                Toast.makeText(GameActivity.this, "You aren't smart enough to catch the robber", Toast.LENGTH_SHORT).show();
                                final_mark[0] = mark[0];
                                final_mark[1] = mark[1];
                                final_mark[2] = mark[2];
                                final_mark[3] = mark[3];
                            }
                        }
                        P3.setEnabled(false);
                        P3.setClickable(false);

                    }
                });

                P4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int indx = score.getGame_no() + 1;
                        P2.setEnabled(false);
                        P3.setEnabled(false);
                        P2.setClickable(false);
                        P3.setClickable(false);
                        next.setClickable(true);
                        next.setEnabled(true);
                        if (indx % 2 == 0) { // Even Thief
                            if (mark[3] == 70) {
                                Toast.makeText(GameActivity.this, "You have successfully caught the thief", Toast.LENGTH_SHORT).show();
                                mark[3] = 0;
                                final_mark[0] = mark[0];
                                final_mark[1] = mark[1];
                                final_mark[2] = mark[2];
                                final_mark[3] = mark[3];
                            } else {
                                Toast.makeText(GameActivity.this, "You aren't smart enough to catch the thief", Toast.LENGTH_SHORT).show();
                                mark[0] = 0;
                                final_mark[0] = mark[0];
                                final_mark[1] = mark[1];
                                final_mark[2] = mark[2];
                                final_mark[3] = mark[3];
                            }
                        } else {
                            if (mark[3] == 80) {
                                Toast.makeText(GameActivity.this, "You have successfully caught the robber", Toast.LENGTH_SHORT).show();
                                mark[3] = 0;
                                final_mark[0] = mark[0];
                                final_mark[1] = mark[1];
                                final_mark[2] = mark[2];
                                final_mark[3] = mark[3];
                            } else {
                                Toast.makeText(GameActivity.this, "You aren't smart enough to catch the robber", Toast.LENGTH_SHORT).show();
                                mark[0] = 0;
                                final_mark[0] = mark[0];
                                final_mark[1] = mark[1];
                                final_mark[2] = mark[2];
                                final_mark[3] = mark[3];
                            }
                        }
                        P4.setEnabled(false);
                        P4.setClickable(false);
                    }
                });

            } else {
                //If you are anything other than Police
                int indx = score.getGame_no() + 1;
                final_mark[0] = mark[0];
                final_mark[1] = mark[1];
                final_mark[2] = mark[2];
                final_mark[3] = mark[3];
                if( indx%2 == 0){
                    if(mark[0]==70){
                        final_mark[0]=0;
                    }else if(mark[1]==70){
                        final_mark[1]=0;
                    }else if(mark[2]==70){
                        final_mark[2]=0;
                    }else if(mark[3]==70){
                        final_mark[3]=0;
                    }
                }else{
                    if(mark[0]==80){
                        final_mark[0]=0;
                    }else if(mark[1]==80){
                        final_mark[1]=0;
                    }else if(mark[2]==80){
                        final_mark[2]=0;
                    }else if(mark[3]==80){
                        final_mark[3]=0;
                    }
                }
                if (mark[0] == 100) textView.setText("You're Landlord. It's not your turn.");
                else textView.setText("It's not your turn. Wait for their turn.");
            }
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent1 = new Intent(GameActivity.this, ShowResultActivity.class);
                    final Intent intent2 = new Intent(GameActivity.this, FinalResultActivity.class);
                    score.setGame_no(score.getGame_no()+1);

                    score.setPlayer1(final_mark[0]);
                    score.setPlayer2(final_mark[1]);
                    score.setPlayer3(final_mark[2]);
                    score.setPlayer4(final_mark[3]);

                    score.setSum1(final_mark[0] + score.getSum1());
                    score.setSum2(final_mark[1] + score.getSum2());
                    score.setSum3(final_mark[2] + score.getSum3());
                    score.setSum4(final_mark[3] + score.getSum4());

                    try {
                        databaseReference.child(key).setValue(score).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if(score.getGame_no()>=10) {
                                        intent2.putExtra("Key", key);
                                        startActivity(intent2);
                                    }else{
                                        intent1.putExtra("Key", key);
                                        startActivity(intent1);
                                    }
                                } else {
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