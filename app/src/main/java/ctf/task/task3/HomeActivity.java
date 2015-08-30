package ctf.task.task3;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        if(checkListObjects.size() == 0)
            setContentView(R.layout.activity_display_empty);
        else {
            mListView = (ListView) findViewById(R.id.mainListView);
            mAdapter = new CheckList(this, checkListObjects);
            mListView.setAdapter(mAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView name = (TextView) view.findViewById(R.id.titleName);
                    String s = name.getText().toString();
                    DatabaseHelper temp = new DatabaseHelper(HomeActivity.this);
                    CheckListObject c = temp.getOldList(s);
                    Toast.makeText(HomeActivity.this, s, Toast.LENGTH_LONG).show();
                    Intent disp = new Intent(HomeActivity.this, DisplayActivity.class);
                    disp.putExtra("single", s);
                    startActivity(disp);
                    //finish();
                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.myFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createNewList = new Intent(getApplicationContext(), CreateNewList.class);
                startActivity(createNewList);
            }
        });
        db.close();
    }

    public void updateUI() {
        mAdapter = new CheckList(this, checkListObjects);
        mListView.setAdapter(mAdapter);
    }

    public void cleanDatabase(String name) {
        CheckListObject s = db.getOldList(name);
        if(s.getTasks().size() == 0) {
            db.deleteListName(name);
            checkListObjects.remove(name);
            updateUI();
        }


    }

}

