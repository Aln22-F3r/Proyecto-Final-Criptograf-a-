package service;

import configuration.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.Block;
import util.Hash;

/**
 * Servicio principal responsable de gestionar la lógica de el blockchain.
 *
 * <p>
 * Sus responsabilidades incluyen:</p>
 * <ul>
 * <li>Agregar nuevos bloques a la cadena</li>
 * <li>Consultar bloques de la base de datos</li>
 * <li>Validar la integridad criptográfica de toda la cadena</li>
 * </ul>
 *
 * <p>
 * La estructura sigue el modelo tradicional de blockchain: cada bloque contiene
 * un hash calculado a partir de sus datos y el hash del bloque anterior,
 * formando una cadena inmutable.</p>
 *
 * @author Alan Ramos
 * @version 1.0
 */
public class Blockchain {

    /**
     * Recupera todos los bloques almacenados en la base de datos.
     *
     * <p>
     * Los bloques se devuelven ordenados por ID ascendente, es decir, desde el
     * bloque génesis hasta el más reciente.</p>
     *
     * @return Lista completa de bloques en la cadena
     * @throws Exception Si ocurre un error al ejecutar la consulta SQL
     */
    public List<Block> obtenerBlock() throws Exception {
        List<Block> bloques = new ArrayList<>();
        String sql = "SELECT * FROM blocks ORDER BY id ASC";

        // Usamos try-with-resources para cerrar automáticamente los recursos JDBC //
        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            // Itera sobre los resultados y crear objetos Block //
            while (rs.next()) {
                bloques.add(new Block(
                        rs.getInt("id"),
                        rs.getString("timestamp"),
                        rs.getString("datos"),
                        rs.getString("hash_anterior"),
                        rs.getString("hash_actual")
                ));
            }
        }

        return bloques;
    }

    /**
     * Obtiene el último bloque almacenado en la base de datos.
     *
     * <p>
     * Es utilizado principalmente para:</p>
     * <ul>
     * <li>Obtener el hash del bloque anterior al crear uno nuevo</li>
     * <li>Determinar el siguiente ID autoincremental</li>
     * </ul>
     *
     * @return Último bloque de la cadena o {@code null} si está vacía
     * @throws Exception Si ocurre un error al consultar la base de datos
     */
    private Block obtenerUltimoBlock() throws Exception {
        String sql = "SELECT * FROM blocks ORDER BY id DESC LIMIT 1";

        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return new Block(
                        rs.getInt("id"),
                        rs.getString("timestamp"),
                        rs.getString("datos"),
                        rs.getString("hash_anterior"),
                        rs.getString("hash_actual")
                );
            }
        }
        // No hay bloques en la cadena //
        return null;
    }

    /**
     * Agrega un nuevo bloque válido a la blockchain.
     *
     * <p>
     * Proceso de creación:</p>
     * <ol>
     * <li>Consultar el último bloque almacenado</li>
     * <li>Asignar ID autoincremental</li>
     * <li>Generar timestamp actual</li>
     * <li>Asignar hash del bloque anterior o "GENESIS"</li>
     * <li>Calcular el hash del nuevo bloque usando SHA-256</li>
     * <li>Insertar el nuevo bloque en la base de datos</li>
     * </ol>
     *
     * <p>
     * El hash del nuevo bloque se calcula concatenando:
     * {@code id + timestamp + datos + hashAnterior}.</p>
     *
     * @param datos Información (payload) que el bloque almacenará
     * @throws Exception Si ocurre un error al obtener el último bloque o
     * insertar en BD
     */
    public void agregarBlock(String datos) throws Exception {
        // Obtener el último bloque de la cadena //
        Block ultimo = obtenerUltimoBlock();

        // Calcular el ID del nuevo bloque //
        int nuevoId = (ultimo == null) ? 1 : ultimo.getId() + 1;

        // Generar timestamp actual //
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Establece el hash anterior, si es el primer bloque se hace GENESIS //
        String hashAnterior = (ultimo == null) ? "GENESIS" : ultimo.getHashActual();

        // Calcula el hash del nuevo bloque //
        String hashActual = Hash.sha256(nuevoId + timestamp + datos + hashAnterior);

        // Prepara la consulta SQL para insertar el bloque //
        String sql = "INSERT INTO blocks (id, timestamp, datos, hash_anterior, hash_actual) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Establece los parámetros de la consulta preparada //
            stmt.setInt(1, nuevoId);
            stmt.setString(2, timestamp);
            stmt.setString(3, datos);
            stmt.setString(4, hashAnterior);
            stmt.setString(5, hashActual);

            // Ejecuta la inserción //
            stmt.executeUpdate();
        }
    }

    /**
     * Verifica la integridad total de la blockchain.
     *
     * <p>
     * Realiza dos validaciones fundamentales:</p>
     *
     * <h3>1. Validación de Hash</h3>
     * <p>
     * Para cada bloque (excepto el génesis) recalcula su hash y lo compara con
     * el almacenado. Si difieren, significa que alguien modificó su
     * contenido.</p>
     *
     * <h3>2. Validación de Encadenamiento</h3>
     * <p>
     * Comprueba que el {@code hash_anterior} de cada bloque coincida
     * exactamente con el {@code hash_actual} del bloque previo.</p>
     *
     * <p>
     * Si se detecta alguna alteración, el método imprime el ID del bloque
     * corrupto y retorna {@code false}. Si toda la cadena es correcta, retorna
     * {@code true}.</p>
     *
     * @return {@code true} si la cadena es totalmente válida; {@code false} si
     * se detecta corrupción
     * @throws Exception Si ocurre un error al obtener los bloques
     */
    public boolean verificarCadena() throws Exception {
        // Obtener todos los bloques de la cadena //
        List<Block> bloques = obtenerBlock();

        // Iterar desde el segundo bloque porque el primero es GENESIS //
        for (int i = 1; i < bloques.size(); i++) {
            Block actual = bloques.get(i);
            Block anterior = bloques.get(i - 1);

            // VERIFICACIÓN 1: Recalcular el hash del bloque actual //
            // Si los datos fueron modificados, este hash no coincidirá con el almacenado //
            String hashRecalculado = Hash.sha256(
                    actual.getId()
                    + actual.getTimestamp()
                    + actual.getDatos()
                    + actual.getHashAnterior()
            );

            // Comparar el hash recalculado con el almacenado //
            if (!actual.getHashActual().equals(hashRecalculado)) {
                System.out.println("Hash corrupto en bloque ID: " + actual.getId());
                System.out.println("   Hash esperado: " + actual.getHashActual());
                System.out.println("   Hash calculado: " + hashRecalculado);
                return false;
            }

            // VERIFICACIÓN 2: Validar el encadenamiento //
            // El hash_anterior del bloque actual debe coincidir con el hash_actual del anterior //
            if (!actual.getHashAnterior().equals(anterior.getHashActual())) {
                System.out.println("Ruptura de encadenamiento en bloque ID: " + actual.getId());
                System.out.println("   Se esperaba: " + anterior.getHashActual());
                System.out.println("   Se encontró: " + actual.getHashAnterior());
                return false;
            }
        }

        // Si pasó todas las validaciones, la cadena es íntegra //
        System.out.println("Cadena íntegra - Todos los bloques verificados correctamente");
        return true;
    }
}

