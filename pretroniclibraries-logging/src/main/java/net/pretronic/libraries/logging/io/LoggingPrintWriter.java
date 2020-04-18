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

package net.pretronic.libraries.logging.io;

import net.pretronic.libraries.logging.MessageInfo;
import net.pretronic.libraries.logging.PretronicLogger;
import net.pretronic.libraries.logging.level.LogLevel;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * The @{@link LoggingPrintWriter} is for hooking the default java output into the pretronic logger.
 */
public class LoggingPrintWriter extends PrintWriter {

    private final PretronicLogger logger;
    private final LogLevel level;
    private final MessageInfo info;

    public LoggingPrintWriter(PretronicLogger logger){
        this(logger,LogLevel.INFO);
    }

    public LoggingPrintWriter(PretronicLogger logger, LogLevel level){
        this(logger,level,null);
    }

    public LoggingPrintWriter(PretronicLogger logger, LogLevel level, MessageInfo info){
        this(logger,level,info,System.out);
    }


    public LoggingPrintWriter(PretronicLogger logger, LogLevel level, MessageInfo info, OutputStream outputStream){
        super(outputStream);
        this.logger = logger;
        this.level = level;
        this.info = info;
    }

    @Override
    public void flush() {
        super.flush();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean checkError() {
        return super.checkError();
    }

    @Override
    protected void setError() {
        super.setError();
    }

    @Override
    protected void clearError() {
        super.clearError();
    }

    @Override
    public void write(int c) {
        super.write(c);
    }

    @Override
    public void write(char[] buf, int off, int len) {
        super.write(buf, off, len);
    }

    @Override
    public void write(char[] buf) {
        super.write(buf);
    }

    @Override
    public void write(String s, int off, int len) {
        super.write(s, off, len);
    }

    @Override
    public void write(String s) {
        super.write(s);
    }

    @Override
    public void print(boolean b) {
        this.logger.log(info,level,b);
    }

    @Override
    public void print(char c) {
        this.logger.log(info,level,c);
    }

    @Override
    public void print(int i) {
        this.logger.log(info,level,i);
    }

    @Override
    public void print(long l) {
        this.logger.log(info,level,l);
    }

    @Override
    public void print(float f) {
        this.logger.log(info,level,f);
    }

    @Override
    public void print(double d) {
        this.logger.log(info,level,d);
    }

    @Override
    public void print(char[] s) {
        this.logger.log(info,level,s);
    }

    @Override
    public void print(String s) {
        this.logger.log(info,level,s);
    }

    @Override
    public void print(Object obj) {
        this.logger.log(info,level,obj);
    }

    @Override
    public void println() {
        this.logger.log(info,level,"");
    }

    @Override
    public void println(boolean x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(char x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(int x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(long x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(float x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(double x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(char[] x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(String x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(Object x) {
        this.logger.log(info,level,x);
    }
}
