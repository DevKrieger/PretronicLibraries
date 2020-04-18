/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:46
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

package net.pretronic.libraries.synchronisation;

import net.pretronic.libraries.document.Document;

public interface SynchronisationHandler<O,I> extends SynchronisationCaller<I> {

    SynchronisationCaller<I> getCaller();

    void onDelete(I identifier, Document data);

    void onCreate(I identifier, Document data);

    void onUpdate(I identifier, Document data);


    void init(SynchronisationCaller<I> caller);

    @Override
    default void update(I identifier, Document data){
        getCaller().update(identifier, data);
    }

    @Override
    default void updateAndIgnore(I identifier, Document data) {
        getCaller().updateAndIgnore(identifier, data);
    }

    @Override
    default void create(I identifier, Document data){
        getCaller().create(identifier, data);
    }

    @Override
    default void createAndIgnore(I identifier, Document data) {
        getCaller().createAndIgnore(identifier, data);
    }

    @Override
    default void delete(I identifier, Document data){
        getCaller().delete(identifier, data);
    }

    @Override
    default void deleteAndIgnore(I identifier, Document data) {
        getCaller().deleteAndIgnore(identifier, data);
    }

    default boolean isConnected(){
        return getCaller().isConnected();
    }
}