package edu.virginia.rich.cs4720;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;

public class PlayActivity extends Activity implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor light;
	private GameState state;
	String IP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playing_game_view);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		light = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		
        IP = this.getIntent().getStringExtra("IP").trim();
        
        state = new GameState(2, 32);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener((SensorEventListener) this, light,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener((SensorEventListener) this, light);
	}

	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do something here if sensor accuracy changes.
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		float lux = event.values[0];
		// Do something with this sensor data.

		// daytime
		if (lux >= .3) {
			findViewById(R.id.game_parent_layout).setBackgroundColor(
					Color.argb(255, 150, 150, 150));
			
			findViewById(R.id.p1plus1).setBackgroundColor(0xfff0f0f0);
			findViewById(R.id.p1plus2).setBackgroundColor(0xfff0f0f0);
			findViewById(R.id.p2plus1).setBackgroundColor(0xfff0f0f0);
			findViewById(R.id.p2plus2).setBackgroundColor(0xfff0f0f0);
			
			((Button) findViewById(R.id.p1plus1)).setTextColor(0xff000000);
			((Button) findViewById(R.id.p1plus2)).setTextColor(0xff000000);
			((Button) findViewById(R.id.p2plus1)).setTextColor(0xff000000);
			((Button) findViewById(R.id.p2plus2)).setTextColor(0xff000000);
		}

		// nighttime
		if (lux < .3) {
			findViewById(R.id.game_parent_layout).setBackgroundColor(0xff0f0f0f);
			findViewById(R.id.p1plus1).setBackgroundColor(0xff696969);
			findViewById(R.id.p1plus2).setBackgroundColor(0xff696969);
			findViewById(R.id.p2plus1).setBackgroundColor(0xff696969);
			findViewById(R.id.p2plus2).setBackgroundColor(0xff696969);
			
			((Button) findViewById(R.id.p1plus1)).setTextColor(0xfff0f0f0);
			((Button) findViewById(R.id.p1plus2)).setTextColor(0xfff0f0f0);
			((Button) findViewById(R.id.p2plus1)).setTextColor(0xfff0f0f0);
			((Button) findViewById(R.id.p2plus2)).setTextColor(0xfff0f0f0);
		}
	}
	
	public void onP1B1Click(View view) {
		JSONObject json = new JSONObject();
		json = this.createLightJSON(1, 255, 0, 0, 1, true, json);
		this.sendJson("http://" + IP + "/rpi", json);
		View p1p1 = findViewById(R.id.p1plus1);
		p1p1.setBackgroundColor(0xff000000 + (0x7fffffff - ((ColorDrawable) p1p1.getBackground()).getColor()));
		((ProgressBar) this.findViewById(R.id.progressBar1)).setProgress(
				(int)(((ProgressBar) (findViewById(R.id.progressBar1))).getProgress()+3.125+0.5));
	}

	public void onP1B2Click(View view) {
		JSONObject json = new JSONObject();
		json = this.createLightJSON(1, 0, 255, 0, 1, true, json);
		this.sendJson("http://" + IP + "/rpi", json);
		View p1p2 = findViewById(R.id.p1plus2);
		p1p2.setBackgroundColor(0xff000000 + (0x7fffffff - ((ColorDrawable) p1p2.getBackground()).getColor()));		
		((ProgressBar) this.findViewById(R.id.progressBar1)).setProgress(
				(int)(((ProgressBar) (findViewById(R.id.progressBar1))).getProgress()+(2*3.125)+0.5));		
	}
	
	public void onP2B1Click(View view) {
		JSONObject json = new JSONObject();
		json = this.createLightJSON(1, 0, 0, 255, 1, true, json);
		this.sendJson("http://" + IP + "/rpi", json);
		View p2p1 = findViewById(R.id.p2plus1);
		p2p1.setBackgroundColor(0xff000000 + (0x7fffffff - ((ColorDrawable) p2p1.getBackground()).getColor()));		
		((ProgressBar) this.findViewById(R.id.progressBar1)).setProgress(
				(int)(((ProgressBar) (findViewById(R.id.progressBar1))).getProgress()+3.125+0.5));		
	}
	
	public void onP2B2Click(View view) {
		JSONObject json = new JSONObject();
		json = this.createLightJSON(1, 255, 255, 255, 1, true, json);
		this.sendJson("http://" + IP + "/rpi", json);
		View p2p2 = findViewById(R.id.p2plus2);
		p2p2.setBackgroundColor(0xff000000 + (0x7fffffff - ((ColorDrawable) p2p2.getBackground()).getColor()));		
		((ProgressBar) this.findViewById(R.id.progressBar1)).setProgress(
				(int)(((ProgressBar) (findViewById(R.id.progressBar1))).getProgress()+(2*3.125)+0.5));
	}
	
	protected void sendJson(final String url, final JSONObject lightCommand) {
		Thread t = new Thread() {

			public void run() {
				Looper.prepare(); // For Preparing Message Pool for the child
									// Thread
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						10000); // Timeout Limit
				HttpResponse response;

				try {
					HttpPost post = new HttpPost(url);
					StringEntity se = new StringEntity(lightCommand.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json"));
					post.setEntity(se);
					response = client.execute(post);

					/* Checking response */
					if (response != null) {
						InputStream in = response.getEntity().getContent(); // Get
																			// the
																			// data
																			// in
																			// the
																			// entity
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				Looper.loop(); // Loop in the message queue
			}
		};

		t.start();
	}
	
	 public JSONObject createLightJSON(int lightId, int red, int green, int blue, double intensity, boolean propagate, JSONObject jObj) {;
		 try {
		 JSONArray lightsArray = new JSONArray();
		 JSONObject indvLight = new JSONObject();
		 indvLight.put("lightId", lightId);
		 indvLight.put("red", red);
		 indvLight.put("blue", blue);
		 indvLight.put("green", green);
		 indvLight.put("intensity", intensity);
		 lightsArray.put(indvLight);
		 jObj.put("lights", lightsArray);
		 jObj.put("propagate", propagate);
		 
		 } catch (JSONException e) {
			 e.printStackTrace();
		 }
		 
		 return jObj;
	 }
	

}