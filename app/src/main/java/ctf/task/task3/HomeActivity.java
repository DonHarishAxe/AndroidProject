package ctf.task.task3;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.myFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createNewList = new Intent(getApplicationContext(), CreateNewList.class);
                startActivity(createNewList);
            }
        });

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


}

