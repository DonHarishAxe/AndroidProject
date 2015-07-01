package ctf.task.task3;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CheckListObject implements Parcelable {
    public String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<String> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<String> tasks;

    CheckListObject() {}

    CheckListObject(String name, ArrayList<String> list) {
        title = name;
        tasks = list;
    }

    protected CheckListObject(Parcel in) {
        title = in.readString();
        if (in.readByte() == 0x01) {
            tasks = new ArrayList<>();
            in.readList(tasks, String.class.getClassLoader());
        } else {
            tasks = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        if (tasks == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tasks);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CheckListObject> CREATOR = new Parcelable.Creator<CheckListObject>() {
        @Override
        public CheckListObject createFromParcel(Parcel in) {
            return new CheckListObject(in);
        }

        @Override
        public CheckListObject[] newArray(int size) {
            return new CheckListObject[size];
        }
    };
}