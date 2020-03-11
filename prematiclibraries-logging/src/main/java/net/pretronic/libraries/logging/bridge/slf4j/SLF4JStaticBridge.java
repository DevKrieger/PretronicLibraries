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
import org.slf4j.impl.StaticLoggerBinder;

public class SLF4JStaticBridge {

    public static LoggerSetter SETTER;

    public static void setLogger(PretronicLogger logger){
        SETTER = detectSetter();
        if(SETTER == null) throw new IllegalArgumentException("SLF4J is not installed.");
        SETTER.setLogger(logger);
    }

    public static void trySetLogger(PretronicLogger logger){
       try{
           setLogger(logger);
       }catch (Exception ignored){}
    }

    private static LoggerSetter detectSetter(){
        try {
            Class.forName("org.slf4j.spi.SLF4JServiceProvider");
            return SLF4JBridgeServiceProvider.PROVIDER;
        } catch (ClassNotFoundException ignored) {
            try {
                Class.forName("org.slf4j.LoggerFactory");
                return StaticLoggerBinder.getSingleton();
            } catch (ClassNotFoundException ignored2) {}
        }
        return null;
    }

    public interface LoggerSetter{

        void setLogger(PretronicLogger logger);

    }
}
