/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.posgrados.facades;

import com.unicauca.posgrados.entidades.Usuario;
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
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "PosgradosPC") 
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public Usuario buscarPorCorreo(String correo) {
        Query query = em.createNamedQuery("Usuario.findByCorreo");
        query.setParameter("correo", correo);
        List<Usuario> findUsuario = query.getResultList();
        if (findUsuario != null && !findUsuario.isEmpty()) {
            return findUsuario.get(0);
        }
        return null;
    }
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        Query query = em.createNamedQuery("Usuario.findByNombreUsuario");
        query.setParameter("nombreUsuario", nombreUsuario);
        List<Usuario> findUsuario = query.getResultList();
        if (findUsuario != null && !findUsuario.isEmpty()) {
            return findUsuario.get(0);
        }
        return null;
    }

    public Usuario buscarPorCorreoExceptoConId(String correo, Integer id) {
        Query query = em.createNamedQuery("Usuario.buscarPorCorreoExceptoConId");
        query.setParameter("correo", correo);
        query.setParameter("usuId", id);

        List<Usuario> resultList = query.getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;

    }
}
