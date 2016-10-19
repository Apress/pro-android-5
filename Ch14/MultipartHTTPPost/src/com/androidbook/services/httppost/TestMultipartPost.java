package com.androidbook.services.httppost;

import java.io.File;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class TestMultipartPost extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            Ion.with(this, "http://www.androidbook.com/akc/update/PublicUploadTest")
            .setMultipartParameter("field1", "This is field number 1")
            .setMultipartParameter("field2", "Field 2 is shorter")
            .setMultipartFile("datafile",
                    new File(Environment.getExternalStorageDirectory()+"/testfile.txt"))
            .asString()
            .setCallback(new FutureCallback<String>() {
            	   @Override
            	    public void onCompleted(Exception e, String result) {
                       System.out.println(result);
            	    }});

        } catch(Exception e) {
        	// Do something about exceptions
        	System.out.println("Got exception: " + e);
        }
    }
}