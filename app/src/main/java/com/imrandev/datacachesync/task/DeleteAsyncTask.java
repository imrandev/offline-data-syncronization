package com.imrandev.datacachesync.task;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.imrandev.datacachesync.R;
import com.imrandev.datacachesync.room.AppDatabase;
import com.imrandev.datacachesync.room.model.Post;
import com.imrandev.datacachesync.ui.MainActivity;

import java.lang.ref.WeakReference;

import static android.content.Context.NOTIFICATION_SERVICE;

public class DeleteAsyncTask extends AsyncTask<Void,Void, Integer> {

    private NotificationManager mNotifyManager;
    private PendingIntent contentPendingIntent;
    private WeakReference<Context> weakReference;
    private Post post;
    private int total;

    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    private static final String TAG = "DeleteAsyncTask";

    public DeleteAsyncTask(Context context, Post post) {
        this.weakReference = new WeakReference<>(context);
        this.post = post;
    }

    @Override
    protected void onPreExecute() {
        //Create the notification channel
        createNotificationChannel();

        //Set up the notification content intent to launch the app when clicked
        contentPendingIntent = PendingIntent.getActivity
                (weakReference.get(), 0, new Intent(
                        weakReference.get(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        Context context = weakReference.get();
        AppDatabase database = AppDatabase.getAppDatabase(context.getApplicationContext());
        total = database.postDao().count();
        database.postDao().delete(post);
        return database.postDao().count();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        Context context = weakReference.get();

        int count = total - integer;
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, PRIMARY_CHANNEL_ID)
                .setContentTitle("Job Service")
                .setContentText(String.format("Your Job ran to completion! (%s/%s)", count, total))
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);
        mNotifyManager.notify(0, builder.build());
        Log.e(TAG, String.format("Job done %s / %s", count, total));
    }

    private void createNotificationChannel() {

        // Define notification manager object.
        mNotifyManager =
                (NotificationManager) weakReference.get().getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Job Service notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifications from Job Service");

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }
}
