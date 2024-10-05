import java.io.*;

import java.security.*;
import java.security.spec.*;
import java.util.stream.Stream;

import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EmpaquetarFactura {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
			mensajeAyuda();
			System.exit(1);
		}

        


        
    }

    public static byte[] leerFactura(String path){
        File file = new File(path);
        byte[] factura = new byte[(int) file.length()];

        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(factura);
            
        } catch (Exception e) {
            System.err.println("No se pudo leer JSON de la factura: " + e.getMessage());
        }
        
        return factura;
    }

    public static void mensajeAyuda() {
		System.out.println("Generador de pares de clave RSA de 512 bits");
		System.out.println("\tSintaxis:   java EmpaquetarFactura <JSON factura> <nombre paquete> <path claves>");
		System.out.println();
	}

}
