package com.imrandev.datacachesync.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.imrandev.datacachesync.app.DemoApp;

import java.util.UUID;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private byte _initFlag = 0;
    protected ProgressDialog progressDialog;


    protected abstract @LayoutRes int getLayoutRes();
    protected abstract void onNetworkCheck(boolean isConnected);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");

        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        if (progressDialog != null){
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
        progressDialog = null;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                String action = intent.getAction() != null ? intent.getAction() : "";
                if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                    if (_initFlag == 0){
                        _initFlag++;
                        return;
                    }
                    onNetworkCheck(isConnected(context));
                }
            }
        }
    };

    private boolean isConnected(Context context) {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnected();
    }

    protected void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    protected String getRandomId(){
        return UUID.randomUUID().toString();
    }
}
