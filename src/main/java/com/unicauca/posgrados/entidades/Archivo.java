/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.posgrados.entidades;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author debian
 */
@Entity
@Table(name = "archivo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Archivo.findAll", query = "SELECT a FROM Archivo a")
    , @NamedQuery(name = "Archivo.findByArctipoPDFcargar", query = "SELECT a FROM Archivo a WHERE a.archivoPK.arctipoPDFcargar = :arctipoPDFcargar")
    , @NamedQuery(name = "Archivo.findByDocIdentificador", query = "SELECT a FROM Archivo a WHERE a.archivoPK.docIdentificador = :docIdentificador")})
public class Archivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ArchivoPK archivoPK;
    @JoinColumn(name = "doc_identificador", referencedColumnName = "doc_identificador", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Documentacion documentacion;

    public Archivo() {
    }

    public Archivo(ArchivoPK archivoPK) {
        this.archivoPK = archivoPK;
    }

    public Archivo(String arctipoPDFcargar, int docIdentificador) {
        this.archivoPK = new ArchivoPK(arctipoPDFcargar, docIdentificador);
    }

    public ArchivoPK getArchivoPK() {
        return archivoPK;
    }

    public void setArchivoPK(ArchivoPK archivoPK) {
        this.archivoPK = archivoPK;
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
        hash += (archivoPK != null ? archivoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Archivo)) {
            return false;
        }
        Archivo other = (Archivo) object;
        if ((this.archivoPK == null && other.archivoPK != null) || (this.archivoPK != null && !this.archivoPK.equals(other.archivoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.unicauca.proyectobase.entidades.Archivo[ archivoPK=" + archivoPK + " ]";
    }
    
}
