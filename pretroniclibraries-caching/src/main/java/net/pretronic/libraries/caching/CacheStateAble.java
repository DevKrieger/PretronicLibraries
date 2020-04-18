/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:42
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

package net.pretronic.libraries.caching;

/**
 * A {@link CacheStateAble} object shows his caching state. In some application it is required
 * to reload objects which are no longer in the cache.
 * @param <T>
 */
public interface CacheStateAble<T> {

    /**
     * Check if the object is in the cache.
     *
     * @return True if the object is in the cache
     */
    boolean isCached();

    /**
    Define if the object is cached.
     */
    boolean setCached(boolean cached);

    /**
     * Reload the object and get the same instance.
     *
     * @return The reloaded object (Can be the same or a new instance).
     */
    T reload();
}
