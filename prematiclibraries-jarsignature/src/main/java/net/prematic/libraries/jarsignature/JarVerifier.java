package net.prematic.libraries.jarsignature;

import net.prematic.libraries.jarsignature.certificate.Certificate;
import net.prematic.libraries.jarsignature.certificate.CertificateAuthorityGroup;
import net.prematic.libraries.jarsignature.certificate.CertificateValidity;
import net.prematic.libraries.utility.io.FileUtil;

import java.io.File;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static net.prematic.libraries.jarsignature.JarSignatureUtil.calculateSignatureCheckSum;

public class JarVerifier {

    private final CertificateAuthorityGroup authorityGroup;
    private final String publicKey, signature;
    private final Certificate certificate;

    private CertificateValidity validity;
    private boolean signatureCorrect;

    public JarVerifier(File jarFile){
        this(jarFile,"prematic");
    }

    public JarVerifier(File jarFile,CertificateAuthorityGroup authorityGroup){
        this(jarFile,"prematic",authorityGroup);
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
            signatureTool.update(calculateSignatureCheckSum(jarFile));

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
