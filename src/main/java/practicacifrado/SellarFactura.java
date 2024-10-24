package practicacifrado;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.nio.charset.StandardCharsets;
import java.io.*;

import java.security.*;
import java.security.spec.*;
import java.util.stream.Stream;

import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SellarFactura {

    public static void main(String[] args) throws Exception{
        if(args.length!=3){
            System.out.println("java -cp [...] SellarFactura <nombre paquete> <path clavePrivadaSellado> <path clavePublicaEmpresa>");
            System.exit(1);
        }
        //BouncyCastle
        Security.addProvider(new BouncyCastleProvider());
        Paquete paquete = new Paquete(args[0]);
        PublicKey clavePublicaEmpresa=null;
        PrivateKey clavePrivadaSellado=null;
        try {
            clavePublicaEmpresa = Utils.leerClavePublica(args[2]);
            clavePrivadaSellado = Utils.leerClavePrivada(args[1]);
        } catch (Exception e) {
            System.err.println("Error al leer claves.");
            e.printStackTrace();
            System.exit(1);
        }
        
        //se obtiene el contenido del paquete (factura) y si esta vacio se sale del programa
        byte[] facturaCifrada =paquete.getContenidoBloque("facturaCifrada");
        if(facturaCifrada==null){
            System.out.println("El paquete no contiene la factura.");
            return;
        }

        byte[] claveDESCifrada = paquete.getContenidoBloque("claveCifrada");
        if(claveDESCifrada==null){
            System.out.println("El paquete no contiene la clave cifrada.");
            return;
        }

        //obtenemos la firma del paquete y comprobamos que corresponda con la empresa
        byte[] firmaPaquete = paquete.getContenidoBloque("firma");        
        if(!verificarFirma(firmaPaquete, facturaCifrada, claveDESCifrada, clavePublicaEmpresa)){
            System.err.println("La empresa que presenta la factura no es la misma que la que creó el paquete.");
            return;
        }
        System.out.println("Firma verificada correctamente.");


        //obtenemos timestamp
        Date fechaActual = new Date();
        paquete.anadirBloque("fecha", fechaActual.toString().getBytes());
        //creamos el sello
        byte [] sello = generarSello(fechaActual, claveDESCifrada, facturaCifrada, firmaPaquete, clavePrivadaSellado);
        //añadimos el sello al paquete
        paquete.anadirBloque("sello", sello);

        //guardar paquete
        paquete.escribirPaquete(args[0]);
        System.out.println("Paquete sellado.");

    }
    
    private static boolean verificarFirma(byte[] firma, byte[] contenidoFactura, byte[] contenidoClaveCifrada, PublicKey clavePublica) throws Exception{
        Signature signature;
            signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(clavePublica);
            // Actualizar con el contenido de la factura cifrada
            signature.update(contenidoFactura);
            // Actualizar con la clave DES cifrada
            signature.update(contenidoClaveCifrada);
            return signature.verify(firma);
        }
    private static byte[] generarSello (Date fecha,byte[] claveDESCifrada ,byte[] facturaCifrada, byte[] firma, PrivateKey pk) throws Exception{
        Signature signature;
            signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(pk);

            signature.update(fecha.toString().getBytes());
            signature.update(facturaCifrada);
            signature.update(claveDESCifrada);
            signature.update(firma);
            return signature.sign();
    }
    
}
