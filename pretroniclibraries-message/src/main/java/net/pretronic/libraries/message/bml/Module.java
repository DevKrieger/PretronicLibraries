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
    private Module rightOperation;
    private Module leftOperation;
    private String operation;

    private Module extension;
    private Module next;

    private MessageBuilder builder;

    public Module(String name, MessageBuilder builder) {
        this.name = name;
        this.builder = builder;
        this.parameters = new Module[0];
    }

    public Module(String name, Module[] parameters, Module extension, Module next, MessageBuilder builder) {
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

    public Module getExtension() {
        return extension;
    }

    public void setExtension(Module extension) {
        this.extension = extension;
    }

    public Module getRightOperation() {
        return rightOperation;
    }

    public void setRightOperation(Module rightOperation) {
        this.rightOperation = rightOperation;
    }

    public Module getLeftOperation() {
        return leftOperation;
    }

    public void setLeftOperation(Module leftOperation) {
        this.leftOperation = leftOperation;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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
        return build(context,false);
    }

    public Object build(BuildContext context,boolean stringResult){
        if(builder == null){
            if(parameters.length > 0){
                return parameters[0].build(context, stringResult);
            }
            return next != null ? next.build(context, stringResult) : null;
        }

        //boolean formatted = builder.isUnformattedResultRequired();

        return builder.build(context,stringResult,name
                ,leftOperation,operation,rightOperation
                ,parameters,extension,next);

/*
        Object[] parameters = new Object[this.parameters.length];
        int index = 0;
        for (Module parameter : this.parameters) {
            parameters[index] = parameter.build(context,formatted);
            index++;
        }
        Object next = null;
        if(this.next != null) next = this.next.build(context,stringResult);

        Object leftOperator = this.leftOperation != null ? leftOperation.build(context, formatted) : null;
        Object rightOperator = this.rightOperation != null ? rightOperation.build(context, formatted) : null;

        String extension = this.extension != null ? this.extension.build(context, formatted).toString() : "";

        return builder.build(context,stringResult,name
                ,leftOperator,operation,rightOperator
                ,parameters,extension,next);
 */
    }

    public static Object build(Module module,BuildContext context,boolean requiresUnformatted){
        if(module == null) return null;
        else return module.build(context,requiresUnformatted);
    }
}
