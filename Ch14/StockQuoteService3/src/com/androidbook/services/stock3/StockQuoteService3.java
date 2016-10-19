package com.androidbook.services.stock3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;

public class StockQuoteService3 extends Service
{
    private NotificationManager notificationMgr;

    public class StockQuoteServiceImpl extends IStockQuoteService.Stub
    {
        public String getQuote(String ticker, Person requester)
                throws RemoteException {
            return "Hello " + requester.getName() +
                "! Quote for " + ticker + " is 20.0";
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationMgr =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        displayNotificationMessage("onCreate() called in StockQuoteService3");
    }

    @Override
    public void onDestroy()
    {
        displayNotificationMessage("onDestroy() called in StockQuoteService3");
        // Clear all notifications from this service
        notificationMgr.cancelAll();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        displayNotificationMessage("onBind() called in StockQuoteService3");
        return new StockQuoteServiceImpl();
    }

    private void displayNotificationMessage(String message)
    {
        PendingIntent contentIntent = 
                PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(this)
            .setContentTitle("StockQuoteService3")
            .setContentText(message)
            .setSmallIcon(R.drawable.emo_im_happy)
            .setTicker(message)
            // .setLargeIcon(aBitmap)
            .setContentIntent(contentIntent)
            .setOngoing(true)
            .build();

        notificationMgr.notify(R.id.app_notification_id, notification);
    }
}
