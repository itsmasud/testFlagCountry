package com.fieldnation.service.crawler;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by mc on 1/22/18.
 */

@RequiresApi(api = Build.VERSION_CODES.O)
public class CrawlerJobService extends JobService {
    private static final int JOB_ID = 1;

    public static void schedule(Context context, long alarmTime) {
        ComponentName componentName = new ComponentName(context, CrawlerJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName)
                .setMinimumLatency(alarmTime - System.currentTimeMillis());

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        startForegroundService(new Intent(this, WebCrawlerService.class));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
