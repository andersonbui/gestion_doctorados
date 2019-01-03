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
@Table(name = "revista")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Revista.findAll", query = "SELECT r FROM Revista r")
    , @NamedQuery(name = "Revista.findByDocIdentificador", query = "SELECT r FROM Revista r WHERE r.docIdentificador = :docIdentificador")
    , @NamedQuery(name = "Revista.findByRevNombreRevista", query = "SELECT r FROM Revista r WHERE r.revNombreRevista = :revNombreRevista")
    , @NamedQuery(name = "Revista.findByRevTituloArticulo", query = "SELECT r FROM Revista r WHERE r.revTituloArticulo = :revTituloArticulo")
    , @NamedQuery(name = "Revista.findByRevCategoria", query = "SELECT r FROM Revista r WHERE r.revCategoria = :revCategoria")
    , @NamedQuery(name = "Revista.findByRevDoi", query = "SELECT r FROM Revista r WHERE r.revDoi = :revDoi")})
public class Revista implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "doc_identificador", nullable = false)
    private Integer docIdentificador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "rev_nombre_revista", nullable = false, length = 80)
    private String revNombreRevista;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "rev_titulo_articulo", nullable = false, length = 200)
    private String revTituloArticulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "rev_categoria", nullable = false, length = 2)
    private String revCategoria;
    @Size(max = 30)
    @Column(name = "rev_doi", length = 30)
    private String revDoi;
    @JoinColumn(name = "doc_identificador", referencedColumnName = "doc_identificador", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Documentacion documentacion;

    public Revista() {
    }

    public Revista(Integer docIdentificador) {
        this.docIdentificador = docIdentificador;
    }

    public Revista(Integer docIdentificador, String revNombreRevista, String revTituloArticulo, String revCategoria) {
        this.docIdentificador = docIdentificador;
        this.revNombreRevista = revNombreRevista;
        this.revTituloArticulo = revTituloArticulo;
        this.revCategoria = revCategoria;
    }

    public Revista(String revNombreRevista, String revTituloArticulo, String revCategoria, String revDoi) {
        this.revNombreRevista = revNombreRevista;
        this.revTituloArticulo = revTituloArticulo;
        this.revCategoria = revCategoria;
        this.revDoi = revDoi;
    }

    public Integer getDocIdentificador() {
        return docIdentificador;
    }

    public void setDocIdentificador(Integer docIdentificador) {
        this.docIdentificador = docIdentificador;
    }

    public String getRevNombreRevista() {
        return revNombreRevista;
    }

    public void setRevNombreRevista(String revNombreRevista) {
        this.revNombreRevista = revNombreRevista;
    }

    public String getRevTituloArticulo() {
        return revTituloArticulo;
    }

    public void setRevTituloArticulo(String revTituloArticulo) {
        this.revTituloArticulo = revTituloArticulo;
    }

    public String getRevCategoria() {
        return revCategoria;
    }

    public void setRevCategoria(String revCategoria) {
        this.revCategoria = revCategoria;
    }

    public String getRevDoi() {
        return revDoi;
    }

    public void setRevDoi(String revDoi) {
        this.revDoi = revDoi;
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
        if (!(object instanceof Revista)) {
            return false;
        }
        Revista other = (Revista) object;
        if ((this.docIdentificador == null && other.docIdentificador != null) || (this.docIdentificador != null && !this.docIdentificador.equals(other.docIdentificador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Revista[ docIdentificador=" + docIdentificador + " ]";
    }
    
}
