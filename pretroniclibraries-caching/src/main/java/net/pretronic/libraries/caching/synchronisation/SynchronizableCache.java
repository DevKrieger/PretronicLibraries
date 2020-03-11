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

package net.pretronic.libraries.caching.synchronisation;

import net.pretronic.libraries.caching.Cache;
import net.pretronic.libraries.caching.CacheQuery;
import net.pretronic.libraries.document.Document;
import net.pretronic.synchronisation.SynchronisationHandler;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface SynchronizableCache<O,I> extends Cache<O>, SynchronisationHandler<O,I> {

    void setIdentifierQuery(CacheQuery<O> query);

    void setDeleteListener(BiConsumer<O, Document> listener);

    void setUpdateListener(BiConsumer<O, Document> listener);

    void setCreateHandler(BiFunction<I, Document,O> handler);
}
