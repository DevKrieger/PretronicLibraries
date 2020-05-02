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

package net.pretronic.libraries.synchronisation.observer;

import java.util.Collections;
import java.util.List;

public class UnusedObservable<O extends Observable<O,T>,T> implements Observable<O,T>{

    @Override
    public List<ObserveCallback<O,T>> getObservers() {
        return Collections.emptyList();
    }

    @Override
    public boolean isObserverSubscribed(ObserveCallback<O, T> callback) {
        return false;//Unused without error
    }

    @Override
    public void subscribeObserver(ObserveCallback<O, T> callback) {
        //Unused without error
    }

    @Override
    public void unsubscribeObserver(ObserveCallback<O, T> callback) {
        //Unused without error
    }
}
