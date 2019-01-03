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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author debian
 */
@Entity
@Table(name = "tipo_documento", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nombre"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoDocumento.findAll", query = "SELECT t FROM TipoDocumento t")
    , @NamedQuery(name = "TipoDocumento.findByIdentificador", query = "SELECT t FROM TipoDocumento t WHERE t.identificador = :identificador")
    , @NamedQuery(name = "TipoDocumento.findByNombre", query = "SELECT t FROM TipoDocumento t WHERE t.nombre = :nombre")})
public class TipoDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "identificador", nullable = false)
    private Integer identificador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nombre", nullable = false, length = 20)
    private String nombre;
    @OneToMany(mappedBy = "correquisitos")
    private List<TipoDocumento> tipoDocumentoList;
    @JoinColumn(name = "correquisitos", referencedColumnName = "identificador")
    @ManyToOne
    private TipoDocumento correquisitos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoDocumentacion")
    private List<Documentacion> documentacionList;

    public TipoDocumento() {
    }

    public TipoDocumento(Integer identificador) {
        this.identificador = identificador;
    }

    public TipoDocumento(Integer identificador, String nombre) {
        this.identificador = identificador;
        this.nombre = nombre;
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Integer identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<TipoDocumento> getTipoDocumentoList() {
        return tipoDocumentoList;
    }

    public void setTipoDocumentoList(List<TipoDocumento> tipoDocumentoList) {
        this.tipoDocumentoList = tipoDocumentoList;
    }

    public TipoDocumento getCorrequisitos() {
        return correquisitos;
    }

    public void setCorrequisitos(TipoDocumento correquisitos) {
        this.correquisitos = correquisitos;
    }

    @XmlTransient
    public List<Documentacion> getDocumentacionList() {
        return documentacionList;
    }

    public void setDocumentacionList(List<Documentacion> documentacionList) {
        this.documentacionList = documentacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (identificador != null ? identificador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoDocumento)) {
            return false;
        }
        TipoDocumento other = (TipoDocumento) object;
        if ((this.identificador == null && other.identificador != null) || (this.identificador != null && !this.identificador.equals(other.identificador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.unicauca.entidades.TipoDocumento[ identificador=" + identificador + " ]";
    }
    
}
