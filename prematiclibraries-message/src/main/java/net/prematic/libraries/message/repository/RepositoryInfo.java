/*
 * (C) Copyright 2020 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 19.01.20, 16:49
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

package net.prematic.libraries.message.repository;

import java.util.UUID;

public class RepositoryInfo {

    private final String name;
    private final String url;
    private final UUID id;
    private final int version;

    public RepositoryInfo(String name, String url, UUID id, int version) {
        this.name = name;
        this.url = url;
        this.id = id;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public UUID getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }
}
