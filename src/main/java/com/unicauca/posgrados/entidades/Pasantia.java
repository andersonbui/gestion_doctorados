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
@Table(name = "pasantia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pasantia.findAll", query = "SELECT p FROM Pasantia p")
    , @NamedQuery(name = "Pasantia.findByFechaInicio", query = "SELECT p FROM Pasantia p WHERE p.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Pasantia.findByFechaTerminacion", query = "SELECT p FROM Pasantia p WHERE p.fechaTerminacion = :fechaTerminacion")
    , @NamedQuery(name = "Pasantia.findByLugar", query = "SELECT p FROM Pasantia p WHERE p.lugar = :lugar")
    , @NamedQuery(name = "Pasantia.findByDocumentacionDocIdentificador", query = "SELECT p FROM Pasantia p WHERE p.documentacionDocIdentificador = :documentacionDocIdentificador")})
public class Pasantia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 45)
    @Column(name = "fecha_inicio", length = 45)
    private String fechaInicio;
    @Size(max = 45)
    @Column(name = "fecha_terminacion", length = 45)
    private String fechaTerminacion;
    @Size(max = 45)
    @Column(name = "lugar", length = 45)
    private String lugar;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "documentacion_doc_identificador", nullable = false)
    private Integer documentacionDocIdentificador;
    @JoinColumn(name = "documentacion_doc_identificador", referencedColumnName = "doc_identificador", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Documentacion documentacion;

    public Pasantia() {
    }

    public Pasantia(Integer documentacionDocIdentificador) {
        this.documentacionDocIdentificador = documentacionDocIdentificador;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaTerminacion() {
        return fechaTerminacion;
    }

    public void setFechaTerminacion(String fechaTerminacion) {
        this.fechaTerminacion = fechaTerminacion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Integer getDocumentacionDocIdentificador() {
        return documentacionDocIdentificador;
    }

    public void setDocumentacionDocIdentificador(Integer documentacionDocIdentificador) {
        this.documentacionDocIdentificador = documentacionDocIdentificador;
    }

    public Documentacion getDocumentacion() {
        return documentacion;
    }

    public void setDocumentacion(Documentacion documentacion) {
        this.documentacion = documentacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentacionDocIdentificador != null ? documentacionDocIdentificador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pasantia)) {
            return false;
        }
        Pasantia other = (Pasantia) object;
        if ((this.documentacionDocIdentificador == null && other.documentacionDocIdentificador != null) || (this.documentacionDocIdentificador != null && !this.documentacionDocIdentificador.equals(other.documentacionDocIdentificador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Pasantia[ documentacionDocIdentificador=" + documentacionDocIdentificador + " ]";
    }
    
}
