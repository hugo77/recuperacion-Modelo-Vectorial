/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 *
 * @author hugo
 */
public class CreateIndex {

    private String rutaDitectorio;
    private int[][] matrisTeminosDocumentos;
    private double[][] matrizTfIdf;
    public CrearListaTerminos cLtU;

    public String getRutaDitectorio() {
        return rutaDitectorio;
    }

    public void setRutaDitectorio(String rutaDitectorio) {
        this.rutaDitectorio = rutaDitectorio;
    }

    public CreateIndex(String rutaDitectorio) throws IOException {
        this.rutaDitectorio = rutaDitectorio;
        cLtU = new CrearListaTerminos(rutaDitectorio);
    }

    public int[][] crearMatrizTerminosDocumentos(String query) throws IOException {

        File[] listaArchivos = cLtU.getListaArchivos();
        ArrayList listaTerminos = cLtU.getListaTerminosUnicos();
        matrisTeminosDocumentos = new int[listaArchivos.length + 1][listaTerminos.size()];

        for (int i = 0; i < listaArchivos.length; i++) {
            File archivoActual = listaArchivos[i];
            ArrayList palabrasArchivoActual = cLtU.eliminarVacias(cLtU.leerArchivo(archivoActual));
            for (int j = 0; j < listaTerminos.size(); j++) {
                String terminoActual = listaTerminos.get(j).toString();
                int nvecesTermino = 0;
                for (int k = 0; k < palabrasArchivoActual.size(); k++) {
                    String palabraActual = (String) palabrasArchivoActual.get(k);
                    if (terminoActual.equals(palabraActual)) {
                        nvecesTermino++;
                    }
                }
                matrisTeminosDocumentos[i][j] = nvecesTermino;
            }

        }
        ArrayList arrayQuery = cLtU.eliminarVacias(query);

        for (int j = 0; j < listaTerminos.size(); j++) {
            String terminoActual = listaTerminos.get(j).toString();
            int nvecesTermino = 0;
            for (int k = 0; k < arrayQuery.size(); k++) {
                String palabraActual = (String) arrayQuery.get(k);
                if (terminoActual.equals(palabraActual)) {
                    nvecesTermino++;
                }
            }
            matrisTeminosDocumentos[listaArchivos.length][j] = nvecesTermino;
        }

        return matrisTeminosDocumentos;

    }

    //--------------------------------------------
    //--------------------------Metodo para crear la matriz TF-IDF
    public double[][] crearMatrizTfIdf(int[][] matrisTerminosDocumentos) {
        File[] listaArchivos = cLtU.getListaArchivos();
        ArrayList listaTerminos = cLtU.getListaTerminosUnicos();
        matrizTfIdf = new double[listaArchivos.length + 1][listaTerminos.size()];
        int n = listaTerminos.size();
        int d = listaArchivos.length;

        for (int i = 0; i < listaArchivos.length + 1; i++) {

            for (int j = 0; j < listaTerminos.size(); j++) {
                int tf = matrisTeminosDocumentos[i][j];
                int df = 0;
                for (int k = 0; k < d; k++) {
                    if (matrisTeminosDocumentos[k][j] > 0) {
                        df++;
                    }
                }
                double valIdf = (double) d / df;
                matrizTfIdf[i][j] = Math.log10(valIdf) * matrisTeminosDocumentos[i][j];
            }

        }
        return matrizTfIdf;

    }

    ///--------------------------------------
    //-------------------------------Metodo para imprimir matrices
    public void imprimirMatriz(double[][] matriz) {

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("\t %.2f", matriz[i][j]);

            }
            System.out.println("");
        }

    }

    public void imprimirMatriz(int[][] matriz) {

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print("\t " + matriz[i][j]);

            }
            System.out.println("");
        }

    }

    //-------------------------------------------------------------------
    //------------------------------------Metodo para realizar la consulta y formar lista de resultados
    public void retornarListaResulados(double[][] matrizTfIdf) {

        SortedMap listaResultados = new TreeMap(java.util.Collections.reverseOrder());
        File[] listaArchivos = cLtU.getListaArchivos();
        for (int i = 0; i < matrizTfIdf.length - 1; i++) {
            double valorSimilitudDQ = 0;
            for (int j = 0; j < matrizTfIdf[i].length; j++) {
                valorSimilitudDQ += matrizTfIdf[matrizTfIdf.length - 1][j] * matrizTfIdf[i][j];

            }
            listaResultados.put(valorSimilitudDQ, listaArchivos[i].getName());
        }

        DecimalFormat formato = new DecimalFormat();
        formato.setMaximumFractionDigits(2);
        Iterator iterator = listaResultados.keySet().iterator();
        int i = 1;
        while (iterator.hasNext()) {
            Object doc = iterator.next();
            System.out.println(i + "  Similitud : " + formato.format(doc) + "  Documento :" + listaResultados.get(doc));
            i++;
        }

    }
//------------------------------------------ 

//----------------------------------------------------------------------------    
    public static void main(String[] args) throws IOException {
        CreateIndex crIndex = new CreateIndex("/home/hugo/e_index/");
//        crIndex.imprimirMatriz(crIndex.crearMatrizTerminosDocumentos("cual es el caudal del rio Danubio"));
//
//        System.out.println("-----------------------------------------------------------------");
//        crIndex.imprimirMatriz(crIndex.crearMatrizTfIdf(crIndex.crearMatrizTerminosDocumentos("cual es el caudal del rio Danubio")));
//        System.out.println(crIndex.cLtU.getListaTerminosUnicos());
   String query="cual es el caudal del rio Danubio";
               System.out.println(" Lista de resultados para la consulta: "+ query );
               System.out.println("-----------------------------------------");
        crIndex.retornarListaResulados(crIndex.crearMatrizTfIdf(crIndex.crearMatrizTerminosDocumentos(query)));

    }
}
