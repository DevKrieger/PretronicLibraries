package net.prematic.libraries.caching;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 20.10.18 17:27
 *
 */


import java.util.Random;
import java.util.UUID;

public class Test {

    public static void main(String[] args){


        long insert = System.currentTimeMillis();


        PrematicCache<TestO> docs = new PrematicCache<>();
        
        //docs.setMaxSize(50000);

        docs.registerQuery("one", new ObjectQuery<TestO>() {
            @Override
            public boolean is(Object identifier, TestO object) {
                return object.getName().equals(identifier);
            }
        });
        docs.registerQuery("two", new ObjectQuery<TestO>() {
            @Override
            public boolean is(Object identifier, TestO object) {
                return object.getUuid().equals(identifier);
            }
        });

        for(int i = 0; i<1000000;i++)docs.insert(new TestO(getRandomString(18),UUID.randomUUID()));
        UUID key = UUID.randomUUID();
        docs.insert(new TestO(getRandomString(18),key));
        System.out.println(System.currentTimeMillis()-insert+"ms "+docs.size());
        long load = System.currentTimeMillis();
        System.out.println("R: "+docs.get("two",key).getName());
        System.out.println(System.currentTimeMillis()-load+"ms");

    }
    private static Random RANDOM = new Random();
    public static String getRandomString(final int size){
        char data = ' ';
        String dat = "";
        for(int i=0;i<=size;i++) {
            data = (char)(RANDOM.nextInt(25)+97);
            dat = data+dat;
        }
        return dat;
    }
}
