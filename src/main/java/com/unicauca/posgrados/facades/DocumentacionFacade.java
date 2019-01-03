/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.posgrados.facades;

import com.unicauca.posgrados.entidades.Documentacion;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author debian
 */
@Stateless
public class DocumentacionFacade extends AbstractFacade<Documentacion> {

    @PersistenceContext(unitName = "PosgradosPC")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocumentacionFacade() {
        super(Documentacion.class);
    }

    public List<Documentacion> buscarPorVisado(String visado) {
        Query query = em.createNamedQuery("Documentacion.findByDocVisado");
        query.setParameter("docVisado", visado);
        List<Documentacion> findUsuario = query.getResultList();
        return findUsuario;
    }
    public List<Documentacion> buscarPorEstudiante(Integer idEstudiante) {
        Query query = em.createNamedQuery("Documentacion.findByIdEstudiante");
        query.setParameter("estId", idEstudiante);
        List<Documentacion> documentos = query.getResultList();
        return documentos;
    }

    public int getnumFilasPubRev() {
        try {
            String queryStr;
            queryStr = "SELECT AUTO_INCREMENT FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'doctorado_2' AND TABLE_NAME = 'documentacion'";
            javax.persistence.Query query = getEntityManager().createNativeQuery(queryStr);
            List results = query.getResultList();
            int autoIncrement = ((BigInteger) results.get(0)).intValue();
            return autoIncrement;
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            System.out.println(e);
            return -1;
        }
    }

    public int CountByMonthYear(String anio, String mes) {

        String comSimple = "\'";
        String queryStr;
        queryStr = "SELECT COUNT(*) FROM documentacion WHERE YEAR(documentacion.doc_fecha_registro) =" + comSimple + anio + comSimple + "  AND Month(documentacion.doc_fecha_registro) = " + comSimple + mes + comSimple;

        javax.persistence.Query query = getEntityManager().createNativeQuery(queryStr);
        List results = query.getResultList();
        int numeroPub = ((Long) results.get(0)).intValue();
        return numeroPub;

    }

    public int CountByMonthYearVis(String anio, String mes) {

        String comSimple = "\'";
        String queryStr;
        queryStr = "SELECT COUNT(*) FROM documentacion WHERE YEAR(documentacion.doc_fecha_visado) =" + comSimple + anio + comSimple + "  AND Month(documentacion.doc_fecha_visado) = " + comSimple + mes + comSimple;

        javax.persistence.Query query = getEntityManager().createNativeQuery(queryStr);
        List results = query.getResultList();
        int numeroPub = ((Long) results.get(0)).intValue();
        return numeroPub;

    }
    
     /**
     * Funcion que me retorna la lista de publicaciones que ha registrado un 
     * estudiante en un determinado año. El listado esta ordenado por tipo de 
     * publicacion de forma descendente
     * @param estudianteId
     * @param anio
     * @return Lista publicaciones
     */
    public List<Documentacion> publicacionesEstudiantePorAnio(int estudianteId,int anio){
        javax.persistence.Query query = getEntityManager().createNamedQuery("Documentacion.StudentPublications_Year");
        query.setParameter("identificador", estudianteId);
        query.setParameter("anio", anio);
        try {
            System.out.println("Buscando publicaciones de estudiante por año");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            System.out.println(e);
            return null;
        }
    }
    
    
    /**
     * Funcion que me retorna la lista de publicaciones que ha registrado un
     * estudiante en un determinado semestre. El listado esta ordenado por tipo de 
     * publicacion de forma descendente
     * @param estudianteId
     * @param anio
     * @param semestre
     * @return Lista publicaciones
     */
    public List<Documentacion> publicacionesEstudiantePorSemestre(int estudianteId, int anio, int semestre){
        javax.persistence.Query query = getEntityManager().createNamedQuery("Documentacion.StudentPublications_Semester");
        query.setParameter("identificador", estudianteId);
        query.setParameter("anio", anio);
        if(semestre == 1){
            query.setParameter("inicio",1);
            query.setParameter("fin",6);
        }else{
            query.setParameter("inicio",7);
            query.setParameter("fin",12);
        }
        try {
            System.out.println("Buscando publicaciones de estudiante por semestre");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            System.out.println(e);
            return null;
        }
    }
    
    
    public List<Documentacion> listadoDocumentacionEst(Integer estudianteId) {
        javax.persistence.Query query = getEntityManager().createNamedQuery("Documentacion.findByIdEstudiante");
        query.setParameter("estId", estudianteId);
        try {
            System.out.println("Buscando publicaciones por estudiante");
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            System.out.println(e);
            return null;
        }
    }
    
    
    /**
     * Funcion para buscar todas las publicaciones que han sido registradas 
     * en un determinado año. El listado esta ordenado por tipo de publicacion
     * de forma descendente
     * @param anio
     * @return Lista de publicaciones
     */
    public List<Documentacion> publicacionesPorAnio(int anio){
        javax.persistence.Query query = getEntityManager().createNamedQuery("Documentacion.findAllByAnioFechaRegistro");
        query.setParameter("anio", anio);
        List<Documentacion> lista = null;
        try {
            System.out.println("Buscando publicaciones en el año "+anio);
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            System.out.println(e);
        }
        return lista;
    }
    
    /**
     * Funcion para buscar todas las publicaciones que han registradas 
     * en un determinado semestre. El listado esta ordenado por tipo de 
     * publicacion de forma descendente
     * @param anio
     * @param semestre
     * @return Lista de publicaciones
     */
    public List<Documentacion> publicacionesPorSemestre(int anio, int semestre){
        javax.persistence.Query query = getEntityManager().createNamedQuery("Documentacion.findAllBySemestre");
        query.setParameter("anio", anio);
        if(semestre == 1){
            query.setParameter("inicio",1);
            query.setParameter("fin",6);
        }else{
            query.setParameter("inicio",7);
            query.setParameter("fin",12);
        }
        List<Documentacion> lista = null;
        try {
            System.out.println("Buscando publicaciones en el semestre "+anio+"-"+semestre);
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            System.out.println(e);
        }
        return lista;
    }
    
}
