/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:39
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

package net.pretronic.libraries.utility.parser;

import net.pretronic.libraries.utility.io.FileUtil;
import net.pretronic.libraries.utility.io.IORuntimeException;
import net.pretronic.libraries.utility.map.Pair;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

public class StringParser {

    private static final char[] EMPTY_LINE = new char[]{' ',' '};

    private final char[][] lines;
    private int lineIndex;
    private int charIndex;

    //Constructor

    public StringParser(File location) {
        this(location,null);
    }

    public StringParser(File location, Charset charset) {
        InputStream inputStream = FileUtil.newFileInputStream(location);
        this.lines = readLinesFromStream(inputStream, charset);
        resetIndex();
        try {
            inputStream.close();
        } catch (IOException ignored) {}

    }

    public StringParser(InputStream stream){
        this(stream,null);
    }

    public StringParser(InputStream stream, Charset charset) {
        this.lines = readLinesFromStream(stream, charset);
        resetIndex();
    }

    public StringParser(BufferedReader reader)  {
        this.lines = readLinesFromBuffer(reader);
        resetIndex();
    }

    public StringParser(String content){
        String[] rawLines = content.split("\n");

        this.lines = new char[rawLines.length][];
        for(int i = 0;i<rawLines.length;i++) this.lines[i] = rawLines[i].toCharArray();
        resetIndex();
    }

    public StringParser(char[][] lines) {
        this.lines = lines;
        resetIndex();
    }

    //Index operations

    public int lineIndex(){
        return this.lineIndex;
    }

    public int charIndex(){
        return this.charIndex;
    }

    public void setCharIndex(int charIndex){
        this.charIndex = charIndex;
    }

    public void setLineIndex(int lineIndex){
        this.lineIndex = lineIndex;
    }

    public void setIndex(int lineIndex, int charIndex){
        this.lineIndex = lineIndex;
        this.charIndex = charIndex;
    }

    public void resetIndex(){
        this.lineIndex = -1;
        this.charIndex = -1;
    }

    public void setPosition(int position){
        int newLineIndex = 0;
        while(position+1 > this.lines[newLineIndex].length){
            position -= this.lines[newLineIndex].length;
            newLineIndex++;
        }
        setIndex(newLineIndex,position);
    }


    //Index Checks (Check all)

    @Deprecated
    public boolean hasNext(){
        return hasNextChar();
    }

    public boolean hasNextChar(){
        return hasChar(lineIndex,charIndex+1) || hasLine(lineIndex+1);
    }

    public boolean hasNextLine(){
        return hasLine(this.lineIndex);
    }

    @Deprecated
    public boolean has(int lineIndex, int charIndex){
        return hasChar(lineIndex,charIndex) || hasLine(lineIndex);
    }

    public boolean hasChar(int lineIndex,int charIndex){
        return lineIndex >= 0 && lines.length>lineIndex && lines[lineIndex].length > charIndex;
    }

    public boolean hasLine(int lineIndex){
        return lines.length>lineIndex;
    }

    public boolean isEmpty(){
        return this.lines.length == 0;
    }


    //Line operation

    public char[][] getLines(){
        return this.lines;
    }

    public String[] lines(){
        String[] lines = new String[this.lines.length];
        for(int i = 0;i<lines.length;i++){
            lines[i] = new String(this.lines[i]);
        }
        return lines;
    }

    public String line(){
        return line(this.lineIndex);
    }

    public String line(int lineIndex){
        return new String(this.lines[lineIndex]);
    }

    public String nextLine(){
        this.lineIndex++;
        this.charIndex = -1;
        return line();
    }

    public String currentUntilNextLine() {
        String result = get(lineIndex,charIndex,lines[lineIndex].length);
        this.lineIndex++;
        this.charIndex = -1;
        return result;
    }

    public void skipLine(){
        skipLines(1);
    }

    public void skipLines(int amount){
        this.lineIndex += amount;
        this.charIndex = -1;
    }

    public void previousLine(){
        previousLine(1);
    }

    public void previousLine(int amount){
        this.lineIndex = this.lineIndex-amount;
        if(hasLine(this.lineIndex)) this.charIndex = this.lines[lineIndex].length-1;
        else this.charIndex = -1;
    }

    public boolean isLineFinished(){
        return charIndex+1 >= lines[lineIndex].length;
    }


    //Char operation

    public char currentChar(){
        return charAt(this.charIndex);
    }

    public char charAt(int index){
        return charAt(this.lineIndex,index);
    }

    public char charAt(int lineIndex, int charIndex){
        return this.lines[lineIndex][charIndex];
    }

    public void skipChar(){
        skipChars(1);
    }

    public char nextChar(){
        if(lineIndex >= 0 && this.lines[this.lineIndex].length > charIndex+1) charIndex++;
        else{
            lineIndex++;
            charIndex = 0;
        }
        return this.lines[this.lineIndex][this.charIndex];
    }

