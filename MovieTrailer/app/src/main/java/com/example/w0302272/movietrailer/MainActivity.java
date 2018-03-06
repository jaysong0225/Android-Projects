package com.example.w0302272.movietrailer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.MenuItemHoverListener;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.database.*;//for cursor
import java.io.*;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.AdapterView;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity {
    /* movie list */
    ListView list;
    ArrayList<Integer> thumbnailList = new ArrayList();
    ArrayList<String> titleList = new ArrayList();
    Cursor c;
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* create a new DB: movies1.db */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            String destPath = "/data/data/" + getPackageName() +"/database/movies1.db";

            File f = new File(destPath);
            if(!f.exists()){
                CopyDB(getBaseContext().getAssets().open("movies1.db"),
                        new FileOutputStream(destPath));
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        db = new DBAdapter(this);

        //add movies- CREATE
        db.open();

        // decision for refreshing the DB table
        if(db.numOfMovies() == 0) {
            // delete existing table records
            db.clearMovies();
            long id = db.insertMovie("Marvel's Avengers: Age of Ultron",
                    "When Tony Stark (Robert Downey Jr.) jumpstarts a dormant peacekeeping program, things go terribly awry, forcing him, Thor (Chris Hemsworth), the Incredible Hulk (Mark Ruffalo) and the rest of the Avengers to reassemble. As the fate of Earth hangs in the balance, the team is put to the ultimate test as they battle Ultron (James Spader), a technological terror hell-bent on human extinction. Along the way, they encounter two mysterious and powerful newcomers, Pietro and Wanda Maximoff.",
                    "http://videos.hd-trailers.net/Avengers_2_trailer_3_51-480p-HDTN.mp4", R.drawable.pic1, 5);
            id = db.insertMovie("Star Wars: The Force Awakens",
                    "Star Wars: The Force Awakens opens in theaters December 18, 2015.Star Wars: The Force Awakens is directed by J.J. Abrams from a screenplay by Lawrence Kasdan & Abrams, and features a cast including actors John Boyega, Daisy Ridley, Adam Driver, Oscar Isaac, Andy Serkis, Academy Award winner Lupita Nyong’o, Gwendoline Christie, Crystal Clarke, Pip Andersen, Domhnall Gleeson, and Max von Sydow.",
                    "http://videos.hd-trailers.net/Star_Wars_Episode_VII_The_Force_Awakens_2015_Trailer_B_5.1-480p-HDTN.mp4", R.drawable.pic2, 4);
            id = db.insertMovie("Tomorrowland",
                    "From Disney comes two-time Oscar® winner Brad Bird’s riveting, mystery adventure “Tomorrowland,” starring Academy Award® winner George Clooney. Bound by a shared destiny, former boy-genius Frank (Clooney), jaded by disillusionment, and Casey (Britt Robertson), a bright, optimistic teen bursting with scientific curiosity, embark on a danger-filled mission to unearth the secrets of an enigmatic place somewhere in time and space known only as “Tomorrowland.”",
                    "http://videos.hd-trailers.net/Tomorrowland_2015_Trailer_H_5.1-480p-HDTN.mp4", R.drawable.pic3, 2);
            id = db.insertMovie("Mission: Impossible Rogue Nation",
                    "Ethan and team take on their most impossible mission yet, eradicating the Syndicate – an International rogue organization as highly skilled as they are, committed to destroying the IMF",
                    "http://videos.hd-trailers.net/MissionImpossible5_TLR-2_5.1-480p-HDTN.mp4", R.drawable.pic4, 5);
            id = db.insertMovie("San Andreas",
                    "After the infamous San Andreas Fault finally gives, triggering a magnitude 9 earthquake in California, a search and rescue helicopter pilot (Dwayne Johnson) and his estranged wife make their way together from Los Angeles to San Francisco to save their only daughter.",
                    "http://videos.hd-trailers.net/San_Andreas_2015_International_Trailer_3_5.1-480p-HDTN.mp4", R.drawable.pic5, 2);
            id = db.insertMovie("Mad Max: Fury Road",
                    "George Miller‘s uncompromising vision of a world gone made gears up again in Mad Max: Fury Road, the fourth film of Road Warrior/Mad Max franchise",
                    "http://videos.hd-trailers.net/Mad_Max_Fury_Road_2015_Trailer_F4_5.1-480p-HDTN.mp4", R.drawable.pic6, 3);
            id = db.insertMovie("Furious 7",
                    "Continuing the global exploits in the unstoppable franchise built on speed, Vin Diesel, Paul Walker and Dwayne Johnson lead the returning cast of Furious 7. James Wan directs this chapter of the hugely successful series that also welcomes back favorites Michelle Rodriguez, Jordana Brewster, Tyrese Gibson, Chris “Ludacris” Bridges, Elsa Pataky and Lucas Black.",
                    "http://videos.hd-trailers.net/Furious_7_2015_International_Trailer_2_5.1-480p-HDTN.mp4", R.drawable.pic7, 1);
        }
        db.close();

        //get all movies - READ
        db.open();
        c = db.getAllMovie();
        if(c.moveToFirst())
        {
            do{
                // extract thumbnails and titles
                getMovieList(c);
            }while(c.moveToNext());
        }

        db.close();

        // Create a movie ListVIew
        Integer[] arrayThumbnail = new Integer[thumbnailList.size()];
        arrayThumbnail = thumbnailList.toArray(arrayThumbnail);

        String[] arrayTitle = new String[titleList.size()];
        arrayTitle = titleList.toArray(arrayTitle);

        MovieList listAdapter = new
                MovieList(MainActivity.this, arrayTitle, arrayThumbnail);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(listAdapter);

        /* ++ context menu for delete/rating option */
        registerForContextMenu(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Create an intent to call an application
                c.moveToPosition(position);
                Intent i = new Intent(MainActivity.this, MovieDetailActivity.class);
                // Create a bundle to send an information along with the intent
                /* ID, TITLE, DESCRIPTION, MOVIE_URL, THUMBNAIL_URL, RATING */
                Bundle extras = new Bundle();
                extras.putString("KEY_Title",c.getString(1));
                extras.putString("KEY_Description",c.getString(2));
                extras.putString("KEY_MovieUrl",c.getString(3));
                extras.putInt("KEY_ThumbUrl",c.getInt(4));
                extras.putInt("KEY_Rate",c.getInt(5));
                i.putExtras(extras);
                startActivityForResult(i,1);
            }
        });
    }

    /* ++ context menu for delete/rating option */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(getResources().getString(R.string.delete));
        menu.add(getResources().getString(R.string.rate));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int listPosition = info.position;
        c.moveToPosition(listPosition);

        if((item.getTitle()).equals("DELETE"))  {
            // Delete the item and refresh the list
            db.open();
            if(db.deleteMovie(c.getLong(0))) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_deleted),
                        Toast.LENGTH_LONG).show();
            }
            db.close();

            // Refresh the MainActivity
            finish();
            overridePendingTransition( 0, 0);
            startActivity(getIntent());
            overridePendingTransition( 0, 0);
        } else {
            // Open a new popup select list for rating
            CharSequence rates[] = new CharSequence[] {"5 Stars: Must See", "4 Stars: Great See", "3 Stars: Good See", "2 Stars: So-so", "1 Stars: Bad"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.score_movie));
            builder.setItems(rates, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // the user clicked on colors[which]
                    db.open();
                    if(db.updateMovie(c.getLong(0), (float)(5 - which) )) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_saved),
                                Toast.LENGTH_LONG).show();
                    }
                    db.close();
                    // Refresh the MainActivity
                    finish();
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    overridePendingTransition( 0, 0);
                }
            });
            builder.show();
        }
        return true;
    }
    /* --context menu for delete option */

    public void CopyDB(InputStream inputStream,OutputStream outputStream)
            throws IOException{
        //copy 1k bytes at a time
        byte[] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0)
        {
            outputStream.write(buffer,0,length);
        }
        inputStream.close();
        outputStream.close();

    }//end method CopyDB

    public void getMovieList(Cursor c)
    {
        this.thumbnailList.add(c.getInt(4));
        this.titleList.add(c.getString(1));
    }
}
