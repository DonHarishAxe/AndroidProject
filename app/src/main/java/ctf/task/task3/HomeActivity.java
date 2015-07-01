package ctf.task.task3;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeActivity extends ActionBarActivity {

    private CheckList mAdapter;
    private ListView mListView;
    private ArrayList<String> checkListObjects = new ArrayList<>();

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DatabaseHelper(this);
        checkListObjects = db.getListNames();

        mListView = (ListView) findViewById(R.id.mainListView);
        mAdapter = new CheckList(this, checkListObjects);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView name = (TextView) view.findViewById(R.id.titleName);
                String s = name.getText().toString();

                Intent disp = new Intent(getApplicationContext(), DisplayActivity.class);
                disp.putExtra("single", s);
                startActivity(disp);
            }
        });

        db.close();
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
        if (item.getItemId() == R.id.action_settings) {
            Intent createNewList = new Intent(this, CreateNewList.class);
            //createNewList.putParcelableArrayListExtra("object", checkListObjects);
            startActivity(createNewList);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

