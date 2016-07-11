package com.me.webapp.modelo;

/**
 * Clase que es utilizada para ser almacenada como un atributo de un HttpSession
 * Created by Spirok on 9/11/2015.
 */
public class UsuarioSesion {

    private int id;
    private String nombre;
    private int idPerfil;
    private String nombrePerfil;

    private int codigoEmpresa;
    private String nombreEmpresa;
    private String emailEmpresa;

    // constructor

    public UsuarioSesion(int id, String nombre, int idP, String nombrePerfil) {
        this(id, nombre, idP, nombrePerfil, 0);
    }

    public UsuarioSesion(int id, String nombre, int idP, String nombrePerfil, int codEmp) {
        this(id, nombre, idP, nombrePerfil, codEmp, "", "");
    }

    public UsuarioSesion(int id, String nombre, int idP, String nombrePerfil, int codEmp, String nombreEmp, String eml) {
        setId(id);
        setNombre(nombre);
        setIdPerfil(idP);
        setNombrePerfil(nombrePerfil);
        setCodigoEmpresa(codEmp);
        setNombreEmpresa(nombreEmp);
        setEmailEmpresa(eml);
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getIdPerfil() { return idPerfil; }
    public String getNombrePerfil() { return nombrePerfil; }
    public int getCodigoEmpresa() { return codigoEmpresa; }
    public String getNombreEmpresa() { return nombreEmpresa; }
    public String getEmailEmpresa() { return emailEmpresa; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setIdPerfil(int idPerfil) { this.idPerfil = idPerfil; }
    public void setNombrePerfil(String nombrePerfil) { this.nombrePerfil = nombrePerfil; }
    public void setCodigoEmpresa(int codigoEmpresa) { this.codigoEmpresa = codigoEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }
    public void setEmailEmpresa(String emailEmpresa) { this.emailEmpresa = emailEmpresa; }

    @Override
    public String toString() { return getId() + " " + getNombre() + " " + getNombrePerfil()  + " " + getCodigoEmpresa()
        + " " + getNombreEmpresa() + " " + getEmailEmpresa(); }
}
