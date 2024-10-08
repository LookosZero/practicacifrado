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

public class EmpaquetarFactura {
    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
			mensajeAyuda();
			System.exit(1);
		}

        Security.addProvider(new BouncyCastleProvider());

        byte[] facturaSinCifrar = leerFactura(args[1]);

        PublicKey clavePublica = leerClavePublica(args[2]);
        PrivateKey clavePrivada = leerClavePrivada(args[3]);

        


        
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
