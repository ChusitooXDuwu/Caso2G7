import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main{
    
    public static void main(String[] args) {
        String input = input("Seleccione el modo (1: Generar Referencias/ 2: Calcular Datos)");

        if(input.equals("1")){

            int tamPag = Integer.parseInt(input("Ingrese el tamaño de página"));
            int nFilas = Integer.parseInt(input("Ingrese el número de filas de la matriz"));
            int nColumnas = Integer.parseInt(input("Ingrese el número de columnas de la matriz"));

            GeneracionReferencias generacionReferencias = new GeneracionReferencias(tamPag, nFilas, nColumnas);

            generacionReferencias.generarReferencias();

            System.out.println("Rerefencias creadas en el archivo: archivo.txt");
        }
        else if(input.equals("2")){
            int nMarcos = Integer.parseInt(input("Ingrese el número de marcos de página"));
            String archivo = input("Ingrese el nombre del archivo de referencias");

            CalcularDatos calcularDatos = new CalcularDatos(nMarcos, archivo);
            calcularDatos.ejecutar();
        }
        else{
            System.out.println("Input no válido.");
        }
    }


     public static String input(String mensaje){
		try
		{
			System.out.print(mensaje + ": ");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			return reader.readLine();
		}
		catch (IOException e)
		{
			System.out.println("Error leyendo de la consola");
			e.printStackTrace();
		}
		return null;
	}
}