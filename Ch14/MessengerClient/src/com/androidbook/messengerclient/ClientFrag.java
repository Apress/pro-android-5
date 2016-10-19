package com.androidbook.messengerclient;

import com.androidbook.messengerservice.MessengerService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;

// ClientFrag is a non-UI fragment that will hang around even
// during configuration changes. Therefore it makes a nice
// container for the service client which can remain in place
// for service responses even if the activity goes away and is
// re-created.
public class ClientFrag extends Fragment {
	private static final String TAG = "MessengerClientFrag";
	static private ClientFrag mClientFrag = null;
	// application context will be used to bind to the service because
	// fragments can't bind and activities can go away.
	private Context appContext = null;

	// Messenger for sending to service.
    Messenger mService = null;
    // Flag indicating whether we have called bind on the service.
    boolean mIsBound;

    // Instantiation method for the client fragment. We just want one
    // and we use setRetainInstance(true) so it hangs around during
    // configuration changes.
	public static ClientFrag getInstance() {
		if(mClientFrag == null) {
			mClientFrag = new ClientFrag();
			mClientFrag.setRetainInstance(true);
		}
		return mClientFrag;
	}
    
    // Handler for response messages from the service
	class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_SET_SIMPLE_VALUE:
                    updateStatus("Received from service: " + msg.arg1);
                    break;
                default:
                	break;
            }
            super.handleMessage(msg);
        }
    }
    
    // Need a Messenger to receive responses. Send this with the
	// Messages to the service.
	final Messenger mMessenger = new Messenger(new IncomingHandler());
    
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through a Messenger, so get a client-side
            // representation of that from the raw service object.
            mService = new Messenger(service);
            updateStatus("Attached.");

            // We want to monitor the service for as long as we are
            // connected to it. This is not strictly necessary. You
            // do not need to register with the service before using
            // it. But if this failed you'd have an early warning.
            try {
                Message msg = Message.obtain(null,
                        MessengerService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            	Log.e(TAG, "Could not establish a connection to the service: " + e);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            updateStatus("Disconnected.");
        }
    };

    public void doBindService() {
        // Establish a connection with the service. We use the String name
    	// of the service since it exists in a separate process and we do
    	// not want to require the service jar in the client. We also grab
    	// the application context and bind the service to that since the
    	// activity context could go away on a configuration change but the
    	// application context will always be there.
    	appContext = getActivity().getApplicationContext();
    	if(mIsBound = appContext.bindService(
    		new Intent("com.androidbook.messengerservice.MessengerService"),
        		    mConnection, Context.BIND_AUTO_CREATE)
        	) {
    		updateStatus("Bound to service.");
    	}
    	else {
    		updateStatus("Bind attempt failed.");   		
    	}
    }

    public void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with
            // it, then now is the time to unregister. Note that the
        	// replyTo value is only used by the service to unregister
        	// this client. No response message will come back to the client.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null,
                            MessengerService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service
                    // has crashed.
                }
            }
            
            // Detach our existing connection.
            appContext.unbindService(mConnection);
            mIsBound = false;
            updateStatus("Unbound.");
        }
    }

    // If you can simplify and send only one or two integers, this
    // is the easy way to do it.
	public void doSendSimple() {
        try {
            Message msg = Message.obtain(null,
                MessengerService.MSG_SET_SIMPLE_VALUE, this.hashCode(), 0);
            mService.send(msg);
            updateStatus("Sending simple message.");
        } catch (RemoteException e) {
            Log.e(TAG, "Could not send a simple message to the service: " + e);
        }
    }

    // If you have more complex data, throw it into a Bundle and
    // add it to the Message. Can also pass Parcelables if you like.
    public void doSendComplex() {
        try {
            Message msg = Message.obtain(null,
                MessengerService.MSG_SET_COMPLEX_VALUE);
            Bundle mBundle = new Bundle();
            mBundle.putString("stringArg", "This is a string to pass");
            mBundle.putDouble("myDouble", 1138L);
            mBundle.putInt("myInt", 42);
            msg.setData(mBundle);
            mService.send(msg);
            updateStatus("Sending complex message.");
        } catch (RemoteException e) {
            Log.e(TAG, "Could not send a complex message to the service: " + e);
        }
    }

    private void updateStatus(String status) {
		// Make sure the latest status is updated in the GUI, which
    	// is handled by the parent activity.
    	ISampleServiceClient uiContext = (ISampleServiceClient) getActivity();
    	if(uiContext != null) {
    		uiContext.updateStatus(status);
    	}
	}
}
