package com.unicauca.posgrados.controladores.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
//import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author sperez
 */
@ManagedBean(name = "vistasCoordinadorController")
@SessionScoped
public class VistasCoordinador implements Serializable {

    private static String ruta;

//    public String getRuta() {
//        return this.ruta;
//    }
    public VistasCoordinador() {
        ruta = getVistasCoordinador();
    }

    public static String getVistasCoordinador() {
        ruta = "/PosgradosUnicauca/faces/usuariosdelsistema/coordinador/estudiantes/ListarEstudiantes.xhtml";
        return ruta;
    }

    public static String getIndex() {
        ruta = "/faces/index.xhtml";
        return ruta;
    }

    public static String verEstudiante() {
        ruta = "VerEstudiante.xhtml";
        return ruta;
    }

    public static String editarEstudiante() {
        ruta = "EditarEstudiante.xhtml";
        return ruta;
    }

    public static String listarEstudiantes() {
        ruta = "/usuariosdelsistema/coordinador/estudiantes/ListarEstudiantes.xhtml";
        return ruta;
    }

    public static String registrarEstudiante() {
        ruta = "/usuariosdelsistema/coordinador/estudiantes/RegistrarEstudiante.xhtml";
        return ruta;
    }

    public static String verGraficaPubReg() {
        ruta = "/usuariosdelsistema/coordinador/graficaPub/GraficaPubReg.xhtml";
        return ruta;
    }

    public static String verGraficaPubVis() {
        ruta = "/usuariosdelsistema/coordinador/graficaPub/GraficaPubVis.xhtml";
        return ruta;
    }

    public static String listarPublicaciones() {
        ruta = "/usuariosdelsistema/coordinador/listarPub/ListarPublicaciones_Coord.xhtml";
        return ruta;
    }

    public static String listarPublicacionesEspera() {
        ruta = "/usuariosdelsistema/coordinador/listarPub/ListarPublicaciones_Espera.xhtml";
        return ruta;
    }

    public static String listarPublicacionesRevisdas() {
        ruta = "/usuariosdelsistema/coordinador/listarPub/ListarPublicaciones_Rev.xhtml";
        return ruta;
    }

    public static String verPublicacionEstudiante() {
        ruta = "/usuariosdelsistema/coordinador/listarPub/VerPublicacion_Coord.xhtml";
        return ruta;
    }

}
