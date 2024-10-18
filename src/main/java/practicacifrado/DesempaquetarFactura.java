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
    public static void main(String[] args) throws Exception {
        
        if(args.length != 4){
            mensajeAyuda();
			return;
        }

        Security.addProvider(new BouncyCastleProvider());

        
        Paquete paquete = new Paquete(args[0]);
        
        // Leer la factura cifrada del paquete
        byte[] facturaCifrada = paquete.getContenidoBloque("facturaCifrada");
        if(facturaCifrada == null){
            System.out.println("El paquete no contiene la factura.");
            return;
        }

        // Leer la clave DES cifrada del paquete
        byte[] claveDESCifrada = paquete.getContenidoBloque("claveCifrada");
        if(claveDESCifrada == null){
            System.out.println("El paquete no contiene la clave cifrada.");
            return;
        }

        // Leer la firma del paquete
        byte[] firma = paquete.getContenidoBloque("firma");
        if(firma == null){
            System.out.println("El paquete no contiene la firma.");
            return;
        }


        // Leer las claves de los ficheros
        PrivateKey privateKeyHacienda = Utils.leerClavePrivada(args[2]);
        if (privateKeyHacienda == null) {
            System.out.println("El fichero no contiene la clave privada de hacienda.");
            return;
        }

        PublicKey publicKeyEmpresa = Utils.leerClavePublica(args[3]);
        if(publicKeyEmpresa == null){
            System.out.println("El fichero no contiene la clave publica de la empresa.");
            return;
        }


        
    }

    public static void mensajeAyuda() {
		System.out.println("Desempaqueta la factura usando ");
		System.out.println("\tSintaxis: java DesempaquetarFactura <nombre paquete> <fichero JSON factura> <path clave privada hacienda> <path clave publica hacienda>");
		System.out.println();
	}

}
