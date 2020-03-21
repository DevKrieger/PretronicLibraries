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

package net.pretronic.libraries.message.bml.function;

import net.pretronic.libraries.message.bml.MessageProcessor;
import net.pretronic.libraries.message.bml.builder.MessageBuilder;
import net.pretronic.libraries.message.bml.builder.MessageBuilderFactory;
import net.pretronic.libraries.message.bml.builder.StaticTextMessageBuilder;
import net.pretronic.libraries.message.bml.variable.VariableSet;

public class FunctionFactory implements MessageBuilderFactory {

    private final MessageProcessor processor;

    public FunctionFactory(MessageProcessor processor) {
        this.processor = processor;
    }

    @Override
    public MessageBuilder create(String name) {
        Function function = processor.getFunction(name);
        if(function == null) return new StaticTextMessageBuilder("{Function not found;"+name+"}");
        return new Builder(function);
    }

    public static class Builder implements MessageBuilder {

        private final Function function;

        public Builder(Function function) {
            this.function = function;
        }

        @Override
        public Object build(Object argument,String name, Object[] parameters, String extension, VariableSet variables) {
            return function.execute(parameters);
        }
    }

    public static class NotFound implements MessageBuilder {

        @Override
        public Object build(Object argument,String name, Object[] parameters, String extension, VariableSet variables) {
            return null;
        }
    }
}
