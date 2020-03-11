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

package net.pretronic.libraries.message.bml.module;

import net.pretronic.libraries.message.bml.variable.VariableSet;

public class LoopModule implements Module{

    private Module loop;
    private Module next;

    public void setLoop(Module loop) {
        this.loop = loop;
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void process(StringBuilder builder, VariableSet variables) {
        if(next != null) next.process(builder,variables);
    }

}
