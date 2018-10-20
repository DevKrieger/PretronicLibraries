package net.prematic.libraries.utility.map;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 20.10.18 16:46
 *
 */

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.prematic.libraries.utility.Document;
import net.prematic.libraries.utility.GeneralUtil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args){


        long insert = System.currentTimeMillis();


        Cache<String,Document> docs = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES).build();
        for(int i = 0; i<1000000;i++)docs.put(GeneralUtil.getRandomString(19),new Document());
        String key = GeneralUtil.getRandomString(19);
        docs.put(key,new Document());
        System.out.println(System.currentTimeMillis()-insert+"ms");
        System.out.println(docs.size());
        long load = System.currentTimeMillis();
        System.out.println(docs.getIfPresent(key).toJson());
        System.out.println(System.currentTimeMillis()-load+"ms");

    }

}
