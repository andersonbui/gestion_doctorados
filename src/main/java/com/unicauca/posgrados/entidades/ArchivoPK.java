/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.posgrados.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author debian
 */
@Embeddable
public class ArchivoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "arc_tipoPDF_cargar", nullable = false, length = 40)
    private String arctipoPDFcargar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "doc_identificador", nullable = false)
    private int docIdentificador;

    public ArchivoPK() {
    }

    public ArchivoPK(String arctipoPDFcargar, int docIdentificador) {
        this.arctipoPDFcargar = arctipoPDFcargar;
        this.docIdentificador = docIdentificador;
    }

    public String getArctipoPDFcargar() {
        return arctipoPDFcargar;
    }

    public void setArctipoPDFcargar(String arctipoPDFcargar) {
        this.arctipoPDFcargar = arctipoPDFcargar;
    }

    public int getDocIdentificador() {
        return docIdentificador;
    }

    public void setDocIdentificador(int docIdentificador) {
        this.docIdentificador = docIdentificador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (arctipoPDFcargar != null ? arctipoPDFcargar.hashCode() : 0);
        hash += (int) docIdentificador;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ArchivoPK)) {
            return false;
        }
        ArchivoPK other = (ArchivoPK) object;
        if ((this.arctipoPDFcargar == null && other.arctipoPDFcargar != null) || (this.arctipoPDFcargar != null && !this.arctipoPDFcargar.equals(other.arctipoPDFcargar))) {
            return false;
        }
        if (this.docIdentificador != other.docIdentificador) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.unicauca.proyectobase.entidades.ArchivoPK[ arctipoPDFcargar=" + arctipoPDFcargar + ", docIdentificador=" + docIdentificador + " ]";
    }
    
}
