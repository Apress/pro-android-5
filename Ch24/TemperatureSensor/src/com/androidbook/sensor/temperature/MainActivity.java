package com.androidbook.sensor.temperature;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager mgr;
    private Sensor temp;
    private TextView text;
    private StringBuilder msg = new StringBuilder(2048);
    private float fahrenheit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        if (Build.VERSION.SDK_INT < 14) {
            temp = mgr.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        } else {
            temp = mgr.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }
        if(temp == null) {
        	Toast.makeText(this, "This device has no temperature sensor", Toast.LENGTH_LONG ).show();
        	finish();
        }
        text = (TextView) findViewById(R.id.text);
    }

    @Override
    protected void onResume() {
        mgr.registerListener(this, temp, SensorManager.SENSOR_DELAY_NORMAL);
    	super.onResume();
    }

    @Override
    protected void onPause() {
        mgr.unregisterListener(this, temp);
    	super.onPause();
    }

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// ignore
	}

	public void onSensorChanged(SensorEvent event) {
		fahrenheit = event.values[0] * 9 / 5 + 32;
		msg.insert(0, "Got a sensor event: " + event.values[0] + " Celsius (" +
				fahrenheit  + " F)\n");
		text.setText(msg);
		text.invalidate();
	}
}