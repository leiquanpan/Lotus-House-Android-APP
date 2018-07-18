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
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nestlabs.sdk.ExceptionHandler;
import com.nestlabs.sdk.NestException;
import com.nestlabs.sdk.NestListener;
import com.nestlabs.sdk.WwnClient;
import com.nestlabs.sdk.models.Camera;
import com.nestlabs.sdk.models.GlobalUpdate;
import com.nestlabs.sdk.models.NestToken;
import com.nestlabs.sdk.models.SmokeCOAlarm;
import com.nestlabs.sdk.models.Structure;
import com.nestlabs.sdk.models.Thermostat;
import com.nestlabs.sdk.rest.parsers.ErrorMessage;

public class thermostat_detail extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = thermostat_detail.class.getSimpleName();

    private static final String THERMOSTAT_KEY_MAIN = "thermostat_key_main";
    private static final String THERMOSTAT_KEY_KITCHEN= "thermostat_key_kitchen";
    private static final String STRUCTURE_KEY = "structure_key";
    private static final String SMOKE_KEY = "smoke";
    private static final String KEY_HEAT = "heat";
    private static final String KEY_COOL = "cool";
    private static final String KEY_OFF = "off";


    private static final String CLIENT_ID = Constants.CLIENT_ID;
    private static final String CLIENT_SECRET = Constants.CLIENT_SECRET;
    private static final String REDIRECT_URL = Constants.REDIRECT_URL;
    private static final int AUTH_TOKEN_REQUEST_CODE = 123;

    //textview
    private ImageView ThermostatPosView;
    private ImageView Thermostatchangeview;
    private ImageView ThermostatLeftView;

    private TextView ThermostatTempValue;
    private TextView ThermostatTempTypeC;
    private TextView ThermostatTempTypeF;
    private TextView ThermostatHumidValue;
    private TextView ThermostatCOStatus;
    private TextView ThermostatPMStatus;

    //nest
    private WwnClient wwnClient = Constants.wwnClient;
    private NestToken mToken = Constants.mToken;
    private Thermostat mThermostat_main = Constants.mThermostat_main;
    private Thermostat mThermostat_kitchen = Constants.mThermostat_kitchen;
    private Structure mStructure = Constants.mStructure;
    private SmokeCOAlarm smokeAlarm = Constants.smokeAlarm;
    private Activity mActivity;

    //true represents kitchen, otherwise livingroom
    private boolean current_thermostat = true;

    //true represents C, otherwise F
    private boolean current_Temp_type = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light_layout);
        mActivity = this;

        Thermostatchangeview = (ImageView) findViewById(R.id.thermostat_view);
        ThermostatPosView = (ImageView) findViewById(R.id.thermostat_pos_view);
        ThermostatLeftView = (ImageView) findViewById(R.id.thermostat_lefticon);

        ThermostatTempValue = (TextView) findViewById(R.id.thermostat_temperature_content);
        ThermostatTempTypeC = (TextView) findViewById(R.id.thermostat_option_c);
        ThermostatTempTypeF = (TextView) findViewById(R.id.thermostat_option_c4);
        ThermostatHumidValue = (TextView) findViewById(R.id.thermostat_humidity_content);
        ThermostatCOStatus = (TextView) findViewById(R.id.thermostat_CO_content);
        ThermostatPMStatus = (TextView) findViewById(R.id.thermostat_PM_content2);

        ThermostatPosView.setOnClickListener(this);
        Thermostatchangeview.setOnClickListener(this);
        ThermostatLeftView.setOnClickListener(this);
        ThermostatTempTypeC.setOnClickListener(this);
        ThermostatTempTypeF.setOnClickListener(this);

        // Load auth token if exists.
        if(wwnClient == null)
        {
            mToken = Settings.loadAuthToken(this);
            wwnClient = new WwnClient(errorHandler);

            // Start streaming if auth token exists or launch authentication otherwise.
            if (mToken != null) {
                startWithListeners(mToken);
            } else {
                wwnClient.oauth2.setConfig(CLIENT_ID, CLIENT_SECRET, REDIRECT_URL);
                wwnClient.oauth2.launchAuthFlow(this, AUTH_TOKEN_REQUEST_CODE);
            }

            Constants.mToken = mToken;
            Constants.wwnClient = wwnClient;

            // If saved state exists then populate thermostat and structure details.
            if (savedInstanceState != null) {
                mThermostat_main = savedInstanceState.getParcelable(THERMOSTAT_KEY_MAIN);
                mThermostat_kitchen = savedInstanceState.getParcelable(THERMOSTAT_KEY_KITCHEN);
                mStructure = savedInstanceState.getParcelable(STRUCTURE_KEY);
                smokeAlarm = savedInstanceState.getParcelable(SMOKE_KEY);

                updateViews();
            }
        }
        else {
            startWithListeners(mToken);
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
                mThermostat_main = update.getThermostats().get(0);//living room
                mThermostat_kitchen = update.getThermostats().get(1);
                mStructure = update.getStructures().get(0);
                smokeAlarm = update.getSmokeCOAlarms().get(0);

                Constants.mThermostat_kitchen = mThermostat_kitchen;
                Constants.mThermostat_main = mThermostat_main;
                Constants.mStructure = mStructure;
                Constants.smokeAlarm = smokeAlarm;

                current_thermostat = true;//set the default to kitchen
                current_Temp_type = true;//set the default to C

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
        outState.putParcelable(THERMOSTAT_KEY_KITCHEN, mThermostat_kitchen);
        outState.putParcelable(STRUCTURE_KEY, mStructure);
        outState.putParcelable(SMOKE_KEY, smokeAlarm);

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

        if (mThermostat_kitchen == null || mThermostat_main == null || mStructure == null || smokeAlarm == null) {
//            Toast.makeText(getApplicationContext(), "Cofiguration failed", Toast.LENGTH_SHORT).show();
            return;
        }

        Thermostat mThermostat = current_thermostat? mThermostat_kitchen:mThermostat_main;

        switch (v.getId()) {

            case R.id.thermostat_lefticon:
                Intent intent_comfort=new Intent(thermostat_detail.this,Comfort.class);
                startActivity(intent_comfort);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;


            case R.id.thermostat_option_c:
                //change to C
                if(current_Temp_type == false){
                    current_Temp_type = true;
//                    ThermostatTempTypeC.setBreakStrategy();
                    ThermostatTempValue.setText(String.valueOf(mThermostat.getAmbientTemperatureC()));

                    ThermostatTempTypeC.setTypeface(Typeface.DEFAULT_BOLD);
                    ThermostatTempTypeC.setTextSize(44);

                    ThermostatTempTypeF.setTypeface(Typeface.DEFAULT);
                    ThermostatTempTypeF.setTextSize(36);
                }
                break;

            case R.id.thermostat_option_c4:
                //change to F
                if(current_Temp_type == true){
                    current_Temp_type = false;
//                    ThermostatTempTypeF.setBreakStrategy();
                    ThermostatTempValue.setText(String.valueOf(mThermostat.getAmbientTemperatureF()));
                    ThermostatTempTypeF.setTypeface(Typeface.DEFAULT_BOLD);
                    ThermostatTempTypeF.setTextSize(44);

                    ThermostatTempTypeC.setTypeface(Typeface.DEFAULT);
                    ThermostatTempTypeC.setTextSize(36);
                }
                break;

            case R.id.thermostat_pos_view:
                //change the thermostat
                if(current_thermostat == true){
                    current_thermostat = false;
                    updateBothView();
                    updateBothValue();
                }
                else {
                    current_thermostat = true;
                    updateBothView();
                    updateBothValue();
                }
                break;
        }

    }

    /**
     * Update the Image part
     */
    private void updateBothView() {

        ThermostatPosView.setImageResource(current_thermostat == true? R.drawable.off_thermostat_drawable : R.drawable.off_thermostat_off);


        Thermostat cur_thermo = current_thermostat == true? mThermostat_kitchen : mThermostat_main;
        if(cur_thermo.getHvacMode().equals(KEY_OFF)) {
            Thermostatchangeview.setImageResource(R.drawable.thermostat_off);
        }
        else {
            Thermostatchangeview.setImageResource(R.drawable.thermostat_on);
        }

    }

    /**
     * Update the value part
     */
    private void updateBothValue() {

        Thermostat mthermostat = current_thermostat? mThermostat_kitchen:mThermostat_main;

        //display Value, and find right way to display them
        if(current_Temp_type){
            ThermostatTempValue.setText(String.valueOf(mthermostat.getAmbientTemperatureC()));
            ThermostatTempTypeC.setTypeface(Typeface.DEFAULT_BOLD);
            ThermostatTempTypeC.setTextSize(44);

            ThermostatTempTypeF.setTypeface(Typeface.DEFAULT);
            ThermostatTempTypeF.setTextSize(36);
        }
        else{
            ThermostatTempValue.setText(String.valueOf(mthermostat.getAmbientTemperatureF()));
            ThermostatTempTypeF.setTypeface(Typeface.DEFAULT_BOLD);
            ThermostatTempTypeF.setTextSize(44);

            ThermostatTempTypeC.setTypeface(Typeface.DEFAULT);
            ThermostatTempTypeC.setTextSize(36);
        }

        ThermostatHumidValue.setText(String.valueOf(mthermostat.getHumidity()));
        ThermostatCOStatus.setText(smokeAlarm.getCOAlarmState().toUpperCase());
        ThermostatPMStatus.setText(smokeAlarm.getSmokeAlarmState().toUpperCase());
    }


    private void updateViews() {

        if (mThermostat_kitchen == null || mThermostat_main == null || mStructure == null || smokeAlarm == null) {
//            Toast.makeText(getApplicationContext(), "Cofiguration failed", Toast.LENGTH_SHORT).show();
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateBothView();
                updateBothValue();
            }
        });
    }
}
