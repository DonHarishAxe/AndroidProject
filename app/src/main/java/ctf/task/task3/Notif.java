package ctf.task.task3;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Random;

public class Notif extends Service {

    private static int id = 0;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String temp = intent.getStringExtra("single");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.alarm)
                        .setContentTitle(temp)
                        .setColor(getResources().getColor(android.R.color.holo_blue_light))
                        .setAutoCancel(true)
                        .setContentText("This task is to be completed");

        Intent openList = new Intent(this, DisplayActivity.class);
        openList.putExtra("single", temp);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, openList, 0);

        mBuilder.setContentIntent(resultPendingIntent);

        Random m = new Random();
        int t = m.nextInt();
        id = intent.getIntExtra("id", 100);
        //id++;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(t, mBuilder.build());

        Toast.makeText(this, intent.getStringExtra("single"), Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
