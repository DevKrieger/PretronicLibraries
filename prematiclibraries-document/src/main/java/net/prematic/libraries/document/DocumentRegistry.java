/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 09.06.19 15:48
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

import net.prematic.libraries.document.adapter.DocumentAdapter;
import net.prematic.libraries.document.adapter.DocumentAdapterFactory;
import net.prematic.libraries.document.adapter.defaults.*;
import net.prematic.libraries.document.annotations.DocumentIgnored;
import net.prematic.libraries.document.annotations.DocumentName;
import net.prematic.libraries.document.simple.SimpleDocumentFactory;
import net.prematic.libraries.document.type.DocumentFileType;
import net.prematic.libraries.utility.Iterators;
import net.prematic.libraries.utility.reflect.Primitives;
import net.prematic.libraries.utility.reflect.ReflectionUtil;
import net.prematic.libraries.utility.reflect.TypeReference;
import net.prematic.libraries.utility.reflect.UnsafeInstanceCreator;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class  DocumentRegistry {

    private static DocumentFactory FACTORY = new SimpleDocumentFactory();
    private static Collection<DocumentFileType> TYPES = new HashSet<>();
    private static Map<Type, DocumentAdapter> ADAPTERS = new LinkedHashMap<>();
    private static Collection<DocumentAdapterFactory> ADAPTER_FACTORIES = new HashSet<>();


    static {
        registerType(DocumentFileType.JSON);
        registerType(DocumentFileType.YAML);

        registerAdapter(UUID.class,new UUIDAdapter());
        registerAdapter(Calendar.class,new CalendarAdapter());
        registerAdapter(Class.class,new ClassAdapter());
        registerAdapter(Currency.class,new CurrencyAdapter());
        registerAdapter(Date.class,new DateAdapter());
        registerAdapter(File.class,new FileAdapter());
        registerAdapter(InetAddress.class,new InetAddressAdapter());
        registerAdapter(java.sql.Date.class,new SqlDateAdapter());
        registerAdapter(Time.class,new SqlTimeAdapter());
        registerAdapter(TimeZone.class,new TimeZoneAdapter());
        registerAdapter(URL.class,new URLAdapter());
        registerAdapter(BigInteger.class,new BigNumberAdapter.Integer());
        registerAdapter(BigDecimal.class,new BigNumberAdapter.Decimal());
        registerAdapter(AtomicInteger.class,new AtomicAdapter.Integer());
        registerAdapter(AtomicLong.class,new AtomicAdapter.Long());
        registerAdapter(AtomicBoolean.class,new AtomicAdapter.Boolean());

        registerHierarchyAdapter(Collection.class,new CollectionAdapter());
        registerHierarchyAdapter(Map.class,new MapAdapter());
    }

    public static DocumentFactory getFactory(){
        return FACTORY;
    }

    public static DocumentFileType getType(String name){
        return Iterators.findOne(TYPES, type -> type.getName().equalsIgnoreCase(name));
    }

    public static DocumentFileType getTypeByEnding(String ending){
        return Iterators.findOne(TYPES, type -> type.getEnding().equalsIgnoreCase(ending));
    }

    public static Collection<DocumentFileType> getTypes(){
        return TYPES;
    }

    public static void registerType(DocumentFileType type){
        TYPES.add(type);
    }

    public static <T> void registerAdapter(Class<T> type, DocumentAdapter<T> adapter){
        ADAPTERS.put(TypeReference.getRawType(type),adapter);
    }

    public static <T> void registerHierarchyAdapter(Class<T> type, DocumentAdapter<T> adapter){
        ADAPTER_FACTORIES.add(HierarchyAdapterFactory.newFactory(adapter,type));
    }

    public static <T> void registerAdapterFactory(DocumentAdapterFactory factory){
        ADAPTER_FACTORIES.add(factory);
    }

    public static DocumentEntry serialize(Object value){
        return serialize(null,value);
    }

    public static DocumentEntry serialize(String key, Object value){
        if(value == null || Primitives.isPrimitive(value)) return getFactory().newPrimitiveEntry(key,value);
        else if(value.getClass().isArray()) return serializeArray(key, value);
        return serializeObject(key, value);
    }

    public static DocumentEntry serializeArray(String key, Object object){
        ArrayEntry array = getFactory().newArrayEntry(key);

        for(int i = 0;i<Array.getLength(object);i++) array.entries().add(serialize("array-index-"+i,Array.get(object,i)));
        return array;
    }

    public static DocumentEntry serializeObject(String key, Object value){
        DocumentAdapter adapter = findAdapter(new TypeReference<>(value.getClass()));
        if(adapter != null) return adapter.write(key, value);

        Document document = getFactory().newDocument(key);
        for(Field field : value.getClass().getDeclaredFields()){
            if(!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())){
               try{
                   field.setAccessible(true);
                   if(field.getAnnotation(DocumentIgnored.class) == null){
                       DocumentName name = field.getAnnotation(DocumentName.class);
                       document.entries().add(serialize(name!=null?name.value():field.getName(),field.get(value)));
                   }
               }catch (Exception ignored){}
            }
        }
        return document;
    }

    public static <T> T deserialize(DocumentEntry entry, Class<T> clazz){
        return deserialize(entry,new TypeReference<>(clazz));
    }

    public static <T> T deserialize(DocumentEntry entry, Type type){
        return deserialize(entry,new TypeReference<>(type));
    }

    public static <T> T deserialize(DocumentEntry entry,TypeReference type){
        return (T) deserializeRaw(entry, type);
    }

    public static Object deserializeRaw(DocumentEntry entry,TypeReference type){
        if(entry.isPrimitive()){
            if(Primitives.isPrimitive(type.getRawClass())){
                if(type.getRawClass() == String.class){
                    return entry.toPrimitive().getAsString();
                }else if(type.getRawClass() == int.class || type.getRawClass() == Integer.class){
                    return entry.toPrimitive().getAsInt();
                }else if(type.getRawClass() == long.class || type.getRawClass() == Long.class){
                    return entry.toPrimitive().getAsLong();
                }else if(type.getRawClass() == double.class || type.getRawClass() == Double.class){
                    return entry.toPrimitive().getAsDouble();
                }else if(type.getRawClass() == short.class || type.getRawClass() == Short.class){
                    return entry.toPrimitive().getAsShort();
                }else if(type.getRawClass() == float.class || type.getRawClass() == Float.class){
                    return entry.toPrimitive().getAsFloat();
                }else if(type.getRawClass() == byte.class || type.getRawClass() == Byte.class){
                    return entry.toPrimitive().getAsByte();
                }else if(type.getRawClass() == boolean.class || type.getRawClass() == Boolean.class){
                    return entry.toPrimitive().getAsBoolean();
                }else if(type.getRawClass() == char.class || type.getRawClass() == Character.class){
                    return entry.toPrimitive().getAsCharacter();
                }else throw new IllegalArgumentException("Invalid Primitive type");
            }else if(type.getRawClass().isEnum()) return Enum.valueOf(type.getRawClass(),entry.toPrimitive().getAsString());
            else{
                DocumentAdapter adapter = findAdapter(type);
                if(adapter != null) return adapter.read(entry, type);
                else throw new IllegalArgumentException("Invalid Primitive type");
            }
        }else if(type.isArray() && entry.isObject()){
            ArrayList<?> instance = new ArrayList();
            entry.toDocument().forEach(entry1 -> instance.add(DocumentRegistry.deserialize(entry1,type.getRawType())));

            Object array = Array.newInstance((Class<?>) type.getRawType(), instance.size());
            for (int i = 0; i < instance.size(); i++) Array.set(array, i, instance.get(i));

            return array;
        }else if(entry.isObject()){
            DocumentAdapter adapter = findAdapter(type);
            if(adapter != null) return adapter.read(entry, type);

            Object instance = UnsafeInstanceCreator.newInstance(type.getRawClass());

            Document document = entry.toDocument();
            for(Field field : ReflectionUtil.getAllFields(type.getRawClass())){
                if(!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())){
                    try{
                        field.setAccessible(true);
                        if(field.getAnnotation(DocumentIgnored.class) == null){
                            DocumentName name = field.getAnnotation(DocumentName.class);
                            String endName = name!=null?name.value():field.getName();

                            if(document.contains(endName)) field.set(instance,deserialize(document.getEntry(endName),field.getGenericType()));
                            else field.set(instance,null);
                        }
                    }catch (Exception ignored){
                        ignored.printStackTrace();
                    }
                }
            }
            return instance;
        }else throw new IllegalArgumentException("Invalid object type");
    }

    public static <T> DocumentAdapter<T> findAdapter(TypeReference<T> reference){
        DocumentAdapter<T>  adapter = ADAPTERS.get(reference.getRawType());
        if(adapter != null) return adapter;
        for(DocumentAdapterFactory factory : ADAPTER_FACTORIES){
            adapter = factory.create(reference);
            if(adapter != null){
                ADAPTERS.put(reference.getRawType(),adapter);
                return adapter;
            }
        }
        return null;
    }
}
