package com.me.webapp.modelo;

import com.me.webapp.conexion.ConexionPsql;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase modelo que representa un perfil de un usuario.
 * Created by Spirok on 29/10/2015.
 */
public class Perfil {

    private int id;
    private String descripcion;

    private static final ConexionPsql con = ConexionPsql.getInstance();

    // constructores
    public Perfil() { this(0); }
    public Perfil(int id) { this(id, ""); }
    public Perfil(int id, String desc) {
        setId(id);
        setDescripcion(desc);
    }

    // Getters
    public int getId() { return this.id; }
    public String getDescripcion() { return this.descripcion; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setDescripcion(String desc) { this.descripcion = desc; }

    // Customs

    public String toString() { return getId() + " " + getDescripcion(); }

    /**
     * Metodo estatico que retorna un ArrayList con todos los perfiles existentes en el sistema.
     * @return ArrayList de perfiles.
     */
    public static ArrayList<Perfil> todosPerfiles() {
        CallableStatement callFunction;
        ResultSet r;
        ArrayList<Perfil> lista = new ArrayList<>();
        try {
            callFunction = con.getConexion().prepareCall("{call listado_perfiles()}");
            r = callFunction.executeQuery();
            while (r.next()) {
                lista.add(new Perfil(r.getInt("id"), r.getString("descripcion")) );
            }
            return lista;
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return null;
        }
    }
}
