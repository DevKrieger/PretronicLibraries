/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 31.08.19, 20:16
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

package net.prematic.libraries.utility.http;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpClientTest {
    
    @Test
    void connect() throws Exception{
        int expected = 200;
        HttpClient client = new HttpClient();

        client.setUrl("https://github.com/DevKrieger/DKBans/archive/master.zip");
        client.setRequestMethod(HttpMethod.GET);
        client.setAcceptedCharset("UTF-8");

        HttpResult result = client.connect();

        result.save(new File("test.zip"));

        assertEquals(expected,result.getCode());
    }
}