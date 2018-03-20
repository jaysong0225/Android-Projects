package com.example.w0302272.quizbuilder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.content.Intent;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Button btnStart;
    private EditText editUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button) findViewById(R.id.btnStart);
        editUserName = (EditText) findViewById(R.id.editUserName);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate the input name: Allows Alphabet only with 2~10 length
                if(editUserName.getText().toString().matches( "[a-zA-Z ]{2,10}" ) ) {
                    // Create an intent to call an application
                    Intent i = new Intent("ActivityTwo");
                    // Create a bundle to send an information along with the intent
                    Bundle extras = new Bundle();
                    extras.putString("KEY",editUserName.getText().toString());
                    i.putExtras(extras);
                    startActivityForResult(i,1);
                } else {
                    // Input name is invalid. Toast a message
                    Toast.makeText(getBaseContext(),"Oops! What is your first name?",Toast.LENGTH_LONG).show();
                }


            }
        });
    }
}
