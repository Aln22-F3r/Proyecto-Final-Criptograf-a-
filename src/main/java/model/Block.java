package model;

/**
 * Representa un bloque individual en el blockchain.
 *
 * <p>
 * Un bloque es la unidad básica de información en un blockchain y
 * contiene:</p>
 * <ul>
 * <li>ID único incremental</li>
 * <li>Timestamp de creación</li>
 * <li>Datos almacenados (payload)</li>
 * <li>Hash del bloque anterior (para encadenamiento)</li>
 * <li>Hash del bloque actual (para verificación de integridad)</li>
 * </ul>
 *
 * <p>
 * Esta clase es inmutable: todos los campos son <code>final</code> y no hay
 * setters por lo que esto garantiza que un bloque no pueda modificarse después de su
 * creación.</p>
 *
 * @author Alan Ramos
 * @version 1.0
 */
public class Block {

    /**
     * Identificador único del bloque (autoincremental)
     */
    private final int id;

    /**
     * Fecha y hora de creación del bloque en formato 'yyyy-MM-dd HH:mm:ss'
     */
    private final String timestamp;

    /**
     * Información almacenada en el bloque
     */
    private final String datos;

    /**
     * Hash SHA-256 del bloque anterior o GENESIS si es el primer bloque
     */
    private final String hashAnterior;

    /**
     * Hash SHA-256 calculado para este bloque basado en todos sus datos
     */
    private final String hashActual;

    /**
     * Constructor que inicializa un bloque con todos sus datos.
     *
     * @param id Identificador único del bloque
     * @param timestamp Marca de tiempo de creación
     * @param datos Información a almacenar en el bloque
     * @param hashAnterior Hash del bloque previo en la cadena
     * @param hashActual Hash calculado para este bloque
     */
    public Block(int id, String timestamp, String datos, String hashAnterior, String hashActual) {
        this.id = id;
        this.timestamp = timestamp;
        this.datos = datos;
        this.hashAnterior = hashAnterior;
        this.hashActual = hashActual;
    }

    /**
     * Obtiene el ID del bloque.
     *
     * @return ID numérico del bloque
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene la marca de tiempo del bloque.
     *
     * @return String con la fecha de creación
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Obtiene los datos almacenados en el bloque.
     *
     * @return String con el contenido del bloque
     */
    public String getDatos() {
        return datos;
    }

    /**
     * Obtiene el hash del bloque anterior en la cadena.
     *
     * @return Hash SHA-256 del bloque previo o GENESIS si es el primero
     */
    public String getHashAnterior() {
        return hashAnterior;
    }

    /**
     * Obtiene el hash calculado para este bloque.
     *
     * @return Hash SHA-256 de este bloque
     */
    public String getHashActual() {
        return hashActual;
    }
}