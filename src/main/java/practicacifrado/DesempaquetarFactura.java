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

import org.bouncycastle.jcajce.provider.digest.GOST3411;
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
        byte[] firmaPaquete = paquete.getContenidoBloque("firma");
        if(firmaPaquete == null){
            System.out.println("El paquete no contiene la firma.");
            return;
        }

        // Leer las clave privada de Hacienda del fichero
        PrivateKey privateKeyHacienda = Utils.leerClavePrivada(args[2]);
        if (privateKeyHacienda == null) {
            System.out.println("El fichero no contiene la clave privada de hacienda.");
            return;
        }

        // Leer la clave publica de la Empresa del fichero
        PublicKey publicKeyEmpresa = Utils.leerClavePublica(args[3]);
        if(publicKeyEmpresa == null){
            System.out.println("El fichero no contiene la clave publica de la empresa.");
            return;
        }

        // Desencriptar la clave DES usando la clave privada de Hacienda
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKeyHacienda);
        byte[] claveDES = rsaCipher.doFinal(claveDESCifrada);

        // Desencriptar la factura usando la clave DES
        SecretKey secretKey = new SecretKeySpec(claveDES, "DES");
        Cipher DESCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        DESCipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] facturaDescifrada = DESCipher.doFinal(facturaCifrada);

        //Crear la firma del resumen utilizando la clave publica de la empresa
        Signature firmaRecibida = Signature.getInstance("SHA256withRSA");
        firmaRecibida.initVerify(publicKeyEmpresa);
        firmaRecibida.update(facturaCifrada);
        firmaRecibida.update(claveDESCifrada);

        //Verificar la firma recibida con la firma del paquete
        boolean verificada = firmaRecibida.verify(firmaPaquete);
        if(verificada){
            System.out.println("La firma es válida.");
        }else{
            System.out.println("La firma no es válida, el paquete podría haber sido alterado.");
            return;
        }
        
        // Verificar autoridad de sellado






        // Escribir la factura a fichero JSON
        String rutaFactura = args[1]; // Ruta del archivo JSON a escribir
        Files.write(Paths.get(rutaFactura), facturaDescifrada);
        System.out.println("Factura desempaquetada y guardada en: " + rutaFactura);
        
    }

    public static void mensajeAyuda() {
		System.out.println("Desempaqueta la factura usando ");
		System.out.println("\tSintaxis: java DesempaquetarFactura <nombre paquete> <fichero JSON factura> <path clave privada hacienda> <path clave publica empresa>");
		System.out.println();
	}

}
