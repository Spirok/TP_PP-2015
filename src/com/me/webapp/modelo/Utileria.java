package com.me.webapp.modelo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase de utileria.
 * Created by spirok on 13/12/15.
 */
public class Utileria {


    /**
     * Metodo estatico que apartir de una archivo de configurarion .properties retorna el valor de la propiedad
     * indicada como parametro.
     * @param name String nombre de la propiedad
     * @return String valor de la propiedad indicada.
     */
    public static String getValueProperty(String name) {
        Properties prop = new Properties();
        InputStream input;
        //System.out.println("Working Directory = " +System.getProperty("user.dir"));
        try {
            input = new FileInputStream("./src/com/me/webapp/config/config.properties");
            // load a properties file
            prop.load(input);
            // get the property value
            return prop.getProperty(name);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
