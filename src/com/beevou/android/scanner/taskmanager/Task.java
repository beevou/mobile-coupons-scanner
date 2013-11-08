package com.beevou.android.scanner.taskmanager;


import android.content.res.Resources;
import android.os.AsyncTask;

public class Task extends AsyncTask<Void, String, Boolean> {
    
    protected final Resources mResources;
    
    private Boolean mResult;
    private String mProgressMessage;
    private IProgressTracker mProgressTracker;
    public int id;

    /* UI Thread */
    public Task(Resources resources) {
        // Keep reference to resources
        mResources = resources;
        // Initialise initial pre-execute message
        mProgressMessage = "start";
    }

    /* UI Thread */
    public void setProgressTracker(IProgressTracker progressTracker) {
        // Attach to progress tracker
        mProgressTracker = progressTracker;
        // Initialise progress tracker with current task state
        if (mProgressTracker != null) {
            mProgressTracker.onProgress(mProgressMessage);
            if (mResult != null) {
                mProgressTracker.onComplete();
            }
        }
    }

    /* UI Thread */
    @Override
    protected void onCancelled() {
        // Detach from progress tracker
        mProgressTracker = null;
    }
    
    /* UI Thread */
    @Override
    protected void onProgressUpdate(String... values) {
        // Update progress message 
        mProgressMessage = values[0];
        // And send it to progress tracker
        if (mProgressTracker != null) {
            mProgressTracker.onProgress(mProgressMessage);
        }
    }

    /* UI Thread */
    @Override
    protected void onPostExecute(Boolean result) {
        // Update result
        mResult = result;
        // And send it to progress tracker
        if (mProgressTracker != null) {
            mProgressTracker.onComplete();
        }
        // Detach from progress tracker
        mProgressTracker = null;
    }

    /* Separate Thread */
    @Override
    protected Boolean doInBackground(Void... arg0) {
        // Working in separate thread
        for (int i = 10; i > 0; --i)
        {
            // Check if task is cancelled
            if (isCancelled()) {
                // This return causes onPostExecute call on UI thread
                return false;
            }

            try {
                // This call causes onProgressUpdate call on UI thread
                publishProgress("work");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                // This return causes onPostExecute call on UI thread
                return false;
            }
        }
        // This return causes onPostExecute call on UI thread
        return true;
    }
}

