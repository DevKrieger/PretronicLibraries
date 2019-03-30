/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 26.03.19 13:46
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

package net.prematic.libraries.logging;

import java.util.concurrent.BlockingQueue;

/**
 * This object is a async que runner, it is made for printing log messages in an async thread.
 *
 * <p>This is a daemon thread with minimal priority.</p>
 */
public class AsyncQueuePrinter extends Thread {

    private final BlockingQueue<Runnable> queue;

    public AsyncQueuePrinter(BlockingQueue<Runnable> queue){
        super("PrematicQueuePrinter");
        this.queue = queue;
        setPriority(Thread.MIN_PRIORITY);
        setDaemon(true);
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                Runnable runnable = queue.take();
                runnable.run();
            }catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
