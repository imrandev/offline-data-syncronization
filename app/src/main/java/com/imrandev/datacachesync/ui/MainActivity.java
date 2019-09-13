package com.imrandev.datacachesync.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.imrandev.datacachesync.R;
import com.imrandev.datacachesync.manager.DataManager;
import com.imrandev.datacachesync.network.RetrofitClient;
import com.imrandev.datacachesync.network.response.EnqueueResponse;
import com.imrandev.datacachesync.room.model.Post;
import com.imrandev.datacachesync.task.PostAsyncTask;
import com.imrandev.datacachesync.util.Constant;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private boolean isOnline, _isNextIntent;

    @BindView(R.id.btn_requisition)
    Button mBtnRequisition;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onNetworkCheck(boolean isConnected) {
        this.isOnline = isConnected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBtnRequisition.setOnClickListener(this);
    }

    private void show(){
        progressDialog.show();
    }

    private void dismiss(){
        if (progressDialog == null) return;
        if (progressDialog.isShowing()){
            progressDialog.hide();

            if (_isNextIntent) goToRequisition();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void onlineTask(){
        Post post = DataManager.getInstance().getPost(getRandomId(), getRandomId());

        RetrofitClient.getInstance(Constant.BASE_URL).post(
                post.getId(),
                post.getUid(),
                post.getTitle(),
                post.getBody()
        ).enqueue(postEnqueueResponse);
    }

    private EnqueueResponse<Post> postEnqueueResponse = new EnqueueResponse<Post>() {

        @Override
        public void onReceived(Post results, String message) {
            _isNextIntent = true;
            showToast(message);
            dismiss();
        }

        @Override
        public void onError(String message) {
            offlineTask();
        }

        @Override
        public void onFailed(String message) {
            offlineTask();
        }
    };

    private void goToRequisition() {
        startActivity(new Intent(getApplicationContext(), RequisitionActivity.class));
        finish();
    }

    private void offlineTask(){

        Post post = DataManager.getInstance().getPost(getRandomId(), getRandomId());
        new PostAsyncTask(this, post, asyncCallback).execute();
    }

    private PostAsyncTask.AsyncCallback asyncCallback = count -> {
        _isNextIntent = true;
        showToast("Successfully stored into offline : Total items " + count);
        dismiss();
    };

    @Override
    public void onClick(View view) {
        show();
        if (isOnline){
            onlineTask();
        } else {
            offlineTask();
        }
    }
}
