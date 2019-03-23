package net.prematic.libraries.utility;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.02.19 16:17
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

public final class SystemUtil {

    public static void sleepUnInterrupt(long time, TimeUnit unit){
       sleepUnInterrupt(unit.toNanos(time));
    }

    public static void sleepUnInterrupt(long millis){
        try{
            Thread.sleep(millis);
        }catch (Exception exception){}
    }

    public static void sleepUnInterrupt(int nanos){
        try{ Thread.sleep(0,nanos); }catch (Exception exception){}
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
