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

import net.pretronic.libraries.message.bml.MessageProcessor;
import net.pretronic.libraries.message.bml.Module;
import net.pretronic.libraries.message.bml.builder.StaticTextMessageBuilder;
import net.pretronic.libraries.message.bml.indicate.Indicate;

public interface ParserState {

    void parse(MessageProcessor processor, MessageParser parser, char current);

    class OutlineState implements ParserState {

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            if(parser.getSequence() == null){
                checkNextIndicate(processor,parser,current,true);
            }else{
                parser.getSequence().getState().parse(processor, parser, current);
            }
        }
    }

    static boolean checkNextIndicate(MessageProcessor processor, MessageParser parser, char current, boolean push){
        Indicate indicate = processor.getIndicate(current);
        parser.getParser().previousChar();
        char previous = parser.getParser().currentChar();
        parser.getParser().nextChar();
        if(indicate != null && previous != processor.getEscapeCharacter()) {
            if (indicate.hasPrefix()) {
                if (indicate.hasName()) {
                    if(push) parser.extractStringAndPush();
                    parser.nextSequence(null, indicate,new NameIndicateState());
                    return true;
                } else {
                    if (parser.getParser().hasNextChar()) {
                        if (parser.getParser().nextChar() == indicate.getStart()) {
                            if(push)parser.extractStringAndPush(-1);
                        } else {
                            parser.getParser().previousChar();
                            return false;//No indicate go further
                        }
                    }
                }
            }else{
                if(push) parser.extractStringAndPush();
            }
            parser.nextSequence(null, indicate,null);
            moveToBody(parser,indicate);
            return true;
        }
        return false;
    }

    static void moveToBody(MessageParser parser,Indicate indicate){
        ParserState state;
        if (indicate.hasOperation()) {
            state = new LeftOperationState();
        }else if(indicate.hasParameters()){
            state = new ParameterState();
        }else if (indicate.isSubIndicateAble()) {
            state = new InlineSequenceState();
        }else {
            state = new InlineTextState();
        }
        parser.getSequence().setState(state);
    }

    static void moveOut(MessageParser parser){
        if(parser.getIndicate().hasExtension()
                && parser.getParser().hasNextChar()
                && parser.getParser().nextChar() == parser.getIndicate().getExtensionStart()){
            if(parser.getIndicate().isExtensionSubIndicateAble()){
                parser.getSequence().setState(new ExtensionIndicateState());
            }else{
                parser.getSequence().setState(new TextExtensionIndicateState());
            }
        }else{
            parser.finishSequence();
        }
        parser.markNext();
    }

    static void findIndicate(MessageProcessor processor, MessageParser parser, char current,boolean push,ParserState nextState){
        if(!processor.isIgnoredChar(current)) {
            if(current == '\'' || current == '"') {
                parser.nextSequence(null,null,new SubSequenceIndicateState(current));
                if(nextState != null) parser.getSequence().getParent().setState(nextState);
            }else {
                Indicate indicate = processor.getIndicate(current);
                if(indicate != null) {
                    if(checkNextIndicate(processor,parser,current,push)){
                        if(nextState != null) parser.getSequence().getParent().setState(nextState);
                        return;
                    }
                }
                parser.getParser().throwException("Invalid character");
            }
        }
    }

    class LeftOperationState implements ParserState {

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            if(parser.getIndicate().getEnd() == current){
                moveOut(parser);
            }else{
                findIndicate(processor,parser,current,false,new RightOperationState());
            }
        }
    }

    class RightOperationState implements ParserState {

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            if(parser.getIndicate().getEnd() == current){
                moveOut(parser);
            }else if(parser.getIndicate().hasParameters() && parser.getIndicate().getParameter() == current){
                parser.getSequence().setState(new ParameterState());
            }else if(current == '\'' || current == '"') {
                parser.getSequence().setOperator(parser.extractString().trim());
                parser.nextSequence(null,null,new SubSequenceIndicateState(current));
                parser.getSequence().getParent().setState(new EndingOperationState());
            }else {
                Indicate indicate = processor.getIndicate(current);
                if(indicate != null) {
                    MessageSequence sequence = parser.getSequence();
                    String operator = parser.extractString();//@Todo optimize
                    if(checkNextIndicate(processor,parser,current,false)){
                        sequence.setOperator(operator.trim());
                        sequence.setState(new EndingOperationState());
                        return;
                    }
                }
            }
        }
    }

    class EndingOperationState implements ParserState {

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            if(!processor.isIgnoredChar(current)){
                Indicate indicate = parser.getIndicate();
                if(indicate.getEnd() == current){
                    moveOut(parser);
                }else if(indicate.hasParameters() && indicate.getParameter() == current){
                    parser.getSequence().setState(new ParameterState());
                }
            }
        }
    }

    class ParameterState implements ParserState {

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            if(parser.getIndicate().getEnd() == current){
                moveOut(parser);
            }else if(parser.getIndicate().getParameter() == current){
                parser.getSequence().nextParameter();
            }else{
                findIndicate(processor,parser,current,false,null);
            }
        }
    }

    class InlineTextState implements ParserState {

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            if(parser.getIndicate().getEnd() == current){
                String text = parser.extractString();
                if(text != null){
                    parser.nextModule(new Module(null,new StaticTextMessageBuilder(text)));
                }
                moveOut(parser);
            }
        }
    }

    class InlineSequenceState implements ParserState {

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            checkNextIndicate(processor,parser,current,true);
        }
    }

    class SubSequenceIndicateState implements ParserState {

        private final char ending;

        public SubSequenceIndicateState(char ending) {
            this.ending = ending;
        }

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            parser.getParser().previousChar();
            if(ending == current && parser.getParser().currentChar() != '\\'){
                parser.extractStringAndPush(1);
                parser.getParser().nextChar();
                parser.finishSequence();
            }else{
                parser.getParser().nextChar();
                checkNextIndicate(processor,parser,current,true);
            }
        }
    }

    class NameIndicateState implements ParserState {

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            if(parser.getSequence().getIndicate().getStart() == current){
                String name = parser.extractString();
                parser.getSequence().setName(name);
                moveToBody(parser,parser.getSequence().getIndicate());
                parser.markNext();
            }
        }
    }

    class TextExtensionIndicateState implements ParserState {

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            if(parser.getSequence().getIndicate().getExtensionEnd() == current){
                String extension = parser.extractString();
                parser.getSequence().setExtension(new Module(null,new StaticTextMessageBuilder(extension)));
                parser.markNext();
                parser.finishSequence();
            }
        }
    }

    class ExtensionIndicateState implements ParserState {

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            if(parser.getSequence().getIndicate().getExtensionEnd() == current){
                parser.markNext();
                parser.finishSequence();
            }else{
                findIndicate(processor,parser,current,false,new EndingExtensionIndicateState());
            }
        }
    }

    class EndingExtensionIndicateState implements ParserState {

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            if(parser.getSequence().getIndicate().getExtensionEnd() == current){
                parser.markNext();
                parser.finishSequence();
            }else if(!processor.isIgnoredChar(current)){
                parser.getParser().throwException("Invalid character");
            }
        }
    }
}
