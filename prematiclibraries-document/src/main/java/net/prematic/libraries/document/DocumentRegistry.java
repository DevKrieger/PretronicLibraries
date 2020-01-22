/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 20.12.19, 22:40
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

/**
 * The {@link DocumentRegistry} is a static class which provides the {@link DocumentFactory} and all
 * available file types ({@link DocumentFileType}). This class also manages
 * global adapters {@link net.prematic.libraries.document.adapter.DocumentAdapter}.
 */
public class DocumentRegistry {

    private static Collection<DocumentFileType> TYPES = new HashSet<>();
    private static DocumentFactory FACTORY = new SimpleDocumentFactory();
    private static DocumentContext DEFAULT_CONTEXT = FACTORY.newContext();

    static {
        registerType(DocumentFileType.JSON);
        registerType(DocumentFileType.YAML);
        registerType(DocumentFileType.BINARY);
        registerType(DocumentFileType.XML);
        registerType(DocumentFileType.PROPERTIES);

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

    private DocumentRegistry(){
        throw new UnsupportedOperationException();
    }

    /**
     * Get all available file types.
     *
     * @return All types in a collection
     */
    public static Collection<DocumentFileType> getTypes() {
        return TYPES;
    }

    /**
     * Get a type by the name
     *
     * @param name The name of the type
     * @return The type, or null when not available
     */
    public static DocumentFileType getType(String name){
        return Iterators.findOne(TYPES, type -> type.getName().equalsIgnoreCase(name));
    }

    /**
     * Get a type by the ending
     *
     * @param ending The ending of the type (json, yml, bin)
     * @return The type, or null when not available
     */
    public static DocumentFileType getTypeByEnding(String ending){
        return Iterators.findOne(TYPES, type -> type.getEnding().equalsIgnoreCase(ending));
    }

    /**
     * Register a new file type.
     *
     * @param type The type to register
     */
    public static void registerType(DocumentFileType type){
        TYPES.add(type);
    }

    /**
     * Find a file type by a file (Must end with extension name)
     * @param file The file
     * @return The type of the file or null when not available
     */
    public static DocumentFileType findType(File file){
        String name = file.getName();
        int index = name.lastIndexOf('.');
        return index > 0 ? getTypeByEnding(name.substring(index+1)) : null;
    }

    /**
     * Get the default context (Contains all global adapters).
     *
     * @return The global context
     */
    public static DocumentContext getDefaultContext() {
        return DEFAULT_CONTEXT;
    }

    /**
     * Change the global context.
     *
     * <p>Warning: If you change this, you will override all global registered adapters.</p>
     *
     * @param defaultContext The new context
     */
    public static void setDefaultContext(DocumentContext defaultContext) {
        DEFAULT_CONTEXT = defaultContext;
    }

    /**
     * Get the document factory.
     *
     * @return The registered factory
     */
    public static DocumentFactory getFactory() {
        return FACTORY;
    }

    /**
     * Set a new document factory
     *
     * @param factory The nw factory
     */
    public static void setFactory(DocumentFactory factory) {
        FACTORY = factory;
    }
}
