/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 25.06.19 19:15
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

import net.prematic.libraries.logging.format.DefaultLogFormatter;
import net.prematic.libraries.logging.handler.ConsoleHandler;

import java.util.Collections;
import java.util.Objects;

public abstract class PrematicLoggerFactory {

    private static PrematicLoggerFactory FACTORY = new DefaultFactory();

    public static PrematicLoggerFactory getFactory() {
        return FACTORY;
    }

    public static PrematicLogger getLogger(){
        return FACTORY.newLogger();
    }

    public static PrematicLogger getLogger(String name){
        Objects.requireNonNull(name);
        return FACTORY.newLogger(name);
    }

    public static PrematicLogger getLogger(Class<?> clazz){
        Objects.requireNonNull(clazz);
        return FACTORY.newLogger(clazz);
    }


    public abstract PrematicLogger newLogger();

    public abstract PrematicLogger newLogger(Class<?> clazz);

    public abstract PrematicLogger newLogger(String name);



    private static class DefaultFactory extends PrematicLoggerFactory{

        private PrematicLogger logger;

        private DefaultFactory() {}

        @Override
        public PrematicLogger newLogger() {
            return newLogger("UNKNOWN");
        }

        @Override
        public PrematicLogger newLogger(Class<?> clazz) {
            return newLogger(clazz.toString());
        }

        @Override
        public PrematicLogger newLogger(String name) {
            if(logger == null){
                logger = new AsyncPrematicLogger(name,new DefaultLogFormatter(), Collections.singletonList(new ConsoleHandler()));
            }
            return logger;
        }
    }
}
