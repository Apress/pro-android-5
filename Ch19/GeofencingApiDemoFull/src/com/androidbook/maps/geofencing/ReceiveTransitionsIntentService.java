package com.androidbook.maps.geofencing;

import java.util.List;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

/*
 * The following borrows from the Google/Android documentation, but
 * fixes all of the errors and other issues.
 */
public class ReceiveTransitionsIntentService extends IntentService {

	private static final String TAG = "ReceiveTransitionsIntentService";
	private NotificationManager notificationMgr;

	public ReceiveTransitionsIntentService() {
		super(TAG);
    }
	
	public void onCreate() {
		super.onCreate();
        notificationMgr = (NotificationManager)getSystemService(
                NOTIFICATION_SERVICE);
	}

    @Override
    protected void onHandleIntent(Intent intent) {
    	GeofencingEvent gfEvent = GeofencingEvent.fromIntent(intent);
        // First check for errors
        if (gfEvent.hasError()) {
            // Get the error code with a static method
            int errorCode = gfEvent.getErrorCode();
            // Log the error
            Log.e(TAG, "Location Services error: " +
                    Integer.toString(errorCode));
        /*
         * If there's no error, get the transition type and the IDs
         * of the geofence or geofences that triggered the transition
         */
        } else {
            // Get the type of transition (entry or exit)
            int transitionType =
            		gfEvent.getGeofenceTransition();
            String tranTypeStr = "UNKNOWN(" + transitionType + ")";
            switch(transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
            	tranTypeStr = "ENTER";
            	break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
            	tranTypeStr = "EXIT";
            	break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
            	tranTypeStr = "DWELL";
            	break;
            }
            Log.v(TAG, "transitionType reported: " + tranTypeStr);
            Location triggerLoc = gfEvent.getTriggeringLocation();
            Log.v(TAG, "triggering location is " + triggerLoc);

            List <Geofence> triggerList =
               		gfEvent.getTriggeringGeofences();

            String[] triggerIds = new String[triggerList.size()];

            for (int i = 0; i < triggerIds.length; i++) {
                // Grab the Id of each geofence
                triggerIds[i] = triggerList.get(i).getRequestId();
                String msg = tranTypeStr + ": " + triggerLoc.getLatitude() +
                	", " + triggerLoc.getLongitude();
                String title = triggerIds[i];
                displayNotificationMessage(title, msg);
            }
        }
    }
    
    private void displayNotificationMessage(String title, String message)
    {
   	    int notif_id = (int) (System.currentTimeMillis() & 0xFFL);

        Notification notification = new NotificationCompat.Builder(this)
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(android.R.drawable.ic_menu_compass)
        .setOngoing(false)
        .build();

        notificationMgr.notify(notif_id, notification);
    }
}
