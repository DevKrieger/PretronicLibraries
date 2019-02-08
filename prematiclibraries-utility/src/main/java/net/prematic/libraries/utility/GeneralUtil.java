package net.prematic.libraries.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.*;

/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.02.19 16:17
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

public class GeneralUtil {

    public static final Random RANDOM = new Random();
    public static final GsonBuilder GSON_BUILDER = new GsonBuilder().setPrettyPrinting();
    public static Gson GSON = GSON_BUILDER.create();
    public static final JsonParser PARSER = new JsonParser();

    public static void main(String[] args){
    }


    public static void createGSON(){
        GSON = GSON_BUILDER.create();
    }

    private static final char [] subset = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

    /*

    String utils

     */
    public static String getRandomString(final int size){
        char chars[] = new char[size];
        for(int i=0;i<chars.length;i++) chars[i] = subset[RANDOM.nextInt(subset.length)];
        return new String(chars);
        /*
        StringBuilder generator = new StringBuilder();
        for(int i=0;i<=size;i++) generator.append((char)(RANDOM.nextInt(25)+97));
        return generator.toString();
         */
    }
    public static String rotateString(String string){
        String newstring = "";
        char[] charArray = string.toCharArray();
        for(int i = charArray.length-1;i > -1;i--) newstring += charArray[i];
        return newstring;
    }
    public static Boolean equalsOne(String string, String... values){
        for(String value : values) if(value.equalsIgnoreCase(string)) return true;
        return false;
    }
    public static Boolean equalsALL(String string, String... values){
        for(String value : values) if(!value.equalsIgnoreCase(string)) return false;
        return true;
    }

    /**
     *
     * @param value A String which could be a number (int/long)
     * @return True for a string which is a number
     */
    public static boolean isNumber(String value){
        for(char c : value.toCharArray()) if(!Character.isDigit(c) && c != '.') return false;
        return true;
    }

    /*

    List and map Utils

     */

    public static int getMaxPages(int pagesize, List<?> list) {
        int max = pagesize;
        int i = list.size();
        if (i % max == 0) return i/max;
        double j = i / pagesize;
        int h = (int) Math.floor(j*100)/100;
        return h+1;
    }

    public static <U> U iterateOne(Iterable<U> list, AcceptAble<U> acceptAble) {
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptAble.accept(result)) return result;
        return null;
    }
    public static <U> void iterateForEach(Iterable<U> list, ForEach<U> forEach){
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) forEach.forEach(result);
    }
    public static <U> void iterateAcceptedForEach(Iterable<U> list, AcceptAble<U> acceptAble, ForEach<U> forEach) {
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptAble.accept(result)) forEach.forEach(result);
    }
    public static <U> List<U> iterateAcceptedReturn(Iterable<U> list, AcceptAble<U> acceptAble){
        List<U> result = new ArrayList<>();
        iterateAcceptedForEach(list,acceptAble,result::add);
        return result;
    }
    public static <U> void iterateAndRemove(Iterable<U> list, AcceptAble<U> acceptAble){
        Iterator<U> iterator = list.iterator();
        U result = null;
        while(iterator.hasNext() && (result=iterator.next()) != null) if(acceptAble.accept(result)) iterator.remove();
    }

    public static <U> U getHighestKey(final Map<U, Integer> map) {
        return map.entrySet().stream().sorted(Map.Entry.<U,Integer>comparingByValue().reversed()).limit(1).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    /*

    encryption tools

     */

    public static String encodeMD5(String password){
        return encodeMD5(password.getBytes());
    }
    public static String encodeMD5(byte[] bytes) {
        MessageDigest digest = getMessageDigest("MD5");
        byte[] hash = digest.digest(bytes);
        StringBuilder builder = new StringBuilder();
        for(int val : hash) builder.append(Integer.toHexString(val&0xff));
        return builder.toString();
    }
    public static MessageDigest getMessageDigest(String name) {
        try {
            return MessageDigest.getInstance(name);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /*

    Simple sub classes

     */


    /*

    An object accepter and for object executor, useful an for iterate methods.

     */
    public interface AcceptAble<T> {
        boolean accept(T object);
    }
    public interface ForEach<T> {
        void forEach(T object);
    }
}
