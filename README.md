tweetsToRdf
===========
Conversión de tweets a rdf

Primeramente se utilizó las librerías de Jackson para mapear los Json de cada tweet a los típicos 
POJO's(Plain Old Java Objet).Una vez tranformados los json a objetos java se instaló virtuoso y con el algoritmo 
puesto en la clase Converter.java que está en el paquete: ontology.pojos se transforman a tripletas en notación n3.


LIBRERÍAS NECESARIAS PARA JACKSON

jackson-annotations-2.2.2.jar

jackson-core-2.2.2.jar

jackson-databind-2.2.2.jar

jackson-jaxrs-1.9.11.jar

jettison-1.3.jar

LIBRERÍAS VIRTUOSO-JENA

arq.jar

axis.jar

commons-logging.jar

icu4j_3_4.jar

iri.jar

jena.jar

xercesImpl.jar

virt_jena.jar

virtjdbc3.jar (driver java para virtuoso)

LIBRERÍAS ADICIONALES.

mysql-connector-java-5.1.25-bin.jar (En la página de mysql pueden obtener la última versión)

Se puede descargar las librerías <a href="carbono.utpl.edu.ec:8008/lib/librerias.zip">aqui</a>.



