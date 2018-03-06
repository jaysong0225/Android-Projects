package com.example.w0302272.movietrailer;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.Log;

public class DBAdapter {
    public static final String DATABASE_NAME = "movies1.db";
    public static final String TABLE_NAME = "movies";
    private static final int DATABASE_VERSION = 2;

    public static final String TAG = "DBAdapter";

    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String MOVIE_URL = "MOVIE_URL";
    public static final String THUMBNAIL_URL = "THUMBNAIL_URL";
    public static final String RATING = "RATING";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    // static!! in a way that why "static main()" : catch-22
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db)
        {
            try{
                String sql = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s REAL)",
                        TABLE_NAME, ID, TITLE, DESCRIPTION, MOVIE_URL, THUMBNAIL_URL, RATING);
                db.execSQL(sql);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }//end method onCreate

        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
        {
            Log.w(TAG,"Upgrade database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);

        }//end method onUpgrade
    } // end inner class

    //open the database
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //close the database
    public void close()
    {
        DBHelper.close();
    }

    //clear the table
    public void clearMovies()
    {
      DBHelper.onUpgrade(db, 2, 2);
    }

    //count the items in the table
    public int numOfMovies()
    {
        String sql = String.format("SELECT COUNT(*) FROM %s", TABLE_NAME);
        Cursor mCount = db.rawQuery(sql, null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();
        return count;
    }

    //insert a movie into the database
    public long insertMovie(String title, String description, String movie_url, int thumbnail_url, float rate)
    {
        ContentValues cv = new ContentValues();
        cv.put(TITLE, title);
        cv.put(DESCRIPTION, description);
        cv.put(MOVIE_URL, movie_url);
        cv.put(THUMBNAIL_URL, thumbnail_url);
        cv.put(RATING, rate);

        return db.insert(TABLE_NAME, null, cv);
    }

    //delete a particular contact
    public boolean deleteMovie(long rowId)
    {
        return db.delete(TABLE_NAME,ID + "=" + rowId,null)>0;
    }

    //retrieve all the contacts
    public Cursor getAllMovie()
    {
        return db.query(TABLE_NAME,new String[]{ID, TITLE, DESCRIPTION, MOVIE_URL, THUMBNAIL_URL, RATING}
                ,null,null,null,null,null);
    }

    //retrieve a single contact
    public Cursor getMovie(long rowId) throws SQLException
    {
        Cursor mCursor = db.query(true, TABLE_NAME, new String[] {ID,
                TITLE,DESCRIPTION, MOVIE_URL, THUMBNAIL_URL, RATING},ID + "=" + rowId,null,null,null,null,null);
        if(mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //updates a contact
    public boolean updateMovie(long rowId, float rate)
    {
        ContentValues cval = new ContentValues();
        cval.put(RATING, rate);
        return db.update(TABLE_NAME, cval, ID + "=" + rowId, null) >0;
    }

}//end class DBAdapter
