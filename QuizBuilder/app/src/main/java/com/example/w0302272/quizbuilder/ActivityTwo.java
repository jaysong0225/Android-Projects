package com.example.w0302272.quizbuilder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.support.design.widget.Snackbar;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import android.util.Log;

public class ActivityTwo extends AppCompatActivity {
    private TextView txtName;   // TextView for user name
    private TextView txtDefinition;    // TextView for Question
    private TextView txtProgress;
    private RadioGroup radioGrpTerm;
    private RadioButton radioBtnTerm1;
    private RadioButton radioBtnTerm2;
    private RadioButton radioBtnTerm3;
    private RadioButton radioBtnTerm4;
    private Button btnNext;

    // Array list for 10 definitions(questions) / terminologies(answers)
    ArrayList<String> arlTerm = new ArrayList();
    ArrayList<String> arlDef = new ArrayList();
    ArrayList<String> arlQuiz;
    String strQuestion;
    Map<String, String> map = new HashMap<String, String>();
    int scoreCount;     // counting for current score

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        // Init Data
        this.scoreCount = 0;

        // Find layout elements
        this.txtName = (TextView) findViewById(R.id.txtName);
        this.txtDefinition = (TextView) findViewById(R.id.txtDefinition);
        this.txtProgress = (TextView) findViewById(R.id.txtProgress);
        this.radioGrpTerm = (RadioGroup) findViewById(R.id.radioGrpTerm);
        this.radioBtnTerm1 = (RadioButton) findViewById(R.id.radioBtnTerm1);
        this.radioBtnTerm2 = (RadioButton) findViewById(R.id.radioBtnTerm2);
        this.radioBtnTerm3 = (RadioButton) findViewById(R.id.radioBtnTerm3);
        this.radioBtnTerm4 = (RadioButton) findViewById(R.id.radioBtnTerm4);
        this.btnNext = (Button) findViewById(R.id.btnNext);

        // Get user name
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String userName;
            // get the user name from Bundle
            userName = extras.getString("KEY");
            this.txtName.setText("Welcome " + userName + "!");
        }

        // Step 1: Extract 10 quiz context(term:definition) from a file
        InputStream is = this.getResources().openRawResource(R.raw.testfile);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String strReadLine;
        try{
            while((strReadLine = br.readLine()) != null) {
                // save the quiz into an array of terminology, definition
                String[] tempStr = strReadLine.split(":");
                this.arlTerm.add(tempStr[0]);
                this.arlDef.add(tempStr[1]);
            }
            br.close();  // close the buffer
        }catch(IOException e) {
            e.printStackTrace();
        }

        // Step 2: Save the quiz into a hash table
        for(int i=0; i < this.arlDef.size(); i++) {
            this.map.put(this.arlDef.get(i), this.arlTerm.get(i));
        }

        // Step 3: Shuffling the questions
        Collections.shuffle(this.arlDef);

        // Step 4: Build the quiz
        if(this.arlDef.size() > 0) {
            // Take out the bottom question from the list
            this.strQuestion = this.arlDef.get(this.arlDef.size() - 1);
            // Build a multiple choice
            this.arlQuiz = buildCurrentQuiz(this.strQuestion);

            // Display on the screen
            this.txtDefinition.setText(this.strQuestion);
            this.radioBtnTerm1.setText(this.arlQuiz.get(0));
            this.radioBtnTerm2.setText(this.arlQuiz.get(1));
            this.radioBtnTerm3.setText(this.arlQuiz.get(2));
            this.radioBtnTerm4.setText(this.arlQuiz.get(3));
            // Remove the current question
            this.arlDef.remove(this.arlDef.size() - 1);
        }

        // Find and set OnClickListener for Next button
        setNextOnClickListener();

        // Find and set OnClickListener for Quit button
        setQuitOnClickListener();
    }

    private void setNextOnClickListener() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Score the current question
            if(radioGrpTerm.getCheckedRadioButtonId() != -1) {
                RadioButton rb = (RadioButton) radioGrpTerm.findViewById(radioGrpTerm.getCheckedRadioButtonId());
                radioGrpTerm.clearCheck();
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("AcitivityTwo", "FileIO Exception: "+ e.getMessage());
                }
                if (map.get(strQuestion) == rb.getText().toString()) {
                    scoreCount++;
                }

                // Get next question
                // Extract the current Term from the list and delete from the list
                if (arlDef.size() > 0) {
                    // Display the progress
                    int progress = 11 - arlDef.size();
                    txtProgress.setText(progress + "/10");

                    strQuestion = arlDef.get(arlDef.size() - 1);
                    arlQuiz = buildCurrentQuiz(strQuestion);

                    // Display on the screen
                    txtDefinition.setText(strQuestion);
                    radioBtnTerm1.setText(arlQuiz.get(0));
                    radioBtnTerm2.setText(arlQuiz.get(1));
                    radioBtnTerm3.setText(arlQuiz.get(2));
                    radioBtnTerm4.setText(arlQuiz.get(3));
                    // Remove the current question
                    arlDef.remove(arlDef.size() - 1);
                } else {
                    // All quiz done. Display the result using "Snackbar"
                    if(btnNext.getText().toString() != "Quit") {
                        Snackbar mySnackbar = Snackbar.make(view, "You received " + scoreCount + " points out of 10!", Snackbar.LENGTH_LONG);
                        mySnackbar.setAction("Exit", new MyExitListener());
                        mySnackbar.show();
                        // Change the button display
                        btnNext.setText("Quit");
                    } else {
                        // Exit the quiz
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                }
            } else {
                if(arlDef.size() > 0)
                    Toast.makeText(getBaseContext(),"Try to choose the answer",Toast.LENGTH_SHORT).show();
                else {
                    //Exit the quiz
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
            }
        });

    }

    private class MyExitListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            // Code to exit the user's last action
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }
    private void setQuitOnClickListener() {
        findViewById(R.id.btnQuit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Restart the quiz
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);
            }
        }); // end annon inner class
    }

    private ArrayList<String> buildCurrentQuiz(String inputDef) {
        // Copy of Definition array
        ArrayList<String> arlTempTerm = new ArrayList<String>(arlTerm);
        // Remove an answer from the temporal definition list
        arlTempTerm.remove(map.get(inputDef));
        // Shuffle the temporal definition list
        Collections.shuffle(arlTempTerm);
        // Get three definitions from the list
        while(arlTempTerm.size() > 3) arlTempTerm.remove(arlTempTerm.size()-1);
        arlTempTerm.add(map.get(inputDef));
        // Shuffle the list to make the final multiple choice
        Collections.shuffle(arlTempTerm);
        return arlTempTerm;
    }
}