/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 25.05.19 22:20
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

package net.prematic.libraries.utility.http;

import net.prematic.libraries.utility.io.FileUtil;
import net.prematic.libraries.utility.io.IORuntimeException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpClient {

    public static final String PROPERTY_USER_AGENT = "User-Agent";
    public static final String PROPERTY_COOKIE = "Cookie";
    public static final String PROPERTY_COOKIE_SET = "Set-Cookie";
    public static final String PROPERTY_CONTENT_TYPE = "Content-Type";
    public static final String PROPERTY_CONTENT_LENGTH = "Content-Length";
    public static final String PROPERTY_ACCEPTED_ENCODING = "Accept-Encoding";
    public static final String PROPERTY_ACCEPTED_LANGUAGE = "Accept-Language";
    public static final String PROPERTY_ACCEPTED_CHARSET = "Accept-Charsete";
    public static final String PROPERTY_AUTHORIZATION = "Authorization";

    private static final int DEFAULT_TIMEOUT = 3000;
    private static final URL DEFAULT_URL = FileUtil.newUrl("http://localhost/");
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 Firefox/26.0";

    private URL url;
    private int timeOut;
    private String requestMethod, content;
    private boolean saveCookies;

    private final Map<String,List<String>> properties;
    private final Map<String,String> parameters;

    public HttpClient() {
        this.url = DEFAULT_URL;
        this.timeOut = DEFAULT_TIMEOUT;
        this.requestMethod = HttpMethod.GET;
        this.parameters = new LinkedHashMap<>();
        this.properties = new LinkedHashMap<>();
        this.properties.put(DEFAULT_USER_AGENT,Arrays.asList(DEFAULT_USER_AGENT));
    }

    public void setUrl(URL url){
       this.url = url;
    }

    public void setUrl(String url){
        this.url = FileUtil.newUrl(url);
    }

    public void setUrl(String protocol,String host,int port,String path,String query){
        setUrl(protocol+"://"+host+":"+port+"/"+path+"?"+query);
    }

    public void setProtocol(String protocol){
        setUrl(protocol,url.getHost(),url.getPort(),url.getPath(),url.getQuery());
    }

    public void setHost(String host){
        setUrl(url.getProtocol(),host,url.getPort(),url.getPath(),url.getQuery());
    }

    public void setPort(int port){
        setUrl(url.getProtocol(),url.getHost(),port,url.getPath(),url.getQuery());
    }

    public void setPath(String path){
        setUrl(url.getProtocol(),url.getHost(),url.getPort(),path,url.getQuery());
    }

    public void setQuery(String query){
        setUrl(url.getProtocol(),url.getHost(),url.getPort(),url.getPath(),query);
    }

    public void setTimeout(int timeout){
        this.timeOut = timeout;
    }

    public void setAgent(String userAgent){
        setProperty(PROPERTY_USER_AGENT,userAgent);
    }

    public void setCookie(String cookies){
        setProperty(PROPERTY_COOKIE,cookies);
    }

    public void addCookie(String cookies){
        addProperty(PROPERTY_COOKIE,cookies);
    }

    public void setRequestMethod(String method){
        this.requestMethod = method;
    }

    public void setContentType(String contentType){
        setProperty(PROPERTY_CONTENT_TYPE,contentType);
    }

    public void setContentLength(String contentType){
        setProperty(PROPERTY_CONTENT_LENGTH,contentType);
    }

    public void setAcceptedEncoding(String encoding){
        setProperty(PROPERTY_ACCEPTED_ENCODING,encoding);
    }

    public void setAcceptedLanguage(String language){
        setProperty(PROPERTY_ACCEPTED_LANGUAGE,language);
    }

    public void setAcceptedCharset(String charset){
        setProperty(PROPERTY_ACCEPTED_CHARSET,charset);
    }

    public void setBasicAuthentication(String username, String password){
        setProperty(PROPERTY_AUTHORIZATION,"Basic "+ Base64.getEncoder().encodeToString((username+":"+password).getBytes(StandardCharsets.UTF_8)));
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setSaveCookies(boolean saveCookies){
        this.saveCookies = saveCookies;
    }

    public void setProperty(String key, Object value){
        List<String> property = this.properties.get(key);
        if(property == null){
            property = new ArrayList<>();
            this.properties.put(key,property);
        }else property.clear();
        property.add(value.toString());
    }

    public void addProperty(String key, Object value){
        List<String> property = this.properties.computeIfAbsent(key, k -> new ArrayList<>());
        property.add(value.toString());
    }

    public void setParameter(String key, Object value){
        this.parameters.put(key,value.toString());
    }

    public HttpResult connect(){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(this.requestMethod);
            connection.setConnectTimeout(this.timeOut);
            connection.setReadTimeout(this.timeOut);

            for(Map.Entry<String, List<String>> entry : this.properties.entrySet()) {
                entry.getValue().forEach(value -> connection.setRequestProperty(entry.getKey(),value));
            }

            if(content != null || (!this.parameters.isEmpty())){
                connection.setDoOutput(true);
                DataOutputStream output = new DataOutputStream(connection.getOutputStream());
                if(content != null) output.writeBytes(content);
                else output.writeBytes(buildParameters(parameters));
                output.flush();
                output.close();
            }

            if(saveCookies) this.properties.put("Cookie",connection.getHeaderFields().get(PROPERTY_COOKIE_SET));

            connection.disconnect();
            return new HttpResult(connection.getResponseCode(),connection.getHeaderFields()
                    ,connection.getResponseCode()==200?connection.getInputStream():connection.getErrorStream());
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    public static String buildParameters(Map<String, String> parameters) {
        try{
            StringBuilder result = new StringBuilder();
            for(Map.Entry<String, String> entry : parameters.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append('=');
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append('&');
            }
            if(result.length() > 2) result.setLength(result.length()-1);
            return result.toString();
        }catch (IOException exception){
            throw new IORuntimeException(exception);
        }
    }

}
