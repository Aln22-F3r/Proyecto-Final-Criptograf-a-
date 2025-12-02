# Mini Blockchain – Proyecto Final

Este proyecto implementa una simulación de un blockchain básica utilizando Java y SQLite, su objetivo es mostrar cómo funciona el encadenamiento de bloques utilizando el algoritmo SHA-256, almacenando todo de manera persistente en una base de datos.

## Descripción del Proyecto

El sistema permite crear y gestionar una pequeña cadena de bloques sin minería ni red distribuida, en cada bloque contiene:

- Un identificador incremental
- Fecha y hora de creación
- Datos proporcionados por el usuario
- Hash del bloque anterior
- Hash propio calculado con SHA-256

### Funcionalidades Principales

La aplicación funciona desde la consola y permite:

- **Agregar nuevos bloques** a la cadena
- **Mostrar toda la cadena** almacenada
- **Verificar la integridad** detectando si algún bloque ha sido alterado
- **Mantener la cadena de forma persistente** mediante SQLite

Este proyecto sirve como demostración del funcionamiento interno de un blockchain, enfocándose en el encadenamiento mediante hasheos.

## Requisitos

- **Java:** 17 o superior
- **Maven:** 3.9.8
- **SQLite:** integrado (no requiere instalación adicional)
- **NetBeans:** 12.6

## Instalación

### Clonar el repositorio
```bash
git clone https://github.com/Aln22-F3r/Proyecto-Final-Criptograf-a-.git
```

### Entrar a la carpeta del proyecto
```bash
cd Proyecto-Final-Criptograf-a-
```

### Compilar y generar el archivo JAR
```bash
mvn clean install
```

Esto generará el archivo ejecutable en:
```
target/Proyecto_Final-1.0-SNAPSHOT.jar
```

## Ejecución

Para ejecutar la aplicación:
```bash
java -jar target/Proyecto_Final-1.0-SNAPSHOT.jar
```

**Nota:** Si la base de datos no existe, el sistema la creará automáticamente utilizando el script:
```
src/main/resources/crearBD.sql
```

Al iniciar, se mostrará el menú de opciones en consola.

## Documentación – Javadoc

El proyecto incluye comentarios en todas las clases.

### Generar la documentación

Para generar la documentación Javadoc:
```bash
mvn javadoc:javadoc
```

Esto creará la documentación en:
```
target/site/apidocs/index.html
```

Puedes abrir este archivo en tu navegador para consultar la documentación completa del proyecto.

## Tecnologías Utilizadas

- **Java:** Lenguaje de programación principal
- **SQLite:** Base de datos para persistencia
- **Maven:** Gestión de dependencias y construcción del proyecto
- **SHA-256:** Algoritmo de hash criptográfico