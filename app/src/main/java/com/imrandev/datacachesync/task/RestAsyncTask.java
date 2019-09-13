package com.imrandev.datacachesync.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.imrandev.datacachesync.network.RetrofitClient;
import com.imrandev.datacachesync.network.response.EnqueueResponse;
import com.imrandev.datacachesync.room.AppDatabase;
import com.imrandev.datacachesync.room.model.Post;
import com.imrandev.datacachesync.util.Constant;

import java.lang.ref.WeakReference;
import java.util.List;

public class RestAsyncTask extends AsyncTask<Void, Integer, Integer> {

    private WeakReference<Context> weakReference;
    private AsyncCallback asyncCallback;

    public RestAsyncTask(Context context, AsyncCallback asyncCallback) {
        this.weakReference = new WeakReference<>(context);
        this.asyncCallback = asyncCallback;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        Context context = weakReference.get();
        AppDatabase database = AppDatabase.getAppDatabase(context.getApplicationContext());
        List<Post> posts = database.postDao().getAll();
        int total = database.postDao().count();

        if (total > 0){
            for (Post post: posts) {
                RetrofitClient.getInstance(Constant.BASE_URL).post(
                        post.getId(),
                        post.getUid(),
                        post.getTitle(),
                        post.getBody()
                ).enqueue(new EnqueueResponse<Post>() {
                    @Override
                    public void onReceived(Post data, String message) {
                        asyncCallback.onDelete(data);
                    }

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onFailed(String message) {

                    }
                });
            }
        } else {
            asyncCallback.onReceive(total, false);
        }

        return database.postDao().count();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }

    @Override
    protected void onPostExecute(Integer integer) {
        asyncCallback.onReceive(integer, integer > 0);
    }

    public interface AsyncCallback {
        void onReceive(int count, boolean isReschedule);
        void onDelete(Post post);
    }

}
