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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author debian
 */
@Entity
@Table(name = "libro", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"lib_isbn"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Libro.findAll", query = "SELECT l FROM Libro l")
    , @NamedQuery(name = "Libro.findByDocIdentificador", query = "SELECT l FROM Libro l WHERE l.docIdentificador = :docIdentificador")
    , @NamedQuery(name = "Libro.findByLibTituloLibro", query = "SELECT l FROM Libro l WHERE l.libTituloLibro = :libTituloLibro")
    , @NamedQuery(name = "Libro.findByLibIsbn", query = "SELECT l FROM Libro l WHERE l.libIsbn = :libIsbn")})
public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "doc_identificador", nullable = false)
    private Integer docIdentificador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "lib_titulo_libro", nullable = false, length = 200)
    private String libTituloLibro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "lib_isbn", nullable = false, length = 30)
    private String libIsbn;
    @JoinColumn(name = "doc_identificador", referencedColumnName = "doc_identificador", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Documentacion documentacion;

    public Libro() {
    }

    public Libro(Integer docIdentificador) {
        this.docIdentificador = docIdentificador;
    }

    public Libro(Integer docIdentificador, String libTituloLibro, String libIsbn) {
        this.docIdentificador = docIdentificador;
        this.libTituloLibro = libTituloLibro;
        this.libIsbn = libIsbn;
    }

    public Integer getDocIdentificador() {
        return docIdentificador;
    }

    public void setDocIdentificador(Integer docIdentificador) {
        this.docIdentificador = docIdentificador;
    }

    public String getLibTituloLibro() {
        return libTituloLibro;
    }

    public void setLibTituloLibro(String libTituloLibro) {
        this.libTituloLibro = libTituloLibro;
    }

    public String getLibIsbn() {
        return libIsbn;
    }

    public void setLibIsbn(String libIsbn) {
        this.libIsbn = libIsbn;
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
        if (!(object instanceof Libro)) {
            return false;
        }
        Libro other = (Libro) object;
        if ((this.docIdentificador == null && other.docIdentificador != null) || (this.docIdentificador != null && !this.docIdentificador.equals(other.docIdentificador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Libro[ docIdentificador=" + docIdentificador + " ]";
    }
    
}
