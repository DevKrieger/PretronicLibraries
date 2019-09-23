/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.09.19, 22:20
 *
 * The PrematicLibraries Project is under the Apache License, version 2.0 (the "License");
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

package net.prematic.libraries.document;

import net.prematic.libraries.document.adapter.defaults.*;
import net.prematic.libraries.document.simple.SimpleDocumentFactory;
import net.prematic.libraries.document.type.DocumentFileType;
import net.prematic.libraries.utility.Iterators;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentRegistry {

    private static Collection<DocumentFileType> TYPES = new HashSet<>();
    private static DocumentFactory FACTORY = new SimpleDocumentFactory();
    private static DocumentContext DEFAULT_CONTEXT = FACTORY.newContext();

    static {
        registerType(DocumentFileType.JSON);
        registerType(DocumentFileType.YAML);
        registerType(DocumentFileType.BINARY);
        registerType(DocumentFileType.XML);

        getDefaultContext().registerAdapter(UUID.class,new UUIDAdapter());
        getDefaultContext().registerAdapter(Calendar.class,new CalendarAdapter());
        getDefaultContext().registerAdapter(Class.class,new ClassAdapter());
        getDefaultContext().registerAdapter(Currency.class,new CurrencyAdapter());
        getDefaultContext().registerAdapter(Date.class,new DateAdapter());
        getDefaultContext().registerAdapter(File.class,new FileAdapter());
        getDefaultContext().registerAdapter(InetAddress.class,new InetAddressAdapter());
        getDefaultContext().registerAdapter(InetSocketAddress.class,new InetSocketAddressAdapter());
        getDefaultContext().registerAdapter(java.sql.Date.class,new SqlDateAdapter());
        getDefaultContext().registerAdapter(Time.class,new SqlTimeAdapter());
        getDefaultContext().registerAdapter(TimeZone.class,new TimeZoneAdapter());
        getDefaultContext().registerAdapter(URL.class,new URLAdapter());
        getDefaultContext().registerAdapter(BigInteger.class,new BigNumberAdapter.Integer());
        getDefaultContext().registerAdapter(BigDecimal.class,new BigNumberAdapter.Decimal());
        getDefaultContext().registerAdapter(AtomicInteger.class,new AtomicAdapter.Integer());
        getDefaultContext().registerAdapter(AtomicLong.class,new AtomicAdapter.Long());
        getDefaultContext().registerAdapter(AtomicBoolean.class,new AtomicAdapter.Boolean());

        getDefaultContext().registerHierarchyAdapter(Collection.class,new CollectionAdapter());
        getDefaultContext().registerHierarchyAdapter(Map.class,new MapAdapter());
    }

    public static Collection<DocumentFileType> getTypes() {
        return TYPES;
    }

    public static DocumentFileType getType(String name){
        return Iterators.findOne(TYPES, type -> type.getName().equalsIgnoreCase(name));
    }

    public static DocumentFileType getTypeByEnding(String ending){
        return Iterators.findOne(TYPES, type -> type.getEnding().equalsIgnoreCase(ending));
    }

    public static void registerType(DocumentFileType type){
        TYPES.add(type);
    }


    public static DocumentContext getDefaultContext() {
        return DEFAULT_CONTEXT;
    }

    public static void setDefaultContext(DocumentContext defaultContext) {
        DEFAULT_CONTEXT = defaultContext;
    }


    public static DocumentFactory getFactory() {
        return FACTORY;
    }

    public static void setFactory(DocumentFactory factory) {
        FACTORY = factory;
    }
}
