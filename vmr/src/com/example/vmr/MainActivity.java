package com.example.vmr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener { // ActionBarActivity
	Intent intent = new Intent(Intent.ACTION_SEND);
	Button button;
    int touchCount;
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    
    
    private static final String[] COLS = new String[] 
      		{ CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART};

    String cache ="EMPTY";
    public static final String[] EVENT_PROJECTION = new String[] {
        Calendars._ID,                           // 0
        Calendars.ACCOUNT_NAME,                  // 1
        Calendars.CALENDAR_DISPLAY_NAME,         // 2
        Calendars.OWNER_ACCOUNT                  // 3
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d("com.example.vmr", "created");
        
        button = new Button( this );
        button.setText( "Touch me!" );
        button.setOnClickListener( this );
        setContentView(button);

        getCalendars();
        getEvents();
    }
   
   public void getCalendars(){
	    Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = Calendars.CONTENT_URI; 
        Log.d("com.example.vmr", "URI:"+uri.toString());

       // String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND (" 
       //                         + Calendars.ACCOUNT_TYPE + " = ?) AND ("
       //                         + Calendars.OWNER_ACCOUNT + " = ?))";
        String selection = "((" + Calendars.OWNER_ACCOUNT + " = ?))";
        
       // String[] selectionArgs = new String[] { "helgelangehaug@gmail.com", 
       // 										"com.google", 
       // 										"helgelangehaug@gmail.com" }; 
        
         String[] selectionArgs = new String[] {"helgelangehaug@gmail.com" }; 
        
        
        // Submit the query and get a Cursor object back. 
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        
        int count=0;
        Log.d("com.example.vmr", "--- calendar ---");

        while (cur.moveToNext()) {
           // long calID = 0;
            count++;
            String displayName = null;
            String accountName = null;
            String ownerName = null;
              
            // Get the field values
            //   calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            cache = displayName;
            button.setText("HELGE3"+displayName+count);

            // Do something with the values...
            Log.d("com.example.vmr", "displayName"+displayName);
        }
        Log.d("com.example.vmr", "--- calendar ---");
   }
   
   
   public void getEvents(){
	   GregorianCalendar beginWindow = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
       beginWindow.setTime(new Date());
       beginWindow.roll(Calendar.DAY_OF_MONTH, false);

       GregorianCalendar endWindow = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
       endWindow.setTime(new Date());
       endWindow.roll(Calendar.DAY_OF_MONTH, 10);

       String[] projection =
               new String[] {
                       CalendarContract.Instances._ID,
                       CalendarContract.Instances.BEGIN,
                       CalendarContract.Instances.END,
                       CalendarContract.Instances.EVENT_ID,
                       CalendarContract.Instances.TITLE
               };
       Cursor calCursor = CalendarContract.Instances.query(
               getContentResolver(),
               projection,
               beginWindow.getTimeInMillis(),
               endWindow.getTimeInMillis());
       SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm");
       ArrayList<String> result = new ArrayList<String>();
       Log.d("com.example.vmr", " ---- events --- ");

       while (calCursor.moveToNext()) {
               long id = calCursor.getLong(0);
               GregorianCalendar begin = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
               begin.setTimeInMillis(calCursor.getLong(1));
               GregorianCalendar end = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
               end.setTimeInMillis(calCursor.getLong(2));
               String title = calCursor.getString(4);
               String when = id + " " + formatter.format(begin.getTime()) + " " + formatter.format(end.getTime()) + " " + title;
               result.add(id + " " + formatter.format(begin.getTime()) + " " + formatter.format(end.getTime()) + " " + title);
               Log.d("com.example.vmr", when);
       }
       Log.d("com.example.vmr", " ---- events --- ");

	   
   }
    
    public void onClick(View v) {
        touchCount++;
        Log.d("com.example.vmr", "fisk"+touchCount+ "'"+cache);

        button.setText("Touched VMR " + touchCount + " times   !!"+ cache);
    }

}
