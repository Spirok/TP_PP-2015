package com.me.webapp.modelo;

import com.me.webapp.conexion.ConexionPsql;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Clase modelo que representa una Empresa.
 * Created by Spirok on 1/11/2015.
 */
public class Empresa {

    private int codigo;
    private String nombre;
    private String direccion;
    private int codPostal;
    private String localidad;
    private String provincia;
    private String telefono;
    private String fax;
    private String web;
    private String email;
    private String responsable;
    private String emailResponsable;
    private String consultasA;
    private boolean estado;
    private Usuario usuario;

    private static final ConexionPsql con = ConexionPsql.getInstance();

    // Constructor

    public Empresa() { /* */ }

    public Empresa(int codigo) {
        this(codigo, "", "", 0, "", "", "", "", "", "", "", "", "", false, null);
    }

    public Empresa(int codigo, String nombre) {
        this(codigo, nombre, "", 0, "", "", "", "", "", "", "", "", "", false, null);
    }

    public Empresa(int codigo, String nombre, String email, Usuario usr) {
        this(codigo, nombre, "", 0, "", "", "", "", "", email, "", "", "", false, usr);
    }

    public Empresa(int codigo, String nombre, Usuario usr) {
        this(codigo, nombre, "", 0, "", "", "", "", "", "", "", "", "", false, usr);
    }

    public Empresa(String nombre, String dir, int cod_postal, String localidad, String provincia, String telefono,  String fax,
                   String web, String email, String responsable, String email_responsable, String consultas_a,
                   boolean estado, Usuario usr) {
        this(0, nombre, dir, cod_postal, localidad, provincia, telefono, fax, web, email, responsable, email_responsable,
                consultas_a, estado, usr);
    }

    public Empresa(int codigo, String nombre, String dir, int cod_postal, String localidad, String provincia, String telefono,
                   String fax, String web, String email, String responsable, String email_responsable,
                   String consultas_a, boolean estado, Usuario usr) {
        setCodigo(codigo);
        setNombre(nombre);
        setDireccion(dir);
        setCodPostal(cod_postal);
        setLocalidad(localidad);
        setProvincia(provincia);
        setTelefono(telefono);
        setFax(fax);
        setWeb(web);
        setEmail(email);
        setResponsable(responsable);
        setEmailResponsable(email_responsable);
        setConsultasA(consultas_a);
        setEstado(estado);
        setUsuario(usr);
    }

    // Getters
    public int getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public int getCodPostal() { return codPostal; }
    public String getLocalidad() { return localidad; }
    public String getProvincia() { return provincia; }
    public String getTelefono() { return telefono; }
    public String getFax() { return fax; }
    public String getWeb() { return web; }
    public String getEmail() { return email; }
    public String getResponsable() { return responsable; }
    public String getEmailResponsable() { return emailResponsable; }
    public String getConsultasA() { return consultasA; }
    public boolean getEstado() { return estado; }
    public Usuario getUsuario() { return  usuario; }

