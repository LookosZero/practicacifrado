En la carpeta input estaran todos los ficheros de claves y en la carpeta output se generará el paquete.

Para ejecutar GenerarClaves:

java -cp "lib/bcprov-jdk18on-1.78.1.jar" src\main\java\practicacifrado\GenerarClaves.java input\Empresa

Para ejecutar EmpaquetarEmpresa:

java -cp "lib/bcprov-jdk18on-1.78.1.jar;target" practicacifrado.EmpaquetarFactura input\factura2203.json output\PaqueteEmpresa input\Hacienda.publica input\Empresa.privada

Para ejecutar SellarFactura:

java -cp "lib/bcprov-jdk18on-1.78.1.jar;target" practicacifrado.SellarFactura output/PaqueteEmpresa input/Sellado.privada input/Empresa.publica

Para ejecutar DesempaquetarFactura:

java -cp "lib/bcprov-jdk18on-1.78.1.jar;target" practicacifrado.DesempaquetarFactura output/PaqueteEmpresa output/factura.json input/Hacienda.privada input/Empresa.publica

(a es un atributo, en el archivo de ejemplo GenerarClaves crearia dos archivos a.publica y a.privada)
