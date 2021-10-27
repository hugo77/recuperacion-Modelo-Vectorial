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
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author hugo
 */
public class CrearListaTerminos {

    private String rutaDirectorio;
    private File directorioArchivos;
    private File[] listaArchivos;
    private ArrayList<String> listaTerminosUnicos = new ArrayList<String>();

    public CrearListaTerminos(String rutaDirectorio) throws IOException {
        this.rutaDirectorio = rutaDirectorio;
        leerDictectorio();
        formarListaDeTerminos();
    }

    public File[] getListaArchivos() {
        return listaArchivos;
    }

    public void setListaArchivos(File[] listaArchivos) {
        this.listaArchivos = listaArchivos;
    }

    
    
    
    public String getRutaDirectorio() {
        return rutaDirectorio;
    }

    public void setRutaDirectorio(String rutaDirectorio) {
        this.rutaDirectorio = rutaDirectorio;
    }

    public ArrayList<String> getListaTerminosUnicos() {
        return listaTerminosUnicos;
    }

    public void setListaTerminosUnicos(ArrayList<String> listaTerminosUnicos) {
        this.listaTerminosUnicos = listaTerminosUnicos;
    }

    //-------------------Metodo para leer el directorio donde estan los archivos para el sistema de ir  
    public File[] leerDictectorio() {

        directorioArchivos = new File(getRutaDirectorio());
        if (directorioArchivos.isDirectory() && directorioArchivos.listFiles().length > 0) {
            listaArchivos = directorioArchivos.listFiles();
             return listaArchivos;
        } else {

            System.out.println("El directorio no existe o esta vacio....");
           return null;
        }

    }

    //-------------------------------------------------------------------
    public String leerArchivo(File archivo) throws FileNotFoundException, IOException {
        FileReader reader = new FileReader(archivo);
        BufferedReader bufered = new BufferedReader(reader);
        String linea = "";
        String salida="";
        while ((linea = bufered.readLine()) != null) {
            ArrayList palabras = eliminarVacias(linea);
            for (int i = 0; i < palabras.size(); i++) {
                String palabra = (String) palabras.get(i);
                if (!listaTerminosUnicos.contains(palabra)) {
                    listaTerminosUnicos.add(palabra);
                }

            }
            salida+=linea;
        }
        
        return salida;
    }

    //----------------------------------metodo elimina las palabras vacias del texto que se envia como parametro  
    public ArrayList eliminarVacias(String listaPalabras) {

        String[] listaVacias = {"si", "es", "su", "por", "porque", "de", "un", "y", "de", "el", "en", "que", "tienen", "tiene",
            "pasa", "mucho", "asciende"};
        ArrayList<String> textoSinVacias = new ArrayList<String>();
        listaPalabras = listaPalabras.replace(",", "");
        listaPalabras = listaPalabras.replace(".", "");
        StringTokenizer tokens = new StringTokenizer(listaPalabras);

        while (tokens.hasMoreElements()) {
            int encuentra = 0;
            String tokenActual = tokens.nextElement().toString();
            for (int i = 0; i < listaVacias.length; i++) {
                String palabraVacia = listaVacias[i];
                if (palabraVacia.equals(tokenActual)) {
                    encuentra = 1;
                }
            }
            if (encuentra == 0) {
                textoSinVacias.add(tokenActual);
            }
        }

        return textoSinVacias;
    }

    //---------------------
//--------------------------------Metodo para formar la lista de terminos unicos 
    public void formarListaDeTerminos() throws IOException {
        for (int i = 0; i < listaArchivos.length; i++) {
            File arhivoActual = listaArchivos[i];
            leerArchivo(arhivoActual);
        }
    }

//    public static void main(String[] args) throws IOException {
//        CrearListaTerminos ct = new CrearListaTerminos("/home/hugo/e_index/");
//
//        for (int i = 0; i < ct.getListaTerminosUnicos().size(); i++) {
//            System.out.println(i + "---" + ct.getListaTerminosUnicos().get(i));
//
//        }
//
//    }

}
