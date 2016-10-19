package com.androidbook.messengerclient;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements ISampleServiceClient {

    protected static final String TAG = "MessengerClient";
    private TextView mCallbackText;
    private ClientFrag clientFrag;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mCallbackText = (TextView)findViewById(R.id.callback);

	    // Get a non-UI fragment to handle the service interface.
	    // If our activity gets destroyed and recreated, the fragment
	    // will still be around and we just need to re-fetch it.
	    if((clientFrag = (ClientFrag) getSupportFragmentManager()
	    		.findFragmentByTag("clientFrag")) == null) {
	    	updateStatus("Creating a clientFrag. No service yet.");
	        clientFrag = ClientFrag.getInstance();
	        getSupportFragmentManager().beginTransaction()
	            .add(clientFrag, "clientFrag")
	            .commit();
	    }
	    else {
	        updateStatus("Found existing clientFrag, will use it");
        }
	}
	
	public void doClick(View view) {
        switch(view.getId()) {
        case R.id.startBtn:
        	clientFrag.doBindService();
            break;
        case R.id.stopBtn:
        	clientFrag.doUnbindService();
            break;
        case R.id.simpleBtn:
        	clientFrag.doSendSimple();
        	break;
        case R.id.complexBtn:
        	clientFrag.doSendComplex();
        	break;
        }
	}

	@Override
	public void updateStatus(String status) {
		mCallbackText.setText(status);
	}
}
