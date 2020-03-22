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
import net.pretronic.libraries.message.bml.builder.StaticTextMessageBuilder;
import net.pretronic.libraries.message.bml.function.Function;
import net.pretronic.libraries.message.bml.indicate.Indicate;
import net.pretronic.libraries.message.bml.parser.MessageParser;
import net.pretronic.libraries.utility.Iterators;
import net.pretronic.libraries.utility.OwnedObject;
import net.pretronic.libraries.utility.Validate;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class DefaultMessageProcessor implements MessageProcessor {//test hallo !(test)[https://]   @hallo(test)

    private final Collection<OwnedObject<Indicate>> indicates;
    private final Collection<FunctionEntry> functions;
    private final Collection<Character> ignoredChars;

    private MessageBuilderFactory textBuilderFactory;

    public DefaultMessageProcessor() {
        this.indicates = new ArrayList<>();
        this.functions = new ArrayList<>();
        this.ignoredChars = new HashSet<>();

        textBuilderFactory = new StaticTextMessageBuilder.Factory();
    }

    @Override
    public Collection<Indicate> getIndicates() {
        return Iterators.map(indicates, OwnedObject::getObject);
    }

    @Override
    public Indicate getIndicate(char char0) {
        OwnedObject<Indicate>  ownedIndicate = Iterators.findOne(this.indicates, ownedObject -> {
            Indicate indicate = ownedObject.getObject();
            return (indicate.hasPrefix() && indicate.getPrefix() == char0) || (indicate.getStart() == char0);
        });
        return ownedIndicate != null ? ownedIndicate.getObject() : null;
    }

    @Override
    public void registerIndicate(ObjectOwner owner, Indicate indicate) {
        Validate.notNull(owner,indicate);
        this.indicates.add(new OwnedObject<>(owner,indicate));
    }

    @Override
    public void unregisterIndicate(Indicate indicate) {
        Iterators.removeOne(this.indicates, ownedIndicate -> ownedIndicate.getObject().equals(indicate));
    }

    @Override
    public void unregisterIndicates(ObjectOwner owner) {
        Iterators.removeSilent(this.indicates, ownedIndicate -> ownedIndicate.getOwner().equals(owner));
    }

    @Override
    public MessageBuilderFactory getTextBuilderFactory() {
        return textBuilderFactory;
    }


    @Override
    public void setTextBuilderFactory(MessageBuilderFactory builder) {
        Validate.notNull(builder);
        textBuilderFactory = builder;
    }

    @Override
    public Collection<Function> getFunctions() {
        return Iterators.map(functions, FunctionEntry::getFunction);
    }

    @Override
    public Function getFunction(String name) {
        FunctionEntry entry = Iterators.findOne(this.functions, functionEntry -> functionEntry.getName().equalsIgnoreCase(name));
        return entry != null ? entry.getFunction() : null;
    }

    @Override
    public void registerFunction(ObjectOwner owner, String name, Function function) {
        Validate.notNull(owner,name,function);
        if(getFunction(name) != null) throw new IllegalArgumentException("A function with the name "+name+" is already registered");
        this.functions.add(new FunctionEntry(owner,name,function));
    }

    @Override
    public void unregisterFunction(Function function) {
        Validate.notNull(function);
        Iterators.removeSilent(this.functions, functionEntry -> functionEntry.getFunction().equals(function));
    }

    @Override
    public void unregisterFunction(String name) {
        Validate.notNull(name);
        Iterators.removeOne(this.functions, functionEntry -> functionEntry.getName().equalsIgnoreCase(name));
    }

    @Override
    public void unregisterFunctions(ObjectOwner owner) {
        Validate.notNull(owner);
        Iterators.removeSilent(this.functions, functionEntry -> functionEntry.getOwner().equals(owner));
    }

    @Override
    public boolean isIgnoredChar(char char0) {
        return ignoredChars.contains(char0);
    }

    @Override
    public void addIgnoredChar(char char0) {
        ignoredChars.add(char0);
    }

    @Override
    public void removeIgnoredChar(char char0) {
        ignoredChars.remove(char0);
    }

    @Override
    public Message parse(String input) {
        return new MessageParser(this,input).parse();
    }

    private static class FunctionEntry {

        private final ObjectOwner owner;
        private final String name;
        private final Function function;

        public FunctionEntry(ObjectOwner owner, String name, Function function) {
            this.owner = owner;
            this.name = name;
            this.function = function;
        }

        public ObjectOwner getOwner() {
            return owner;
        }

        public String getName() {
            return name;
        }

        public Function getFunction() {
            return function;
        }
    }
}
