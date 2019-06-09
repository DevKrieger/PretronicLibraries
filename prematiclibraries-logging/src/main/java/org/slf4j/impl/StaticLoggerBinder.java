/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.06.19 16:57
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

package org.slf4j.impl;

import net.prematic.libraries.logging.PrematicLogger;
import net.prematic.libraries.logging.bridge.slf4j.SLF4JBridge;
import net.prematic.libraries.logging.bridge.slf4j.SLF4JStaticBridge;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder, SLF4JStaticBridge.LoggerSetter {

    private static final StaticLoggerBinder BINDER = new StaticLoggerBinder();
    private static final Factory FACTORY = new Factory();
    private static SLF4JBridge LOGGER;

    public static final StaticLoggerBinder getSingleton() {
        return BINDER;
    }
    @Override
    public void setLogger(PrematicLogger logger){
        LOGGER = new SLF4JBridge(logger);
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return FACTORY;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return Factory.class.getName();
    }

    private static class Factory implements ILoggerFactory {

        @Override
        public Logger getLogger(String name) {
            return LOGGER;
        }
    }
}
