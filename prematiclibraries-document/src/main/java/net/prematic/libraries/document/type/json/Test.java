/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 14.12.19, 13:10
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

package net.prematic.libraries.document.type.json;

import net.prematic.libraries.document.Document;

import java.io.File;

public class Test {

    public static void mainu(String[] args) {
        JsonDocumentReader reader = new JsonDocumentReader();

        Document result = reader.read(new File("test.json"));



        System.out.println();
    }

    public static void mainF(String[] args) {
        Document document = Document.newDocument();

        document.getAttributes().set("Test",1000);
        document.getAttributes().set("HHH","sadasdsad");

        document.add("test","Hallo");
        document.add("test2",false);
        document.add("test3",null);
        document.add("test4",838474);

        document.add("array",new TestObject[]{new TestObject("test",100)
                ,new TestObject("test3",10)
                ,new TestObject("test2",1050) ,new TestObject(null,1050)});

        document.getEntry("test").getAttributes().set("testX","hallo");
        document.getEntry("test").getAttributes().set("testX2","hallo3");

        //document.getEntry("array").getAttributes().set("et","adad");

        JsonDocumentWriter writer = new JsonDocumentWriter();
        writer.write(new File("output.json"),document);
    }

    public static void main(String[] args) {
        JsonDocumentReader reader = new JsonDocumentReader();
        JsonDocumentWriter writer = new JsonDocumentWriter();

        Document result = reader.read(new File("testFinal.json"));

        writer.write(new File("output.json"),result,false);
        System.out.println();
    }

    public static class TestObject {

        private String name;
        private int age;

        public TestObject(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
