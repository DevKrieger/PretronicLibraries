/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:39
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

package net.pretronic.libraries.utility.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public class AsyncExecutor {

    private final ExecutorService service;

    public AsyncExecutor(ExecutorService service) {
        this.service = service;
    }

    public ExecutorService getService() {
        return service;
    }

    public <T> CompletableFuture<T> execute(Supplier<T> execution){
        CompletableFuture<T> future = new CompletableFuture<>();
        service.execute(() -> {
            try{
                future.complete(execution.get());
            }catch (Exception exception){
                future.completeExceptionally(exception);
            }
        });
        return future;
    }

    public CompletableFuture<Void> executeVoid(Runnable execution){
        CompletableFuture<Void> future = new CompletableFuture<>();
        service.execute(() -> {
            try{
                execution.run();
                future.complete(null);
            }catch (Exception exception){
                future.completeExceptionally(exception);
            }
        });
        return future;
    }
}
