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
import net.pretronic.libraries.message.bml.indicate.Indicate;

public interface ParserState {


    void parse(MessageProcessor processor, MessageParser parser, char current);

    class IndicateState implements ParserState {

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            if(parser.getIndicate().getEnd() == current){
                parser.extractStringAndPush();
                if(parser.getSequence().getIndicate().hasExtension() ){
                    if(parser.getParser().hasNextChar() && parser.getParser().nextChar() == parser.getIndicate().getExtensionStart()){
                        parser.setState(new ExtensionIndicateState());
                    }
                }else{
                    parser.finishSequenceAndSwitchState();
                }
                parser.markNext();
            }else if(parser.getIndicate().hasParameters() && parser.getIndicate().getParameter() == current){
                parser.extractStringAndPush();
                parser.getSequence().nextParameter();
                parser.markNext();
            }else if(parser.getIndicate().isSubIndicateAble()){
                Indicate indicate = processor.getIndicate(current);
                if(indicate != null){
                    if(indicate.hasPrefix()){
                        if(indicate.hasName()){
                            parser.extractStringAndPush();
                            parser.setState(new NameIndicateState());
                            parser.markNext();
                            parser.nextSequence(null,indicate);
                        }else{
                            if(parser.getParser().hasNextChar()){
                                if(parser.getParser().nextChar() == indicate.getStart()){
                                    parser.extractStringAndPush(-1);
                                    parser.nextSequence(null,indicate);
                                }else{
                                    parser.getParser().previousChar();
                                }
                            }
                        }
                    }else{
                        parser.extractStringAndPush();
                        parser.nextSequence(null,indicate);
                    }
                }
            }
        }
    }

    class NameIndicateState implements ParserState {

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            if(parser.getSequence().getIndicate().getStart() == current){
                String name = parser.extractString();
                parser.getSequence().setName(name);
                parser.setState(new IndicateState());
                parser.markNext();
            }
        }
    }

    class ExtensionIndicateState implements ParserState {

        @Override
        public void parse(MessageProcessor processor, MessageParser parser, char current) {
            if(parser.getSequence().getIndicate().getExtensionEnd() == current){
                String extension = parser.extractString();
                parser.getSequence().setExtension(extension);
                parser.setState(new IndicateState());
                parser.markNext();
                parser.extractStringAndPush();
                parser.finishSequenceAndSwitchState();
            }
        }
    }
}
