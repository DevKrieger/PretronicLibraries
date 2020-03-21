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
import net.pretronic.libraries.message.bml.Module;
import net.pretronic.libraries.message.bml.builder.MessageBuilder;
import net.pretronic.libraries.message.bml.builder.StaticTextMessageBuilder;
import net.pretronic.libraries.message.bml.indicate.Indicate;

public class MessageSequence {

    private final MessageSequence parent;
    private final Indicate indicate;
    private final Module module;

    private Module current;

    public MessageSequence(MessageSequence parent, Indicate indicate, Module module) {
        this.parent = parent;
        this.indicate = indicate;
        this.module = module;
    }

    public MessageSequence getParent() {
        return parent;
    }

    public Indicate getIndicate() {
        return indicate;
    }

    public void pushModule(Module module){
        if(current == null){
            this.module.addParameter(module);
        }else{
            this.current.setNext(module);
        }
        this.current = module;
    }

    public void nextParameter(){
        current = null;
    }

    public void setName(String name){
        this.module.setName(name);
        this.module.setBuilder(indicate.getFactory().create(name));
    }

    public void setExtension(String extension){
        this.module.setExtension(extension);
    }
}
