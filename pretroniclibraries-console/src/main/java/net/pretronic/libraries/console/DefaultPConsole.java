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

package net.pretronic.libraries.console;

import net.pretronic.libraries.console.terminal.Terminal;
import net.pretronic.libraries.console.terminal.TerminalFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class DefaultPConsole implements PConsole {

    private final Terminal terminal;
    private final Cursor cursor;

    private final InputStream inputStream;
    private final OutputStream outputStream;

    private final Map<Integer, Consumer<StringBuilder>> keyEventListeners;
    private final Collection<TabCompleter> tabCompleters;

    private String prompt;

    private final StringBuilder input;

    public DefaultPConsole() {
        this(TerminalFactory.get());
    }

    public DefaultPConsole(Terminal terminal) {
        this(terminal,System.in,System.out);
    }

    public DefaultPConsole(InputStream inputStream, OutputStream outputStream) {
        this(TerminalFactory.get(),inputStream,outputStream);
    }

    public DefaultPConsole(Terminal terminal, InputStream inputStream, OutputStream outputStream) {
        this.terminal = terminal;
        this.inputStream = inputStream;
        this.outputStream = outputStream;

        this.keyEventListeners = new LinkedHashMap<>();
        this.tabCompleters = new HashSet<>();

        this.cursor = new Cursor(this);
        input = new StringBuilder();

        terminal.initialise();
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public Terminal getTerminal() {
        return terminal;
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public Collection<TabCompleter> getTabCompleters() {
        return tabCompleters;
    }

    @Override
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public int read() throws IOException {
        int read = inputStream.read();
        if(inputStream.available() > 0 && read == 27 && inputStream.read() == 91){
            while(inputStream.available() > 0) read = inputStream.read();
            if(read == 65) read = 38;
            else if(read == 66) read = 40;
            else if(read == 67) read = 39;
            else if(read == 68) read = 37;
            else read = 0;
        }
        return read;
    }

    @Override
    public char readCharacter() throws IOException{
        return (char)read();
    }

    @Override
    public String readLine(String prompt, char mask) throws IOException{
        try{
            if(prompt != null) print(prompt);
            int key;
            while((key = read()) != Keys.ENTER.code()){
                char c = (char)key;
                Consumer<StringBuilder> eventListener = this.keyEventListeners.get(key);
                if(eventListener != null) eventListener.accept(input);
                else if(Character.isDefined(c)){
                    if(mask != 0) print(mask);
                    else print(c);
                    input.append(c);
                }
            }
            print(System.lineSeparator());
            return input.toString();
        }finally {
            input.setLength(0);
        }
    }

    @Override
    public void print(char char0) {
        try {
            write(char0);
            flush();
        } catch (IOException exception) {
            throw new IllegalArgumentException("Could not print a message.",exception);
        }
    }

    @Override
    public void print(CharSequence sequence) {
        try {
            write(sequence);
            flush();
        } catch (IOException exception) {
            throw new IllegalArgumentException("Could not print a message.",exception);
        }
    }

    @Override
    public void println(CharSequence sequence) {
        try {
            write("\\u001b[1000D");
            write(sequence);

            int length = prompt.length()+input.length();

            for(int i = 0;i<length;i++) write(' ');

            newLine();

            write(prompt);
            write(input);


            flush();
        } catch (IOException exception) {
            throw new IllegalArgumentException("Could not print a message.",exception);
        }
    }

    @Override
    public void write(char char0) throws IOException {
        outputStream.write(char0);
    }

    @Override
    public void write(CharSequence sequence) throws IOException {
        outputStream.write(sequence.toString().getBytes());
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void newLine() {
        try {
            write(System.lineSeparator());
        } catch (IOException exception) {
            throw new UnsupportedOperationException("Could not create a new line",exception);
        }
    }

    @Override
    public void resetLine() {

    }

    @Override
    public void resetLastLine() {

    }

    @Override
    public void backspace() {
        try {
            write('\b');
        } catch (IOException exception) {
            throw new UnsupportedOperationException("Could not write a backspace char.",exception);
        }
    }

    @Override
    public void backspace(int count) {
        try {
            for(int i = 0;i<count;i++)  write('\b');
        } catch (IOException exception) {
            throw new UnsupportedOperationException("Could not write a backspace char.",exception);
        }
    }

    @Override
    public void printAnsiSequence(CharSequence sequence) {
        try {
            write("\u001b["+sequence);
        } catch (IOException exception) {
            throw new UnsupportedOperationException("Could not write ansi sequence.",exception);
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public void look() {

    }

    @Override
    public void unLook() {

    }

    @Override
    public void addTabCompleter(Function<String, List<String>> completer) {

    }

    @Override
    public void removeTabCompleter(Function<String, List<String>> completer) {

    }

    @Override
    public void addKeyEventListener(int keyCode, Consumer<StringBuilder> listener) {
        this.keyEventListeners.put(keyCode,listener);
    }

    @Override
    public void reset() {
        cursor.reset();
    }

}
