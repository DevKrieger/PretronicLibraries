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

package net.pretronic.libraries.utility;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public final class SystemUtil {

    public static void sleepUninterruptible(long time, TimeUnit unit){
        sleepUninterruptible(unit.toMillis(time));
    }

    public static void sleepUninterruptible(long millis){
        try{ Thread.sleep(millis);
        }catch (Exception ignored){}
    }

    public static void sleepUntil(BooleanSupplier finished){
        while(!finished.getAsBoolean()){
            try {
                Thread.sleep(0,250000);
            } catch (InterruptedException ignored) {}
        }
    }

    public static void sleepAsLong(BooleanSupplier finished){
        while(finished.getAsBoolean()){
            try { Thread.sleep(0,250000);
            } catch (InterruptedException ignored) {}
        }
    }

    public static String getJavaVersion(){
        return System.getProperty("java.version");
    }

    public static int getJavaVersionNumber(){
        return Integer.parseInt(getJavaVersion().replace(".",""));
    }

    public static int getJavaBaseVersion() {
        String version = getJavaVersion();
        if(version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if(dot != -1) { version = version.substring(0, dot); }
        } return Integer.parseInt(version);
    }

}
