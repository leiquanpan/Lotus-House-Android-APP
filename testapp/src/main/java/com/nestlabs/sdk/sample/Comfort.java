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

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.MenuItem;
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
import com.nestlabs.sdk.models.GlobalUpdate;
import com.nestlabs.sdk.models.NestToken;
import com.nestlabs.sdk.models.SmokeCOAlarm;
import com.nestlabs.sdk.models.Structure;
import com.nestlabs.sdk.models.Thermostat;
import com.nestlabs.sdk.rest.parsers.ErrorMessage;


public class Comfort extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = Comfort.class.getSimpleName();

    private static final String THERMOSTAT_KEY_MAIN = "thermostat_key_main";
    private static final String THERMOSTAT_KEY_KITCHEN= "thermostat_key_kitchen";
    private static final String STRUCTURE_KEY = "structure_key";
    private static final String KEY_AWAY = "away";
    private static final String KEY_AUTO_AWAY = "auto-away";
    private static final String KEY_HEAT = "heat";
    private static final String KEY_COOL = "cool";
    private static final String KEY_OFF = "off";
    private static final String DEG_F = "%d°";

    private static final String HUMID = "%d%%";
    private static final String TEMP = "%.1f℃";
    private static final String SMOKE_KEY = "smoke";

    private static final String CLIENT_ID = Constants.CLIENT_ID;
    private static final String CLIENT_SECRET = Constants.CLIENT_SECRET;
    private static final String REDIRECT_URL = Constants.REDIRECT_URL;
    private static final int AUTH_TOKEN_REQUEST_CODE = 123;

    //textview
    private TextView thermostatGotoText;
    private ImageView thermostatGotoView;
    private ImageView ComfortLeftView;
    private TextView[] thermostatValue = new TextView[5];

    private TextView thermostatTempValue;
    private TextView thermostatHumidValue;
    private Button switchBetweenThermostats;

    private ImageView thermostatHeatView;
    private ImageView thermostatCoolView;
    private ImageView thermostatOffView;

    private TextView thermostatHeatText;
    private TextView thermostatCoolText;
    private TextView thermostatOffText;
    private TextView currentThermostatMode;

    //nest
    private WwnClient wwnClient = Constants.wwnClient;
    private NestToken mToken = Constants.mToken;
    private Thermostat mThermostat_main = Constants.mThermostat_main;
    private Thermostat mThermostat_kitchen = Constants.mThermostat_kitchen;
    private Structure mStructure = Constants.mStructure;
    private SmokeCOAlarm smokeAlarm = Constants.smokeAlarm;
    private Activity mActivity;

    private boolean current_thermostat = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thermostat);
        mActivity = this;

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        //register
        ComfortLeftView = (ImageView) findViewById(R.id.Comfort_lefticon);
        thermostatGotoText = (TextView) findViewById(R.id.Comfort_to_thermostat_title);
        thermostatGotoView = (ImageView) findViewById(R.id.Comfort_to_thermostat_icon);

        thermostatValue[0] = (TextView) findViewById(R.id.Comfort_1_level);
        thermostatValue[1] = (TextView) findViewById(R.id.Comfort_2_level);
        thermostatValue[2] = (TextView) findViewById(R.id.Comfort_3_level);
        thermostatValue[3] = (TextView) findViewById(R.id.Comfort_4_level);
        thermostatValue[4] = (TextView) findViewById(R.id.Comfort_5_level);

        thermostatTempValue = (TextView) findViewById(R.id.Comfort_temperature_content);
        thermostatHumidValue = (TextView) findViewById(R.id.Comfort_humidity_content);
        switchBetweenThermostats = (Button) findViewById(R.id.Comfort_thermostat_switch);

        thermostatHeatView = (ImageView) findViewById(R.id.Comfort_heat_button);
        thermostatCoolView = (ImageView) findViewById(R.id.Comfort_cool_button);
        thermostatOffView = (ImageView) findViewById(R.id.Comfort_off_button);

        thermostatHeatText = (TextView) findViewById(R.id.Comfort_heat_text);
        thermostatCoolText = (TextView) findViewById(R.id.Comfort_cool_text);
        thermostatOffText = (TextView) findViewById(R.id.Comfort_off_text);
        currentThermostatMode = (TextView) findViewById(R.id.Comfort_cur_mode);

        thermostatGotoText.setOnClickListener(this);
        thermostatGotoView.setOnClickListener(this);
        switchBetweenThermostats.setOnClickListener(this);
        thermostatHeatView.setOnClickListener(this);
        thermostatCoolView.setOnClickListener(this);
        thermostatOffView.setOnClickListener(this);
        ComfortLeftView.setOnClickListener(this);


        // Load auth token if exists.
        if(wwnClient == null)
        {
            currentThermostatMode.setText("Entered again");
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

                current_thermostat = true;//set the default to maindoor

                Constants.mThermostat_kitchen = mThermostat_kitchen;
                Constants.mThermostat_main = mThermostat_main;
                Constants.mStructure = mStructure;
                Constants.smokeAlarm = smokeAlarm;

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
//        wwnClient.removeAllListeners();
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
            Toast.makeText(getApplicationContext(), "Cofiguration failed", Toast.LENGTH_SHORT).show();
            return;
        }

        Thermostat mThermostat = current_thermostat? mThermostat_kitchen:mThermostat_main;

        String thermostatID = mThermostat.getDeviceId();
        String mode = mThermostat.getHvacMode();

        switch (v.getId()) {
            case R.id.Comfort_to_thermostat_icon:
            case R.id.Comfort_to_thermostat_title:
                Intent intent_thermostat=new Intent(Comfort.this,thermostat_detail.class);
                startActivity(intent_thermostat);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;


            case R.id.Comfort_lefticon:
                Intent intent_dashboard=new Intent(Comfort.this,DashBoard.class);
                startActivity(intent_dashboard);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Comfort_thermostat_switch:
                if(current_thermostat)
                {
                    current_thermostat = false;
                }
                else{
                    current_thermostat = true;
                }

                updateTHRange();
                break;

            case R.id.Comfort_heat_button:

                String state_heat = mStructure.getAway();
                boolean isAway = state_heat.equals(KEY_AWAY) || state_heat.equals(KEY_AUTO_AWAY);

                if(isAway == false && mode != KEY_HEAT) {
                    thermostatHeatView.setImageResource(R.drawable.hvac_heat);
                    thermostatHeatText.setTypeface(Typeface.DEFAULT_BOLD);

                    thermostatCoolView.setImageResource(R.drawable.hvac_cool_off);
                    thermostatCoolText.setTypeface(Typeface.DEFAULT);

                    thermostatOffView.setImageResource(R.drawable.off_thermostat_drawable);
                    thermostatOffText.setTypeface(Typeface.DEFAULT);
                    wwnClient.thermostats.setHVACMode(thermostatID, KEY_HEAT);
                    }
                    break;

            case R.id.Comfort_cool_button:

                String state_cool = mStructure.getAway();
                boolean isAway_cool = state_cool.equals(KEY_AWAY) || state_cool.equals(KEY_AUTO_AWAY);

                if(isAway_cool == false && mode != KEY_COOL) {
                    thermostatHeatView.setImageResource(R.drawable.hvac_heat_off);
                    thermostatHeatText.setTypeface(Typeface.DEFAULT);

                    thermostatCoolView.setImageResource(R.drawable.hvac_cool);
                    thermostatCoolText.setTypeface(Typeface.DEFAULT_BOLD);

                    thermostatOffView.setImageResource(R.drawable.off_thermostat_drawable);
                    thermostatOffText.setTypeface(Typeface.DEFAULT);
                    wwnClient.thermostats.setHVACMode(thermostatID, KEY_COOL);
                    }
                    break;

            case R.id.Comfort_off_button:

                if(mode != KEY_OFF) {
                    thermostatHeatView.setImageResource(R.drawable.hvac_heat_off);
                    thermostatHeatText.setTypeface(Typeface.DEFAULT);

                    thermostatCoolView.setImageResource(R.drawable.hvac_cool_off);
                    thermostatCoolText.setTypeface(Typeface.DEFAULT);

                    thermostatOffView.setImageResource(R.drawable.off_thermostat_off);
                    thermostatOffText.setTypeface(Typeface.DEFAULT);
                    wwnClient.thermostats.setHVACMode(thermostatID, KEY_OFF);
                }
                break;

        }

    }

    /**
     * Update the TH value part
     */
    private void updateTHRange() {

        Thermostat mThermostat = current_thermostat? mThermostat_kitchen:mThermostat_main;

        Double temp_update = mThermostat.getAmbientTemperatureC();
        long humid_update = mThermostat.getHumidity();
        String mode = mThermostat.getHvacMode();


        thermostatTempValue.setText(String.format(TEMP,temp_update));
        thermostatHumidValue.setText(String.format(HUMID,humid_update));
        currentThermostatMode.setText(mode);

        if(mode.equals(KEY_HEAT)) {
            thermostatHeatView.setImageResource(R.drawable.hvac_heat);
            thermostatHeatText.setTypeface(Typeface.DEFAULT_BOLD);

            thermostatCoolView.setImageResource(R.drawable.hvac_cool_off);
            thermostatCoolText.setTypeface(Typeface.DEFAULT);

            thermostatOffView.setImageResource(R.drawable.off_thermostat_drawable);
            thermostatOffText.setTypeface(Typeface.DEFAULT);
        }
        else if(mode.equals(KEY_COOL)) {
            thermostatHeatView.setImageResource(R.drawable.hvac_heat_off);
            thermostatHeatText.setTypeface(Typeface.DEFAULT);

            thermostatCoolView.setImageResource(R.drawable.hvac_cool);
            thermostatCoolText.setTypeface(Typeface.DEFAULT_BOLD);

            thermostatOffView.setImageResource(R.drawable.off_thermostat_drawable);
            thermostatOffText.setTypeface(Typeface.DEFAULT);
        }
        else if(mode.equals(KEY_OFF)) {
            thermostatHeatView.setImageResource(R.drawable.hvac_heat_off);
            thermostatHeatText.setTypeface(Typeface.DEFAULT);

            thermostatCoolView.setImageResource(R.drawable.hvac_cool_off);
            thermostatCoolText.setTypeface(Typeface.DEFAULT);

            thermostatOffView.setImageResource(R.drawable.off_thermostat_off);
            thermostatOffText.setTypeface(Typeface.DEFAULT);
        }

    }

    /**
     * Update the Value value part
     */
    private void updateValueRange() {

        Double temp_update = mThermostat_kitchen.getAmbientTemperatureC();
        long humid_update = mThermostat_kitchen.getHumidity();

        //comfort calculation
        double DI = temp_update - (0.55 - 0.55 * ((double)humid_update/(double)100.0))*(temp_update - 58);

        for(int i = 0; i < 5; i++)
            thermostatValue[i].setTextSize(18);

        if(DI >= 85) {//1
            thermostatValue[0].setTextSize(50);
        }
        else if(DI >= 76 && DI <= 84) {//2
            thermostatValue[1].setTextSize(50);
        }
        else if(DI >= 51 && DI <= 75) {//3
            thermostatValue[2].setTextSize(50);
        }
        else if(DI >= 26 && DI <= 50) {//4
            thermostatValue[3].setTextSize(50);
        }
        else {//5
            thermostatValue[4].setTextSize(50);
        }

    }

    /**
     * Updates views to show current state from thermostat and structures.
     */
    private void updateViews() {

        if (mThermostat_kitchen == null || mThermostat_main == null || mStructure == null || smokeAlarm == null) {
            Toast.makeText(getApplicationContext(), "Cofiguration failed", Toast.LENGTH_SHORT).show();
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
