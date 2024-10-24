package practicacifrado;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
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
            return;
        }

        Security.addProvider(new BouncyCastleProvider());

        // Leer la factura y las claves de los ficheros
        byte[] facturaSinCifrar = leerFactura(args[0]);
        if (facturaSinCifrar == null) {
            System.out.println("El fichero no contiene la factura.");
            return;
        }
        PublicKey publicKeyHacienda = Utils.leerClavePublica(args[2]);
        if (publicKeyHacienda == null) {
            System.out.println("El fichero no contiene la clave publica de hacienda.");
            return;
        }

        PrivateKey privateKeyEmpresa = Utils.leerClavePrivada(args[3]);
        if (privateKeyEmpresa == null) {
            System.out.println("El fichero no contiene la clave privada de la empresa.");
            return;
        }

        //Crear el paquete donde se guardara todo cifrado.
        Paquete paqueteEmpresa = new Paquete();

        // Inicializamos la clave en DES aleatoria con KeyGenerator que utilizara para cifrar la factura
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56);
        SecretKey secretKey = keyGen.generateKey();

        // Se utiliza la clase cipher para cifrar la factura con la secretKey que generamos en DES
        Cipher DESCipher = Cipher.getInstance("DES");
        DESCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Se cifra la factura utilizando doFinal()
        byte[] facturaCifrada = DESCipher.doFinal(facturaSinCifrar);

        //Se añade el bloque de la factura cifrada al paquete
        paqueteEmpresa.anadirBloque("facturaCifrada", facturaCifrada);

        // Cifrar la clave publica de hacienda con RSA
        /*
         * La clave DES que se utilizo para cifrar la factura se cifra con la clave publica de hacienda,
         * esto es para que solo Hacienda pueda descifrar esta clave DES con su clave privada y acceder
         * a los datos de la factura.
         */
        Cipher RSACipher = Cipher.getInstance("RSA");
        RSACipher.init(Cipher.ENCRYPT_MODE, publicKeyHacienda);

        byte[] claveDES = secretKey.getEncoded();
        byte[] claveDESCifrada = RSACipher.doFinal(claveDES);

        //Añadimos la clave cifrada al paquete
        paqueteEmpresa.anadirBloque("claveCifrada", claveDESCifrada);

        //Crear la firma del resumen utilizando la clave privada de la empresa
        Signature firma = Signature.getInstance("SHA256withRSA");
        firma.initSign(privateKeyEmpresa);
        firma.update(facturaCifrada);
        firma.update(claveDESCifrada);
        byte[] firmaGenerada = firma.sign();

        //Añadimos la firma al paquete
        paqueteEmpresa.anadirBloque("firma", firmaGenerada);

        //Escribimos el paquete a un fichero
        paqueteEmpresa.escribirPaquete(args[1]);

        System.out.println("Generado el paquete de la empresa.");

    }

    private static byte[] leerFactura(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    public static void mensajeAyuda() {
        System.out.println("Empaqueta la factura en un paquete cifrado que contiene la factura cifrada, la clave cifrada y la firma.");
        System.out.println("\tSintaxis: java EmpaquetarFactura <JSON factura> <nombre paquete> <path ClavePublicaHacienda> <path ClavePrivadaEmpresa>");
        System.out.println();
    }

}
