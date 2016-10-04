package ctf.task.task3;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class DisplayActivity extends ActionBarActivity {

    private CheckListObject checkListObjectOld = new CheckListObject();
    private CheckListObject checkListObjectNew = new CheckListObject();
    private ArrayList<String> localNewCache = new ArrayList<>();
    private ArrayList<String> localOldCache = new ArrayList<>();
    private Button b;
    private Calendar calendar;
    private int notifID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        DatabaseHelper db = new DatabaseHelper(this);

        calendar = Calendar.getInstance();

        String temp  = getIntent().getStringExtra("single");
        checkListObjectNew = db.getNewList(temp);
        checkListObjectOld = db.getOldList(temp);

        localNewCache = checkListObjectNew.getTasks();
        localOldCache = checkListObjectOld.getTasks();

        if(localNewCache.size() == 0 && localOldCache.size() == 0) {
            setContentView(R.layout.activity_display_empty);
            db.close();
            return;
        }
        notifID = db.getId(temp);

        ListView newListView = (ListView) findViewById(R.id.display_list);
        b = (Button) findViewById(R.id.button345);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddDialog().show();
            }
        });
        DisplayAdapter pendingTaskAdapter = new DisplayAdapter(this, localNewCache, false);
        newListView.setAdapter(pendingTaskAdapter);
        newListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView t = (TextView) view.findViewById(R.id.taskName);
                String s = t.getText().toString();
                doneTask(s);
            }
        });

        newListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView t = (TextView) view.findViewById(R.id.taskName);
                String s = t.getText().toString();
                //modifyNewTask("complete assess","complete sem");
                createDeleteDialog(s,true).show();

                return true;
            }
        });

        ListView doneListView = (ListView) findViewById(R.id.display_done_list);

        DisplayAdapter doneTaskAdapter = new DisplayAdapter(this, localOldCache, true);
        doneListView.setAdapter(doneTaskAdapter);
        doneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView t = (TextView) view.findViewById(R.id.name);
                String s = t.getText().toString();
                undoTask(s);
            }
        });

        doneListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView t = (TextView) view.findViewById(R.id.name);
                String s = t.getText().toString();
                createDeleteDialog(s,false).show();
                //modifyDoneTask("waste","dhendam");
                return true;
            }
        });


        TextView tV = (TextView) findViewById(R.id.checkListTitle);
        tV.setText(checkListObjectNew.title);

        db.close();
    }
    public AlertDialog createAddDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new item");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String m_Text = input.getText().toString();
                addTask(m_Text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }
    public AlertDialog createModifyDialog(final String temp,final Boolean b){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter the new item name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String m_Text = input.getText().toString();
                if(b==false)
                modifyDoneTask(temp,m_Text);
                else
                modifyNewTask(temp,m_Text);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public AlertDialog createDeleteDialog(String task,final Boolean b) {
        AlertDialog.Builder alb = new AlertDialog.Builder(this);
        alb.setMessage("Delete Task");
        TextView tV = new TextView(this);
        tV.setText("Do you want to delete this task?");
        tV.setTextSize(16);
        tV.setPadding(5,0,5,0);
        tV.setTextColor(Color.BLACK);
        tV.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        alb.setView(tV);

        final String temp = task;

        alb.setCancelable(true);
        alb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTask(temp);
            }
        });
        alb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createModifyDialog(temp,b).show();
            }
        });
        return alb.create();
    }
    public void updateUI() {
        ListView mListView = (ListView) this.findViewById(R.id.display_list);
        DisplayAdapter pendingTaskAdapter = new DisplayAdapter(this, localNewCache, false);
        mListView.setAdapter(pendingTaskAdapter);

        ListView doneListView = (ListView) findViewById(R.id.display_done_list);
        DisplayAdapter doneTaskAdapter = new DisplayAdapter(this, localOldCache, true);
        doneListView.setAdapter(doneTaskAdapter);
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
    public void modifyNewTask(String temp,String newname){
        DatabaseHelper db = new DatabaseHelper(DisplayActivity.this);
        db.deleteNewTask(temp);
        localNewCache.remove(temp);
        localNewCache.add(newname);
        ArrayList<String> singleItem = new ArrayList<>();
        singleItem.add(newname);

        db.addNewList(new CheckListObject(checkListObjectNew.title, singleItem));
        updateUI();
        db.close();

    }
    public void modifyDoneTask(String temp,String newname){
        DatabaseHelper db = new DatabaseHelper(DisplayActivity.this);
        db.deleteOldTask(temp);
        localOldCache.remove(temp);
        localOldCache.add(newname);
        ArrayList<String> singleItem = new ArrayList<>();
        singleItem.add(newname);

        db.addNewList(new CheckListObject(checkListObjectOld.title, singleItem));
        updateUI();
        db.close();

    }
    public void addTask(String newname)
    {
        DatabaseHelper db = new DatabaseHelper(DisplayActivity.this);
        ArrayList<String> singleItem = new ArrayList<>();
        singleItem.add(newname);
        localNewCache.add(newname);
        db.addNewList(new CheckListObject(checkListObjectNew.title, singleItem));
        updateUI();
        db.close();
    }
    public void deleteTask(String temp) {
        DatabaseHelper db = new DatabaseHelper(DisplayActivity.this);
        db.deleteNewTask(temp);
        db.deleteOldTask(temp);

        localOldCache.remove(temp);
        localNewCache.remove(temp);
        updateUI();

        db.close();
    }
    public void doneTask(String temp) {
        //View view = (View) v.getParent();
        //TextView t = (TextView) view.findViewById(R.id.taskName);
        //String temp = t.getText().toString();

        DatabaseHelper db = new DatabaseHelper(DisplayActivity.this);
        db.deleteNewTask(temp);

        ArrayList<String> singleItem = new ArrayList<>();
        singleItem.add(temp);

        db.addOldList(new CheckListObject(checkListObjectNew.title, singleItem));
        db.close();

        localNewCache.remove(temp);
        localOldCache.add(temp);
        updateUI();
    }

    public void undoTask(String temp) {
        //View view = (View) v.getParent();
        //TextView t = (TextView) view.findViewById(R.id.taskName);
        //String temp = t.getText().toString();

        DatabaseHelper db = new DatabaseHelper(DisplayActivity.this);
        db.deleteOldTask(temp);

        ArrayList<String> singleItem = new ArrayList<>();
        singleItem.add(temp);

        db.addNewList(new CheckListObject(checkListObjectOld.title, singleItem));
        db.close();

        localNewCache.add(temp);
        localOldCache.remove(temp);
        updateUI();
    }

    public void deleteList() {

        String temp = checkListObjectNew.title;

        DatabaseHelper db = new DatabaseHelper(DisplayActivity.this);
        db.deleteListName(temp);
        db.close();

    }

    public void setAlarm() {
        Intent myIntent = new Intent(this , Notif.class);
        myIntent.putExtra("single", checkListObjectNew.title);
        myIntent.putExtra("id", notifID);
        PendingIntent pendingIntent = PendingIntent.getService(this, notifID, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT).show();
    }

    public void removeAlarm() {
        Intent myIntent = new Intent(this , Notif.class);
        myIntent.putExtra("single", checkListObjectNew.title);
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
                if(localNewCache.isEmpty())
                {
                    Toast.makeText(this, "Can't set alarm. All tasks completed", Toast.LENGTH_SHORT).show();
                    return true;
                }
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
                deleteList();
                removeAlarm();
                startActivity(new Intent(this, HomeActivity.class));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


