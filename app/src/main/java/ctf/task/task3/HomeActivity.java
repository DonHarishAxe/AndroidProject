package ctf.task.task3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends ActionBarActivity {

    private CheckList mAdapter;
    private ListView mListView;
    private ArrayList<String> checkListObjects = new ArrayList<>();
    private Button b1;
    EditText eText;
    private DatabaseHelper db;
    private String s1;
    private String s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        eText = (EditText) findViewById(R.id.Text1);
        b1 = (Button) findViewById(R.id.button123);
        db = new DatabaseHelper(this);
        checkListObjects = db.getListNames();
        s1 = new String();
        s2 = new String();
        if (checkListObjects.size() == 0)
            setContentView(R.layout.activity_display_empty);
        else {
            mListView = (ListView) findViewById(R.id.mainListView);
            mAdapter = new CheckList(this, checkListObjects);
            mListView.setAdapter(mAdapter);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //db.add
                    /*ArrayList<String> l=new ArrayList<String>();
                    l.add("Chellam");
                    CheckListObject p=new CheckListObject("new_my_list",l);
                    db.addListName(p);
                    db.addNewList(p);
                    checkListObjects = db.getListNames();
                    updateUI();*/
                    String check = (String) (eText.getText() + "");
                    eText.setText(db.search(check));

                }
            });

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView name = (TextView) view.findViewById(R.id.titleName);
                    String s = name.getText().toString();
                    DatabaseHelper temp = new DatabaseHelper(HomeActivity.this);
                    //temp.incCount(s);
                    CheckListObject c = temp.getOldList(s);
                    Toast.makeText(HomeActivity.this, s, Toast.LENGTH_LONG).show();
                    Intent disp = new Intent(HomeActivity.this, DisplayActivity.class);
                    disp.putExtra("single", s);
                    startActivity(disp);
                    //finish();
                }
            });
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView name = (TextView) view.findViewById(R.id.titleName);
                    String str = name.getText().toString();
                    //db.incCount(str);
                    createDupl(str);

                    if (s1.isEmpty()) s1 = str;
                    else {
                        s2 = s1;

                        s1 = str;
                        /*String s3=s1.concat(s2);
                        s3=s3+"_merge";
                        db.db_merge(s1,s2,s3);
                        checkListObjects = db.getListNames();
                        updateUI();
                        s1=new String();
                        s2=new String();*/
                    }
                    return true;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    //public void fn(String s1) {
    //    Toast.makeText(this, "Alarm set" + s1, Toast.LENGTH_SHORT).show();
    //}

    public void updateUI() {
        mAdapter = new CheckList(this, checkListObjects);
        mListView.setAdapter(mAdapter);
    }

    public void createDupl(String s) {
        createDuplDialog(s).show();

    }

    public AlertDialog createDuplDialog(final String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter name for duplicate list");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String m_Text = input.getText().toString();
                db.createDuplicate(s, m_Text);
                checkListObjects = db.getListNames();
                updateUI();
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

    public void cleanDatabase(String name) {
        CheckListObject s = db.getOldList(name);
        if (s.getTasks().size() == 0) {
            db.deleteListName(name);
            checkListObjects.remove(name);
            updateUI();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.merge:
                String s3=s1.concat(s2);
                        s3=s3+"_merge";
                        db.db_merge(s1,s2,s3);
                        checkListObjects = db.getListNames();
                        updateUI();
                        s1=new String();
                        s2=new String();
                        return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}