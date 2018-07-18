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
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

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

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import java.util.List;
import java.util.Random;
import retrofit2.Response;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.XAxis;
import com.wang.avi.AVLoadingIndicatorView;
import android.support.design.widget.NavigationView;

public class DashBoard extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = DashBoard.class.getSimpleName();

    private static final String THERMOSTAT_KEY_MAIN = "thermostat_key_main";
    private static final String THERMOSTAT_KEY_KITCHEN= "thermostat_key_kitchen";
    private static final String STRUCTURE_KEY = "structure_key";
    private static final String KEY_AWAY = "away";
    private static final String KEY_AUTO_AWAY = "auto-away";
    private static final String KEY_SMOKECO_ENERGENCY = "emergency";
    private static final String SMOKE_KEY = "smoke";
    private static Retrofit retrofit = Constants.retrofit;
    public static final String BASE_URL = "http://test.growatt.com/v1/";

    private static final String HUMID = "Humid: %d%%";
    private static final String TEMP = "Temp: %.1f℃";
    private static final String PM = "PM2.5: %s";
    private static final String ENERGY = "%.1fKW";
    private static final String CO = "CO: %s";

    private static final String CLIENT_ID = Constants.CLIENT_ID;
    private static final String CLIENT_SECRET = Constants.CLIENT_SECRET;
    private static final String REDIRECT_URL = Constants.REDIRECT_URL;
    private static final int AUTH_TOKEN_REQUEST_CODE = 123;

    //menu
    private DrawerLayout drawer;
    private NavigationView navigationView;

    //textview
    private TextView securityGotoText;
    private TextView comfortGotoText;
    private TextView energyGotoText;

    //imageview
    private ImageView securityGotoView;
    private ImageView comfortGotoView;
    private ImageView energyGotoView;

    private ImageView dashboardLeftView;

    //security view members
    private TextView securityContainerClock;
    private TextView securityContainerDay;
    private ImageView SecurityContainerHome;
    private TextView SecurityContainerHomeText;
    private ImageView SecurityContainerSecure;
    private TextView SecurityContainerSecureText;

    //comfort view members
    private TextView comfortContainerValue;
    private TextView comfortContainerTemp;
    private TextView comfortContainerHumid;
    private TextView comfortContainerPM;
    private TextView comfortContainerCO;

    //energy view members
    private LineChart lineChart;
    private TextView EnergyContainerInflux;
    private TextView EnergyContainerOutflux;

    //for inflater
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private Dialog mLoadingDialog;

    //nest
    private WwnClient wwnClient = Constants.wwnClient;
    private NestToken mToken = Constants.mToken;
    private Camera indoor_camera = Constants.indoor_camera;
    private Camera outdoor_camera = Constants.outdoor_camera;
    private Camera hello_camera = Constants.hello_camera;
    private Thermostat mThermostat_main = Constants.mThermostat_main;
    private Thermostat mThermostat_kitchen = Constants.mThermostat_kitchen;
    private Structure mStructure = Constants.mStructure;
    private SmokeCOAlarm smokeAlarm = Constants.smokeAlarm;
    private Activity mActivity;

    private boolean isSecure = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_with_navigation);
        mActivity = this;

        UIHelper.showDialogForLoading(DashBoard.this, "Lotus House Building...");

        //menu
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //register
        securityGotoText = (TextView) findViewById(R.id.DashBoard_security_title);
        comfortGotoText = (TextView) findViewById(R.id.DashBoard_comfort_title);
        energyGotoText = (TextView) findViewById(R.id.DashBoard_energy_title);

        securityGotoView = (ImageView) findViewById(R.id.DashBoard_securityicon);
        comfortGotoView = (ImageView) findViewById(R.id.DashBoard_comfort_icon);
        energyGotoView = (ImageView) findViewById(R.id.DashBoard_energy_icon);

        dashboardLeftView = (ImageView) findViewById(R.id.DashBoard_lefticon);

        securityContainerClock = (TextView) findViewById(R.id.security_main_time);
        securityContainerDay = (TextView) findViewById(R.id.security_main_day);
        SecurityContainerHomeText = (TextView) findViewById(R.id.security_main_home_text);
        SecurityContainerSecureText = (TextView) findViewById(R.id.security_main_secure_text);
        SecurityContainerHome = (ImageView) findViewById(R.id.security_main_home_icon);
        SecurityContainerSecure = (ImageView) findViewById(R.id.security_main_secure_icon);

        comfortContainerValue = (TextView) findViewById(R.id.comfort_main_value);
        comfortContainerTemp = (TextView) findViewById(R.id.comfort_main_temp_value);
        comfortContainerHumid = (TextView) findViewById(R.id.comfort_main_humid_value);
        comfortContainerPM = (TextView) findViewById(R.id.comfort_main_air_value);
        comfortContainerCO = (TextView) findViewById(R.id.comfort_main_CO_value2);

        EnergyContainerInflux = (TextView) findViewById(R.id.energy_main_influx);
        EnergyContainerOutflux = (TextView) findViewById(R.id.energy_main_outflux);

        lineChart = (LineChart) findViewById(R.id.linechart);

        securityGotoText.setOnClickListener(this);
        comfortGotoText.setOnClickListener(this);
        energyGotoText.setOnClickListener(this);
        securityGotoView.setOnClickListener(this);
        comfortGotoView.setOnClickListener(this);
        energyGotoView.setOnClickListener(this);
        dashboardLeftView.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        //retrofit configuration
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Constants.retrofit = retrofit;
        }

        lineChart.setDrawBorders(false);


        if(retrofit != null) {
            //First Drawing for the chart
            List<Entry> entries = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                entries.add(new Entry(i + 1, random.nextInt(240) + 30));

            }

            LineDataSet dataSet = new LineDataSet(entries, "能源数据");
            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
        }else {
            Toast.makeText(getApplicationContext(), "Growatt connection failed", Toast.LENGTH_SHORT).show();
        }



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

    //when back if menu open ,then close it
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //menu for left menu
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_thermostat) {
//            drawer.closeDrawer(GravityCompat.START);
            Intent intent_security=new Intent(DashBoard.this,thermostat_detail.class);
            startActivity(intent_security);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            // Handle the camera action
        } else if (id == R.id.nav_camera) {
//            Intent intent_security=new Intent(DashBoard.this,camera_detail.class);
//            startActivity(intent_security);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } else if (id == R.id.nav_lighting) {
            Intent intent_security=new Intent(DashBoard.this,hue_activity.class);
            startActivity(intent_security);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } else if (id == R.id.nav_normal) {
            Toast.makeText(getApplicationContext(), "normal", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_evening) {
            Toast.makeText(getApplicationContext(), "evening", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_sleep) {
            Toast.makeText(getApplicationContext(), "sleep", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_away) {
            Toast.makeText(getApplicationContext(), "away", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_party) {
            Toast.makeText(getApplicationContext(), "party", Toast.LENGTH_SHORT).show();

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                smokeAlarm = update.getSmokeCOAlarms().get(0);

                Constants.indoor_camera = indoor_camera;
                Constants.outdoor_camera = outdoor_camera;
                Constants.hello_camera = hello_camera;
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

    //add exceptionHandler
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

    //when stop remove all the listeners
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

        switch (v.getId()) {

            //to security page
            case R.id.DashBoard_securityicon:
            case R.id.DashBoard_security_title:
                Intent intent_security=new Intent(DashBoard.this,Security.class);

                startActivity(intent_security);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

                //to Comfort page
            case R.id.DashBoard_comfort_icon:
            case R.id.DashBoard_comfort_title:
                Intent intent_comfort=new Intent(DashBoard.this,Comfort.class);
                startActivity(intent_comfort);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

                //to energy page
            case R.id.DashBoard_energy_icon:
            case R.id.DashBoard_energy_title:
                Intent intent_energy=new Intent(DashBoard.this,Energy_java.class);
                startActivity(intent_energy);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

        }

    }

    /**
     * Update the security part
     */
    private void updateSecurityRange() {

        String state = mStructure.getAway();
        boolean isAway = state.equals(KEY_AWAY) || state.equals(KEY_AUTO_AWAY);

        if(isAway) {
            SecurityContainerHomeText.setText("Away");
            //SecurityContainerHome.
        }
        else {
            SecurityContainerHomeText.setText("Home");
            //SecurityContainerHome.
        }

        if(isSecure) {
            DateFormat time = new SimpleDateFormat("HH:mm:ss a");
            DateFormat day = new SimpleDateFormat("EEE, d MMM");
            String time_s = time.format(Calendar.getInstance().getTime());
            String day_s = day.format(Calendar.getInstance().getTime());

            securityContainerClock.setText(time_s);
            securityContainerDay.setText(day_s);

            SecurityContainerSecureText.setText("Secure");
            //SecurityContainerSecure.
        }
        else {

            SecurityContainerSecureText.setText("Insecure");
            SecurityContainerSecureText.setTextColor(Color.RED);
            securityContainerDay.setTextColor(Color.RED);
            securityContainerClock.setTextColor(Color.RED);
            //SecurityContainerSecure.
        }
    }

    /**
     * Update the comfort part
     */
    private void updateComfortRange() {

        double ambientC = mThermostat_kitchen.getAmbientTemperatureC();
        long humidC = mThermostat_kitchen.getHumidity();

        //calculate comfort value
        double DI = ambientC - (0.55 - 0.55 * ((double)humidC/(double)100.0))*(ambientC - 58);
        int TH_index = 0;

        if(DI >= 85) {//1
            TH_index = 1;
        }
        else if(DI >= 76 && DI <= 84) {//2
            TH_index = 2;
        }
        else if(DI >= 51 && DI <= 75) {//3
            TH_index = 3;
        }
        else if(DI >= 26 && DI <= 50) {//4
            TH_index = 4;
        }
        else {//5
            TH_index = 5;
        }

        //update the information
        String COstate = smokeAlarm.getCOAlarmState();
        String PMstate = smokeAlarm.getSmokeAlarmState();

        comfortContainerPM.setText(String.format(PM, COstate).toUpperCase());
        comfortContainerCO.setText(String.format(CO, COstate).toUpperCase());

        if(COstate == KEY_SMOKECO_ENERGENCY || PMstate == KEY_SMOKECO_ENERGENCY)
            isSecure = false;

        comfortContainerTemp.setText(String.format(TEMP, ambientC));
        comfortContainerHumid.setText(String.format(HUMID, humidC));

        comfortContainerValue.setText(String.valueOf(TH_index));

    }

    /**
     * Update the energy part
     */
    private void updateEnergyRange() {

        double Influx = 1.0;
        double Outflux = 1.0;

        EnergyContainerInflux.setText(String.format(ENERGY,Influx));
        EnergyContainerOutflux.setText(String.format(ENERGY,Outflux));
        //EnergyContainerImage.

        UIHelper.hideDialogForLoading();
    }

    /**
     * Updates views to show current state.
     */
    private void updateViews() {

        if (mThermostat_kitchen == null || mThermostat_main == null || mStructure == null || retrofit == null || smokeAlarm == null) {
            Toast.makeText(getApplicationContext(), "Configuration failed", Toast.LENGTH_SHORT).show();
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateSecurityRange();
                updateComfortRange();
                updateEnergyRange();
            }
        });
    }
}
