package com.nestlabs.sdk.sample;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.wrapper.HueLog;
import com.philips.lighting.hue.sdk.wrapper.Persistence;
import com.philips.lighting.hue.sdk.wrapper.connection.BridgeConnection;
import com.philips.lighting.hue.sdk.wrapper.connection.BridgeConnectionCallback;
import com.philips.lighting.hue.sdk.wrapper.connection.BridgeConnectionType;
import com.philips.lighting.hue.sdk.wrapper.connection.BridgeResponseCallback;
import com.philips.lighting.hue.sdk.wrapper.connection.BridgeStateUpdatedCallback;
import com.philips.lighting.hue.sdk.wrapper.connection.BridgeStateUpdatedEvent;
import com.philips.lighting.hue.sdk.wrapper.connection.ConnectionEvent;
import com.philips.lighting.hue.sdk.wrapper.discovery.BridgeDiscovery;
import com.philips.lighting.hue.sdk.wrapper.discovery.BridgeDiscoveryResult;
import com.philips.lighting.hue.sdk.wrapper.domain.Bridge;
import com.philips.lighting.hue.sdk.wrapper.domain.BridgeBuilder;
import com.philips.lighting.hue.sdk.wrapper.domain.BridgeState;
import com.philips.lighting.hue.sdk.wrapper.domain.HueError;
import com.philips.lighting.hue.sdk.wrapper.domain.ReturnCode;
import com.philips.lighting.hue.sdk.wrapper.domain.clip.ClipResponse;
import com.philips.lighting.hue.sdk.wrapper.domain.device.light.LightPoint;
import com.philips.lighting.hue.sdk.wrapper.domain.device.light.LightState;

import java.util.List;

public class hue_activity extends AppCompatActivity implements View.OnClickListener {

    static {
        System.loadLibrary("huesdk" );
    }
    private Bridge bridge;
    private BridgeDiscovery bridgeDiscovery;
    private List<BridgeDiscoveryResult> bridgeDiscoveryResults;
    private static final String TAG = hue_activity.class.getSimpleName();

    private static final int MAX_HUE = 65535;

    private TextView current_lighting_percentage;
    private SeekBar lighting_seekbar;
    private ImageView Turn_on_off;
    private ImageView light_position;
    /*
    潘哥看这里，改这个**（*（*（*（*（*（*（*/
    private int lounge_bulb=1;
    private int kitchen_bulb=1;
    private int room_bulb=1;

