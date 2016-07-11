package com.me.webapp.servlets;

import com.me.webapp.modelo.Empresa;
import com.me.webapp.modelo.Perfil;
import com.me.webapp.modelo.Usuario;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Servlet que maneja tod o lo relacionado a usuarios. listados, abm, etc.
 * Created by Spirok on 28/10/2015.
 */
public class Usuarios extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");

        if ( req.getRequestURI().toLowerCase().endsWith("/listado_usuarios") ) {
            System.out.println("LISTADO DE USUARIOS " + this.getServletName());
            ArrayList<Usuario> listado = Usuario.todosUsuarios();
            StringBuilder buffer = new StringBuilder("{\"Result\":\"OK\",\"Records\":[");
            int i = 0;
            for (Usuario u : listado) {
                if (i != 0) buffer.append(",");
                buffer.append("{\"id\" : \"" + u.getId() + "\"," +
                               "\"nick\" : \"" + u.getNombre() + "\"}");
                i++;
            }
            buffer.append("]}");
            out.write(buffer.toString());
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/listado_perfiles") ) {
            System.out.println("LISTADO DE PERFILES " + this.getServletName());
            ArrayList<Perfil> listado = Perfil.todosPerfiles();
            StringBuilder buffer = new StringBuilder("{\"Result\":\"OK\",\"Records\":[");
            int i = 0;
            for (Perfil p : listado) {
                if (i != 0) buffer.append(",");
                buffer.append("{\"id\" : \"" + p.getId() + "\"," +
                              "\"descripcion\" : \"" + p.getDescripcion() + "\"}");
                i++;
            }
            buffer.append("]}");
            out.write(buffer.toString());
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/operadores_disponibles") ) {
            System.out.println("LISTADO DE USUARIOS OPERADORES DISPONIBLES " + this.getServletName());
            ArrayList<Usuario> listado = Usuario.todosOperadoresDisponibles();
            StringBuilder buffer = new StringBuilder("{\"Result\":\"OK\",\"Records\":[");
            int i = 0;
            for (Usuario u : listado) {
                if (i != 0) buffer.append(",");
                buffer.append("{\"cod_usuario\" : \"" + u.getId() + "\"," +
                        "\"nick\" : \"" + u.getNombre() + "\"," +
                        "\"password\" : \"" + u.getPasswd() + "\"}");
                i++;
            }
            buffer.append("]}");
            out.write(buffer.toString());
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/agregar_usuario") ) {
            // obteniendo datos del formulario
            String nombre = req.getParameter("nick").trim();
            String passwd = req.getParameter("password").trim();
            int perfil    = Integer.parseInt(req.getParameter("perfil").trim());
            // creando usuario
            Usuario u = new Usuario(nombre, passwd, new Perfil(perfil));
            // dando de alta el usuario
            TreeMap<Boolean, String> ret = Usuario.agregarUsuario(u);
            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se agrego correctamente
                System.out.println(msj + " " + u );
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + " \"}";
            } else {
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/eliminar_usuario") ) {
            // obteniendo datos del formulario
            int id        = Integer.parseInt(req.getParameter("codigo").trim());
            String nombre = req.getParameter("nick").trim();
            String passwd = req.getParameter("password").trim();
            int perfil    = Integer.parseInt(req.getParameter("perfil").trim());
            // creando usuario
            Usuario u = new Usuario(id, nombre, passwd, new Perfil(perfil));
            // dando de baja el usuario
            TreeMap<Boolean, String> ret = Usuario.eliminarUsuario(u);
            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se elimino correctamente
                System.out.println(msj + " " + u );
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + " \"}";
            } else {
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/modificar_usuario") ) {
            // obteniendo datos del formulario
            int id        = Integer.parseInt(req.getParameter("codigo").trim());
            String nombre = req.getParameter("nick").trim();
            String passwd = req.getParameter("password").trim();
            int perfil    = Integer.parseInt(req.getParameter("perfil").trim());
            // creando usuario
            Usuario u = new Usuario(id, nombre, passwd, new Perfil(perfil));
            // modificando el usuario
            TreeMap<Boolean, String> ret = Usuario.modificarUsuario(u);
            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se modifico correctamente
                System.out.println(msj + " " + u );
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + " \"}";
            } else {
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/info_usuario") ) {
            String json;
            String usr = req.getParameter("usuario_seleccionado");
            Usuario u  = Usuario.obtenerUsuario(usr);
            if (u == null) { System.err.println("no existe usuario " + usr); return; }
            json = "{\"cod_usuario\":\"" + u.getId() + " \", \"nick\":\"" + u.getNombre() + "\", \"password\":\"" + u.getPasswd() + "\", \"id_perfil\":\"" + u.getPerfil().getId() + "\"  }";
            out.write(json);
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/info_usuario_operador") ) {
            String json;
            String usr = req.getParameter("nombre_usuario");
            Empresa e = Usuario.obtenerUsuarioOperador(usr);
            if (e == null) { System.err.println("no existe usuario operador " + usr); return; }
            json = "{\"cod_usuario\":\"" + e.getUsuario().getId() + " \", \"nick\":\"" + e.getUsuario().getNombre() + "\", \"codigo_empresa\":\"" + e.getCodigo() + "\", \"nombre_empresa\":\"" + e.getNombre() + "\"  }";
            out.write(json);
        }

        out.close();
    }
}
