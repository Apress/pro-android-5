package com.androidbook.messengerservice;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * This is an example of implementing an application service that uses the
 * {@link Messenger} class for communicating with clients.  This allows for
 * remote interaction with a service, without needing to define an AIDL
 * interface.
 * 
 * This sample is based on the Android Sample called API Demos but has been
 * modified for the Pro Android book.
 */

public class MessengerService extends Service {
    /** For showing and hiding our notification. */
    NotificationManager mNM;
    /** Keeps track of all current registered clients. */
    ArrayList<Messenger> mClients = new ArrayList<Messenger>();
    /** Holds last value set by a client. */
    int mValue = 0;
    
    /**
     * Command to the service to register a client, receiving callbacks
     * from the service.  The Message's replyTo field must be a Messenger of
     * the client where callbacks should be sent.
     */
    public static final int MSG_REGISTER_CLIENT = 1;
    
    /**
     * Command to the service to unregister a client, ot stop receiving callbacks
     * from the service.  The Message's replyTo field must be a Messenger of
     * the client as previously given with MSG_REGISTER_CLIENT.
     */
    public static final int MSG_UNREGISTER_CLIENT = 2;
    
    /**
     * Command to service to set a new simple value.  This can be sent to the
     * service to supply a new value.
     */
    public static final int MSG_SET_SIMPLE_VALUE = 3;
    
    /**
     * Command to service to set several values, not just one or two.
     */
    public static final int MSG_SET_COMPLEX_VALUE = 4;
    
	public static final String TAG = "MessengerService";
    
    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
			switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    Log.v(TAG, "Registering client");
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    Log.v(TAG, "Unregistering client");
                    break;
                case MSG_SET_SIMPLE_VALUE:
                    mValue = msg.arg1;
                    Log.v(TAG, "Receiving arg1: " + mValue);
                    showNotification("Received arg1: " + mValue);
                    for (int i=mClients.size()-1; i>=0; i--) {
                        try {
                            mClients.get(i).send(Message.obtain(null,
                                    MSG_SET_SIMPLE_VALUE, mValue, 0));
                        } catch (RemoteException e) {
                            // The client is dead.  Remove it from the list;
                            // we are going through the list from back to front
                            // so this is safe to do inside the loop.
                            mClients.remove(i);
                        }
                    }
                    break;
                case MSG_SET_COMPLEX_VALUE:
                	Bundle mBundle = msg.getData();
                    Log.v(TAG, "Receiving bundle: ");
                	if(mBundle != null) {
                        showNotification("Got complex msg: myDouble = "
                                + mBundle.getDouble("myDouble"));
                        for(String key : mBundle.keySet()) {
                            Log.v(TAG, "    " + key);
                        }
                	}
                	else {
                        Log.v(TAG, "    no bundle passed");
                	}
                    break;
                default:
                    Log.v(TAG, "Got some other message: " + msg.what);
                    super.handleMessage(msg);
            }
        }
    }
    
    // Target we publish for clients to send messages to IncomingHandler.
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    
    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.
        Log.v(TAG, "Service is starting");
        showNotification(getText(R.string.remote_service_started));
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(R.string.remote_service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    /**
     * Show a notification while this service is running. Note that
     * we don't include an intent since we're just a service here. The
     * service stops when the client tells it to.
     */
    private void showNotification(CharSequence text) {
        Notification notification = new NotificationCompat.Builder(this)
            .setContentTitle("MessengerService")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setTicker(text)
            .setOngoing(true)
            .build();

        mNM.notify(R.string.remote_service_started, notification);
    }

}