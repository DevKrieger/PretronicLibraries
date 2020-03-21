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

package net.pretronic.libraries.message.bml.builder;

import net.pretronic.libraries.message.bml.variable.VariableSet;

public class StaticTextMessageBuilder implements MessageBuilder{

    private final String text;

    public StaticTextMessageBuilder(String text) {
        this.text = text;
    }

    @Override
    public Object build(Object argument,String name, Object[] parameters, String extension, VariableSet variables) {
        return text;
    }

    public static class Factory implements MessageBuilderFactory {

        @Override
        public MessageBuilder create(String name) {
            return new StaticTextMessageBuilder(name);
        }
    }
}
