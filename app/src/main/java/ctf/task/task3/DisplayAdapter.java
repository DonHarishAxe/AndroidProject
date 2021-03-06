package ctf.task.task3;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayAdapter implements ListAdapter {

    private ArrayList<String> task;
    private Context t_context;
    private LayoutInflater inflater;
    private Boolean done;

    DisplayAdapter(Context context, ArrayList<String> updatedList, boolean isDone){
        t_context = context;
        inflater = (LayoutInflater) t_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        task = updatedList;
        done = isDone;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
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

        if(done){
            convertView = inflater.inflate(R.layout.list_item_display_done, parent, false);
            TextView oldName = (TextView) convertView.findViewById(R.id.name);
            oldName.setText(task.get(position));

            return convertView;
        }
        convertView = inflater.inflate(R.layout.list_item_display, parent, false);

        TextView taskName = (TextView) convertView.findViewById(R.id.taskName);
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
