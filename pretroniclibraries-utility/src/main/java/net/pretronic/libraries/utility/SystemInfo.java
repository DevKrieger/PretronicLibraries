package net.pretronic.libraries.utility;

public final class SystemInfo {

    private static final String OS_NAME = System.getProperty("os.name");
    private static final String OS_VERSION = System.getProperty("os.version");
    private static final String OS_ARCH = System.getProperty("os.arch");

    public static String getOsName() {
        return OS_NAME;
    }

    public static String getOsVersion() {
        return OS_VERSION;
    }

    public static String getOsArch() {
        return OS_ARCH;
    }

    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static long getAllocatedMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getTotalFreeMemory() {
        return getFreeMemory() + (getMaxMemory() - getAllocatedMemory());
    }
}
