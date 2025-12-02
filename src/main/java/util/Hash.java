package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Utilidad para generar hashes criptográficos utilizando el algoritmo SHA-256.
 *
 * <p>
 * SHA-256 es una función hash criptográfica
 * ampliamente utilizada debido a sus propiedades:</p>
 *
 * <ul>
 * <li>Produce un hash fijo de 256 bits (64 caracteres hexadecimales)</li>
 * <li>Es determinista: el mismo texto siempre genera el mismo hash</li>
 * <li>Es irreversible: no es posible reconstruir el mensaje original desde el
 * hash</li>
 * <li>Es sensible a cambios: una mínima variación en el input produce un hash
 * totalmente diferente</li>
 * </ul>
 *
 * <p>
 * Por estas características, SHA-256 es ideal para validar integridad y se
 * utiliza comúnmente en tecnologías como blockchain.</p>
 *
 * @author Alan Ramos
 * @version 1.0
 */
public class Hash {

    /**
     * Genera el hash SHA-256 a partir de una cadena de texto.
     *
     * <p>
     * Proceso interno:</p>
     * <ol>
     * <li>Convierte el texto a bytes utilizando UTF-8</li>
     * <li>Aplica la función hash SHA-256 mediante {@link MessageDigest}</li>
     * <li>Convierte el resultado binario en una representación hexadecimal</li>
     * </ol>
     *
     * <p>
     * <strong>Ejemplo:</strong></p>
     * <pre>
     * Input:  "Hola Mundo"
     * Output: "2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae"
     * </pre>
     *
     * @param input Texto del cual se quiere obtener el hash
     * @return Hash SHA-256 expresado como cadena hexadecimal de 64 caracteres
     * @throws RuntimeException Si el algoritmo SHA-256 no está disponible
     */
    public static String sha256(String input) {
        try {
            // Solicita el algoritmo SHA-256 //
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Genera el hash como arreglo de bytes //
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convierte los bytes en cadena hexadecimal //
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();

        } catch (Exception e) {
            // SHA-256 siempre está disponible, pero se manejamos la excepción por seguridad //
            throw new RuntimeException("Error calculando SHA-256: " + e.getMessage());
        }
    }
}