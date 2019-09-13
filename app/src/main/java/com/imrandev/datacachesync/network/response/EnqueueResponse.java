package com.imrandev.datacachesync.network.response;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class EnqueueResponse<T> implements Callback<T> {

    private static final String TAG = "EnqueueResponse";

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        String message = response.raw().message();
        if (response.isSuccessful()){
            T data = response.body();
            if (data != null){
                onReceived(data, "Successfully data sent to server");
            } else {
                onError(message);
            }
        }
        Log.e(TAG, "onResponse: " + message);
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        onFailed(t.getMessage());
        Log.e(TAG, "onFailure: " + t.getMessage());
    }

    public abstract void onReceived(T data, String message);
    public abstract void onError(String message);
    public abstract void onFailed(String message);
}
