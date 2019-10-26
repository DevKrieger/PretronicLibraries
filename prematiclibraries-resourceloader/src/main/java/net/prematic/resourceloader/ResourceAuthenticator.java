/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 25.10.19, 22:21
 *
 * The PrematicLibraries Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package net.prematic.resourceloader;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * With the resource authenticator you are able to authenticate a download or version check request.
 */
public interface ResourceAuthenticator {

    /**
     * Prepare the request and set your authentication properties.
     *
     * @param connection The http connection for authenticating
     */
    void invokeAuthentication(HttpURLConnection connection);

    /**
     * Use this method for creating a basic http authenticator with username and password.
     *
     * @param username The username
     * @param password The password
     * @return The created authenticator
     */
    static ResourceAuthenticator newBasicHttp(String username, String password){
        return new BasicHttp(username, password);
    }

    class BasicHttp implements ResourceAuthenticator{

        private static final String PROPERTY_AUTHORIZATION = "Authorization";

        private final String username;
        private final String password;

        private BasicHttp(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public void invokeAuthentication(HttpURLConnection connection) {
            connection.setRequestProperty(PROPERTY_AUTHORIZATION,"Basic "+ Base64.getEncoder().encodeToString((username+":"+password).getBytes(StandardCharsets.UTF_8)));
        }
    }
}
