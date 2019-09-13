package com.imrandev.datacachesync.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.imrandev.datacachesync.R;
import com.imrandev.datacachesync.room.model.Post;
import com.imrandev.datacachesync.task.DeleteAsyncTask;
import com.imrandev.datacachesync.task.RestAsyncTask;
import com.imrandev.datacachesync.ui.MainActivity;

public class ApiJobService extends JobService {

    private JobParameters jobParameters;

    private static final String TAG = "ApiJobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        this.jobParameters = jobParameters;

        Log.e(TAG, "Job Started");

        RestAsyncTask restAsyncTask = new RestAsyncTask(getApplicationContext(), asyncCallback);
        restAsyncTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private RestAsyncTask.AsyncCallback asyncCallback = new RestAsyncTask.AsyncCallback() {
        @Override
        public void onReceive(int count, boolean isReschedule) {
            jobFinished(jobParameters, isReschedule);

            Log.e(TAG, "Job Finished");
        }

        @Override
        public void onDelete(Post post) {
            new Handler().postDelayed(() -> {
                DeleteAsyncTask asyncTask = new DeleteAsyncTask(getApplicationContext(), post);
                asyncTask.execute();
            }, 5000);
        }
    };
}
