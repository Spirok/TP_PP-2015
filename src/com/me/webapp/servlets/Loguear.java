package com.me.webapp.servlets;

import com.me.webapp.modelo.Empresa;
import com.me.webapp.modelo.Usuario;
import com.me.webapp.modelo.UsuarioSesion;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet que recibe parametros nombre y password(un usuario) por post y loguea al usuario al sistea (si existe).
 * Ademas setea variables de sesion, y retorna json con el resultado del login
 * Created by Spirok on 26/10/2015.
 */
public class Loguear extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out    = resp.getWriter();
        HttpSession sesion = req.getSession();

        resp.setContentType("application/json");

        String nombre = req.getParameter("nombre");
        String passwd = req.getParameter("password");

        boolean usrValido = Usuario.validarUsuario( new Usuario(nombre, passwd) );

        if (usrValido) {
            // usuario valido, obtengo informacion y seteo variables de sesion
            Usuario u = Usuario.obtenerUsuario(nombre);
            if (u == null) {
                System.err.println("error al obtener usuario : " + nombre + " " + this.getServletName());
                return;
            }

            UsuarioSesion uSesion = null;

            // si el usuario es operador
            if (u.getPerfil().getId() == 2) {
                // obtengo la empresa del usuario operador
                Empresa empUsuario = Usuario.obtenerUsuarioOperador(nombre);
                if (empUsuario == null) { System.err.println("usuario : " + nombre + " no tiene asignado una empresa");  }
                int codEmp = (empUsuario != null ? empUsuario.getCodigo() : 0 );
                String nomEmp = (empUsuario != null ? empUsuario.getNombre() : "");
                String emlEmp = (empUsuario != null ? empUsuario.getEmail() : "");
                uSesion = new UsuarioSesion(u.getId(), u.getNombre(), u.getPerfil().getId(), u.getPerfil().getDescripcion(), codEmp, nomEmp, emlEmp);
            }

            if (u.getPerfil().getId() == 1) {
                uSesion = new UsuarioSesion(u.getId(), u.getNombre(), u.getPerfil().getId(), u.getPerfil().getDescripcion());
            }

            // seteando objeto usario sesion
            sesion.setAttribute("sesion_usuario", uSesion);
            System.out.println("SE LOGUEO USUARIO : " + uSesion + " " + this.getServletName());
        }

        String objJson = (usrValido ? "{\"resultado\" : \"ok\", \"path\" : \"./docs/main.html\"}" :
                                      "{\"resultado\" : \"error\"}");

        out.write(objJson);
        out.close();
    }
}
