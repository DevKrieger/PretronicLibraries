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

import net.pretronic.libraries.message.bml.builder.BuildContext;
import net.pretronic.libraries.message.bml.builder.MessageBuilder;

import java.util.Arrays;

public class Module {

    private String name;
    private Module[] parameters;
    private String extension;
    private Module next;

    private MessageBuilder builder;

    public Module(String name, MessageBuilder builder) {
        this.name = name;
        this.builder = builder;
        this.parameters = new Module[0];
    }

    public Module(String name, Module[] parameters, String extension, Module next, MessageBuilder builder) {
        this.name = name;
        this.parameters = parameters;
        this.extension = extension;
        this.next = next;
        this.builder = builder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBuilder(MessageBuilder builder) {
        this.builder = builder;
    }

    public Module[] getParameters() {
        return parameters;
    }

    public void addParameter(Module module){
        this.parameters = Arrays.copyOf(parameters,parameters.length+1);
        this.parameters[parameters.length-1] = module;
    }

    public void setParameters(Module[] parameters) {
        this.parameters = parameters;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Module getNext() {
        return next;
    }

    public void setNext(Module next) {
        this.next = next;
    }

    public MessageBuilder getBuilder() {
        return builder;
    }

    public Object build(BuildContext context){
        Object result = buildInternal(context);
        if(next != null){
            StringBuilder builder = new StringBuilder();
            builder.append(result);
            process(context,builder);
            return builder.toString();
        }
        else return result;
    }

    public void process(BuildContext context, StringBuilder builder){
        builder.append(buildInternal(context));
        if(next != null) next.process(context,builder);
    }

    private Object buildInternal(BuildContext context) {
        Object[] parameters = new Object[this.parameters.length];
        int index = 0;
        for (Module parameter : this.parameters) {
            parameters[index] = parameter.build(context);
            index++;
        }
        return builder.build(context,name,parameters,extension);
    }
}
