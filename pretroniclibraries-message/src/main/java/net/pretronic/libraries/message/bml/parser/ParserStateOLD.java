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

import net.pretronic.libraries.message.bml.module.TextModule;
import net.pretronic.libraries.message.bml.module.VariableModule;

public interface ParserStateOLD {

    ParserStateOLD START = new StartState();
    //ParserState TEXT = new TextState();
    ParserStateOLD VARIABLE = new VariableState();
    ParserStateOLD FUNCTION = new FunctionState();
    ParserStateOLD FUNCTION_PARAMETER = new ParameterState();

    void parse(MessageParser parser, char current);

    static boolean isSpace(char current){
        return current == ' ' || current == '\t' || current == '\r';
    }

    class StartState implements ParserStateOLD {

        @Override
        public void parse(MessageParser parser, char current) {
            if(current == '@'){
                parser.getSequence().pushInline(new TextModule(parser.getString()));
                //to function
            }else if(current == '%'){
                parser.getSequence().pushInline(new TextModule(parser.getString()));
                //to variable
            }
        }
    }

    class VariableState implements ParserStateOLD {

        @Override
        public void parse(MessageParser parser, char current) {
            if(current == '%'){
                parser.getSequence().pushInline(new VariableModule(parser.getString()));
            }else if(parser.getParser().isLineFinished() || !(Character.isLetter(current) || Character.isDigit(current))){
                parser.getSequence().pushInline(new TextModule(parser.getString()));
                parser.markNext();
                if(current == '@'){
                    //function
                }else{
                    //satte
                }
            }
        }
    }

    class FunctionState implements ParserStateOLD {

        @Override
        public void parse(MessageParser parser, char current) {
            if(current == '('){

            }
        }
    }

    class ParameterState implements ParserStateOLD {

        @Override
        public void parse(MessageParser parser, char current) {
            if(current == ','){

            }else if(current == ')'){

            }
        }
    }
}
