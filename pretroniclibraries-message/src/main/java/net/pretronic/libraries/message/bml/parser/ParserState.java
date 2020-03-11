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

package net.pretronic.libraries.message.bml.parser;

import net.pretronic.libraries.message.bml.MessageProcessor;
import net.pretronic.libraries.message.bml.indicate.Indicate;
import net.pretronic.libraries.message.bml.module.TextModule;

public interface ParserState {

    void parse(MessageProcessor processor, MessageParser parser, char current);

    static boolean isSpace(char current){
        return current == ' ' || current == '\t' || current == '\r';
    }

    class UndefinedState implements ParserState {

        @Override
        public void parse(MessageProcessor processor,MessageParser parser, char current) {
            Indicate indicate = processor.getIndicate(current);
            if(indicate != null){
                parser.mark();
                parser.setIndicate(indicate);
                if(indicate.hasPrefix()){

                }else{

                }
            }
        }
    }

    class PrefixedIndicateState implements ParserState {

        @Override
        public void parse(MessageProcessor processor,MessageParser parser, char current) {
            if(current == parser.getIndicate().getStart()){
                if(parser.getIndicate().isParametrized()){

                }else{

                }

            }else {
                Indicate indicate = processor.getIndicate(current);
                if(indicate != null){
                    //
                }
            }
        }
    }

    class ParametrizedIndicateState implements ParserState {
        @Override
        public void parse(MessageProcessor processor,MessageParser parser, char current) {

        }
    }


    class IndicateState implements ParserState {

        @Override
        public void parse(MessageProcessor processor,MessageParser parser, char current) {
            if(current == parser.getIndicate().getStart()){

                //to function
            }else if(current == '%'){
                parser.getSequence().pushInline(new TextModule(parser.getString()));
                parser.mark();
                //to variable
            }
        }
    }




}
