package com.me.webapp.servlets;

import com.me.webapp.modelo.Rubro;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Servlet que maneja tod o lo relacionado a rubros. listados, abm, etc.
 * Created by Spirok on 17/11/2015.
 */
public class Rubros extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");

        if ( req.getRequestURI().toLowerCase().endsWith("/listado_rubros") ) {
            System.out.println("LISTADO DE RUBROS " + this.getServletName());
            ArrayList<Rubro> listado = Rubro.todosRubros();
            StringBuilder buffer = new StringBuilder("{\"Result\":\"OK\",\"Records\":[");
            int i = 0;
            for (Rubro r : listado) {
                if (i != 0) buffer.append(",");
                buffer.append("{\"codigo\" : \"" + r.getCodigo() + "\"," +
                        "\"nombre\" : \"" + r.getNombre() + "\"}");
                i++;
            }
            buffer.append("]}");
            System.out.println(buffer);
            out.write(buffer.toString());
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/agregar_rubro") ) {
            System.out.println("AGREGAR RUBRO " + this.getServletName());

            // obteniendo datos del formulario
            String nombre = req.getParameter("nombre").trim();
            boolean estado = ( req.getParameter("estado").equals("on") );

            // creando rubro
            Rubro r = new Rubro(nombre, estado);

            // dando de alta el rubro
            TreeMap<Boolean, String> ret = Rubro.agregarRubro(r);
            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se agrego correctamente
                System.out.println(msj + " " + r );
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + " \"}";
            } else {
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/eliminar_rubro") ) {
            System.out.println("ELIMINAR RUBRO " + this.getServletName());

            // obteniendo datos del formulario
            int codigo = Integer.parseInt(req.getParameter("codigo").trim());
            String nombre = req.getParameter("nombre").trim();
            boolean estado = ( req.getParameter("estado").equals("on") );

            // creando rubro
            Rubro r = new Rubro(codigo, nombre, estado);

            // dando de baja el rubro
            TreeMap<Boolean, String> ret = Rubro.eliminarRubro(r);
            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se elimino correctamente
                System.out.println(msj + " " + r );
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + " \"}";
            } else {
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/modificar_rubro") ) {
            System.out.println("MODIFICAR RUBRO " + this.getServletName());
            // obteniendo datos del formulario
            int codigo = Integer.parseInt(req.getParameter("codigo").trim());
            String nombre = req.getParameter("nombre").trim();
            boolean estado = ( req.getParameter("estado").equals("on") );

            // creando rubro
            Rubro r = new Rubro(codigo, nombre, estado);

            // modificando la marca
            TreeMap<Boolean, String> ret = Rubro.modificarRubro(r);
            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se modifico correctamente
                System.out.println(msj + " " + r );
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + " \"}";
            } else {
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/info_rubro") ) {
            System.out.println("INFO RUBRO " + this.getServletName());

            String json;
            int cod  = Integer.parseInt(req.getParameter("rubro_seleccionado"));
            Rubro r = Rubro.obtenerRubro(cod);
            if (r  == null) { System.err.println("no existe rubro codigo " + cod); return; }
            json = "{\"codigo\":\"" + r.getCodigo() + " \", \"nombre\":\"" + r.getNombre() + "\", \"estado\":\"" + r.getEstado() +  "\"  }";
            System.out.println(json);
            out.write(json);
        }

        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");

        if ( req.getRequestURI().toLowerCase().endsWith("/buscar_rubros") ) {
            System.out.println("BUSQUEDA RUBROS " + this.getServletName());

            String nombre = req.getParameter("nombre").trim();
            int desde      = Integer.parseInt(req.getParameter("desde"));
            int hasta      = Integer.parseInt(req.getParameter("hasta"));
            ArrayList<Rubro> listado = Rubro.buscarRubros(nombre, desde, hasta);

            System.out.println("NOMBRE :" + nombre + " DESDE : " + desde + " HASTA : " + hasta);
            int tamanio = (listado != null ? listado.size() : 0);
            StringBuilder buffer = new StringBuilder("{\"Result\":\"OK\", \"TotalRecordCount\": \""  + tamanio +
                    "\", \"Records\":[");
            if (listado == null)  buffer.append("{}");
            else {
                int i = 0;
                for (Rubro r : listado) {
                    if (i != 0) buffer.append(",");
                    buffer.append("{\"codigo\" : \"" + r.getCodigo() + "\"," +
                            "\"nombre\" : \"" + r.getNombre() + "\"," +
                            "\"estado\" : \"" + r.getEstado() + "\"}");
                    i++;
                }
                buffer.append("]}");
                System.out.println(buffer);
            }
            out.write(buffer.toString());
        }

        out.close();
    }
}
