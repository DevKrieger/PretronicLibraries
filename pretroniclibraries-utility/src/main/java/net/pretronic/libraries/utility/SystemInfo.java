package net.pretronic.libraries.utility;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Base64;
import java.util.Enumeration;

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

    public static String getDeviceId(){
        try{
            ByteBuf buffer = Unpooled.directBuffer();
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()){
                NetworkInterface networkInterface = interfaces.nextElement();
                if(networkInterface.getHardwareAddress() != null){
                    buffer.writeBytes(networkInterface.getHardwareAddress());
                }
            }
            byte[] result = new byte[buffer.readableBytes()];
            buffer.readBytes(result);
            return Base64.getEncoder().encodeToString(result);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    public static double getProcessCpuLoad() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
            AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

            if (list.isEmpty())     return 0;

            Attribute att = (Attribute)list.get(0);
            Double value  = (Double)att.getValue();
            if(value < 0) return 0;
            return value;
        } catch (MalformedObjectNameException | InstanceNotFoundException | ReflectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static double getPercentProcessCpuLoad(int precision) {

        if(precision == 0) {
            return ((int)(getProcessCpuLoad() * 1000) / 10);
        } else {
            int multiplier = (int) (1000*Math.pow(10, precision-1));
            double divider = 10.0*Math.pow(10, precision-1);
            return ((int)(getProcessCpuLoad() * multiplier) / divider);
        }

    }
}
