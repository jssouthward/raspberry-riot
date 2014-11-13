package edu.virginia.rich.cs4720;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;


public class GamePanel extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_panel);
        
        //set an onClick for the red button to call onRedButtonClicked()
//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onRedButtonClicked();
//            }
//        });
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.menu_game_panel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onStartButtonClicked(View view) {
    	this.setContentView(R.layout.playing_game_view);
//        sendJson("http://" + ((EditText)findViewById(R.id.editText)).getText().toString() + "/rpi", createRedLightJSON());
    }

    public void onGreenButtonClicked(View view) {
        sendJson("http://" + ((EditText)findViewById(R.id.editText)).getText().toString() + "/rpi", createGreenLightJSON());
    }

    public void onBlueButtonClicked(View view) {
        sendJson("http://" + ((EditText)findViewById(R.id.editText)).getText().toString() + "/rpi", createBlueLightJSON());
    }


    protected void sendJson(final String url, final JSONObject lightCommand) {
        Thread t = new Thread() {

            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;

                try {
                    HttpPost post = new HttpPost(url);
                    StringEntity se = new StringEntity( lightCommand.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                    /*Checking response */
                    if(response!=null){
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                }

                Looper.loop(); //Loop in the message queue
            }
        };

        t.start();
    }

    public JSONObject createRedLightJSON() {
        return createLightJSON(1, 255, 0, 0, 0.5, true);
    }

    public JSONObject createGreenLightJSON() {
        return createLightJSON(1, 0, 0, 255, 0.5, true);
    }

    public JSONObject createBlueLightJSON() {
        return createLightJSON(1, 0, 255, 0, 0.5, true);
    }

    public JSONObject createLightJSON(int lightId, int red, int blue, int green, double intensity, boolean propagate) {
        JSONObject lightsJSON = new JSONObject();
        try {

            JSONArray lightsArray = new JSONArray();
            JSONObject redLight = new JSONObject();

            redLight.put("lightId", lightId);
            redLight.put("red", red);
            redLight.put("blue", blue);
            redLight.put("green", green);
            redLight.put("intensity", intensity);

            lightsArray.put(redLight);

            lightsJSON.put("lights", lightsArray);
            lightsJSON.put("propagate", propagate);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return lightsJSON;
    }
    
}
