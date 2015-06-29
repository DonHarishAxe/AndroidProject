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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        mListView = (ListView) findViewById(R.id.display_list);
        mAdapter = new DisplayAdapter(this, taskList);
        mListView.setAdapter(mAdapter);

        TextView tV = (TextView) findViewById(R.id.checkListTitle);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            checkListObject = bundle.getParcelable("single");
            taskList = checkListObject.tasks;
            tV.setText(checkListObject.title);
            Toast.makeText(this, checkListObject.title, Toast.LENGTH_SHORT).show();
            updateUI();
        }
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
                myIntent.putExtra("single", checkListObject);
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


class DisplayAdapter implements ListAdapter {

    private ArrayList<String> task;
    private Context t_context;
    private LayoutInflater inflater;

    DisplayAdapter(Context context, ArrayList<String> updatedList){
        t_context = context;
        inflater = (LayoutInflater) t_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        task = updatedList;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return task.size();
    }

    @Override
    public Object getItem(int position) {
        return task.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.list_item_display, parent, false);

        CheckedTextView taskName = (CheckedTextView) convertView.findViewById(R.id.taskName);
        taskName.setText(task.get(position));

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
