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
import net.pretronic.libraries.synchronisation.NetworkSynchronisationCallback;
import net.pretronic.libraries.synchronisation.SynchronisationHandler;
import net.pretronic.libraries.synchronisation.UnconnectedSynchronisationCaller;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * The {@link SynchronizableCache} is an integrated cache into the Pretronic synchronisation libraries.
 * It provides additional functionalities for synchronizing objects in a cluster network. Read more about
 * the synchronisation in the corresponding module.
 */
public interface SynchronizableCache<O,I> extends Cache<O>, SynchronisationHandler<O,I>, NetworkSynchronisationCallback {

    /**
     * If this option is enabled, the cache will be clear after a network disconnect.
     *
     * @param enabled True if enabled
     */
    void setClearOnDisconnect(boolean enabled);

    /**
     * If this option is enable. The cache does not cache objects when the network is not connected.
     *
     * @param enabled True if enabled
     */
    void setSkipOnDisconnect(boolean enabled);

    /**
     * This is very helpful when there is not network available in a special situation.
     * In this case, you can initialize a fake network connection.
     */
    default void initUnconnected(){
        init(new UnconnectedSynchronisationCaller<>(true));
    }

    /**
     * This is very import. This synchronisation process uses this connection for identifying the objects in the cache.
     *
     * @param query The query (by identifier)
     */
    void setIdentifierQuery(CacheQuery<O> query);

    /**
     * The delete listener is called when a objects gets deleted on a remote server.
     *
     * <p>Usually the cache will automatically remove the objects.</p>
     *
     * @param listener The delete listener
     */
    void setDeleteListener(BiConsumer<O, Document> listener);

    /**
     * The update listener is called when a objects get updated on a remote server.
     *
     * <p>If the update is called in the called, all other servers in the network will receive this update.</p>
     *
     * <p>If your object does implement @{@link net.pretronic.libraries.synchronisation.Synchronizable} it is called as well.</p>
     *
     * @param listener The update listener
     */
    void setUpdateListener(BiConsumer<O, Document> listener);

    /**
     * The create listener is called when a object is created on a remote server.
     *
     * <p>The result of the function will be inserted in the local cache.</p>
     *
     * @param handler The create handler
     */
    void setCreateHandler(BiFunction<I, Document,O> handler);
}
