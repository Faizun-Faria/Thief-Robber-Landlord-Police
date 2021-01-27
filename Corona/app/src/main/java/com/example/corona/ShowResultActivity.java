package com.example.corona;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

public class ShowResultActivity extends AppCompatActivity {
    protected TextView player1_score;
    protected TextView player2_score;
    protected TextView player3_score;
    protected TextView player4_score;
    protected TextView player1_total;
    protected TextView player2_total;
    protected TextView player3_total;
    protected TextView player4_total;
    protected TextView round_no_header;
    protected TextView round_no;
    protected Button next;
    DatabaseReference databaseReference;
    scoreboard score;
    void setScoreboard(scoreboard score) {
        this.score = score;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        player1_score = (TextView) findViewById(R.id.player1_score);
        player2_score = (TextView) findViewById(R.id.player2_score);
        player3_score = (TextView) findViewById(R.id.player3_score);
        player4_score = (TextView) findViewById(R.id.player4_score);

        player1_total = (TextView) findViewById(R.id.player1_total);
        player2_total = (TextView) findViewById(R.id.player2_total);
        player3_total = (TextView) findViewById(R.id.player3_total);
        player4_total = (TextView) findViewById(R.id.player4_total);

        next = (Button) findViewById(R.id.next);
        round_no_header = (TextView) findViewById(R.id.round_no_header);
        round_no = (TextView) findViewById(R.id.round_no);
        score = new scoreboard();
        Intent intent = getIntent();
        final String key = (String) intent.getStringExtra("Key");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("bl");
        databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scoreboard board = dataSnapshot.getValue(scoreboard.class);
                setScoreboard(board);
                round_no_header.setText("ROUND " + board.getGame_no());
                round_no.setText("ROUND " + board.getGame_no() + " Result");

                player1_score.setText(score.getPlayer1_name() + " : " + board.getPlayer1() );
                player2_score.setText(score.getPlayer2_name() + " : " + board.getPlayer2() );
                player3_score.setText(score.getPlayer3_name() + " : " + board.getPlayer3() );
                player4_score.setText(score.getPlayer4_name() + " : " +  board.getPlayer4() );

                player1_total.setText(score.getPlayer1_name() + " : " + board.getSum1() );
                player2_total.setText(score.getPlayer2_name() + " : " + board.getSum2() );
                player3_total.setText(score.getPlayer3_name() + " : " + board.getSum3() );
                player4_total.setText(score.getPlayer4_name() + " : " + board.getSum4() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ShowResultActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        round_no_header.setText("ROUND " + score.getGame_no());
        round_no.setText("ROUND " + score.getGame_no() + " Result");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(ShowResultActivity.this, GameActivity.class);
                    intent.putExtra("Key", key);
                    startActivity(intent);
            }
        });
    }
}

