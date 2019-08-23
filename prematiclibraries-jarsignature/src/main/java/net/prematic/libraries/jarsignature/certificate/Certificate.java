/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 25.05.19 22:26
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

package net.prematic.libraries.jarsignature.certificate;

import net.prematic.libraries.utility.io.IORuntimeException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Certificate{

    private final Properties properties;

    public Certificate(Properties properties) {
        this.properties = properties;
    }

    public String getOrganisation(){
        return properties.getProperty("organisation");
    }

    public String getAuthority(){
        return properties.getProperty("authority");
    }

    public String getVersion(){
        return properties.getProperty("version");
    }

    public String getAuthor(){
        return properties.getProperty("author");
    }

    public String getWebsite(){
        return properties.getProperty("website");
    }

    public String getCopyright(){
        return properties.getProperty("copyright");
    }

    public long getExpire(){
        try{
            return Long.parseLong(properties.getProperty("expire"));
        }catch (Exception ignored){
            return 0;
        }
    }

    public long getRegistration(){
        try{
            return Long.parseLong(properties.getProperty("registration"));
        }catch (Exception ignored){
            return 0;
        }
    }

    public byte[] getEncoded(){
        return getBase64Encoded().getBytes(StandardCharsets.UTF_8);
    }

    public String getBase64Encoded(){
        try{
            StringWriter writer = new StringWriter();
            properties.store(writer,null);
            return Base64.getEncoder().encodeToString(writer.toString().getBytes(StandardCharsets.UTF_8));
        }catch (IOException exception){
            throw new IORuntimeException(exception);
        }
    }

    public static Certificate load(String base64Content){
        try{
            Properties properties = new Properties();
            properties.load(new StringReader(new String(Base64.getDecoder().decode(base64Content),StandardCharsets.UTF_8)));
            return new Certificate(properties);
        }catch (IOException exception){
            throw new IORuntimeException(exception);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {

        private Properties properties;

        public Builder() {
            this.properties = new Properties();
            this.properties.setProperty("registration",String.valueOf(System.currentTimeMillis()));
            this.properties.setProperty("expire",String.valueOf(System.currentTimeMillis()+TimeUnit.DAYS.toMillis(365)));
        }

        public Builder setOrganisation(String organisation){
            properties.setProperty("organisation",organisation);
            return this;
        }

        public Builder setAuthority(String authority){
            properties.setProperty("authority",authority);
            return this;
        }

        public Builder setVersion(String version){
            properties.setProperty("version",version);
            return this;
        }

        public Builder setAuthor(String author){
            properties.setProperty("author",author);
            return this;
        }

        public Builder setWebsite(String website){
            properties.setProperty("website",website);
            return this;
        }

        public Builder setCopyright(String copyright){
            properties.setProperty("copyright",copyright);
            return this;
        }

        public Builder setExpire(long expire){
            properties.setProperty("expire",String.valueOf(expire));
            return this;
        }

        public Certificate build(){
            return new Certificate(this.properties);
        }
    }
}
