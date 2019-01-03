package com.unicauca.posgrados.controladores.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author sperez
 */
@javax.faces.bean.ManagedBean(name = "vistasEstudiante")
@SessionScoped
public class VistasEstudiante implements Serializable {

    private static String ruta;

    public static String getRuta() {
        return ruta;
    }

    public VistasEstudiante() {
        ruta = verVistaEstudiante();
    }

    public static String verVistaEstudiante() {
        ruta = "/PosgradosUnicauca/faces/usuariosdelsistema/estudiante/ListarPublicaciones.xhtml";
        return ruta;
    }

    public static String verDatosPersonales() {
        ruta = "/usuariosdelsistema/estudiante/VerEstudiante_Est.xhtml";
        return ruta;
    }

    public static String verPublicacion() {
        ruta = "/usuariosdelsistema/estudiante/VerPublicacion.xhtml";
        return ruta;
    }

    public static String registrarPublicacion() {
        ruta = "/usuariosdelsistema/estudiante/RegistrarPublicacion.xhtml";
        return ruta;
    }

    public static String verPublicaciones() {
        ruta = "/usuariosdelsistema/estudiante/ListarPublicaciones.xhtml";
        return ruta;
    }

}
