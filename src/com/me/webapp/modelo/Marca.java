package com.me.webapp.modelo;

import com.me.webapp.conexion.ConexionPsql;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Clase modelo que representa una marca
 * Created by Spirok on 17/11/2015.
 */
public class Marca {


    private int codigo;
    private String nombre;
    private boolean estado;

    private static final ConexionPsql con = ConexionPsql.getInstance();

    // Constructors
    public Marca() {
        this(0);
    }

    public Marca(int cod) {
        this(cod, "");
    }

    public Marca(int cod, String nom) {
        this(cod, nom, false);
    }

    public Marca(String nom, boolean est) {
        this(0, nom, est);
    }

    public Marca(int cod, String nom, boolean est) {
        setCodigo(cod);
        setNombre(nom);
        setEstado(est);
    }

    // Getters
    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean getEstado() {
        return estado;
    }

    // Setters
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // Customs

    @Override
    public String toString() {
        return getCodigo() + " " + getNombre() + " " + getEstado();
    }


    /**
     * Metodo estatico que agrega una marca al sistema. Retorna un TreeMap donde se indica el estado del insert
     * (true o false y un mensaje).
     *
     * @param m Marca a dar de alta
     * @return TreeMap que informa el estado del insert.
     */
    public static TreeMap<Boolean, String> agregarMarca(Marca m) {
        CallableStatement callFunction;
        TreeMap<Boolean, String> ret = new TreeMap<>();
        try {
            // llamando funcion psql
            callFunction = con.getConexion().prepareCall("{call abm_marca(?, ?, ?, ?)}");
            // seteando parametros de entrada
            callFunction.setInt(1, 0); // en insert no se toma en cuenta la id, se utiliza serial ai
            callFunction.setString(2, m.getNombre());
            callFunction.setBoolean(3, m.getEstado());
            callFunction.setString(4, "alta");
            // seteando parametros de salida
            callFunction.registerOutParameter(1, Types.INTEGER);
            callFunction.execute();
            // obteniendo resultado de retorno de la funcion
            //return callFunction.getInt(1);
            ret.put(true, "Marca Agregada");
        } catch (SQLException sqle) {
            //con.showSQLException(sqle);
            System.out.println("error retornado : " + sqle.getMessage());
            ret.put(false, sqle.getMessage());
        }
        return ret;
    }

    /**
     * Metodo estatico que elimina una marca al sistema. Retorna un TreeMap donde se indica el estado del delete
     * (true o false y un mensaje).
     *
     * @param m Marca a dar de baja
     * @return TreeMap que informa el estado del delete.
     */
    public static TreeMap<Boolean, String> eliminarMarca(Marca m) {
        CallableStatement callFunction;
        TreeMap<Boolean, String> ret = new TreeMap<>();
        try {
            // llamando funcion psql
            callFunction = con.getConexion().prepareCall("{call abm_marca(?, ?, ?, ?)}");
            // seteando parametros de entrada
            callFunction.setInt(1, m.getCodigo());
            callFunction.setString(2, m.getNombre());
            callFunction.setBoolean(3, m.getEstado());
            callFunction.setString(4, "baja");
            // seteando parametros de salida
            callFunction.registerOutParameter(1, Types.INTEGER);
            callFunction.execute();
            // obteniendo resultado de retorno de la funcion
            //return callFunction.getInt(1);
            ret.put(true, "Marca Eliminada");
        } catch (SQLException sqle) {
            //con.showSQLException(sqle);
            System.out.println("error retornado : " + sqle.getMessage());
            ret.put(false, sqle.getMessage());
        }
        return ret;
    }

    /**
     * Metodo estatico que modifica una marca del sistema. Retorna un TreeMap donde se indica el estado del update
     * (true o false y un mensaje).
     *
     * @param m Marca a modificar
     * @return TreeMap que informa el estado del update.
     */
    public static TreeMap<Boolean, String> modificarMarca(Marca m) {
        CallableStatement callFunction;
        TreeMap<Boolean, String> ret = new TreeMap<>();
        try {
            // llamando funcion psql
            callFunction = con.getConexion().prepareCall("{call abm_marca(?, ?, ?, ?)}");
            // seteando parametros de entrada
            callFunction.setInt(1, m.getCodigo());
            callFunction.setString(2, m.getNombre());
            callFunction.setBoolean(3, m.getEstado());
            callFunction.setString(4, "modificacion");
            // seteando parametros de salida
            callFunction.registerOutParameter(1, Types.INTEGER);
            callFunction.execute();
            // obteniendo resultado de retorno de la funcion
            //return callFunction.getInt(1);
            ret.put(true, "Marca Modificada");
        } catch (SQLException sqle) {
            //con.showSQLException(sqle);
            System.out.println("error retornado : " + sqle.getMessage());
            ret.put(false, sqle.getMessage());
        }
        return ret;
    }

    /**
     * Metodo estatico que retorna todos lss marcas existentes en un arraylist
     *
     * @return ArrayList<Marca> lista con las marcas existentes.
     */
    public static ArrayList<Marca> todosMarcas() {
        CallableStatement callFunction;
        ResultSet r;
        ArrayList<Marca> list = new ArrayList<>();
        try {
            callFunction = con.getConexion().prepareCall("{call listado_marcas()}");
            r = callFunction.executeQuery();
            while (r.next()) {
                list.add(new Marca(r.getInt("codigo"), r.getString("nombre"), r.getBoolean("estado")));
            }
            return list;
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return null;
        }
    }

    /**
     * Metodo estatico que recibe el codigo de un rubro y si existe retorna los datos del mismo
     *
     * @param cod de la Marca  a obtener
     * @return Rubro si existe, sino null.
     */
    public static Marca obtenerMarca(int cod) {
        CallableStatement callFunction;
        ResultSet r;
        Marca m;
        try {
            callFunction = con.getConexion().prepareCall("{call obtener_marca(?)}");
            callFunction.setInt(1, cod);
            r = callFunction.executeQuery();
            r.next();
            m = new Marca(r.getInt("cod_out"), r.getString("nombre"), r.getBoolean("estado"));
            return m;
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return null;
        }
    }

    /**
     * Metodo estatico que retorna todos las marcas existentes en un arraylist segun el nombre a buscar.
     * @return ArrayList<Marca> lista con las marcas existentes.
     */
    public static ArrayList<Marca> buscarMarcas(String nombre, int desde, int hasta) {
        CallableStatement callFunction;
        ResultSet r;
        ArrayList<Marca> list = new ArrayList<>();
        try {
            callFunction = con.getConexion().prepareCall("{call buscar_marcas(?, ?, ?)}");
            callFunction.setString(1, nombre);
            callFunction.setInt(2, desde);
            callFunction.setInt(3, hasta);
            r = callFunction.executeQuery();
            while (r.next()) {
                list.add(new Marca(r.getInt("codigo"), r.getString("nombre"), r.getBoolean("estado")));
            }
            return list;
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return null;
        }
    }

}