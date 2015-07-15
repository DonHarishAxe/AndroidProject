package ctf.task.task3;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class DisplayActivity extends ActionBarActivity {

    private CheckListObject checkListObject = new CheckListObject();
    private ArrayList<String> localCache = new ArrayList<>();

    Runner r = new Runner();
    private Calendar calendar;
    private int notifID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        DatabaseHelper db = new DatabaseHelper(this);

        calendar = Calendar.getInstance();

        String temp  = getIntent().getStringExtra("single");
         if(temp == null) { Toast.makeText(this, "NULL", Toast.LENGTH_LONG).show(); return;}
        checkListObject = db.getList(temp);
        notifID = db.getId(temp);

        ListView mListView = (ListView) findViewById(R.id.display_list);
        localCache = checkListObject.getTasks();
        DisplayAdapter mAdapter = new DisplayAdapter(this, localCache);
        mListView.setAdapter(mAdapter);

        TextView tV = (TextView) findViewById(R.id.checkListTitle);
        tV.setText(checkListObject.title);

        Toast.makeText(this, checkListObject.title, Toast.LENGTH_LONG).show();
        db.close();
    }

    public void updateUI() {
        ListView mListView = (ListView) this.findViewById(R.id.display_list);
        DisplayAdapter mAdapter = new DisplayAdapter(this, localCache);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    public void setTime(AlertDialog.Builder builder) {

        final TimePicker timePicker = new TimePicker(this);
        timePicker.setIs24HourView(true);

        builder.setView(timePicker);
        builder.setTitle("Set time");

        builder.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Integer hr = timePicker.getCurrentHour();
                        Integer min = timePicker.getCurrentMinute();
                        calendar.set(Calendar.HOUR_OF_DAY, hr);
                        calendar.set(Calendar.MINUTE, min);
                        calendar.set(Calendar.SECOND, 0);
                        setAlarm();
                    }
                });

        builder.create().show();
    }

    public void deleteTask(View v) {
        View view = (View) v.getParent();
        TextView t = (TextView) view.findViewById(R.id.taskName);
        String temp = t.getText().toString();

        DatabaseHelper db = new DatabaseHelper(DisplayActivity.this);
        db.deleteTask(temp);
        db.close();

        updateUI();
      /*  localCache.remove(temp);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.deleteTask(temp);

      /*  if(localCache.size() == 0) {
            db.deleteListName(temp);
            //db.close();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            removeAlarm();

        } else {
            db.close();
            updateUI();
        }*/
    }

    public void deleteList() {

        TextView tV = (TextView) findViewById(R.id.checkListTitle);
        String temp = tV.getText().toString();
        DatabaseHelper db = new DatabaseHelper(DisplayActivity.this);
        db.deleteListName(temp);
        db.close();

       // updateUI();
      /*  localCache.remove(temp);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.deleteTask(temp);

      /*  if(localCache.size() == 0) {
            db.deleteListName(temp);
            //db.close();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            removeAlarm();

        } else {
            db.close();
            updateUI();
        }*/
    }

    public void setAlarm() {
        Intent myIntent = new Intent(this , Notif.class);
        myIntent.putExtra("single", checkListObject);
        myIntent.putExtra("id", notifID);
        PendingIntent pendingIntent = PendingIntent.getService(this, notifID, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT).show();
    }

    public void removeAlarm() {
        Intent myIntent = new Intent(this , Notif.class);
        myIntent.putExtra("single", checkListObject);
        myIntent.putExtra("id", notifID);
        PendingIntent pendingIntent = PendingIntent.getService(this, notifID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action:

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Set date");

                final DatePicker datePicker = new DatePicker(this);

                builder.setView(datePicker);
                builder.setPositiveButton("Next",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Integer day = datePicker.getDayOfMonth();
                                Integer month = datePicker.getMonth();
                                Integer year = datePicker.getYear();
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.YEAR, year);
                                setTime(builder);
                            }
                        });

                builder.setNegativeButton("Cancel", null);
                builder.create().show();

                return true;
            case R.id.delete:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class Runner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String task = params[1];

            DatabaseHelper db = new DatabaseHelper(DisplayActivity.this);

            if(task.equals("delete_task")) db.deleteTask(params[0]);
            else if(task.equals("delete_list")) db.deleteListName(params[0]);

            db.close();


            if(isCancelled()) return null;
            return task;
        }

        @Override
        protected void onPostExecute(String s) {
            DisplayActivity disp = new DisplayActivity();
            disp.updateUI();

        }
    }
}


