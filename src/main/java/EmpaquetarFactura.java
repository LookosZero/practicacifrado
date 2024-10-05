import java.io.*;

import java.security.*;
import java.security.spec.*;

import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EmpaquetarFactura {
    public static void main(String[] args) {
        if (args.length != 3) {
			mensajeAyuda();
			System.exit(1);
		}


        
    }

    public static void mensajeAyuda() {
		System.out.println("Generador de pares de clave RSA de 512 bits");
		System.out.println("\tSintaxis:   java EmpaquetarFactura <JSON factura> <nombre paquete> <path claves>");
		System.out.println();
	}

}
