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
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.support.v7.app.AppCompatActivity;

import com.nestlabs.sdk.ExceptionHandler;
import com.nestlabs.sdk.NestException;
import com.nestlabs.sdk.NestListener;
import com.nestlabs.sdk.WwnClient;
import com.nestlabs.sdk.models.Camera;
import com.nestlabs.sdk.models.GlobalUpdate;
import com.nestlabs.sdk.models.NestToken;
import com.nestlabs.sdk.models.SmokeCOAlarm;
import com.nestlabs.sdk.models.Structure;
import com.nestlabs.sdk.rest.parsers.ErrorMessage;


public class Security extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = Security.class.getSimpleName();

    private static final String CAMERA_KEY_INDOOR= "camera_key_indoor";
    private static final String CAMERA_KEY_OUTDOOR= "camera_key_outdoor";
    private static final String CAMERA_KEY_HELLO= "camera_key_hello";
    private static final String STRUCTURE_KEY = "structure_key";
    private static final String KEY_AWAY = "away";
    private static final String KEY_AUTO_AWAY = "auto-away";
    private static final String KEY_HOME = "home";
    private static final String SMOKE_KEY = "smoke";

    private static final String CLIENT_ID = Constants.CLIENT_ID;
    private static final String CLIENT_SECRET = Constants.CLIENT_SECRET;
    private static final String REDIRECT_URL = Constants.REDIRECT_URL;
    private static final int AUTH_TOKEN_REQUEST_CODE = 123;
    private final int MAX_NUM = 5;

    //textview
    private TextView securityGotoText;
    private ImageView securityGotoView;

    private ImageView securityLeftView;

    private Button indoor_button;
    private Button outdoor_button;
    private Button hello_button;

    private VideoView cameraRealTimeVideo;
    private MediaController cameraController;

    private ImageView securityHomeAwayView;
    private TextView securityHomeAwayText;

    private TextView[] securityJournalTime;
    private ImageView[] securityJournalIcon;
    private TextView[] securityJournalEvent;
    private TextView[] securityJournalStatus;

    //SHAREDPREFERENCES
    private SharedPreferences Indoor_Journal_file;
    private SharedPreferences.Editor Indoor_Journal_file_editor;

    private SharedPreferences Outdoor_Journal_file;
    private SharedPreferences.Editor Outdoor_Journal_file_editor;

    private SharedPreferences Hello_Journal_file;
    private SharedPreferences.Editor Hello_Journal_file_editor;

    private String[] time_to_file = {"time_1", "time_2","time_3","time_4","time_5"};

    private String[] icon_to_file = {"icon_1", "icon_2","icon_3","icon_4","icon_5"};

    private String[] event_to_file = {"event_1", "event_2","event_3","event_4","event_5"};

    private String[] secure_to_file = {"secure_1", "secure_2","secure_3","secure_4","secure_5"};

    private String[] url_to_file = {"url_1", "url_2","url_3","url_4","url_5"};

    //nest
    private WwnClient wwnClient = Constants.wwnClient;
    private NestToken mToken = Constants.mToken;
    private Camera indoor_camera = Constants.indoor_camera;
    private Camera outdoor_camera = Constants.outdoor_camera;
    private Camera hello_camera = Constants.hello_camera;
    private SmokeCOAlarm smokeAlarm = Constants.smokeAlarm;
    private Structure mStructure = Constants.mStructure;
    private Activity mActivity;

    private Camera current_camera = indoor_camera;
    private SharedPreferences.Editor current_editor = Indoor_Journal_file_editor;
    private static boolean camera_changed = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security);
        mActivity = this;

        //register
        securityLeftView = (ImageView) findViewById(R.id.Security_lefticon);
        securityGotoText = (TextView) findViewById(R.id.Security_to_camera_title);
        securityGotoView = (ImageView) findViewById(R.id.Security_to_camera_icon);

        indoor_button = (Button) findViewById(R.id.Security_camera_1);
        outdoor_button = (Button) findViewById(R.id.Security_camera_2);
        hello_button = (Button) findViewById(R.id.Security_camera_3);

        cameraRealTimeVideo = (VideoView) findViewById(R.id.Security_video_view);
        securityHomeAwayView = (ImageView) findViewById(R.id.Security_Home_Away_Button);
        securityHomeAwayText = (TextView) findViewById(R.id.Security_status);

        cameraController = new MediaController(this);

        securityJournalTime = new TextView[MAX_NUM];
        securityJournalIcon = new ImageView[MAX_NUM];
        securityJournalEvent = new TextView[MAX_NUM];
        securityJournalStatus = new TextView[MAX_NUM];

        int[] Time_series = {R.id.Security_Journal_time_1, R.id.Security_Journal_time_2, R.id.Security_Journal_time_3, R.id.Security_Journal_time_4, R.id.security_Journal_time_5};
        int[] Icon_series = {R.id.Security_Journal_image_1, R.id.Security_Journal_image_2, R.id.Security_Journal_image_3, R.id.Security_Journal_image_4, R.id.security_Journal_image_5};
        int[] Event_series = {R.id.Security_Journal_event_1, R.id.Security_Journal_event_2, R.id.Security_Journal_event_3, R.id.Security_Journal_event_4, R.id.security_Journal_event_5};
        int[] Status_series = {R.id.Security_Journal_status_1, R.id.Security_Journal_status_2, R.id.Security_Journal_status_3, R.id.Security_Journal_status_4, R.id.security_Journal_status_5};

        for(int i = 0; i < MAX_NUM; i++) {
            securityJournalTime[i] = (TextView) findViewById(Time_series[i]);
            securityJournalIcon[i] = (ImageView) findViewById(Icon_series[i]);
            securityJournalEvent[i] = (TextView) findViewById(Event_series[i]);
            securityJournalStatus[i] = (TextView) findViewById(Status_series[i]);
        }

        securityLeftView.setOnClickListener(this);
        securityGotoText.setOnClickListener(this);
        securityGotoView.setOnClickListener(this);
        indoor_button.setOnClickListener(this);
        outdoor_button.setOnClickListener(this);
        hello_button.setOnClickListener(this);
        securityHomeAwayView.setOnClickListener(this);
        securityHomeAwayText.setOnClickListener(this);

        //file preparation
        Indoor_Journal_file = getSharedPreferences("Indoor_Journal", 0);
        Outdoor_Journal_file = getSharedPreferences("Outdoor_Journal", 0);
        Hello_Journal_file = getSharedPreferences("Hello_Journal", 0);

        Indoor_Journal_file_editor = Indoor_Journal_file.edit();
        Outdoor_Journal_file_editor = Outdoor_Journal_file.edit();
        Hello_Journal_file_editor = Hello_Journal_file.edit();

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
                mStructure = savedInstanceState.getParcelable(STRUCTURE_KEY);
                smokeAlarm = savedInstanceState.getParcelable(SMOKE_KEY);

                current_camera = indoor_camera;

                updateViews();
            }
        }
        else {
            startWithListeners(mToken);

            current_camera = indoor_camera;
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
                mStructure = update.getStructures().get(0);
                smokeAlarm = update.getSmokeCOAlarms().get(0);

                Constants.indoor_camera = indoor_camera;
                Constants.outdoor_camera = outdoor_camera;
                Constants.hello_camera = hello_camera;
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


        switch (v.getId()) {
//            case R.id.Security_to_camera_title:
//            case R.id.Security_to_camera_icon:
//                Intent intent_camera=new Intent(Security.this,camera_detail.class);
//                startActivity(intent_camera);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                break;

            case R.id.Security_lefticon:
                Intent intent_dashboard=new Intent(Security.this,DashBoard.class);
                startActivity(intent_dashboard);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.Security_camera_1:

                if (indoor_camera == null) {
                    Toast.makeText(getApplicationContext(), "Indoor Don't exist", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(current_camera != indoor_camera && current_editor != Indoor_Journal_file_editor) {
                    current_camera = indoor_camera;
                    current_editor = Indoor_Journal_file_editor;
                    camera_changed = true;
                    updateVideoRange();
                    updateJournalRange();
                }
                break;

            case R.id.Security_camera_2:

                if (outdoor_camera == null) {
                    Toast.makeText(getApplicationContext(), "Outdoor Don't exist", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(current_camera != outdoor_camera && current_editor != Outdoor_Journal_file_editor) {
                    current_camera = outdoor_camera;
                    current_editor = Outdoor_Journal_file_editor;
                    camera_changed = true;
                    updateVideoRange();
                    updateJournalRange();
                }
                break;

            case R.id.Security_camera_3:

                if (hello_camera == null) {
                    Toast.makeText(getApplicationContext(), "Doorbell Don't exist", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(current_camera != hello_camera && current_editor != Hello_Journal_file_editor) {
                    current_camera = hello_camera;
                    current_editor = Hello_Journal_file_editor;
                    camera_changed = true;
                    updateVideoRange();
                    updateJournalRange();
                }
                break;

            case R.id.Security_Home_Away_Button:

                String state = mStructure.getAway();
                boolean isAway = state.equals(KEY_AWAY);
                boolean isAutoAway = state.equals(KEY_AUTO_AWAY);

                if(isAway || isAutoAway) {
                    wwnClient.structures.setAway(mStructure.getStructureId(), KEY_HOME);
                    //securityHomeAwayView.
                    securityHomeAwayText.setText(KEY_HOME.toUpperCase());
                }
                else {
                    wwnClient.structures.setAway(mStructure.getStructureId(), KEY_AWAY);
                    //securityHomeAwayView.
                    securityHomeAwayText.setText(KEY_AWAY.toUpperCase());
                }

                break;
        }

    }

    /**
     * update video and events
     */
    private void updateVideoRange() {

        if (current_camera == null) {
            Toast.makeText(getApplicationContext(), "No Camera available", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!current_camera.isStreaming() && camera_changed) {

            Toast.makeText(getApplicationContext(), "you entered it", Toast.LENGTH_SHORT).show();

            if(current_camera == indoor_camera) {
                indoor_button.setBackgroundResource(R.drawable.indoor_camera);
                outdoor_button.setBackgroundResource(R.drawable.outdoor_camera_off);
                hello_button.setBackgroundResource(R.drawable.hello_camera_off);
            }
            else if(current_camera == outdoor_camera) {
                indoor_button.setBackgroundResource(R.drawable.indoor_camera_off);
                outdoor_button.setBackgroundResource(R.drawable.outdoor_camera);
                hello_button.setBackgroundResource(R.drawable.hello_camera_off);
            }
            else {
                indoor_button.setBackgroundResource(R.drawable.indoor_camera_off);
                outdoor_button.setBackgroundResource(R.drawable.outdoor_camera_off);
                hello_button.setBackgroundResource(R.drawable.hello_camera);
            }

//            cameraRealTimeVideo.setVideoURI(Uri.parse(current_camera.getPublicShareUrl()));
//            cameraRealTimeVideo.setMediaController(cameraController);
//            cameraController.setMediaPlayer(cameraRealTimeVideo);

            camera_changed = false;

        }

    }

    private void updateJournalRange() {

        boolean changed_lastEvent = false;

        securityHomeAwayText.setText(mStructure.getAway().toUpperCase());

        if(camera_changed == true) {

            for(int i = 0; i < MAX_NUM; i++){

            }

            camera_changed = false;

        }

    }

    /**
     * Updates views to show current state from thermostat and structures.
     */
    private void updateViews() {

        if (hello_camera == null || indoor_camera == null || outdoor_camera == null || mStructure == null) {
            Toast.makeText(getApplicationContext(), "Nest configuration failed", Toast.LENGTH_SHORT).show();
            return;
        }

        if (current_editor == null || Hello_Journal_file_editor == null || Indoor_Journal_file_editor == null || Outdoor_Journal_file_editor == null) {
            //Toast.makeText(getApplicationContext(), "File configuration failed", Toast.LENGTH_SHORT).show();
            //return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateVideoRange();
                updateJournalRange();
            }
        });
    }
}