    public void skipChars(int amount){
        if(this.lineIndex < 0) skipLine();
        int newLineIndex = this.lineIndex;
        int newCharIndex = this.charIndex+amount;

        while(newCharIndex >= lines[newLineIndex].length){
            newLineIndex++;
            newCharIndex = newCharIndex-lines[newLineIndex-1].length;
        }
        setIndex(newLineIndex,newCharIndex);
    }

    public void skipChars(char c){
        skipChars(c,null);
    }


    public void skipChars(char c, Character comment){
        nextCharIndex(c, comment);
    }

    public void skipUntil(char c){
        Pair<Integer,Integer> index = findNextCharIndex(c);
        if(index == null) end();
        else setIndex(index.getKey(),index.getValue());
    }

    public void skipSpaces(){
        while(hasNext() && nextChar() == ' ');
    }

    public void previousChar(){
        previousChars(1);
    }

    public void previousChars(int amount){
        int newLineIndex = this.lineIndex;
        int newCharIndex = this.charIndex-amount;

        while(newCharIndex < 0 && newLineIndex > 0){
            newLineIndex--;
            newCharIndex = lines[newLineIndex].length+newCharIndex;
        }

        setIndex(newLineIndex,newCharIndex);
    }

    public Pair<Integer,Integer> getEnd(){
        int lastLine = this.lineIndex = this.lines.length-1;
        return new Pair<>(lastLine,this.lines[lastLine].length);
    }

    public void end(){
        this.lineIndex = this.lines.length-1;
        this.charIndex = this.lines[this.lineIndex].length-2;
    }

    //Search options

    public Pair<Integer,Integer> findNextCharIndex(char c){
        return findNextCharIndex(c,(Character)null);
    }

    public Pair<Integer,Integer> findNextCharIndex(char c, Character comment){
        return findNextCharIndex(c,null, comment);
    }

    public Pair<Integer,Integer> findNextCharIndex(char c, Consumer<Character> validator){
        return findNextCharIndex(c,validator, null);
    }

    public Pair<Integer,Integer> findNextCharIndex(char c, Consumer<Character> validator, Character comment){
        return findNextCharIndex(this.lineIndex,this.charIndex,c,validator, comment);
    }

    public Pair<Integer,Integer> findNextCharIndex(int lineIndex, int charIndex, char c, Character comment){
        return findNextCharIndex(lineIndex, charIndex, c,null,comment);
    }

    public Pair<Integer,Integer> findNextCharIndex(int lineIndex, int charIndex, char c, Consumer<Character> validator){
        return findNextCharIndex(lineIndex, charIndex, c,validator,null);
    }

    public Pair<Integer,Integer> findNextCharIndex(int lineIndex, int charIndex, char c, Consumer<Character> validator, Character comment){
        char previous = ' ';
        while(has(lineIndex,charIndex)){
            char current = charAt(lineIndex,charIndex);
            if(current == c && (comment == null || previous != comment)) return new Pair<>(lineIndex,charIndex);
            else if(validator != null) validator.accept(current);
            previous = current;
            if(this.lines[lineIndex].length > charIndex+1) charIndex++;
            else{
                lineIndex++;
                charIndex = 0;
            }
        }
        return null;
    }


    public Pair<Integer,Integer> findNextCharIndex(Function<Character,Boolean> acceptor){
        return findNextCharIndex(this.lineIndex,this.charIndex,acceptor);
    }

    public Pair<Integer,Integer> findNextCharIndex(int lineIndex, int charIndex, Function<Character,Boolean> acceptor){
        while(has(lineIndex,charIndex)){
            char current = charAt(lineIndex,charIndex);
            if(acceptor.apply(current)) return new Pair<>(lineIndex,charIndex);
            if(this.lines[lineIndex].length > charIndex+1) charIndex++;
            else{
                lineIndex++;
                charIndex = 0;
            }
        }
        return null;
    }


    public Pair<Integer,Integer> nextCharIndex(char c, Character comment){
        char previous = ' ';
        while(hasNext()){
            if(nextChar() == c && (comment == null || previous != comment)) return new Pair<>(this.lineIndex,this.charIndex);
            previous = currentChar();
        }
        return null;
    }


    //Get Part operations

    public String getOnLine(int from, int to){
        return String.copyValueOf(Arrays.copyOfRange(lines[lineIndex],from,to));
    }

    public String get(int line, int from, int to){
        return String.copyValueOf(Arrays.copyOfRange(lines[line],from,to));
    }

    public String get(int toLine, int toChar){
        return get(this.lineIndex,this.charIndex,toLine,toChar);
    }

    public String get(int fromLine, int fromChar, int toLine, int toChar){
        return get(fromLine, fromChar, toLine, toChar,0);
    }

