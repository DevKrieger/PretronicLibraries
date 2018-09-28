package net.prematic.libraries.utility;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 21.09.18 19:46
 *
 */

public class OperatingSystemInfo {

    private String name, version, architecture;

    public OperatingSystemInfo(String name, String version, String architecture) {
        this.name = name;
        this.version = version;
        this.architecture = architecture;
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
    public static OperatingSystemInfo build(){
        return new OperatingSystemInfo(System.getProperty("os.name")
        ,System.getProperty("os.version"),System.getProperty("os.arch"));
    }
}
