/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:42
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

package net.pretronic.libraries.console.terminal;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class UnixTerminal implements Terminal{

    public static final String DEFAULT_STTY_COMMAND = "stty";
    public static final String DEFAULT_SH_COMMAND = "sh";
    public static final String DEFAULT_TEXT_ONLY_CONSOLE_DEVICE = "/dev/tty";

    //Copied from Jline jline.internal.TerminalLineSettings.class
    public static final boolean SUPPORTS_REDIRECT;
    public static final Object REDIRECT_INHERIT;
    public static final Method REDIRECT_INPUT_METHOD;

    static {

        //Copied from Jline jline.internal.TerminalLineSettings.class
        boolean supportsRedirect;
        Object redirectInherit = null;
        Method redirectInputMethod = null;
        try {
            Class<?> redirect = Class.forName("java.lang.ProcessBuilder$Redirect");
            redirectInherit = redirect.getField("INHERIT").get(null);
            redirectInputMethod = ProcessBuilder.class.getMethod("redirectInput", redirect);
            supportsRedirect = System.class.getMethod("console").invoke(null) != null;
        } catch (Throwable t) {
            supportsRedirect = false;
        }
        SUPPORTS_REDIRECT = supportsRedirect;
        REDIRECT_INHERIT = redirectInherit;
        REDIRECT_INPUT_METHOD = redirectInputMethod;
    }

    public final String textOnlyConsoleDevice, sttyCommand, shCommand, defaultSttyConfig;
    public final boolean useRedirect;
    public String sttyConfig;
    public long lastConfigLoadTime;

    public UnixTerminal(){
        this(DEFAULT_TEXT_ONLY_CONSOLE_DEVICE);
    }

    public UnixTerminal(String textOnlyConsoleDevice){
        this(textOnlyConsoleDevice,SUPPORTS_REDIRECT);
    }

    public UnixTerminal(String textOnlyConsoleDevice, boolean useRedirect) {
        this(textOnlyConsoleDevice,useRedirect,DEFAULT_STTY_COMMAND,DEFAULT_SH_COMMAND);
    }

    public UnixTerminal(String textOnlyConsoleDevice,boolean useRedirect, String sttyCommand, String shCommand) {
        this.textOnlyConsoleDevice = textOnlyConsoleDevice;
        this.sttyCommand = sttyCommand;
        this.shCommand = shCommand;
        this.useRedirect = useRedirect;

        this.defaultSttyConfig = getStty("-g").trim();
    }

    @Override
    public int getWidth() {
        int result = getIntProperty("columns");
        if(result <= 0) return UnknownTerminal.DEFAULT_WIDTH;
        return result;
    }

    @Override
    public int getHeight() {
        int result = getIntProperty("rows");
        if(result <= 0) return UnknownTerminal.DEFAULT_HEIGHT;
        return result;
    }

    public String getSttyConfig(){
        if(sttyConfig == null || lastConfigLoadTime < System.currentTimeMillis()){
            this.sttyConfig = getStty("-a");
            this.lastConfigLoadTime = System.currentTimeMillis()+1500;
        }
        return this.sttyConfig;
    }

    @Override
    public boolean isAvailable() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        return !(os.contains("win") || os.contains("osv"));
    }

    @Override
    public boolean isAnsiSupported() {
        return isAvailable();
    }

    @Override
    public void initialise() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

        //Execute for FreeBSD or for another unix operating system.
        if(os.contains("freebsd")) setStty("-icanon min 1 -inlcr -ixon");
        else setStty("-icanon min 1 -icrnl -inlcr -ixon");

        //Execute for HpUx or for another operating system.
        if(os.contains("hp")) setStty("dsusp","^-");
        else setStty("dsusp","undef");

        //Disable console echo
        setStty("-echo");

        System.out.println("ready");
    }

    @Override
    public void reset() {
        setStty(defaultSttyConfig);
        System.out.println();
    }

    public String getStty(final String args) {
        return getStty(args.split(" "));
    }

    //Copied and edited from Jline jline.internal.TerminalLineSettings.class
    public String getStty(final String... args) {
        try{
            String[] command = new String[args.length + 1];
            command[0] = sttyCommand;
            System.arraycopy(args, 0, command, 1, args.length);

            Process process = null;
            if (useRedirect) {
                try {
                    ProcessBuilder builder = new ProcessBuilder(command);
                    REDIRECT_INPUT_METHOD.invoke(builder, REDIRECT_INHERIT);
                    process = builder.start();
                } catch (Exception ignored) {}
            }
            if(process == null) {
                StringBuilder builder = new StringBuilder();
                for(int i = 0; i < command.length; i++) {
                    if (i > 0) builder.append(' ');
                    builder.append(command[i]);
                }
                builder.append(" < ");
                builder.append(textOnlyConsoleDevice);
                process = new ProcessBuilder(shCommand, "-c",builder.toString()).start();
            }

            ByteArrayOutputStream content = new ByteArrayOutputStream();
            InputStream input = null;
            InputStream error = null;
            OutputStream output = null;
            try {
                int in;
                input = process.getInputStream();
                while ((in = input.read()) != -1) content.write(in);
                error = process.getErrorStream();
                while ((in = error.read()) != -1) content.write(in);

                output = process.getOutputStream();
                process.waitFor();
            }
            finally {
                if(input != null) input.close();
                if(output != null) output.close();
                if(error != null) error.close();
            }
            return content.toString();
        }catch (Exception exception){
            throw new UnsupportedOperationException("Could not execute Stty command.",exception);
        }
    }

    public void setStty(final String args) {
        getStty(args);
    }


    public void setStty(final String... args) {
        getStty(args);
    }

    public int getIntProperty(String name){
        return Integer.valueOf(getProperty(name));
    }

    //Copied from Jline jline.internal.TerminalLineSettings.class
    public String getProperty(String name){
        try{
            String stty = getSttyConfig();
            Pattern pattern = Pattern.compile(name + "\\s+=\\s+(.*?)[;\\n\\r]");
            Matcher matcher = pattern.matcher(stty);
            if (!matcher.find()) {
                // try a second kind of regex
                pattern = Pattern.compile(name + "\\s+([^;]*)[;\\n\\r]");
                matcher = pattern.matcher(stty);
                if (!matcher.find()) {
                    // try a second try of regex
                    pattern = Pattern.compile("(\\S*)\\s+" + name);
                    matcher = pattern.matcher(stty);
                    if (!matcher.find()) {
                        return null;
                    }
                }
            }
            return matcher.group(1);
        }catch (Exception ignored){}
        return null;
    }
}


