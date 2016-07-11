package com.me.webapp.servlets;


import com.me.webapp.modelo.Marca;
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
 * Servlet que maneja tod o lo relacionado a marcas. listados, abm, etc.
 * Created by Spirok on 17/11/2015.
 */
public class Marcas extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");

        if ( req.getRequestURI().toLowerCase().endsWith("/listado_marcas") ) {
            System.out.println("LISTADO DE MARCAS " + this.getServletName());
            ArrayList<Marca> listado = Marca.todosMarcas();
            StringBuilder buffer = new StringBuilder("{\"Result\":\"OK\",\"Records\":[");
            int i = 0;
            for (Marca m : listado) {
                if (i != 0) buffer.append(",");
                buffer.append("{\"codigo\" : \"" + m.getCodigo() + "\"," +
                        "\"nombre\" : \"" + m.getNombre() + "\"}");
                i++;
            }
            buffer.append("]}");
            System.out.println(buffer);
            out.write(buffer.toString());
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/agregar_marca") ) {
            System.out.println("AGREGAR MARCA " + this.getServletName());

            // obteniendo datos del formulario
            String nombre = req.getParameter("nombre").trim();
            boolean estado = ( req.getParameter("estado").equals("on") );

            // creando marca
            Marca m = new Marca(nombre, estado);

            // dando de alta la marca
            TreeMap<Boolean, String> ret = Marca.agregarMarca(m);
            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se agrego correctamente
                System.out.println(msj + " " + m );
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + " \"}";
            } else {
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/eliminar_marca") ) {
            System.out.println("ELIMINAR MARCA " + this.getServletName());

            // obteniendo datos del formulario
            int codigo = Integer.parseInt(req.getParameter("codigo").trim());
            String nombre = req.getParameter("nombre").trim();
            boolean estado = ( req.getParameter("estado").equals("on") );

            // creando marca
            Marca m = new Marca(codigo, nombre, estado);

            // dando de baja la marca
            TreeMap<Boolean, String> ret = Marca.eliminarMarca(m);
            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se elimino correctamente
                System.out.println(msj + " " + m );
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + " \"}";
            } else {
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/modificar_marca") ) {
            System.out.println("MODIFICAR MARCA " + this.getServletName());

            // obteniendo datos del formulario
            int codigo = Integer.parseInt(req.getParameter("codigo").trim());
            String nombre = req.getParameter("nombre").trim();
            boolean estado = ( req.getParameter("estado").equals("on") );

            // creando rubro
            Marca m = new Marca(codigo, nombre, estado);

            // modificando la marca
            TreeMap<Boolean, String> ret = Marca.modificarMarca(m);
            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se modifico correctamente
                System.out.println(msj + " " + m );
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + " \"}";
            } else {
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }

        if ( req.getRequestURI().toLowerCase().endsWith("/info_marca") ) {
            System.out.println("INFO MARCA " + this.getServletName());

            String json;
            int cod  = Integer.parseInt(req.getParameter("marca_seleccionado"));
            Marca m = Marca.obtenerMarca(cod);
            if (m  == null) { System.err.println("no existe marca codigo " + cod); return; }
            json = "{\"codigo\":\"" + m.getCodigo() + " \", \"nombre\":\"" + m.getNombre() + "\", \"estado\":\"" +
                    m.getEstado() +  "\"  }";
            System.out.println(json);
            out.write(json);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");

        if ( req.getRequestURI().toLowerCase().endsWith("/buscar_marcas") ) {
            System.out.println("BUSQUEDA MARCAS " + this.getServletName());

            String nombre = req.getParameter("nombre").trim();
            int desde      = Integer.parseInt(req.getParameter("desde"));
            int hasta      = Integer.parseInt(req.getParameter("hasta"));
            ArrayList<Marca> listado = Marca.buscarMarcas(nombre, desde, hasta);

            System.out.println("NOMBRE :" + nombre + " DESDE : " + desde + " HASTA : " + hasta);
            int tamanio = (listado != null ? listado.size() : 0);
            StringBuilder buffer = new StringBuilder("{\"Result\":\"OK\", \"TotalRecordCount\":"  + tamanio +
                    ", \"Records\":[");
            if (listado == null)  buffer.append("{}");
            else {
                int i = 0;
                for (Marca m : listado) {
                    if (i != 0) buffer.append(",");
                    buffer.append("{\"codigo\" : \"" + m.getCodigo() + "\"," +
                            "\"nombre\" : \"" + m.getNombre() + "\"," +
                            "\"estado\" : \"" + m.getEstado() + "\"}");
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
