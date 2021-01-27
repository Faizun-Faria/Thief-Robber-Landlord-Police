package com.example.corona;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.DatabaseMetaData;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    protected EditText username;
    protected Button playbutton;
    protected DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.username);
        playbutton = (Button) findViewById(R.id.playbutton);
        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String key = username.getText().toString().trim();
                if(key.isEmpty()){
                    username.setError("Enter username");
                    username.requestFocus();
                    return;
                }

                final String name[] = {"Sabi", "Ishmam", "Sadia", "Jigar", "Ifrat", "Raisa", "Nabhan", "Fahim", "Fahmid", "Muneeza"};
                List<String> strList = Arrays.asList(name);
                Collections.shuffle(strList);
                strList.toArray(name);

                scoreboard score = new scoreboard();
                score.setGame_no(0);

                score.setPlayer1(0);
                score.setPlayer2(0);
                score.setPlayer3(0);
                score.setPlayer4(0);

                score.setSum1(0);
                score.setSum2(0);
                score.setSum3(0);
                score.setSum4(0);

                score.setPlayer1_name(key);
                score.setPlayer2_name(name[0]);
                score.setPlayer3_name(name[1]);
                score.setPlayer4_name(name[2]);

                try {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("bl");
                    databaseReference.child(key).setValue(score).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                                intent.putExtra("Key", key);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
