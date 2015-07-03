package ctf.task.task3;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class DisplayActivity extends ActionBarActivity {

    private CheckListObject checkListObject = new CheckListObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        DatabaseHelper db = new DatabaseHelper(this);

        String temp = getIntent().getStringExtra("single");
        checkListObject = db.getList(temp);

        ListView mListView = (ListView) findViewById(R.id.display_list);
        DisplayAdapter mAdapter = new DisplayAdapter(this, checkListObject.getTasks());
        mListView.setAdapter(mAdapter);

        TextView tV = (TextView) findViewById(R.id.checkListTitle);
        tV.setText(checkListObject.title);

        db.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    public void setTime(AlertDialog.Builder builder, final Calendar calendar) {

        final TimePicker timePicker = new TimePicker(this);
        timePicker.setIs24HourView(true);

        builder.setView(timePicker);
        builder.setMessage("Set time");

        builder.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Integer hr = timePicker.getCurrentHour();
                        Integer min = timePicker.getCurrentMinute();
                        calendar.set(Calendar.HOUR_OF_DAY, hr);
                        calendar.set(Calendar.MINUTE, min);
                        calendar.set(Calendar.SECOND, 0);
                        setAlarm(calendar);
                    }
                });

        builder.create().show();
    }
    public void setAlarm(Calendar calendar) {
        Intent myIntent = new Intent(this , Notif.class);
        myIntent.putExtra("single", checkListObject.title);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Notify");
                builder.setMessage("Set date");
                final Calendar calendar = Calendar.getInstance();

                final DatePicker datePicker = new DatePicker(this);

                builder.setView(datePicker);
                builder.setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Integer day = datePicker.getDayOfMonth();
                                Integer month = datePicker.getMonth();
                                Integer year = datePicker.getYear();
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.YEAR, year);
                                setTime(builder, calendar);
                            }
                        });

                builder.setNegativeButton("Cancel", null);
                builder.create().show();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


