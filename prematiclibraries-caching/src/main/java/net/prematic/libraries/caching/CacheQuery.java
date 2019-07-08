/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 16.06.19 18:26
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

package net.prematic.libraries.caching;

/**
 * With a cache query, you are able to search an object in a cache.
 *
 * <p>With the loader</p>
 *
 * @param <O>
 */
public interface CacheQuery<O> {

    /**
     * Check if this is the correct object.
     *
     * @param item The object to check.
     * @param identifiers The search identifier
     * @return If the object is valid
     */
    boolean check(O item, Object[] identifiers);

    /**
     * Check if this identifier is okay.
     *
     * @param identifiers The search identifier
     */
    default void validate(Object[] identifiers){
        //Default it is not necessary to validate identifiers, but recommended.
    }

    /**
     * Load a object from a external datasource.
     *
     * @param identifiers The search identifier
     * @return The loaded object
     */
    default O load(Object[] identifiers){
        return null;
    }


}
