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

package net.pretronic.libraries.message.bml.indicate;

import net.pretronic.libraries.message.bml.builder.MessageBuilder;
import net.pretronic.libraries.message.bml.builder.MessageBuilderFactory;

public class IndicateBuilder {

    private char prefix;
    private char start;
    private char end;
    private char parameter;

    private char extensionStart;
    private char extensionEnd;

    private boolean hasPrefix;
    private boolean hasName;
    private boolean hasParameters;
    private boolean hasExtension;
    private boolean isSubIndicateAble;

    private MessageBuilderFactory factory;

    public IndicateBuilder prefix(char prefix) {
        this.prefix = prefix;
        this.hasPrefix = true;
        return this;
    }

    public IndicateBuilder define(char start,char end) {
        this.start = start;
        this.end = end;
        return this;
    }

    public IndicateBuilder parameter(char parameter) {
        this.parameter = parameter;
        this.hasParameters = true;
        return this;
    }

    public IndicateBuilder extension(char start, char end) {
        this.extensionStart = start;
        this.extensionEnd = end;
        this.hasExtension = true;
        return this;
    }

    public IndicateBuilder factory(MessageBuilderFactory factory) {
        this.factory = factory;
        return this;
    }

    public IndicateBuilder builder(MessageBuilder builder) {
        this.factory = name -> builder;
        return this;
    }

    public IndicateBuilder hasName() {
        this.hasName = true;
        return this;
    }

    public IndicateBuilder hasParameters() {
        this.hasParameters = true;
        return this;
    }

    public IndicateBuilder isSubIndicateAble() {
        this.isSubIndicateAble = true;
        return this;
    }

    public Indicate create() {
        if(factory == null) throw new IllegalArgumentException("No factory is not defined");
        if(hasName && !hasPrefix) throw new IllegalArgumentException("HasName requires a prefix");
        return new Indicate(prefix, start, end, parameter, extensionStart,extensionEnd,hasPrefix, hasName, hasParameters,hasExtension,isSubIndicateAble,factory);
    }

    public static IndicateBuilder newBuilder(){
        return new IndicateBuilder();
    }
}
