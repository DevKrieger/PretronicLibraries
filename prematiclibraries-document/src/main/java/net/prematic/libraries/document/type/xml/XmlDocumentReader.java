package net.prematic.libraries.document.type.xml;

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.DocumentEntry;
import net.prematic.libraries.document.DocumentRegistry;
import net.prematic.libraries.document.io.DocumentReader;
import net.prematic.libraries.utility.GeneralUtil;
import net.prematic.libraries.utility.parser.StringParser;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static net.prematic.libraries.document.type.Characters.*;

public class XmlDocumentReader implements DocumentReader {

    @Override
    public Document read(byte[] content) {
        return read(new String(content));
    }

    @Override
    public Document read(byte[] content, Charset charset) {
        return read(new String(content,charset));
    }

    @Override
    public Document read(String content) {
        return read(new StringParser(content));
    }

    @Override
    public Document read(File location) {
        return read(new StringParser(location));
    }

    @Override
    public Document read(File location, Charset charset) {
        return read(new StringParser(location,charset));
    }

    @Override
    public Document read(InputStream input) {
        return read(new StringParser(input));
    }

    @Override
    public Document read(InputStream input, Charset charset) {
        return read(new StringParser(input,charset));
    }

    @Override
    public Document read(StringParser parser) {
        return load(parser);
    }

    private Document load(StringParser parser) {
        while(parser.hasNext()){
            char input = parser.nextChar();
            if(!isIgnoredChar(input)){
                if(input == LESSER_THAN && parser.hasNext()){
                    char next = parser.nextChar();
                    if(next == QUESTION_MARK || next== '!'){
                        parser.skipUntil(LESSER_THAN);
                        parser.skipChar();
                    }
                }else {
                    parser.previousChar();
                    String key = readKey(parser);
                    parser.skipUntil(LESSER_THAN);
                    parser.skipChar();
                    return readObject(key,parser);
                }
            }
        }
        parser.throwException(ERROR_INVALID_CHARACTER);
        return null;
    }

    private DocumentEntry readNext(String key, StringParser parser) {
        while (parser.hasNext()) {
            char input = parser.nextChar();
            if(!isIgnoredChar(input)) {
                if(input == LESSER_THAN) {
                    return readObject(key,parser);
                } else {
                    parser.previousChar();
                    return readPrimitive(key,parser);
                }
            }
        }
        parser.throwException(ERROR_INVALID_CHARACTER);
        return null;
    }

    private Document readObject(String key, StringParser parser){
        List<DocumentEntry> entries = new ArrayList<>();
        char input;
        while (parser.hasNext() && (input = parser.nextChar()) != SLASH) {
            if(!isIgnoredChar(input)){
                parser.previousChar();
                if(input == '!'){
                    parser.skipUntil('-');
                    parser.skipChars(3);
                    //@Todo fix bastelei
                }else{
                    String entryKey = readKey(parser);
                    entries.add(readNext(entryKey, parser));
                }
            }
        }
        parser.skipUntil(LESSER_THAN);
        if(parser.hasNext()){
            parser.skipChar();
        }
        Document entry;
        if(entries.size() > 1 && entries.get(0).getKey().equals(entries.get(1).getKey())){
            entry =  DocumentRegistry.getFactory().newArrayEntry(key);
        }else entry =  DocumentRegistry.getFactory().newDocument(key);
        entry.entries().addAll(entries);
        return entry;
    }

    private DocumentEntry readPrimitive(String key, StringParser parser) {
        String primitive = parser.nextUntil(LESSER_THAN);
        parser.skipChar();
        parser.skipUntil(LESSER_THAN);
        parser.skipChar();
        if("true".equalsIgnoreCase(primitive)) {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key, true);
        } else if("false".equals(primitive)) {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key, false);
        } else if(GeneralUtil.isNaturalNumber(primitive)) {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key, Long.valueOf(primitive));
        } else if(GeneralUtil.isNumber(primitive)) {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key, Double.valueOf(primitive));
        } else {
            return DocumentRegistry.getFactory().newPrimitiveEntry(key, primitive);
        }
    }

    private String readKey(StringParser parser){
        StringBuilder key = new StringBuilder();
        char input = parser.nextChar();
        do {
            key.append(input);
            input = parser.nextChar();
        } while (input != SPACE && input != GREATER_THAN);
        if(input != GREATER_THAN){
            parser.skipUntil(GREATER_THAN);
            parser.skipChar();
        }
        return key.toString();
    }

    private boolean isIgnoredChar(char c) {
        return   c == SPACE || c == BREAK || c == BACK || c == TAB;
    }
}

