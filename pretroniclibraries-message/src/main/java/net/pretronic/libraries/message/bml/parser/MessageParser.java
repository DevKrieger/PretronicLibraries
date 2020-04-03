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
import net.pretronic.libraries.message.bml.builder.MessageBuilder;
import net.pretronic.libraries.message.bml.indicate.Indicate;
import net.pretronic.libraries.utility.parser.StringParser;


//@if(test in test;test,${test})
public class MessageParser {

    private final MessageProcessor processor;
    private final StringParser parser;
    private final Module root;

    private Module current;
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
        this.state = new ParserState.OutlineState();

        this.root = new Module(null, null);
        this.current = this.root;

        if(!parser.isEmpty()){
            parser.nextChar();
            mark();
            parser.previousChar();
        }
    }

    public StringParser getParser() {
        return parser;
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

        if(text != null){
            MessageBuilder builder = processor.getTextBuilderFactory().create(text);
            Module module = new Module(null,builder);
            nextModule(module);
        }
    }

    public void extractStringAndPush(){
        extractStringAndPush(0);
    }

    public void nextSequence(String name, Indicate indicate,ParserState start){
        Module module = new Module(name,indicate != null ? (indicate.hasName() ? null : indicate.getFactory().create(name)) : null);
        nextModule(module);

        this.sequence = new MessageSequence(this.sequence,indicate,module,start);
        markNext();
    }

    public void nextModule(Module module){
        if(this.sequence != null){
            if(this.sequence.getState() instanceof ParserState.LeftOperationState){
                this.sequence.getModule().setLeftOperation(module);
            }else if(this.sequence.getState() instanceof ParserState.RightOperationState){
                this.sequence.getModule().setRightOperation(module);
            }else if(this.sequence.getState() instanceof ParserState.ExtensionIndicateState){
                this.sequence.getModule().setExtension(module);
            }else{
                this.sequence.pushModule(module);
            }
        }else{
            this.current.setNext(module);
            this.current = module;
        }
    }

    public void finishSequence(){
        this.sequence = this.sequence.getParent();
        markNext();
    }

    public Message parse(){
        if(parser.isEmpty()) return new Message(null);
        while (this.parser.hasNextChar()){
            this.state.parse(processor,this,parser.nextChar());
        }
        extractStringAndPush(1);
        return new Message(root.getNext());
    }
}

