/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.posgrados.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author debian
 */
@Entity
@Table(name = "tipo_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoUsuario.findAll", query = "SELECT t FROM TipoUsuario t")
    , @NamedQuery(name = "TipoUsuario.findByDescripcion", query = "SELECT t FROM TipoUsuario t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "TipoUsuario.findByIdTipo", query = "SELECT t FROM TipoUsuario t WHERE t.idTipo = :idTipo")})
public class TipoUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 100)
    @Column(name = "descripcion", length = 100)
    private String descripcion;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "id_tipo", nullable = false, length = 30)
    private String idTipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipo")
    private List<GrupoTipoUsuario> grupoTipoUsuarioList;

    public TipoUsuario() {
    }

    public TipoUsuario(String idTipo) {
        this.idTipo = idTipo;
    }

    public TipoUsuario(String idTipo, String descripcion) {
        this.idTipo = idTipo;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(String idTipo) {
        this.idTipo = idTipo;
    }

    @XmlTransient
    public List<GrupoTipoUsuario> getGrupoTipoUsuarioList() {
        return grupoTipoUsuarioList;
    }

    public void setGrupoTipoUsuarioList(List<GrupoTipoUsuario> grupoTipoUsuarioList) {
        this.grupoTipoUsuarioList = grupoTipoUsuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipo != null ? idTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoUsuario)) {
            return false;
        }
        TipoUsuario other = (TipoUsuario) object;
        if ((this.idTipo == null && other.idTipo != null) || (this.idTipo != null && !this.idTipo.equals(other.idTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entidades.TipoUsuario[ idTipo=" + idTipo + " ]";
    }
    
}
