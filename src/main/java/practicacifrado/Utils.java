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
    
    public PublicKey leerClavePublica(String path) throws Exception{

        Security.addProvider(new BouncyCastleProvider());

        KeyFactory keyFactoryRSA = KeyFactory.getInstance("RSA");

        byte[] bufferPub = Files.readAllBytes(Paths.get(path + ".publica"));

		X509EncodedKeySpec clavePublicaSpec = new X509EncodedKeySpec(bufferPub);
		PublicKey clavePublica = keyFactoryRSA.generatePublic(clavePublicaSpec);

        return clavePublica;
    }

    public PublicKey leerClavePrivada(String path){



    }



}
