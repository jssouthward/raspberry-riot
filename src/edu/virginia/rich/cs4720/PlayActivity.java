package edu.virginia.rich.cs4720;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class PlayActivity extends Activity implements SensorEventListener {
	
    private SensorManager mSensorManager;
    private Sensor light;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playing_game_view);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}
	
	@Override
	protected void onResume() 
	 {
		super.onResume();
		mSensorManager.registerListener((SensorEventListener)this, light, SensorManager.SENSOR_DELAY_NORMAL);
	 }
	
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener((SensorEventListener)this, light);
    }

	
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        // Do something with this sensor data.

        //daytime
        if(lux > .5) {
            findViewById(R.id.game_parent_layout).setBackgroundColor(Color.argb(255, 150, 150, 150));
        }

        //nighttime
        if(lux < .5) {
            findViewById(R.id.game_parent_layout).setBackgroundColor(Color.argb(255, 98, 98, 98));        	
        }
    }
    
}
