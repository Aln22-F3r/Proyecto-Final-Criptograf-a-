package configuration;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Clase de configuración para gestionar la conexión a la base de datos SQLite.
 *
 * <p>
 * <b>Responsabilidades:</b></p>
 * <ul>
 * <li>Crear la estructura de directorios necesaria.</li>
 * <li>Establecer conexiones con la base de datos.</li>
 * <li>Inicializar el esquema de tablas mediante scripts SQL.</li>
 * </ul>
 *
 * <p>
 * La base de datos se almacena en: <code>data/blockchain.db</code></p>
 *
 * @author Alan Ramos
 * @version 1.0
 */
public class ConexionBD {

    /**
     * Directorio donde se almacenará la base de datos
     */
    private static final String DB_DIR = "data";

    /**
     * Nombre del archivo de base de datos SQLite
     */
    private static final String DB_FILE = "blockchain.db";

    /**
     * Construye la ruta completa de la base de datos.
     *
     * @return String con la ruta relativa al archivo .db
     */
    private static String getDatabasePath() {
        return DB_DIR + "/" + DB_FILE;
    }

    /**
     * <p>
     * Obtiene una conexión activa a la base de datos SQLite,
     * esta conexión debe cerrarse después de usarse para evitar fugas de
     * recursos.<p>
     *
     * @return Connection objeto de conexión JDBC
     * @throws SQLException si no se puede establecer la conexión
     */
    public static Connection obtenerConexion() throws SQLException {
        String url = "jdbc:sqlite:" + getDatabasePath();
        return DriverManager.getConnection(url);
    }

    /**
     * Inicializa la base de datos creando la estructura necesaria.
     *
     * <p>
     * <b>Proceso de inicialización:</b></p>
     * <ol>
     * <li>Verifica si existe el directorio <code>data</code>; si no, lo
     * crea.</li>
     * <li>Lee el archivo <code>crear_tabla.sql</code> desde la carpeta
     * <i>resources</i>.</li>
     * <li>Ejecuta todas las sentencias SQL encontradas.</li>
     * <li>Crea la tabla <code>blocks</code> si no existe.</li>
     * </ol>
     *
     * <p>
     * Este método es idempotente: puede ejecutarse múltiples veces sin causar
     * errores.</p>
     *
     * @throws Exception si ocurre un error al crear directorios o ejecutar SQL
     */
    public static void inicializarBD() throws Exception {
        // Crea la carpeta 'data' si no existe //
        Path dir = Paths.get(DB_DIR);
        if (!java.nio.file.Files.exists(dir)) {
            java.nio.file.Files.createDirectories(dir);
        }

        // Ejecuta el script SQL para crear la estructura de la tabla //
        try (Connection conn = obtenerConexion();
                Statement stmt = conn.createStatement()) {

            // Lee los archivo SQL desde la carpeta resources //
            InputStream is = ConexionBD.class
                    .getClassLoader()
                    .getResourceAsStream("crear_tabla.sql");

            if (is == null) {
                throw new RuntimeException("No se encontró el archivo crear_tabla.sql en resources");
            }

            // Parsea y ejecuta cada sentencia SQL //
            Scanner scanner = new Scanner(is).useDelimiter(";");
            while (scanner.hasNext()) {
                String sentencia = scanner.next().trim();
                if (!sentencia.isEmpty()) {
                    stmt.execute(sentencia);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al inicializar la base de datos: " + e.getMessage(), e);
        }
    }
}