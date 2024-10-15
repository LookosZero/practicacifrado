package practicacifrado;

import java.io.*;

import java.security.*;
import java.security.spec.*;
import java.util.stream.Stream;

import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class DesempaquetarFactura {
    public static void main(String[] args) {

        Security.addProvider(new BouncyCastleProvider());





        
    }

    public static void mensajeAyuda() {
		System.out.println("Desempaqueta la factura usando ");
		System.out.println("\tSintaxis: java DesempaquetarFactura <nombre paquete> <fichero JSON factura> <ficheros con las claves necesarias>");
		System.out.println();
	}

}
