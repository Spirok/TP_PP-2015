package com.me.webapp.modelo;

import java.util.ArrayList;

/**
 * Pruebas varias...
 * Created by Spirok on 30/10/2015.
 */
public class PruebasTmp {

    public static void main(String[] args) {

        //Usuario u = new Usuario(1, "SpirokKK", "a", new Perfil(2));
        //Usuario.modificarUsuario(u);

        /*
        ArrayList<Empresa> empresas = Empresa.todosEmpresasMin();

        for (Empresa emp : empresas) {
            System.out.println(emp);
        }

        Empresa e = Empresa.obtenerEmpresa(1);

        System.out.println(e);


        ArrayList<Usuario> operDisp = Usuario.todosOperadoresDisponibles();
        for (Usuario u : operDisp) {
            System.out.println(u);
        }
        */

        /*
        ArrayList<Rubro> rubrosEmp = RubroEmpresa.todosRubrosEmpresa(24, 0, 3, "todos");

        System.out.println("Rubros asignados a la empresa 24 : ");
        for (Rubro r : rubrosEmp) {
            System.out.println(r);
        }
        */

        //Empresa e = Usuario.obtenerUsuarioOperador("operadorUno");
        //System.out.println(e);

        ArrayList<Empresa> empresas = Empresa.buscarEmpresas("b", 0 , 5);

        for (Empresa emp : empresas) {
            System.out.println(emp);
        }
    }
}
