package com.android.devdefllo.application.fullscreendownloader;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity implements FullScreenFragment.FragCallbacks {

	private FullScreenFragment mFullScreenFragment;
	private TextView textAct;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textAct = (TextView) findViewById(R.id.text);
		
		mFullScreenFragment = (FullScreenFragment) 
				getSupportFragmentManager().findFragmentByTag("FRAG_FULLSCREEN");
		
		((Button) findViewById(R.id.button)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(mFullScreenFragment == null) {
					mFullScreenFragment = new FullScreenFragment();
					getSupportFragmentManager().beginTransaction()
						.add(R.id.frame_view, mFullScreenFragment, "FRAG_FULLSCREEN").commit();
				} else if(!mFullScreenFragment.isVisible()) {
					getSupportFragmentManager().beginTransaction()
						.add(R.id.frame_view, mFullScreenFragment, "FRAG_FULLSCREEN").commit();
				}
			}
		});
	}
	
	// AsyncTask
	
	@Override
	public void onPreExecute() {  }

	@Override
	public void onProgressUpdate(int percent, ProgressBar progressBar, TextView textFrag) {
		progressBar.setProgress(percent);
		textFrag.setText(percent+"/"+progressBar.getMax());
	}

	@Override
	public void onCancelled() { }

	@Override
	public void onPostExecute(String result) {
		if(result.equals("Loaded")) {
			getSupportFragmentManager().beginTransaction().remove(mFullScreenFragment).commit();
			textAct.setText("The file is downloaded.");
		}
		else Toast.makeText(this, "The file is not downloaded.", Toast.LENGTH_LONG).show();
	}

}
