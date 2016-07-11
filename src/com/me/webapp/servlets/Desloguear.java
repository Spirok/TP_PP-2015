package com.me.webapp.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet que cierra la sesion y envia al cliente a login.html
 * Created by Spirok on 28/10/2015.
 */
public class Desloguear extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession();

        System.out.println("USUARIO " + sesion.getAttribute("sesion_usuario")  + " CERRO SESION");

        // cerrando sesion
        sesion.invalidate();

        // redireccionando al directorio principal (login.html)
        resp.sendRedirect("/");
    }
}
