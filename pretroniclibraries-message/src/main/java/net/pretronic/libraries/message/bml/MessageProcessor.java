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

import net.pretronic.libraries.message.bml.builder.MessageBuilderFactory;
import net.pretronic.libraries.message.bml.function.Function;
import net.pretronic.libraries.message.bml.indicate.Indicate;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;

public interface MessageProcessor {

    Collection<Indicate> getIndicates();

    Indicate getIndicate(char prefix);

    void registerIndicate(ObjectOwner owner,Indicate indicate);

    void unregisterIndicate(Indicate indicate);

    void unregisterIndicates(ObjectOwner owner);

    MessageBuilderFactory getTextBuilderFactory();

    void setTextBuilderFactory(MessageBuilderFactory builder);


    Collection<Function> getFunctions();

    Function getFunction(String name);

    void registerFunction(ObjectOwner owner,String name,Function function);

    void unregisterFunction(Function function);

    void unregisterFunction(String name);

    void unregisterFunctions(ObjectOwner owner);


    boolean isIgnoredChar(char char0);

    void addIgnoredChar(char char0);

    void removeIgnoredChar(char char0);


    Message parse(String input);

}
