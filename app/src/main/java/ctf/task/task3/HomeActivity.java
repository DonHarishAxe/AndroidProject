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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends ActionBarActivity {

    private CheckList mAdapter;
    private ListView mListView;
    private ArrayList<CheckListObject> checkListObjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mListView = (ListView) findViewById(R.id.mainListView);
        mAdapter = new CheckList(this, checkListObjects);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendCheckList(view);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String title = bundle.getString("checklist_name");
            ArrayList<String> items = bundle.getStringArrayList("checklist_items");
            checkListObjects = bundle.getParcelableArrayList("object");
            CheckListObject checkListObject = new CheckListObject();
            checkListObject.title = title;
            checkListObject.tasks = items;
            checkListObjects.add(checkListObject);
            updateUI();
        }

    }

    public void sendCheckList(View v) {
        TextView name = (TextView) v.findViewById(R.id.titleName);
        String s = name.getText().toString();
        CheckListObject c = new CheckListObject();
        c.title = s;
        int index = 0;
        for(index = 0; index < checkListObjects.size(); index++)
            if (checkListObjects.get(index).title.equals(c.title)) break;

        Intent disp = new Intent(this, DisplayActivity.class);
        disp.putExtra("single", checkListObjects.get(index));
        startActivity(disp);
    }

    public void updateUI() {
        mAdapter = new CheckList(this, checkListObjects);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent createNewList = new Intent(this, MainActivity.class);
                createNewList.putParcelableArrayListExtra("object", checkListObjects);
                startActivity(createNewList);
                return true;
            /*case R.id.action:
                Intent myIntent = new Intent(HomeActivity.this , Notif.class);
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getService(HomeActivity.this, 0, myIntent, 0);

                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3 * 1000, pendingIntent);
   /*             Intent resultIntent = new Intent(this, Notif.class);
                 NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle("My notification")
                                .setContentText("Hello World!");
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
  /             TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(Notif.class);
// Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                mNotificationManager.notify(100, mBuilder.build());
                Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT).show();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

class CheckList implements ListAdapter {

    private ArrayList<CheckListObject> mArray;
    private Context mContext;
    private LayoutInflater inflater;

    CheckList(Context context, ArrayList<CheckListObject> arrayList){
        mContext = context;
        mArray = arrayList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {}

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {}

    @Override
    public int getCount() {
        return mArray.size();
    }

    @Override
    public Object getItem(int position) {
        return mArray.get(position);
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
        convertView = inflater.inflate(R.layout.list_item_home, parent, false);

        TextView textView = (TextView) convertView.findViewById(R.id.titleName);
        textView.setText(mArray.get(position).title);
        TextView textView1 = (TextView) convertView.findViewById(R.id.noOfItems);
        textView1.setText(Integer.toString(mArray.get(position).tasks.size()));

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