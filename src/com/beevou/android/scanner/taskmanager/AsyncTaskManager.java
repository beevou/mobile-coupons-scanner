package com.beevou.android.scanner.taskmanager;

import com.beevou.android.scanner.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public final class AsyncTaskManager implements IProgressTracker, OnCancelListener {
    
    private final OnTaskCompleteListener mTaskCompleteListener;
    private Dialog pd;
    private Task mAsyncTask;
    private boolean mProgress = false;
    private View dView;
    private TextView progressText;
    
    public AsyncTaskManager(Activity activity, OnTaskCompleteListener taskCompleteListener) {
        // Save reference to complete listener (activity)
        mTaskCompleteListener = taskCompleteListener;
    }
    // Constructor with progress
    public AsyncTaskManager(Activity activity, OnTaskCompleteListener taskCompleteListener, String titleText) {
        
        this(activity, taskCompleteListener);
        
        mProgress = true;
        
        
        
        pd = new Dialog(activity, R.style.Theme_Dialog_Translucent);
                pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                dView =  inflater.inflate(R.layout.dialog,null);
                TextView progressTitle = (TextView) dView.findViewById(R.id.progressTitle);
                progressText = (TextView) dView.findViewById(R.id.progressText);
                
                progressTitle.setText(titleText);
                progressText.setText(R.string.loading_);
                pd.setContentView(dView);
            pd.setCancelable(false);
            
    }
    
    public void setupTask(Task asyncTask) {
        // Keep task
        mAsyncTask = asyncTask;
        // Wire task to tracker (this)
        mAsyncTask.setProgressTracker(this);
        // Start task
        mAsyncTask.execute();
    }

    @Override
    public void onProgress(String message) {
        // Show dialog if it wasn't shown yet or was removed on configuration (rotation) change
        if(mProgress)
        {
        	progressText.setText(message);
        	pd.show();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // Cancel task
        mAsyncTask.cancel(true);
        // Notify activity about completion
        mTaskCompleteListener.onTaskComplete(mAsyncTask);
        // Reset task
        mAsyncTask = null;
        
        if(mProgress)
                pd.dismiss();
    }
    
    @Override
    public void onComplete() {
        // Notify activity about completion
        mTaskCompleteListener.onTaskComplete(mAsyncTask);
        // Reset task
        mAsyncTask = null;
        
        if(mProgress)
                pd.dismiss();
    }

    public Object retainTask(){
        // Detach task from tracker (this) before retain
        if (mAsyncTask != null) {
                mAsyncTask.setProgressTracker(null);
        }
        // Retain task
        return mAsyncTask;
    }

    public void handleRetainedTask(Object instance) {
        // Restore retained task and attach it to tracker (this)
        if (instance instanceof Task) {
                mAsyncTask = (Task) instance;
                mAsyncTask.setProgressTracker(this);
        }
    }

    public boolean isWorking() {
        // Track current status
        return mAsyncTask != null;
    }
}
