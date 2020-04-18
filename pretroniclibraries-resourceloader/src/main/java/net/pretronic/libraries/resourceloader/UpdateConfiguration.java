/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 04.04.20, 22:17
 * @web %web%
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

package net.pretronic.libraries.resourceloader;

public class UpdateConfiguration {

    protected static UpdateConfiguration DEFAULT = new UpdateConfiguration(true,"RELEASE");

    private final boolean enabled;
    private final String qualifier;

    public UpdateConfiguration(boolean enabled, String qualifier) {
        this.enabled = enabled;
        this.qualifier = qualifier;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getQualifier() {
        return qualifier;
    }

    public static UpdateConfiguration parse(String input){
        String[] result = input.split(";");
        if(result.length != 2) throw new IllegalArgumentException("Invalid configuration");
        return new UpdateConfiguration(Boolean.parseBoolean(result[0]),result[1]);
    }
}
