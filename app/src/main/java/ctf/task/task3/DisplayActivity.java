package ctf.task.task3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayActivity extends ActionBarActivity {

    private ListView mListView;
    private DisplayAdapter mAdapter;
    private ArrayList<String> taskList = new ArrayList<>();

    private CheckListObject checkListObject = new CheckListObject();

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        db = new DatabaseHelper(this);

        String temp = getIntent().getStringExtra("single");
        checkListObject = db.getList(temp);

        mListView = (ListView) findViewById(R.id.display_list);
        mAdapter = new DisplayAdapter(this, checkListObject.getTasks());
        mListView.setAdapter(mAdapter);

        TextView tV = (TextView) findViewById(R.id.checkListTitle);
        tV.setText(checkListObject.title);

        db.close();

    }

    public void updateUI() {
        mAdapter = new DisplayAdapter(this, taskList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action:
                Intent myIntent = new Intent(this , Notif.class);
                myIntent.putExtra("single", checkListObject.title);
                PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3 * 1000, pendingIntent);

                Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


