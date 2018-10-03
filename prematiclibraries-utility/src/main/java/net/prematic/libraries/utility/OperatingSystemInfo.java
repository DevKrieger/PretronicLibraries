package net.prematic.libraries.utility;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 21.09.18 19:46
 *
 */

public class OperatingSystemInfo {

    private String name, version, architecture, javaVersion;

    public OperatingSystemInfo(String name, String version, String architecture, String javaVersion) {
        this.name = name;
        this.version = version;
        this.architecture = architecture;
        this.javaVersion = javaVersion;
    }
    public String getName() {
        return this.name;
    }
    public String getVersion() {
        return this.version;
    }
    public String getArchitecture() {
        return this.architecture;
    }
    public String getJavaVersion() {
        return this.javaVersion;
    }
    public static OperatingSystemInfo build(){
        return new OperatingSystemInfo(System.getProperty("os.name"),System.getProperty("os.version")
                ,System.getProperty("os.arch"),System.getProperty("java.version"));
    }
}
