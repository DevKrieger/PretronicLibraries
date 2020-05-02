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
import net.pretronic.libraries.utility.exception.OperationFailedException;
import net.pretronic.libraries.utility.io.archive.ZipArchive;

import java.io.File;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class JarSigner {

    private final File jarFile;
    private final String name;
    private final String privateKey;
    private final String publicKey;

    private Certificate certificate;

    public JarSigner(File jarFile){
        this(jarFile,"pretronic");
    }

    public JarSigner(File jarFile, String name){
        this.jarFile = jarFile;
        this.name = name;

        try{
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair pair = kpg.genKeyPair();
            this.privateKey = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
            this.publicKey = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
        }catch (Exception exception){
            throw new IllegalArgumentException("Could not generate key pair.",exception);
        }
    }

    public JarSigner(File jarFile, String name, String privateKey, String publicKey){
        this.jarFile = jarFile;
        this.name = name;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public void setCertificate(Certificate certificate){
        this.certificate = certificate;
    }

    public void writeCertificate(){
        if(this.certificate == null) return;
        ZipArchive archive = new ZipArchive(jarFile);
        archive.add("META-INF/"+name+".crt",this.certificate.getBase64Encoded());
        archive.compress();
    }

    public String sign(){
        writeCertificate();
        try{
            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(this.privateKey));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey pvt = kf.generatePrivate(ks);

            Signature signatureTool = Signature.getInstance("SHA1WithRSA");
            signatureTool.initSign(pvt);
            signatureTool.update(JarSignatureUtil.calculateSignatureCheckSum(this.jarFile));

            String signature = Base64.getEncoder().encodeToString(signatureTool.sign());

            ZipArchive archive = new ZipArchive(this.jarFile);

            archive.add("META-INF/"+name+".sig",signature);
            archive.add("META-INF/"+name+".key",this.publicKey);

            archive.compress();

            return signature;
        }catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException exception){
            throw new OperationFailedException(exception);
        }
    }

}
