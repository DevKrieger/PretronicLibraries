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

import net.pretronic.libraries.utility.Iterators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class AbstractObservable<O extends Observable<O,T>,T> implements Observable<O,T>{

    private final List<ObserveCallback<O, T>> callbacks;

    protected AbstractObservable() {
        this(new ArrayList<>());
    }

    protected AbstractObservable(List<ObserveCallback<O, T>> callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public List<ObserveCallback<O,T>> getObservers() {
        return Collections.unmodifiableList(callbacks);
    }

    @Override
    public boolean isObserverSubscribed(ObserveCallback<O, T> callback) {
        return this.callbacks.contains(callback);
    }

    @Override
    public void subscribeObserver(ObserveCallback<O, T> callback) {
        this.callbacks.add(callback);
    }

    @Override
    public void unsubscribeObserver(ObserveCallback<O, T> callback) {
        Iterators.removeOne(this.callbacks, callback1 -> callback1.equals(callback));
    }

    @SuppressWarnings("unchecked")
    public void callObservers(T arg){
        callObservers((O) this,arg);
    }

    public void callObservers(O object, T arg){
        for (ObserveCallback<O, T> callback : getObservers()) callback.callback(object,arg);
    }
}