    // Setters
    public void setCodigo(int codigo) { this.codigo = codigo; }
    public void setDireccion(String direccion) { if (direccion == null) direccion = ""; this.direccion = direccion; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCodPostal(int cod_postal) { this.codPostal = cod_postal; }
    public void setLocalidad(String localidad) { if (localidad == null) localidad = ""; this.localidad = localidad; }
    public void setProvincia(String provincia) { if (provincia == null) provincia = ""; this.provincia = provincia; }
    public void setTelefono(String telefono) { if (telefono == null) telefono = ""; this.telefono = telefono; }
    public void setFax(String fax) { if (fax == null) fax = ""; this.fax = fax; }
    public void setWeb(String web) { if (web == null) web = ""; this.web = web; }
    public void setEmail(String email) { if (email == null) email = ""; this.email = email; }
    public void setResponsable(String responsable) { if (responsable == null) responsable = ""; this.responsable = responsable; }
    public void setEmailResponsable(String email_responsable) { if (email_responsable == null) email_responsable = ""; this.emailResponsable = email_responsable; }
    public void setConsultasA(String consultas_a) { if (consultas_a == null) consultas_a = ""; this.consultasA = consultas_a; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public void setUsuario(Usuario u) { usuario = u; }

    // customs

    public String toString() {
        return getCodigo() + " " + getNombre() + " " + getDireccion() + " " + getCodPostal() + " " + getLocalidad() + " " +
                getProvincia() + " " + getTelefono() + " " + getFax() + " " + getWeb() + " " + getEmail() + " " + getResponsable()
                + " " + getEmailResponsable() + " " + getConsultasA() + " " + getEstado() + " " + getUsuario();
    }


    /**
     * Metodo estatico que agrega una empresa al sistema. Retorna un TreeMap donde se indica el estado del insert
     * (true o false y un mensaje).
     * @param e Empresa  a dar de alta
     * @return TreeMap que informa el estado del insert.
     */
    public static TreeMap<Boolean, String> agregarEmpresa(Empresa e) {
        CallableStatement callFunction;
        TreeMap<Boolean, String> ret = new TreeMap<>();
        try {
            // llamando funcion psql
            callFunction = con.getConexion().prepareCall("{call abm_empresa(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            // seteando parametros de entrada
            callFunction.setInt(1, 0); // en insert no se toma en cuenta la id, se utiliza serial ai
            callFunction.setString(2,   e.getNombre());
            callFunction.setString(3,   e.getDireccion());
            callFunction.setInt(4,      e.getCodPostal());
            callFunction.setString(5,   e.getLocalidad());
            callFunction.setString(6,   e.getProvincia());
            callFunction.setString(7,   e.getTelefono());
            callFunction.setString(8,   e.getFax());
            callFunction.setString(9,   e.getWeb());
            callFunction.setString(10,  e.getEmail());
            callFunction.setString(11,  e.getResponsable());
            callFunction.setString(12,  e.getEmailResponsable());
            callFunction.setString(13,  e.getConsultasA());
            callFunction.setBoolean(14, e.getEstado());
            callFunction.setInt(15,     e.getUsuario().getId());
            callFunction.setString(16,  "alta");
            // seteando parametros de salida
            callFunction.registerOutParameter(1, Types.INTEGER);
            callFunction.execute();
            // obteniendo resultado de retorno de la funcion
            //return callFunction.getInt(1);
            ret.put(true, "Empresa Agregada");
        } catch (SQLException sqle) {
            //con.showSQLException(sqle);
            System.out.println("error retornado : " + sqle.getMessage());
            ret.put(false, sqle.getMessage());
        }
        return ret;
    }

    /**
     * Metodo estatico que elimina una empresa del sistema. Retorna un TreeMap donde se indica el estado del delete
     * (true o false y un mensaje).
     * @param e Empresa  a eliminar
     * @return TreeMap que informa el estado del delete.
     */
    public static TreeMap<Boolean, String> eliminarEmpresa(Empresa e) {
        CallableStatement callFunction;
        TreeMap<Boolean, String> ret = new TreeMap<>();
        try {
            // llamando funcion psql
            callFunction = con.getConexion().prepareCall("{call abm_empresa(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            // seteando parametros de entrada
            callFunction.setInt(1, e.getCodigo());
            callFunction.setString(2,   "");
            callFunction.setString(3,   "");
            callFunction.setInt(4,      0);
            callFunction.setString(5,   "");
            callFunction.setString(6,   "");
            callFunction.setString(7,   "");
            callFunction.setString(8,   "");
            callFunction.setString(9,   "");
            callFunction.setString(10,  "");
            callFunction.setString(11,  "");
            callFunction.setString(12,  "");
            callFunction.setString(13,  "");
            callFunction.setBoolean(14, false);
            callFunction.setInt(15,     0);
            callFunction.setString(16,  "baja");
            // seteando parametros de salida
            callFunction.registerOutParameter(1, Types.INTEGER);
            callFunction.execute();
            // obteniendo resultado de retorno de la funcion
            //return callFunction.getInt(1);
            ret.put(true, "Empresa Eliminada");
        } catch (SQLException sqle) {
            //con.showSQLException(sqle);
            System.out.println("error retornado : " + sqle.getMessage());
            ret.put(false, sqle.getMessage());
        }
        return ret;
    }


    /**
     * Metodo estatico que modifica una empresa del sistema. Retorna un TreeMap donde se indica el estado del upadte
     * (true o false y un mensaje).
     * @param e Empresa  a modificar
     * @return TreeMap que informa el estado del update.
     */
    public static TreeMap<Boolean, String> modificarEmpresa(Empresa e) {
        CallableStatement callFunction;
        TreeMap<Boolean, String> ret = new TreeMap<>();
        try {
            // llamando funcion psql
            callFunction = con.getConexion().prepareCall("{call abm_empresa(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            // seteando parametros de entrada
            callFunction.setInt(1,      e.getCodigo());
            callFunction.setString(2,   e.getNombre());
            callFunction.setString(3,   e.getDireccion());
            callFunction.setInt(4,      e.getCodPostal());
            callFunction.setString(5,   e.getLocalidad());
            callFunction.setString(6,   e.getProvincia());
            callFunction.setString(7,   e.getTelefono());
            callFunction.setString(8,   e.getFax());
            callFunction.setString(9,   e.getWeb());
            callFunction.setString(10,  e.getEmail());
            callFunction.setString(11,  e.getResponsable());
            callFunction.setString(12,  e.getEmailResponsable());
            callFunction.setString(13,  e.getConsultasA());
            callFunction.setBoolean(14, e.getEstado());
            callFunction.setInt(15,     e.getUsuario().getId());
            callFunction.setString(16,  "modificacion");
            // seteando parametros de salida
            callFunction.registerOutParameter(1, Types.INTEGER);
            callFunction.execute();
            // obteniendo resultado de retorno de la funcion
            //return callFunction.getInt(1);
            ret.put(true, "Empresa Modificada");
        } catch (SQLException sqle) {
            //con.showSQLException(sqle);
            System.out.println("error retornado : " + sqle.getMessage());
            ret.put(false, sqle.getMessage());
        }
        return ret;
    }

    /**
     * Metodo estatico que retorna todos las empresas existentes en un arraylist
     * @return ArrayList<Empresa> lista con las empresas existentes.
     */
    public static ArrayList<Empresa> todosEmpresas() {
        CallableStatement callFunction;
        ResultSet r;
        ArrayList<Empresa> list = new ArrayList<>();
        try {
            callFunction = con.getConexion().prepareCall("{call listado_empresas()}");
            r = callFunction.executeQuery();
            while (r.next()) {
                list.add( new Empresa(r.getInt("codigo"), r.getString("nombre"), r.getString("direccion"), r.getInt("cod_postal"),
                        r.getString("localidad"), r.getString("provincia"), r.getString("telefono"), r.getString("fax"),
                        r.getString("web"), r.getString("email"), r.getString("responsable"), r.getString("email_responsable"),
                        r.getString("consultas_a"), r.getBoolean("estado"), null ) ); // # no necesito los usuarios #
            }
            return list;
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return null;
        }
    }

    /**
     * Metodo estatico que retorna todos las empresas existentes en un arraylist segun el nombre indicado a buscar.
     * @return ArrayList<Empresa> lista con las empresas existentes.
     */
    public static ArrayList<Empresa> buscarEmpresas(String nombre, int desde, int hasta) {
        CallableStatement callFunction;
        ResultSet r;
        ArrayList<Empresa> list = new ArrayList<>();
        try {
            callFunction = con.getConexion().prepareCall("{call buscar_empresas(?, ?, ?)}");
            callFunction.setString(1, nombre);
            callFunction.setInt(2, desde);
            callFunction.setInt(3, hasta);
            r = callFunction.executeQuery();
            while (r.next()) {
                list.add( new Empresa(r.getInt("codigo"), r.getString("nombre"), r.getString("direccion"), r.getInt("cod_postal"),
                        r.getString("localidad"), r.getString("provincia"), r.getString("telefono"), r.getString("fax"),
                        r.getString("web"), r.getString("email"), r.getString("responsable"), r.getString("email_responsable"),
                        r.getString("consultas_a"), r.getBoolean("estado"), null ) ); // # no necesito los usuarios #
            }
            return list;
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return null;
        }
    }

    /**
     * Metodo estatico similar a todosEmpresas() solo que retorna informacion reducida (codigo y nombre).
     * @return ArrayList<Empresa> lista con las empresas existentes.
     */
    public static ArrayList<Empresa> todosEmpresasMin() {
        CallableStatement callFunction;
        ResultSet r;
        ArrayList<Empresa> list = new ArrayList<>();
        try {
            callFunction = con.getConexion().prepareCall("{call listado_empresas_min()}");
            r = callFunction.executeQuery();
            while (r.next()) {
                list.add( new Empresa(r.getInt("codigo"), r.getString("nombre")) );
            }
            return list;
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return null;
        }
    }

    /**
     * Metodo estatico que recibe la id de una empresa y retorna su informacion completa.
     * @param id_emp id de empresa a obtener
     * @return Empresa encontrada. Si no existe retorna null.
     */
    public static Empresa obtenerEmpresa(int id_emp) {
        CallableStatement callFunction;
        ResultSet r;
        Empresa emp;
        try {
            callFunction = con.getConexion().prepareCall("{call obtener_empresa(?)}");
            callFunction.setInt(1, id_emp);
            r = callFunction.executeQuery();
            r.next();
            emp = new Empresa(r.getInt("codigo"), r.getString("nombre"), r.getString("direccion"), r.getInt("cod_postal"),
                        r.getString("localidad"), r.getString("provincia"), r.getString("telefono"), r.getString("fax"),
                        r.getString("web"), r.getString("email"), r.getString("responsable"), r.getString("email_responsable"),
                        r.getString("consultas_a"), r.getBoolean("estado"), new Usuario(r.getInt("id_us"), r.getString("nick"), r.getString("passwd")) );
            return emp;
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return null;
        }
    }

    /**
     * Metodo estatico siminar a obtenerEmpresa solo que retorna informacion reducida(solo codigo y nombre) de la
     * empresa.
     * @return Empresa encontrada. Si no existe retorna null.
     */
    public static Empresa obtenerEmpresaMin(int id_emp) {
        CallableStatement callFunction;
        ResultSet r;
        Empresa emp;
        try {
            callFunction = con.getConexion().prepareCall("{call obtener_empresa_min(?)}");
            callFunction.setInt(1, id_emp);
            r = callFunction.executeQuery();
            r.next();
            emp = new Empresa(r.getInt("codigo"), r.getString("nombre") );
            return emp;
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return null;
        }
    }
}
