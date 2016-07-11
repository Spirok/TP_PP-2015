package com.me.webapp.servlets;

import com.me.webapp.modelo.UsuarioSesion;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet que retorna json con info de la sesion.
 * Created by Spirok on 28/10/2015.
 */
public class DatosSesion extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        PrintWriter out    = resp.getWriter();
        HttpSession sesion = req.getSession();

        String retorno;
        UsuarioSesion uSesion = (UsuarioSesion)sesion.getAttribute("sesion_usuario");

        if (uSesion != null) {
            retorno = "{\"resultado\" : \"ok\",\"nombre\" : \"" + uSesion.getNombre() + "\", \"id_perfil\" : \"" + uSesion.getIdPerfil() + "\", \"nombre_perfil\" : \"" + uSesion.getNombrePerfil() + " \", \"cod_emp\" : \"" + uSesion.getCodigoEmpresa() + "\", \"nombre_empresa\" : \"" + uSesion.getNombreEmpresa() +  "\", \"email_empresa\" : \"" + uSesion.getEmailEmpresa() +"\"}";
        } else {
            retorno = "{\"resultado\" : \"error\",\"path\" : \"/\"}";
            System.out.println("¡¡¡ATENCION!!!. Intentaron ingresar sin loguearse...");
        }
        out.write(retorno);
        out.close();
    }
}
