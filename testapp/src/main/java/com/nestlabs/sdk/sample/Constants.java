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

import com.nestlabs.sdk.WwnClient;
import com.nestlabs.sdk.models.Camera;
import com.nestlabs.sdk.models.NestToken;
import com.nestlabs.sdk.models.SmokeCOAlarm;
import com.nestlabs.sdk.models.Structure;
import com.nestlabs.sdk.models.Thermostat;

import retrofit2.Retrofit;

/**
 * The values below need to be substituted with your selected Works with Nest product fields.
 * The selected product configuration can be found by selecting a product you created
 * from https://console.developers.nest.com/products
 *
 * @see <a href="https://developers.nest.com/documentation/cloud/how-to-auth">Authentication and Authorization with OAuth 2.0</a>
 */

public class Constants {

    /**
     * Replace this with your Nest Product Client ID in the OAuth section.
     */
    public static final String CLIENT_ID = "09d15151-fff4-4ac8-8e4d-2962bbb85d86";

    /**
     * Replace this with your Nest Product Client Secret in the OAuth section. Keep this secret safe.
     */
    public static final String CLIENT_SECRET = "IVl0GitjzZVuGnePfbXhzc2rO";

    /**
     * Replace this with your Nest Product Redirect URI above the OAuth section (not the Authorization URL).
     * Remember to set this Url in Product Configuration (click the edit button on that product view).
     * @see <a href="https://developers.nest.com/documentation/cloud/how-to-auth#redirect_uri_experience">Redirect URI experience</a>
     */
    public static final String REDIRECT_URL = "http://localhost:8080/auth/nest/callback"; //ex: "http://localhost/"

    public static WwnClient wwnClient = null;
    public static NestToken mToken;
    public static Camera indoor_camera;
    public static Camera outdoor_camera;
    public static Camera hello_camera;
    public static Thermostat mThermostat_main;
    public static Thermostat mThermostat_kitchen;
    public static Structure mStructure;
    public static SmokeCOAlarm smokeAlarm;
    public static Retrofit retrofit;

}
