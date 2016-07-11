package com.me.webapp.conexion;

import com.me.webapp.modelo.Utileria;

import java.sql.*;

/**
 * Created by Spirok on 25/10/2015.
 * Clase Singleton de conexion a postgres
 */
public class ConexionPsql {

    // Singleton
    private static final ConexionPsql conPsql = new ConexionPsql();

    private Connection con;

    // constructor privado, no se puede realizar new ConexionPsql desde afuera de esta clase
    private ConexionPsql() {
        if (!setConexion())
            System.exit(0);
    }

    // metodo estatico que retorna la unica instancia de la clase
    public static ConexionPsql getInstance() { return conPsql; }

    public Connection getConexion() { return con; }

    /*
     * Metodo que setea conexion a postgres. Retorna true si esta ok, false caso contrario.
    */
    private boolean setConexion() {
        try {
            Class.forName(Utileria.getValueProperty("nombreDriverPsql"));
            String jdbc     = Utileria.getValueProperty("nombreJDBCPsql");
            String ip       = Utileria.getValueProperty("ipPsql");
            String puerto   = Utileria.getValueProperty("puertoPsql");
            String nombreBD = Utileria.getValueProperty("nombreBdPsql");
            String usuario  = Utileria.getValueProperty("usuarioPsql");
            String passwd   = Utileria.getValueProperty("passwdPsql");

            con = DriverManager.getConnection(jdbc + ip + ":" + puerto + "/" + nombreBD, usuario, passwd);

            //con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/TP_PP_2015", "postgres", "masterkey");
            System.out.println("Conexion postgres establecida");
            con.setAutoCommit(true);
            return true;
        } catch (SQLException sqe) {
            showSQLException(sqe);
            return false;
        } catch (ClassNotFoundException cnfe) {
            //showSQLException(cnfe);
            return false;
        }
    }

    /**
     * Metodo que ejecuta una query de accion y retorna true si fue ok, false caso contrario.
     */
    public boolean ejecutarQuery(String query) {
        Statement s;
        boolean ret = true;
        try {
            // creo statement
            s = con.createStatement();
            // creo resultset
            s.executeUpdate(query);
            //con.commit(); //commit seteado auto.
        } catch(SQLException e) {
            showSQLException(e);
            ret = false;
        }
        return ret;
    }

    /**
     * Metodo que ejecuta una consulta y devuelve dichos datos en un ResultSet
     */
    public ResultSet ejecutarConsulta(String query) {
        Statement s;
        ResultSet rs = null;
        try {
            // creo statement
            s = con.createStatement();
            // creo resultset
            rs = s.executeQuery(query);
        } catch (SQLException e) {
            showSQLException(e);
        }
        return rs;
    }


    /**
     * Metodo que imprime errores sqlEx.
     */
    public void showSQLException(SQLException e) {
        SQLException next = e;
        while (next != null) {
            System.out.println(next.getMessage());
            System.out.println("Error Code: " + next.getErrorCode());
            System.out.println("SQL State: " + next.getSQLState());
            next = next.getNextException();
        }
    }

    /**
     * Metodo que cierra la conexion con la base de datos
     */
    public void cerrarConexion() {
        if (con == null) return;
        try {
            con.close();
            System.out.println("Conexion postgres cerrada");
        } catch (SQLException e) {
            showSQLException(e);
        }

    }
}
