package practicacifrado;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.stream.Stream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EmpaquetarFactura {
    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
			mensajeAyuda();
			System.exit(1);
		}

        Security.addProvider(new BouncyCastleProvider());

        // Leer la factura y las claves de los ficheros
        byte[] facturaSinCifrar = leerFactura(args[1]);

        PublicKey publicKey = Utils.leerClavePublica(args[2]);
        PrivateKey privateKey = Utils.leerClavePrivada(args[3]);

        //Crear el paquete donde se guardara todo cifrado.
        Paquete paqueteEmpresa = new Paquete("paqueteEmpresa");

        // Inicializamos la clave en DES aleatoria con KeyGenerator que utilizara para cifrar la factura
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56);
        SecretKey secretKey = keyGen.generateKey();

        // Se utiliza la clase cipher para cifrar la factura con la secretKey que generamos en DES
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Se cifra la factura utilizando update() y doFinal()
        byte[] facturaCifrada = desCipher.update(facturaSinCifrar);
        facturaCifrada = desCipher.doFinal();

        //Se añade el bloque de la factura cifrada al paquete
        paqueteEmpresa.anadirBloque("facturaCifrada", facturaCifrada);
        
        // Cifrar la clave publica de hacienda con RSA
        /*
         * La clave DES que se utilizo para cifrar la factura se cifra con la clave publica de hacienda,
         * esto es para que solo Hacienda pueda descifrar esta clave DES con su clave privada y acceder
         * a los datos de la factura.
         */
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS5Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] clavePublicaCifrada = rsaCipher.update(publicKey.getEncoded());
        clavePublicaCifrada = rsaCipher.doFinal(clavePublicaCifrada);

        //

        


















        


        
    }

    private static byte[] leerFactura(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    public static void mensajeAyuda() {
		System.out.println("Empaqueta la factura en un paquete cifrado que contiene la factura cifrada, la clave cifrada y la firma.");
		System.out.println("\tSintaxis:   java EmpaquetarFactura <JSON factura> <nombre paquete> <path clave publica> <path clave privada>");
		System.out.println();
	}

}
