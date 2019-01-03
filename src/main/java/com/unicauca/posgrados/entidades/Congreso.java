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
@Table(name = "congreso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Congreso.findAll", query = "SELECT c FROM Congreso c")
    , @NamedQuery(name = "Congreso.findByDocIdentificador", query = "SELECT c FROM Congreso c WHERE c.docIdentificador = :docIdentificador")
    , @NamedQuery(name = "Congreso.findByCongTituloPonencia", query = "SELECT c FROM Congreso c WHERE c.congTituloPonencia = :congTituloPonencia")
    , @NamedQuery(name = "Congreso.findByCongNombreEvento", query = "SELECT c FROM Congreso c WHERE c.congNombreEvento = :congNombreEvento")
    , @NamedQuery(name = "Congreso.findByCongTipoCongreso", query = "SELECT c FROM Congreso c WHERE c.congTipoCongreso = :congTipoCongreso")
    , @NamedQuery(name = "Congreso.findByCongDoi", query = "SELECT c FROM Congreso c WHERE c.congDoi = :congDoi")
    , @NamedQuery(name = "Congreso.findByCongIssn", query = "SELECT c FROM Congreso c WHERE c.congIssn = :congIssn")})
public class Congreso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "doc_identificador", nullable = false)
    private Integer docIdentificador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "cong_titulo_ponencia", nullable = false, length = 200)
    private String congTituloPonencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "cong_nombre_evento", nullable = false, length = 100)
    private String congNombreEvento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "cong_tipo_congreso", nullable = false, length = 30)
    private String congTipoCongreso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "cong_doi", nullable = false, length = 30)
    private String congDoi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "cong_issn", nullable = false, length = 30)
    private String congIssn;
    @JoinColumn(name = "doc_identificador", referencedColumnName = "doc_identificador", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Documentacion documentacion;

    public Congreso() {
    }

    public Congreso(Integer docIdentificador) {
        this.docIdentificador = docIdentificador;
    }

    public Congreso(Integer docIdentificador, String congTituloPonencia, String congNombreEvento, String congTipoCongreso, String congDoi, String congIssn) {
        this.docIdentificador = docIdentificador;
        this.congTituloPonencia = congTituloPonencia;
        this.congNombreEvento = congNombreEvento;
        this.congTipoCongreso = congTipoCongreso;
        this.congDoi = congDoi;
        this.congIssn = congIssn;
    }

    public Integer getDocIdentificador() {
        return docIdentificador;
    }

    public void setDocIdentificador(Integer docIdentificador) {
        this.docIdentificador = docIdentificador;
    }

    public String getCongTituloPonencia() {
        return congTituloPonencia;
    }

    public void setCongTituloPonencia(String congTituloPonencia) {
        this.congTituloPonencia = congTituloPonencia;
    }

    public String getCongNombreEvento() {
        return congNombreEvento;
    }

    public void setCongNombreEvento(String congNombreEvento) {
        this.congNombreEvento = congNombreEvento;
    }

    public String getCongTipoCongreso() {
        return congTipoCongreso;
    }

    public void setCongTipoCongreso(String congTipoCongreso) {
        this.congTipoCongreso = congTipoCongreso;
    }

    public String getCongDoi() {
        return congDoi;
    }

    public void setCongDoi(String congDoi) {
        this.congDoi = congDoi;
    }

    public String getCongIssn() {
        return congIssn;
    }

    public void setCongIssn(String congIssn) {
        this.congIssn = congIssn;
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
        if (!(object instanceof Congreso)) {
            return false;
        }
        Congreso other = (Congreso) object;
        if ((this.docIdentificador == null && other.docIdentificador != null) || (this.docIdentificador != null && !this.docIdentificador.equals(other.docIdentificador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Congreso[ docIdentificador=" + docIdentificador + " ]";
    }
    
}
