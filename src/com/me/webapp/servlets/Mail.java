package com.me.webapp.servlets;

import com.me.webapp.modelo.EnvioCorreo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet encargado de enviar un mail.
 * Created by spirok on 09/12/15.
 */
public class Mail extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String desde        = req.getParameter("desde");
        String destinatario = req.getParameter("destinatario");
        String asunto       = req.getParameter("asunto");
        String mensaje      = req.getParameter("mensaje");

        System.out.println(asunto + " " + mensaje);

        if (EnvioCorreo.enviarCorreo(desde, destinatario, asunto, mensaje) )
            out.write("{\"resultado\" : \"ok\" }");
        else
            out.write("{\"resultado\" : \"error\" }");
        out.close();
    }
}
