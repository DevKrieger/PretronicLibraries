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

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public final class SystemUtil {

    @Deprecated
    public static void sleepUnInterrupt(long time, TimeUnit unit){
       sleepUnInterrupt(unit.toNanos(time));
    }

    @Deprecated
    public static void sleepUnInterrupt(long millis){
        try{
            Thread.sleep(millis);
        }catch (Exception exception){}
    }

    @Deprecated
    public static void sleepUnInterrupt(int nanos){
        try{ Thread.sleep(0,nanos); }catch (Exception exception){}
    }



    public static void sleepUninterruptibly(long time, TimeUnit unit){
        sleepUninterruptibly(unit.toMillis(time));
    }


    public static void sleepUninterruptibly(long millis){
        try{
            Thread.sleep(millis);
        }catch (Exception ignored){}
    }

    public static void sleepUntil(Supplier<Boolean> finished){
        while(!finished.get()){
            try {
                Thread.sleep(0,250000);
            } catch (InterruptedException ignored) {}
        }
    }

    public static void sleepAsLong(Supplier<Boolean> finished){
        while(finished.get()){
            try {
                Thread.sleep(0,250000);
            } catch (InterruptedException ignored) {}
        }
    }










    public static double getCpuUsage() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad()*100;
    }

    public static double getProcessCpuUsage() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getProcessCpuLoad()*100;
    }

    public static long getProcessMaxMemory(){
        return Runtime.getRuntime().maxMemory()/(1024*1024);
    }

    public static long getProccessUsedMemory(){
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed()/(1024*1024);
    }

    public static long getSystemMemory() {
        return Runtime.getRuntime().totalMemory()/(1024*1024);
    }

    public static long getFreeSystemMemory() {
        return Runtime.getRuntime().freeMemory()/(1024*1024);
    }

    public static long getUsedSystemMemory() {
        return getSystemMemory()-getFreeSystemMemory();
    }

}
