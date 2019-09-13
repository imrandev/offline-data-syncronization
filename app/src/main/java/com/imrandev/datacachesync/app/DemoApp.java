package com.imrandev.datacachesync.app;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;

import com.imrandev.datacachesync.service.ApiJobService;

public class DemoApp extends Application {

    private static final int JOB_ID = 0;
    private JobScheduler jobScheduler;

    @Override
    public void onCreate() {
        super.onCreate();
        scheduleJob();
    }

    public void scheduleJob(){
        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        ComponentName componentName = new ComponentName(this,
                ApiJobService.class.getName());

        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setMinimumLatency(5000)
                .build();
        jobScheduler.schedule(jobInfo);
    }

    public void cancelJob(){
        if (jobScheduler != null){
            jobScheduler.cancelAll();
            jobScheduler = null;
        }
    }
}
