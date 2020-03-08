package net.prematic.libraries.jarsignature;

import net.prematic.libraries.jarsignature.certificate.Certificate;
import net.prematic.libraries.utility.exception.OperationFailedException;
import net.prematic.libraries.utility.io.archive.ZipArchive;

import java.io.File;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import static net.prematic.libraries.jarsignature.JarSignatureUtil.calculateSignatureCheckSum;

public class JarSigner {

    private final File jarFile;
    private final String name;
    private final String privateKey, publicKey;

    private Certificate certificate;

    public JarSigner(File jarFile){
        this(jarFile,"prematic");
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
            signatureTool.update(calculateSignatureCheckSum(this.jarFile));

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
