/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 23.12.19, 11:02
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

import net.prematic.libraries.message.bml.functions.RandomTextFunction;
import net.prematic.libraries.message.bml.module.Module;
import net.prematic.libraries.message.bml.module.*;
import net.prematic.libraries.message.bml.variable.HashVariableSet;
import net.prematic.libraries.message.bml.variable.VariableSet;

public class Test {

    public static void main(String[] args) {
        Module root = new TextModule("Hallo: ");

        Module playerVariable = new VariableModule("player");

        FunctionModule function = new FunctionModule(new RandomTextFunction());
        function.getParameters().add(new PrimitiveModule(10));
        function.setNext(new TextModule("!"));

        Module text = new TextModule("Random: ");
        text.setNext(function);

        playerVariable.setNext(function);
        root.setNext(playerVariable);

        VariableSet variables = new HashVariableSet();
        variables.add("player","Test");
        variables.add("age",10);

        long start = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        root.process(builder,variables);
        String result = builder.toString();
        start = System.currentTimeMillis()-start;
        System.out.println(start);
        System.out.println(result);

    }
}
