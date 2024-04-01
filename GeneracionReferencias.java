import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class GeneracionReferencias {
    int tamPag;
    int nFilas;
    int nColumnas;

    int nPaginas;

    ArrayList<String> instrucciones;

    //en memoria se guarda primero filtro, datos, resultado


    public GeneracionReferencias(int tamPag, int nFilas, int nColumnas) {
        this.tamPag = tamPag;
        this.nFilas = nFilas;
        this.nColumnas = nColumnas;


        nPaginas = Math.ceilDiv((((nFilas*nColumnas)*2*4) + (9*4)), tamPag);
      //nPaginas = (((nFilas * nColumnas) * 2 * 4) + (9 * 4) + tamPag - 1) / tamPag;
 
        instrucciones = new ArrayList<>();
    }

    public void generarReferencias(){
        simularReferencias();
        escribirReferencias();
    }

    public void simularReferencias(){
        int nf = nFilas; 
        int nc = nColumnas; 

        for (int i = 1; i < nf - 1; i++) {
            for (int j = 1; j < nc - 1; j++) {
                for (int a = -1; a <= 1; a++) {
                    for (int b = -1; b <= 1; b++) {
                        int i2 = i + a;
                        int j2 = j + b;
                        int i3 = 1 + a;
                        int j3 = 1 + b;

                        int[] m = obtenerPaginaVirtualYDesplazamiento(1, i2, j2);
                        instrucciones.add("M[" + i2 + "][" + j2 + "]," + m[0] + "," + m[1] + ",R");

                        int[] f = obtenerPaginaVirtualYDesplazamiento(0, i3, j3);
                        instrucciones.add("F[" + i3 + "][" + j3 + "]," + f[0] + "," + f[1] + ",R");

                    }
                }
                int[] r = obtenerPaginaVirtualYDesplazamiento(2, i, j);
                instrucciones.add("R[" + i + "][" + j + "]," + r[0] + "," + r[1] + ",W");
            }
        }
        for (int i = 0; i < nc; i++) {
            int[] r = obtenerPaginaVirtualYDesplazamiento(2,0, i);
            instrucciones.add("R[0][" + i + "]," + r[0] + "," + r[1] + ",W");

            r = obtenerPaginaVirtualYDesplazamiento(2, nf-1, i);
            instrucciones.add("R[" + (nf-1) + "][" + i + "]," + r[0] + "," + r[1] + ",W");
        }
        for (int i = 1; i < nf - 1; i++) {
            int[] r = obtenerPaginaVirtualYDesplazamiento(2,i, 0);
            instrucciones.add("R[" + (i) + "][" + 0 + "]," + r[0] + "," + r[1] + ",W");

            r = obtenerPaginaVirtualYDesplazamiento(2, i, nc-1);
            instrucciones.add("R[" + (i) + "][" + (nc-1) + "]," + r[0] + "," + r[1] + ",W");
        }
    }

    public int[] obtenerPaginaVirtualYDesplazamiento(int caso, int fila, int columna){
        int pos = 0;
        //caso 0 = filtro
        if(caso == 1){   //datos
            pos += 9;
        }
        else if(caso == 2){ //resultado
            pos += 9 + (nFilas*nColumnas);
        }

        pos += (fila * nColumnas) + (columna);

        int nPag = Math.floorDiv(pos, tamPag/4);
        int desplazamiento = (pos % (tamPag/4)) * 4;

        return new int[]{nPag,desplazamiento};
    }

    public void escribirReferencias(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("archivo.txt"));

            writer.write("TP=" + tamPag + "\n");
            writer.write("NF=" + nFilas + "\n");
            writer.write("NC=" + nColumnas + "\n");
            writer.write("NF_NC_Filtro=3\n");
            writer.write("NR=" + instrucciones.size() + "\n");
            writer.write("NP=" + nPaginas + "\n");

            for(String instruccion : instrucciones){
                writer.write(instruccion + "\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
