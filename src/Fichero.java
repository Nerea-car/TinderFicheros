import java.io.*;
import java.lang.reflect.Array;
import java.util.*;


public class Fichero {
    static void main() {
        Scanner lee = new Scanner(System.in);

        System.out.println("Introduce el nombre del fichero de datos: (con TinderFicheros/)");
        String nombreFichero = lee.nextLine();

        if (nombreFichero.isEmpty()) {
            System.out.println("Error: el nombre del fichero no puede estar vacío.");
            return;
        }

        File fichero = new File(nombreFichero);

        // Comprobar que existe y se puede leer
        if (!fichero.exists() || !fichero.isFile() || !fichero.canRead()) {
            System.out.println("Error: no se puede leer el fichero de entrada.");
            return;
        }

        // Comprobar tamaño máximo
        if (fichero.length() > 10000) {
            System.out.println("Error: el fichero supera los 10000 bytes.");
            return;
        }

        mostrarMenu();
        System.out.println("Elige una opcion: ");
        int opcion = lee.nextInt();



        while (opcion != 4){


            switch (opcion) {
                //Funciona
                case 1 -> {
                    System.out.println("Escribe el código del usuario: ");
                    String codigo = lee.nextLine();

                    if (comprobarCodigo(codigo, nombreFichero)) continue;

                    System.out.println("Escribe las aficiones de este usuario separadas por espacios: ");
                    String aficiones = lee.nextLine();

                    if (aficiones.isEmpty()) {
                        System.out.println("Las aficiones no pueden estar vacias");
                        continue;
                    }

                    try (FileWriter escribiendo = new FileWriter(nombreFichero, true)) {
                        escribiendo.write(codigo + " " + aficiones.toUpperCase() + "\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Añadido");


                }
                //Funciona
                case 2 -> {
                    System.out.println("Mostrando el fichero");
                    System.out.println();


                    //Lee lo que hay escrito en el fichero
                    try (BufferedReader lectura = new BufferedReader(new FileReader(nombreFichero))) {
                        String linea;
                        while ((linea = lectura.readLine()) != null) {
                            System.out.println(linea);
                        }
                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                }
                case 3 -> {
                    System.out.println("Generando fichero con las concordancias");

                    //Lee todos los usuarios y los mete en un ArrayList
                    ArrayList<String> usuarios = new ArrayList<>();

                    try (BufferedReader br =
                                 new BufferedReader(new FileReader(fichero))) {

                        String linea;

                        while ((linea = br.readLine()) != null) {

                            if (!linea.trim().isEmpty()) {
                                usuarios.add(linea.trim());
                            }
                        }

                    } catch (IOException e) {
                        System.out.println(
                                "Error: no se puede leer el fichero."
                        );
                        continue;
                    }

                    //Busca concordancias
                    ArrayList<String> concordancias = new ArrayList<>();
                    ArrayList<Integer> numeroComunes = new ArrayList<>();

                    for (int i = 0; i < usuarios.size(); i++) {

                        String[] usuario1 = usuarios.get(i).split("\\s+");

                        String codigo1 = usuario1[0];

                        // Conjunto de aficiones del usuario 1
                        HashSet<String> aficiones1 = new HashSet<>();

                        for (int x = 1; x < usuario1.length; x++) {
                            aficiones1.add(usuario1[x]);
                        }

                        for (int j = i + 1; j < usuarios.size(); j++) {

                            String[] usuario2 = usuarios.get(j).split("\\s+");

                            String codigo2 = usuario2[0];

                            // Conjunto de aficiones del usuario 2
                            HashSet<String> aficiones2 = new HashSet<>();

                            for (int x = 1; x < usuario2.length; x++) {
                                aficiones2.add(usuario2[x]);
                            }

                            // Obtener las aficiones comunes
                            HashSet<String> comunes =
                                    new HashSet<>(aficiones1);

                            comunes.retainAll(aficiones2);

                            // Comprobar número mínimo
                            if (!comunes.isEmpty()) {

                                ArrayList<String> listaComunes =
                                        new ArrayList<>(comunes);

                                // Orden alfabético
                                Collections.sort(listaComunes);

                                String resultado =
                                        codigo1 + " " + codigo2;

                                for (String aficion : listaComunes) {
                                    resultado += " " + aficion;
                                }

                                concordancias.add(resultado);
                                numeroComunes.add(comunes.size());
                            }
                        }
                    }


                    //Ordena las conordancias de mayor a menor
                    for (int i = 0; i < concordancias.size() - 1; i++) {

                        for (int j = i + 1; j < concordancias.size(); j++) {

                            if (numeroComunes.get(j) >
                                    numeroComunes.get(i)) {

                                // Intercambiar número de comunes
                                int auxNumero = numeroComunes.get(i);

                                numeroComunes.set(
                                        i,
                                        numeroComunes.get(j)
                                );

                                numeroComunes.set(
                                        j,
                                        auxNumero
                                );

                                // Intercambiar concordancia
                                String auxConcordancia =
                                        concordancias.get(i);

                                concordancias.set(
                                        i,
                                        concordancias.get(j)
                                );

                                concordancias.set(
                                        j,
                                        auxConcordancia
                                );
                            }
                        }
                    }

                    if (concordancias.isEmpty()) System.out.println("No hay parejas con aficiones comunes.");
                    else {

                        // CREAR FICHERO DE SALIDA
                        try (BufferedWriter bw = new BufferedWriter(new FileWriter("TinderFicheros/concordancias.txt"))) {
                            for (String concordancia : concordancias) {
                                bw.write(concordancia);
                                bw.newLine();
                            }

                            System.out.println("Fichero concordancias.txt creado correctamente.");

                        } catch (IOException e) {
                            System.out.println("Error: no se ha podido crear el fichero de salida.");
                        }
                    }
                }
                default -> System.out.println("Esa opcion no sale en el menú \n");
            }

            mostrarMenu();
            System.out.println("Elija otra opción: ");
            opcion = lee.nextInt();
        }
        System.out.println("Saliendo");
    }

    private static boolean comprobarCodigo(String codigo, String nombreFichero) {
        if (codigo.isEmpty()) {
            System.out.println("El código no puede estar vacio");
            return true;
        }

        boolean existe = false;
        try (BufferedReader lectura = new BufferedReader(new FileReader(nombreFichero))) {
            String linea;
            while ((linea = lectura.readLine()) != null) {

                String[] datos = linea.trim().split("\\s+");  //.trim() es para que elimine los espacios del principio y del final del String
                                                                    //.split para que el array tenga en cada una de las posiciones una palabra separada por espacio, es decir, "FUTBOL LEER COMER" en "array[0] = FUTBOL; array[1] = LEER; array[2] = COMER;".
                if (datos.length > 0 && datos[0].equals(codigo)) {
                    existe = true;
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (existe) System.out.println("El código ya existe");
        return false;
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("--------MENÚ PRINCIPAL-------\n");
        System.out.println("1. Añadir usuario");
        System.out.println("2. Mostrar usuarios");
        System.out.println("3. Generar fichero de concordancias");
        System.out.println("4. Salir\n");
        System.out.println("-----------------------------\n");
    }
}
