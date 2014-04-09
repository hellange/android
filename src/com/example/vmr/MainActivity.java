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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
        setContentView(R.layout.activity_main);

        
        Log.d("com.example.vmr", "fisk");
        
        
        button = new Button( this );
        button.setText( "Touch me!" );
        button.setOnClickListener( this );
        setContentView(button);
       
        /*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        */
         /*
     // Create the text message with a string
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "xxx");
        sendIntent.setType(HTTP.PLAIN_TEXT_TYPE); // "text/plain" MIME type

        // Verify that the intent will resolve to an activity
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendIntent);
        } 
        
        
        
        /*
        /*
        
     // Always use string resources for UI text.
     // This says something like "Share this photo with"
     // Create intent to show chooser
     Intent chooser = Intent.createChooser(intent, "CHOSE SOMETHING");
     Intent sendIntent = new Intent();
     // Verify the intent will resolve to at least one activity
     if (sendIntent.resolveActivity(getPackageManager()) != null) {
         startActivity(sendIntent);
     }
     */
     // The indices for the projection array above.
    
     // Run query
        
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = Calendars.CONTENT_URI; 
        Log.d("com.example.vmr", "URI:"+uri.toString());

        String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND (" 
                                + Calendars.ACCOUNT_TYPE + " = ?) AND ("
                                + Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[] {"helgelangehaug@gmail.com", "com.google", "helgelangehaug@gmail.com"}; 
        // Submit the query and get a Cursor object back. 
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
     // Use the cursor to step through the returned records
        button.setText("HELGE"+cur);
        int count=0;
        Log.d("com.example.vmr", "--- calendar ---");

        while (cur.moveToNext()) {
           // long calID = 0;
            button.setText("HELGE2"+cur);
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

       

        getEvents();
        
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

        button.setText("Touched me " + touchCount + " times   !!!!!!!!!    !"+ cache);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
