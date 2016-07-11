package com.me.webapp.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Spirok on 26/10/2015.
 */
public class Prueba extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
        PrintWriter out = respuesta.getWriter();
        String ret      = "hola mundo";

        out.write(ret);
        out.close();

    }
}
