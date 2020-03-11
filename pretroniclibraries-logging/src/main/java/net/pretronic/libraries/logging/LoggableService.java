/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:45
 *
 * The PretronicLibraries Project is under the Apache License, version 2.0 (the "License");
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

package net.pretronic.libraries.logging;

/**
 * A loggable service is part of the message info. It gives information about
 * the service, which created this log message or caused this error.
 *
 * <p>Important: Create a static object for your service and do not create for every record a new object.</p>
 */
public class LoggableService {

    private final String name, shortName, version, website;

    public LoggableService(String name) {
        this(name,name);
    }

    public LoggableService(String name, String shortName) {
        this(name,shortName,"Unknown",null);
    }

    public LoggableService(String name, String shortName, String version) {
        this(name,shortName,version,null);
    }

    public LoggableService(String name, String shortName, String version, String website) {
        this.name = name;
        this.shortName = shortName;
        this.version = version;
        this.website = website;
    }

    /**
     * Get the name of the service.
     *
     * @return The name of the service.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the short name of the service (for better displaying).
     *
     * @return The short name of the service
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Get the version of the service.
     *
     * @return The version of the service
     */
    public String getVersion() {
        return version;
    }

    /**
     * Get the website of the service.
     *
     * @return The website of the service.
     */
    public String getWebsite() {
        return website;
    }
}
