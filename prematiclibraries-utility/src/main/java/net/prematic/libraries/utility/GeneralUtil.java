package net.prematic.libraries.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.prematic.libraries.utility.map.Pair;
import sun.net.util.IPAddressUtil;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private static ExecutorService DEFAULT_EXECUTOR_SERVICE;

    public static final Random RANDOM = new Random();

    public static ExecutorService getDefaultExecutorService(){
        if(DEFAULT_EXECUTOR_SERVICE == null) DEFAULT_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
        return DEFAULT_EXECUTOR_SERVICE;
    }

    public void setDefaultExecutorService(ExecutorService executorService){
        DEFAULT_EXECUTOR_SERVICE = executorService;
    }


    public static boolean isNaturalNumber(String value){
        for(char c : value.toCharArray()) if(!Character.isDigit(c)) return false;
        return true;
    }

    /**
     *
     * @param value A String which could be a number (int/long)
     * @return True for a string which is a number
     */
    public static boolean isNumber(String value){
        boolean dot = false;
        for(char c : value.toCharArray()){
            if(!Character.isDigit(c)){
                if(c == '.' && !dot) dot = true;
                else return false;
            }
        }
        return true;
    }


    /*

    List and map Utils

     */

    public static int getMaxPages(int pageSize, List<?> list) {
        int i = list.size();
        if (i % pageSize == 0) return i/pageSize;
        double j = i/pageSize;
        int h = (int) Math.floor(j*100)/100;
        return h+1;
    }

    public static <U> List<U> getItemsOnPage(List<U> list, int page, int perPage){
        List<U> result = new ArrayList<>();

        int from = (perPage*(page - 1))+1;
        int to = page*perPage;

        for(int i = from;i<=to;i++){
            if(i > list.size()) break;
            result.add(list.get(i));
        }
        return result;
    }

    public static <U> U getRandomItem(Collection<U> collection){
        IPAddressUtil.textToNumericFormatV4("");
        int random = RANDOM.nextInt(collection.size());
        for(U object : collection) if (--random < 0) return object;
        return null;
    }

    public static <U> U getHighestKey(final Map<U, Integer> map) {
        return map.entrySet().stream().sorted(Map.Entry.<U,Integer>comparingByValue().reversed()).limit(1).map(Map.Entry::getKey).findFirst().orElse(null);
    }

}
