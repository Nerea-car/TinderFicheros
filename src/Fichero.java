import java.io.*;
import java.util.Scanner;


public class Fichero {
    static void main() {
        Scanner lee = new Scanner(System.in);


        System.out.println("--------MENÚ PRINCIPAL-------\n");
        System.out.println("1. Añadir usuario");
        System.out.println("2. Mostrar usuarios");
        System.out.println("3. Generar fichero de concordancias");
        System.out.println("4. Salir\n");
        System.out.println("-----------------------------\n");
        System.out.println("Elige una opcion: ");
        int opcion = lee.nextInt();


        int codigo = 109;




        while (opcion != 4){


            switch (opcion) {
                case 1 -> {
                    codigo++;
                    System.out.println("Escribe las aficiones de este usuario separadas por espacios: ");
                    String aficiones = lee.nextLine();


                    try (FileWriter escribiendo = new FileWriter("usuarios.txt", true)) {
                        escribiendo.write("U" + codigo + " " + aficiones.toUpperCase() + "\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                    System.out.println("Añadido");


                }
                case 2 -> {
                    System.out.println("Mostrando el fichero");
                    System.out.println();


                    //Lee lo que hay escrito en el fichero
                    try (BufferedReader lectura = new BufferedReader(new FileReader("usuarios.txt"))) {
                        String linea;
                        while ((linea = lectura.readLine()) != null) {
                            System.out.println(linea);
                        }
                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                }
                case 3 -> {
                    System.out.println("Generando fichero");
                    System.out.println();
                    System.out.println("Como se llamará el fichero: ");
                    String ruta = lee.nextLine();


                    System.out.println("Escriba que quiere añadir en el fichero:");
                    String escrito = lee.nextLine();


                    //Crear fichero                                    append true para que no se sobrescriba
                    try (FileWriter fileWriter = new FileWriter(ruta, true)) {
                        fileWriter.write(escrito + "\n");


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                default -> System.out.println("Esa opcion no sale en el menú \n");
            }


            System.out.println("Elija otra opción: ");
            opcion = lee.nextInt();
        }
        System.out.println("Saliendo");
    }
}