    private int cur_bulb = lounge_bulb;
    private int cur_state;
    private boolean succeed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lighting);

        Persistence.setStorageLocation(getFilesDir().getAbsolutePath(), "hue_page");
        HueLog.setConsoleLogLevel(HueLog.LogLevel.INFO);

        lighting_seekbar = (SeekBar) findViewById(R.id.lighting_seekBar);
        current_lighting_percentage = (TextView) findViewById(R.id.lighting_max);
        Turn_on_off = (ImageView) findViewById(R.id.lighting_on_off);
        light_position = (ImageView) findViewById(R.id.lighting_position);


        Turn_on_off.setOnClickListener(this);
        light_position.setOnClickListener(this);


        lighting_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightnessTurn(cur_bulb,progress,Turn_on_off);
                if(progress == 100)
                    current_lighting_percentage.setText("MAX");
                else
                    current_lighting_percentage.setText(String.valueOf(progress));
                // 当拖动条的滑块位置发生改变时触发该方法,在这里直接使用参数progress，即当前滑块代表的进度值,最大值100

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "start！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "end！");
            }

        });

        String bridgeIp = "192.168.0.199";
        connectToBridge(bridgeIp);
    }

    //!!!!!!!!!!!need to implement
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.lighting_lefticon:
                Intent intent_energy=new Intent(hue_activity.this,Energy_java.class);
                startActivity(intent_energy);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

                //still undecided
            case R.id.lighting_position:
                if(cur_bulb == lounge_bulb)
                {
                    cur_bulb = kitchen_bulb;
//                    light_position.setImageResource();

                }
                else if(cur_bulb == kitchen_bulb)
                {
                    cur_bulb = room_bulb;
//                    light_position.setImageResource();
                }
                else
                {
                    cur_bulb = lounge_bulb;
//                    light_position.setImageResource();
                }
                break;

            case R.id.lighting_color:
                //change color
                break;

            case R.id.lighting_on_off:
                if(cur_state == 0)
                {
                    turnOnlight(true, cur_bulb,lighting_seekbar);
                    cur_state = 100;
                }
                else
                {
                    turnOnlight(false, cur_bulb,lighting_seekbar);
                    cur_state = 0;
                }
                break;
        }

    }

    private void turnOnlight(boolean lightOn, int bulb, SeekBar progressBar ){
        if(succeed == false) {
            Toast.makeText(hue_activity.this, "Bridge connection failed", Toast.LENGTH_SHORT).show();
            return;
        }
        BridgeState bridgeState = bridge.getBridgeState();
        List<LightPoint> lights = bridgeState.getLights();
        Log.i(TAG, "***entered***");
        final LightPoint light=lights.get(bulb);
        if(light == null) {
            Toast.makeText(hue_activity.this, "Light DO NOT exist", Toast.LENGTH_SHORT).show();
            return;
        }
        final LightState lightState = new LightState();
        if (lightOn){
            Log.i(TAG, "catched");
            lightState.setOn(true);
        }else{
            Log.i(TAG, "not catched");
            lightState.setOn(false);
        }
        light.updateState(lightState, BridgeConnectionType.LOCAL, new BridgeResponseCallback() {
            @Override
            public void handleCallback(Bridge bridge, ReturnCode returnCode, List<ClipResponse> list, List<HueError> errorList) {
                if (returnCode == ReturnCode.SUCCESS) {
                    Log.i(TAG, "Changed hue of light " + light.getIdentifier() + " to " + lightState.getHue());
                } else {
                    Log.e(TAG, "Error changing hue of light " + light.getIdentifier());
                    for (HueError error : errorList) {
                        Log.e(TAG, error.toString());
                    }
                }
            }
        });
        UIupdate(lightOn,progressBar);
    }

    private void UIupdate(boolean lightOn, SeekBar progressBar){
        if (lightOn)
            progressBar.setProgress(100);
        else
            progressBar.setProgress(0);

    }

    private void brightnessTurn(int bulb,int progress,ImageView imageview){
        if(succeed == false) {
            Toast.makeText(hue_activity.this, "Bridge connection failed", Toast.LENGTH_SHORT).show();
            return;
        }
        BridgeState bridgeState = bridge.getBridgeState();
        List<LightPoint> lights = bridgeState.getLights();
        Log.i(TAG, "brightness control*****");
        final LightPoint light=lights.get(bulb);
        if(light == null) {
            Toast.makeText(hue_activity.this, "Light DO NOT exist", Toast.LENGTH_SHORT).show();
            return;
        }
        final LightState lightState = new LightState();
        if (progress==0){
            lightState.setOn(false);
        }
        else{
            lightState.setOn(true);
        }
        lightState.setBrightness(progress);
        cur_state = progress;
        light.updateState(lightState, BridgeConnectionType.LOCAL, new BridgeResponseCallback() {
            @Override
            public void handleCallback(Bridge bridge, ReturnCode returnCode, List<ClipResponse> list, List<HueError> errorList) {
                if (returnCode == ReturnCode.SUCCESS) {
                    Log.i(TAG, "Changed hue of light " + light.getIdentifier() + " to " + lightState.getHue());
                } else {
                    Log.e(TAG, "Error changing hue of light " + light.getIdentifier());
                    for (HueError error : errorList) {
                        Log.e(TAG, error.toString());
                    }
                }
            }
        });
        ImageUpdate(progress,imageview);
    }

    private void ImageUpdate(int progress, ImageView imageview){
        if(progress!=0)
            imageview.setImageResource(R.drawable.heat_thermostat_drawable);
        else
            imageview.setImageResource(R.drawable.off_thermostat_drawable);
    }

    private BridgeStateUpdatedCallback bridgeStateUpdatedCallback = new BridgeStateUpdatedCallback() {
        @Override
        public void onBridgeStateUpdated(Bridge bridge, BridgeStateUpdatedEvent bridgeStateUpdatedEvent) {
            Log.i(TAG, "Bridge state updated event: " + bridgeStateUpdatedEvent);

            switch (bridgeStateUpdatedEvent) {
                case INITIALIZED:
                    // The bridge state was fully initialized for the first time.
                    // It is now safe to perform operations on the bridge state.
                    Log.i(TAG,  "Connected!");
                    succeed = true;
                    //connection_status.setText("Connected!");
                    break;

                case LIGHTS_AND_GROUPS:
                    // At least one light was updated.
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * The callback that receives bridge connection events
     */
    private BridgeConnectionCallback bridgeConnectionCallback = new BridgeConnectionCallback() {
        @Override
        public void onConnectionEvent(BridgeConnection bridgeConnection, ConnectionEvent connectionEvent) {
            Log.i(TAG, "Connection event: " + connectionEvent);

            switch (connectionEvent) {
                case LINK_BUTTON_NOT_PRESSED:
                    Log.i(TAG,  "Press the link button to authenticate.");
                    succeed = false;
//                    Toast.makeText(hue_activity.this, "Press the link button to authenticate.", Toast.LENGTH_SHORT).show();
                    //connection_status.setText("Press the link button to authenticate.");
                    break;

                case COULD_NOT_CONNECT:
                    succeed = false;
                    Log.i(TAG,   "Could not connect.");
//                    Toast.makeText(hue_activity.this, "could not connect.", Toast.LENGTH_SHORT).show();
                    //connection_status.setText("Could not connect.");
                    break;

                case CONNECTION_LOST:
                    succeed = false;
                    Log.i(TAG,   "Connection lost. Attempting to reconnect.");
//                    Toast.makeText(hue_activity.this, "Connection lost. Attempting to reconnect.", Toast.LENGTH_SHORT).show();
                    //connection_status.setText("Connection lost. Attempting to reconnect.");
                    break;

                case CONNECTION_RESTORED:
                    succeed = true;
//                    Toast.makeText(hue_activity.this, "Connection restored.", Toast.LENGTH_SHORT).show();
                    Log.i(TAG,   "Connection restored.");
                    //connection_status.setText("Connection restored.");
                    break;

                case DISCONNECTED:
                    succeed = false;
                    // User-initiated disconnection.
                    break;

                default:
                    succeed = true;
                    break;
            }
        }

        @Override
        public void onConnectionError(BridgeConnection bridgeConnection, List<HueError> list) {
            for (HueError error : list) {
                Log.e(TAG, "Connection error: " + error.toString());
                succeed = false;
            }
        }
    };

    private void stopBridgeDiscovery() {
        if (bridgeDiscovery != null) {
            bridgeDiscovery.stop();
            bridgeDiscovery = null;
        }
    }

    private void disconnectFromBridge() {
        if (bridge != null) {
            bridge.disconnect();
            bridge = null;
        }
    }

    private void connectToBridge(String bridgeIp) {
        stopBridgeDiscovery();
        disconnectFromBridge();

        bridge = new BridgeBuilder("app name", "device name")
                .setIpAddress(bridgeIp)
                .setConnectionType(BridgeConnectionType.LOCAL)
                .setBridgeConnectionCallback(bridgeConnectionCallback)
                .addBridgeStateUpdatedCallback(bridgeStateUpdatedCallback)
                .build();

        bridge.connect();
    }

}

