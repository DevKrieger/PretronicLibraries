/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 25.05.19 20:24
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

package net.prematic.libraries.jarsignature;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.prematic.libraries.utility.io.IORuntimeException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarSignatureUtil {

    public static byte[] calculateSignatureCheckSum(File jarFile){
        try{
            JarFile jar = new JarFile(jarFile);
            Enumeration<JarEntry> entries = jar.entries();
            ByteBuf buffer = Unpooled.buffer();

            JarEntry entry;
            while(entries.hasMoreElements()){
                entry = entries.nextElement();
                if(entry.getName().startsWith("META-INF/") && (entry.getName().endsWith(".sig") || entry.getName().endsWith(".key"))) continue;
                buffer.writeBytes(entry.getName().getBytes(StandardCharsets.UTF_8));
                buffer.writeLong(entry.getMethod());
                buffer.writeLong(entry.getSize());
                buffer.writeLong(entry.getCompressedSize());
                buffer.writeLong(entry.getCrc());
            }
            jar.close();

            byte[] result = new byte[buffer.readableBytes()];
            buffer.readBytes(result);

            return result;
        }catch (IOException exception){
            throw new IORuntimeException(exception);
        }
    }

}
