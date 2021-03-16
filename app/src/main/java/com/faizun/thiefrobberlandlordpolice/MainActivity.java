package com.faizun.thiefrobberlandlordpolice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    TextView title;
    TextInputLayout filledTextField;
    TextInputEditText username;
    Button play;

    protected DatabaseReference databaseReference;

    public static final String[] languages = {"Select Language", "English", "বাংলা"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        spinner = findViewById(R.id.spinner);
        filledTextField = findViewById(R.id.filledTextField);
        play = findViewById(R.id.playbutton);
        username = findViewById(R.id.username);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                boolean flag = false;
                if (selectedLanguage.equals("English")) {
                    setLocal(MainActivity.this, "en");
                    flag = true;
                } else if (selectedLanguage.equals("বাংলা")) {
                    setLocal(MainActivity.this, "bn");
                    flag = true;
                } else {
                    Toast.makeText(MainActivity.this, "Please, select a Language", Toast.LENGTH_SHORT).show();
                }
                if (flag) {
                    title.setText(getString(R.string.title));
                    filledTextField.setHint(getString(R.string.enter_name));
                    play.setText(getString(R.string.play));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String key = username.getText().toString().trim();
                if(key.isEmpty()){
                    username.setError("Enter username");
                    username.requestFocus();
                    return;
                }

                final String name[] = {"Player 2", "Player 3", "Player 4"};

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
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("play");
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

    public void setLocal(Activity activity, String code) {
        Locale locale = new Locale(code);
        locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}