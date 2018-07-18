/*
 * Copyright 2016, Google Inc.
 * Copyright 2014, Nest Labs Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nestlabs.sdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

import com.nestlabs.sdk.ExceptionHandler;
import com.nestlabs.sdk.NestException;
import com.nestlabs.sdk.NestListener;
import com.nestlabs.sdk.WwnClient;
import com.nestlabs.sdk.models.Camera;
import com.nestlabs.sdk.models.GlobalUpdate;
import com.nestlabs.sdk.models.NestToken;
import com.nestlabs.sdk.models.Structure;
import com.nestlabs.sdk.models.Thermostat;
import com.nestlabs.sdk.rest.parsers.ErrorMessage;

public class camera_detail extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = camera_detail.class.getSimpleName();

    private static final String THERMOSTAT_KEY_MAIN = "thermostat_key_main";
    private static final String THERMOSTAT_KEY_KITCHEN= "thermostat_key_kitchen";
    private static final String CAMERA_KEY_INDOOR= "camera_key_indoor";
    private static final String CAMERA_KEY_OUTDOOR= "camera_key_outdoor";
    private static final String CAMERA_KEY_HELLO= "camera_key_hello";
    private static final String STRUCTURE_KEY = "structure_key";
    private static final String KEY_AWAY = "away";
    private static final String KEY_AUTO_AWAY = "auto-away";
    private static final String KEY_HOME = "home";
    private static final String KEY_HEAT = "heat";
    private static final String KEY_COOL = "cool";
    private static final String KEY_HEAT_COOL = "heat-cool";
    private static final String KEY_OFF = "off";
    private static final String DEG_F = "%d°";

    private static final String HUMID = "Humid: %d%%";
    private static final String TEMP = "Temp: %.1f℃";

    private static final String CLIENT_ID = Constants.CLIENT_ID;
    private static final String CLIENT_SECRET = Constants.CLIENT_SECRET;
    private static final String REDIRECT_URL = Constants.REDIRECT_URL;
    private static final int AUTH_TOKEN_REQUEST_CODE = 123;

    //textview
    private TextView thermostatGotoText;
    private ImageView thermostatGotoView;

    private ImageView ComfortLeftView;

    private TextView thermostatValue_1;
    private TextView thermostatValue_2;
    private TextView thermostatValue_3;
    private TextView thermostatValue_4;
    private TextView thermostatValue_5;

    private TextView thermostatTempValue;
    private TextView thermostatHumidValue;
    private Button switchBetweenThermostats;

    private ImageView thermostatHeatView;
    private ImageView thermostatCoolView;
    private ImageView thermostatOffView;

    private TextView thermostatHeatText;
    private TextView thermostatCoolText;
    private TextView thermostatOffText;

    //nest
    private WwnClient wwnClient;
    private NestToken mToken;
    private Camera indoor_camera;
    private Camera outdoor_camera;
    private Camera hello_camera;
    private Thermostat mThermostat_main;
    private Thermostat mThermostat_kitchen;
    private Structure mStructure;
    private Activity mActivity;

    private boolean thermostat_number = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.nestlabs.sdk.sample.R.layout.camera_layout);
        mActivity = this;

        ComfortLeftView = (ImageView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_lefticon);
        //register
        thermostatGotoText = (TextView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_to_thermostat_title);
        thermostatGotoView = (ImageView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_to_thermostat_icon);

        thermostatValue_1 = (TextView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_1_level);
        thermostatValue_2 = (TextView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_2_level);
        thermostatValue_3 = (TextView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_3_level);
        thermostatValue_4 = (TextView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_4_level);
        thermostatValue_5 = (TextView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_5_level);

        thermostatTempValue = (TextView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_temperature_content);
        thermostatHumidValue = (TextView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_humidity_content);
        switchBetweenThermostats = (Button) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_thermostat_switch);

        thermostatHeatView = (ImageView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_heat_button);
        thermostatCoolView = (ImageView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_cool_button);
        thermostatOffView = (ImageView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_off_button);

        thermostatHeatText = (TextView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_heat_text);
        thermostatCoolText = (TextView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_cool_text);
        thermostatOffText = (TextView) findViewById(com.nestlabs.sdk.sample.R.id.Comfort_off_text);

        thermostatGotoText.setOnClickListener(this);
        thermostatGotoView.setOnClickListener(this);
        switchBetweenThermostats.setOnClickListener(this);
        thermostatHeatView.setOnClickListener(this);
        thermostatCoolView.setOnClickListener(this);
        thermostatOffView.setOnClickListener(this);

        // Load auth token if exists.
        mToken = Settings.loadAuthToken(this);
        wwnClient = new WwnClient(errorHandler);

        // Start streaming if auth token exists or launch authentication otherwise.
        if (mToken != null) {
            startWithListeners(mToken);
        } else {
            wwnClient.oauth2.setConfig(CLIENT_ID, CLIENT_SECRET, REDIRECT_URL);
            wwnClient.oauth2.launchAuthFlow(this, AUTH_TOKEN_REQUEST_CODE);
        }

        // If saved state exists then populate thermostat and structure details.
        if (savedInstanceState != null) {
            indoor_camera = savedInstanceState.getParcelable(CAMERA_KEY_INDOOR);
            outdoor_camera = savedInstanceState.getParcelable(CAMERA_KEY_OUTDOOR);
            hello_camera = savedInstanceState.getParcelable(CAMERA_KEY_HELLO);
            mStructure = savedInstanceState.getParcelable(STRUCTURE_KEY);
            updateViews();
        }

    }

    /**
     * Adds listeners to receive updates when thermostat(s) and structure(s) change their state, then starts streaming.
     */
    private void startWithListeners(NestToken token) {

        // Listen for and handle updates from thermostat(s) and structure.
        wwnClient.addListener(new NestListener.GlobalListener() {
            @Override
            public void onUpdate(@NonNull GlobalUpdate update) {
                indoor_camera = update.getCameras().get(0);
                outdoor_camera = update.getCameras().get(1);
                hello_camera = update.getCameras().get(2);
                mThermostat_main = update.getThermostats().get(0);//living room
                mThermostat_kitchen = update.getThermostats().get(1);
                mStructure = update.getStructures().get(0);

                thermostat_number = true;//set the default to maindoor

                updateViews();
            }
        });

        // Add listener to handle auth. events.
        wwnClient.addListener(new NestListener.AuthListener() {

            @Override
            public void onAuthFailure(NestException exception) {
                Log.e(TAG, "Authentication failed with error: " + exception.getMessage());
                Settings.saveAuthToken(mActivity, null);
                wwnClient.oauth2.launchAuthFlow(mActivity, AUTH_TOKEN_REQUEST_CODE);
            }

            @Override
            public void onAuthRevoked() {
                Log.e(TAG, "Auth token was revoked!");
                Settings.saveAuthToken(mActivity, null);
                wwnClient.oauth2.launchAuthFlow(mActivity, AUTH_TOKEN_REQUEST_CODE);
            }
        });

        // Add listener to handle error events.
        wwnClient.addListener(new NestListener.ErrorListener() {
            @Override
            public void onError(ErrorMessage errorMessage) {
                Log.e(TAG, "Server error: " + errorMessage.getMessage());
            }
        });

        // Start streaming
        wwnClient.startWithToken(mToken);
    }

    private ExceptionHandler errorHandler = new ExceptionHandler() {
        @Override
        public void handle(NestException ex) {
            Log.e(TAG, "WwnClient error: " + ex.toString());
        }
    };

    /**
     * Saves instance state including thermostat and structure.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelable(THERMOSTAT_KEY_MAIN, mThermostat_main);
        outState.putParcelable(CAMERA_KEY_INDOOR, indoor_camera);
        outState.putParcelable(CAMERA_KEY_OUTDOOR, outdoor_camera);
        outState.putParcelable(CAMERA_KEY_HELLO, hello_camera);
        outState.putParcelable(THERMOSTAT_KEY_KITCHEN, mThermostat_kitchen);
        outState.putParcelable(STRUCTURE_KEY, mStructure);
    }

    /**
     * Listens for auth activity result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK || requestCode != AUTH_TOKEN_REQUEST_CODE) {
            Log.e(TAG, "Finished with no result.");
            return;
        }

        mToken = wwnClient.oauth2.getAccessTokenFromIntent(intent);
        if (mToken != null) {
            Settings.saveAuthToken(this, mToken);
            startWithListeners(mToken);
        } else {
            Log.e(TAG, "Unable to resolve access token from payload.");
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        wwnClient.removeAllListeners();
    }

    /**
     * Identifies setting to update from click event, and updates the thermostat control using the Nest API.
     * NOTE: The Works with Nest product configuration must have write access permission
     *       in order to update the selected thermostat, and away permission for setting away status.
     * @param v the view component
     */
    @Override
    public void onClick(View v) {

        if (mThermostat_kitchen == null || mThermostat_main == null || mStructure == null) {
            return;
        }

        Thermostat mThermostat;//determine current thermostat number
        if (thermostat_number)
            mThermostat = mThermostat_main;
        else
            mThermostat = mThermostat_kitchen;

        String thermostatID = mThermostat.getDeviceId();
        String mode = mThermostat.getHvacMode();

        switch (v.getId()) {
//            case R.id.Comfort_to_thermostat_icon:
//            case R.id.Comfort_to_thermostat_title:
//                Intent intent_thermostat=new Intent(Comfort.this,thermostat_detail.class);
//                startActivity(intent_thermostat);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                break;

            case R.id.Comfort_lefticon:
                Intent intent_dashboard=new Intent(camera_detail.this,DashBoard.class);
                startActivity(intent_dashboard);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;


            case R.id.Comfort_thermostat_switch:
                if(thermostat_number)
                    thermostat_number = false;
                else
                    thermostat_number = true;
                break;

            case R.id.Comfort_heat_button:

                String state_heat = mStructure.getAway();
                boolean isAway = state_heat.equals(KEY_AWAY) || state_heat.equals(KEY_AUTO_AWAY);

                if(isAway == false && mode != KEY_HEAT) {
                    //doing something
                    //Comfort_heat_button.
                    //Comfort_heat_text.
                    wwnClient.thermostats.setHVACMode(thermostatID, KEY_HEAT);
                    break;}

            case com.nestlabs.sdk.sample.R.id.Comfort_cool_button:

                String state_cool = mStructure.getAway();
                boolean isAway_cool = state_cool.equals(KEY_AWAY) || state_cool.equals(KEY_AUTO_AWAY);

                if(isAway_cool == false && mode != KEY_COOL) {
                    //doing something
                    //Comfort_cool_button.
                    //Comfort_cool_text.
                    wwnClient.thermostats.setHVACMode(thermostatID, KEY_COOL);
                    break;}

            case com.nestlabs.sdk.sample.R.id.Comfort_off_button:

                if(mode != KEY_OFF) {
                    //doing something
                    //Comfort_off_button.
                    //Comfort_off_text.
                    wwnClient.thermostats.setHVACMode(thermostatID, KEY_OFF);
                }
                //turn off
                break;

        }

    }

    /**
     * Updates thermostat control temp range from UI events.
     * NOTE: The Works with Nest product configuration must have write access permission
     *       in order to update the selected thermostat.
     * @param v the view component
     */
    private void updateTHRange() {

        Thermostat mThermostat;//determine current thermostat number
        if (thermostat_number)
            mThermostat = mThermostat_main;
        else
            mThermostat = mThermostat_kitchen;

        Double temp_update = mThermostat.getTargetTemperatureC();
        long humid_update = mThermostat.getHumidity();

        thermostatTempValue.setText(String.format(TEMP, temp_update));
        thermostatHumidValue.setText(String.format(HUMID,humid_update));

    }

    private void updateValueRange() {

        //calculation towards comfort value then set specific movement

    }

    /**
     * Updates views to show current state from thermostat and structures.
     */
    private void updateViews() {

        if (mThermostat_kitchen == null || mThermostat_main == null || hello_camera == null || indoor_camera == null || outdoor_camera == null || mStructure == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateTHRange();
                updateValueRange();
            }
        });
    }
}
