package com.me.webapp.servlets;

import com.me.webapp.modelo.Empresa;
import com.me.webapp.modelo.Rubro;
import com.me.webapp.modelo.RubroEmpresa;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Servlet que maneja tod o lo relacionado a rubrosempresas
 * Created by spirok on 29/11/15.
 */
public class RubrosEmpresas extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");

        if ( req.getRequestURI().toLowerCase().endsWith("/rubros_de_empresa") ) {
            int codEmpresa = Integer.parseInt(req.getParameter("codigo_empresa").trim());
            int desde      = Integer.parseInt(req.getParameter("desde"));
            int hasta      = Integer.parseInt(req.getParameter("hasta"));
            String accion    = req.getParameter("accion");

            //System.out.println("empresa : " + codEmpresa + " desde : " + desde + " hasta : " + hasta);

            System.out.println("LISTADO DE RUBROS ASIGNADOS A EMPRESA " + codEmpresa + " tipo : " + accion + " "
                                    + this.getServletName());
            ArrayList<Rubro> listado = RubroEmpresa.todosRubrosEmpresa(codEmpresa, desde, hasta, accion);
            int tamanio = (listado != null ? listado.size() : 0);
            StringBuilder buffer = new StringBuilder("{\"Result\":\"OK\", \"TotalRecordCount\": " + tamanio +
                    ", \"Records\":[");

                int i = 0;
                for (Rubro r : listado) {
                    if (i != 0) buffer.append(",");
                    buffer.append("{\"codigo\" : \"" + r.getCodigo() + "\"," +
                            "\"nombre\" : \"" + r.getNombre() + "\"," +
                            "\"estado\" : " + r.getEstado() + "}");
                    i++;
                }

            buffer.append("]}");
            out.write(buffer.toString());

            System.out.println(buffer.toString());
        }

        if (req.getRequestURI().toLowerCase().endsWith("/agregar_rubro_empresa")) {
            System.out.println("AGREGA RUBRO_EMPRESA");
            int codEmpresa = Integer.parseInt(req.getParameter("codigo_empresa"));
            int codRubro   = Integer.parseInt(req.getParameter("codigo_rubro"));

            RubroEmpresa re = new RubroEmpresa(new Rubro(codRubro), new Empresa(codEmpresa));
            TreeMap<Boolean, String> ret = RubroEmpresa.accionRubroEmpresa(re, "agregar");

            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se agrego correctamente
                System.out.println(msj + " " + re );
                json = "{\"resultado\":\"ok\",\"mensaje\":\"" + msj + " \"}";
            } else {
                msj = msj.replace("\"", "'");
                System.out.println(" msj : " + msj);
                json = "{\"resultado\":\"error\",\"mensaje\":\"" + msj + " \"}";
            }
            out.write(json);
        }

        if (req.getRequestURI().toLowerCase().endsWith("/quitar_rubro_empresa")) {
            System.out.println("QUITAR RUBRO_EMPRESA");
            int codEmpresa = Integer.parseInt(req.getParameter("codigo_empresa"));
            int codRubro   = Integer.parseInt(req.getParameter("codigo_rubro"));

            RubroEmpresa re = new RubroEmpresa(new Rubro(codRubro), new Empresa(codEmpresa));
            TreeMap<Boolean, String> ret = RubroEmpresa.accionRubroEmpresa(re, "quitar");

            String msj = ret.firstEntry().getValue();
            String json;
            if (ret.firstEntry().getKey() ) { // si se quito correctamente
                System.out.println(msj + " " + re );
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
