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

package net.pretronic.libraries.message.bml;

import net.pretronic.libraries.message.bml.variable.VariableSet;

public class Message {

    private final Module root;

    public Message(Module root) {
        this.root = root;
    }

    public String build(Object argument,VariableSet variables){
        StringBuilder builder = new StringBuilder();
        build(argument,builder,variables);
        return builder.toString();
    }

    public void build(Object argument,StringBuilder builder, VariableSet variables) {
        if(root != null){
            root.process(argument,builder,variables);
        }
    }

    @Override
    public String toString() {
        return build(null,VariableSet.newEmptySet());
    }
}
