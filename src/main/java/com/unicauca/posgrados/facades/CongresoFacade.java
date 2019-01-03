/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.posgrados.facades;

import com.unicauca.posgrados.entidades.Congreso;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author debian
 */
@Stateless
public class CongresoFacade extends AbstractFacade<Congreso> {

    @PersistenceContext(unitName = "PosgradosPC")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CongresoFacade() {
        super(Congreso.class);
    }
    
}