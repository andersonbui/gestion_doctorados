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
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author debian
 */
@Entity
@Table(name = "capitulo_libro", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"caplib_isbn"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CapituloLibro.findAll", query = "SELECT c FROM CapituloLibro c")
    , @NamedQuery(name = "CapituloLibro.findByDocIdentificador", query = "SELECT c FROM CapituloLibro c WHERE c.docIdentificador = :docIdentificador")
    , @NamedQuery(name = "CapituloLibro.findByCaplibTituloLibro", query = "SELECT c FROM CapituloLibro c WHERE c.caplibTituloLibro = :caplibTituloLibro")
    , @NamedQuery(name = "CapituloLibro.findByCaplibTituloCapitulo", query = "SELECT c FROM CapituloLibro c WHERE c.caplibTituloCapitulo = :caplibTituloCapitulo")
    , @NamedQuery(name = "CapituloLibro.findByCaplibIsbn", query = "SELECT c FROM CapituloLibro c WHERE c.caplibIsbn = :caplibIsbn")})
public class CapituloLibro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "doc_identificador", nullable = false)
    private Integer docIdentificador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "caplib_titulo_libro", nullable = false, length = 200)
    private String caplibTituloLibro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "caplib_titulo_capitulo", nullable = false, length = 80)
    private String caplibTituloCapitulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "caplib_isbn", nullable = false, length = 30)
    private String caplibIsbn;
    @JoinColumn(name = "doc_identificador", referencedColumnName = "doc_identificador", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Documentacion documentacion;

    public CapituloLibro() {
    }

    public CapituloLibro(Integer docIdentificador) {
        this.docIdentificador = docIdentificador;
    }

    public CapituloLibro(Integer docIdentificador, String caplibTituloLibro, String caplibTituloCapitulo, String caplibIsbn) {
        this.docIdentificador = docIdentificador;
        this.caplibTituloLibro = caplibTituloLibro;
        this.caplibTituloCapitulo = caplibTituloCapitulo;
        this.caplibIsbn = caplibIsbn;
    }

    public Integer getDocIdentificador() {
        return docIdentificador;
    }

    public void setDocIdentificador(Integer docIdentificador) {
        this.docIdentificador = docIdentificador;
    }

    public String getCaplibTituloLibro() {
        return caplibTituloLibro;
    }

    public void setCaplibTituloLibro(String caplibTituloLibro) {
        this.caplibTituloLibro = caplibTituloLibro;
    }

    public String getCaplibTituloCapitulo() {
        return caplibTituloCapitulo;
    }

    public void setCaplibTituloCapitulo(String caplibTituloCapitulo) {
        this.caplibTituloCapitulo = caplibTituloCapitulo;
    }

    public String getCaplibIsbn() {
        return caplibIsbn;
    }

    public void setCaplibIsbn(String caplibIsbn) {
        this.caplibIsbn = caplibIsbn;
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
        hash += (docIdentificador != null ? docIdentificador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CapituloLibro)) {
            return false;
        }
        CapituloLibro other = (CapituloLibro) object;
        if ((this.docIdentificador == null && other.docIdentificador != null) || (this.docIdentificador != null && !this.docIdentificador.equals(other.docIdentificador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.unicauca.proyectobase.entidades.CapituloLibro[ docIdentificador=" + docIdentificador + " ]";
    }
    
}
