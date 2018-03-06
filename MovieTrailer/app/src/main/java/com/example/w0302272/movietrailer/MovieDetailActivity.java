package com.example.w0302272.movietrailer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.content.Intent;
import android.view.View;

public class MovieDetailActivity extends AppCompatActivity {
    private Button btnPlay;
    private TextView txtTitle, txtDetail;
    private ImageView imgPoster;
    private ImageView imgRate1, imgRate2, imgRate3, imgRate4, imgRate5;
    private String strVideoURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        btnPlay = (Button) findViewById(R.id.btnPlay);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDetail = (TextView) findViewById(R.id.txtDetail);
        imgPoster = (ImageView) findViewById(R.id.imgPoster);
        imgRate1 = (ImageView) findViewById(R.id.imgRate1);
        imgRate2 = (ImageView) findViewById(R.id.imgRate2);
        imgRate3 = (ImageView) findViewById(R.id.imgRate3);
        imgRate4 = (ImageView) findViewById(R.id.imgRate4);
        imgRate5 = (ImageView) findViewById(R.id.imgRate5);

        // Get Movie Details
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String strTitle, strDescription;
            int poster, rating;
            // get the user name from Bundle
            strTitle = extras.getString("KEY_Title");
            strDescription = extras.getString("KEY_Description");
            poster = extras.getInt("KEY_ThumbUrl");
            rating = extras.getInt("KEY_Rate");
            this.strVideoURL = extras.getString("KEY_MovieUrl");

            this.txtTitle.setText(strTitle);
            this.txtDetail.setText(strDescription);
            this.imgPoster.setImageResource(poster);
            if(rating == 5) {
                this.imgRate1.setImageResource(android.R.drawable.btn_star_big_on);
                this.imgRate2.setImageResource(android.R.drawable.btn_star_big_on);
                this.imgRate3.setImageResource(android.R.drawable.btn_star_big_on);
                this.imgRate4.setImageResource(android.R.drawable.btn_star_big_on);
                this.imgRate5.setImageResource(android.R.drawable.btn_star_big_on);
            } else if (rating == 4) {
                this.imgRate1.setImageResource(android.R.drawable.btn_star_big_on);
                this.imgRate2.setImageResource(android.R.drawable.btn_star_big_on);
                this.imgRate3.setImageResource(android.R.drawable.btn_star_big_on);
                this.imgRate4.setImageResource(android.R.drawable.btn_star_big_on);
            } else if (rating == 3) {
                this.imgRate1.setImageResource(android.R.drawable.btn_star_big_on);
                this.imgRate2.setImageResource(android.R.drawable.btn_star_big_on);
                this.imgRate3.setImageResource(android.R.drawable.btn_star_big_on);
            } else if (rating ==2) {
                this.imgRate1.setImageResource(android.R.drawable.btn_star_big_on);
                this.imgRate2.setImageResource(android.R.drawable.btn_star_big_on);
            } else if (rating ==1){
                this.imgRate1.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                /* do nothing */
            }
        }

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to call an application
                Intent i = new Intent(MovieDetailActivity.this, VideoViewActivity.class);
                // Create a bundle to send an information along with the intent
                Bundle extras = new Bundle();
                extras.putString("KEY_PlayUrl", strVideoURL);
                i.putExtras(extras);
                startActivityForResult(i,1);
            }
        });
    }
}
