package com.android.devdefllo.application.fullscreendownloader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class FullScreenFragment extends SherlockFragment {

	private FragCallbacks mCallbacks;
	private ProgressBar progressBar;
	private TextView textFrag;
	
	public static interface FragCallbacks {
	    void onPreExecute();
	    void onProgressUpdate(int percent, ProgressBar progressBar, TextView textFrag);
	    void onCancelled();
	    void onPostExecute(String string);
	}
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    mCallbacks = (FragCallbacks) activity;
	    getSherlockActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    getSherlockActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	    getSherlockActivity().getSupportActionBar().hide();
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setRetainInstance(true);
		new AsyncTaskProgress().execute();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frag_view, container, false);
		textFrag = (TextView) v.findViewById(R.id.text2);
		progressBar = (ProgressBar) v.findViewById(R.id.progress);
		progressBar.getProgressDrawable().setColorFilter(0xFFCDF1FF, android.graphics.PorterDuff.Mode.MULTIPLY);
		return v;
	}
	
	class AsyncTaskProgress extends AsyncTask<Void,Integer,Void> {
		private int progressStatus = 0;
		
		@Override
	    protected void onPreExecute() {
			if (mCallbacks != null) {
				mCallbacks.onPreExecute();
			}
	    }
		
		@Override
		protected Void doInBackground(Void... ignore) {
			while(progressStatus < 100) {
				try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				progressStatus += 1;
				publishProgress(progressStatus);
			}
			return null;
		}
		@Override
		protected void onProgressUpdate(Integer... percent) {
			super.onProgressUpdate(percent);
			if (mCallbacks != null) {
				mCallbacks.onProgressUpdate(percent[0], progressBar, textFrag);
			}
	    }

		@Override
	    protected void onCancelled() {
			if (mCallbacks != null) {
				mCallbacks.onCancelled();
			}
	    }

	    @Override
	    protected void onPostExecute(Void ignore) {
	    	if (mCallbacks != null) {
	    		mCallbacks.onPostExecute("Loaded");
	    	}
	    }
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getSherlockActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getSherlockActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getSherlockActivity().getSupportActionBar().show();
	}
	
	@Override
	public void onDetach() {
	    super.onDetach();
	    mCallbacks = null;
	}
}
