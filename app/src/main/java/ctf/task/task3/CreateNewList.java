package ctf.task.task3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateNewList extends ActionBarActivity {

    private TaskAdapter taskAdapter;
    private ArrayList<String> taskList;
    private ListView mListView;
    private EditText editText;
    private String title;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        mListView = (ListView) findViewById(R.id.listView);
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, taskList);
        mListView.setAdapter(taskAdapter);

        editText = (EditText) findViewById(R.id.checkListName);
        title = editText.toString();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewList.this);
                builder.setTitle("Add a task");
                final EditText inputField = new EditText(CreateNewList.this);
                builder.setView(inputField);
                builder.setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String task = inputField.getText().toString();
                                taskList.add(task);
                                updateUI();
                            }
                        });

                builder.setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });
    }

    public void updateUI() {
        taskAdapter = new TaskAdapter(this, taskList);
        mListView.setAdapter(taskAdapter);
    }

    public void deleteTask(View view) {
        View v = (View) view.getParent();

        TextView taskTextView = (TextView) v.findViewById(R.id.textView);
        String task = taskTextView.getText().toString();
        taskList.remove(task);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                title = editText.getText().toString();
                if(title == null)
                    Toast.makeText(this, "Enter a name for the list", Toast.LENGTH_SHORT).show();
                else if(taskList.size() == 0)
                    Toast.makeText(this, "No tasks created", Toast.LENGTH_SHORT).show();
                else {
                    db.addListName(new CheckListObject(title, taskList));
                    db.addNewList(new CheckListObject(title, taskList));
                    db.close();
                    Intent sendData = new Intent(this, HomeActivity.class);
                    startActivity(sendData);
                    finish();
                }
                return true;
            case R.id.cancel:
                Intent noList = new Intent(this, HomeActivity.class);
                startActivity(noList);
                finish();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

