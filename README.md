Para ejecutar los .java se utiliza el comando:

java -cp "lib/bcprov-jdk18on-1.78.1.jar" src\main\java\practicacifrado\GenerarClaves.java a

Para ejecutar EmpaquetarEmpresa:
java -cp "lib/bcprov-jdk18on-1.78.1.jar;target" practicacifrado.EmpaquetarFactura input\factura2203.json output\PaqueteEmpresa input\Hacienda.publica input\Empresa.privada

En la carpeta input estaran todos los ficheros de claves y en la carpeta output se generará el paquete.

(a es un atributo, en el archivo de ejemplo GenerarClaves crearia dos archivos a.publica y a.privada)
