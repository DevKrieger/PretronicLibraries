/*
 * (C) Copyright 2020 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.01.20, 20:19
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

package net.prematic.libraries.message;

import net.prematic.libraries.message.language.Language;
import net.prematic.libraries.message.repository.RepositoryInfo;
import net.prematic.libraries.utility.annonations.Nullable;

public class MessagePackMeta {

    private final String name;
    private final String module;
    private final boolean bml;
    private final boolean enabled;
    private final Language language;
    @Nullable
    private final RepositoryInfo repository;

    public MessagePackMeta(String name,String module, boolean bml, boolean enabled, Language language, RepositoryInfo repository) {
        this.name = name;
        this.module = module;
        this.bml = bml;
        this.enabled = enabled;
        this.language = language;
        this.repository = repository;
    }

    public String getName() {
        return name;
    }

    public String getModule() {
        return module;
    }

    public boolean isBml() {
        return bml;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Language getLanguage() {
        return language;
    }

    public RepositoryInfo getRepository() {
        return repository;
    }
}
