package com.imrandev.datacachesync.task;

import android.content.Context;
import android.os.AsyncTask;

import com.imrandev.datacachesync.room.model.Post;
import com.imrandev.datacachesync.room.AppDatabase;

import java.lang.ref.WeakReference;

public class PostAsyncTask extends AsyncTask<Void, Void, Integer> {

    private WeakReference<Context> weakReference;
    private Post post;
    private AsyncCallback asyncCallback;

    public PostAsyncTask(Context context, Post post, AsyncCallback asyncCallback) {
        this.weakReference = new WeakReference<>(context);
        this.post = post;
        this.asyncCallback = asyncCallback;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        Context context = weakReference.get();
        AppDatabase database = AppDatabase.getAppDatabase(context.getApplicationContext());
        database.postDao().insert(post);
        return database.postDao().count();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        asyncCallback.onReceive(integer);
    }

    public interface AsyncCallback {
        void onReceive(int count);
    }
}
