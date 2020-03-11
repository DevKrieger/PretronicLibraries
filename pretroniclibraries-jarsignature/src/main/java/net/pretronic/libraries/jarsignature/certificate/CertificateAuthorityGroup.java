/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:44
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

package net.pretronic.libraries.jarsignature.certificate;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class CertificateAuthorityGroup {

    public static CertificateAuthorityGroup DEFAULT = new CertificateAuthorityGroup();

    private Map<String,CertificateAuthority> authorities;

    public CertificateAuthorityGroup() {
        this.authorities = new LinkedHashMap<>();
        addAuthority(CertificateAuthority.PRETRONIC);
    }

    public Collection<CertificateAuthority> getAuthorities(){
        return this.authorities.values();
    }

    public void addAuthority(CertificateAuthority authority){
        this.authorities.put(authority.getName(),authority);
    }

    public CertificateValidity verify(String publicKey, Certificate certificate){
        if(certificate.getAuthority() != null){
            CertificateAuthority authority = this.authorities.get(certificate.getAuthority());
            if(authority != null) return authority.verify(publicKey, certificate);
        }
        return CertificateValidity.INVALID;
    }
}
