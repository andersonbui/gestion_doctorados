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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author debian
 */
@Entity
@Table(name = "grupo_tipo_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GrupoTipoUsuario.findAll", query = "SELECT g FROM GrupoTipoUsuario g")
    , @NamedQuery(name = "GrupoTipoUsuario.findByNombreUsuario", query = "SELECT g FROM GrupoTipoUsuario g WHERE g.nombreUsuario = :nombreUsuario")
    , @NamedQuery(name = "GrupoTipoUsuario.findByUsuId", query = "SELECT g FROM GrupoTipoUsuario g WHERE g.usuId = :usuId")})
public class GrupoTipoUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre_usuario", nullable = false, length = 45)
    private String nombreUsuario;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "usu_id", nullable = false)
    private Integer usuId;
    @JoinColumn(name = "id_tipo", referencedColumnName = "id_tipo", nullable = false)
    @ManyToOne(optional = false)
    private TipoUsuario idTipo;
    @JoinColumn(name = "usu_id", referencedColumnName = "usu_id", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Usuario usuario;

    public GrupoTipoUsuario() {
    }

    public GrupoTipoUsuario(Integer usuId) {
        this.usuId = usuId;
    }

    public GrupoTipoUsuario(Integer usuId, String nombreUsuario) {
        this.usuId = usuId;
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Integer getUsuId() {
        return usuId;
    }

    public void setUsuId(Integer usuId) {
        this.usuId = usuId;
    }

    public TipoUsuario getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(TipoUsuario idTipo) {
        this.idTipo = idTipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuId != null ? usuId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GrupoTipoUsuario)) {
            return false;
        }
        GrupoTipoUsuario other = (GrupoTipoUsuario) object;
        if ((this.usuId == null && other.usuId != null) || (this.usuId != null && !this.usuId.equals(other.usuId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.unicauca.proyectobase.entidades.GrupoTipoUsuario[ usuId=" + usuId + " ]";
    }
    
}
