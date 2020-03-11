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

package net.pretronic.libraries.logging.bridge.slf4j;

import net.pretronic.libraries.logging.PretronicLogger;
import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

import java.lang.reflect.Field;

public class SLF4JBridgeServiceProvider implements SLF4JServiceProvider, SLF4JStaticBridge.LoggerSetter {

    public static final SLF4JBridgeServiceProvider PROVIDER = new SLF4JBridgeServiceProvider();
    private static final Factory FACTORY = new Factory();
    private static SLF4JBridge LOGGER;

    public SLF4JBridgeServiceProvider() {
        try{
            Field providerField = LoggerFactory.class.getDeclaredField("PROVIDER");
            providerField.setAccessible(true);
            providerField.set(null,this);

            Field stateField = LoggerFactory.class.getDeclaredField("INITIALIZATION_STATE");
            stateField.setAccessible(true);
            stateField.set(null,3);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new IllegalArgumentException("Could not initialize Pretronic SLF4J static logger bridge adapter.");
        }
    }

    @Override
    public void setLogger(PretronicLogger logger) {
        LOGGER =  new SLF4JBridge(logger);
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return FACTORY;
    }

    @Override
    public IMarkerFactory getMarkerFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MDCAdapter getMDCAdapter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRequesteApiVersion() {
        return "1.8";
    }

    @Override
    public void initialize() {
        //This updater requires no initialization.
    }

    private static class Factory implements ILoggerFactory {

        @Override
        public Logger getLogger(String name) {
            return LOGGER;
        }
    }
}
