package com.me.webapp.modelo;

import com.me.webapp.conexion.ConexionPsql;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Clase modelo que representa un usuario. Tiene metodos especiales como validarUsuario, todosUsuarios, obtenerUsuario, etc.
 * Created by Spirok on 26/10/2015.
 */
public class Usuario {

    private int id;
    private String nombre;
    private String passwd;
    private Perfil perfil;

    private static final ConexionPsql con = ConexionPsql.getInstance();

    // Constructor
    public Usuario() {
        this(0);
    }
    public Usuario(int id) {
        this(id, "");
    }
    public Usuario(int id, String nombre) {
        this(id, nombre, "");
    }
    public Usuario(int id, String nombre, String pwd) {
        this(id, nombre, pwd, new Perfil());
    }


    public Usuario(String nombre, String pwd) {
        this(0, nombre, pwd, new Perfil());
    }

    public Usuario(String nombre, String pwd, Perfil p) {
        this(0, nombre, pwd, p);
    }

    public Usuario(int id, String nombre, String pwd, Perfil p) {
        setId(id);
        setNombre(nombre);
        setPasswd(pwd);
        setPerfil(p);
    }

    // Getters
    public int getId() { return this.id; }
    public String getNombre() { return this.nombre; }
    public String getPasswd()  { return this.passwd; }
    public Perfil getPerfil() { return this.perfil; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPasswd(String passwd) { this.passwd = passwd; }
    public void setPerfil(Perfil p) { this.perfil = p; }

    // customs

    @Override
    public String toString() { return getId() + " " + getNombre() + " " + getPasswd() + " " + getPerfil(); }


    /**
     * Metodo estatico que agrega un usuario al sistema. Retorna un TreeMap donde se indica el estado del insert
     * (true o false y un mensaje).
     * @param u Usuario a dar de alta
     * @return TreeMap que informa el estado del insert.
     */
    public static TreeMap<Boolean, String> agregarUsuario(Usuario u) {
        CallableStatement callFunction;
        TreeMap<Boolean, String> ret = new TreeMap<>();
        try {
            // llamando funcion psql
            callFunction = con.getConexion().prepareCall("{call abm_usuario(?, ?, ?, ?, ?)}");
            // seteando parametros de entrada
            callFunction.setInt(1, 0); // en insert no se toma en cuenta la id, se utiliza serial ai
            callFunction.setString(2, u.getNombre());
            callFunction.setString(3, u.getPasswd());
            callFunction.setInt(4, u.getPerfil().getId());
            callFunction.setString(5, "alta");
            // seteando parametros de salida
            callFunction.registerOutParameter(1, Types.INTEGER);
            callFunction.execute();
            // obteniendo resultado de retorno de la funcion
            //return callFunction.getInt(1);
            ret.put(true, "Usuario Agregado");
        } catch (SQLException sqle) {
            //con.showSQLException(sqle);
            System.out.println("error retornado : " + sqle.getMessage());
            ret.put(false, sqle.getMessage());
        }
        return ret;
    }

    /**
     * Metodo estatico que elimina un usuario del sistema. Retorna un TreeMap donde se indica el estado del delete
     * (true o false y un mensaje).
     * @param u Usuario a dar de baja
     * @return TreeMap que informa el estado del delete.
     */
    public static TreeMap<Boolean, String> eliminarUsuario(Usuario u) {
        CallableStatement callFunction;
        TreeMap<Boolean, String> ret = new TreeMap<>();
        try {
            // llamando funcion psql
            callFunction = con.getConexion().prepareCall("{call abm_usuario(?, ?, ?, ?, ?)}");
            // seteando parametros de entrada
            callFunction.setInt(1, u.getId());
            callFunction.setString(2, u.getNombre());
            callFunction.setString(3, u.getPasswd());
            callFunction.setInt(4, u.getPerfil().getId());
            callFunction.setString(5, "baja");
            // seteando parametros de salida
            callFunction.registerOutParameter(1, Types.INTEGER);
            callFunction.execute();
            // obteniendo resultado de retorno de la funcion
            //return callFunction.getInt(1);
            ret.put(true, "Usuario Eliminado");
        } catch (SQLException sqle) {
            //con.showSQLException(sqle);
            System.out.println("error retornado : " + sqle.getMessage());
            ret.put(false, sqle.getMessage());
        }
        return ret;
    }

    /**
     * Metodo estatico que modifica un usuario del sistema. Retorna un TreeMap donde se indica el estado del update
     * (true o false y un mensaje).
     * @param u Usuario a modificar
     * @return TreeMap que informa el estado del update.
     */
    public static TreeMap<Boolean, String> modificarUsuario(Usuario u) {
        CallableStatement callFunction;
        TreeMap<Boolean, String> ret = new TreeMap<>();
        try {
            // llamando funcion psql
            callFunction = con.getConexion().prepareCall("{call abm_usuario(?, ?, ?, ?, ?)}");
            // seteando parametros de entrada
            callFunction.setInt(1, u.getId());
            callFunction.setString(2, u.getNombre());
            callFunction.setString(3, u.getPasswd());
            callFunction.setInt(4, u.getPerfil().getId());
            callFunction.setString(5, "modificacion");
            // seteando parametros de salida
            callFunction.registerOutParameter(1, Types.INTEGER);
            callFunction.execute();
            // obteniendo resultado de retorno de la funcion
            //return callFunction.getInt(1);
            ret.put(true, "Usuario Modificado");
        } catch (SQLException sqle) {
            //con.showSQLException(sqle);
            System.out.println("error retornado : " + sqle.getMessage());
            ret.put(false, sqle.getMessage());
        }
        return ret;
    }

    /**
     * Metodo estatico que apartir de un usuario recibido por parametro valida si existe en la bbdd, retornando valor
     * booleano.
     * @param u Usuario a validar
     * @return boolean true si existe, false contrario.
     */
    public static boolean validarUsuario(Usuario u) {
        CallableStatement callFunction;
        try {
            // preparando llamada a funcion psql
            callFunction = con.getConexion().prepareCall("{call validar_usuario(?, ?)}");
            // seteando parametros de entrada
            callFunction.setString(1, u.getNombre());
            callFunction.setString(2, u.getPasswd());
            // seteando parametros de salida
            callFunction.registerOutParameter(1, Types.BOOLEAN);
            callFunction.execute();
            // obteniendo resultado de retorno de la funcion
            return callFunction.getBoolean(1);
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return false;
        }
    }

    /**
     * Metodo estatico que retorna todos los usuarios existentes en un arraylist
     * @return ArrayList<Usuario> lista con los usuarios existentes.
     */
    public static ArrayList<Usuario> todosUsuarios() {
        CallableStatement callFunction;
        ResultSet r;
        ArrayList<Usuario> list = new ArrayList<>();
        try {
            callFunction = con.getConexion().prepareCall("{call listado_usuarios()}");
            r = callFunction.executeQuery();
            while (r.next()) {
                list.add(new Usuario(r.getInt("id"), r.getString("nick"), r.getString("password"),
                                                        new Perfil(r.getInt("per_id"), r.getString("per_desc"))) );
            }
            return list;
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return null;
        }
    }

    /**
     * Metodo estatico que retorna todos los usuarios operadores que no estan asignados a una empresa en un arraylist
     * @return ArrayList<Usuario> lista con los usuarios existentes.
     */
    public static ArrayList<Usuario> todosOperadoresDisponibles() {
        CallableStatement callFunction;
        ResultSet r;
        ArrayList<Usuario> list = new ArrayList<>();
        try {
            callFunction = con.getConexion().prepareCall("{call operadores_disponibles()}");
            r = callFunction.executeQuery();
            while (r.next()) {
                list.add(new Usuario(r.getInt("id_usr"), r.getString("nick"), r.getString("passwd"), null ) );
            }
            return list;
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return null;
        }
    }

    /**
     * Metodo estatico que recibe el nombre de un usuario y si existe retorna los datos del mismo
     * @param nombre del usuario a obtener
     * @return Usuario si existe, sino null.
     */
    public static Usuario obtenerUsuario(String nombre) {
        CallableStatement callFunction;
        ResultSet r;
        try {
            callFunction = con.getConexion().prepareCall("{call obtener_usuario(?)}");
            callFunction.setString(1, nombre);
            r = callFunction.executeQuery();
            r.next();
            return new Usuario(r.getInt("id"), r.getString("nick"), r.getString("password"),
                                                 new Perfil(r.getInt("per_id"), r.getString("per_desc") ) );
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return null;
        }
    }

    /**
     * Metodo estatico que retorna empresa con su usuario operador asignado.
     * @param nombreUsuario nombre del usuario
     * @return Empresa empresa con usuario operador asignado.
     */
    public static Empresa obtenerUsuarioOperador(String nombreUsuario) {
        CallableStatement callFunction;
        ResultSet r;
        try {
            callFunction = con.getConexion().prepareCall("{call obtener_usuario_operador(?)}");
            callFunction.setString(1, nombreUsuario);
            r = callFunction.executeQuery();
            if (r.next())
                return new Empresa(r.getInt("codigo_empresa"), r.getString("nombre_empresa"), r.getString("email_empresa"),
                                new Usuario(r.getInt("id"), r.getString("nick") ) );
            else return null;
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return null;
        }
    }
}
