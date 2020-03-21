/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.03.20, 17:04
 * @web %web%
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

package net.pretronic.libraries.message.bml.parser;

import net.pretronic.libraries.message.bml.Message;
import net.pretronic.libraries.message.bml.MessageProcessor;
import net.pretronic.libraries.message.bml.Module;
import net.pretronic.libraries.message.bml.builder.InputMessageBuilder;
import net.pretronic.libraries.message.bml.builder.MessageBuilder;
import net.pretronic.libraries.message.bml.builder.StaticTextMessageBuilder;
import net.pretronic.libraries.message.bml.indicate.Indicate;
import net.pretronic.libraries.utility.parser.StringParser;

public class MessageParser {

    private final MessageProcessor processor;
    private final StringParser parser;
    private final Module root;

    private ParserState state;
    private MessageSequence sequence;

    private int markedIndex;
    private int markedLine;

    public MessageParser(MessageProcessor processor,String input){
        this(processor,new StringParser(input));
    }

    public MessageParser(MessageProcessor processor,StringParser parser){
        this.processor = processor;
        this.parser = parser;
        this.state = new ParserState.IndicateState();

        this.root = new Module(null, null);
        this.sequence = new MessageSequence(null,Indicate.DUMMY_START,root);

        if(!parser.isEmpty()){
            parser.nextChar();
            mark();
            parser.previousChar();
        }
    }

    public StringParser getParser() {
        return parser;
    }

    public ParserState getState() {
        return state;
    }

    public void setState(ParserState state) {
        this.state = state;
    }

    public Indicate getIndicate(){
        return getSequence().getIndicate();
    }

    public MessageSequence getSequence() {
        return sequence;
    }

    public void mark(){
        markedIndex = parser.charIndex();
        markedLine = parser.lineIndex();
    }

    public void markNext(){
        if(parser.hasNextChar()){
            parser.nextChar();
            mark();
            parser.previousChar();
        }else mark();
    }

    public String extractString(){
        return extractString(0);
    }

    public String extractString(int added){
        if(parser.lineIndex() > markedLine || parser.charIndex() > markedIndex ){
            return parser.get(markedLine,markedIndex,parser.lineIndex(),parser.charIndex()+added);
        }
        return null;
    }

    public void extractStringAndPush(int added){
        String text = extractString(added);

        MessageBuilder builder;
        if(getSequence().getParent() != null) builder = new StaticTextMessageBuilder(text);
        else builder = processor.getTextBuilderFactory().create(text);

        Module module = new Module(null,builder);
        if(text != null) getSequence().pushModule(module);
    }

    public void extractStringAndPush(){
        extractStringAndPush(0);
    }

    public void nextSequence(String name, Indicate indicate){
        Module module = new Module(name,indicate.hasName() ? null : indicate.getFactory().create(name));
        this.sequence.pushModule(module);
        this.sequence = new MessageSequence(this.sequence,indicate,module);
        markNext();
    }

    public void finishSequence(){
        this.sequence = this.sequence.getParent();
    }

    public void finishSequenceAndSwitchState(){
        finishSequence();
        markNext();
    }

    public Message parse(){
        if(parser.isEmpty()) return new Message(null);
        while (this.parser.hasNextChar()){
            this.state.parse(processor,this,parser.nextChar());
        }
        extractStringAndPush(1);
        return new Message(root.getParameters()[0]);
    }
}

