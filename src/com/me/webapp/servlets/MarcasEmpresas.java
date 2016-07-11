package com.me.webapp.servlets;

import com.me.webapp.modelo.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Servlet que maneja tod o lo relacionado a marcasempresas
 * Created by spirok on 29/11/15.
 */
public class MarcasEmpresas extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");

        if ( req.getRequestURI().toLowerCase().endsWith("/marcas_de_empresa") ) {
            int codEmpresa = Integer.parseInt(req.getParameter("codigo_empresa").trim());
            int desde      = Integer.parseInt(req.getParameter("desde"));
            int hasta      = Integer.parseInt(req.getParameter("hasta"));
            String accion    = req.getParameter("accion");

            // accion es : 'todos', 'seleccionados', 'no_seleccionados'

            //System.out.println("empresa : " + codEmpresa + " desde : " + desde + " hasta : " + hasta);

            System.out.println("LISTADO DE MARCAS ASIGNADAS A EMPRESA " + codEmpresa + " tipo : " + accion + " "
                                    + this.getServletName());
            ArrayList<Marca> listado = MarcaEmpresa.todosMarcasEmpresa(codEmpresa, desde, hasta, accion);
            int tamanio = (listado != null ? listado.size() : 0);
            StringBuilder buffer = new StringBuilder("{\"Result\":\"OK\", \"TotalRecordCount\": " + tamanio +
                    ", \"Records\":[");
            int i = 0;
            for (Marca m : listado) {
                if (i != 0) buffer.append(",");
                buffer.append("{\"codigo\" : \"" + m.getCodigo() + "\"," +
                             "\"nombre\" : \"" + m.getNombre() + "\"," +
                            "\"estado\" : " + m.getEstado() + "}");
                i++;
            }
            buffer.append("]}");
            out.write(buffer.toString());

            System.out.println(buffer.toString());
        }

        if (req.getRequestURI().toLowerCase().endsWith("/agregar_marca_empresa")) {
            System.out.println("AGREGA MARCA_EMPRESA");
            int codEmpresa = Integer.parseInt(req.getParameter("codigo_empresa"));
            int codMarca   = Integer.parseInt(req.getParameter("codigo_marca"));

            MarcaEmpresa me = new MarcaEmpresa(new Marca(codMarca), new Empresa(codEmpresa));
            TreeMap<Boolean, String> ret = MarcaEmpresa.accionMarcaEmpresa(me, "agregar");

            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se agrego correctamente
                System.out.println(msj + " " + me );
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + " \"}";
            } else {
                msj = msj.replace("\"", "'");
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }

        if (req.getRequestURI().toLowerCase().endsWith("/quitar_marca_empresa")) {
            System.out.println("QUITAR MARCA_EMPRESA");
            int codEmpresa = Integer.parseInt(req.getParameter("codigo_empresa"));
            int codMarca   = Integer.parseInt(req.getParameter("codigo_marca"));

            MarcaEmpresa me = new MarcaEmpresa(new Marca(codMarca), new Empresa(codEmpresa));
            TreeMap<Boolean, String> ret = MarcaEmpresa.accionMarcaEmpresa(me, "quitar");

            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se quito correctamente
                System.out.println(msj + " " + me );
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + " \"}";
            } else {
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }
        out.close();
    }
}
