/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 28.03.19 15:54
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

/**
 * This class is for implementing the prematic loggeing framework in the java exception processor.
 */
public class LoggingUncaughtExceptionHandler  implements Thread.UncaughtExceptionHandler {

    private final PrematicLogger logger;

    public LoggingUncaughtExceptionHandler(PrematicLogger logger) {
        this.logger = logger;
    }

    @Override
    public void uncaughtException(Thread fromThread, Throwable thrown) {
        this.logger.error(thrown);
    }

    /**
     * Hook a prematic logger in all threads (Set as default handler).
     *
     * @param logger The logger for hooking
     */
    public static void hook(PrematicLogger logger){
        Thread.UncaughtExceptionHandler handler = new LoggingUncaughtExceptionHandler(logger);
        Thread.setDefaultUncaughtExceptionHandler(handler);
        Thread.currentThread().setUncaughtExceptionHandler(handler);
    }

    /**
     * Hook a premaic logger ro a specified thread.
     *
     * @param thread The thread in which the logger should be hooked
     * @param logger The logger for hooking
     */
    public static void hook(Thread thread, PrematicLogger logger){
        thread.setUncaughtExceptionHandler(new LoggingUncaughtExceptionHandler(logger));
    }
}