    public String get(int fromLine, int fromChar, int toLine, int toChar, int skip){
        if(fromLine == toLine) return get(fromLine,fromChar,toChar);
        StringBuilder builder = new StringBuilder();

        for(int i = fromLine;i<=toLine;i++){
            int start = i==fromLine?fromChar:skip;

            for(int e = start;e<this.lines[i].length;e++){
                if(i == toLine){
                    if(e >= toChar) break;
                    else builder.append(this.lines[i][e]);
                }else builder.append(this.lines[i][e]);
            }
            if(i != toLine) builder.append("\n");
        }
        return builder.toString();
    }

    public String getUntil(int lineIndex, int charIndex, char c,Consumer<Character> validator, Character comment){
        Pair<Integer,Integer> index = findNextCharIndex(lineIndex,charIndex,c,validator, comment);
        return get(index.getKey(),index.getValue());
    }

    public String nextUntil(char c){
        return nextUntil(c,null,null,false);
    }

    public String nextUntil(char c, Consumer<Character> validator){
        return nextUntil(c,validator,null,false);
    }

    public String nextUntil(char c, Character comment){
        return nextUntil(c,null,comment,false);
    }

    public String nextUntil(char c, Consumer<Character> validator, Character comment, boolean nullByEnd){
        return nextUntil(findNextCharIndex(c,validator, comment),nullByEnd);
    }

    public String nextUntil(Function<Character,Boolean> acceptor){
        return nextUntil(acceptor,false);
    }

    public String nextUntil(Function<Character,Boolean> acceptor, boolean nullByEnd){
        return nextUntil(findNextCharIndex(acceptor),nullByEnd);
    }

    public String nextUntil(Pair<Integer,Integer> index){
        return nextUntil(index,false);
    }

    public String nextUntil(Pair<Integer,Integer> index, boolean nullByEnd){
        String result;
        if(index == null) {
            if(nullByEnd) return null;
            else{
                index = getEnd();
                result = get(index.getKey(),index.getValue());
                end();
            }
        }else result = get(index.getKey(),index.getValue());
        setIndex(index.getKey(),index.getValue());
        return result;
    }

    //Copy operations

    public StringParser copyOf(int fromLine, int fromIndex){
        return copyOf(fromLine, fromIndex,this.lineIndex,this.charIndex);
    }

    public StringParser copyOf(int fromLine0, int fromIndex,int toLine, int toIndex){
        int fromLine = fromLine0;
        if(fromIndex > this.lines[fromLine].length) fromLine++;

        char[][] lines = Arrays.copyOfRange(this.lines,fromLine,toLine+1);

        char[] first = lines[0];
        if(fromIndex > 0 && fromIndex < first.length){
            lines[0] = Arrays.copyOfRange(first,fromIndex,lines[0].length);
        }

        char[] last = lines[lines.length-1];

        if(toIndex < last.length) lines[lines.length-1] = Arrays.copyOfRange(last,0,toIndex+1);
        return new StringParser(lines);
    }

    //Parser exception

    public String getExceptionContent(int line, int index){
        return getExceptionContent(line, index,null);
    }

    public String getExceptionContent(int line, int index, String prefix){
        StringBuilder builder = new StringBuilder();
        buildExceptionContent(line,index,builder,prefix);
        return builder.toString();
    }

    public void buildExceptionContent(int line, int index, StringBuilder builder){
        buildExceptionContent(line,index,builder,null);
    }

    public void buildExceptionContent(int line, int index, StringBuilder builder, String prefix){
        int start = index>10?index-18:0;
        int end = index+18;

        while(end > lines[line].length) end--;

        if(prefix != null) builder.append(prefix);
        builder.append(get(line,start,end));
        builder.append(System.lineSeparator());
        if(prefix != null) builder.append(prefix);
        for(int i = 0;i<index-start;i++) builder.append(" ");
        builder.append("^");
    }

    public void throwException(String message){
        throw new ParserException(this,lineIndex,charIndex,"Exception at "+(lineIndex+1)+":"+(charIndex+1)+" -> "+message);
    }

    public void throwException(String message, Exception exception){
        throw new ParserException(this,lineIndex,charIndex,message,exception);
    }



    //Object operations

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (char[] line : lines) builder.append(line).append('\n');
        builder.setLength(builder.length()-1);
        return builder.toString();
    }


    private static char[][] readLinesFromStream(InputStream stream, Charset charset){
        BufferedReader reader = charset!=null?new BufferedReader(new InputStreamReader(stream,charset)):new BufferedReader(new InputStreamReader(stream));
        char[][] lines = readLinesFromBuffer(reader);
        try {
            reader.close();
        } catch (IOException ignored) {}
        return lines;
    }

    private static char[][] readLinesFromBuffer(BufferedReader reader){
        try{
            ArrayList<char[]> lines = new ArrayList<>();

            //Read lines from reader
            String line;
            while((line = reader.readLine())!= null){
                char[] lineData = line.toCharArray();
                lines.add(lineData.length>0?lineData:EMPTY_LINE);
            }

            return lines.toArray(new char[0][]);
        }catch (IOException exception){
            throw new IORuntimeException(exception);
        }
    }
}
