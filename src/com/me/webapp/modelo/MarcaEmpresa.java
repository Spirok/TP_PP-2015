package com.me.webapp.modelo;

import com.me.webapp.conexion.ConexionPsql;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Clase modelo que representa una relacion de marca y empresa
 * Created by Spirok on 27/11/2015.
 */
public class MarcaEmpresa {

    private Marca marca;
    private Empresa empresa;

    private static final ConexionPsql con = ConexionPsql.getInstance();

    // Constructors
    public MarcaEmpresa() {
        this(null, null);
    }

    public MarcaEmpresa(Marca m, Empresa e) {
        setMarca(m);
        setEmpresa(e);
    }

    // Getters
    public Marca getMarca() { return marca; }
    public Empresa getEmpresa() { return empresa; }

    // Setters
    public void setMarca(Marca marca) { this.marca = marca; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    // Customs
    @Override
    public String toString()  { return getMarca() + " " + getEmpresa(); }


    /**
     * Metodo estatico que retorna todos las marcas asignados a la empresa indicada por parametro.
     * @param cod_emp codigo de empresa
     * @param desde inicio indice del grupo de rubros
     * @param hasta fin indice del grupo de rubros
     * @param tipo define que tipo de seleccion se quiere ('todos', 'seleccionados', 'no_seleccionados')
     * @return ArrayList<Marca> con todos las marcas encontradss.
     */
    public static ArrayList<Marca> todosMarcasEmpresa(int cod_emp, int desde, int hasta, String tipo) {
        CallableStatement callFunction;
        ResultSet r;
        ArrayList<Marca> list = new ArrayList<>();
        try {
            // preparando funcion psql
            callFunction = con.getConexion().prepareCall("{call listado_marca_empresa(?, ?, ?, ?)}");
            // seteando parametros de entrada
            callFunction.setInt(1, cod_emp);
            callFunction.setInt(2, desde);
            callFunction.setInt(3, hasta);
            callFunction.setString(4, tipo);
            // ejecutando funcion
            r = callFunction.executeQuery();
            // recorriendo los rubros obtenidos
            while (r.next()) {
                list.add(new Marca(r.getInt("codigo"), r.getString("nombre"), r.getBoolean("estado") ) );
            }
            return list;
        } catch (SQLException sqle) {
            con.showSQLException(sqle);
            return null;
        }
    }

    /**
     * Metodo estatico que se encarga de realizar un insert, delete a la relacion MarcaEmpresa indicada.
     * @param re relacion MarcaEmpresa a ser procesada.
     * @param accion representa la accion a ejecutar ('agregar', 'quitar')
     * @return TreeMap<Boolean.String> informa el estado de la operacion, con un boolean y su correspondiente mensaje.
     */
    public static TreeMap<Boolean, String> accionMarcaEmpresa(MarcaEmpresa re, String accion) {
        CallableStatement callFunction;
        TreeMap<Boolean, String> ret = new TreeMap<>();
        try {
            // preparando funcion psql
            callFunction = con.getConexion().prepareCall("{call acciones_marca_empresa(?, ?, ?)}");
            // seteando parametros de entrada
            callFunction.setInt(1, re.getMarca().getCodigo());
            callFunction.setInt(2, re.getEmpresa().getCodigo());
            callFunction.setString(3, accion);
            // seteando parametros de salida
            callFunction.registerOutParameter(1, Types.INTEGER);
            // ejecutando funcion
            callFunction.execute();
            ret.put(true, accion + " OK");
        } catch (SQLException sqle) {
            //con.showSQLException(sqle);
            System.out.println("error retornado : " + sqle.getMessage());
            ret.put(false, sqle.getMessage());
        }
        return ret;
    }
}
