/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.posgrados.facades;

import com.unicauca.posgrados.entidades.Estudiante;
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
public class EstudianteFacade extends AbstractFacade<Estudiante> {

    @PersistenceContext(unitName = "PosgradosPC")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstudianteFacade() {
        super(Estudiante.class);
    }

    public Estudiante buscarPorCodigoExceptoConId(String codigo, Integer id) {
        Query query = em.createNamedQuery("Estudiante.buscarPorCodigoExceptoConId");
        query.setParameter("estCodigo", codigo);
        query.setParameter("estId", id);

        List<Estudiante> resultList = query.getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;

    }

    public Estudiante buscarPorCedulaExceptoConId(Integer cedula, Integer id) {
        Query query = em.createNamedQuery("Estudiante.buscarPorCedulaExceptoConId");
        query.setParameter("estCedula", cedula);
        query.setParameter("estId", id);

        List<Estudiante> resultList = query.getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;

    }
//    public void edit(Estudiante entity, String correoAntiguo) {
//        getEntityManager().createNativeQuery("update grupo_tipo_usuario set correo=? where correo=?").setParameter(1, entity.getCorreo().getCorreo()).setParameter(2, correoAntiguo);
//        super.edit(entity); //To change body of generated methods, choose Tools | Templates.
//        
//    }
     
}
