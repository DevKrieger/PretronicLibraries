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

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.type.DocumentFileType;
import net.prematic.libraries.utility.http.HttpClient;
import net.prematic.libraries.utility.http.HttpResult;

public class CertificateAuthority {

    private static final String HEADER_VERIFY_PUBLIC_KEY = "VERIFY_PUBLIC_KEY";
    private static final String HEADER_VERIFY_CERTIFICATE = "VERIFY_CERTIFICATE";

    public static final CertificateAuthority PREMATIC = new CertificateAuthority("Prematic","https://verify.prematic.net/api/v1/verify/");

    private final String name, apiUrl;

    public CertificateAuthority(String name, String apiUrl) {
        this.name = name;
        this.apiUrl = apiUrl;
    }

    public String getName() {
        return name;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public CertificateValidity verify(String publicKey, Certificate certificate){
        HttpClient client = new HttpClient();
        client.setUrl(apiUrl);
        client.setProperty(HEADER_VERIFY_PUBLIC_KEY,publicKey);
        client.setProperty(HEADER_VERIFY_CERTIFICATE,certificate.getBase64Encoded());

        HttpResult result = client.connect();

        if(result.getCode() == 200){
            Document document = result.getContent(DocumentFileType.JSON.getReader());
            return CertificateValidity.valueOf(document.getString("validity").toUpperCase());
        }
        return CertificateValidity.INVALID;
    }
}
