/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.posgrados.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author debian
 */
@Entity
@Table(name = "coordinador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Coordinador.findAll", query = "SELECT c FROM Coordinador c")
    , @NamedQuery(name = "Coordinador.findByIdPosgrado", query = "SELECT c FROM Coordinador c WHERE c.idPosgrado = :idPosgrado")})
public class Coordinador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_posgrado", nullable = false)
    private Integer idPosgrado;
    @JoinColumn(name = "id_posgrado", referencedColumnName = "id_posgrado", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Postgrado postgrado;
    @JoinColumn(name = "usu_id", referencedColumnName = "usu_id", nullable = false)
    @ManyToOne(optional = false)
    private Usuario usuId;

    public Coordinador() {
    }

    public Coordinador(Integer idPosgrado) {
        this.idPosgrado = idPosgrado;
    }

    public Integer getIdPosgrado() {
        return idPosgrado;
    }

    public void setIdPosgrado(Integer idPosgrado) {
        this.idPosgrado = idPosgrado;
    }

    public Postgrado getPostgrado() {
        return postgrado;
    }

    public void setPostgrado(Postgrado postgrado) {
        this.postgrado = postgrado;
    }

    public Usuario getUsuId() {
        return usuId;
    }

    public void setUsuId(Usuario usuId) {
        this.usuId = usuId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPosgrado != null ? idPosgrado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Coordinador)) {
            return false;
        }
        Coordinador other = (Coordinador) object;
        if ((this.idPosgrado == null && other.idPosgrado != null) || (this.idPosgrado != null && !this.idPosgrado.equals(other.idPosgrado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Coordinador[ idPosgrado=" + idPosgrado + " ]";
    }
    
}
