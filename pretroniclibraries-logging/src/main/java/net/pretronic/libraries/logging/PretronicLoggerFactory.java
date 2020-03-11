/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:45
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

package net.pretronic.libraries.logging;

import net.pretronic.libraries.logging.format.DefaultLogFormatter;
import net.pretronic.libraries.logging.handler.ConsoleHandler;

import java.util.Collections;
import java.util.Objects;

public abstract class PretronicLoggerFactory {

    private static PretronicLoggerFactory FACTORY = new DefaultFactory();

    public static PretronicLoggerFactory getFactory() {
        return FACTORY;
    }

    public static PretronicLogger getLogger(){
        return FACTORY.newLogger();
    }

    public static PretronicLogger getLogger(String name){
        Objects.requireNonNull(name);
        return FACTORY.newLogger(name);
    }

    public static PretronicLogger getLogger(Class<?> clazz){
        Objects.requireNonNull(clazz);
        return FACTORY.newLogger(clazz);
    }


    public abstract PretronicLogger newLogger();

    public abstract PretronicLogger newLogger(Class<?> clazz);

    public abstract PretronicLogger newLogger(String name);



    private static class DefaultFactory extends PretronicLoggerFactory {

        private PretronicLogger logger;

        private DefaultFactory() {}

        @Override
        public PretronicLogger newLogger() {
            return newLogger("UNKNOWN");
        }

        @Override
        public PretronicLogger newLogger(Class<?> clazz) {
            return newLogger(clazz.toString());
        }

        @Override
        public PretronicLogger newLogger(String name) {
            if(logger == null){
                logger = new AsyncPretronicLogger(name,new DefaultLogFormatter(), Collections.singletonList(new ConsoleHandler()));
            }
            return logger;
        }
    }
}
