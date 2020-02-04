/*
 * (C) Copyright 2020 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 01.02.20, 18:03
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

package net.prematic.libraries.command.command.configuration;

public class DefaultCommandConfiguration implements CommandConfiguration {

    private final String name;
    private final String permission;
    private final String description;
    private final String[] aliases;

    protected DefaultCommandConfiguration(String name, String permission, String description, String[] aliases) {
        this.name = name;
        this.permission = permission;
        this.description = description;
        this.aliases = aliases;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public boolean hasAlias(String alias) {
        if(name.equalsIgnoreCase(alias)) return true;
        else{
            for (String alias0 : aliases) {
                if(alias0.equalsIgnoreCase(alias)) return true;
            }
            return false;
        }
    }
}
