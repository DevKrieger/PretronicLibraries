/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 06.04.19 16:58
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

package net.prematic.libraries.console;

import net.prematic.libraries.console.terminal.Terminal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface PConsole {

    char PASSWORD_MASK = '*';

    Terminal getTerminal();

    InputStream getInputStream();

    OutputStream getOutputStream();


    Cursor getCursor();

    String getPrompt();

    Collection<TabCompleter> getTabCompleters();

    void setPrompt(String prompt);

    int read() throws IOException;

    char readCharacter() throws IOException;

    default String readLine() throws IOException {
        return readLine(getPrompt());
    }

    default String readLine(char mask) throws IOException {
        return readLine(getPrompt(),mask);
    }

    default String readLine(String prompt) throws IOException {
        return readLine(prompt,(char)0);
    }

    String readLine(String prompt, char mask) throws IOException;

    default String readPassword() throws IOException{
        return readLine(PASSWORD_MASK);
    }

    void print(char char0);

    void print(CharSequence sequence);

    void println(CharSequence sequence);

    void write(char char0) throws IOException;

    void write(CharSequence sequence) throws IOException;

    void flush() throws IOException;

    void newLine();

    void resetLine();

    void resetLastLine();

    void backspace();

    void backspace(int count);

    void printAnsiSequence(CharSequence sequence);

    void clear();

    void look();

    void unLook();

    void addTabCompleter(Function<String, List<String>> completer);

    void removeTabCompleter(Function<String, List<String>> completer);

    default void addKeyEventListener(Keys key, Consumer<StringBuilder> listener){
        addKeyEventListener(key.code(), listener);
    }

    void addKeyEventListener(int keyCode, Consumer<StringBuilder> listener);

    void reset();

    class SimpleStringCompleter implements Function<String, List<String>> {

        public final List<String> suggestions;

        public SimpleStringCompleter(String... suggestions) {
            this(Arrays.asList(suggestions));
        }

        public SimpleStringCompleter(List<String> suggestions) {
            this.suggestions = suggestions;
        }

        @Override
        public List<String> apply(String a) {
            final String search = a.toLowerCase();
            List<String> result = new ArrayList<>();
            suggestions.forEach(suggestion -> {
                if(suggestion.toLowerCase().startsWith(search))  result.add(suggestion);
            });
            return result;
        }
    }
}
