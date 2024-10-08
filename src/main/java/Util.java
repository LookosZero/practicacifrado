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

public class Util {
    public PublicKey leerClavePublica(String path) throws Exception {
        /*** 4 Recuperar clave PUBLICA del fichero */
		// 4.1 Leer datos binarios /en formato X.509)
		byte[] bufferPub = Files.readAllBytes(Paths.get(path + ".publica"));

		// 4.2 Recuperar clave publica desde datos codificados en formato X509
		X509EncodedKeySpec clavePublicaSpec = new X509EncodedKeySpec(bufferPub);
		PublicKey clavePublica2 = keyFactoryRSA.generatePublic(clavePublicaSpec);

        return clavePublica2;
    }

    


}
