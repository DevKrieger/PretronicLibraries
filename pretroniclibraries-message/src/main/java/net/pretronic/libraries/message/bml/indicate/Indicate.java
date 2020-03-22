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

import net.pretronic.libraries.message.bml.builder.MessageBuilderFactory;

public class Indicate {

    public static final Indicate DUMMY_START = new Indicate((char) 255,(char) 255,(char) 255,(char) 255,(char) 255
            ,(char) 255,false, false,false,false,true,null);

    private final char prefix;
    private final char start;
    private final char end;
    private final char parameter;

    private final char extensionStart;
    private final char extensionEnd;

    private final boolean hasPrefix;
    private final boolean hasName;
    private final boolean hasParameters;
    private final boolean hasExtension;
    private final boolean isSubIndicateAble;

    private final MessageBuilderFactory factory;

    public Indicate(char prefix, char start, char end, char parameter, char extensionStart, char extensionEnd, boolean hasPrefix
            , boolean hasName, boolean hasParameters, boolean hasExtension, boolean isSubIndicateAble, MessageBuilderFactory factory) {
        this.prefix = prefix;
        this.start = start;
        this.end = end;
        this.parameter = parameter;
        this.extensionStart = extensionStart;
        this.extensionEnd = extensionEnd;
        this.hasPrefix = hasPrefix;
        this.hasName = hasName;
        this.hasParameters = hasParameters;
        this.hasExtension = hasExtension;
        this.isSubIndicateAble = isSubIndicateAble;
        this.factory = factory;
    }

    public char getPrefix() {
        return prefix;
    }

    public char getStart() {
        return start;
    }

    public char getEnd() {
        return end;
    }

    public char getParameter() {
        return parameter;
    }

    public char getExtensionStart() {
        return extensionStart;
    }

    public char getExtensionEnd() {
        return extensionEnd;
    }

    public MessageBuilderFactory getFactory() {
        return factory;
    }

    public boolean hasPrefix() {
        return hasPrefix;
    }

    public boolean hasName() {
        return hasName;
    }

    public boolean hasParameters() {
        return hasParameters;
    }

    public boolean isSubIndicateAble() {
        return isSubIndicateAble;
    }

    public boolean hasExtension() {
        return hasExtension;
    }

    @Override
    public String toString() {
        return "Indicate{" +
                "prefix=" + prefix +
                ", start=" + start +
                ", end=" + end +
                ", parameter=" + parameter +
                ", extensionStart=" + extensionStart +
                ", extensionEnd=" + extensionEnd +
                ", hasPrefix=" + hasPrefix +
                ", hasName=" + hasName +
                ", hasParameters=" + hasParameters +
                ", hasExtension=" + hasExtension +
                ", isSubIndicateAble=" + isSubIndicateAble +
                '}';
    }
}
