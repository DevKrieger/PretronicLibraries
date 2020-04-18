/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:40
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

package net.pretronic.libraries.command.command.configuration;

import net.pretronic.libraries.utility.Validate;

public class DefaultCommandConfigurationBuilder {

    private boolean enabled = true;
    private String name;
    private String permission;
    private String description;
    private String[] aliases = new String[]{};

    protected DefaultCommandConfigurationBuilder(){}

    public DefaultCommandConfigurationBuilder name(String name) {
        this.name = name;
        return this;
    }

    public DefaultCommandConfigurationBuilder permission(String permission) {
        this.permission = permission;
        return this;
    }

    public DefaultCommandConfigurationBuilder description(String description) {
        this.description = description;
        return this;
    }

    public DefaultCommandConfigurationBuilder aliases(String... aliases) {
        this.aliases = aliases;
        return this;
    }

    public DefaultCommandConfigurationBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public DefaultCommandConfigurationBuilder disabled() {
        this.enabled = false;
        return this;
    }

    public DefaultCommandConfiguration create() {
        Validate.notNull(name);
        return new DefaultCommandConfiguration(enabled,name, permission, description, aliases);
    }
}
