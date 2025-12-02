package app;

import configuration.ConexionBD;
import service.Blockchain;
import java.util.Scanner;

/**
 * Clase principal que ejecuta la aplicación Mini Blockchain.
 *
 * <p>
 * Esta clase proporciona una interfaz de consola para interactuar con el
 * sistema de blockchain, permitiendo agregar bloques, visualizar la cadena
 * completa y verificar la integridad de los datos.<p>
 *
 * @author Alan Ramos
 * @version 1.0
 * @since 2025
 */
public class Main {

    /**
     * Método principal que inicia la aplicación.
     *
     * <p>
     * <b>Flujo de ejecución:</b></p>
     * <ol>
     * <li>Inicializa la base de datos SQLite.</li>
     * <li>Crea instancia del servicio Blockchain.</li>
     * <li>Muestra menú interactivo con 4 opciones.</li>
     * <li>Procesa la selección del usuario hasta que elija salir.</li>
     * </ol>
     *
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            // Inicializa la base de datos y si no existe, la crea //
            ConexionBD.inicializarBD();

            // Creamos el scanner para leer entrada del usuario //
            Scanner sc = new Scanner(System.in);

            // Instanciamos el servicio que gestiona la blockchain //
            Blockchain service = new Blockchain();

            System.out.println("\n--- MINI BLOCKCHAIN ---");

            // Bucle principal del menú //
            while (true) {
                // Mostrar opciones disponibles //
                System.out.println("\n1. Agregar bloque");
                System.out.println("2. Mostrar cadena");
                System.out.println("3. Verificar integridad");
                System.out.println("4. Salir");
                System.out.print("Opción: ");

                int op = sc.nextInt();
                sc.nextLine();

                switch (op) {
                    case 1:
                        // Caso 1: Agregar un nuevo bloque a la cadena //
                        System.out.print("Datos del bloque: ");
                        String datos = sc.nextLine();
                        service.agregarBlock(datos);
                        System.out.println("Bloque agregado correctamente");
                        break;

                    case 2:
                        // Caso 2: Mostrar todos los bloques en orden cronológico //
                        service.obtenerBlock().forEach(b -> {
                            System.out.println("\n" + "=".repeat(50));
                            System.out.println("ID: " + b.getId());
                            System.out.println("Fecha: " + b.getTimestamp());
                            System.out.println("Datos: " + b.getDatos());
                            System.out.println("Hash Anterior: " + b.getHashAnterior());
                            System.out.println("Hash Actual: " + b.getHashActual());
                        });
                        break;

                    case 3:
                        // Caso 3: Verificar que la cadena no ha sido alterada //
                        service.verificarCadena();
                        break;

                    case 4:
                        // Caso 4: Salir de la aplicación //
                        System.out.println("Saliendo...");
                        return;

                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                }
            }
        } catch (Exception e) {
            // Capturar cualquier error y lo muestra //
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}