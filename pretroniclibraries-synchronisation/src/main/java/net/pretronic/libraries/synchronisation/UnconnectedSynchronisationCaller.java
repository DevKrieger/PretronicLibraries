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

public class UnconnectedSynchronisationCaller<I> implements SynchronisationCaller<I> {

    private boolean connected;

    public UnconnectedSynchronisationCaller(boolean connected) {
        this.connected = connected;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void update(I identifier, Document data) {
        //Ignored
    }

    @Override
    public void updateAndIgnore(I identifier, Document data) {
        //Ignored
    }

    @Override
    public void create(I identifier, Document data) {
        //Ignored
    }

    @Override
    public void createAndIgnore(I identifier, Document data) {
        //Ignored
    }

    @Override
    public void delete(I identifier, Document data) {
        //Ignored
    }

    @Override
    public void deleteAndIgnore(I identifier, Document data) {
        //Ignored
    }
}
