package net.prematic.libraries.utility;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 21.09.18 20:08
 *
 */

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

public class SystemUtil {

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
    public static int getAvalibalCores(){
        return Runtime.getRuntime().availableProcessors();
    }
}
