/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 22.12.19, 21:59
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

package net.prematic.libraries.message.bml;

import net.prematic.libraries.message.bml.module.Module;
import net.prematic.libraries.message.bml.parser.MessageParser;
import net.prematic.libraries.message.bml.variable.HashVariableSet;
import net.prematic.libraries.message.bml.variable.VariableSet;
import net.prematic.libraries.utility.parser.StringParser;

public class Message {

    private final Module root;

    public Message(Module root) {
        this.root = root;
    }

    public String build(VariableSet variables){
        StringBuilder builder = new StringBuilder();
        build(builder,variables);
        return builder.toString();
    }

    public void build(StringBuilder builder, VariableSet variables){
        root.process(builder,variables);
    }

    public static Message parse(String content){
        return parse(new StringParser(content));
    }

    public static Message parse(StringParser parser0){
        return new MessageParser(parser0).parse();
    }

    //"Hallo %player%, du bist %age% Jahre tes alt!%"
    public static void main(String[] args) {
        VariableSet variables = new HashVariableSet();
        variables.add("player","Hans");
        variables.add("age",10);
        variables.add("stats",1);

        Message message = parse("Hallo %player%, du bist %age% Jahre alt! Random -> @RandomText()!");
        System.out.println(message.build(variables));
    }
}
