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

import android.content.Context;
import android.content.SharedPreferences;

import com.nestlabs.sdk.models.NestToken;

/**
 * Stores and returns a NestToken used for calling the Nest API.
 * @see <a href="https://developers.nest.com/documentation/cloud/how-to-auth">Authentication and Authorization with OAuth 2.0</a>
 */

public class Settings {
    private static final String TOKEN_KEY = "token";
    private static final String EXPIRATION_KEY = "expiration";

    /**
     * Saves the NestToken in preferences, or removes from preferences if null.
     */
    public static void saveAuthToken(Context context, NestToken token) {
        if (token == null) { // remove access token and expiration entries
            getPrefs(context).edit().remove(TOKEN_KEY).remove(EXPIRATION_KEY).commit();
            return;
        }
        getPrefs(context).edit() // save access token in preferences with expiration
                .putString(TOKEN_KEY, token.getToken())
                .putLong(EXPIRATION_KEY, token.getExpiresIn())
                .commit();
    }

    /**
     * Loads token from preferences. 
     */
    public static NestToken loadAuthToken(Context context) {
        final SharedPreferences prefs = getPrefs(context);
        final String token = prefs.getString(TOKEN_KEY, null);
        final long expirationDate = prefs.getLong(EXPIRATION_KEY, -1);

        if (token == null || expirationDate == -1) {
            return null;
        }

        return new NestToken(token, expirationDate);
    }

    /**
     * Helper to return the NestToken preference map.
     */
    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(NestToken.class.getSimpleName(), 0);
    }

}
