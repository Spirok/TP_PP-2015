package com.me.webapp.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Servlet que retorna codigo html del archivo especificado en el parametro 'ruta_archivo' por post.
 * Created by Spirok on 27/10/2015.
 */
public class PlantillaHtml extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        PrintWriter out;
        BufferedReader br;
        StringBuilder buffer = new StringBuilder();
        String rutaArchivo   = req.getParameter("ruta_archivo");
        File archivo         = new File(rutaArchivo);
        // chequeando que el archivo exista
        if (!archivo.exists()) {System.err.println(rutaArchivo + " Archivo inexistente. " + this.getServletName()); return; }
        // chequeando que el archivo pueda ser leido
        if (!archivo.canRead()) {System.err.println(rutaArchivo + "Archivo no puede ser leido. " + this.getServletName()); return; }
        try {
            out = resp.getWriter();
            resp.setContentType("text/html");
            br = new BufferedReader(new FileReader(rutaArchivo));
            String sCurrentLine;
            // leyendo archivo html y almacenando su contenido en un buffer
            while ((sCurrentLine = br.readLine()) != null) {
                buffer.append(sCurrentLine);
            }
            br.close();
            out.write(buffer.toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
