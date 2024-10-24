package practicacifrado;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.stream.Stream;

import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Utils {

    public static PublicKey leerClavePublica(String path) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA", "BC");

        byte[] bufferPub = Files.readAllBytes(Paths.get(path));

        X509EncodedKeySpec clavePublicaSpec = new X509EncodedKeySpec(bufferPub);
        PublicKey clavePublica = keyFactoryRSA.generatePublic(clavePublicaSpec);

        return clavePublica;
    }

    public static PrivateKey leerClavePrivada(String path) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA", "BC");

        byte[] bufferPriv = Files.readAllBytes(Paths.get(path));

        PKCS8EncodedKeySpec clavePrivadaSpec = new PKCS8EncodedKeySpec(bufferPriv);
        PrivateKey clavePrivada = keyFactoryRSA.generatePrivate(clavePrivadaSpec);

        return clavePrivada;
    }

}
