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
 * Servlet que maneja lo relacionado a empresas, abm, listados, etc.
 * Created by Spirok on 1/11/2015.
 */
public class Empresas extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");

        if ( req.getRequestURI().toLowerCase().endsWith("/buscar_empresas") ) {
            String nombre = req.getParameter("nombre").trim();
            int desde      = Integer.parseInt(req.getParameter("desde"));
            int hasta      = Integer.parseInt(req.getParameter("hasta"));
            System.out.println("BUSQUEDA DE EMPRESAS " + this.getServletName());
            ArrayList<Empresa> listado = Empresa.buscarEmpresas(nombre, desde, hasta);
            int tamanio = (listado != null ? listado.size() : 0);
            StringBuilder buffer = new StringBuilder("{\"Result\":\"OK\", \"TotalRecordCount\":"  + tamanio +
                    ", \"Records\":[");
            if (listado == null)  buffer.append("{}");
            else {
                int i = 0;
                for (Empresa u : listado) {
                    if (i != 0) buffer.append(",");
                    buffer.append("{\"codigo\" : \"" + u.getCodigo() + "\", " +
                            "\"nombre\" : \"" + u.getNombre() + "\", " +
                            "\"cod_postal\" : \"" + u.getCodPostal() + "\", " +
                            "\"localidad\" : \"" + u.getLocalidad() + "\", " +
                            "\"provincia\" : \"" + u.getProvincia() + "\", " +
                            "\"telefono\" : \"" + u.getTelefono() + "\", " +
                            "\"fax\" : \"" + u.getFax() + "\", " +
                            "\"web\" : \"" + u.getWeb() + "\", " +
                            "\"email\" : \"" + u.getEmail() + "\", " +
                            "\"responsable\" : \"" + u.getResponsable() + "\", " +
                            "\"email_responsable\" : \"" + u.getEmailResponsable() + "\", " +
                            "\"consultas_a\" : \"" + u.getConsultasA() + "\", " +
                            "\"estado\" : \"" + u.getEstado() + "\"" +
                            "}");
                    i++;
                }
                buffer.append("]}");
                System.out.println(buffer);
                out.write(buffer.toString());
            }
        }
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");

        if ( req.getRequestURI().toLowerCase().endsWith("/listado_empresas") ) {
            System.out.println("LISTADO DE EMPRESAS " + this.getServletName());
            ArrayList<Empresa> listado = Empresa.todosEmpresas();
            StringBuilder buffer = new StringBuilder("{\"Result\":\"OK\",\"Records\":[");
            int i = 0;
            if (listado == null) { return; }
            for (Empresa u : listado) {
                if (i != 0) buffer.append(",");
                buffer.append("{\"codigo\" : \"" + u.getCodigo() + "\", " +
                        "\"nombre\" : \"" + u.getNombre() + "\", " +
                        "\"cod_postal\" : \"" + u.getCodPostal() + "\", " +
                        "\"localidad\" : \"" + u.getLocalidad() + "\", " +
                        "\"provincia\" : \"" + u.getProvincia() + "\", " +
                        "\"telefono\" : \"" + u.getTelefono() + "\", " +
                        "\"fax\" : \"" + u.getFax() + "\", " +
                        "\"web\" : \"" + u.getWeb() + "\", " +
                        "\"email\" : \"" + u.getEmail() + "\", " +
                        "\"responsable\" : \"" + u.getResponsable() + "\", " +
                        "\"email_responsable\" : \"" + u.getEmailResponsable() + "\", " +
                        "\"consultas_a\" : \"" + u.getConsultasA() + "\", " +
                        "\"estado\" : \"" + u.getEstado() + "\"" +
                        "}");
                i++;
            }
            buffer.append("]}");
            out.write(buffer.toString());
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/listado_empresas_min") ) {
            System.out.println("LISTADO DE EMPRESAS MIN " + this.getServletName());
            ArrayList<Empresa> listado = Empresa.todosEmpresas();
            StringBuilder buffer = new StringBuilder("{\"Result\":\"OK\",\"Records\":[");
            int i = 0;
            if (listado == null) { return; }
            for (Empresa u : listado) {
                if (i != 0) buffer.append(",");
                buffer.append("{\"codigo\" : \"" + u.getCodigo() + "\", " +  "\"nombre\" : \"" + u.getNombre() + "\"}");
                i++;
            }
            buffer.append("]}");
            out.write(buffer.toString());
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/info_empresa") ) {
            System.out.println("INFO DE EMPRESA " + this.getServletName());
            int id = Integer.parseInt(req.getParameter("select_empresa").trim());
            Empresa e = Empresa.obtenerEmpresa(id);
            if (e == null) { System.err.println("no existe empresa id : " + id + " " + this.getServletName()); return; }
            String ret = "{\"codigo\" : \"" + e.getCodigo() + "\", " +
                        "\"nombre\" : \"" + e.getNombre() + "\", " +
                        "\"cod_postal\" : \"" + e.getCodPostal() + "\", " +
                        "\"localidad\" : \"" + e.getLocalidad() + "\", " +
                        "\"provincia\" : \"" + e.getProvincia() + "\", " +
                        "\"telefono\" : \"" + e.getTelefono() + "\", " +
                        "\"fax\" : \"" + e.getFax() + "\", " +
                        "\"web\" : \"" + e.getWeb() + "\", " +
                        "\"email\" : \"" + e.getEmail() + "\", " +
                        "\"responsable\" : \"" + e.getResponsable() + "\", " +
                        "\"email_responsable\" : \"" + e.getEmailResponsable() + "\", " +
                        "\"consultas_a\" : \"" + e.getConsultasA() + "\", " +
                        "\"estado\" : \"" + e.getEstado() + "\", " +
                        "\"id_usr\" : \"" + e.getUsuario().getId() + "\", " +
                        "\"nick\" : \"" + e.getUsuario().getNombre() + "\", " +
                        "\"password\" : \"" + e.getUsuario().getPasswd() + "\"" +
                    "}";
            out.write(ret);
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/agregar_empresa") ) {
            int cod_emp    = 0;
            int cod_postal = 0;
            int id_usuario;
            // obteniendo datos ingresados
            try {
                cod_emp    = Integer.parseInt(req.getParameter("cod_empresa"));
                cod_postal = Integer.parseInt(req.getParameter("cod_postal"));
            } catch (NumberFormatException nfe) {
                System.err.println(nfe.getMessage());
            }
            id_usuario     = Integer.parseInt(req.getParameter("id_usr"));
            String nombre  = req.getParameter("nombre");
            String dir     = req.getParameter("direccion");
            String loc     = req.getParameter("localidad");
            String prov    = req.getParameter("provincia");
            String tel     = req.getParameter("telefono");
            String fax     = req.getParameter("fax");
            String email   = req.getParameter("email");
            String web     = req.getParameter("web");
            String respnb  = req.getParameter("responsable");
            String e_resp  = req.getParameter("email_resp");
            String conslt  = req.getParameter("consultas_a");
            String nom_usr = req.getParameter("usuario");
            String clave   = req.getParameter("clave");
            boolean estado = ( req.getParameter("estado").equals("on") );

            // creando objeto empresa
            Usuario u   = new Usuario(id_usuario, nom_usr, clave, new Perfil(2));
            Empresa emp = new Empresa(cod_emp, nombre, dir, cod_postal, loc, prov, tel, fax, web, email, respnb,
                    e_resp, conslt, estado, u);

            // intentando agregar la empresa
            TreeMap<Boolean, String> ret = Empresa.agregarEmpresa(emp);
            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se agrego correctamente
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + "\"}";
                System.out.println("se agrego " + emp);
            } else {
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/eliminar_empresa") ) {
            int cod_emp  = Integer.parseInt(req.getParameter("cod_empresa"));
            Empresa e = Empresa.obtenerEmpresa(cod_emp);
            TreeMap<Boolean, String> ret = Empresa.eliminarEmpresa(e);
            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se elimino correctamente
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + "\"}";
                System.out.println("se elimino " + e);
            } else {
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/modificar_empresa") ) {
            int cod_emp    = 0;
            int cod_postal = 0;
            int id_usuario;
            try {
                cod_emp    = Integer.parseInt(req.getParameter("cod_empresa"));
                cod_postal = Integer.parseInt(req.getParameter("cod_postal"));
            } catch (NumberFormatException nfe) {
                System.err.println(nfe.getMessage());
            }
            id_usuario     = Integer.parseInt(req.getParameter("id_usr"));
            String nombre  = req.getParameter("nombre");
            String dir     = req.getParameter("direccion");
            String loc     = req.getParameter("localidad");
            String prov    = req.getParameter("provincia");
            String tel     = req.getParameter("telefono");
            String fax     = req.getParameter("fax");
            String email   = req.getParameter("email");
            String web     = req.getParameter("web");
            String respnb  = req.getParameter("responsable");
            String e_resp  = req.getParameter("email_resp");
            String conslt  = req.getParameter("consultas_a");
            String nom_usr = req.getParameter("usuario");
            String clave   = req.getParameter("clave");
            boolean estado = ( req.getParameter("estado").equals("on") );

            Usuario u   = new Usuario(id_usuario, nom_usr, clave, new Perfil(2));
            Empresa emp = new Empresa(cod_emp, nombre, dir, cod_postal, loc, prov, tel, fax, web, email, respnb,
                    e_resp, conslt, estado, u);

            TreeMap<Boolean, String> ret = Empresa.modificarEmpresa(emp);
            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se agrego correctamente
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + "\"}";
                System.out.println("se modifico " + emp);
            } else {
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }


        out.close();
    }
}
