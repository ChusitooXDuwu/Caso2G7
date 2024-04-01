import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CalcularDatos extends Thread{
    boolean tipoThread;

    static boolean terminado = false;

    static int hits = 0;
    static int fallos = 0;

    static int nMarcos;
    static String archivo;

    static int[][] marcos;

    static int[] tablaDePaginas;

    public CalcularDatos(boolean tipoThread){
        this.tipoThread = tipoThread;
    };

    public CalcularDatos(int nMarcos, String archivo) {
        CalcularDatos.nMarcos = nMarcos;
        CalcularDatos.archivo = archivo;

        marcos = new int[nMarcos][3];

        for(int i = 0; i < nMarcos; i++){
            marcos[i] = new int[]{-1,0,0}; //{pagina, bit R, bit M}
        }
    }

    public void ejecutar(){
        CalcularDatos thread1 = new CalcularDatos(false);
        CalcularDatos thread2 = new CalcularDatos(true);


        thread1.start();
        thread2.start();

    }

    public void run(){

        if(tipoThread){  //Thread Actualizador tabla 

            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                br.readLine();
                br.readLine();
                br.readLine();
                br.readLine();
                int nr = Integer.parseInt(br.readLine().replace("NR=", ""));
                int npags = Integer.parseInt(br.readLine().replace("NP=", ""));

                tablaDePaginas = new int[npags];

                for(int i = 0; i < npags; i++){
                    tablaDePaginas[i] = -1;
                }

                String instruccion = br.readLine();
                for(int i = 0; i < nr; i++){
                    String[] split = instruccion.split(",");

                    analizarInstruccion(Integer.parseInt(split[1]), split[3]);
                    instruccion = br.readLine();

                    sleep(1);
                }

                terminado = true;


            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        else{   // Thread actualización bit R
            
            while(!terminado){
                
                resetBitR();

                try {
                    sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Hits: " + hits);
            System.out.println("Fallos de página: " + fallos);
            System.out.println("Total de referencias: " + (hits + fallos));
        }


    }
    public synchronized void resetBitR(){
        for(int i = 0; i < marcos.length; i++){
            marcos[i][1] = 0;
        }
    }

    public synchronized void analizarInstruccion(int pagina, String operacion){
        if(tablaDePaginas[pagina] != -1){
            hits++;

            marcos[tablaDePaginas[pagina]][1] = 1;
            if(operacion.equals("W")){
                marcos[tablaDePaginas[pagina]][2] = 1;
            }
        }
        else{

            fallos++;

            int index = noUsadasRecientemente();  //pagina para reemplazar

            if(marcos[index][0] != -1){
                tablaDePaginas[marcos[index][0]] = -1;
            }
            tablaDePaginas[pagina] = index;

            marcos[index][0] = pagina;
            marcos[index][1] = 1;
            if(operacion.equals("W")){
                marcos[index][2] = 1;
            }
            else{
                marcos[index][2] = 0;
            }

        }
    }

    public  synchronized int noUsadasRecientemente(){
        int clase0 = -1;
        int clase1 = -1;
        int clase2 = -1;
        int clase3 = -1;

        for(int i = 0; i < marcos.length; i++){
            int[] marco = marcos[i];

            if(marco[0] == -1){
                return i;
            }

            if(marco[1] == 0 && marco[2] == 0 && clase0 == -1){
                clase0 = i;
            }
            else if(marco[1] == 0 && marco[2] == 1 && clase1 == -1){
                clase1 = i;
            }
            else if(marco[1] == 1 && marco[2] == 0 && clase2 == -1){
                clase2 = i;
            }
            else if(marco[1] == 1 && marco[2] == 1 && clase3 == -1){
                clase3 = i;
            }

            if(clase0 != -1 && clase1 != -1 && clase2 != -1 && clase3 != -1){
                break;
            }
        }

        if(clase0 != -1){
            return clase0;
        }
        if(clase1 != -1){
            return clase1;
        }
        if(clase2 != -1){
            return clase2;
        }
        return clase3;
    }

}
