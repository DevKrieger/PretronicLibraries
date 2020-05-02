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

package net.pretronic.libraries.jarsignature;

import net.pretronic.libraries.jarsignature.certificate.Certificate;
import net.pretronic.libraries.jarsignature.certificate.CertificateAuthorityGroup;
import net.pretronic.libraries.jarsignature.certificate.CertificateValidity;
import net.pretronic.libraries.utility.io.FileUtil;

import java.io.File;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarVerifier {

    private final CertificateAuthorityGroup authorityGroup;
    private final String publicKey;
    private final String signature;
    private final Certificate certificate;

    private CertificateValidity validity;
    private boolean signatureCorrect;

    public JarVerifier(File jarFile){
        this(jarFile,"pretronic");
    }

    public JarVerifier(File jarFile,CertificateAuthorityGroup authorityGroup){
        this(jarFile,"pretronic",authorityGroup);
    }

    public JarVerifier(File jarFile, String name){
        this(jarFile,name,CertificateAuthorityGroup.DEFAULT);
    }

    public JarVerifier(File jarFile, String name, CertificateAuthorityGroup authorityGroup){
        this.authorityGroup = authorityGroup;
        try{
            JarFile jar = new JarFile(jarFile);

            JarEntry signature = jar.getJarEntry("META-INF/"+name+".sig");
            JarEntry publicKey = jar.getJarEntry("META-INF/"+name+".key");
            JarEntry certificate = jar.getJarEntry("META-INF/"+name+".crt");

            this.signature = FileUtil.readContent(jar.getInputStream(signature));
            this.publicKey = FileUtil.readContent(jar.getInputStream(publicKey));

            jar.close();

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(this.publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            Signature signatureTool = Signature.getInstance("SHA1WithRSA");
            signatureTool.initVerify(keyFactory.generatePublic(keySpec));
            signatureTool.update(JarSignatureUtil.calculateSignatureCheckSum(jarFile));

            this.signatureCorrect = signatureTool.verify(Base64.getDecoder().decode(this.signature));

            if(this.signatureCorrect){
                if(certificate != null) this.certificate = Certificate.load(FileUtil.readContent(jar.getInputStream(certificate)));
                else this.certificate = null;
            }else this.certificate = null;

        }catch (Exception exception){
            throw new IllegalArgumentException("Could not verify jar.",exception);
        }
    }

    public String getPublicKey(){
        return this.publicKey;
    }

    public String getSignature(){
        return this.signature;
    }

    public Certificate getCertificate(){
        return this.certificate;
    }

    public boolean isSinged(){
        return this.signatureCorrect;
    }

    public boolean isVerified(){
        return isSinged() && validity == CertificateValidity.VALID;
    }

    public CertificateValidity verify(){
        if(isSinged() && this.certificate != null && this.authorityGroup != null){
            this.validity = this.authorityGroup.verify(publicKey,certificate);
            return this.validity;
        }
        return CertificateValidity.INVALID;
    }
}
