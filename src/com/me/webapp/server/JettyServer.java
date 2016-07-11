package com.me.webapp.server;

import com.me.webapp.conexion.ConexionPsql;
import com.me.webapp.modelo.Utileria;
import com.me.webapp.servlets.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.servlet.*;


/**
 * Clase de jetty embebido
 * Created by Spirok on 26/10/2015.
 */
public class JettyServer {

    private Server server;
    private ServerConnector http;
    private ServletContextHandler context;

    private ConexionPsql conexion;

    public JettyServer() {
        System.out.println("Configuro server Jetty...");
        server = new Server();
        http = new ServerConnector(server);

        http.setHost(Utileria.getValueProperty("ipJetty"));
        http.setPort(Integer.parseInt(Utileria.getValueProperty("puertoJetty")));
        http.setIdleTimeout(30000);
        server.addConnector(http);

        // resource handle, para manejo de contenido html estatico
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);

        resource_handler.setWelcomeFiles(new String[]{"./docs/login.html"});
        resource_handler.setResourceBase("./src/com/me/webapp");

        context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.setResourceBase("./webapp/servlets"); // seteando path donde se encuentran los servlets
        context.setClassLoader(Thread.currentThread().getContextClassLoader());

        // asocio servlets - uri
        context.addServlet(Prueba.class, "/pruebaServlet");
        context.addServlet(Loguear.class, "/Loguear");
        context.addServlet(Desloguear.class, "/Desloguear");
        context.addServlet(PlantillaHtml.class, "/PlantillaHtml");
        context.addServlet(Usuarios.class, "/listado_usuarios,/operadores_disponibles,/agregar_usuario,/eliminar_usuario,/modificar_usuario,/listado_perfiles,/info_usuario,/info_usuario_operador");
        context.addServlet(Empresas.class, "/listado_empresas,/listado_empresas_min,/info_empresa,/agregar_empresa,/eliminar_empresa,/modificar_empresa,/buscar_empresas");
        context.addServlet(Rubros.class, "/listado_rubros,/agregar_rubro,/eliminar_rubro,/modificar_rubro,/info_rubro,/buscar_rubros");
        context.addServlet(Marcas.class, "/listado_marcas,/agregar_marca,/eliminar_marca,/modificar_marca,/info_marca,/buscar_marcas");
        context.addServlet(RubrosEmpresas.class, "/rubros_de_empresa,/agregar_rubro_empresa,/quitar_rubro_empresa");
        context.addServlet(MarcasEmpresas.class, "/marcas_de_empresa,/agregar_marca_empresa,/quitar_marca_empresa");
        context.addServlet(Mail.class, "/envio_mail");
        context.addServlet(DatosSesion.class, "/DatosSesion");

        // asocio lista de handlers al servidor (file server + servlets)
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { context, resource_handler, new DefaultHandler() });
        server.setHandler(handlers);

        // obtengo conexion postgres
        conexion =  ConexionPsql.getInstance();
        try {
            server.start();
            System.out.println("Servidor Started! version " + Server.getVersion() + " IP : " + http.getHost() +
                                                                    " en puerto "+http.getPort());
            System.out.println("Presione INTRO para terminar!");
            System.in.read();
            System.out.println("Detengo Servidor Jetty Embebido!");
            server.stop();
            conexion.cerrarConexion();
            System.out.println("Termino Aplicacion!");
            System.exit(0);
        } catch(Exception ex) {
            System.err.println("Error Starting Server ..."+ex.getMessage());
        }
    }
    public static void main(String[] arg) {
        new JettyServer();
    }
}
