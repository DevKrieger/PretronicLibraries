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

import java.util.LinkedHashMap;
import java.util.Map;

public class PrematicLogggerFactory {

    private static LoggerCreator CREATOR;

    public static LoggerCreator getCreator(){
        if(CREATOR == null) CREATOR = new SimpleCreator();
        return CREATOR;
    }

    public static PrematicLogger getLogger(){
        return getCreator().getLogger();
    }

    public static PrematicLogger getLogger(String name){
        return getCreator().getLogger(name);
    }

    public static PrematicLogger getLogger(Class<?> clazz){
        return getCreator().getLogger(clazz);
    }

    public static PrematicLogger getLogger(String name, Class<?> clazz){
        return getCreator().getLogger(name, clazz);
    }

    public static void setCreator(LoggerCreator creator){
        CREATOR = creator;
    }

    public interface LoggerCreator {

        PrematicLogger getLogger();

        PrematicLogger getLogger(String name);

        PrematicLogger getLogger(Class<?> clazz);

        PrematicLogger getLogger(String name, Class<?> clazz);

    }

    public static void main(String[] args){
        getLogger();
    }

    public static class SimpleCreator implements LoggerCreator{

        private final Map<String,PrematicLogger> loggers;

        public SimpleCreator() {
            this.loggers = new LinkedHashMap<>();
        }

        @Override
        public PrematicLogger getLogger() {
            return getLogger("UNKNOWN");
        }

        @Override
        public PrematicLogger getLogger(String name) {
            PrematicLogger logger =  loggers.get(name);
            if(logger == null){
                logger = new SimplePrematicLogger(name);
                this.loggers.put(name,logger);
            }
            return logger;
        }

        @Override
        public PrematicLogger getLogger(Class<?> clazz) {
            return getLogger(clazz.getName());
        }

        @Override
        public PrematicLogger getLogger(String name, Class<?> clazz) {
            return getLogger(name);
        }

    }

    public static class LogGetter implements LoggerCreator{

        private final PrematicLogger logger;

        public LogGetter(PrematicLogger logger) {
            this.logger = logger;
        }

        @Override
        public PrematicLogger getLogger() {
            return logger;
        }

        @Override
        public PrematicLogger getLogger(String name) {
            return logger;
        }

        @Override
        public PrematicLogger getLogger(Class<?> clazz) {
            return logger;
        }

        @Override
        public PrematicLogger getLogger(String name, Class<?> clazz) {
            return logger;
        }
    }

}
