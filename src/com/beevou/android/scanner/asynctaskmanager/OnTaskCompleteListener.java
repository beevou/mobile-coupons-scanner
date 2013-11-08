package com.beevou.android.scanner.asynctaskmanager;

import android.os.AsyncTask;

public interface OnTaskCompleteListener {
    // Notifies about task completeness
    
    /** Notifies about task completeness */
    void onTaskComplete(AsyncTask task);
}