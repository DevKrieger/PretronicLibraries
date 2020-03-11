/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 23.12.19, 10:55
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

package net.prematic.libraries.message.bml.module;

import net.prematic.libraries.message.bml.function.Function;
import net.prematic.libraries.message.bml.variable.VariableSet;

import java.util.ArrayList;
import java.util.List;

public class FunctionModule implements Module{

    private final Function function;
    private final List<Module> parameters;

    private Module next;

    public FunctionModule(Function function) {
        this.function = function;
        this.parameters = new ArrayList<>();
    }

    public Module getNext() {
        return next;
    }

    public Function getFunction() {
        return function;
    }

    public List<Module> getParameters() {
        return parameters;
    }

    public void addParameter(Module module){
        this.parameters.add(module);
    }

    @Override
    public void setNext(Module next) {
        this.next = next;
    }

    @Override
    public void pushParameter(Module module) {

    }

    @Override
    public Object process(VariableSet variables) {
        Object[] parameters = new Object[this.parameters.size()];
        for (int i = 0; i < this.parameters.size(); i++) {
            parameters[i] = this.parameters.get(i).process(variables);
        }
        return function.execute(parameters);
    }

    @Override
    public void process(StringBuilder builder, VariableSet variables) {
        builder.append(process(variables));
        if(next != null) next.process(builder, variables);
    }

}
