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
    



    public static void main(String[] args) {
        if(args.length!=3){
            System.out.println("java -cp [...] SellarFactura <nombre paquete> <ficheros con las claves necesarias>");
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
            e.printStackTrace();
        }
        
        //se obtiene el contenido del paquete (factura) y si esta vacio se sale del programa
        byte[] contenidoPaquete =paquete.getContenidoBloque("facturaCifrada");
        if(contenidoPaquete==null){
            System.out.println("El paquete no contiene la factura.");
            return;}


        //obtenemos la firma del paquete y comprobamos que corresponda con la empresa
        byte[] firmaPaquete = paquete.getContenidoBloque("firma");        
        if(!verificarFirma(firmaPaquete, contenidoPaquete, clavePublicaEmpresa)){
            System.out.println("La empresa que presenta la factura no es la misma que la que creó el paquete.");
            return;}


        //obtenemos timestamp
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //creamos el sello
        String sello = "Fecha: " + timestamp;
        //firmamos sello
        Signature signature;
        byte[]firma=null;
        try{
            signature= Signature.getInstance("SHA256withRSA");
            signature.initSign(clavePrivadaSellado);
            signature.update(sello.getBytes("UTF-8"));
            firma = signature.sign();
        }catch(Exception e){e.printStackTrace();}

        //añadimos el sello al paquete
        paquete.anadirBloque("sello", firma);
        paquete.anadirBloque("timestamp", timestamp.getBytes(StandardCharsets.UTF_8));

        //guardar paquete
        paquete.escribirPaquete(args[0]);
        System.out.println("Paquete sellado.");



    }
    
    private static boolean verificarFirma(byte[] firma, byte[] contenidoPaquete, PublicKey clavePublica){
        Signature signature;
        try {
            signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(clavePublica);
            signature.update(contenidoPaquete);
            return signature.verify(firma);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
}
