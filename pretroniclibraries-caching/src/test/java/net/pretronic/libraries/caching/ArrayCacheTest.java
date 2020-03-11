/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:42
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

package net.pretronic.libraries.caching;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayCacheTest {

    @Test
    protected void size() {
        int expected = 3;

        Cache<TestObject> cache = new ArrayCache<>();
        cache.insert(new TestObject("Hey",10));
        cache.insert(new TestObject("Nop",10));
        cache.insert(new TestObject("Test",10));

        int result = cache.size();

        assertEquals(expected,result);
    }

    @Test
    protected void clear() {
        int expected = 0;

        Cache<TestObject> cache = new ArrayCache<>();
        cache.insert(new TestObject("Hey",10));
        cache.insert(new TestObject("Nop",10));
        cache.insert(new TestObject("Test",10));

        cache.clear();
        int result = cache.size();

        assertEquals(expected,result);
    }

    @Test
    protected void getWithQuery() {
        TestObject expected = new TestObject("Test",10);

        Cache<TestObject> cache = new ArrayCache<>();
        cache.insert(new TestObject("Hey",10));
        cache.insert(new TestObject("Nop",10));
        cache.insert(new TestObject("Tester",10));
        cache.insert(expected);
        //No type validation implemented!
        cache.registerQuery("TestSearcher", (item, identifiers) -> item.name.equalsIgnoreCase((String) identifiers[0]));

        TestObject result = cache.get("TestSearcher","Test");

        assertEquals(expected,result);
    }

    @Test
    protected void getWithPredicate() {
        TestObject expected = new TestObject("Test",10);

        Cache<TestObject> cache = new ArrayCache<>();
        cache.insert(new TestObject("Hey",10));
        cache.insert(new TestObject("Nop",10));
        cache.insert(new TestObject("Test45",10));
        cache.insert(expected);

        TestObject result = cache.get(item -> item.name.equalsIgnoreCase("Test"));

        assertEquals(expected,result);
    }

    @Test
    protected void setMaxSize() {
        int expected = 4;

        Cache<TestObject> cache = new ArrayCache<>();
        cache.setMaxSize(4);
        cache.insert(new TestObject("Hey",10));
        cache.insert(new TestObject("Nop",10));
        cache.insert(new TestObject("Test",10));
        cache.insert(new TestObject("sadsad",105));
        cache.insert(new TestObject("Nosdasdp",150));
        cache.insert(new TestObject("sdasdsds",10));

        int result = cache.size();

        assertEquals(expected,result);
    }


    private static class TestObject {

        private String name;
        private int age;

        private TestObject(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
